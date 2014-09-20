package com.pataniqa.coursera.potlatch.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.storage.GiftData;
import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;
import com.pataniqa.coursera.potlatch.storage.StorageUtilities;

/**
 * The activity that allows a user to create and save a Gift.
 */
public class CreateGiftActivity extends GiftActivityBase {

    private final static String LOG_TAG = CreateGiftActivity.class.getCanonicalName();

    // Used as the request codes in startActivityForResult().
    static final int CAMERA_PIC_REQUEST = 1;
    static final int MIC_SOUND_REQUEST = 3;

    // The various UI elements we use
    private EditText titleET;
    private EditText bodyET;
    private EditText imageNameET;
    private TextView imageLocation;

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath; 
    private Uri fileUri;
    private PotlatchResolver resolver;
    private Uri imagePathFinal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        setContentView(R.layout.create_gift_activity);

        // Start a resolver to help us store/retrieve data from a database
        resolver = new PotlatchResolver(this);

        // Get references to all the UI elements
        titleET = (EditText) findViewById(R.id.gift_create_value_title);
        bodyET = (EditText) findViewById(R.id.gift_create_value_body);
        imageNameET = (EditText) findViewById(R.id.gift_create_value_image_name);
        imageLocation = (TextView) findViewById(R.id.gift_create_value_image_location);
    }

    // Reset all the fields to their default values
    public void buttonClearClicked(View v) {
        titleET.setText("" + "");
        bodyET.setText("" + "");
        imageNameET.setText("" + "");
    }

    // Close this activity if the cancel button is clicked
    public void buttonCancelClicked(View v) {
        finish(); // same as hitting 'back' button
    }

    // Create a GiftData object from the input data and store it using the
    // resolver
    public void buttonCreateClicked(View v) {
        Log.d(LOG_TAG, "create button pressed");

        // local Editables
        Editable titleCreateable = titleET.getText();
        Editable bodyCreateable = bodyET.getText();
        Editable imageNameCreateable = imageNameET.getText();

        long loginId = 0;
        long GiftId = 0;
        String title = "";
        String body = "";
        String videoLink = "";
        String imageName = "";
        String imageData = "";

        // pull values from Editables
        loginId = LoginActivity.getLoginId(this);
        GiftId = 1; // TODO Pull this from somewhere.
        title = String.valueOf(titleCreateable.toString());
        body = String.valueOf(bodyCreateable.toString());
        if (fileUri != null) {
            videoLink = fileUri.toString();
        }
        imageName = String.valueOf(imageNameCreateable.toString());
        if (imagePathFinal != null) {
            imageData = imagePathFinal.toString();
        }

        // new GiftData object with above info
        GiftData newData = new GiftData(-1,
        // -1 row index, because there is no way to know which
        // row it will go into
                loginId, GiftId, title, body, videoLink, imageName, imageData);
        Log.d(LOG_TAG, "imageName" + imageNameET.getText());

        Log.d(LOG_TAG, "newGiftData:" + newData);

        // insert it through Resolver to be put into the database
        try {
            resolver.insert(newData);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage());
            e.printStackTrace();
        }

        finish(); // same as hitting 'back' button

    }

    /**
     * Method to be called when the Add Photo button is pressed.
     */
    public void addPhotoClicked(View aView) {
        Log.v(LOG_TAG, "addPhotoClicked(View) called.");

        // Create an intent that asks for an image to be captured
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        // Tell the capturing activity where to store the image
        Uri uriPath = StorageUtilities.getOutputMediaFileUri(this, StorageUtilities.MEDIA_TYPE_IMAGE,
                StorageUtilities.SECURITY_PUBLIC, null);

        if (uriPath == null)
            return;

        imagePath = uriPath;
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imagePath);

        // Start the activity
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    /**
     * This is called when an activity that we've started returns a result.
     * 
     * In our case, we're looking for the results returned from the
     * SoundRecordActivity or the Camera Activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(LOG_TAG, "CreateFragment onActivtyResult called. requestCode: " + requestCode + " resultCode:"
                + resultCode + "data:" + data);

        if (requestCode == CreateGiftActivity.CAMERA_PIC_REQUEST) {
            if (resultCode == CreateGiftActivity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                imagePathFinal = imagePath;
                imageLocation.setText(imagePathFinal.toString());
            } else if (resultCode == CreateGiftActivity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(getApplicationContext(), "Image capture failed.", Toast.LENGTH_LONG).show();
            }
        } 
    }
}
