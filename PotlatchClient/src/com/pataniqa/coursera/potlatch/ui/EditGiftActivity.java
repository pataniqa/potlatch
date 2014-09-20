package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.storage.GiftData;
import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;

/**
 * This activity allows users to edit some parts of a previously posted Gift
 */
public class EditGiftActivity extends GiftActivityBase {

    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    // variable for passing around row index
    public final static String rowIdentifyerTAG = "index";

    // The TextViews and EditTexts we use
    private TextView loginIdET;
    private TextView GiftIdET;
    private EditText titleET;
    private EditText bodyET;
    private EditText imageMetaDataET;

    // custom ContentResolver wrapper.
    private PotlatchResolver resolver;

    private GiftData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        setContentView(R.layout.edit_gift_activity);

        // Start the Resolver to help us get/set data in the database
        resolver = new PotlatchResolver(this);

        // Get the EditTexts
        loginIdET = (TextView) findViewById(R.id.gift_edit_login_id);
        GiftIdET = (TextView) findViewById(R.id.gift_edit_gift_id);
        titleET = (EditText) findViewById(R.id.gift_edit_title);
        bodyET = (EditText) findViewById(R.id.gift_edit_body);
        imageMetaDataET = (EditText) findViewById(R.id.gift_edit_image_meta_data);

        // set the EditTexts to this Gift's Values
        setValuesToDefault();

    }

    // The listener for the various buttons
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

    // Update the provided values in the database
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

    // Use the data the user input into the UI to construct a GiftData object
    public GiftData makeGiftDataFromUI() {

        // Get the editables from the UI
        Editable titleEditable = titleET.getText();
        Editable bodyEditable = bodyET.getText();

        // Pull values from Editables
        String title = titleEditable.toString();
        String body = bodyEditable.toString();

        // Construct the Gift Data Object
        GiftData rValue = new GiftData(getUniqueKey(), mData.loginId, mData.giftId, title, body, 
                mData.videoLink, mData.imageLink);

        // Make sure the new GiftData has the same key as the old one so that
        // it will
        // replace the old one in the database.
        rValue.key = mData.key;

        // return GiftData object with new values
        return rValue;

    }

    public void doCancelButtonClick() {
        finish(); // same as hitting 'back' button
    }

    /**
     * Sets all the UI elements to their original values
     * 
     * @return
     */
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
            mData = GiftData;
            Log.d(LOG_TAG, "setValuesToDefualt :" + GiftData.toString());

            // set the EditTexts to the current values
            loginIdET.setText(Long.valueOf(GiftData.loginId).toString());
            GiftIdET.setText(Long.valueOf(GiftData.giftId).toString());
            titleET.setText(GiftData.title);
            bodyET.setText(GiftData.body);
            imageMetaDataET.setText(GiftData.imageLink);
            return true;
        }
        return false;
    }

    /**
     * Returns the unique identifier of this GiftData in the database
     */
    public long getUniqueKey() {
        return getIntent().getLongExtra(rowIdentifyerTAG, 0);
    }
}
