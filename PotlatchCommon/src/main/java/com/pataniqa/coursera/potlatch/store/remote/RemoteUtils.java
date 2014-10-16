package com.pataniqa.coursera.potlatch.store.remote;

public class RemoteUtils {
    
    public static String getLoginUrl(String endpoint) {
        return endpoint + RemoteGiftApi.TOKEN_PATH;
    }

}
