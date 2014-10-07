package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.GetId;

import rx.Observable;

public interface SaveDelete <T extends GetId> {
    
    /**
     * Delete a <T> object.
     * 
     * @param id
     */
    void delete(long id);
    
    /**
     * Save the <T> object.
     * 
     * @param <T> object to be saved.
     * @return The object that was saved.
     */
    <S extends T> Observable<S> save(S data);
}
