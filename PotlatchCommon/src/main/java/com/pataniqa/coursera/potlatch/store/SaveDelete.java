package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.HasId;

import rx.Observable;

/**
 * An interface for saving and deleting records.
 *
 * @param <T> The record type.
 */
public interface SaveDelete <T extends HasId> {
    
    /**
     * Delete a <T> object.
     * 
     * @param id The record ID>
     */
    Observable<Boolean> delete(long id);
    
    /**
     * Save the <T> object.
     * 
     * @param <T> object to be saved.
     * @return The object that was saved.
     */
    <S extends T> Observable<S> save(S data);
}
