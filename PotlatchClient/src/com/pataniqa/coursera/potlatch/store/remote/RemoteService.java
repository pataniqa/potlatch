package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.experimental.Accessors;
import retrofit.RestAdapter;

import com.google.common.collect.Lists;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.store.GiftChains;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Service;

@Accessors(fluent=true)
public class RemoteService implements Service {
    
    @Getter private Gifts gifts;
    @Getter private GiftChains giftChains;
    @Getter private GiftMetadata giftMetadata;

    private RemoteGiftApi giftService;
    private RemoteGiftChainApi giftChainService;

    public RemoteService(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();
        giftService = restAdapter.create(RemoteGiftApi.class);
        giftChainService = restAdapter.create(RemoteGiftChainApi.class);
        gifts = new RemoteGiftQueryService();
        giftChains = new RemoteGiftChainService();
        giftMetadata = new RemoteGiftMetadataService();
    }

    class RemoteGiftChainService implements GiftChains {

        @Override
        public Collection<GiftChain> findAll() {
            return giftChainService.findAll();
        }

        @Override
        public GiftChain save(GiftChain data) {
            if (data.getId() == GetId.UNDEFINED_ID)
                data = giftChainService.insert(data);
            else
                giftChainService.update(data.getId(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            giftChainService.delete(id);
        }

    }

    class RemoteGiftMetadataService implements GiftMetadata {

        @Override
        public void setLike(long giftID, boolean like) {
            giftService.setLike(giftID, like);
        }

        @Override
        public void setFlag(long giftID, boolean flag) {
            giftService.setFlag(giftID, flag);
        }
    }

    class RemoteGiftQueryService implements Gifts {
        
        @Override
        public Gift save(Gift data) {
            if (data.getId() == GetId.UNDEFINED_ID)
                data = giftService.insert(data);
            else
                giftService.update(data.getId(), data);
            return data;
        }

        @Override
        public void delete(long id) {
            giftService.delete(id);
        }

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
