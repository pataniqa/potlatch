package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface GiftStore extends Store<ClientGift> {

    /**
     * Query gift data by title.
     * 
     * @param title The title.
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByTitle(String title) throws RemoteException;
}
