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
        rValue.put(PotlatchSchema.Gift.Cols.TITLE, data.title);
        rValue.put(PotlatchSchema.Gift.Cols.DESCRIPTION, data.description);
        rValue.put(PotlatchSchema.Gift.Cols.VIDEO_URI, data.videoUri);
        rValue.put(PotlatchSchema.Gift.Cols.IMAGE_URI, data.imageUri);
        rValue.put(PotlatchSchema.Gift.Cols.CREATED, data.created.format2445());
        rValue.put(PotlatchSchema.Gift.Cols.USER_ID, data.userID);
        rValue.put(PotlatchSchema.Gift.Cols.LIKE, data.like);
        rValue.put(PotlatchSchema.Gift.Cols.FLAG, data.flag);
        rValue.put(PotlatchSchema.Gift.Cols.LIKES, data.likes);
        rValue.put(PotlatchSchema.Gift.Cols.FLAGGED, data.flagged);
        rValue.put(PotlatchSchema.Gift.Cols.GIFT_CHAIN_ID, data.giftChainID);
        rValue.put(PotlatchSchema.Gift.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        return rValue;
    }

    @Override
    public ClientGift getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.ID));
        String title = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.TITLE));
        String description = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.DESCRIPTION));
        String videoUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.VIDEO_URI));
        String imageUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.IMAGE_URI));
        Time created = new Time();
        created.parse(cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.CREATED)));
        long userID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.USER_ID));
        boolean like = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.LIKE)) > 0;
        boolean flag = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.FLAG)) > 0;
        long likes = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.LIKES));
        boolean flagged = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.FLAGGED)) > 0;
        long giftChainID = cursor.getLong(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.GIFT_CHAIN_ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.GIFT_CHAIN_NAME));

        return new ClientGift(rowID, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainID, giftChainName);
    }
}
