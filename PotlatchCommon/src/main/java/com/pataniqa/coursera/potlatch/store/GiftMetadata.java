package com.pataniqa.coursera.potlatch.store;

import rx.Observable;

/**
 * Set whether a user likes a gift or flags it as
 * inappropriate.
 */
public interface GiftMetadata {

    /**
     * Set whether a user likes a gift.
     * 
     * @param giftID The gift ID.
     * @param like Do they like it?
     * @return Indicate the operation has finished.
     */
    Observable<Boolean> setLike(long giftID, boolean like);

    /**
     * Set whether a user flags a gift as in-appropriate.
     * 
     * @param giftID The gift ID.
     * @param flag Have they flagged it as inappropriate?
     * @return Indicate the operation has finished.
     */
    Observable<Boolean> setFlag(long giftID, boolean flag);

}
