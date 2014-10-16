package com.pataniqa.coursera.potlatch.ui;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
        setContentView(R.layout.view_gift);
        ButterKnife.inject(this);

        initializeSpinner();
        deleteButton.setVisibility(View.VISIBLE);
        readyToSave();

        // set the EditTexts to this Gift's Values
        setValuesToDefault();
    }

    void setValuesToDefault() {
        Log.d(LOG_TAG, "setValuesToDefault");
        getDataService().gifts().findOne(getRowIdentifier()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).map(new Func1<GiftResult, Boolean>() {
                    @Override
                    public Boolean call(GiftResult gift) {
                        Log.d(LOG_TAG, "setValuesToDefault :" + gift);
                        if (gift != null) {
                            // set the EditTexts to the current values
                            titleInput.setText(gift.getTitle());
                            descriptionInput.setText(gift.getDescription());

                            displayImage(Uri.parse(gift.getImageUri()).getPath());

                            if (gift.getGiftChainName() != null
                                    && !gift.getGiftChainName().isEmpty())
                                giftChain.setText(gift.getGiftChainName());

                            imagePathFinal = stringToUri(gift.getImageUri());
                            videoPathFinal = stringToUri(gift.getVideoUri());
                            image.requestFocus();
                            return true;
                        }
                        return false;
                    }

                }).forEach(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean arg0) {
                        Log.d(LOG_TAG, "Finished setting up item");
                    }
                });
    }

    public void saveButtonClicked(View v) {
        Log.d(LOG_TAG, "saveButtonClicked");
        makeGiftDataFromUI(getRowIdentifier()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Gift, Observable<Gift>>() {
                    @Override
                    public Observable<Gift> call(Gift gift) {
                        Log.d(LOG_TAG, "newGiftData:" + gift);
                        return getDataService().gifts().save(gift);
                    }
                }).forEach(new Action1<Gift>() {
                    @Override
                    public void call(Gift arg0) {
                        finish();
                    }
                });
    }

    public void deleteButtonClicked(View v) {
        Log.d(LOG_TAG, "deleteButtonClicked");
        final long identifier = getRowIdentifier();
        Log.d(LOG_TAG, "Deleting gift with " + identifier);
        getDataService().gifts().delete(identifier).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).forEach(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean arg0) {
                        finish();
                    }
                });
    }
}
