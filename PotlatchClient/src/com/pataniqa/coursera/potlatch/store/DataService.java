package com.pataniqa.coursera.potlatch.store;

public interface DataService {

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
    
    /**
     * @return The interface to manage users.
     */
    Users users();

}
