package com.pataniqa.coursera.potlatch.server
import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import com.pataniqa.coursera.potlatch.model.User
import com.pataniqa.coursera.potlatch.server.UnsafeHttpsClient
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder

class ApiSpecHelper {
    
    def TEST_URL = "https://localhost:8443"
    def USERNAME1 = "user0"
    def PASSWORD = "pass"
    def CLIENT_ID = "mobile"
    
    def svcUser = new SecuredRestBuilder()
        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(TEST_URL)
        .loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
        .setLogLevel(LogLevel.FULL)
        .username(USERNAME1)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .build()
}

