package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.RestAdapter;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.MetadataStore;

import retrofit.http.*;

public interface RemoteGiftMetadataStore {
    @GET(RemoteGiftStore.GIFT_SVC_PATH + "/{id}/meta")
    GiftMetadata findOne(Long id);

    @PUT(RemoteGiftStore.GIFT_SVC_PATH + "/{id}/meta")
    GiftMetadata update(@Body GiftMetadata data);
}

class RemoteGiftMetadataService implements MetadataStore {
    
    private RemoteGiftMetadataStore service;

    RemoteGiftMetadataService(RestAdapter restAdapter) {
        service = restAdapter.create(RemoteGiftMetadataStore.class);
    }

    @Override
    public GiftMetadata save(GiftMetadata data) throws RemoteException {
        return service.update(data);
    }

    @Override
    public GiftMetadata findOne(Long id) throws RemoteException {
        return service.findOne(id);
    }

}
