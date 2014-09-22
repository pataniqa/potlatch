package com.pataniqa.coursera.potlatch.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView.ScaleType;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.local.LocalGiftStore;

public class EditGiftActivity extends ViewGiftActivity {

    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.edit_gift_activity);
        ButterKnife.inject(this);

        resolver = new LocalGiftStore(this);

        // set the EditTexts to this Gift's Values
        setValuesToDefault();
    }

    boolean setValuesToDefault() {
        Log.d(LOG_TAG, "setValuesToDefault");
        try {
            ClientGift gift = resolver.get(getRowIdentifier());
            Log.d(LOG_TAG, "setValuesToDefualt :" + gift);
            if (gift != null) {
                // set the EditTexts to the current values
                titleInput.setText(gift.title);
                descriptionInput.setText(gift.description);
                image.setImageURI(Uri.parse(gift.imageUri));
                image.setVisibility(View.VISIBLE);
                image.setScaleType(ScaleType.FIT_CENTER);
                // TODO need a thumbnail for videos
                // TODO clicking the image should display a higher resolution version
                // TODO or in the case of a video play the video

                imagePathFinal = stringToUri(gift.imageUri);
                videoPathFinal = stringToUri(gift.videoUri);
                return true;
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        return false;
    }

    public void saveButtonClicked(View v) {
        Log.d(LOG_TAG, "saveButtonClicked");
        try {
            ClientGift gift = makeGiftDataFromUI(getRowIdentifier());
            Log.d(LOG_TAG, "newGiftData:" + gift);
            resolver.update(gift);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }

    public void deleteButtonClicked(View v) {
        Log.d(LOG_TAG, "deleteButtonClicked");
        try {
            long identifier = getRowIdentifier();
            Log.d(LOG_TAG, "Deleting gift with " + identifier);
            resolver.delete(identifier);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }
}
