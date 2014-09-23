package com.pataniqa.coursera.potlatch.store;

import java.util.Collection;

import android.os.RemoteException;

public interface Query<T> {
    
    /**
     * Retrieve a <T> object with a specific rowID.
     * 
     * @param rowID
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    T get(final long rowID) throws RemoteException;
    
    /**
     * Query <T>.
     * 
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    Collection<T> query() throws RemoteException;
}
