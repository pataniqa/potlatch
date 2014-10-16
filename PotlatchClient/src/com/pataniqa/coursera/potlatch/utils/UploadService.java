package com.pataniqa.coursera.potlatch.utils;

import java.io.File;

import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pataniqa.coursera.potlatch.store.Media;
import com.pataniqa.coursera.potlatch.store.remote.RemoteService;

public class UploadService extends IntentService {

    private static final String LOG_TAG = UploadService.class.getName();
    private static final String SERVICE_NAME = UploadService.class.getName();
    private static final String UPLOAD_ACTION = "potlatch.upload";
    private static final String ID_TAG = "id";
    private static final String IS_IMAGE_TAG = "isImage";
    private static final String FILE_TAG = "file";
    private static final String ENDPOINT_TAG = "endpoint";
    private static final String CLIENT_TAG = "client";
    public final static String USER_NAME_TAG = "user_name";
    public final static String PASSWORD_TAG = "password";

    public UploadService() {
        super(SERVICE_NAME);
    }

    public static void startUpload(Context context,
            long id,
            boolean isImage,
            File file,
            String endpoint,
            String username,
            String password,
            String client) {
        final Intent intent = new Intent(context, UploadService.class);
        intent.setAction(UPLOAD_ACTION);
        intent.putExtra(ID_TAG, id);
        intent.putExtra(IS_IMAGE_TAG, isImage);
        intent.putExtra(FILE_TAG, file.getAbsolutePath());
        intent.putExtra(USER_NAME_TAG, username);
        intent.putExtra(PASSWORD_TAG, password);
        intent.putExtra(ENDPOINT_TAG, endpoint);
        intent.putExtra(CLIENT_TAG, client);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final long id = intent.getLongExtra(ID_TAG, 0);
        final boolean isImage = intent.getBooleanExtra(IS_IMAGE_TAG, true);
        final String path = intent.getStringExtra(FILE_TAG);
        final String username = intent.getStringExtra(USER_NAME_TAG);
        final String password = intent.getStringExtra(PASSWORD_TAG);
        final String endpoint = intent.getStringExtra(ENDPOINT_TAG);
        final String client = intent.getStringExtra(CLIENT_TAG);

        final Media media = new RemoteService(new UnsafeHttpClient(),
                endpoint,
                username,
                password,
                client).media();
        final File file = new File(path);
        final Context context = this;
        final File outputDir = context.getCacheDir();
        try {
            final File outputFile = File.createTempFile("potlatch", "png", outputDir);
            Observable<Boolean> result;
            if (isImage) {
                
                // resize the image and correct the orientation

                Observable<TypedFile> a = Observable
                        .create(new Observable.OnSubscribe<TypedFile>() {
                            @Override
                            public void call(Subscriber<? super TypedFile> subscriber) {
                                try {
                                    int imageWidth = 440;
                                    int imageHeight = 440;
                                    int imageQuality = 80;

                                    ImageUtils.compressImageFile(path,
                                            imageWidth,
                                            imageHeight,
                                            imageQuality,
                                            outputFile);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                                TypedFile imageData = new TypedFile("image/png", outputFile);
                                Log.d(LOG_TAG, "Uploading image");
                                subscriber.onNext(imageData);
                                subscriber.onCompleted();
                            }
                        });

                result = a.flatMap(new Func1<TypedFile, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(TypedFile imageData) {
                        return media.setImageData(id, imageData);
                    }
                });

            } else {
                TypedFile videoData = new TypedFile("video/mp4", file);
                result = media.setVideoData(id, videoData);
            }
            result.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .forEach(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean arg0) {
                            Log.d(LOG_TAG, "Uploaded  " + file.getAbsolutePath());
                        }
                    });
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

}
