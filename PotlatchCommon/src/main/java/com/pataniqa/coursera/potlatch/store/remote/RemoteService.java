package com.pataniqa.coursera.potlatch.store.remote;

import java.util.ArrayList;

import lombok.Getter;
import lombok.experimental.Accessors;

import org.apache.http.client.HttpClient;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.HasId;
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.store.CRUD;
import com.pataniqa.coursera.potlatch.store.DataService;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Gifts;

/**
 * A store implementation that connects to the remote store's REST API via
 * Retrofit.
 */
@Accessors(fluent = true)
public class RemoteService implements DataService {

    @Getter private final Gifts gifts;
    @Getter private final CRUD<GiftChain> giftChains;
    @Getter private final GiftMetadata giftMetadata;
    @Getter private final CRUD<User> users;

    private final RemoteGiftApi giftService;
    private final RemoteGiftChainApi giftChainService;
    private final RemoteUserApi userService;

    public RemoteService(HttpClient httpClient,
            String endpoint,
            String username,
            String password,
            String clientId) {
        JacksonConverter converter = new JacksonConverter(new ObjectMapper());

        RestAdapter restAdapter = new SecuredRestBuilder().setClient(new ApacheClient(httpClient))
                .setEndpoint(endpoint).loginUrl(SecuredRestBuilder.getLoginUrl(endpoint))
                .setLogLevel(LogLevel.FULL).username(username).password(password)
                .clientId(clientId).setConverter(converter).build();

        giftService = restAdapter.create(RemoteGiftApi.class);
        giftChainService = restAdapter.create(RemoteGiftChainApi.class);
        userService = restAdapter.create(RemoteUserApi.class);
        gifts = new RemoteGiftQueryService();
        giftChains = new RemoteGiftChainService();
        giftMetadata = new RemoteGiftMetadataService();
        users = new RemoteUserService();
    }

    class RemoteUserService implements CRUD<User> {

        @Override
        public Observable<ArrayList<User>> findAll() {
            return userService.findAll();
        }

        @Override
        public Observable<User> save(User data) {
            if (data.getId() == HasId.UNDEFINED_ID)
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

    class RemoteGiftChainService implements CRUD<GiftChain> {

        @Override
        public Observable<ArrayList<GiftChain>> findAll() {
            return giftChainService.findAll();
        }

        @Override
        public Observable<GiftChain> save(GiftChain data) {
            if (data.getId() == HasId.UNDEFINED_ID)
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
            if (data.getId() == HasId.UNDEFINED_ID)
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
                ResultOrderDirection resultOrderDirection,
                boolean hide) {
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

        @Override
        public Observable<Boolean> setImageData(long id, TypedFile imageData) {
            return giftService.setImageData(id, imageData);
        }

        @Override
        public Observable<Response> getImageData(long id) {
            return giftService.getImageData(id);
        }

        @Override
        public Observable<Boolean> setVideoData(long id, TypedFile videoData) {
            return giftService.setVideoData(id, videoData);
        }

        @Override
        public Observable<Response> getVideoData(long id) {
            return giftService.getVideoData(id);
        }

    }

}
