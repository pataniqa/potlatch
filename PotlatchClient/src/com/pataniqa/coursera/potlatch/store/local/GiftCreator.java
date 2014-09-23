package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.pataniqa.coursera.potlatch.model.Gift;

public class GiftCreator extends BaseCreator<Gift> implements Creator<Gift> {

    @Override
    public ContentValues getCV(Gift data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PotlatchSchema.Cols.TITLE, data.title);
        rValue.put(PotlatchSchema.Cols.DESCRIPTION, data.description);
        rValue.put(PotlatchSchema.Cols.VIDEO_URI, data.videoUri);
        rValue.put(PotlatchSchema.Cols.IMAGE_URI, data.imageUri);
        rValue.put(PotlatchSchema.Cols.CREATED, data.created.format2445());
        rValue.put(PotlatchSchema.Cols.USER_ID, data.userID);
        rValue.put(PotlatchSchema.Cols.GIFT_CHAIN_ID, data.giftChainID);
        rValue.put(PotlatchSchema.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        return rValue;
    }

    @Override
    public Gift getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.ID));
        String title = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.TITLE));
        String description = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.DESCRIPTION));
        String videoUri = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.VIDEO_URI));
        String imageUri = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.IMAGE_URI));
        Time created = new Time();
        created.parse(cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.CREATED)));
        long userID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.USER_ID));
        long giftChainID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.GIFT_CHAIN_ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.GIFT_CHAIN_NAME));

        return new Gift(rowID, title, description, videoUri, imageUri, created, userID,
                giftChainID, giftChainName);
    }
}
