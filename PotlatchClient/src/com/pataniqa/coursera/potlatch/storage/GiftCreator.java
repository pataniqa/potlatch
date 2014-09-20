package com.pataniqa.coursera.potlatch.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateFormat;

import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;

/**
 * StoryCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as StoryData), ContentValues, and Cursors.
 * 
 * @author Michael A. Walker
 * 
 */
public class GiftCreator {

	/**
	 * Create a ContentValues from a provided StoryData.
	 * 
	 * @param data
	 *            StoryData to be converted.
	 * @return ContentValues that is created from the StoryData object
	 */
	public static ContentValues getCVfromStory(final GiftData data) {
		ContentValues rValue = new ContentValues();
		rValue.put(PotlatchSchema.Story.Cols.LOGIN_ID, data.loginId);
		rValue.put(PotlatchSchema.Story.Cols.STORY_ID, data.giftId);
		rValue.put(PotlatchSchema.Story.Cols.TITLE, data.title);
		rValue.put(PotlatchSchema.Story.Cols.BODY, data.body);
		rValue.put(PotlatchSchema.Story.Cols.AUDIO_LINK, data.audioLink);
		rValue.put(PotlatchSchema.Story.Cols.VIDEO_LINK, data.videoLink);
		rValue.put(PotlatchSchema.Story.Cols.IMAGE_NAME, data.imageName);
		rValue.put(PotlatchSchema.Story.Cols.IMAGE_LINK, data.imageLink);
		rValue.put(PotlatchSchema.Story.Cols.TAGS, data.tags);
		rValue.put(PotlatchSchema.Story.Cols.CREATION_TIME, data.creationTime);
		rValue.put(PotlatchSchema.Story.Cols.STORY_TIME, data.giftTime);
		rValue.put(PotlatchSchema.Story.Cols.LATITUDE, data.latitude);
		rValue.put(PotlatchSchema.Story.Cols.LONGITUDE, data.longitude);
		return rValue;
	}

	/**
	 * Get all of the StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor to get StoryData(s) of.
	 * @return ArrayList<StoryData\> The set of StoryData
	 */
	public static ArrayList<GiftData> getStoryDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<GiftData> rValue = new ArrayList<GiftData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getStoryDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}

	/**
	 * Get the first StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return StoryData object
	 */
	public static GiftData getStoryDataFromCursor(Cursor cursor) {

		long rowID = cursor.getLong(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.ID));
		long loginId = cursor.getLong(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.LOGIN_ID));
		long storyId = cursor.getLong(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.STORY_ID));
		String title = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.TITLE));
		String body = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.BODY));
		String audioLink = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.AUDIO_LINK));
		String videoLink = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.VIDEO_LINK));
		String imageName = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.IMAGE_NAME));
		String imageMetaData = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.IMAGE_LINK));
		String tags = cursor.getString(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.TAGS));
		long creationTime = cursor.getLong(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.CREATION_TIME));
		long storyTime = cursor.getLong(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.STORY_TIME));
		double latitude = cursor.getDouble(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.LATITUDE));
		double longitude = cursor.getDouble(cursor
				.getColumnIndex(PotlatchSchema.Story.Cols.LONGITUDE));

		// construct the returned object
		GiftData rValue = new GiftData(rowID, loginId, storyId, title, body,
				audioLink, videoLink, imageName, imageMetaData, tags,
				creationTime, storyTime, latitude, longitude);

		return rValue;
	}
	
	/**
	 * A convenience function for converting a date expressed in minutes, days, and hours into
	 * a millisecond time stamp.
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long componentTimeToTimestamp(int year, int month, int day, int hour,
			int minute) {

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
	 * A convenience function for converting a millisecond time stamp into a String format
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
