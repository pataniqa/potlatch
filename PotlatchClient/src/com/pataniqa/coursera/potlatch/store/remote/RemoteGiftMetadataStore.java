package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.MetadataStore;

interface RemoteGiftMetadataStore extends MetadataStore {

    @Override
    @GET(RemoteGiftStore.GIFT_SVC_PATH + "/{id}/meta")
    GiftMetadata findOne(Long id) throws RemoteException;

    @Override
    @PUT(RemoteGiftStore.GIFT_SVC_PATH + "/{id}/meta")
    void update(@Body GiftMetadata data) throws RemoteException;

}
