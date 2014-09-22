package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface GiftStore extends Store<ClientGift> {

    enum ResultOrder {
        TIME, LIKES
    };

    enum ResultOrderDirection {
        ASCENDING, DESCENDING
    };

    enum QueryType {
        USER, TOP_GIFT_GIVERS, ALL
    };

    /**
     * Query gift data by title - corresponds to QueryType.ALL
     * 
     * @param title The title.
     * @param resultOrder
     * @param resultOrderDirection
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */

    ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    /**
     * Query gift data by user - corresponds to QueryType.USER
     * 
     * @param userID
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByUser(long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    /**
     * Query gift data by top gift givers - corresponds to
     * QueryType.TOP_GIFT_GIVERS
     * 
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByTopGiftGivers(ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    /**
     * Queyr gift data by gift chain.
     * 
     * @param giftChainID
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByGiftChain(long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;
}
