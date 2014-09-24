package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface Retrieve<T, ID> {
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    T findOne(ID id) throws RemoteException;
}
