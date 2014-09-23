package com.pataniqa.coursera.potlatch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;

/**
 * The activity that allows the user to provide login information.
 */
public class LoginActivity extends GiftActivity {

    private static final String LOG_TAG = LoginActivity.class.getCanonicalName();

    @InjectView(R.id.login_username)
    EditText usernameET;
    @InjectView(R.id.login_password)
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.login_activity);
        
        ButterKnife.inject(this);

    }

    public void loginClicked(View v) {
        Log.d(LOG_TAG, "loginClicked");
        String username = editTextToString(usernameET);
        String password = editTextToString(passwordET);
        if (!username.isEmpty())
            openListGiftActivity();
    }
}