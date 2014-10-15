package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.store.Query;

@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class BaseQuery<T> implements Query<T> {

    @Getter private final Creator<T> creator;
    @Getter private final String tableName;
    @Getter private final SQLiteOpenHelper helper;

    protected Observable<ArrayList<T>> query(final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<T>>() {
            @Override
            public void call(Subscriber<? super ArrayList<T>> subscriber) {
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
                subscriber.onNext(rValue);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<ArrayList<T>> findAll() {
        return query(null, null, null, null);
    }

    @Override
    public Observable<T> findOne(long rowID) {
        String[] selectionArgs = { String.valueOf(rowID) };
        Observable<ArrayList<T>> results = query(null,
                LocalSchema.Cols.ID + "= ?",
                selectionArgs,
                null);
        return results.map(new Func1<ArrayList<T>, T>() {
            @Override
            public T call(ArrayList<T> results) {
                return results.size() > 0 ? results.iterator().next() : null;
            }
        });

    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }
}
