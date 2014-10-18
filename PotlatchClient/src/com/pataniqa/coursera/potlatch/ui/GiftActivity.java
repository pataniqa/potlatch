package com.pataniqa.coursera.potlatch.ui;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.HasId;
import com.pataniqa.coursera.potlatch.store.DataService;
import com.pataniqa.coursera.potlatch.store.local.LocalService;
import com.pataniqa.coursera.potlatch.store.remote.RemoteService;
import com.pataniqa.coursera.potlatch.store.remote.unsafe.UnsafeHttpClient;
import com.squareup.picasso.Picasso;

/**
 * Base class for all GiftData UI activities.
 * 
 * This class provides convenience functions for switching between the various
 * activities in the application.
 */
@SuppressLint("Registered")
abstract class GiftActivity extends Activity {

    public final static String ROW_IDENTIFIER_TAG = "row_index";
    public final static String VIEW_MODE_TAG = "view_mode";
    public final static String USER_ID_TAG = "user_id";
    public final static String USER_NAME_TAG = "user_name";
    public final static String PASSWORD_TAG = "password";
    public final static String IMAGE_URL_TAG = "image_url";
    public final static String VIDEO_URL_TAG = "video_url";
    public final static String CLIENT_ID = "mobile";
    public final static String CLIENT_SECRET = "";
    public final static String GIFT_ID_TAG = "id";

    private static final String LOG_TAG = GiftActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        startActivity(new Intent(this, ListGiftsActivity.class));
    }

    // Utility methods

    static String editTextToString(EditText et) {
        return String.valueOf(et.getText().toString());
    }

    long getUserID() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getLong(USER_ID_TAG, HasId.UNDEFINED_ID);
    }

    String getUserName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(USER_NAME_TAG, "Unknown");
    }

    String getPassword() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(PASSWORD_TAG, "Unknown");
    }

    boolean useLocalStore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(SettingsActivity.USE_LOCAL_STORE, true);
    }

    DataService getDataService() {
        return useLocalStore() ? localDataService() : remoteDataService();
    }

    DataService localDataService() {
        return new LocalService(this);
    }

    DataService remoteDataService() {
        return new RemoteService(new UnsafeHttpClient(),
                getEndpoint(),
                getUserName(),
                getPassword(),
                GiftActivity.CLIENT_ID);
    }

    AuthenticationTokenFactory getTokenFactory() {
        return new AuthenticationTokenFactoryImpl(getUserName(),
                getPassword(),
                getEndpoint(),
                GiftActivity.CLIENT_ID,
                GiftActivity.CLIENT_SECRET);
    }

    String getEndpoint() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String host = prefs.getString(SettingsActivity.SERVER_ADDRESS, "192.168.1.71");
        String port = prefs.getString(SettingsActivity.SERVER_PORT, "8443");
        return "https://" + host + ":" + port;
    }

    static int getMaxSize(WindowManager windowManager) {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return Math.min(size.x, size.y);
    }
    
    static void getImage(final boolean useLocalStore, final AuthenticationTokenFactory tokenFactory, final String url, final int maxsize, final Context context, final ImageView image) {
        if (useLocalStore) {
            Uri uri = url.startsWith("file") ? Uri.parse(url) : Uri.fromFile(new File(url));
            Picasso.with(context).load(uri).resize(maxsize, maxsize)
                    .placeholder(R.drawable.ic_fa_image).centerInside().into(image);
        } else {
            tokenFactory.getToken().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).forEach(new Action1<String>() {
                        @Override
                        public void call(String accessToken) {
                            OAuthPicasso
                                    .load(context,
                                            tokenFactory.getEndpoint(),
                                            accessToken,
                                            url).resize(maxsize, maxsize)
                                    .placeholder(R.drawable.ic_fa_image).centerInside()
                                    .into(image);

                        }
                    });
        }
    }
    
    void getImage(final String url, final ImageView image) {
        final int maxsize = GiftActivity.getMaxSize(getWindowManager());
        getImage(useLocalStore(), getTokenFactory(), url, maxsize, this, image);
    }
}
