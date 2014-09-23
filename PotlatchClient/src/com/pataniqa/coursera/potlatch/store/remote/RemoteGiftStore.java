package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.Store;

interface RemoteGiftStore extends Store<Gift> {

    public static final String GIFT_SVC_PATH = "/gift";

    @Override
    @POST(GIFT_SVC_PATH)
    long insert(@Body Gift data) throws RemoteException;
    
    @Override
    @GET(GIFT_SVC_PATH + "/{id}")
    Gift get(long id) throws RemoteException;

    // TODO FIXME this will not work because ID is not an argument we are breaking REST
    @Override
    @PUT(GIFT_SVC_PATH + "/{id}")
    void update(@Body Gift data) throws RemoteException;
    
    @Override
    @DELETE(GIFT_SVC_PATH + "/{id}")
    void delete(long id) throws RemoteException;
    
    @Override
    @GET(GIFT_SVC_PATH)
    Collection<Gift> query() throws RemoteException;

}
