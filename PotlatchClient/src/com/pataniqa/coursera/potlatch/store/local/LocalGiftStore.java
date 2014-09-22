package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.GiftStore;

/**
 * This used to be a wrapper for a ContentResolver to a single URI, which would
 * be used to interface with a ContentProvider which communicates with a server
 * on the web.
 * 
 * However, to simplify this assignment and remove any dependencies on the
 * database server being online, we removed the ContentResolver and instead are
 * storing GiftData in the default SQLite Database that is hosted by the device.
 * 
 */
public class LocalGiftStore implements GiftStore {

    private static final String tableName = "PotlatchTable";

    // A private instance of our locally defined mSQLiteOpenHelper
    private mSQLiteOpenHelper helper;
    
    private GiftCreator creator = new GiftCreator();

    // An extension of SQLiteOpenHelper which helps us create and manage
    // an SQLiteDatabase
    static class mSQLiteOpenHelper extends SQLiteOpenHelper {

        // Simply forward construction to the super class
        public mSQLiteOpenHelper(Context context) {
            super(context, "PotlatchSecurityDatabase", null, 1);
        }

        // When the database is created, create a table to store our Gift data,
        // if it does not exist.
        @Override
        public void onCreate(SQLiteDatabase db) {
            SQLiteUtils.createDatabase(db,
                    tableName,
                    PotlatchSchema.Gift.Cols.ID,
                    PotlatchSchema.Gift.COLUMNS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }

    }

    public LocalGiftStore(Context context) {
        helper = new mSQLiteOpenHelper(context);
    }

    /**
     * When the user of this class is done with it, make sure to close the
     * database.
     */
    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }

    @Override
    public long insert(final GiftData gift) throws RemoteException {
        ContentValues tempCV = creator.getCV(gift);
        tempCV.remove(PotlatchSchema.Gift.Cols.ID);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(tableName, null, tempCV);
        db.close();
        return res;
    }

    @Override
    public ArrayList<GiftData> query(final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) throws RemoteException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        ArrayList<GiftData> rValue = new ArrayList<GiftData>();
        rValue.addAll(creator.getListFromCursor(result));
        result.close();
        db.close();
        return rValue;
    }

    @Override
    public GiftData get(final long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<GiftData> results = query(null,
                PotlatchSchema.Gift.Cols.ID + "= ?",
                selectionArgs,
                null);
        return results.size() > 0 ? results.get(0) : null;
    }

    @Override
    public int delete(long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        String selection = PotlatchSchema.Gift.Cols.ID + " = ? ";
        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.delete(tableName, selection, selectionArgs);
        db.close();
        return res;
    }

    @Override
    public int update(GiftData data) throws RemoteException {
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(data.keyID) };
        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.update(tableName, creator.getCV(data), selection, selectionArgs);
        db.close();
        return res;
    }

    @Override
    public ArrayList<GiftData> queryByTitle(String title) throws RemoteException {
        // create String that will match with 'like' in query
        if (title == null || title.isEmpty())
            return query(null, null, null, null);

        String filterWord = "%" + title + "%";

        // Get all the GiftData in the database
        return query(null,
                PotlatchSchema.Gift.Cols.TITLE + " LIKE ? ",
                new String[] { filterWord },
                null);
    }

}