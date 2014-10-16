package com.pataniqa.coursera.potlatch.ui;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
        String imageUrl = intent.getStringExtra(IMAGE_URL_TAG);
        Log.d(LOG_TAG, "Displaying image " + imageUrl);
        if (!imageUrl.isEmpty()) {

            // Keep getting out of memory errors so scale the image to
            // 50% of displays width / height
            // this looks ugly but works for now

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int maxsize = Math.min(size.x, size.y);

            customPicasso(this)
                .load(Uri.parse(imageUrl))
                .resize(maxsize, maxsize)
                .placeholder(R.drawable.ic_fa_image)
                .centerInside()
                .into(image);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            customPicasso(this).cancelRequest(image);
        }
    }

}
