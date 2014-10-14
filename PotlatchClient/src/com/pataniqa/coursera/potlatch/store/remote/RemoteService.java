package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import lombok.Getter;
import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import rx.Observable;

import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.store.GiftChains;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.DataService;
import com.pataniqa.coursera.potlatch.store.Users;

@Accessors(fluent = true)
public class RemoteService implements DataService {

    @Getter private final Gifts gifts;
    @Getter private final GiftChains giftChains;
    @Getter private final GiftMetadata giftMetadata;
    @Getter private final Users users;

    private final RemoteGiftApi giftService;
    private final RemoteGiftChainApi giftChainService;
    private final RemoteUserApi userService;

    public RemoteService(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();
        giftService = restAdapter.create(RemoteGiftApi.class);
        giftChainService = restAdapter.create(RemoteGiftChainApi.class);
        userService = restAdapter.create(RemoteUserApi.class);
        gifts = new RemoteGiftQueryService();
        giftChains = new RemoteGiftChainService();
        giftMetadata = new RemoteGiftMetadataService();
        users = new RemoteUserService();
    }

    class RemoteUserService implements Users {

        @Override
        public Observable<ArrayList<User>> findAll() {
            return userService.findAll();
        }

        @Override
        public Observable<User> save(User data) {
            if (data.getId() == GetId.UNDEFINED_ID)
                return userService.insert(data);
            else
                return userService.update(data.getId(), data);
        }

        @Override
        public Observable<Boolean> delete(long id) {
            return userService.delete(id);
        }
        
        @Override
        public Observable<User> findOne(long id) {
            return userService.findOne(id);
        }
    }

    class RemoteGiftChainService implements GiftChains {

        @Override
        public Observable<ArrayList<GiftChain>> findAll() {
            return giftChainService.findAll();
        }

        @Override
        public Observable<GiftChain> save(GiftChain data) {
            if (data.getId() == GetId.UNDEFINED_ID)
                return giftChainService.insert(data);
            else
                return giftChainService.update(data.getId(), data);
        }

        @Override
        public Observable<Boolean> delete(long id) {
            return giftChainService.delete(id);
        }
        
        @Override
        public Observable<GiftChain> findOne(long id) {
            return giftChainService.findOne(id);
        }

    }

    class RemoteGiftMetadataService implements GiftMetadata {

        @Override
        public Observable<Boolean> setLike(long giftID, boolean like) {
            return giftService.setLike(giftID, like);
        }

        @Override
        public Observable<Boolean> setFlag(long giftID, boolean flag) {
            return giftService.setFlag(giftID, flag);
        }
    }

    class RemoteGiftQueryService implements Gifts {

        @Override
        public Observable<Gift> save(Gift data) {
            if (data.getId() == GetId.UNDEFINED_ID)
                return giftService.insert(data);
            else
                return giftService.update(data.getId(), data);
        }

        @Override
        public Observable<Boolean> delete(long id) {
            return giftService.delete(id);
        }

        @Override
        public Observable<ArrayList<GiftResult>> findAll() {
            return giftService.findAll();
        }

        @Override
        public Observable<GiftResult> findOne(long id) {
            return giftService.findOne(id);
        }

        @Override
        public Observable<ArrayList<GiftResult>> queryByTitle(String title,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection, boolean hide) {
            return giftService.queryByTitle(title, resultOrder, resultOrderDirection, hide);
        }

        @Override
        public Observable<ArrayList<GiftResult>> queryByUser(String title,
                long userID,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection,
                boolean hide) {
            return giftService.queryByUser(title, userID, resultOrder, resultOrderDirection, hide);
        }

        @Override
        public Observable<ArrayList<GiftResult>> queryByTopGiftGivers(String title,
                ResultOrderDirection resultOrderDirection,
                boolean hide) {
            return giftService.queryByTopGiftGivers(title, resultOrderDirection, hide);
        }

        @Override
        public Observable<ArrayList<GiftResult>> queryByGiftChain(String title,
                long giftChainID,
                ResultOrder resultOrder,
                ResultOrderDirection resultOrderDirection,
                boolean hide) {
            return giftService.queryByGiftChain(title,
                    giftChainID,
                    resultOrder,
                    resultOrderDirection,
                    hide);
        }

    }

}
