package com.pataniqa.coursera.potlatch.store.local;

import android.content.Context;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.GiftQuery;
import com.pataniqa.coursera.potlatch.store.Service;
import com.pataniqa.coursera.potlatch.store.Store;
import com.pataniqa.coursera.potlatch.store.Update;

public class LocalService implements Service {

    Store<Gift> userGifts;
    GiftQuery gifts;
    Store<GiftChain> giftChains;
    Update<GiftMetadata> giftMetadata;

    public LocalService(Context context) {
        LocalDatabase helper = new LocalDatabase(context);
        userGifts = new LocalGiftStore(helper);
        gifts = new LocalGiftQuery(helper);
        giftChains = new LocalGiftChainStore(helper);
        giftMetadata = new LocalGiftMetadataStore(helper);
    }

    @Override
    public Store<Gift> userGifts() {
        return userGifts;
    }

    @Override
    public GiftQuery gifts() {
        return gifts;
    }

    @Override
    public Store<GiftChain> giftChains() {
        return giftChains;
    }

    @Override
    public Update<GiftMetadata> giftMetadata() {
        return giftMetadata;
    }

}
