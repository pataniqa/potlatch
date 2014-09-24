package com.pataniqa.coursera.potlatch.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView.ScaleType;
import android.widget.MediaController;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.model.Gift;

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

        initializeSpinner();

        // set the EditTexts to this Gift's Values
        setValuesToDefault();
    }

    boolean setValuesToDefault() {
        Log.d(LOG_TAG, "setValuesToDefault");
        try {
            ClientGift gift = service.gifts().get(getRowIdentifier());
            Log.d(LOG_TAG, "setValuesToDefault :" + gift);
            if (gift != null) {
                // set the EditTexts to the current values
                titleInput.setText(gift.title);
                descriptionInput.setText(gift.description);
                
                if (gift.videoUri != null && !gift.videoUri.isEmpty()) {
                    if (viewSwitcher.getCurrentView() != video)
                        viewSwitcher.showNext();
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(video);
                    video.setMediaController(mediaController);
                    video.setVideoURI(Uri.parse(gift.videoUri));
                } else {
                    if (viewSwitcher.getCurrentView() != image)
                        viewSwitcher.showPrevious();
                    image.setImageURI(Uri.parse(gift.imageUri));
                    image.setVisibility(View.VISIBLE);
                    image.setScaleType(ScaleType.FIT_CENTER);
                }
                
                if (gift.giftChainName != null && !gift.giftChainName.isEmpty()) 
                    giftChain.setText(gift.giftChainName);
                
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
            Gift gift = makeGiftDataFromUI(getRowIdentifier());
            Log.d(LOG_TAG, "newGiftData:" + gift);
            service.userGifts().update(gift);
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
            service.userGifts().delete(identifier);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }
}
