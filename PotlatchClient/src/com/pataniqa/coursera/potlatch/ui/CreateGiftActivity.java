package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.service.UploadService;

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
        Observable<Gift> gift = makeGiftDataFromUI(GetId.UNDEFINED_ID)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Gift, Observable<Gift>>() {
                    @Override
                    public Observable<Gift> call(Gift gift) {
                        Log.d(LOG_TAG, "newGiftData: " + gift);
                        return service.gifts().save(gift);
                    }
                });
        gift.forEach(new Action1<Gift>() {
            @Override
            public void call(Gift gift) {
                File imageFile = new File(Uri.parse(gift.getImageUri()).getPath());
                String endpoint = "https://192.168.1.71:8443";
                String client = "mobile";
                UploadService.startUpload(context,
                        gift.getId(),
                        true,
                        imageFile,
                        endpoint,
                        getUserName(),
                        getPassword(),
                        client);
                finish();
            }
        });
    }
}