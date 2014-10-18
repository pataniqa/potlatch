package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;

public class VideoDetailActivity extends GiftActivity {

    private static final String LOG_TAG = VideoDetailActivity.class.getCanonicalName();

    @InjectView(R.id.gift_video_detail) VideoView video;
    @InjectView(R.id.gift_video_detail_ctrlActivityIndicator) ProgressBar progressBar;

    private File outputFile = null;

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
        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        final Context context = this;
        if (useLocalStore()) {

            // The video is local

            String videoUrl = intent.getStringExtra(VIDEO_URL_TAG);
            Log.d(LOG_TAG, "Displaying image " + videoUrl);
            video.setMediaController(mediaController);
            video.setVideoURI(Uri.parse(videoUrl));
            video.requestFocus();
            video.start();
        } else {

            // the video is remote - download it to a temporary file

            long giftId = intent.getLongExtra(GIFT_ID_TAG, 0);
            progressBar.setVisibility(View.VISIBLE);
            getDataService().gifts().getVideoData(giftId).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).forEach(new Action1<Response>() {
                        @Override
                        public void call(Response response) {
                            try {
                                InputStream in = response.getBody().in();
                                final File outputDir = context.getCacheDir();
                                outputFile = File.createTempFile("potlatch", "mp4", outputDir);
                                FileUtils.copyInputStreamToFile(in, outputFile);
                                video.setMediaController(mediaController);
                                video.setVideoURI(Uri.fromFile(outputFile));
                                progressBar.setVisibility(View.INVISIBLE);
                                video.requestFocus();
                                video.start();
                            } catch (Exception e) {
                                Log.e(LOG_TAG, e.getMessage(), e);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (outputFile != null) {
            FileUtils.deleteQuietly(outputFile);
            outputFile = null;
        }
    }
}
