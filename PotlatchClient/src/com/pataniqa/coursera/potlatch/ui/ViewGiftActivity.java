package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;
import com.pataniqa.coursera.potlatch.storage.GiftCreator;
import com.pataniqa.coursera.potlatch.storage.GiftData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pataniqa.coursera.potlatch.R;

/**
 * This activity lets a user view a story in full screen mode.
 */
public class ViewGiftActivity extends GiftActivityBase {

	private static final String LOG_TAG = ViewGiftActivity.class
			.getCanonicalName();
	
	// The StoryData we're displaying
	GiftData giftData;

	// The UI elements we'll be using
	TextView loginIdTV;
	TextView giftIdTV;
	TextView titleTV;
	TextView bodyTV;
	TextView audioLinkTV;
	TextView videoLinkTV;
	TextView imageNameTV;

	TextView tagsTV;
	TextView creationTimeTV;
	TextView giftTimeTV;
	TextView latitudeTV;
	TextView longitudeTV;

	ImageView imageMetaDataView;

	// buttons for edit and delete
	Button editButton;
	Button deleteButton;

	// Helps us retrieve data from the database
	private PotlatchResolver resolver;
	
	// Used to pass around the row ID of stories
	public final static String rowIdentifyerTAG = "index";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set up the UI
		setContentView(R.layout.view_gift_activity);
		
		// Create a resolver to help us retrieve data from the database
		resolver = new PotlatchResolver(this);	
		
		// Get actual references to the instantiated UI objects
		loginIdTV = (TextView) findViewById(R.id.story_view_value_login_id);
		giftIdTV = (TextView) findViewById(R.id.story_view_value_story_id);
		titleTV = (TextView) findViewById(R.id.story_view_value_title);
		bodyTV = (TextView) findViewById(R.id.story_view_value_body);
		audioLinkTV = (TextView) findViewById(R.id.story_view_value_audio_link);
		videoLinkTV = (TextView) findViewById(R.id.story_view_value_video_link);
		imageNameTV = (TextView) findViewById(R.id.story_view_value_image_name);

		tagsTV = (TextView) findViewById(R.id.story_view_value_tags);
		creationTimeTV = (TextView) findViewById(R.id.story_view_value_creation_time);
		giftTimeTV = (TextView) findViewById(R.id.story_view_value_story_time);
		latitudeTV = (TextView) findViewById(R.id.story_view_value_latitude);
		longitudeTV = (TextView) findViewById(R.id.story_view_value_longitude);

		imageMetaDataView = (ImageView) findViewById(R.id.story_view_value_image_meta_data);

		// Set the default values
		loginIdTV.setText("" + 0);
		giftIdTV.setText("" + 0);
		titleTV.setText("" + "");
		bodyTV.setText("" + "");
		audioLinkTV.setText("" + "");
		videoLinkTV.setText("" + "");
		imageNameTV.setText("" + "");
		tagsTV.setText("" + "");

		latitudeTV.setText("" + 0);
		longitudeTV.setText("" + 0);

		editButton = (Button) findViewById(R.id.button_story_view_to_edit);
		deleteButton = (Button) findViewById(R.id.button_story_view_to_delete);

		try {
			// Fill out all the UI elements with data from our StoryData
			setUiToGiftData(getUniqueKey());
		} catch (RemoteException e) {
			Toast.makeText(this,
					"Error retrieving information from local data store.",
					Toast.LENGTH_LONG).show();
			Log.e(LOG_TAG, "Error getting Story data from C.P.");
			// e.printStackTrace();
		}
		
	}
	
	// Fills out the UI elements with data from a StoryData in the database specified by a unique key
	public void setUiToGiftData(long getUniqueKey) throws RemoteException {
		Log.d(LOG_TAG, "setUiToGiftData");	
		
		// Get the GiftData from the database
		giftData = resolver.getGiftDataViaRowID(getUniqueKey);
		
		if (giftData != null) 
		{
			Log.d(LOG_TAG, "setUiToStoryData + storyData:" + giftData.toString());
			
			// Fill in the appropriate UI elements
			loginIdTV.setText(Long.valueOf(giftData.loginId).toString());
			giftIdTV.setText(Long.valueOf(giftData.giftId).toString());
			titleTV.setText(String.valueOf(giftData.title).toString());
			bodyTV.setText(String.valueOf(giftData.body).toString());
			audioLinkTV.setText(String.valueOf(giftData.audioLink).toString());
			videoLinkTV.setText(String.valueOf(giftData.videoLink).toString());
			imageNameTV.setText(String.valueOf(giftData.imageName).toString());
			tagsTV.setText(String.valueOf(giftData.tags).toString());
			creationTimeTV.setText(GiftCreator.getStringDate(giftData.creationTime));
			giftTimeTV.setText(GiftCreator.getStringDate(giftData.giftTime));

			latitudeTV.setText(Double.valueOf(giftData.latitude).toString());
			longitudeTV.setText(Double.valueOf(giftData.longitude).toString());

			Log.d(LOG_TAG, "image file path: " + giftData.imageLink);
			
			// Set up image
			imageNameTV.setVisibility(View.GONE);
			imageMetaDataView.setVisibility(View.GONE);

			if (giftData.imageLink != null
					&& giftData.imageLink.equals("") == false
					&& giftData.imageLink.equals("null") == false) {
				Log.d(LOG_TAG, "image link is valid" + giftData.imageLink);
				Uri uri = Uri.parse(giftData.imageLink);
				File image = new File(uri.getPath());
				
				if (image != null && image.exists()) {
					Log.d(LOG_TAG, "image link is valid");
					imageNameTV.setVisibility(View.VISIBLE);
					imageNameTV.setText("Image");

					Bitmap bmp = BitmapFactory.decodeFile(image
							.getAbsolutePath());
					imageMetaDataView.setVisibility(View.VISIBLE);
					imageMetaDataView.setImageBitmap(bmp);
				}
			}

		}
	}
	
	// Action to be performed when the edit button is pressed
	private void editButtonPressed() {
		openEditStoryActivity(giftData.KEY_ID);
	}

	// Action to be performed when the delete button is pressed
	private void deleteButtonPressed() {
		String message;

		message = getResources().getString(
				R.string.story_view_deletion_dialog_message);

		// Show a dialog confirming that the user wants to delete this story
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.story_view_deletion_dialog_title)
				.setMessage(message)
				.setPositiveButton(R.string.story_view_deletion_dialog_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									resolver.deleteAllStoryWithRowID(giftData.KEY_ID);
								} catch (RemoteException e) {
									Log.e(LOG_TAG, "RemoteException Caught => "
											+ e.getMessage());
									e.printStackTrace();
								}
						
								finish();
								
							}

						})
				.setNegativeButton(R.string.story_view_deletion_dialog_no, null)
				.show();
	}

	// Get the unique key associated with the StoryData we're displaying
	public long getUniqueKey() {
		return getIntent().getLongExtra(rowIdentifyerTAG, 0);
	}
	
	// A clickListener that forwards clicks to the appropriate function
	public void clickListener(View view) {
		switch (view.getId()) {
		case R.id.button_story_view_to_delete:
			deleteButtonPressed();
			break;
		case R.id.button_story_view_to_edit:
			editButtonPressed();
			break;
		default:
			break;
		}
	}
}
