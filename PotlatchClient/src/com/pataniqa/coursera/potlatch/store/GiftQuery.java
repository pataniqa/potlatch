package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface GiftQuery extends Query<GiftResult>, Retrieve<GiftResult, Long> {

    /**
     * Query gift data by title - corresponds to QueryType.ALL
     * 
     * @param title The title.
     * @param resultOrder
     * @param resultOrderDirection
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */

    ArrayList<GiftResult> queryByTitle(String title,
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
    ArrayList<GiftResult> queryByUser(String title, 
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
    ArrayList<GiftResult> queryByTopGiftGivers(String title,
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
    ArrayList<GiftResult> queryByGiftChain(String title, String giftChainName,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException;
}
