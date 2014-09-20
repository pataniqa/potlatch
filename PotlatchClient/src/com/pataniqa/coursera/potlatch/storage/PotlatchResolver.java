package com.pataniqa.coursera.potlatch.storage;

import java.util.ArrayList;

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
 * This used to be a wrapper for a ContentResolver to a single URI, which would be used to interface
 * with a ContentProvider which communicates with a server on the web.
 * 
 * However, to simplify this assignment and remove any dependencies on the database server being online,
 * we removed the ContentResolver and instead are storing StoryData in the default SQLite Database that 
 * is hosted by the device. 
 * 
 */
public class PotlatchResolver {

	// A private instance of our locally defined mSQLiteOpenHelper
	private mSQLiteOpenHelper helper;

	// An extension of SQLiteOpenHelper which helps us create and manage
	// an SQLiteDatabase
	static class mSQLiteOpenHelper extends SQLiteOpenHelper {
		
		// Simply forward construction to the super class
		public mSQLiteOpenHelper (Context context) {
			super(context, "PotlatchSecurityDatabase", null, 1);
		}
		
		// When the database is created, create a table to store our story data, if it does not exist.
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			StringBuilder createTable = new StringBuilder();
			createTable.append("create table if not exists " + tableName + " (");
			createTable.append(PotlatchSchema.Story.Cols.ID + " integer primary key autoincrement ");
			
			String [] names = PotlatchSchema.Story.ALL_COLUMN_NAMES;
			String [] types = PotlatchSchema.Story.ALL_COLUMN_TYPES;
			
			for (int i = 1; i < names.length; ++i) {
				createTable.append(", " + names[i] + " " +types[i]);
			}
			
			createTable.append(");");
			
			Log.d("PotlatchResolver", "onCreate() called: " + createTable.toString());
			
			try {
				db.execSQL(createTable.toString());
			}
			catch (SQLException e) {
				Log.e("PotlatchResolver", e.getMessage());
			}
		}

		// We won't worry about upgrading our database in this simple version.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}
	
	// The name of the table that will store our story data in the database
	private static final String tableName = "PotlatchTable";
	
	/**
	 * Constructor
	 * 
	 * @param context  The context in which to create the database.
	 */
	public PotlatchResolver(Context context) {
		// Create a new instance of mSQLiteOpenHelper to manage the database
		helper = new mSQLiteOpenHelper(context);
	}
	
	/**
	 * When the user of this class is done with it, make sure to close the database.
	 */
	@Override
	public void finalize() {
		if (helper != null)
			helper.close();
	}
	
	/*
	 * Delete for each ORM Data Type
	 */
	/**
	 * Delete all StoryData(s) from the database that match the
	 * selectionArgs
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @return number of StoryData rows deleted
	 * @throws RemoteException
	 */
	public int deleteStoryData(final String selection,
			final String[] selectionArgs) throws RemoteException {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete(tableName, selection, selectionArgs);
		return res;
	}

	/**
	 * Insert a new StoryData object into the database
	 * 
	 * @param storyObject
	 *            object to be inserted
	 * @return row ID of inserted StoryData in the ContentProvider
	 * @throws RemoteException
	 */
	public long insert(final GiftData storyObject) throws RemoteException {
		ContentValues tempCV = storyObject.getCV();
		tempCV.remove(PotlatchSchema.Story.Cols.ID);
		SQLiteDatabase db = helper.getWritableDatabase();
		long res = db.insert(tableName, null, tempCV);
		db.close();
		return res;
	}

	/*
	 * Query for each ORM Data Type
	 */

	/**
	 * Query the database for StoryData conforming to certain specifications.
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return an ArrayList of StoryData objects
	 * @throws RemoteException
	 */
	public ArrayList<GiftData> queryStoryData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the database
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.query(tableName, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make return object
		ArrayList<GiftData> rValue = new ArrayList<GiftData>();
		// convert cursor to reutrn object
		rValue.addAll(StoryCreator.getStoryDataArrayListFromCursor(result));
		result.close();
		
		//close the database
		db.close();
		
		// return 'return object'
		return rValue;
	}

	/*
	 * Update for each ORM Data Type
	 */

	/**
	 * Update the specified StoryData with new values.
	 * 
	 * @param values
	 * @param selection
	 * @param selectionArgs
	 * @return number of rows changed
	 * @throws RemoteException
	 */
	public int updateStoryData(final GiftData values, final String selection,
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
	 * Get all the StoryData objects current stored in the Content Provider
	 * 
	 * @return an ArrayList containing all the StoryData objects
	 * @throws RemoteException
	 */
	public ArrayList<GiftData> getAllStoryData() throws RemoteException {
		return queryStoryData(null, null, null, null);
	}

	/**
	 * Get a StoryData from the data stored at the given rowID
	 * 
	 * @param rowID
	 * @return StoryData at the given rowID
	 * @throws RemoteException
	 */
	public GiftData getStoryDataViaRowID(final long rowID)
			throws RemoteException {
		String[] selectionArgs = { String.valueOf(rowID) };
		ArrayList<GiftData> results = queryStoryData(null,
				PotlatchSchema.Story.Cols.ID + "= ?", selectionArgs, null);
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Delete All rows, from AllStory table, that have the given rowID. (Should
	 * only be 1 row, but Content Providers/SQLite3 deletes all rows with
	 * provided rowID)
	 * 
	 * @param rowID
	 * @return number of rows deleted
	 * @throws RemoteException
	 */
	public int deleteAllStoryWithRowID(long rowID) throws RemoteException {
		String[] args = { String.valueOf(rowID) };
		return deleteStoryData(PotlatchSchema.Story.Cols.ID + " = ? ", args);
	}

	/**
	 * Updates all StoryData stored with the provided StoryData's 'KEY_ID'
	 * (should only be 1 row of data in the content provider, but content
	 * provider implementation will update EVERY row that matches.)
	 * 
	 * @param data
	 * @return number of rows altered
	 * @throws RemoteException
	 */
	public int updateStoryWithID(GiftData data) throws RemoteException {
		String selection = "_id = ?";
		String[] selectionArgs = { String.valueOf(data.KEY_ID) };
		return updateStoryData(data, selection, selectionArgs);
	}

	
}