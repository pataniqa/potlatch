package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Store;

public class BaseStore<T extends HasID> extends BaseQuery<T> implements Store<T> {

    @Override
    public long insert(T data) throws RemoteException {
        ContentValues tempCV = creator.getCV(data);
        tempCV.remove(id);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(tableName, null, tempCV);
        db.close();
        return res;
    }

    @Override
    public int delete(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        String selection = id + " = ? ";
        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.delete(tableName, selection, selectionArgs);
        db.close();
        return res;
    }

    @Override
    public int update(T data) throws RemoteException {
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(data.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.update(tableName, creator.getCV(data), selection, selectionArgs);
        db.close();
        return res;
    }
}