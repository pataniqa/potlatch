package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;
import java.util.Collection;

import retrofit.RestAdapter;

import com.google.common.collect.Lists;
import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.model.client.Gift;
import com.pataniqa.coursera.potlatch.model.client.GiftChain;
import com.pataniqa.coursera.potlatch.model.client.GiftResult;
import com.pataniqa.coursera.potlatch.store.BaseService;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;
import com.pataniqa.coursera.potlatch.store.GiftQuery;
import com.pataniqa.coursera.potlatch.store.GiftStore;
import com.pataniqa.coursera.potlatch.store.MetadataStore;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Service;

public class RemoteService extends BaseService implements Service {

    private RemoteGiftApi giftService;
    private RemoteGiftChainApi giftChainService;

    public RemoteService(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();
        giftService = restAdapter.create(RemoteGiftApi.class);
        giftChainService = restAdapter.create(RemoteGiftChainApi.class);
        userGifts = new RemoteGiftService();
        gifts = new RemoteGiftQueryService();
        giftChains = new RemoteGiftChainService();
        giftMetadata = new RemoteGiftMetadataService();
    }

    class RemoteGiftChainService implements GiftChainStore {

        @Override
        public Collection<GiftChain> findAll() {
            return giftChainService.findAll();
        }

        @Override
        public GiftChain save(GiftChain data) {
            if (data.getID() == HasID.UNDEFINED_ID)
                data = giftChainService.insert(data);
            else
                giftChainService.update(data.getID(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            giftChainService.deleteGiftChain(id);
        }

    }

    class RemoteGiftMetadataService implements MetadataStore {

        @Override
        public void setLike(long giftID, long userID, boolean like) {
            giftService.setLike(giftID, userID, like);
        }

        @Override
        public void setFlag(long giftID, long userID, boolean flag) {
            giftService.setFlag(giftID, userID, flag);
        }
    }

    class RemoteGiftService implements GiftStore {

        @Override
        public Gift save(Gift data) {
            if (data.getID() == HasID.UNDEFINED_ID)
                data = giftService.insert(data);
            else
                giftService.update(data.getID(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            giftService.deleteGift(id);
        }

    }

    class RemoteGiftQueryService implements GiftQuery {

        @Override
        public Collection<GiftResult> findAll() {
            return giftService.findAll();
        }

        @Override
        public GiftResult findOne(Long id) {
            return giftService.findOne(id);
        }

        @Override
        public ArrayList<GiftResult> queryByTitle(String title,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection) {
            return Lists.newArrayList(giftService.queryByTitle(title,
                    resultOrder.getVal(),
                    resultOrderDirection.getVal()));
        }

        @Override
        public ArrayList<GiftResult> queryByUser(String title,
                long userID,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection) {
            return Lists.newArrayList(giftService.queryByUser(title,
                    userID,
                    resultOrder.getVal(),
                    resultOrderDirection.getVal()));
        }

        @Override
        public ArrayList<GiftResult> queryByTopGiftGivers(String title,
                ResultOrderDirection resultOrderDirection) {
            return Lists
                    .newArrayList(giftService.queryByTopGiftGivers(title, resultOrderDirection.getVal()));
        }

        @Override
        public ArrayList<GiftResult> queryByGiftChain(String title,
                String giftChainName,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection) {
            return Lists.newArrayList(giftService.queryByGiftChain(title,
                    giftChainName,
                    resultOrder.getVal(),
                    resultOrderDirection.getVal()));
        }

    }

}
