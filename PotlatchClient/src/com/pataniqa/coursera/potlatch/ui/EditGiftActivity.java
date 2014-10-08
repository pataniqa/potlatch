package com.pataniqa.coursera.potlatch.ui;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView.ScaleType;
import android.widget.MediaController;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

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

    Observable<Boolean> setValuesToDefault() {
        Log.d(LOG_TAG, "setValuesToDefault");
        Observable<GiftResult> gift = service.gifts().findOne(getRowIdentifier());
        final Context context = this;
        return gift.map(new Func1<GiftResult, Boolean>() {

            @Override
            public Boolean call(GiftResult gift) {
                Log.d(LOG_TAG, "setValuesToDefault :" + gift);
                if (gift != null) {
                    // set the EditTexts to the current values
                    titleInput.setText(gift.getTitle());
                    descriptionInput.setText(gift.getDescription());

                    if (gift.getVideoUri() != null && !gift.getVideoUri().isEmpty()) {
                        if (viewSwitcher.getCurrentView() != video)
                            viewSwitcher.showNext();
                        MediaController mediaController = new MediaController(context);
                        mediaController.setAnchorView(video);
                        video.setMediaController(mediaController);
                        video.setVideoURI(Uri.parse(gift.getVideoUri()));
                    } else {
                        if (viewSwitcher.getCurrentView() != image)
                            viewSwitcher.showPrevious();
                        image.setImageURI(Uri.parse(gift.getImageUri()));
                        image.setVisibility(View.VISIBLE);
                        image.setScaleType(ScaleType.FIT_CENTER);
                    }

                    if (gift.getGiftChainName() != null && !gift.getGiftChainName().isEmpty())
                        giftChain.setText(gift.getGiftChainName());

                    // TODO clicking the image should display a higher
                    // resolution version

                    // TODO or in the case of a video play the video

                    imagePathFinal = stringToUri(gift.getImageUri());
                    videoPathFinal = stringToUri(gift.getVideoUri());
                    return true;
                }
                return false;
            }

        });
    }

    public void saveButtonClicked(View v) {
        Log.d(LOG_TAG, "saveButtonClicked");
        Observable<Gift> gift = makeGiftDataFromUI(getRowIdentifier())
                .flatMap(new Func1<Gift, Observable<Gift>>() {
                    @Override
                    public Observable<Gift> call(Gift gift) {
                        Log.d(LOG_TAG, "newGiftData:" + gift);
                        return service.gifts().save(gift);
                    }
                });
        gift.forEach(new Action1<Gift>() {
            @Override
            public void call(Gift arg0) {
                finish();
            }
        });
    }

    public void deleteButtonClicked(View v) {
        Log.d(LOG_TAG, "deleteButtonClicked");
        long identifier = getRowIdentifier();
        Log.d(LOG_TAG, "Deleting gift with " + identifier);
        service.gifts().delete(identifier);
        finish();
    }
}
