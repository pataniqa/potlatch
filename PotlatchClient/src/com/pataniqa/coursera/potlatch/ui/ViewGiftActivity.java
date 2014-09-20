package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.IPotlatchStore;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

/**
 * This activity lets a user view a Gift in full screen mode.
 */
public class ViewGiftActivity extends GiftActivityBase {

    private static final String LOG_TAG = ViewGiftActivity.class.getCanonicalName();

    // Used to pass around the row ID of stories
    public final static String rowIdentifyerTAG = "index";

    // The GiftData we're displaying
    private GiftData giftData;

    // The UI elements we'll be using
    private TextView titleTV;
    private TextView descriptionTV;
    private TextView videoUriTV;
    private ImageView imageView;

    // Helps us retrieve data from the database
    private IPotlatchStore resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the UI
        setContentView(R.layout.view_gift_activity);

        // Create a resolver to help us retrieve data from the database
        resolver = new PotlatchResolver(this);

        // Get actual references to the instantiated UI objects
        titleTV = (TextView) findViewById(R.id.gift_view_value_title);
        descriptionTV = (TextView) findViewById(R.id.gift_view_value_description);
        videoUriTV = (TextView) findViewById(R.id.gift_view_value_video_uri);
        imageView = (ImageView) findViewById(R.id.gift_view_value_image_uri);

        // Set the default values
        titleTV.setText("");
        descriptionTV.setText("");
        videoUriTV.setText("");

        try {
            // Fill out all the UI elements with data from our GiftData
            setUiToGiftData(getUniqueKey());
        } catch (RemoteException e) {
            Toast.makeText(this, "Error retrieving information from local data store.",
                    Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "Error getting Gift data from C.P.");
        }

    }

    public void setUiToGiftData(long getUniqueKey) throws RemoteException {
        Log.d(LOG_TAG, "setUiToGiftData");

        // Get the GiftData from the database
        giftData = resolver.getGiftDataViaRowID(getUniqueKey);

        if (giftData != null) {
            Log.d(LOG_TAG, "setUiToGiftData + GiftData:" + giftData.toString());

            // Fill in the appropriate UI elements
            titleTV.setText(String.valueOf(giftData.title).toString());
            descriptionTV.setText(String.valueOf(giftData.description).toString());
            videoUriTV.setText(String.valueOf(giftData.videoUri).toString());

            Log.d(LOG_TAG, "image file path: " + giftData.imageUri);

            // Set up image
            imageView.setVisibility(View.GONE);

            if (giftData.imageUri != null && giftData.imageUri.equals("") == false
                    && giftData.imageUri.equals("null") == false) {
                Log.d(LOG_TAG, "image URI is valid" + giftData.imageUri);
                Uri uri = Uri.parse(giftData.imageUri);
                File image = new File(uri.getPath());

                if (image != null && image.exists()) {
                    Log.d(LOG_TAG, "image URI is valid");

                    Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp);
                }
            }

        }
    }

    private void editButtonPressed() {
        openEditGiftActivity(giftData.KEY_ID);
    }

    private void deleteButtonPressed() {
        String message;

        message = getResources().getString(R.string.confirm_delete);

        // Show a dialog confirming that the user wants to delete this Gift
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.closing_activity)
                .setMessage(message)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    resolver.deleteAllGiftWithRowID(giftData.KEY_ID);
                                } catch (RemoteException e) {
                                    Log.e(LOG_TAG, "RemoteException Caught => " + e.getMessage());
                                    e.printStackTrace();
                                }

                                finish();

                            }

                        }).setNegativeButton(R.string.no, null).show();
    }

    public long getUniqueKey() {
        return getIntent().getLongExtra(rowIdentifyerTAG, 0);
    }

    public void clickListener(View view) {
        switch (view.getId()) {
        case R.id.button_gift_view_to_delete:
            deleteButtonPressed();
            break;
        case R.id.button_gift_view_to_edit:
            editButtonPressed();
            break;
        default:
            break;
        }
    }
}
