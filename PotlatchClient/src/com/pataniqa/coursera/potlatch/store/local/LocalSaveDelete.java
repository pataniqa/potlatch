package com.pataniqa.coursera.potlatch.store.local;

import com.pataniqa.coursera.potlatch.model.GetId;

public interface LocalSaveDelete <T extends GetId> {
    
    /**
     * Delete a <T> object.
     * 
     * @param id
     */
    Boolean delete(long id);
    
    /**
     * Save the <T> object.
     * 
     * @param <T> object to be saved.
     * @return The object that was saved.
     */
    <S extends T> S save(S data);
}
