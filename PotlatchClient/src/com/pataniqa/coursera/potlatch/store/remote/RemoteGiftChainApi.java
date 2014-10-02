package com.pataniqa.coursera.potlatch.store.remote;

import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.ID_PARAMETER;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import com.pataniqa.coursera.potlatch.model.GiftChain;

public interface RemoteGiftChainApi {
    
    static final String GIFT_CHAIN_PATH = "/giftchain";
    
    static final String GIFT_CHAIN_ID_PATH = "/giftchain/{id}";
    
    @POST(GIFT_CHAIN_PATH)
    GiftChain insert(@Body GiftChain data);
    
    @GET(GIFT_CHAIN_PATH)
    Collection<GiftChain> findAll();

    @PUT(GIFT_CHAIN_ID_PATH)
    void update(@Path(ID_PARAMETER) long id, @Body GiftChain data);

    @DELETE(GIFT_CHAIN_ID_PATH)
    void delete(@Path(ID_PARAMETER) long id);

}
