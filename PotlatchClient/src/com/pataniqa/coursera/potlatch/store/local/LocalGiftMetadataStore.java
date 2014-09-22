package com.pataniqa.coursera.potlatch.store.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.GiftMetadataStore;

public class LocalGiftMetadataStore extends BaseStore<GiftMetadata> implements GiftMetadataStore {

    public LocalGiftMetadataStore(Context context) {
        creator = new GiftMetadataCreator();
        id = PotlatchSchema.GiftMetadata.Cols.ID;
        tableName = PotlatchSchema.GiftMetadata.TABLE_NAME;
        helper = new SQLiteOpenHelper(context, PotlatchSchema.PARENT_DATABASE, null, 1) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                SQLiteUtils.createDatabase(db, tableName, id, PotlatchSchema.GiftMetadata.COLUMNS);
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

}