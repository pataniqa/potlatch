package com.pataniqa.coursera.potlatch.store;

import com.pataniqa.coursera.potlatch.model.HasId;

/**
 * Create - retrieve - update - delete interface.
 * 
 * @param <T> A class that can be stored in a data store.
 */
public interface CRUD<T extends HasId> extends Query<T>, SaveDelete<T> {

}
