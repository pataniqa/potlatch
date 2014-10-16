package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.local.LocalStorageUtilities;
import com.pataniqa.coursera.potlatch.utils.ImageUtils;

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
    @InjectView(R.id.gift_create_gift_chain) AutoCompleteTextView giftChain;
    @InjectView(R.id.gift_create_save_button) ImageButton saveButton;
    @InjectView(R.id.gift_edit_delete_button) ImageButton deleteButton;
    @InjectView(R.id.gift_select_image_button) ImageButton selectImageButton;
    @InjectView(R.id.gift_new_image_button) ImageButton newImageButton;
    @InjectView(R.id.gift_new_video_button) ImageButton newVideoButton;

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
        imagePath = getNewFile(LocalStorageUtilities.MediaType.IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
        startActivityForResult(cameraIntent, Request.CAMERA_PHOTO.ordinal());
    }

    public void addVideoButtonClicked(View v) {
        Log.v(LOG_TAG, "addVideoClicked(View) called.");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoPath = getNewFile(LocalStorageUtilities.MediaType.VIDEO);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, Request.CAMERA_VIDEO.ordinal());
    }

    private Uri getNewFile(LocalStorageUtilities.MediaType type) {
        return LocalStorageUtilities.getOutputMediaFileUri(this,
                type,
                LocalStorageUtilities.Security.PUBLIC,
                null);
    }

    public void detailButtonClicked(View v) {
        if (videoPathFinal != null) {
            Intent intent = new Intent(this, VideoDetailActivity.class);
            intent.putExtra(VIDEO_URL_TAG, uriToString(videoPathFinal));
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ImageDetailActivity.class);
            intent.putExtra(IMAGE_URL_TAG, uriToString(imagePathFinal));
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode
                + "data:" + data);

        switch (Request.values()[requestCode]) {
        case CAMERA_PHOTO:
            if (resultCode == Activity.RESULT_OK) {
                this.imagePathFinal = imagePath;
                displayBitmap(imagePathFinal.getPath());
                readyToSave();
            } else {
                Log.e(LOG_TAG, "Image capture failed.");
            }
            break;
        case GALLERY_PHOTO:
            if (resultCode == Activity.RESULT_OK) {
                String picturePath = getImageUriFromGallery(data);
                this.imagePathFinal = Uri.fromFile(new File(picturePath));
                displayBitmap(imagePathFinal.getPath());
                readyToSave();
            } else {
                Log.e(LOG_TAG, "Image selection failed.");
            }
            break;
        case CAMERA_VIDEO:
            if (resultCode == Activity.RESULT_OK) {
                Log.d(LOG_TAG, "Video capture completed: " + videoPath);
                videoPathFinal = videoPath;
                this.imagePathFinal = createVideoThumbnail(new File(videoPathFinal.getPath()));
                displayBitmap(imagePathFinal.getPath());
                readyToSave();
            } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                Log.e(LOG_TAG, "Video capture failed.");
            }
            break;
        }
    }

    Uri createVideoThumbnail(File videoFile) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        Uri thumbnailImagePath = getNewFile(LocalStorageUtilities.MediaType.IMAGE);
        File file = new File(thumbnailImagePath.getPath());
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            thumb.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Log.d(LOG_TAG, "Created new thumbnail image: " + thumbnailImagePath.getPath());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return thumbnailImagePath;
    }

    String getImageUriFromGallery(Intent data) {
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(data.getData(), filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        cursor.close();
        return imagePath;
    }

    void displayBitmap(String path) {
        File imageFile = new File(path);
        if (imageFile != null && imageFile.exists()) {
            Bitmap bitmap = ImageUtils.fileToBitmap(path, image.getWidth(), image.getHeight());
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(bitmap);
            image.setScaleType(ScaleType.FIT_CENTER);
        } else {
            Log.e(LOG_TAG, "Failed to find image.");
        }
    }

    void readyToSave() {
        saveButton.setVisibility(View.VISIBLE);
        selectImageButton.setVisibility(View.GONE);
        newImageButton.setVisibility(View.GONE);
        newVideoButton.setVisibility(View.GONE);
        image.setBackgroundColor(0x00000000);
    }

    Observable<Gift> makeGiftDataFromUI(final long key) {
        final String giftChainName = editTextToString(giftChain);
        Observable<GiftChain> result = null;
        if (giftChains.containsKey(giftChainName)) {
            GiftChain giftChain = new GiftChain(giftChains.get(giftChainName), giftChainName);
            result = Observable.just(giftChain);
        } else {
            GiftChain giftChain = new GiftChain(GetId.UNDEFINED_ID, giftChainName);
            result = service.giftChains().save(giftChain).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
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
        final Context context = this;
        service.giftChains().findAll().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<ArrayList<GiftChain>>() {
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
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        giftChain.setAdapter(spinnerArrayAdapter);
                    }
                });
    }
}