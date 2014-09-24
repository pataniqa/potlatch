package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.RestAdapter;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.store.MetadataStore;

import retrofit.http.*;

public interface RemoteGiftMetadataStore {
    // TODO
}

class RemoteGiftMetadataService implements MetadataStore {
    
    private RemoteGiftMetadataStore service;

    RemoteGiftMetadataService(RestAdapter restAdapter) {
        service = restAdapter.create(RemoteGiftMetadataStore.class);
    }

    @Override
    public void like(long giftID, long userID) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unlike(long giftID, long userID) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void flag(long giftID, long userID) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unflag(long giftID, long userID) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}
