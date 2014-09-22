package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.HasID;

public interface Creator<T extends HasID> {
    /**
     * Create a ContentValues from a provided <T>
     * 
     * @param data <T> to be converted.
     * @return ContentValues that is created from the <T> object
     */
    public ContentValues getCV(final T data);
    
    /**
     * Get all of the <T> from the passed in cursor.
     * 
     * @param cursor passed in cursor to get <T>(s) of.
     * @return ArrayList<T> The set of <T>
     */
    public ArrayList<T> getListFromCursor(Cursor cursor);
    
    /**
     * Get the first <T> from the passed in cursor.
     * 
     * @param cursor passed in cursor
     * @return <T> object
     */
    public T getFromCursor(Cursor cursor);
}
