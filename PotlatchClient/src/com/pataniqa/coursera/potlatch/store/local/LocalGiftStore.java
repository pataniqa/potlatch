package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftStore;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 * 
 */
public class LocalGiftStore extends BaseStore<ClientGift> implements GiftStore {

    public LocalGiftStore(DatabaseHelper helper) {
        creator = new GiftCreator();
        id = PotlatchSchema.Cols.ID;
        tableName = PotlatchSchema.Gift.TABLE_NAME;
        this.helper = helper;
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }

    String sortOrder(ResultOrder resultOrder, ResultOrderDirection resultOrderDirection) {
        String sortCol = resultOrder == ResultOrder.LIKES ? PotlatchSchema.Cols.LIKES
                : PotlatchSchema.Cols.CREATED;
        String order = resultOrderDirection == ResultOrderDirection.ASCENDING ? "ASC" : "DESC";
        return sortCol + " " + order;
    }

    @Override
    public ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {

        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        if (title == null || title.isEmpty())
            return query(null, null, null, sortOrder);
        else {
            String filterWord = "%" + title + "%";
            return query(null,
                    PotlatchSchema.Cols.TITLE + " LIKE ? ",
                    new String[] { filterWord },
                    sortOrder);
        }
    }

    @Override
    public ArrayList<ClientGift> queryByUser(long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        String[] selectionArgs = { String.valueOf(userID) };
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        return query(null, PotlatchSchema.Cols.USER_ID + "= ?", selectionArgs, sortOrder);
    }

    @Override
    public ArrayList<ClientGift> queryByTopGiftGivers(ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO need to add support to query by top gift givers
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByGiftChain(long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        String[] selectionArgs = { String.valueOf(giftChainID) };
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        return query(null, PotlatchSchema.Cols.GIFT_CHAIN_ID + "= ?", selectionArgs, sortOrder);
    }

}