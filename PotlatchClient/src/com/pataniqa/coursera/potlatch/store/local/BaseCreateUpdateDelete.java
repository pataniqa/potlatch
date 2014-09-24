package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Create;
import com.pataniqa.coursera.potlatch.store.Delete;
import com.pataniqa.coursera.potlatch.store.Update;

abstract class BaseCreateUpdateDelete<T extends HasID> extends BaseQuery<T> implements Create<T>,
        Update<T>, Delete<T> {

    @Override
    public T insert(T data) throws RemoteException {
        ContentValues tempCV = creator.getCV(data);
        tempCV.remove(LocalSchema.Cols.ID);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(tableName, null, tempCV);
        db.close();
        data.setID(res);
        return data;
    }

    @Override
    public void update(T data) throws RemoteException {
        String selection = LocalSchema.Cols.ID  + " = ?";
        String[] selectionArgs = { String.valueOf(data.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, creator.getCV(data), selection, selectionArgs);
        db.close();
    }

    @Override
    public void delete(long rowID) throws RemoteException {
        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(rowID) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName, selection, selectionArgs);
        db.close();
    }
}
