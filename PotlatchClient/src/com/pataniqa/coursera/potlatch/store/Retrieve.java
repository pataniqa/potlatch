package com.pataniqa.coursera.potlatch.store;

public interface Retrieve<T, ID> {
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     */
    T findOne(ID id);
}
