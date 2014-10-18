package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.HasId;
import com.pataniqa.coursera.potlatch.utils.UploadService;

/**
 * The activity that allows a user to create and save a Gift.
 */
public class CreateGiftActivity extends ViewGiftActivity {

    private final static String LOG_TAG = CreateGiftActivity.class.getCanonicalName();

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
    }

    public void saveButtonClicked(View v) {
        Log.d(LOG_TAG, "createButtonClicked");
        final Context context = this;

        // create the gift from the UI

        makeGiftDataFromUI(HasId.UNDEFINED_ID).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).forEach(new Action1<Gift>() {
                    @Override
                    public void call(final Gift gift) {
                        Log.d(LOG_TAG, "newGiftData: " + gift);
                        getDataService().gifts().save(gift).forEach(new Action1<Gift>() {
                            @Override
                            public void call(final Gift serverGift) {

                                // update the local gift with the ID from the
                                // server

                                Log.d(LOG_TAG, "The server returned gift ID: " + serverGift.getId());
                                gift.setId(serverGift.getId());
                                Log.d(LOG_TAG,
                                        "Starting service to upload image " + gift.getImageUri());
                                File imageFile = new File(Uri.parse(gift.getImageUri()).getPath());
                                UploadService.startUpload(context,
                                        gift.getId(),
                                        true,
                                        imageFile,
                                        getEndpoint(),
                                        getUserName(),
                                        getPassword(),
                                        GiftActivity.CLIENT_ID);
                                Log.d(LOG_TAG, "Request successfully sent to upload service");
                                if (gift.getVideoUri() != null && !gift.getVideoUri().isEmpty()) {

                                    // if the gift has a video, upload that to
                                    // the server

                                    Log.d(LOG_TAG,
                                            "Starting service to upload video "
                                                    + gift.getVideoUri());
                                    File videoFile = new File(Uri.parse(gift.getVideoUri())
                                            .getPath());
                                    UploadService.startUpload(context,
                                            gift.getId(),
                                            false,
                                            videoFile,
                                            getEndpoint(),
                                            getUserName(),
                                            getPassword(),
                                            GiftActivity.CLIENT_ID);
                                    Log.d(LOG_TAG, "Request successfully sent to upload service");
                                }
                                Log.d(LOG_TAG, "Calling finish()");
                                finish();
                            }
                        });
                    }
                });
    }

}