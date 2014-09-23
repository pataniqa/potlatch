package com.pataniqa.coursera.potlatch.store.local;

import android.content.Context;

import com.pataniqa.coursera.potlatch.store.BaseService;
import com.pataniqa.coursera.potlatch.store.Service;

public class LocalService extends BaseService implements Service {

    public LocalService(Context context) {
        LocalDatabase helper = new LocalDatabase(context);
        userGifts = new LocalGiftStore(helper);
        gifts = new LocalGiftQuery(helper);
        giftChains = new LocalGiftChainStore(helper);
        giftMetadata = new LocalGiftMetadataStore(helper);
    }
}
