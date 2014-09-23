package com.pataniqa.coursera.potlatch.store;

public interface Service {

    /**
     * @return Get the interface to manage gifts created by this user.
     */
    GiftStore userGifts();

    /**
     * @return Get the interface to query all gifts.
     */
    GiftQuery gifts();

    /**
     * @return Get the interface to manage gift chains.
     */
    GiftChainStore giftChains();

    /**
     * @return Get the interface to manage gift metadata.
     */
    MetadataStore giftMetadata();

}
