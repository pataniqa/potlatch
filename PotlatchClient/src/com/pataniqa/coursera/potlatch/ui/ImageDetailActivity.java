package com.pataniqa.coursera.potlatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;

public class ImageDetailActivity extends GiftActivity {

    private static final String LOG_TAG = ImageDetailActivity.class.getCanonicalName();

    @InjectView(R.id.gift_image_detail) ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.image_detail);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        final String url = intent.getStringExtra(IMAGE_URL_TAG);
        Log.d(LOG_TAG, "Displaying image " + url);
        if (!url.isEmpty()) 
            getImage(url, image);
    }
}
