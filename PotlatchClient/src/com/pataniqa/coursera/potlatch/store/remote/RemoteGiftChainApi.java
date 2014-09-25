package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

import com.pataniqa.coursera.potlatch.model.GiftChain;

public interface RemoteGiftChainApi {
    
    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";
    
    @POST(GIFT_CHAIN_SVC_PATH)
    GiftChain insert(@Body GiftChain data);

    @PUT(GIFT_CHAIN_SVC_PATH + "/{id}")
    void update(long id, @Body GiftChain data);

    @DELETE(GIFT_CHAIN_SVC_PATH + "/{id}")
    void deleteGiftChain(long id);

    @GET(GIFT_CHAIN_SVC_PATH)
    Collection<GiftChain> findAll();

}
