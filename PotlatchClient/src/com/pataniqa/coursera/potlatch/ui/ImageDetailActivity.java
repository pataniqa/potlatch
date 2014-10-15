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
import com.squareup.picasso.Picasso;

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
            Uri url = Uri.parse(imageUrl);

            // Keep getting out of memory errors so scale the image to
            // 50% of displays width / height
            // this looks ugly but works for now

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int maxsize = Math.min(size.x, size.y);

            Picasso.with(this)
                .load(url)
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
            // Always cancel the request here, this is safe to call even if the
            // image has been loaded.
            // This ensures that the anonymous callback we have does not prevent
            // the activity from
            // being garbage collected. It also prevents our callback from
            // getting invoked even after the
            // activity has finished.
            Picasso.with(this).cancelRequest(image);
        }
    }

}
