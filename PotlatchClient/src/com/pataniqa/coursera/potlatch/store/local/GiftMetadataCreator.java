package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;

public class GiftMetadataCreator extends BaseCreator<GiftMetadata> implements Creator<GiftMetadata> {

    @Override
    public ContentValues getCV(GiftMetadata data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PotlatchSchema.GiftMetadata.Cols.GIFT_ID, data.giftID);
        rValue.put(PotlatchSchema.GiftMetadata.Cols.LIKE, data.like);
        rValue.put(PotlatchSchema.GiftMetadata.Cols.FLAG, data.flag);
        rValue.put(PotlatchSchema.GiftMetadata.Cols.LIKES, data.likes);
        rValue.put(PotlatchSchema.GiftMetadata.Cols.FLAGGED, data.flagged);
        rValue.put(PotlatchSchema.GiftMetadata.Cols.GIFT_CHAIN_ID, data.giftChainID);
        return rValue;
    }

    @Override
    public GiftMetadata getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.ID));
        long giftID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.GIFT_ID));
        boolean like = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.LIKE)) > 0;
        boolean flag = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.FLAG)) > 0;
        long likes = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.LIKES));
        boolean flagged = cursor.getInt(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.FLAGGED)) > 0;
        long giftChainID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.GiftMetadata.Cols.GIFT_CHAIN_ID));
        return new GiftMetadata(rowID, giftID, like, flag, likes, flagged, giftChainID);
    }
}
