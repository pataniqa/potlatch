package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.Store;

class RemoteGiftStore implements Store<Gift> {

    @Override
    public int update(Gift data) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Gift get(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Gift> query() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Gift> query(String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long insert(Gift data) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }

}
