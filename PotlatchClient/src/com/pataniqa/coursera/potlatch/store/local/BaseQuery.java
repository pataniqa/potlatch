package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.store.Query;
import com.pataniqa.coursera.potlatch.store.Retrieve;

@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class BaseQuery<T> implements Query<T>, Retrieve<T, Long> {

    @Getter private final Creator<T> creator;
    @Getter private final String tableName;
    @Getter private final SQLiteOpenHelper helper;
    
    protected ArrayList<T> query(String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
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
    public T findOne(Long rowID) {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<T> results = query(null, LocalSchema.Cols.ID + "= ?", selectionArgs, null);
        return results.size() > 0 ? results.get(0) : null;
    }
    
    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }
}
