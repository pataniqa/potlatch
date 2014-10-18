package com.pataniqa.coursera.potlatch.store.local;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.TimeUtils;
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.ui.EditGiftActivity;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 */
class LocalGiftStore extends BaseCreateUpdateDelete<Gift> implements LocalSaveDelete<Gift> {
    
    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    LocalGiftStore(LocalDatabase helper, LocalCRUD<User> users, LocalCRUD<GiftChain> chains) {
        super(new GiftCreator(users, chains),
                LocalSchema.Gift.TABLE_NAME,
                helper);
    }

    private static class GiftCreator extends BaseCreator<Gift> implements Creator<Gift> {
        
        private final LocalCRUD<User> users;
        private final LocalCRUD<GiftChain> chains;

        GiftCreator(LocalCRUD<User> users, LocalCRUD<GiftChain>chains) {
            this.users = users;
            this.chains = chains;
        }

        @Override
        public ContentValues getCV(Gift data) {
            ContentValues rValue = new ContentValues();
            try {
            rValue.put(LocalSchema.Cols.TITLE, data.getTitle());
            rValue.put(LocalSchema.Cols.DESCRIPTION, data.getDescription());
            rValue.put(LocalSchema.Cols.VIDEO_URI, data.getVideoUri());
            rValue.put(LocalSchema.Cols.IMAGE_URI, data.getImageUri());
            rValue.put(LocalSchema.Cols.CREATED, TimeUtils.toLong(data.getCreated()));
            rValue.put(LocalSchema.Cols.USER_ID, data.getUserID());
            rValue.put(LocalSchema.Cols.LIKE, false);
            rValue.put(LocalSchema.Cols.FLAG, false);
            rValue.put(LocalSchema.Cols.LIKES, 0);
            rValue.put(LocalSchema.Cols.FLAGGED, false);
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_ID, data.getGiftChainID());
            String chain = chains.findOne(data.getGiftChainID()).getName();
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, chain);
            rValue.put(LocalSchema.Cols.USER_LIKES, 0);
            String user = users.findOne(data.getUserID()).getName();
            rValue.put(LocalSchema.Cols.USER_NAME, user);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return rValue;
        }

        @Override
        public Gift getFromCursor(Cursor cursor) {
            long rowID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
            String title = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.TITLE));
            String description = cursor.getString(cursor
                    .getColumnIndex(LocalSchema.Cols.DESCRIPTION));
            String videoUri = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.VIDEO_URI));
            String imageUri = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.IMAGE_URI));
            Date created = TimeUtils.toDate(cursor.getLong(cursor
                    .getColumnIndex(LocalSchema.Cols.CREATED)));
            long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_ID));
            long giftChainID = cursor
                    .getLong(cursor.getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_ID));

            return new Gift(rowID,
                    title,
                    description,
                    imageUri,
                    videoUri,
                    created,
                    userID,
                    giftChainID);
        }
    }

}