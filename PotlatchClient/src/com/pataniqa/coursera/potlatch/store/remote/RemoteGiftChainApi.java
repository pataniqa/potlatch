package com.pataniqa.coursera.potlatch.store.remote;

import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.ID;

import java.util.ArrayList;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

import com.pataniqa.coursera.potlatch.model.GiftChain;

public interface RemoteGiftChainApi {

    static final String GIFT_CHAIN_PATH = "/giftchain";

    static final String GIFT_CHAIN_ID_PATH = "/giftchain/{id}";

    @POST(GIFT_CHAIN_PATH)
    Observable<GiftChain> insert(@Body GiftChain data);

    @GET(GIFT_CHAIN_PATH)
    Observable<ArrayList<GiftChain>> findAll();

    @PUT(GIFT_CHAIN_ID_PATH)
    Observable<GiftChain> update(@Path(ID) long id, @Body GiftChain data);

    @DELETE(GIFT_CHAIN_ID_PATH)
    Observable<Boolean> delete(@Path(ID) long id);

}
