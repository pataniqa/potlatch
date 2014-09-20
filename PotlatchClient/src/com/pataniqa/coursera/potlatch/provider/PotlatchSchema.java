package com.pataniqa.coursera.potlatch.provider;

import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A schema is defined as an overall method of organization for some set of
 * data.
 * 
 * This class contains many class constants that help organize data storage and
 * database access.
 */
public class PotlatchSchema {

    public static final String ORGANIZATIONAL_NAME = "org.pataniqa";
    public static final String PROJECT_NAME = "potlatch";

    /**
     * ContentProvider Related Constants
     */
    public static final String AUTHORITY = ORGANIZATIONAL_NAME + "." + PROJECT_NAME + ".provider";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    // Define a static class that represents description of stored content
    // entity.
    public static class Gift {
        // define a URI paths to access entity
        // BASE_URI/gift - for list of gifts(s)
        // BASE_URI/gift/* - retrieve specific gift by id
        public static final String PATH = "gift";
        public static final int PATH_TOKEN = 110;

        public static final String PATH_FOR_ID = PATH + "/*";
        public static final int PATH_FOR_ID_TOKEN = 120;

        // URI for all content stored as gift entity
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        private final static String MIME_TYPE_END = PATH;

        // define the MIME type of data in the content provider
        public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME + ".cursor.dir/"
                + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
        public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME + ".cursor.item/"
                + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

        private static final String INTEGER = "integer";
        private static final String TEXT = "text";
        
        private static Map<String, String> columns = new TreeMap<String, String>() {
            {
                put(Cols.ID, INTEGER);
                put(Cols.LOGIN_ID, INTEGER);
                put(Cols.GIFT_ID, INTEGER);
                put(Cols.TITLE, TEXT);
                put(Cols.BODY, TEXT);
                put(Cols.VIDEO_LINK, TEXT);
                put(Cols.IMAGE_LINK, TEXT);
            }
        };

        // the names and order of ALL columns, including internal use ones
        public static final String[] ALL_COLUMN_NAMES = columns.keySet().toArray(
                new String[columns.keySet().size()]);

        // the names and order of ALL column types, including internal use ones
        // (for use with SQLite)
        public static final String[] ALL_COLUMN_TYPES = columns.values().toArray(
                new String[columns.values().size()]);

        public static ContentValues initializeWithDefault(final ContentValues assignedValues) {
            // final Long now = Long.valueOf(System.currentTimeMillis());
            final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                    : assignedValues;
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                String key = entry.getKey(); 
                if (!key.equals(Cols.ID)) {
                    if (!setValues.containsKey(key)) {
                        if (entry.getValue().equals(INTEGER))
                            setValues.put(key, 0);
                        else
                            setValues.put(key, "");
                    }
                }
            }
            return setValues;
        }

        // a static class to store columns in entity
        public static class Cols {
            public static final String ID = BaseColumns._ID; 
            // The name and column index of each column in your database
            public static final String LOGIN_ID = "LOGIN_ID";
            public static final String GIFT_ID = "GIFT_ID";
            public static final String TITLE = "TITLE";
            public static final String BODY = "BODY";
            public static final String VIDEO_LINK = "VIDEO_LINK";
            public static final String IMAGE_LINK = "IMAGE_LINK";
        }
    }

}
