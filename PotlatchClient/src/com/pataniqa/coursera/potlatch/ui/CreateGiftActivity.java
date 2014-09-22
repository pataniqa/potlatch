package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.store.local.LocalGiftStore;

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
        setContentView(R.layout.create_gift_activity);
        ButterKnife.inject(this);
        
        resolver = new LocalGiftStore(this);
        
        initializeSpinner();
    }
}