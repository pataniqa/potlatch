package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;
import com.pataniqa.coursera.potlatch.storage.GiftCreator;
import com.pataniqa.coursera.potlatch.storage.GiftData;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.pataniqa.coursera.potlatch.R;

/**
 * This activity allows users to edit some parts of a previously posted story
 */
public class EditGiftActivity extends GiftActivityBase {
	
	// The tag used for debugging with Logcat
	final static public String LOG_TAG = EditGiftActivity.class 
			.getCanonicalName();

	// variable for passing around row index
	final static public String rowIdentifyerTAG = "index";
	
	// The TextViews and EditTexts we use
	TextView loginIdET;
	TextView storyIdET;
	EditText titleET;
	EditText bodyET;
	EditText imageNameET;
	EditText imageMetaDataET;
	EditText audioLinkET;
	EditText tagsET;
	TextView creationTimeTV;
	EditText latitudeET;
	EditText longitudeET;
	
	DatePicker storyDate;
	

	// Button(s) used
	Button saveButton;
	Button resetButton;
	Button cancelButton;

	// custom ContentResolver wrapper.
	PotlatchResolver resolver;

	GiftData mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the UI
		setContentView(R.layout.edit_gift_activity);
		
		// Start the Resolver to help us get/set data in the database
		resolver = new PotlatchResolver(this);
		
		// Get the Buttons
		saveButton = (Button) findViewById(R.id.story_edit_button_save);
		resetButton = (Button) findViewById(R.id.story_edit_button_reset);
		cancelButton = (Button) findViewById(R.id.story_edit_button_cancel);

		// Get the EditTexts
		loginIdET = (TextView) findViewById(R.id.story_edit_login_id);
		storyIdET = (TextView) findViewById(R.id.story_edit_story_id);
		titleET = (EditText) findViewById(R.id.story_edit_title);
		bodyET = (EditText) findViewById(R.id.story_edit_body);
		audioLinkET = (EditText) findViewById(R.id.story_edit_audio_link);
		imageNameET = (EditText) findViewById(R.id.story_edit_image_name);
		imageMetaDataET = (EditText) findViewById(R.id.story_edit_image_meta_data);
		tagsET = (EditText) findViewById(R.id.story_edit_tags);
		creationTimeTV = (TextView) findViewById(R.id.story_edit_creation_time);
		storyDate = (DatePicker) findViewById(R.id.story_edit_value_story_time_date_picker);
		latitudeET = (EditText) findViewById(R.id.story_edit_latitude);
		longitudeET = (EditText) findViewById(R.id.story_edit_longitude);
		
		// set the EditTexts to this Story's Values
		setValuesToDefault();  

	}

	// The listener for the various buttons
	public void clickListener(View v) {
		switch (v.getId()) {
		case R.id.story_edit_button_save:
			doSaveButtonClick();	
			break;
		case R.id.story_edit_button_reset:
			doResetButtonClick();
			break;
		case R.id.story_edit_button_cancel:
			doCancelButtonClick();
			break;
		default:
		}
	}
	
	public void doResetButtonClick() {
		setValuesToDefault();
	}

	// Update the provided values in the database
	public void doSaveButtonClick() {
		Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
		
		// Make the story data from the UI
		GiftData story = makeStoryDataFromUI();
		
		// If we succeeded, go ahead and update the data in the database
		if (story != null) {
			try {
				resolver.updateStoryWithID(story);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		
		finish(); // same as hitting 'back' button
		
	}

	// Use the data the user input into the UI to construct a StoryData object
	public GiftData makeStoryDataFromUI() {

		// Get the editables from the UI
		Editable titleEditable = titleET.getText();
		Editable bodyEditable = bodyET.getText();
		Editable imageNameEditable = imageMetaDataET.getText();
		Editable tagsEditable = tagsET.getText();
		Editable latitudeEditable = latitudeET.getText();
		Editable longitudeEditable = longitudeET.getText();

		// Pull values from Editables
		String title = titleEditable.toString();
		String body = bodyEditable.toString();
		String imageName = imageNameEditable.toString();

		String tags = tagsEditable.toString();
		Double latitude = Double.valueOf(latitudeEditable.toString());		
		Double longitude = Double.valueOf(longitudeEditable.toString());
		
		long storyTime = GiftCreator.componentTimeToTimestamp(storyDate.getYear(),
				storyDate.getMonth(), storyDate.getDayOfMonth(), 0, 0);

		// Construct the Story Data Object
		GiftData rValue = new GiftData(getUniqueKey(), mData.loginId,
				mData.giftId, title, body, mData.audioLink, mData.videoLink,
				imageName, mData.imageLink, tags, mData.creationTime,
				storyTime, latitude, longitude);

		// Make sure the new StoryData has the same key as the old one so that it will
		// replace the old one in the database.
		rValue.key = mData.key;		

		// return StoryData object with new values
		return rValue;

	}

	public void doCancelButtonClick() {
		finish(); // same as hitting 'back' button
	}
	
	/**
	 * Sets all the UI elements to their original values
	 * @return
	 */
	public boolean setValuesToDefault() {

		GiftData storyData;
		try {
			storyData = resolver.getStoryDataViaRowID(getUniqueKey());
		} catch (RemoteException e) {
			Log.d(LOG_TAG, "" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		if (storyData != null) {
			mData = storyData;
			Log.d(LOG_TAG, "setValuesToDefualt :" + storyData.toString());
			
			// set the EditTexts to the current values
			loginIdET.setText(Long.valueOf(storyData.loginId).toString());  
			storyIdET.setText(Long.valueOf(storyData.giftId).toString());
			titleET.setText(storyData.title);
			bodyET.setText(storyData.body);
			imageNameET.setText(storyData.imageName);
			imageMetaDataET.setText(storyData.imageLink);
			tagsET.setText(storyData.tags);
			creationTimeTV.setText(Long.valueOf(storyData.creationTime)
					.toString());
			latitudeET.setText(Double.valueOf(storyData.latitude).toString());
			longitudeET.setText(Double.valueOf(storyData.longitude).toString());
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the unique identifier of this StoryData in the database
	 */
	public long getUniqueKey() {
		return getIntent().getLongExtra(rowIdentifyerTAG, 0);
	}
}
