package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

/**
 * The activity that allows a user to create and save a Gift.
 */
public class CreateGiftActivity extends ViewGiftActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the UI
        createActionBar();
        setContentView(R.layout.create_gift_activity);
        getActionBar().show();

        // Get references to all the UI elements
        imageView = (ImageView) findViewById(R.id.gift_create_img);
        titleInput = (EditText) findViewById(R.id.gift_create_title);
        descriptionInput = (EditText) findViewById(R.id.gift_create_description);

        resolver = new PotlatchResolver(this);
    }
}