package com.pataniqa.coursera.potlatch.store;

import rx.Observable;

public interface Retrieve<T, ID> {
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     */
    Observable<T> findOne(ID id);
}
