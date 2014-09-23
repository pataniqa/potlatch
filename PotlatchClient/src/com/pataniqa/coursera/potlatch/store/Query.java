package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

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
    ArrayList<T> query() throws RemoteException;

    /**
     * Query <T>.
     * 
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<T> query(final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) throws RemoteException;
}
