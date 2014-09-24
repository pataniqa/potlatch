package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.PUT;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.GiftStore;

interface RemoteGiftStore extends GiftStore {

    public static final String GIFT_SVC_PATH = "/gift";

    // TODO not sure about returning a long here?
    @Override
    @POST(GIFT_SVC_PATH)
    Gift insert(@Body Gift data) throws RemoteException;
    
    // TODO this will not work because ID is not an argument we are breaking REST?
    @Override
    @PUT(GIFT_SVC_PATH + "/{id}")
    void update(@Body Gift data) throws RemoteException;
    
    @Override
    @DELETE(GIFT_SVC_PATH + "/{id}")
    void delete(long id) throws RemoteException;
   
}
