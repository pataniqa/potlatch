package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.pataniqa.coursera.potlatch.R;

/**
 * The activity that allows the user to provide login information.
 */
public class LoginActivity extends GiftActivity {

    private static final String LOG_TAG = LoginActivity.class.getCanonicalName();

    // The edit texts used
    private EditText loginId;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.login_activity);

        // Find the edit texts
        loginId = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }

    public void loginClicked(View v) {
        Log.d(LOG_TAG, "loginClicked");
        openListGiftActivity(DEFAULT_TITLE_QUERY);
    }
}