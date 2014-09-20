package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.IPotlatchStore;
import com.pataniqa.coursera.potlatch.store.LocalStorageUtilities;

/**
 * Abstract class that forms the basis of the CreateGiftActivity and
 * EditGiftActivity.
 */
abstract class ViewGiftActivity extends GiftActivity {

    private final static String LOG_TAG = CreateGiftActivity.class.getCanonicalName();

    // Used as the request codes in startActivityForResult().
    static final int CAMERA_PIC_REQUEST = 1;
    static final int GALLERY_PIC_REQUEST = 2;
    static final int CAMERA_VIDEO_REQUEST = 3;

    // The various UI elements we use
    protected EditText titleInput;
    protected EditText descriptionInput;
    protected ImageView imageView;

    // Making this static keeps it from getting GC'd when we take pictures
    private static Uri imagePath = null;
    private static Uri videoPath = null;
    protected Uri imagePathFinal = null;
    protected Uri videoPathFinal = null;

    protected IPotlatchStore resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void selectPhotoButtonClicked(View view) {
        Log.v(LOG_TAG, "selectPhotoClicked");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
    }

    public void addPhotoButtonClicked(View aView) {
        Log.v(LOG_TAG, "addPhotoClicked");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = LocalStorageUtilities
                .getOutputMediaFileUri(this, LocalStorageUtilities.MEDIA_TYPE_IMAGE,
                        LocalStorageUtilities.SECURITY_PUBLIC, null);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imagePath);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    public void addVideoButtonClicked(View v) {
        Log.v(LOG_TAG, "addVideoClicked(View) called.");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoPath = LocalStorageUtilities
                .getOutputMediaFileUri(this, LocalStorageUtilities.MEDIA_TYPE_VIDEO,
                        LocalStorageUtilities.SECURITY_PUBLIC, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_VIDEO_REQUEST);
    }

    public void createButtonClicked(View v) {
        Log.d(LOG_TAG, "buttonCreateClicked");
        GiftData newData = makeGiftDataFromUI();
        Log.d(LOG_TAG, "newGiftData:" + newData);

        try {
            resolver.insert(newData);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage());
            e.printStackTrace();
        }

        finish();
    }

    public void cancelButtonClicked(View v) {
        Log.d(LOG_TAG, "buttonCancelClicked");
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode
                + "data:" + data);

        switch (requestCode) {
        case CreateGiftActivity.CAMERA_PIC_REQUEST:
            if (resultCode == CreateGiftActivity.RESULT_OK) {
                imagePathFinal = imagePath;
                File image = new File(imagePathFinal.getPath());

                if (image != null && image.exists()) {
                    Log.d(LOG_TAG, "image URI is valid");
                    Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp);
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Image capture failed.",
                            Toast.LENGTH_LONG).show();
                }
            }
            break;
        case CreateGiftActivity.GALLERY_PIC_REQUEST:
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String picturePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            imagePathFinal = Uri.fromFile(new File(picturePath));
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(thumbnail);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            break;
        case CreateGiftActivity.CAMERA_VIDEO_REQUEST:
            if (resultCode == CreateGiftActivity.RESULT_OK) {
                videoPathFinal = videoPath;
            } else if (resultCode != CreateGiftActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Video capture failed.", Toast.LENGTH_LONG)
                        .show();
            }
            break;
        }
    }

    protected long getUniqueKey() {
        return -1;
    }

    protected GiftData makeGiftDataFromUI() {
        String title = String.valueOf(titleInput.getText().toString());
        String description = String.valueOf(descriptionInput.getText().toString());
        String videoUri = videoPathFinal != null ? videoPathFinal.toString() : "";
        String imageData = imagePathFinal != null ? imagePathFinal.toString() : "";
        return new GiftData(getUniqueKey(), title, description, videoUri, imageData);
    }

}