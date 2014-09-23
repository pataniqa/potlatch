package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Update;

public class RemoteGiftMetadataStore implements Update<GiftMetadata> {

    @Override
    public GiftMetadata get(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<GiftMetadata> query() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(GiftMetadata data) throws RemoteException {
        // TODO Auto-generated method stub
    }

}
