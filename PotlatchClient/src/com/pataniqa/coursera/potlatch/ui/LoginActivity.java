package com.pataniqa.coursera.potlatch.ui;

import java.util.Arrays;

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
import com.pataniqa.coursera.potlatch.store.CRUD;
import com.pataniqa.coursera.potlatch.store.Gifts;

/**
 * The activity that allows the user to provide login information.
 */
public class LoginActivity extends GiftActivity {

    private static final String LOG_TAG = LoginActivity.class.getCanonicalName();

    @InjectView(R.id.login_username) EditText usernameET;
    @InjectView(R.id.login_password) EditText passwordET;
    @InjectView(R.id.server_and_port) EditText endpointET;

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
        String endpoint = editTextToString(endpointET);
        if (username != null && password != null && endpoint != null 
                && !username.isEmpty() && !password.isEmpty() && !endpoint.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(USER_NAME_TAG, username);
            ed.putString(PASSWORD_TAG, password);
            ed.putString(ENDPOINT_TAG,  endpoint);
            ed.commit();
            savePreferences();
        }
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
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
        final CRUD<User> users = getDataService().users(); 
        users.save(new User(username))
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())//.observeOn(AndroidSchedulers.mainThread())
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
                        startActivity(new Intent(context, ListGiftsActivity.class));
                    }
                });
    }
}