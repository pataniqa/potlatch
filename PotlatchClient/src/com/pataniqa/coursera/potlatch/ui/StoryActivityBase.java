package com.pataniqa.coursera.potlatch.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Base class for all StoryData UI activities.
 * 
 * This class provides convenience functions for switching between the various activities
 * in the application.
 * 
 */
@SuppressLint("Registered")
public class StoryActivityBase extends Activity {

	// A tag used for debugging with Logcat
	private static final String LOG_TAG = StoryActivityBase.class
			.getCanonicalName();

	/**
	 * Open the LoginActivity
	 */
	public void openLoginActivity() {
		Intent intent = newLoginActivityIntent(this); 
		startActivity(intent);
	}
	
	/**
	 *  Open the ViewStoryActivity
	 * @param index		The index in the database of the StoryData to display
	 */
	public void openViewStoryActivity(long index) {
		Log.d(LOG_TAG, "openStoryViewActivity(" + index + ")");
		Intent intent = newStoryViewIntent(this, index);
		startActivity(intent);
	}

	/**
	 *  Open the EditStoryActivity
	 * @param index		The index in the database of the StoryData to edit
	 */
	public void openEditStoryActivity(final long index) {
		Log.d(LOG_TAG, "openEditStoryActivity(" + index + ")");
		Intent intent = newEditStoryIntent(this, index);
		startActivity(intent);
	}

	/**
	 * Open the CreateStoryActivity
	 */
	public void openCreateStoryActivity() {
		Log.d(LOG_TAG, "openCreateStoryActivity");
		Intent intent = newCreateStoryIntent(this);
		startActivity(intent);
	}

	/**
	 * Open the ListStoryActivity
	 */
	public void openListStoryActivity() {
		Log.d(LOG_TAG, "openCreateStoryActivity");
		Intent intent = newListStoryIntent(this);
		startActivity(intent);
	}

	/*************************************************************************/
	/*
	 * Create Intents for the various Activities
	 */
	/*************************************************************************/

	public static Intent newLoginActivityIntent(Activity activity) {
		return new Intent(activity, LoginActivity.class);
	}
	
	public static Intent newStoryViewIntent(Activity activity, long index) {
		Intent intent = new Intent();
		intent.setClass(activity, ViewStoryActivity.class);
		intent.putExtra(ViewStoryActivity.rowIdentifyerTAG, index);	
		return intent;
	}

	public static Intent newEditStoryIntent(Activity activity, long index) {
		Intent intent = new Intent();
		intent.setClass(activity, EditStoryActivity.class);
		intent.putExtra(EditStoryActivity.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newListStoryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ListStoryActivity.class); 
		return intent;
	}

	public static Intent newCreateStoryIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, CreateStoryActivity.class);
		return intent;
	}
}
