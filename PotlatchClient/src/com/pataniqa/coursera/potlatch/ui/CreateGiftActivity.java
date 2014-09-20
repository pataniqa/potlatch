package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
    static final int GALLERY_PIC_REQUEST = 2;
    static final int VIDEO_REQUEST = 3;

    // The various UI elements we use
    private EditText titleInput;
    private EditText descriptionInput;
    private ImageView imageView;

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath;
    private Uri fileUri;
    private Uri imagePathFinal = null;
    
    private PotlatchResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.create_gift_activity);
        getActionBar().show();

        // Get references to all the UI elements
        imageView = (ImageView) findViewById(R.id.gift_create_img);
        titleInput = (EditText) findViewById(R.id.gift_create_value_title);
        descriptionInput = (EditText) findViewById(R.id.gift_create_value_description);

        // Start a resolver to help us store/retrieve data from a database
        resolver = new PotlatchResolver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                // setGiftQuery(query);
                // updateGifts();
                // TODO - code duplication from ListGiftsActivity
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // setGiftQuery(query);
                // updateGifts();
                // TODO - code duplication from ListGiftsActivity
                return true;
            }

        });

        return true;
    }

    public void selectPhotoClicked(View view) {
        Log.v(LOG_TAG, "selectPhotoClicked(View) called.");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        saveImageToLocalMedia(galleryIntent, GALLERY_PIC_REQUEST);
    }

    public void addPhotoClicked(View aView) {
        Log.v(LOG_TAG, "addPhotoClicked(View) called.");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        saveImageToLocalMedia(cameraIntent, CAMERA_PIC_REQUEST);
    }

    private void saveImageToLocalMedia(Intent intent, int result) {
        Uri tmpImagePath = StorageUtilities.getOutputMediaFileUri(this,
                StorageUtilities.MEDIA_TYPE_IMAGE, StorageUtilities.SECURITY_PUBLIC, null);

        if (tmpImagePath != null) {
            imagePath = tmpImagePath;
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imagePath);
            startActivityForResult(intent, result);
        }
    }

    public void addVideoClicked(View v) {
        Log.v(LOG_TAG, "addVideoClicked(View) called.");
        // TODO
    }

    public void buttonCreateClicked(View v) {
        Log.d(LOG_TAG, "accept button pressed");

        Editable titleCreateable = titleInput.getText();
        Editable bodyCreateable = descriptionInput.getText();

        long loginId = 0;
        long GiftId = 0;
        String title = String.valueOf(titleCreateable.toString());
        String body = String.valueOf(bodyCreateable.toString());
        String videoLink = fileUri != null ? fileUri.toString() : "";
        String imageData = imagePathFinal != null ? imagePathFinal.toString() : "";

        GiftData newData = new GiftData(loginId, GiftId, title, body, videoLink, imageData);

        Log.d(LOG_TAG, "newGiftData:" + newData);

        try {
            resolver.insert(newData);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage());
            e.printStackTrace();
        }

        finish();
    }

    public void buttonCancelClicked(View v) {
        Log.d(LOG_TAG, "cancel button pressed");
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "CreateFragment onActivtyResult called. requestCode: " + requestCode
                + " resultCode:" + resultCode + "data:" + data);

        switch (requestCode) {
        case CreateGiftActivity.CAMERA_PIC_REQUEST:
            imageResult(resultCode);
            break;
        case CreateGiftActivity.GALLERY_PIC_REQUEST:
            imageResult(resultCode);
            break;
        case CreateGiftActivity.VIDEO_REQUEST:
            // TODO
            break;
        }
    }

    private void imageResult(int resultCode) {
        switch (resultCode) {
        case CreateGiftActivity.RESULT_OK:
            imagePathFinal = imagePath;
            File image = new File(imagePathFinal.getPath());

            if (image != null && image.exists()) {
                Log.d(LOG_TAG, "image link is valid");

                Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bmp);
            }
            break;
        case CreateGiftActivity.RESULT_CANCELED:
            break;
        default:
            Toast.makeText(getApplicationContext(), "Image capture failed.", Toast.LENGTH_LONG)
                    .show();
            break;
        }
    }
}
