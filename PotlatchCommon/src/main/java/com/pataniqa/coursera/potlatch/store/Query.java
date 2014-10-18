package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import rx.Observable;

import com.pataniqa.coursera.potlatch.model.HasId;

/**
 * An interface for querying stores.
 * 
 * @param <T> The record type.
 */
public interface Query<T extends HasId> {
    
    /**
     * Get all the <T> objects.
     * 
     * @return an ArrayList of <T> objects
     */
    Observable<ArrayList<T>> findAll();
    
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id The unique ID.
     * @return <T> with the unique ID.
     */
    Observable<T> findOne(long id);
}
