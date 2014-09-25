package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.RestAdapter;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.BaseService;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;
import com.pataniqa.coursera.potlatch.store.GiftQuery;
import com.pataniqa.coursera.potlatch.store.GiftStore;
import com.pataniqa.coursera.potlatch.store.MetadataStore;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Service;

public class RemoteService extends BaseService implements Service {

    private RemoteApi service;

    public RemoteService(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();
        service = restAdapter.create(RemoteApi.class);
        userGifts = new RemoteGiftService();
        gifts = new RemoteGiftQueryService();
        giftChains = new RemoteGiftChainService();
        giftMetadata = new RemoteGiftMetadataService();
    }

    class RemoteGiftChainService implements GiftChainStore {

        @Override
        public Collection<GiftChain> findAll() {
            return service.findAllGiftChains();
        }

        @Override
        public GiftChain save(GiftChain data) {
            if (data.getID() == HasID.UNDEFINED_ID)
                data = service.insert(data);
            else
                service.update(data.getID(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            service.deleteGiftChain(id);
        }

    }

    class RemoteGiftMetadataService implements MetadataStore {

        @Override
        public void setLike(long giftID, long userID, boolean like) {
            service.setLike(giftID, userID, like);
        }

        @Override
        public void setFlag(long giftID, long userID, boolean flag) {
            service.setFlag(giftID, userID, flag);
        }
    }

    class RemoteGiftService implements GiftStore {

        @Override
        public Gift save(Gift data) {
            if (data.getID() == HasID.UNDEFINED_ID)
                data = service.insert(data);
            else
                service.update(data.getID(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            service.deleteGift(id);
        }

    }
    
    class RemoteGiftQueryService implements GiftQuery {
        
        @Override
        public Collection<ClientGift> findAll() {
            return service.findAllGifts();
        }

        @Override
        public ClientGift findOne(Long id) {
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
                ResultOrderDirection resultOrderDirection) {
            return toArrayList(service.queryByTitle(title, resultOrder, resultOrderDirection));
        }

        @Override
        public ArrayList<ClientGift> queryByUser(String title,
                long userID,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection) {
            return toArrayList(service.queryByUser(title, userID, resultOrder, resultOrderDirection));
        }

        @Override
        public ArrayList<ClientGift> queryByTopGiftGivers(String title,
                ResultOrderDirection resultOrderDirection) {
            return toArrayList(service.queryByTopGiftGivers(title, resultOrderDirection));
        }

        @Override
        public ArrayList<ClientGift> queryByGiftChain(String title,
                String giftChainName,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection) {
            return toArrayList(service.queryByGiftChain(title, giftChainName, resultOrder, resultOrderDirection));
        }

    }

}
