package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.IPotlatchStore;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class EditGiftActivity extends GiftActivityBase {

    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    // variable for passing around row index
    public final static String rowIdentifyerTAG = "index";

    // The TextViews and EditTexts we use
    private EditText titleET;
    private EditText descriptionET;
    private EditText imageUriET;

    // custom ContentResolver wrapper.
    private IPotlatchStore resolver;

    private GiftData giftData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        setContentView(R.layout.edit_gift_activity);

        // Start the Resolver to help us get/set data in the database
        resolver = new PotlatchResolver(this);

        // Get the EditTexts
        titleET = (EditText) findViewById(R.id.gift_edit_title);
        descriptionET = (EditText) findViewById(R.id.gift_edit_description);
        imageUriET = (EditText) findViewById(R.id.gift_edit_image_uri);

        // set the EditTexts to this Gift's Values
        setValuesToDefault();

    }

    public void clickListener(View v) {
        switch (v.getId()) {
        case R.id.gift_edit_button_save:
            doSaveButtonClick();
            break;
        case R.id.gift_edit_button_reset:
            doResetButtonClick();
            break;
        case R.id.gift_edit_button_cancel:
            doCancelButtonClick();
            break;
        default:
        }
    }

    public void doResetButtonClick() {
        setValuesToDefault();
    }

    public void doSaveButtonClick() {
        Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();

        // Make the Gift data from the UI
        GiftData Gift = makeGiftDataFromUI();

        // If we succeeded, go ahead and update the data in the database
        if (Gift != null) {
            try {
                resolver.updateGiftWithID(Gift);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        } else {
            return;
        }

        finish(); // same as hitting 'back' button

    }

    public GiftData makeGiftDataFromUI() {
        String title = titleET.getText().toString();
        String description = descriptionET.getText().toString();

        // Construct the Gift Data Object
        GiftData rValue = new GiftData(getUniqueKey(), title, description, 
                giftData.videoUri, giftData.imageUri);

        return rValue;

    }

    public void doCancelButtonClick() {
        finish(); 
    }

    public boolean setValuesToDefault() {

        GiftData GiftData;
        try {
            GiftData = resolver.getGiftDataViaRowID(getUniqueKey());
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "" + e.getMessage());
            e.printStackTrace();
            return false;
        }

        if (GiftData != null) {
            giftData = GiftData;
            Log.d(LOG_TAG, "setValuesToDefualt :" + GiftData.toString());

            // set the EditTexts to the current values
            titleET.setText(GiftData.title);
            descriptionET.setText(GiftData.description);
            imageUriET.setText(GiftData.imageUri);
            return true;
        }
        return false;
    }

    public long getUniqueKey() {
        return getIntent().getLongExtra(rowIdentifyerTAG, 0);
    }
}
