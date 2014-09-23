package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.Store;

public class RemoteGiftChainStore implements Store<GiftChain> {

    @Override
    public int update(GiftChain data) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public GiftChain get(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<GiftChain> query() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<GiftChain> query(String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long insert(GiftChain data) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

}
