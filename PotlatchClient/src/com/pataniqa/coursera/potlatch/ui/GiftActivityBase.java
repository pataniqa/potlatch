package com.pataniqa.coursera.potlatch.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Base class for all GiftData UI activities.
 * 
 * This class provides convenience functions for switching between the various
 * activities in the application.
 * 
 */
@SuppressLint("Registered")
public class GiftActivityBase extends Activity {

    private static final String LOG_TAG = GiftActivityBase.class.getCanonicalName();

    public void openLoginActivity() {
        Intent intent = newLoginActivityIntent(this);
        startActivity(intent);
    }

    public void openEditGiftActivity(final long index) {
        Log.d(LOG_TAG, "openEditGiftActivity(" + index + ")");
        Intent intent = newEditGiftIntent(this, index);
        startActivity(intent);
    }

    public void openCreateGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = newCreateGiftIntent(this);
        startActivity(intent);
    }

    public void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = newListGiftIntent(this);
        startActivity(intent);
    }

    public static Intent newLoginActivityIntent(Activity activity) {
        return new Intent(activity, LoginActivity.class);
    }

    public static Intent newEditGiftIntent(Activity activity, long index) {
        Intent intent = new Intent();
        intent.setClass(activity, EditGiftActivity.class);
        intent.putExtra(EditGiftActivity.rowIdentifyerTAG, index);
        return intent;
    }

    public static Intent newListGiftIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ListGiftsActivity.class);
        return intent;
    }

    public static Intent newCreateGiftIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, CreateGiftActivity.class);
        return intent;
    }
}
