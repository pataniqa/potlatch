package com.pataniqa.coursera.potlatch.store.local;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.TimeUtils;
import com.pataniqa.coursera.potlatch.store.SaveDelete;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 */
public class LocalGiftStore extends BaseCreateUpdateDelete<Gift> implements SaveDelete<Gift> {

    public LocalGiftStore(LocalDatabase helper) {
        super(new GiftCreator(), LocalSchema.Gift.TABLE_NAME, helper);
    }

    private static class GiftCreator extends BaseCreator<Gift> implements Creator<Gift> {

        @Override
        public ContentValues getCV(Gift data) {
            ContentValues rValue = new ContentValues();
            rValue.put(LocalSchema.Cols.TITLE, data.getTitle());
            rValue.put(LocalSchema.Cols.DESCRIPTION, data.getDescription());
            rValue.put(LocalSchema.Cols.VIDEO_URI, data.getVideoUri());
            rValue.put(LocalSchema.Cols.IMAGE_URI, data.getImageUri());
            rValue.put(LocalSchema.Cols.CREATED, TimeUtils.toLong(data.getCreated()));
            rValue.put(LocalSchema.Cols.USER_ID, data.getUserID());
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_ID, data.getGiftChainID());
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
                    videoUri,
                    imageUri,
                    created,
                    userID,
                    giftChainID);
        }
    }

}