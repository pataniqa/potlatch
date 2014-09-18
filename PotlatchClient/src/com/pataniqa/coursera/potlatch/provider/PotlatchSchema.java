package com.pataniqa.coursera.potlatch.provider;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A schema is defined as an overall method of organization for some set of data.
 * 
 * This class contains many class constants that help organize data storage
 * and database access.
 */
public class PotlatchSchema {

	/**
	 * Project Related Constants
	 */

	public static final String ORGANIZATIONAL_NAME = "org.pataniqa";
	public static final String PROJECT_NAME = "potlatch";

	/**
	 * ConentProvider Related Constants
	 */
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "."
			+ PROJECT_NAME + ".moocprovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	// Define a static class that represents description of stored content
	// entity.
	public static class Story {
		// define a URI paths to access entity
		// BASE_URI/story - for list of story(s)
		// BASE_URI/story/* - retrieve specific story by id
		public static final String PATH = "story";
		public static final int PATH_TOKEN = 110;

		public static final String PATH_FOR_ID = "story/*";
		public static final int PATH_FOR_ID_TOKEN = 120;

		// URI for all content stored as story entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		private final static String MIME_TYPE_END = "story";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
				Cols.LOGIN_ID, Cols.STORY_ID, Cols.TITLE, Cols.BODY,
				Cols.AUDIO_LINK, Cols.VIDEO_LINK, Cols.IMAGE_NAME,
				Cols.IMAGE_LINK, Cols.TAGS, Cols.CREATION_TIME,
				Cols.STORY_TIME, Cols.LATITUDE, Cols.LONGITUDE };

		// the names and order of ALL column types, including internal use ones (for use with SQLite)
		public static final String[] ALL_COLUMN_TYPES = { "integer",
				"integer", "integer", "text", "text",
				"text", "text", "text",
				"text", "text", "integer",
				"integer", "integer", "integer" };
	
		public static ContentValues initializeWithDefault(
				final ContentValues assignedValues) {
			// final Long now = Long.valueOf(System.currentTimeMillis());
			final ContentValues setValues = (assignedValues == null) ? new ContentValues()
					: assignedValues;
			if (!setValues.containsKey(Cols.LOGIN_ID)) {
				setValues.put(Cols.LOGIN_ID, 0);
			}
			if (!setValues.containsKey(Cols.STORY_ID)) {
				setValues.put(Cols.STORY_ID, 0);
			}
			if (!setValues.containsKey(Cols.TITLE)) {
				setValues.put(Cols.TITLE, "");
			}
			if (!setValues.containsKey(Cols.BODY)) {
				setValues.put(Cols.BODY, "");
			}
			if (!setValues.containsKey(Cols.AUDIO_LINK)) {
				setValues.put(Cols.AUDIO_LINK, "");
			}
			if (!setValues.containsKey(Cols.VIDEO_LINK)) {
				setValues.put(Cols.VIDEO_LINK, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_NAME)) {
				setValues.put(Cols.IMAGE_NAME, "");
			}
			if (!setValues.containsKey(Cols.IMAGE_LINK)) {
				setValues.put(Cols.IMAGE_LINK, "");
			}
			if (!setValues.containsKey(Cols.TAGS)) {
				setValues.put(Cols.TAGS, "");
			}
			if (!setValues.containsKey(Cols.CREATION_TIME)) {
				setValues.put(Cols.CREATION_TIME, 0);
			}
			if (!setValues.containsKey(Cols.STORY_TIME)) {
				setValues.put(Cols.STORY_TIME, 0);
			}
			if (!setValues.containsKey(Cols.LATITUDE)) {
				setValues.put(Cols.LATITUDE, 0);
			}
			if (!setValues.containsKey(Cols.LONGITUDE)) {
				setValues.put(Cols.LONGITUDE, 0);
			}
			return setValues;
		}

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database
			public static final String LOGIN_ID = "LOGIN_ID";
			public static final String STORY_ID = "STORY_ID";
			public static final String TITLE = "TITLE";
			public static final String BODY = "BODY";
			public static final String AUDIO_LINK = "AUDIO_LINK";
			public static final String VIDEO_LINK = "VIDEO_LINK";
			public static final String IMAGE_NAME = "IMAGE_NAME";
			public static final String IMAGE_LINK = "IMAGE_LINK";
			public static final String TAGS = "TAGS";
			public static final String CREATION_TIME = "CREATION_TIME";
			public static final String STORY_TIME = "STORY_TIME";
			public static final String LATITUDE = "LATITUDE";
			public static final String LONGITUDE = "LONGITUDE";
		}
	}

}
