package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.ClientGift;

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
            ClientGift gift = makeGiftDataFromUI(-1);
            Log.d(LOG_TAG, "newGiftData:" + gift);
            giftStore.insert(gift);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }
}