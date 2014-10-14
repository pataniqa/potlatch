package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import rx.Observable;

public interface Query<T> {
    
    /**
     * Query <T>.
     * 
     * @return an ArrayList of GiftData objects
     */
    Observable<ArrayList<T>> findAll();
    
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     */
    Observable<T> findOne(long id);
}
