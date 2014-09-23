package com.pataniqa.coursera.potlatch.store.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, PotlatchSchema.PARENT_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteUtils.createDatabase(db,
                PotlatchSchema.Gift.TABLE_NAME,
                PotlatchSchema.Cols.ID,
                PotlatchSchema.Gift.COLUMNS);
        SQLiteUtils.createDatabase(db,
                PotlatchSchema.GiftChain.TABLE_NAME,
                PotlatchSchema.Cols.ID,
                PotlatchSchema.GiftChain.COLUMNS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO need to add support to upgrade database when schema
        // changes
    }
}
