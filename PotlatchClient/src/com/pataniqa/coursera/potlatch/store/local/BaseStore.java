package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Store;

public class BaseStore<T extends HasID> implements Store<T> {

    protected Creator<T> creator;
    protected String tableName;
    protected String id;
    protected SQLiteOpenHelper helper;

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
    public T get(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<T> results = query(null, id + "= ?", selectionArgs, null);
        return results.size() > 0 ? results.get(0) : null;
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

    @Override
    public ArrayList<T> query(String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) throws RemoteException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        ArrayList<T> rValue = new ArrayList<T>();
        rValue.addAll(creator.getListFromCursor(result));
        result.close();
        db.close();
        return rValue;
    }

    @Override
    public ArrayList<T> query() throws RemoteException {
        return query(null, null, null, null);
    }

}
