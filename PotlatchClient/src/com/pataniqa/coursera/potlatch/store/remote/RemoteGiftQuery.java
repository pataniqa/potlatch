package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftQuery;

interface RemoteGiftQuery extends GiftQuery {

    @Override
    ClientGift get(long rowID) throws RemoteException;

    @Override
    ArrayList<ClientGift> query() throws RemoteException;

    @Override
    ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    @Override
    ArrayList<ClientGift> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    @Override
    ArrayList<ClientGift> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    @Override
    ArrayList<ClientGift> queryByGiftChain(String title,
            String giftChainName,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

}
