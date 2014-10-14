package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import rx.Observable;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface Gifts extends Query<GiftResult>, SaveDelete<Gift> {

    Observable<ArrayList<GiftResult>> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    Observable<ArrayList<GiftResult>> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection, 
            boolean hideFlaggedContent);

    Observable<ArrayList<GiftResult>> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    Observable<ArrayList<GiftResult>> queryByGiftChain(String title,
            long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);
}
