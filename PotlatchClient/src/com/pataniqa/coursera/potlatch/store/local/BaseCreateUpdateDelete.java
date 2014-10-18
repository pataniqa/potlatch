package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pataniqa.coursera.potlatch.model.HasId;

abstract class BaseCreateUpdateDelete<T extends HasId> extends BaseQuery<T> implements LocalSaveDelete<T> {

    private final static String LOG_TAG = BaseCreateUpdateDelete.class.getCanonicalName();

    private static String selection = LocalSchema.Cols.ID + " = ?";

    BaseCreateUpdateDelete(Creator<T> creator, String tableName, SQLiteOpenHelper helper) {
        super(creator, tableName, helper);
    }

    @Override
    public <S extends T> S save(final S data) {
        if (data.getId() == HasId.UNDEFINED_ID) {
            ContentValues tempCV = creator().getCV(data);
            tempCV.remove(LocalSchema.Cols.ID);
            SQLiteDatabase db = helper().getWritableDatabase();
            data.setId(db.insert(tableName(), null, tempCV));
            db.close();
        } else {
            String[] selectionArgs = { String.valueOf(data.getId()) };
            ContentValues creator = creator().getCV(data);
            SQLiteDatabase db = helper().getWritableDatabase();
            db.update(tableName(), creator, selection, selectionArgs);
            db.close();
        }
        Log.d(LOG_TAG, "Stored: " + data);
        return data;
    }

    @Override
    public Boolean delete(final long rowID) {
        String[] selectionArgs = { String.valueOf(rowID) };
        SQLiteDatabase db = helper().getWritableDatabase();
        db.delete(tableName(), selection, selectionArgs);
        db.close();
        return true;
    }
}
