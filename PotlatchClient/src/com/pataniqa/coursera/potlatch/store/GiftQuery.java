package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface GiftQuery extends Query<ClientGift>, Retrieve<ClientGift, Long> {

    enum ResultOrder {
        TIME, LIKES
    };

    enum ResultOrderDirection {
        ASCENDING, DESCENDING
    };

    enum QueryType {
        USER, TOP_GIFT_GIVERS, ALL, CHAIN
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
     * @param title
     * @param userID
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByUser(String title, 
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;

    /**
     * Query gift data by top gift givers - corresponds to
     * QueryType.TOP_GIFT_GIVERS
     * 
     * @param title
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection)
            throws RemoteException;

    /**
     * Query gift data by gift chain.
     * 
     * @param title
     * @param giftChainName
     * @param resultOrder
     * @param resultOrderDirection
     * @return
     * @throws RemoteException
     */
    ArrayList<ClientGift> queryByGiftChain(String title, String giftChainName,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;
}
