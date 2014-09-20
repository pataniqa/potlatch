package com.pataniqa.coursera.potlatch.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateFormat;

import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;

/**
 * GiftCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as StoryData), ContentValues, and
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
        rValue.put(PotlatchSchema.Gift.Cols.BODY, data.body);
        rValue.put(PotlatchSchema.Gift.Cols.AUDIO_LINK, data.audioLink);
        rValue.put(PotlatchSchema.Gift.Cols.VIDEO_LINK, data.videoLink);
        rValue.put(PotlatchSchema.Gift.Cols.IMAGE_NAME, data.imageName);
        rValue.put(PotlatchSchema.Gift.Cols.IMAGE_LINK, data.imageLink);
        return rValue;
    }

    /**
     * Get all of the StoryData from the passed in cursor.
     * 
     * @param cursor passed in cursor to get StoryData(s) of.
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
     * Get the first StoryData from the passed in cursor.
     * 
     * @param cursor passed in cursor
     * @return GiftData object
     */
    public static GiftData getGiftDataFromCursor(Cursor cursor) {

        long rowID = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.ID));
        long loginId = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.LOGIN_ID));
        long giftId = cursor.getLong(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.GIFT_ID));
        String title = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.TITLE));
        String body = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.BODY));
        String audioLink = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.AUDIO_LINK));
        String videoLink = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.VIDEO_LINK));
        String imageName = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.IMAGE_NAME));
        String imageMetaData = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.IMAGE_LINK));
        String tags = cursor.getString(cursor.getColumnIndex(PotlatchSchema.Gift.Cols.TAGS));

        // construct the returned object
        GiftData rValue = new GiftData(rowID, loginId, giftId, title, body, audioLink, videoLink, imageName,
                imageMetaData);

        return rValue;
    }

    /**
     * A convenience function for converting a date expressed in minutes, days,
     * and hours into a millisecond time stamp.
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }

    /**
     * A convenience function for converting a millisecond time stamp into a
     * String format
     * 
     * @param timestamp
     * @return
     */
    public static String getStringDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
