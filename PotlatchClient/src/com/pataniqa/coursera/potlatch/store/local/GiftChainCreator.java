package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.GiftChain;

public class GiftChainCreator extends BaseCreator<GiftChain> implements Creator<GiftChain> {

    @Override
    public ContentValues getCV(GiftChain data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PotlatchSchema.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        return rValue;
    }

    @Override
    public GiftChain getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Cols.ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Cols.GIFT_CHAIN_NAME));
        return new GiftChain(rowID, giftChainName);
    }
}
