package com.pataniqa.coursera.potlatch.ui;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.User;

/**
 * The activity that allows the user to provide login information.
 */
public class LoginActivity extends GiftActivity {

    private static final String LOG_TAG = LoginActivity.class.getCanonicalName();

    @InjectView(R.id.login_username) EditText usernameET;
    @InjectView(R.id.login_password) EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Setup the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.login_activity);

        ButterKnife.inject(this);
        loadPreferences();
    }

    public void loginClicked(View v) {
        Log.d(LOG_TAG, "loginClicked");
        String username = editTextToString(usernameET);
        String password = editTextToString(passwordET);
        savePreferences();
        if (!username.isEmpty() && !password.isEmpty())
            openListGiftActivity();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        savePreferences();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        savePreferences();
        super.onStop();
    }

    private void loadPreferences() {
        Log.d(LOG_TAG, "loadPreferences");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString(USER_NAME_TAG, "");
        if (username != null && !username.isEmpty())
            usernameET.setText(username);
    }

    private void savePreferences() {
        String username = editTextToString(usernameET);
        long userID = getUserID();
        Log.d(LOG_TAG, "savePreferences: username " + username + " " + userID);
        final Context context = this;
        if (username != null && !username.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(USER_NAME_TAG, username);
            ed.putString(PASSWORD_TAG, editTextToString(passwordET));
            ed.commit();
        }
        if (userID == GetId.UNDEFINED_ID) {
            service.users().save(new User(userID, username)).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).forEach(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor ed = prefs.edit();
                            ed.putLong(USER_ID_TAG, user.getId());
                            ed.commit();
                            Log.d(LOG_TAG,
                                    Arrays.toString(service.users().findAll().toBlocking().first()
                                            .toArray()));
                        }
                    });
        }
    }
}