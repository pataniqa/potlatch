package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
abstract class BaseQuery<T> implements LocalQuery<T> {

    private final Creator<T> creator;
    private final String tableName;
    private final SQLiteOpenHelper helper;

    ArrayList<T> query(final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) {
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
    public ArrayList<T> findAll() {
        return query(null, null, null, null);
    }

    @Override
    public T findOne(long rowID) {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<T> results = query(null, LocalSchema.Cols.ID + "= ?", selectionArgs, null);
        return results.size() > 0 ? results.iterator().next() : null;
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }
}
