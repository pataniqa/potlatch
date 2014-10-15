package com.pataniqa.coursera.potlatch.ui;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;

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

    public void createButtonClicked(View v) {
        Log.d(LOG_TAG, "createButtonClicked");
        Observable<Gift> gift = makeGiftDataFromUI(GetId.UNDEFINED_ID)
                .flatMap(new Func1<Gift, Observable<Gift>>() {
                    @Override
                    public Observable<Gift> call(Gift gift) {
                        Log.d(LOG_TAG, "newGiftData: " + gift);
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
}