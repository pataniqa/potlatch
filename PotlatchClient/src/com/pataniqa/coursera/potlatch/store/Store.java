package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;

public interface Store<T extends HasID> extends Query<T> {
    /**
     * Insert a new <T> object.
     * 
     * @param gift object to be inserted
     * @return row ID of inserted GiftData in the ContentProvider
     * @throws RemoteException
     */
    long insert(final T data) throws RemoteException;

    /**
     * Delete a <T> object.
     * 
     * @param rowID
     * @return number of rows deleted
     * @throws RemoteException
     */
    int delete(long rowID) throws RemoteException;

    /**
     * Update a <T> object with a specific rowID.
     * 
     * @param data
     * @return number of rows altered
     * @throws RemoteException
     */
    int update(T data) throws RemoteException;
}