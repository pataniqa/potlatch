package com.pataniqa.coursera.potlatch.ui;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.content.Intent;
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
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder;

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
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            // reset the authorization tokens to force a new log in
            Log.d(LOG_TAG, "Logging the previous user out");
            OAuthPicasso.reset();
            SecuredRestBuilder.reset();
            savePreferences();
            startActivity(new Intent(this, ListGiftsActivity.class));
        }
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
        String username = getUserName();
        if (username != null && !username.isEmpty() && usernameET != null)
            usernameET.setText(username);
    }

    private void savePreferences() {
        final String username = editTextToString(usernameET);
        final Context context = this;
        getDataService().users().save(new User(username))
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putLong(USER_ID_TAG, user.getId());
                        ed.putString(USER_NAME_TAG, username);
                        ed.putString(PASSWORD_TAG, editTextToString(passwordET));
                        Log.d(LOG_TAG, "savePreferences: username " + username + " " + user.getId());

                        // the user logged in so reset the query parameters

                        ed.putString(GiftQuery.TITLE_QUERY_TAG, "");
                        ed.putInt(GiftQuery.RESULT_ORDER_TAG, Gifts.ResultOrder.LIKES.ordinal());
                        ed.putInt(GiftQuery.RESULT_ORDER_DIRECTION_TAG,
                                Gifts.ResultOrderDirection.DESCENDING.ordinal());
                        ed.putInt(GiftQuery.QUERY_TYPE_TAG, GiftQuery.QueryType.ALL.ordinal());
                        ed.putString(GiftQuery.GIFT_CHAIN_NAME_TAG, "");
                        ed.putLong(GiftQuery.GIFT_CHAIN_ID_TAG, 0);
                        ed.putString(GiftQuery.QUERY_USER_NAME_TAG, user.getName());
                        ed.putLong(GiftQuery.QUERY_USER_ID_TAG, user.getId());
                        ed.commit();
                        Log.d(LOG_TAG,
                                Arrays.toString(getDataService().users().findAll().toBlocking()
                                        .first().toArray()));
                    }
                });
    }
}