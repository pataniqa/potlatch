package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.store.Save;
import com.pataniqa.coursera.potlatch.store.Delete;

abstract class BaseCreateUpdateDelete<T extends HasID> extends BaseQuery<T> implements Save<T>,
        Delete<T> {

    @Override
    public <S extends T> S save(S data) throws RemoteException {
        if (data.getId() == HasID.UNDEFINED_ID) {
            ContentValues tempCV = creator.getCV(data);
            tempCV.remove(LocalSchema.Cols.ID);
            SQLiteDatabase db = helper.getWritableDatabase();
            long res = db.insert(tableName, null, tempCV);
            db.close();
            data.setId(res);
        } else {
            String selection = LocalSchema.Cols.ID + " = ?";
            String[] selectionArgs = { String.valueOf(data.getId()) };
            SQLiteDatabase db = helper.getWritableDatabase();
            db.update(tableName, creator.getCV(data), selection, selectionArgs);
            db.close();
        }
        return data;
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
