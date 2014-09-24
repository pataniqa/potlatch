package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;

interface RemoteGiftChainStore extends GiftChainStore {
    
    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";

    // TODO not sure about returning a long here?
    @Override
    @POST(GIFT_CHAIN_SVC_PATH)
    public GiftChain insert(@Body GiftChain data) throws RemoteException;
            
    @Override
    @DELETE(GIFT_CHAIN_SVC_PATH + "/{id}")
    public void delete(long id) throws RemoteException;

    @Override
    @GET(GIFT_CHAIN_SVC_PATH)
    public Collection<GiftChain> findAll() throws RemoteException;

}
