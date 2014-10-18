package com.pataniqa.coursera.potlatch.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.remote.RemoteService;
import com.pataniqa.coursera.potlatch.store.remote.unsafe.UnsafeHttpClient;
import com.pataniqa.coursera.potlatch.ui.CreateGiftActivity;
import com.pataniqa.coursera.potlatch.ui.ListGiftsActivity;

public class UploadService extends IntentService {
    
    public static final String UPLOAD_RESPONSE =  ListGiftsActivity.class.getCanonicalName()  
            + ".FILE_UPLOADED";

    private static final String LOG_TAG = UploadService.class.getName();
    private static final String SERVICE_NAME = UploadService.class.getCanonicalName();
    
    public static final String IS_IMAGE_TAG = "is_image";
    public static final String FILE_TAG = "file";
    public static final String ENDPOINT_TAG = "endpoint";
    public static final String CLIENT_TAG = "client";
    public final static String UPLOAD_HANDLER_TAG="handler";

    public UploadService() {
        super(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");
        final long id = intent.getLongExtra(CreateGiftActivity.GIFT_ID_TAG, 0);
        final boolean isImage = intent.getBooleanExtra(IS_IMAGE_TAG, true);
        final String path = intent.getStringExtra(FILE_TAG);
        final String username = intent.getStringExtra(CreateGiftActivity.USER_NAME_TAG);
        final String password = intent.getStringExtra(CreateGiftActivity.PASSWORD_TAG);
        final String endpoint = intent.getStringExtra(ENDPOINT_TAG);
        final String client = intent.getStringExtra(CLIENT_TAG);

        final Gifts gifts = new RemoteService(new UnsafeHttpClient(),
                endpoint,
                username,
                password,
                client).gifts();
        final File file = new File(path);
        final Context context = this;
        final File outputDir = context.getCacheDir();
        try {
            final File outputFile = File.createTempFile("potlatch", ".png", outputDir);
            Log.d(LOG_TAG, "Creating temporary file to store compressed image " + outputFile);
            Observable<Boolean> result;
            if (isImage) {

                // resize the image and correct the orientation

                result = Observable.create(new Observable.OnSubscribe<TypedFile>() {
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
                        Log.d(LOG_TAG, "Uploading image " + path);
                        subscriber.onNext(imageData);
                        subscriber.onCompleted();
                    }
                }).flatMap(new Func1<TypedFile, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(TypedFile imageData) {
                        return gifts.setImageData(id, imageData);
                    }
                });

            } else {
                TypedFile videoData = new TypedFile("video/mp4", file);
                result = gifts.setVideoData(id, videoData);
            }
            result/*.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())*/
                    .forEach(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean arg0) {
                            FileUtils.deleteQuietly(outputFile);
                            Log.d(LOG_TAG, "Uploaded  " + file.getAbsolutePath());
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction(UPLOAD_RESPONSE);
                            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                            sendBroadcast(broadcastIntent);
                        }
                    });
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }
}
