package com.pataniqa.coursera.potlatch.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class EditGiftActivity extends ViewGiftActivity {

    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    // variable for passing around row index
    public final static String rowIdentifyerTAG = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.edit_gift_activity);
        getActionBar().show();

        // Get references to all the UI elements
        imageView = (ImageView) findViewById(R.id.gift_create_img);
        titleInput = (EditText) findViewById(R.id.gift_create_title);
        descriptionInput = (EditText) findViewById(R.id.gift_create_description);

        // Start a resolver to help us store/retrieve data from a database
        resolver = new PotlatchResolver(this);

        // set the EditTexts to this Gift's Values
        setValuesToDefault();
    }
    
    private boolean setValuesToDefault() {
        try {
            GiftData giftData = resolver.getGiftDataViaRowID(getUniqueKey());
            if (giftData != null) {
                Log.d(LOG_TAG, "setValuesToDefualt :" + giftData.toString());

                // set the EditTexts to the current values
                titleInput.setText(giftData.title);
                descriptionInput.setText(giftData.description);
                imageView.setImageURI(Uri.parse(giftData.imageUri));
                imageView.setVisibility(View.VISIBLE);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                imagePathFinal = !giftData.imageUri.isEmpty() ? Uri.parse(giftData.imageUri) : null;
                videoPathFinal = !giftData.videoUri.isEmpty() ? Uri.parse(giftData.videoUri) : null;
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void saveButtonClicked(View v) {
        // Make the Gift data from the UI
        GiftData gift = makeGiftDataFromUI();
        if (gift != null) {
            try {
                resolver.updateGiftWithID(gift);
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        } else {
            return;
        }

        finish();
    }

    public void deleteButtonClicked(View v) {
        try {
            resolver.deleteAllGiftWithRowID(getUniqueKey());
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }
        finish();
    }

    protected long getUniqueKey() {
        return getIntent().getLongExtra(rowIdentifyerTAG, 0);
    }
}
