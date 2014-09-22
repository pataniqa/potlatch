package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftData;

public interface GiftStore extends Store<GiftData> {

    /**
     * Query gift data by title.
     * 
     * @param title The title.
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<GiftData> queryByTitle(String title) throws RemoteException;
}
