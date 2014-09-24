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
    public void setLike(long giftID, long userID, boolean like) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFlag(long giftID, long userID, boolean flag) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
}
