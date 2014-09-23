package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Store;

abstract class BaseStore<T extends HasID> extends BaseQuery<T> implements Store<T> {

    @Override
    public long insert(T data) throws RemoteException {
        ContentValues tempCV = creator.getCV(data);
        tempCV.remove(LocalSchema.Cols.ID);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(tableName, null, tempCV);
        db.close();
        return res;
    }

    @Override
    public void delete(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        String selection = LocalSchema.Cols.ID + " = ? ";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName, selection, selectionArgs);
        db.close();
    }

    @Override
    public void update(T data) throws RemoteException {
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(data.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, creator.getCV(data), selection, selectionArgs);
        db.close();
    }
}
