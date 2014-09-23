package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.Store;

public class LocalGiftChainStore extends BaseStore<GiftChain> implements Store<GiftChain> {
    
    public LocalGiftChainStore(LocalDatabase helper) {
        creator = new GiftChainCreator();
        tableName = LocalSchema.GiftChain.TABLE_NAME;
        this.helper = helper;
    }
}

class GiftChainCreator extends BaseCreator<GiftChain> implements Creator<GiftChain> {

    @Override
    public ContentValues getCV(GiftChain data) {
        ContentValues rValue = new ContentValues();
        rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        return rValue;
    }

    @Override
    public GiftChain getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_NAME));
        return new GiftChain(rowID, giftChainName);
    }
}
