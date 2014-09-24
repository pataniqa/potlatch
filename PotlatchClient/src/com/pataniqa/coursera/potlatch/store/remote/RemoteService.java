package com.pataniqa.coursera.potlatch.store.remote;

import retrofit.RestAdapter;

import com.pataniqa.coursera.potlatch.store.BaseService;
import com.pataniqa.coursera.potlatch.store.Service;

public class RemoteService extends BaseService implements Service {
    
    public RemoteService(String endpoint) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(endpoint).build();
        userGifts = new RemoteGiftService(restAdapter);
        gifts = new RemoteGiftQueryService(restAdapter);
        giftChains = new RemoteGiftChainService(restAdapter);
        giftMetadata = new RemoteGiftMetadataService(restAdapter);
    }

}
