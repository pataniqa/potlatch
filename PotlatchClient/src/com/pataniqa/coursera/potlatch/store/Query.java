package com.pataniqa.coursera.potlatch.store;

import java.util.Collection;

import android.os.RemoteException;

public interface Query<T> {
    
    /**
     * Query <T>.
     * 
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    Collection<T> query() throws RemoteException;
}
