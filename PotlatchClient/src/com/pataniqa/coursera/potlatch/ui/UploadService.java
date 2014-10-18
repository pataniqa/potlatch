package com.pataniqa.coursera.potlatch.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.remote.RemoteService;
import com.pataniqa.coursera.potlatch.store.remote.unsafe.UnsafeHttpClient;

public class UploadService extends IntentService {

    public static final String UPLOAD_RESPONSE = ListGiftsActivity.class.getCanonicalName()
            + ".FILE_UPLOADED";

    private static final String LOG_TAG = UploadService.class.getName();
    private static final String SERVICE_NAME = UploadService.class.getCanonicalName();

    public static final String IS_IMAGE_TAG = "is_image";
    public static final String FILE_TAG = "file";
    public static final String ENDPOINT_TAG = "endpoint";
    public static final String CLIENT_TAG = "client";
    public final static String UPLOAD_HANDLER_TAG = "handler";

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

                            compressImageFile(path,
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
            result/*
                   * .subscribeOn(Schedulers.newThread()).observeOn(
                   * AndroidSchedulers.mainThread())
                   */
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

    static void compressImageFile(String path,
            int imageWidth,
            int imageHeight,
            int imageQuality,
            File outputFile) throws IOException {
        Bitmap resizedBitmap = fileToBitmap(path, imageWidth, imageHeight);
        final ByteArrayOutputStream bao = new ByteArrayOutputStream();
        resizedBitmap.compress(CompressFormat.PNG, imageQuality, bao);
        FileUtils.writeByteArrayToFile(outputFile, bao.toByteArray());
    }

    /**
     * Convert the file to a bitmap with the correct size and orientation. This
     * is based on sample code for <a href=
     * "http://developer.android.com/training/displaying-bitmaps/load-bitmap.html"
     * >loading large bitmaps</a>.
     */
    static Bitmap fileToBitmap(String path, int width, int height) {
        File imageFile = new File(path);
        if (imageFile != null && imageFile.exists()) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            float rotation = getPhotoOrientation(new File(path));

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            return Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    matrix,
                    true);
        } else {
            Log.e(LOG_TAG, "Failed to find image.");
        }
        return null;
    }

    /**
     * Find out the correct orientation of the image. See <a href=
     * "http://stackoverflow.com/questions/12726860/android-how-to-detect-the-image-orientation-portrait-or-landscape-picked-fro"
     * >Stack Overflow</a>
     */
    static float getPhotoOrientation(File imageFile) {
        float rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            }

            Log.d(LOG_TAG, "Exif orientation: " + orientation);
            Log.d(LOG_TAG, "Rotate value: " + rotate);

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return rotate;
    }

  
    
    /**
     * Sample down the image to get the right size. This
     * is based on sample code for <a href=
     * "http://developer.android.com/training/displaying-bitmaps/load-bitmap.html"
     * >loading large bitmaps</a>.
     */
    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
