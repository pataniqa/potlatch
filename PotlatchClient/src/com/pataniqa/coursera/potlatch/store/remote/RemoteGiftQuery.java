package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.RestAdapter;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftQuery;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;

interface RemoteGiftQuery {
    ClientGift findOne(Long id);

    List<ClientGift> findAll();

    List<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);

    List<ClientGift> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);

    List<ClientGift> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection);

    List<ClientGift> queryByGiftChain(String title,
            String giftChainName,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);
}

class RemoteGiftQueryService implements GiftQuery {
    
    private RemoteGiftQuery service;

    RemoteGiftQueryService(RestAdapter restAdapter) {
        service = restAdapter.create(RemoteGiftQuery.class);
    }

    @Override
    public Collection<ClientGift> findAll() throws RemoteException {
        return service.findAll();
    }

    @Override
    public ClientGift findOne(Long id) throws RemoteException {
        return service.findOne(id);
    }

    ArrayList<ClientGift> toArrayList(List<ClientGift> input) {
        ArrayList<ClientGift> result = new ArrayList<ClientGift>();
        result.addAll(input);
        return result;
    }
    
    @Override
    public ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        return toArrayList(service.queryByTitle(title, resultOrder, resultOrderDirection));
    }

    @Override
    public ArrayList<ClientGift> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        return toArrayList(service.queryByUser(title, userID, resultOrder, resultOrderDirection));
    }

    @Override
    public ArrayList<ClientGift> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        return toArrayList(service.queryByTopGiftGivers(title, resultOrderDirection));
    }

    @Override
    public ArrayList<ClientGift> queryByGiftChain(String title,
            String giftChainName,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        return toArrayList(service.queryByGiftChain(title, giftChainName, resultOrder, resultOrderDirection));
    }

}
