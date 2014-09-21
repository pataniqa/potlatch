package com.pataniqa.coursera.potlatch.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView.ScaleType;
import butterknife.ButterKnife;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class EditGiftActivity extends ViewGiftActivity {

    private final static String LOG_TAG = EditGiftActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        createActionBar();
        setContentView(R.layout.edit_gift_activity);
        getActionBar().show();
        ButterKnife.inject(this);

        resolver = new PotlatchResolver(this);

        // set the EditTexts to this Gift's Values
        setValuesToDefault();
    }

    private boolean setValuesToDefault() {
        Log.d(LOG_TAG, "setValuesToDefault");
        try {
            GiftData gift = resolver.getGiftDataViaRowID(getRowIdentifier());
            Log.d(LOG_TAG, "setValuesToDefualt :" + gift);
            if (gift != null) {
                // set the EditTexts to the current values
                titleInput.setText(gift.title);
                descriptionInput.setText(gift.description);
                imageView.setImageURI(Uri.parse(gift.imageUri));
                imageView.setVisibility(View.VISIBLE);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                imagePathFinal = stringToUri(gift.imageUri);
                videoPathFinal = stringToUri(gift.videoUri);
                return true;
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        return false;
    }

    public void saveButtonClicked(View v) {
        Log.d(LOG_TAG, "saveButtonClicked");
        try {
            GiftData gift = makeGiftDataFromUI(getRowIdentifier());
            Log.d(LOG_TAG, "newGiftData:" + gift);
            resolver.updateGiftWithID(gift);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }

    public void deleteButtonClicked(View v) {
        Log.d(LOG_TAG, "deleteButtonClicked");
        try {
            resolver.deleteAllGiftWithRowID(getRowIdentifier());
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Caught RemoteException => " + e.getMessage(), e);
        }
        finish();
    }
}
