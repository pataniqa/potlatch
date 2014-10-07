package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import rx.Observable;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface Gifts extends Query<GiftResult>, Retrieve<GiftResult, Long>, SaveDelete<Gift> {

    /**
     * Query gift data by title - corresponds to QueryType.ALL
     * 
     * @param title The title.
     * @param resultOrder
     * @param resultOrderDirection
     * @return an ArrayList of GiftData objects
     */

    Observable<ArrayList<GiftResult>> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);

    /**
     * Query gift data by user - corresponds to QueryType.USER
     * 
     * @param title
     * @param userID
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     */
    Observable<ArrayList<GiftResult>> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);

    /**
     * Query gift data by top gift givers - corresponds to
     * QueryType.TOP_GIFT_GIVERS
     * 
     * @param title
     * @param resultOrderDirection
     * @return
     */
    Observable<ArrayList<GiftResult>> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection);

    /**
     * Query gift data by gift chain.
     * 
     * @param title
     * @param giftChainName
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     */
    Observable<ArrayList<GiftResult>> queryByGiftChain(String title,
            long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection);
}
