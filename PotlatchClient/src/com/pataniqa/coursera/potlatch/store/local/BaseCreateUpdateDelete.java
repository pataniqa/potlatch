package com.pataniqa.coursera.potlatch.store.local;

import rx.Observable;
import rx.Subscriber;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.SetId;
import com.pataniqa.coursera.potlatch.store.SaveDelete;
import com.pataniqa.coursera.potlatch.ui.CreateGiftActivity;

abstract class BaseCreateUpdateDelete<T extends SetId> extends BaseQuery<T> implements
        SaveDelete<T> {

    private final static String LOG_TAG = BaseCreateUpdateDelete.class.getCanonicalName();
    
    private static String selection = LocalSchema.Cols.ID + " = ?";

    protected BaseCreateUpdateDelete(Creator<T> creator, String tableName, SQLiteOpenHelper helper) {
        super(creator, tableName, helper);
    }

    @Override
    public <S extends T> Observable<S> save(final S data) {
        return Observable.create(new Observable.OnSubscribe<S>() {
            @Override
            public void call(Subscriber<? super S> subscriber) {
                SQLiteDatabase db = helper().getWritableDatabase();
                if (data.getId() == GetId.UNDEFINED_ID) {
                    ContentValues tempCV = creator().getCV(data);
                    tempCV.remove(LocalSchema.Cols.ID);
                    data.setId(db.insert(tableName(), null, tempCV));
                } else {
                    String[] selectionArgs = { String.valueOf(data.getId()) };
                    db.update(tableName(), creator().getCV(data), selection, selectionArgs);
                }
                Log.i(LOG_TAG, "Stored: " + data);
                db.close();
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> delete(final long rowID) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String[] selectionArgs = { String.valueOf(rowID) };
                SQLiteDatabase db = helper().getWritableDatabase();
                db.delete(tableName(), selection, selectionArgs);
                db.close();
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }
}
