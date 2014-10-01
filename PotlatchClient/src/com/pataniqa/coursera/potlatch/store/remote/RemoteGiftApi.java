package com.pataniqa.coursera.potlatch.store.remote;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface RemoteGiftApi {

    static final String DATA_PARAMETER = "data";

    static final String ID_PARAMETER = "id";

    static final String GIFT_PATH = "/gift";

    @GET(GIFT_PATH)
    List<GiftResult> findAll();

    @POST(GIFT_PATH)
    Gift insert(@Body Gift data);

    static final String GIFT_ID_PATH = "/gift/{id}";

    @PUT(GIFT_ID_PATH)
    void update(long id, @Body Gift data);

    @DELETE(GIFT_ID_PATH)
    void delete(long id);

    @GET(GIFT_ID_PATH)
    GiftResult findOne(Long id);

    static final String GIFT_LIKE_PATH = "/gift/{id}/like/{like}";

    @PUT(GIFT_LIKE_PATH)
    void setLike(long id, boolean like);

    static final String GIFT_FLAG_PATH = "/gift/{id}/like/{flag}";

    @PUT(GIFT_FLAG_PATH)
    void setFlag(long id, boolean flag);

    static final String QUERY_BY_TITLE = "/gift/title?title={title}&resultorder={order}&direction={direction}";

    @GET(QUERY_BY_TITLE)
    List<GiftResult> queryByTitle(String title, int order, int direction);

    static final String QUERY_BY_USER = "/gift/user?user={userID}&title={title}&resultorder={order}&direction={direction}";

    @GET(QUERY_BY_USER)
    List<GiftResult> queryByUser(String title, long userID, int order, int direction);

    static final String QUERY_BY_TOP_GIFT_GIVERS = "/gift/topGiftGivers?title={title}&direction={direction}";

    @GET(QUERY_BY_TOP_GIFT_GIVERS)
    List<GiftResult> queryByTopGiftGivers(String title, int direction);

    static final String QUERY_BY_GIFT_CHAIN = "/gift/giftchain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}";

    @GET(QUERY_BY_GIFT_CHAIN)
    List<GiftResult> queryByGiftChain(String title, String giftChain, int order, int direction);

    static final String GIFT_IMAGE_PATH = "/{id}/image";

    @Multipart
    @POST(GIFT_IMAGE_PATH)
    ResourceStatus setImageData(@Path(ID_PARAMETER) long id,
            @Part(DATA_PARAMETER) TypedFile imageData);

    @Streaming
    @GET(GIFT_IMAGE_PATH)
    Response getImageData(@Path(ID_PARAMETER) long id);

    static final String GIFT_VIDEO_PATH = "/gift/{id}/video";

    @Multipart
    @POST(GIFT_VIDEO_PATH)
    ResourceStatus setVideoData(@Path(ID_PARAMETER) long id,
            @Part(DATA_PARAMETER) TypedFile imageData);

    @Streaming
    @GET(GIFT_VIDEO_PATH)
    Response getVideoData(@Path(ID_PARAMETER) long id);

}
