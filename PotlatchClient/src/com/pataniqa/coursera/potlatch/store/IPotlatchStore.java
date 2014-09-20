package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftData;

public interface IPotlatchStore {
    /**
     * Insert a new GiftData object into the database
     * 
     * @param gift object to be inserted
     * @return row ID of inserted GiftData in the ContentProvider
     * @throws RemoteException
     */
    long insert(final GiftData gift) throws RemoteException;

    /**
     * Delete All rows, from AllGift table, that have the given rowID. (Should
     * only be 1 row, but Content Providers/SQLite3 deletes all rows with
     * provided rowID)
     * 
     * @param rowID
     * @return number of rows deleted
     * @throws RemoteException
     */
    int deleteAllGiftWithRowID(long rowID) throws RemoteException;

    /**
     * Get a GiftData from the data stored at the given rowID
     * 
     * @param rowID
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    GiftData getGiftDataViaRowID(final long rowID) throws RemoteException;
    
    /**
     * Updates all GiftData stored with the provided GiftData's 'KEY_ID' (should
     * only be 1 row of data in the content provider, but content provider
     * implementation will update EVERY row that matches.)
     * 
     * @param data
     * @return number of rows altered
     * @throws RemoteException
     */
    int updateGiftWithID(GiftData data) throws RemoteException;
    
    /**
     * Query the database for GiftData conforming to certain specifications.
     * 
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<GiftData> queryGiftData(final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) throws RemoteException;
    
    
    ArrayList<GiftData> getGiftsThatMatchTitle(String title) throws RemoteException;
}
