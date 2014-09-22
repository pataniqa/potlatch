package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.database.Cursor;

abstract class BaseCreator<T> implements Creator<T> {
    
    @Override
    public ArrayList<T> getListFromCursor(Cursor cursor) {
        ArrayList<T> rValue = new ArrayList<T>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    rValue.add(getFromCursor(cursor));
                } while (cursor.moveToNext() == true);
            }
        }
        return rValue;
    }

}
