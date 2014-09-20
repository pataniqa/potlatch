package com.pataniqa.coursera.potlatch.storage;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateFormat;

import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;

/**
 * GiftCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as GiftData), ContentValues, and
 * Cursors.
 */
public class GiftCreator {

    /**
     * Create a ContentValues from a provided GiftData.
     * 
     * @param data GiftData to be converted.
     * @return ContentValues that is created from the GiftData object
     */
    public static ContentValues getCVfromGift(final GiftData data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PotlatchSchema.Gift.Cols.LOGIN_ID, data.loginId);
        rValue.put(PotlatchSchema.Gift.Cols.GIFT_ID, data.giftId);
        rValue.put(PotlatchSchema.Gift.Cols.TITLE, data.title);
        rValue.put(PotlatchSchema.Gift.Cols.DESCRIPTION, data.description);
        rValue.put(PotlatchSchema.Gift.Cols.VIDEO_URI, data.videoUri);
        rValue.put(PotlatchSchema.Gift.Cols.IMAGE_URI, data.imageUri);
        return rValue;
    }

    /**
     * Get all of the GiftData from the passed in cursor.
     * 
     * @param cursor passed in cursor to get GiftData(s) of.
     * @return ArrayList<GiftData\> The set of GiftData
     */
    public static ArrayList<GiftData> getGiftDataArrayListFromCursor(Cursor cursor) {
        ArrayList<GiftData> rValue = new ArrayList<GiftData>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    rValue.add(getGiftDataFromCursor(cursor));
                } while (cursor.moveToNext() == true);
            }
        }
        return rValue;
    }

    /**
     * Get the first GiftData from the passed in cursor.
     * 
     * @param cursor passed in cursor
     * @return GiftData object
     */
    public static GiftData getGiftDataFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.ID));
        long loginId = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.LOGIN_ID));
        long giftId = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.GIFT_ID));
        String title = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.TITLE));
        String body = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.DESCRIPTION));
        String videoUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.VIDEO_URI));
        String imageUri = cursor.getString(cursor
                .getColumnIndex(PotlatchSchema.Gift.Cols.IMAGE_URI));
        return new GiftData(rowID, loginId, giftId, title, body, videoUri, imageUri);
    }

    /**
     * Converting a millisecond time stamp into a String
     * 
     * @param timestamp
     * @return The date in dd-MM-yyyy format
     */
    public static String getStringDate(long timestamp) {
        return DateFormat.format("dd-MM-yyyy", new Date(timestamp)).toString();
    }
}
