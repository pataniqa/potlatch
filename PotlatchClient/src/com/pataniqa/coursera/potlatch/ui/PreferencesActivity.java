package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class PreferencesActivity extends GiftActivity {

    private final static String LOG_TAG = PreferencesActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.preferences_activity);

        resolver = new PotlatchResolver(this);
    }

    public void acceptButtonClicked(View v) {
        Log.d(LOG_TAG, "acceptButtonClicked");
        finish();
    }

    public void cancelButtonClicked(View v) {
        Log.d(LOG_TAG, "cancelButtonClicked");
        finish();
    }

}
