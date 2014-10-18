package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.User;

/**
 * Facade for local and remote implementations of the store.
 */
public interface DataService {

    /**
     * @return The gift store.
     */
    Gifts gifts();

    /**
     * @return The gift chain store.
     */
    CRUD<GiftChain> giftChains();

    /**
     * @return The gift metadata store.
     */
    GiftMetadata giftMetadata();
    
    /**
     * @return The user store..
     */
    CRUD<User> users();
}
