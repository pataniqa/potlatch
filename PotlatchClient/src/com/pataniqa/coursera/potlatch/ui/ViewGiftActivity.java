package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
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
        CAMERA_PHOTO, GALLERY_PHOTO, CAMERA_VIDEO
    };

    // The various UI elements we use
    @InjectView(R.id.gift_create_title) EditText titleInput;
    @InjectView(R.id.gift_create_description) EditText descriptionInput;
    @InjectView(R.id.gift_create_img) ImageView image;
    @InjectView(R.id.view_gift_viewswitcher) ViewSwitcher viewSwitcher;
    @InjectView(R.id.gift_create_video) VideoView video;
    @InjectView(R.id.gift_create_gift_chain) AutoCompleteTextView giftChain;
    @InjectView(R.id.gift_create_save_button) ImageButton saveButton;
    @InjectView(R.id.gift_edit_delete_button) ImageButton deleteButton;

    protected Uri imagePathFinal = null;
    protected Uri videoPathFinal = null;
    private Map<String, Long> giftChains = new HashMap<String, Long>();

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath = null;
    private static Uri videoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
    }

    public void selectPhotoButtonClicked(View view) {
        Log.v(LOG_TAG, "selectPhotoClicked");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Request.GALLERY_PHOTO.ordinal());
    }

    public void addPhotoButtonClicked(View aView) {
        Log.v(LOG_TAG, "addPhotoClicked");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = LocalStorageUtilities.getOutputMediaFileUri(this,
                LocalStorageUtilities.MediaType.IMAGE,
                LocalStorageUtilities.Security.PUBLIC,
                null);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imagePath);
        startActivityForResult(cameraIntent, Request.CAMERA_PHOTO.ordinal());
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
        startActivityForResult(intent, Request.CAMERA_VIDEO.ordinal());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode
                + "data:" + data);

        switch (Request.values()[requestCode]) {
        case CAMERA_PHOTO:
            if (resultCode == Activity.RESULT_OK) {
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
                    float rotation = ImageUtils.getPhotoOrientation(this, imageFile);
                    image.setRotation(rotation);
                    saveButton.setVisibility(View.VISIBLE);
                } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                    Log.e(LOG_TAG, "Image capture failed.");
                }
            }
            break;
        case GALLERY_PHOTO:
            if (viewSwitcher.getCurrentView() != image)
                viewSwitcher.showPrevious();
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            File imageFile = new File(picturePath);
            imagePathFinal = Uri.fromFile(imageFile);
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(thumbnail);
            image.setScaleType(ScaleType.FIT_CENTER);
            float rotation = ImageUtils.getPhotoOrientation(this, imageFile);
            image.setRotation(rotation);
            saveButton.setVisibility(View.VISIBLE);
            break;
        case CAMERA_VIDEO:
            if (resultCode == Activity.RESULT_OK) {
                Log.i(LOG_TAG, "Video capture completed: " + videoPath);
                if (viewSwitcher.getCurrentView() != video)
                    viewSwitcher.showNext();
                videoPathFinal = videoPath;
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(video);
                video.setMediaController(mediaController);
                video.setVideoURI(videoPathFinal);
                saveButton.setVisibility(View.VISIBLE);
            } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                Log.e(LOG_TAG, "Video capture failed.");
            }
            break;
        }
    }

    Observable<Gift> makeGiftDataFromUI(final long key) {
        final String giftChainName = editTextToString(giftChain);
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
                Log.d(LOG_TAG, "Creating gift for giftchain " + result);
                String title = editTextToString(titleInput);
                String description = editTextToString(descriptionInput);
                String videoUri = uriToString(videoPathFinal);
                String imageData = uriToString(imagePathFinal);
                Time created = new Time();
                created.setToNow();
                return new Gift(key, title, description, videoUri, imageData, new Date(created
                        .toMillis(false)), getUserID(), giftChainID);
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