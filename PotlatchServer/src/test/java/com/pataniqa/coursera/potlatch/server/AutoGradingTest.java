package com.pataniqa.coursera.potlatch.server;

import static org.junit.Assert.assertEquals;

import java.util.List;

import lombok.Getter;

import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi;
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder;

public class AutoGradingTest {

    private class ErrorRecorder implements ErrorHandler {

        @Getter private RetrofitError error;

        @Override
        public Throwable handleError(RetrofitError cause) {
            error = cause;
            return error.getCause();
        }

    }

    private final String TEST_URL = "https://localhost:8443";

    private final String USERNAME1 = "admin";
    private final String USERNAME2 = "user0";
    private final String PASSWORD = "pass";
    private final String CLIENT_ID = "mobile";

    private RemoteGiftApi readWriteVideoSvcUser1 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
            .setLogLevel(LogLevel.FULL).username(USERNAME1).password(PASSWORD).clientId(CLIENT_ID)
            .build().create(RemoteGiftApi.class);

    private RemoteGiftApi readWriteVideoSvcUser2 = new SecuredRestBuilder()
            .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
            .setEndpoint(TEST_URL).loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
            // .setLogLevel(LogLevel.FULL)
            .username(USERNAME2).password(PASSWORD).clientId(CLIENT_ID).build()
            .create(RemoteGiftApi.class);

    private Gift gift = TestData.randomGift();
        
    @Test
    public void testGetGifts() throws Exception {
        List<GiftResult> gifts = readWriteVideoSvcUser1.findAll();
        assertEquals(0, gifts.size());
    }

    @Test
    public void testAddGift() throws Exception {
        Gift received = readWriteVideoSvcUser1.insert(gift);
        assertEquals(gift.getTitle(), received.getTitle());
        assertEquals(gift.getDescription(), received.getDescription());
        assertEquals(gift.getGiftChainID(), received.getGiftChainID());
        assertEquals(gift.getUserID(), received.getUserID());
    }

}
