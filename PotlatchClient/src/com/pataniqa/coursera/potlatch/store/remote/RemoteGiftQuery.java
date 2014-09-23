package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftQuery;

class RemoteGiftQuery implements GiftQuery {

    @Override
    public ClientGift get(long rowID) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> query() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> query(String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByUser(long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByTopGiftGivers(ResultOrderDirection resultOrderDirection)
            throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByGiftChain(long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

}
