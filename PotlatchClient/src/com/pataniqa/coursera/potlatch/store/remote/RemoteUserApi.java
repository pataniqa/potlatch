package com.pataniqa.coursera.potlatch.store.remote;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

import com.pataniqa.coursera.potlatch.model.User;

public interface RemoteUserApi {
    
    static final String USER_PATH = "/user";
    
    static final String USER_ID_PATH = "/user/{id}";
    
    @POST(USER_PATH)
    User insert(@Body User data);
    
    @GET(USER_PATH)
    Collection<User> findAll();

    @PUT(USER_ID_PATH)
    void update(long id, @Body User user);

    @DELETE(USER_ID_PATH)
    void delete(long id);
}
