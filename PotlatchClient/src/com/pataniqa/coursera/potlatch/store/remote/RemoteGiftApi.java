package com.pataniqa.coursera.potlatch.store.remote;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface RemoteGiftApi {
    
    public static final String GIFT_SVC_PATH = "/gift";
    
    @POST(GIFT_SVC_PATH)
    Gift insert(@Body Gift data);
    
    @PUT(GIFT_SVC_PATH + "/{id}")
    void update(long id, @Body Gift data);
    
    @DELETE(GIFT_SVC_PATH + "/{id}")
    void deleteGift(long id);
    
    @PUT(GIFT_SVC_PATH + "/{id}/like/{like}")
    void setLike(long id, boolean like);
    
    @PUT(GIFT_SVC_PATH + "/{id}/flag/{flag}")
    void setFlag(long id, boolean flag);
    
    @GET(GIFT_SVC_PATH + "/{id}")
    GiftResult findOne(Long id);

    @GET(GIFT_SVC_PATH)
    List<GiftResult> findAll();

    @GET(GIFT_SVC_PATH + 
            "/queryTitle?title={title}&resultorder={order}&direction={direction}")
    List<GiftResult> queryByTitle(String title,
            int order,
            int direction);

    @GET(GIFT_SVC_PATH + 
            "/queryUser?user={userID}&title={title}&resultorder={order}&direction={direction}")
    List<GiftResult> queryByUser(String title,
            long userID,
            int order,
            int direction);

    @GET(GIFT_SVC_PATH + "" +
            "/queryTopGiftGivers?title={title}&direction={direction}")
    List<GiftResult> queryByTopGiftGivers(String title,
            int direction);

    @GET(GIFT_SVC_PATH + 
            "/queryGiftChain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}")
    List<GiftResult> queryByGiftChain(String title,
            String giftChain,
            int order,
            int direction);

}
