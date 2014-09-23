package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftMetadata;

public interface Service {
    
    /**
     * @return Get the interface to manage gifts created by this user.
     */
    Store<Gift> userGifts();
    
    /**
     * @return Get the interface to query all gifts.
     */
    GiftQuery gifts();
    
    /**
     * @return Get the interface to manage gift chains.
     */
    Store<GiftChain> giftChains();
    
    /**
     * @return Get the interface to manage gift metadata.
     */
    Update<GiftMetadata> giftMetadata();

}
