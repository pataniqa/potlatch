package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.SetId;
import com.pataniqa.coursera.potlatch.store.SaveDelete;

abstract class BaseCreateUpdateDelete<T extends SetId> extends BaseQuery<T> implements
        SaveDelete<T> {

    private static String selection = LocalSchema.Cols.ID + " = ?";
    
    protected BaseCreateUpdateDelete(Creator<T> creator, String tableName, SQLiteOpenHelper helper) {
        super(creator, tableName, helper);
    }
    
    @Override
    public <S extends T> S save(S data) throws RemoteException {
        if (data.getId() == GetId.UNDEFINED_ID) {
            ContentValues tempCV = creator().getCV(data);
            tempCV.remove(LocalSchema.Cols.ID);
            SQLiteDatabase db = helper().getWritableDatabase();
            long res = db.insert(tableName(), null, tempCV);
            db.close();
            data.setId(res);
        } else {
            String[] selectionArgs = { String.valueOf(data.getId()) };
            SQLiteDatabase db = helper().getWritableDatabase();
            db.update(tableName(), creator().getCV(data), selection, selectionArgs);
            db.close();
        }
        return data;
    }

    @Override
    public void delete(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        SQLiteDatabase db = helper().getWritableDatabase();
        db.delete(tableName(), selection, selectionArgs);
        db.close();
    }
}
