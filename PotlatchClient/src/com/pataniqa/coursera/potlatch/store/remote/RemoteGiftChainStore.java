package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;

import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;

public interface RemoteGiftChainStore {

    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";

    @POST(GIFT_CHAIN_SVC_PATH)
    GiftChain insert(@Body GiftChain data);

    @PUT(GIFT_CHAIN_SVC_PATH + "/{id}")
    void update(long id, @Body GiftChain data);

    @DELETE(GIFT_CHAIN_SVC_PATH + "/{id}")
    void delete(long id);

    @GET(GIFT_CHAIN_SVC_PATH)
    Collection<GiftChain> findAll();

}

class RemoteGiftChainService implements GiftChainStore {

    private RemoteGiftChainStore service;

    RemoteGiftChainService(RestAdapter restAdapter) {
        service = restAdapter.create(RemoteGiftChainStore.class);
    }

    @Override
    public Collection<GiftChain> findAll() throws RemoteException {
        return service.findAll();
    }

    @Override
    public GiftChain save(GiftChain data) throws RemoteException {
        if (data.getID() == HasID.UNDEFINED_ID)
            data = service.insert(data);
        else
            service.update(data.getID(), data);
        return data;
    }

    @Override
    public void delete(long id) throws RemoteException {
        service.delete(id);
    }

}
