package com.pataniqa.coursera.potlatch.storage;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.util.Log;

import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;

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
public class PotlatchResolver {

    private static final String tableName = "PotlatchTable";

    // A private instance of our locally defined mSQLiteOpenHelper
    private mSQLiteOpenHelper helper;

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

            StringBuilder createTable = new StringBuilder();
            createTable.append("create table if not exists " + tableName + " (");
            createTable.append(PotlatchSchema.Gift.Cols.ID + " integer primary key autoincrement ");
            for (Map.Entry<String, String> entry : PotlatchSchema.Gift.COLUMNS.entrySet()) {
                createTable.append(", " + entry.getKey() + " " + entry.getValue());
            }
            createTable.append(");");

            Log.d("PotlatchResolver", "onCreate() called: " + createTable.toString());

            try {
                db.execSQL(createTable.toString());
            } catch (SQLException e) {
                Log.e("PotlatchResolver", e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }

    }

    public PotlatchResolver(Context context) {
        // Create a new instance of mSQLiteOpenHelper to manage the database
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

    /**
     * Delete all GiftData(s) from the database that match the selectionArgs
     * 
     * @param selection
     * @param selectionArgs
     * @return number of GiftData rows deleted
     * @throws RemoteException
     */
    public int deleteGiftData(final String selection, final String[] selectionArgs)
            throws RemoteException {
        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.delete(tableName, selection, selectionArgs);
        db.close();
        return res;
    }

    /**
     * Insert a new GiftData object into the database
     * 
     * @param gift object to be inserted
     * @return row ID of inserted GiftData in the ContentProvider
     * @throws RemoteException
     */
    public long insert(final GiftData gift) throws RemoteException {
        ContentValues tempCV = gift.getCV();
        tempCV.remove(PotlatchSchema.Gift.Cols.ID);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(tableName, null, tempCV);
        db.close();
        return res;
    }

    /**
     * Query the database for GiftData conforming to certain specifications.
     * 
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of GiftData objects
     * @throws RemoteException
     */
    public ArrayList<GiftData> queryGiftData(final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) throws RemoteException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = db.query(tableName, projection, selection, selectionArgs, null, null,
                sortOrder);
        ArrayList<GiftData> rValue = new ArrayList<GiftData>();
        rValue.addAll(GiftCreator.getGiftDataArrayListFromCursor(result));
        result.close();
        db.close();
        return rValue;
    }

    /**
     * Update the specified GiftData with new values.
     * 
     * @param values
     * @param selection
     * @param selectionArgs
     * @return number of rows changed
     * @throws RemoteException
     */
    public int updateGiftData(final GiftData values, final String selection,
            final String[] selectionArgs) throws RemoteException {

        SQLiteDatabase db = helper.getWritableDatabase();
        int res = db.update(tableName, values.getCV(), selection, selectionArgs);
        db.close();
        return res;
    }

    /*
     * Sample extensions of above for customized additional methods for classes
     * that extend this one
     */

    /**
     * Get all the GiftData objects current stored in the Content Provider
     * 
     * @return an ArrayList containing all the GiftData objects
     * @throws RemoteException
     */
    public ArrayList<GiftData> getAllGiftData() throws RemoteException {
        return queryGiftData(null, null, null, null);
    }

    /**
     * Get a GiftData from the data stored at the given rowID
     * 
     * @param rowID
     * @return GiftData at the given rowID
     * @throws RemoteException
     */
    public GiftData getGiftDataViaRowID(final long rowID) throws RemoteException {
        String[] selectionArgs = { String.valueOf(rowID) };
        ArrayList<GiftData> results = queryGiftData(null, PotlatchSchema.Gift.Cols.ID + "= ?",
                selectionArgs, null);
        return results.size() > 0 ? results.get(0) : null;
    }

    /**
     * Delete All rows, from AllGift table, that have the given rowID. (Should
     * only be 1 row, but Content Providers/SQLite3 deletes all rows with
     * provided rowID)
     * 
     * @param rowID
     * @return number of rows deleted
     * @throws RemoteException
     */
    public int deleteAllGiftWithRowID(long rowID) throws RemoteException {
        String[] args = { String.valueOf(rowID) };
        return deleteGiftData(PotlatchSchema.Gift.Cols.ID + " = ? ", args);
    }

    /**
     * Updates all GiftData stored with the provided GiftData's 'KEY_ID' (should
     * only be 1 row of data in the content provider, but content provider
     * implementation will update EVERY row that matches.)
     * 
     * @param data
     * @return number of rows altered
     * @throws RemoteException
     */
    public int updateGiftWithID(GiftData data) throws RemoteException {
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(data.KEY_ID) };
        return updateGiftData(data, selection, selectionArgs);
    }

}