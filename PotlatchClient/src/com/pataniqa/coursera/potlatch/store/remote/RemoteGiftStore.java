package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.*;
import retrofit.http.*;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.GiftStore;

interface RemoteGiftStore {
    public static final String GIFT_SVC_PATH = "/gift";
    
    @POST(GIFT_SVC_PATH)
    Gift insert(@Body Gift data) throws RemoteException;
    
    @PUT(GIFT_SVC_PATH + "/{id}")
    void update(@Body Gift data) throws RemoteException;
    
    @DELETE(GIFT_SVC_PATH + "/{id}")
    void delete(long id) throws RemoteException;
}

class RemoteGiftService implements GiftStore {
    
    private RemoteGiftStore service;

    RemoteGiftService(RestAdapter restAdapter) {
        service = restAdapter.create(RemoteGiftStore.class);
    }

    @Override
    public Gift save(Gift data) throws RemoteException {
        if (data.getID() ==  HasID.UNDEFINED_ID) 
            data = service.insert(data);
        else
            service.update(data);
        return data;
    }

    @Override
    public void delete(long id) throws RemoteException {
        service.delete(id);
    }

}
