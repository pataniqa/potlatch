package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

interface LocalQuery<T> {
    
    /**
     * Query <T>.
     * 
     * @return an ArrayList of GiftData objects
     */
    ArrayList<T> findAll();
    
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     */
    T findOne(long id);
}
