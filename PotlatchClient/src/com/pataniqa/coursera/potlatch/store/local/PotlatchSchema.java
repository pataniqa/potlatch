package com.pataniqa.coursera.potlatch.store.local;

import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.provider.BaseColumns;

public class PotlatchSchema {

    public static final String ORGANIZATIONAL_NAME = "org.pataniqa";
    public static final String PROJECT_NAME = ORGANIZATIONAL_NAME + "potlatch";
    public static final String INTEGER = "integer";
    public static final String TEXT = "text";
    public static final String PARENT_DATABASE = "PotlatchSecurityDatabase";

    public static class Gift {
        public static final String PATH = "gift";

        public static final String TABLE_NAME = PROJECT_NAME + "." + PATH;

        public static Map<String, String> COLUMNS = new TreeMap<String, String>() {
            {
                put(Cols.ID, INTEGER);
                put(Cols.TITLE, TEXT);
                put(Cols.DESCRIPTION, TEXT);
                put(Cols.VIDEO_URI, TEXT);
                put(Cols.IMAGE_URI, TEXT);
                put(Cols.CREATED, TEXT);
                put(Cols.USER_ID, INTEGER);
                put(Cols.LIKE, INTEGER);
                put(Cols.FLAG, INTEGER);
                put(Cols.LIKES, INTEGER);
                put(Cols.FLAGGED, INTEGER);
                put(Cols.GIFT_CHAIN_ID, INTEGER);
                put(Cols.GIFT_CHAIN_NAME, TEXT);
            }
        };

        public static ContentValues initializeWithDefault(final ContentValues assignedValues) {
            return SQLiteUtils.initializeWithDefault(COLUMNS, assignedValues);
        }

        // a static class to store columns in entity
        public static class Cols {
            public static final String ID = BaseColumns._ID;
            public static final String TITLE = "TITLE";
            public static final String DESCRIPTION = "DESCRIPTION";
            public static final String VIDEO_URI = "VIDEO_URI";
            public static final String IMAGE_URI = "IMAGE_URI";
            public static final String CREATED = "CREATED";
            public static final String USER_ID = "USER_ID";
            public static final String LIKE = "LIKE";
            public static final String FLAG = "FLAG";
            public static final String LIKES = "LIKES";
            public static final String FLAGGED = "FLAGGED";
            public static final String GIFT_CHAIN_ID = "GIFT_CHAIN_ID";
            public static final String GIFT_CHAIN_NAME = "GIFT_CHAIN_NAME";
        }
    }
}
