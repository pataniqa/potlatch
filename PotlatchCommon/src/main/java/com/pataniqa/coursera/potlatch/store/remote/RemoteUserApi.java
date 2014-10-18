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

import com.pataniqa.coursera.potlatch.model.User;

/**
 * Retrofit User REST API definition.
 */
public interface RemoteUserApi {
    
    static final String USER_PATH = "/user";
    
    static final String USER_ID_PATH = "/user/{id}";
    
    @POST(USER_PATH)
    Observable<User> insert(@Body User data);
    
    @GET(USER_PATH)
    Observable<ArrayList<User>> findAll();
    
    @GET(USER_ID_PATH)
    Observable<User> findOne(@Path(ID) long id);

    @PUT(USER_ID_PATH)
    Observable<User> update(@Path(ID) long id, @Body User user);

    @DELETE(USER_ID_PATH)
    Observable<Boolean> delete(@Path(ID) long id);
}
