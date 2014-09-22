package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftData;

public interface GiftStore {
    /**
     * Insert a new GiftData object.
     * 
     * @param gift object to be inserted
     * @return row ID of inserted GiftData in the ContentProvider
     * @throws RemoteException
     */
    long insert(final GiftData gift) throws RemoteException;

    /**
     * Delete a GiftData object.
     * 
     * @param rowID
     * @return number of rows deleted
     * @throws RemoteException
     */
    int delete(long rowID) throws RemoteException;

    /**
     * Retrieve a GiftData object with a specific rowID.
     * 
     * @param rowID
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    GiftData get(final long rowID) throws RemoteException;

    /**
     * Update a GiftData object with a specific rowID.
     * 
     * @param data
     * @return number of rows altered
     * @throws RemoteException
     */
    int update(GiftData data) throws RemoteException;

    /**
     * Query gift data.
     * 
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<GiftData> query(final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) throws RemoteException;

    /**
     * Query gift data by title.
     * 
     * @param title The title.
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<GiftData> queryByTitle(String title) throws RemoteException;
}
