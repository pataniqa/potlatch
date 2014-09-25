package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.client.GiftChain;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;

public class LocalGiftChainStore extends BaseCreateUpdateDelete<GiftChain> implements GiftChainStore {
    
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
        rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, data.getGiftChainName());
        return rValue;
    }

    @Override
    public GiftChain getFromCursor(Cursor cursor) {
        long giftChainID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_NAME));
        return new GiftChain(giftChainID, giftChainName);
    }
}
