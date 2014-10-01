package com.pataniqa.coursera.potlatch.store.local;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * This utility class provides several options for storing temporary and
 * permanent files on the file system with varying degrees of security.
 */
public class LocalStorageUtilities {

    private final static String LOG_TAG = LocalStorageUtilities.class.getCanonicalName();

    public enum Security {PUBLIC, PRIVATE};
    
    // Constant that denotes what media type a file should be stored as.
    public enum MediaType {IMAGE, VIDEO, TEXT};

    public final static Map<Integer, String> MEDIA_TYPES = new TreeMap<Integer, String>() {
        {
            put(MediaType.IMAGE.ordinal(), "IMG_");
            put(MediaType.VIDEO.ordinal(), "VID_");
            put(MediaType.TEXT.ordinal(), "TXT_");
        }
    };

    /**
     * Creates an output file to store some kind of media (images, audio, text).
     * 
     * The directory the file is created in depends both on the media type and
     * the security level. If the security is private, we store it in
     * app-specific private memory. If it is public, we store the file on
     * external storage. Android has different directories in external storage
     * for each media type, so we choose the directory depending on the media
     * type parameter.
     * 
     * If the provided filename is null, we generate a filename based on the
     * current time and media type.
     * 
     * @param context The context of the calling component
     * @param type The media type that's being stored (determines file location
     *            and name if not specified)
     * @param security How securely we should store the temporary files. We can
     *            store it on the SD card or in private app memory.
     * @param name The name of the file to be created. If null, we generate a
     *            name based on the current time and media type.
     * @return A File reference to a newly created temporary file
     */
    public static File getOutputMediaFile(Context context, MediaType type, Security security, String name) {
        Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);

        // Get the current time stamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        // Make sure external storage is mounted.
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(LOG_TAG, "External storage not mounted. Can't write/read file.");
            return null;
        }

        File storageDir = null;
        if (security == Security.PRIVATE) {
            storageDir = context.getFilesDir();
        } else {
            switch (type) {
            case IMAGE:
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                break;
            case VIDEO:
                storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                break;
            case TEXT:
                storageDir = context.getExternalFilesDir(null);
                break;
            }
        }

        File outputFile = null;
        if (storageDir != null) {
            if (name != null) {
                outputFile = new File(storageDir.getPath() + File.separator + name);
            } else if (MEDIA_TYPES.containsKey(type)) {
                outputFile = new File(storageDir.getPath() + File.separator + MEDIA_TYPES.get(type)
                        + timeStamp);
            }
        }

        return outputFile;
    }

    /**
     * A convenience function for getting a URI to an output file instead of a
     * File reference.
     * 
     * @param context The context of the calling component
     * @param type The media type that's being stored (determines file name and
     *            location)
     * @param security How securely we should store the temporary files. We can
     *            store it on the SD card or in private app memory.
     * @param name The name of the file to be created (optional)
     * @return A URI to a newly created temporary file
     */
    public static Uri getOutputMediaFileUri(Context context, MediaType type, Security security, String name) {
        File outFile = getOutputMediaFile(context, type, security, name);
        return outFile != null ? Uri.fromFile(outFile) : null;
    }

}
