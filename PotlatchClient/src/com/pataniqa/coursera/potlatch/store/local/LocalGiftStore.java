package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.Store;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 * 
 */
public class LocalGiftStore extends BaseStore<Gift> implements Store<Gift> {

    public LocalGiftStore(LocalDatabase helper) {
        creator = new GiftCreator();
        tableName = LocalSchema.Gift.TABLE_NAME;
        this.helper = helper;
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }

}

class GiftCreator extends BaseCreator<Gift> implements Creator<Gift> {

    @Override
    public ContentValues getCV(Gift data) {
        ContentValues rValue = new ContentValues();
        rValue.put(LocalSchema.Cols.TITLE, data.title);
        rValue.put(LocalSchema.Cols.DESCRIPTION, data.description);
        rValue.put(LocalSchema.Cols.VIDEO_URI, data.videoUri);
        rValue.put(LocalSchema.Cols.IMAGE_URI, data.imageUri);
        rValue.put(LocalSchema.Cols.CREATED, data.created.format2445());
        rValue.put(LocalSchema.Cols.USER_ID, data.userID);
        rValue.put(LocalSchema.Cols.GIFT_CHAIN_ID, data.giftChainID);
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
        Time created = new Time();
        created.parse(cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.CREATED)));
        long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_ID));
        long giftChainID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_ID));

        return new Gift(rowID, title, description, videoUri, imageUri, created, userID,
                giftChainID);
    }
}