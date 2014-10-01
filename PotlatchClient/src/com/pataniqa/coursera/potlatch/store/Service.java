package com.pataniqa.coursera.potlatch.store;

public interface Service {

    /**
     * @return Get the interface to query all gifts.
     */
    Gifts gifts();

    /**
     * @return Get the interface to manage gift chains.
     */
    GiftChains giftChains();

    /**
     * @return Get the interface to manage gift metadata.
     */
    GiftMetadata giftMetadata();

}
