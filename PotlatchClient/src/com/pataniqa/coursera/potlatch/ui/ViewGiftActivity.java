package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.local.LocalStorageUtilities;

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
    @InjectView(R.id.gift_create_title) EditText titleInput;
    @InjectView(R.id.gift_create_description) EditText descriptionInput;
    @InjectView(R.id.gift_create_img) ImageView image;
    @InjectView(R.id.view_gift_viewswitcher) ViewSwitcher viewSwitcher;
    @InjectView(R.id.gift_create_video) VideoView video;
    @InjectView(R.id.gift_create_gift_chain) AutoCompleteTextView giftChain;

    protected Uri imagePathFinal = null;
    protected Uri videoPathFinal = null;
    private Map<String, Long> giftChains = new HashMap<String, Long>();

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

    Observable<Gift> makeGiftDataFromUI(final long key) {
        String giftChainName = editTextToString(giftChain);
        Observable<GiftChain> result = null;
        if (giftChains.containsKey(giftChainName)) {
            GiftChain giftChain = new GiftChain(giftChains.get(giftChainName), giftChainName);
            result = Observable.just(giftChain);
        } else {
            GiftChain giftChain = new GiftChain(GetId.UNDEFINED_ID, giftChainName);
            result = service.giftChains().save(giftChain);
        }
        return result.map(new Func1<GiftChain, Gift>() {
            @Override
            public Gift call(GiftChain result) {
                long giftChainID = result.getId();
                Log.d(LOG_TAG, "Creating gift");
                String title = editTextToString(titleInput);
                String description = editTextToString(descriptionInput);
                String videoUri = uriToString(videoPathFinal);
                String imageData = uriToString(imagePathFinal);
                Time created = new Time();
                created.setToNow();
                return new Gift(key, title, description, videoUri, imageData, new Date(created
                        .toMillis(false)), userID, giftChainID);
            }
        });

    }

    void initializeSpinner() {

        // TODO this will not scale with the number of gift chains!

        final Context context = this;
        Observable<ArrayList<GiftChain>> results = service.giftChains().findAll();
        results.forEach(new Action1<ArrayList<GiftChain>>() {
            @Override
            public void call(ArrayList<GiftChain> results) {
                ArrayList<String> giftChainNames = new ArrayList<String>();
                for (GiftChain result : results) {
                    giftChains.put(result.getName(), result.getId());
                    giftChainNames.add(result.getName());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        giftChainNames);
                spinnerArrayAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                giftChain.setAdapter(spinnerArrayAdapter);
            }
        });
    }
}