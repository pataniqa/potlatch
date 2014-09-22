package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftStore;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 * 
 */
public class LocalGiftStore extends BaseStore<ClientGift> implements GiftStore {

    public LocalGiftStore(Context context) {
        creator = new GiftCreator();
        id = PotlatchSchema.Gift.Cols.ID;
        tableName = PotlatchSchema.Gift.TABLE_NAME;
        helper = new SQLiteOpenHelper(context, PotlatchSchema.PARENT_DATABASE, null, 1) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                SQLiteUtils.createDatabase(db, tableName, id, PotlatchSchema.Gift.COLUMNS);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // TODO
            }
        };
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }

    @Override
    public ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        
        // TODO need to use resultOrder / resultOrderDirection
        
        if (title == null || title.isEmpty())
            return query(null, null, null, null);
        else {
            String filterWord = "%" + title + "%";
            return query(null,
                    PotlatchSchema.Gift.Cols.TITLE + " LIKE ? ",
                    new String[] { filterWord },
                    null);
        }
    }

    @Override
    public ArrayList<ClientGift> queryByUser(long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByTopGiftGivers(ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ClientGift> queryByGiftChain(long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

}