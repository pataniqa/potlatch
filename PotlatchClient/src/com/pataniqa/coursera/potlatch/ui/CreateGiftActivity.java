package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.HasID;

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
        
        initializeSpinner();
    }
    
    public void createButtonClicked(View v) {
        Log.d(LOG_TAG, "createButtonClicked");
        try {
            Gift gift = makeGiftDataFromUI(HasID.UNDEFINED_ID);
            Log.d(LOG_TAG, "newGiftData:" + gift);
            service.userGifts().save(gift);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }
}