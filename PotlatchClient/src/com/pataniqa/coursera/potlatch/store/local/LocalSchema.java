package com.pataniqa.coursera.potlatch.store.local;

import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.provider.BaseColumns;

class LocalSchema {

     static final String ORGANIZATIONAL_NAME = "org.pataniqa";
     static final String PROJECT_NAME = ORGANIZATIONAL_NAME + "potlatch";
     static final String INTEGER = "integer";
     static final String TEXT = "text";
     static final String PARENT_DATABASE = "PotlatchDatabase";

     static class Gift {
         static final String TABLE_NAME = "gifts";

         static Map<String, String> COLUMNS = new TreeMap<String, String>() {
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
                put(Cols.USER_LIKES, INTEGER);
                put(Cols.USER_NAME, TEXT);
            }
        };

         static ContentValues initializeWithDefault(final ContentValues assignedValues) {
            return initialize(COLUMNS, assignedValues);
        }

    }
    
     static class GiftChain {
         static final String TABLE_NAME = "giftchains";

         static Map<String, String> COLUMNS = new TreeMap<String, String>() {
            {
                put(Cols.ID, INTEGER);
                put(Cols.GIFT_CHAIN_NAME, TEXT);
            }
        };

         static ContentValues initializeWithDefault(final ContentValues assignedValues) {
            return initialize(COLUMNS, assignedValues);
        }

    }
    
     static class GiftMetadata {
         static final String TABLE_NAME = "giftmetadata";
    }
    
    // a static class to store columns in entity
     static class Cols {
         static final String ID = BaseColumns._ID;
         static final String TITLE = "TITLE";
         static final String DESCRIPTION = "DESCRIPTION";
         static final String VIDEO_URI = "VIDEO_URI";
         static final String IMAGE_URI = "IMAGE_URI";
         static final String CREATED = "CREATED";
         static final String USER_ID = "USER_ID";
         static final String LIKE = "ULIKE";
         static final String FLAG = "UFLAG";
         static final String LIKES = "LIKES";
         static final String FLAGGED = "FLAGGED";
         static final String GIFT_CHAIN_NAME = "GIFT_CHAIN_NAME";
         static final String USER_LIKES = "USER_LIKES";
         static final String GIFT_ID = "GIFT_ID";
         static final String USER_NAME = "USERNAME";
         static final String GIFT_CHAIN_ID = "GIFT_CHAIN_ID";
    }
    
    static ContentValues initialize(Map<String, String> columns,
            final ContentValues assignedValues) {
        final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                : assignedValues;
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            String key = entry.getKey();
            if (!key.equals(BaseColumns._ID)) {
                if (!setValues.containsKey(key)) {
                    if (entry.getValue().equals(LocalSchema.INTEGER))
                        setValues.put(key, 0);
                    else
                        setValues.put(key, "");
                }
            }
        }
        return setValues;
    }
}
