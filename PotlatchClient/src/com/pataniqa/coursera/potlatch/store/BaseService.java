package com.pataniqa.coursera.potlatch.store;

public abstract class BaseService implements Service {

    protected GiftStore userGifts;
    protected GiftQuery gifts;
    protected GiftChainStore giftChains;
    protected MetadataStore giftMetadata;

    @Override
    public GiftStore userGifts() {
        return userGifts;
    }

    @Override
    public GiftQuery gifts() {
        return gifts;
    }

    @Override
    public GiftChainStore giftChains() {
        return giftChains;
    }

    @Override
    public MetadataStore giftMetadata() {
        return giftMetadata;
    }

}
