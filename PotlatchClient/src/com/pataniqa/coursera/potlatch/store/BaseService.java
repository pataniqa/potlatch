package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftMetadata;

public abstract class BaseService implements Service {

    protected Store<Gift> userGifts;
    protected GiftQuery gifts;
    protected Store<GiftChain> giftChains;
    protected Update<GiftMetadata> giftMetadata;

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
