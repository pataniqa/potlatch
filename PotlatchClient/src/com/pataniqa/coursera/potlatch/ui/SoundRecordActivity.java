package com.pataniqa.coursera.potlatch.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

// http://developer.android.com/guide/topics/media/audio-capture.html
/**
 * Activity to capture Audio
 * 
 * @see <a href=
 *      'http://developer.android.com/guide/topics/media/audio-capture.html'>http://developer.android.com/guide/topics/media/audio-capture.ht
 *      m l < / a >
 */
public class SoundRecordActivity extends Activity {

	// A tag used for debugging with Logcat
    private static final String LOG_TAG = SoundRecordActivity.class.getName();
	
	// We return this result to activities when we properly capture the audio
	public static final int RESULT_OK = -1;
	
	// The filename we'll store the audio at
	private static String mFileName = null;

	// A privately defined button that starts/stops the MediaRecorder
    private RecordButton mRecordButton = null;
    // Provided by the Android framework to help record audio
    private MediaRecorder mRecorder = null;

    // A privately defined button that starts/stops the MediaPlayer
    private PlayButton mPlayButton = null;
    // Provided by the Android framework to help play audio
    private MediaPlayer mPlayer = null;

    // The button to quit this activity
    private Button mDoneButton = null;
    
    // Called when the record button is pressed
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    // Called when the play button is pressed
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    // Starts the MediaPlayer
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);	
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    // Stops the MediaPlayer
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    // Starts the MediaRecorder
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);	
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();	
    }

    // Stops the MediaRecorder
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    // The Record Button & its logic
    class RecordButton extends Button {
    	// Used to keep track of if we should start or stop recording
        boolean mStartRecording = true;

        // The click listener used to respond to clicks
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
            	// Forward actual recording logic to the activity function
                onRecord(mStartRecording);
                
                // If we're starting to record, show the Stop Recording text on this button
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                	// Otherwise, set the result of the recording in an intent
                    Intent data = new Intent();
                    data.putExtra("data", mFileName);
                    setResult(RESULT_OK, data);
                    
                    // Show the Start recording text on this button
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    // The Play button & its logic
    class PlayButton extends Button {
    	// Used to keep track of if we should start or stop playing audio
        boolean mStartPlaying = true;

        // The click listener used to respond to clicks on this button
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
            	// Forward actual audio playing logic to the activity
                onPlay(mStartPlaying);
                
                // If we should start playing, show the "Stop Playing" button
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                	// Otherwise, show the "Start Playing Button"
                    setText("Start playing");
                }
                
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    @Override
    /**
     * Sets up Activity's View
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        //Get the filename to store audio data from the intent used to start this activity
        mFileName = getIntent().getStringExtra("FILENAME");
        
        //Add our custom buttons to a linear layout
        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mDoneButton = new Button(this);
        mDoneButton.setText("Done");
        ll.addView(mDoneButton, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        
        //When we're done, return to the calling activity
        mDoneButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// Release the MediaRecorder/MediaPlayer if they're still being used.
				if (mRecorder != null) {
					mRecorder.stop();
					mRecorder.release();
				}
				if (mPlayer != null)	{
					mPlayer.release();
				}
				
				// Finish
				finish();
			}
		});
        
        //Display the linear layout
        setContentView(ll);
    }

    @Override
    /**
     * Handle onPause to release the media Recorder and Player instances.
     * @see android.app.Activity#onPause()
     */
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    /**
     * @see android.app.Activity#onStop()
     */
    public void onStop() {
        super.onStop();
    }
}
