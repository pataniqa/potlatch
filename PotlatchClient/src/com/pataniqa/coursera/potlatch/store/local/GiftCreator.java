package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

import com.pataniqa.coursera.potlatch.model.ClientGift;

/**
 * GiftCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as GiftData), ContentValues, and
 * Cursors.
 */
public class GiftCreator extends BaseCreator<ClientGift> implements Creator<ClientGift> {

    @Override
    public ContentValues getCV(ClientGift data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PotlatchSchema.Cols.TITLE, data.title);
        rValue.put(PotlatchSchema.Cols.DESCRIPTION, data.description);
        rValue.put(PotlatchSchema.Cols.VIDEO_URI, data.videoUri);
        rValue.put(PotlatchSchema.Cols.IMAGE_URI, data.imageUri);
        rValue.put(PotlatchSchema.Cols.CREATED, data.created.format2445());
        rValue.put(PotlatchSchema.Cols.USER_ID, data.userID);
        rValue.put(PotlatchSchema.Cols.LIKE, data.like);
        rValue.put(PotlatchSchema.Cols.FLAG, data.flag);
        rValue.put(PotlatchSchema.Cols.LIKES, data.likes);
        rValue.put(PotlatchSchema.Cols.FLAGGED, data.flagged);
        rValue.put(PotlatchSchema.Cols.GIFT_CHAIN_ID, data.giftChainID);
        rValue.put(PotlatchSchema.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        return rValue;
    }

    @Override
    public ClientGift getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.ID));
        String title = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.TITLE));
        String description = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.DESCRIPTION));
        String videoUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.VIDEO_URI));
        String imageUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.IMAGE_URI));
        Time created = new Time();
        created.parse(cursor.getString(cursor.getColumnIndex(PotlatchSchema.Cols.CREATED)));
        long userID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.USER_ID));
        boolean like = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Cols.LIKE)) > 0;
        boolean flag = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Cols.FLAG)) > 0;
        long likes = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.LIKES));
        boolean flagged = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Cols.FLAGGED)) > 0;
        long giftChainID = cursor.getLong(cursor
                .getColumnIndex(PotlatchSchema.Cols.GIFT_CHAIN_ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.GIFT_CHAIN_NAME));

        return new ClientGift(rowID, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainID, giftChainName);
    }
}
