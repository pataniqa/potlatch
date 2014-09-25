package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;

public interface RemoteApi {
    
    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";
    public static final String GIFT_SVC_PATH = "/gift";
    
    @POST(GIFT_SVC_PATH)
    Gift insert(@Body Gift data);
    
    @PUT(GIFT_SVC_PATH + "/{id}")
    void update(long id, @Body Gift data);
    
    @DELETE(GIFT_SVC_PATH + "/{id}")
    void deleteGift(long id);
    
    @PUT(GIFT_SVC_PATH + "/{id}/like/{userID}/{like}")
    void setLike(long id, long userID, boolean like);
    
    @PUT(GIFT_SVC_PATH + "/{id}/flag/{userID}/{like}")
    void setFlag(long id, long userID, boolean like);
    
    @GET(GIFT_SVC_PATH + "/{id}")
    ClientGift findOne(Long id);

    @GET(GIFT_SVC_PATH)
    List<ClientGift> findAllGifts();

    @GET(GIFT_SVC_PATH + 
            "/queryTitle?title={title}&resultorder={order}&direction={direction}")
    List<ClientGift> queryByTitle(String title,
            ResultOrder order,
            ResultOrderDirection direction);

    @GET(GIFT_SVC_PATH + 
            "/queryUser?user={userID}&title={title}&resultorder={order}&direction={direction}")
    List<ClientGift> queryByUser(String title,
            long userID,
            ResultOrder order,
            ResultOrderDirection direction);

    @GET(GIFT_SVC_PATH + "" +
            "/queryTopGiftGivers?title={title}&direction={direction}")
    List<ClientGift> queryByTopGiftGivers(String title,
            ResultOrderDirection direction);

    @GET(GIFT_SVC_PATH + 
            "/queryGiftChain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}")
    List<ClientGift> queryByGiftChain(String title,
            String giftChain,
            ResultOrder order,
            ResultOrderDirection direction);
    
    @POST(GIFT_CHAIN_SVC_PATH)
    GiftChain insert(@Body GiftChain data);

    @PUT(GIFT_CHAIN_SVC_PATH + "/{id}")
    void update(long id, @Body GiftChain data);

    @DELETE(GIFT_CHAIN_SVC_PATH + "/{id}")
    void deleteGiftChain(long id);

    @GET(GIFT_CHAIN_SVC_PATH)
    Collection<GiftChain> findAllGiftChains();

}
