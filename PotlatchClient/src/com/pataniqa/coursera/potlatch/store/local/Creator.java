package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

interface Creator<T> {
    /**
     * Create a ContentValues from a provided <T>
     * 
     * @param data <T> to be converted.
     * @return ContentValues that is created from the <T> object
     */
    ContentValues getCV(final T data);
    
    /**
     * Get all of the <T> from the passed in cursor.
     * 
     * @param cursor passed in cursor to get <T>(s) of.
     * @return ArrayList<T> The set of <T>
     */
    ArrayList<T> getListFromCursor(Cursor cursor);
    
    /**
     * Get the first <T> from the passed in cursor.
     * 
     * @param cursor passed in cursor
     * @return <T> object
     */
    T getFromCursor(Cursor cursor);
}

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

