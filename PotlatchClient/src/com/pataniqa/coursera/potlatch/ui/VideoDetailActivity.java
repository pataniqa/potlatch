package com.pataniqa.coursera.potlatch.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;

public class VideoDetailActivity extends GiftActivity {

    private static final String LOG_TAG = VideoDetailActivity.class.getCanonicalName();

    @InjectView(R.id.gift_video_detail) VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.video_detail);

        ButterKnife.inject(this);

        Intent intent = getIntent();

        String videoUrl = intent.getStringExtra(VIDEO_URL_TAG);
        Log.d(LOG_TAG, "Displaying image " + videoUrl);
        if (!videoUrl.isEmpty()) {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(Uri.parse(videoUrl));
            video.requestFocus();
            video.start();
        }
    }
}
