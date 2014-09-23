package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface Retrieve<T> {
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param id
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    T get(final long id) throws RemoteException;
}
