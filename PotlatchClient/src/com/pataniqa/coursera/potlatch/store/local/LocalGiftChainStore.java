package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.GiftChain;

public class LocalGiftChainStore extends BaseCreateUpdateDelete<GiftChain> implements
        LocalGiftChains {

    public LocalGiftChainStore(LocalDatabase helper) {
        super(new GiftChainCreator(), LocalSchema.GiftChain.TABLE_NAME, helper);
    }

    private static class GiftChainCreator extends BaseCreator<GiftChain> implements
            Creator<GiftChain> {

        @Override
        public ContentValues getCV(GiftChain data) {
            ContentValues rValue = new ContentValues();
            rValue.put(LocalSchema.Cols.ID, data.getId());
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, data.getName());
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
}
