package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.Store;

public class RemoteGiftChainStore implements Store<GiftChain> {

    @Override
    public void update(GiftChain data) throws RemoteException {
        // TODO Auto-generated method stub
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
    public long insert(GiftChain data) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
    }

}
