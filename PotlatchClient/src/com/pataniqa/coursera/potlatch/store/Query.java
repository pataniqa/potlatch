package com.pataniqa.coursera.potlatch.store;

import java.util.Collection;

public interface Query<T> {
    
    /**
     * Query <T>.
     * 
     * @return an ArrayList of GiftData objects
     */
    Collection<T> findAll();
}
