package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
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
import com.pataniqa.coursera.potlatch.model.ClientGift;
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

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath = null;
    private static Uri videoPath = null;
    Uri imagePathFinal = null;
    Uri videoPathFinal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void createButtonClicked(View v) {
        Log.d(LOG_TAG, "createButtonClicked");
        try {
            ClientGift gift = makeGiftDataFromUI(-1);
            Log.d(LOG_TAG, "newGiftData:" + gift);
            resolver.insert(gift);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
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

    ClientGift makeGiftDataFromUI(long key) {
        String title = editTextToString(titleInput);
        String description = editTextToString(descriptionInput);
        String videoUri = uriToString(videoPathFinal);
        String imageData = uriToString(imagePathFinal);
        String giftChainName = editTextToString(giftChain);
        Time created = new Time();
        created.setToNow();

        // TODO need to handle userID properly

        long userID = 0;
        return new ClientGift(key, title, description, videoUri, imageData, created, userID, giftChainName);
    }

    void initializeSpinner() {
        String[] s = { "Cars", "Tractors" };
        ArrayList<String> spinnerArray = new ArrayList<String>(Arrays.asList(s));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        giftChain.setAdapter(spinnerArrayAdapter);
    }
}