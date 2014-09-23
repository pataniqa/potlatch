package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewSwitcher;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.LocalStorageUtilities;

/**
 * Abstract class that forms the basis of the CreateGiftActivity and
 * EditGiftActivity.
 */
abstract class ViewGiftActivity extends GiftActivity {

    private final static String LOG_TAG = ViewGiftActivity.class.getCanonicalName();

    // Used as the request codes in startActivityForResult().
    enum Request {
        CAMERA_PIC_REQUEST, GALLERY_PIC_REQUEST, CAMERA_VIDEO_REQUEST
    };

    // The various UI elements we use
    @InjectView(R.id.gift_create_title)
    EditText titleInput;
    @InjectView(R.id.gift_create_description)
    EditText descriptionInput;
    @InjectView(R.id.gift_create_img)
    ImageView image;
    @InjectView(R.id.view_gift_viewswitcher)
    ViewSwitcher viewSwitcher;
    @InjectView(R.id.gift_create_video)
    VideoView video;
    @InjectView(R.id.gift_create_gift_chain)
    AutoCompleteTextView giftChain;

    Uri imagePathFinal = null;
    Uri videoPathFinal = null;
    Map<String, Long> giftChains = new HashMap<String, Long>();

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath = null;
    private static Uri videoPath = null;
    private SharedPreferences prefs;
    private long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPreferences();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        savePreferences();
    }

    public void selectPhotoButtonClicked(View view) {
        Log.v(LOG_TAG, "selectPhotoClicked");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Request.GALLERY_PIC_REQUEST.ordinal());
    }

    public void addPhotoButtonClicked(View aView) {
        Log.v(LOG_TAG, "addPhotoClicked");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = LocalStorageUtilities.getOutputMediaFileUri(this,
                LocalStorageUtilities.MediaType.IMAGE,
                LocalStorageUtilities.Security.PUBLIC,
                null);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imagePath);
        startActivityForResult(cameraIntent, Request.CAMERA_PIC_REQUEST.ordinal());
    }

    public void addVideoButtonClicked(View v) {
        Log.v(LOG_TAG, "addVideoClicked(View) called.");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoPath = LocalStorageUtilities.getOutputMediaFileUri(this,
                LocalStorageUtilities.MediaType.VIDEO,
                LocalStorageUtilities.Security.PUBLIC,
                null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, Request.CAMERA_VIDEO_REQUEST.ordinal());
    }

    public void cancelButtonClicked(View v) {
        Log.d(LOG_TAG, "cancelButtonClicked");
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode
                + "data:" + data);

        switch (Request.values()[requestCode]) {
        case CAMERA_PIC_REQUEST:
            if (resultCode == CreateGiftActivity.RESULT_OK) {
                if (viewSwitcher.getCurrentView() != image)
                    viewSwitcher.showPrevious();
                imagePathFinal = imagePath;
                File imageFile = new File(imagePathFinal.getPath());
                if (imageFile != null && imageFile.exists()) {
                    Log.d(LOG_TAG, "image URI is valid");
                    Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    image.setVisibility(View.VISIBLE);
                    image.setImageBitmap(bmp);
                    image.setScaleType(ScaleType.FIT_CENTER);
                } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                    Log.e(LOG_TAG, "Image capture failed.");
                }
            }
            break;
        case GALLERY_PIC_REQUEST:
            if (viewSwitcher.getCurrentView() != image)
                viewSwitcher.showPrevious();
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            imagePathFinal = Uri.fromFile(new File(picturePath));
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(thumbnail);
            image.setScaleType(ScaleType.FIT_CENTER);
            break;
        case CAMERA_VIDEO_REQUEST:
            if (resultCode == CreateGiftActivity.RESULT_OK) {
                if (viewSwitcher.getCurrentView() != video)
                    viewSwitcher.showNext();
                videoPathFinal = videoPath;
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(video);
                video.setMediaController(mediaController);
                video.setVideoURI(videoPathFinal);
            } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                Log.e(LOG_TAG, "Video capture failed.");
            }
            break;
        }
    }

    void loadPreferences() {
        Log.d(LOG_TAG, "loadPreferences");
        prefs = this.getPreferences(MODE_PRIVATE);
        if (prefs.contains(USER_ID_TAG))
            userID = prefs.getLong(USER_ID_TAG, 0);
    }

    void savePreferences() {
        Log.d(LOG_TAG, "savePreferences");
        SharedPreferences.Editor ed = prefs.edit();
        ed.putLong(USER_ID_TAG, 0);
        ed.commit();
    }

    Gift makeGiftDataFromUI(long key) throws RemoteException {
        String title = editTextToString(titleInput);
        String description = editTextToString(descriptionInput);
        String videoUri = uriToString(videoPathFinal);
        String imageData = uriToString(imagePathFinal);
        String giftChainName = editTextToString(giftChain);
        Time created = new Time();
        created.setToNow();

        // TODO we re-use the gift chain map here - so if someone adds a gift
        // chain in the meantime it will not show up

        long giftChainID;
        if (giftChains.containsKey(giftChainName)) {
            giftChainID = giftChains.get(giftChainName);
        } else {
            GiftChain giftChain = new GiftChain(-1, giftChainName);
            giftChainID = service.giftChains().insert(giftChain);
            giftChains.put(giftChainName, giftChainID);
        }
        return new Gift(key, title, description, videoUri, imageData, created.toMillis(false),
                userID, giftChainName);
    }

    void initializeSpinner() {

        // TODO this will not scale with the number of gift chains!

        ArrayList<String> giftChainNames = new ArrayList<String>();
        try {
            Collection<GiftChain> results = service.giftChains().query();
            for (GiftChain result : results) {
                giftChains.put(result.giftChainName, result.giftChainID);
                giftChainNames.add(result.giftChainName);
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, giftChainNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        giftChain.setAdapter(spinnerArrayAdapter);
    }
}