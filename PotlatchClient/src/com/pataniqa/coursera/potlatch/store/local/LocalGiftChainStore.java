package com.pataniqa.coursera.potlatch.store.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;

public class LocalGiftChainStore extends BaseStore<GiftChain> implements GiftChainStore {
    
    public LocalGiftChainStore(Context context) {
        creator = new GiftChainCreator();
        id = PotlatchSchema.Cols.ID;
        tableName = PotlatchSchema.GiftChain.TABLE_NAME;
        helper = new SQLiteOpenHelper(context, PotlatchSchema.PARENT_DATABASE, null, 1) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                SQLiteUtils.createDatabase(db, tableName, id, PotlatchSchema.GiftChain.COLUMNS);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // TODO need to add support to upgrade database when schema changes
            }
        };
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }
}
