package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class ImageUtils {
    private final static String LOG_TAG = ImageUtils.class.getCanonicalName();

    /**
     * Find out the correction orientation of the image.
     */
    public static float getPhotoOrientation(Context context, File imageFile) {
        float rotate = 0;
        try {
            //File imageFile = new File( Uri.parse(imageUri).getPath());
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

            Log.i(LOG_TAG, "Exif orientation: " + orientation);
            Log.i(LOG_TAG, "Rotate value: " + rotate);

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return rotate;
    }
}
