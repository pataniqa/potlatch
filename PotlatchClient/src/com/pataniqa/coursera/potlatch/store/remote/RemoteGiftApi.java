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
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;

public interface RemoteGiftApi {

    static final String TOKEN_PATH = "/oauth/token";

    static final String GIFT_PATH = "/gift";

    @GET(GIFT_PATH)
    List<GiftResult> findAll();

    @POST(GIFT_PATH)
    Gift insert(@Body Gift data);

    static final String GIFT_ID_PATH = "/gift/{id}";
    static final String ID = "id";

    @PUT(GIFT_ID_PATH)
    Gift update(@Path(ID) long id, @Body Gift data);

    @DELETE(GIFT_ID_PATH)
    boolean delete(@Path(ID) long id);

    @GET(GIFT_ID_PATH)
    GiftResult findOne(@Path(ID) long id);

    static final String GIFT_LIKE_PATH = "/gift/{id}/like/{like}";
    static final String LIKE = "like";

    @PUT(GIFT_LIKE_PATH)
    boolean setLike(@Path(ID) long id, @Path(LIKE) boolean like);

    static final String GIFT_FLAG_PATH = "/gift/{id}/flag/{flag}";
    static final String FLAG = "flag";

    @PUT(GIFT_FLAG_PATH)
    boolean setFlag(@Path(ID) long id, @Path(FLAG) boolean flag);

    static final String QUERY_BY_TITLE = "/gift/title";
    static final String TITLE = "title";
    static final String ORDER = "order";
    static final String DIRECTION = "direction";

    @GET(QUERY_BY_TITLE)
    List<GiftResult> queryByTitle(@Query(TITLE) String title,
            @Query(ORDER) ResultOrder order,
            @Query(DIRECTION) ResultOrderDirection direction);

    static final String QUERY_BY_USER = "/gift/user";

    static final String USER = "user";

    @GET(QUERY_BY_USER)
    List<GiftResult> queryByUser(@Query(TITLE) String title,
            @Query(USER) long userID,
            @Query(ORDER) ResultOrder order,
            @Query(DIRECTION) ResultOrderDirection direction);

    static final String QUERY_BY_TOP_GIFT_GIVERS = "/gift/topGivers";

    @GET(QUERY_BY_TOP_GIFT_GIVERS)
    List<GiftResult> queryByTopGiftGivers(@Query(TITLE) String title,
            @Query(DIRECTION) ResultOrderDirection direction);

    static final String QUERY_BY_GIFT_CHAIN = "/gift/giftchain";

    static final String GIFT_CHAIN = "giftchain";

    @GET(QUERY_BY_GIFT_CHAIN)
    List<GiftResult> queryByGiftChain(@Query(TITLE) String title,
            @Query(GIFT_CHAIN) long giftChainID,
            @Query(ORDER) ResultOrder order,
            @Query(DIRECTION) ResultOrderDirection direction);

    static final String GIFT_IMAGE_PATH = "/{id}/image";
    static final String DATA = "data";

    @Multipart
    @POST(GIFT_IMAGE_PATH)
    ResourceStatus setImageData(@Path(ID) long id,
            @Part(DATA) TypedFile imageData);

    @Streaming
    @GET(GIFT_IMAGE_PATH)
    Response getImageData(@Path(ID) long id);

    static final String GIFT_VIDEO_PATH = "/gift/{id}/video";

    @Multipart
    @POST(GIFT_VIDEO_PATH)
    ResourceStatus setVideoData(@Path(ID) long id,
            @Part(DATA) TypedFile imageData);

    @Streaming
    @GET(GIFT_VIDEO_PATH)
    Response getVideoData(@Path(ID) long id);

}
