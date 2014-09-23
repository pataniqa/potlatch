package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Query;

class BaseQuery<T extends HasID> extends Base<T> implements Query<T> {

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

    @Override
    public T get(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<T> results = query(null, id + "= ?", selectionArgs, null);
        return results.size() > 0 ? results.get(0) : null;
    }
}
