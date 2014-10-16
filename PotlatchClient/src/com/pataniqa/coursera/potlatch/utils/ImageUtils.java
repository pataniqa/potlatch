package com.pataniqa.coursera.potlatch.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class ImageUtils {
    private final static String LOG_TAG = ImageUtils.class.getCanonicalName();

    /**
     * Find out the correction orientation of the image. See
     * http://stackoverflow
     * .com/questions/12726860/android-how-to-detect-the-image
     * -orientation-portrait-or-landscape-picked-fro
     */
    public static float getPhotoOrientation(File imageFile) {
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

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth,
            int reqHeight) {
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

    public static Bitmap fileToBitmap(String path, int width, int height) {
        File imageFile = new File(path);
        if (imageFile != null && imageFile.exists()) {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = ImageUtils.calculateInSampleSize(options, width, height);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            float rotation = ImageUtils.getPhotoOrientation(new File(path));

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            return Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    width,
                    height,
                    matrix,
                    true);
        } else {
            Log.e(LOG_TAG, "Failed to find image.");
        }
        return null;
    }
}
