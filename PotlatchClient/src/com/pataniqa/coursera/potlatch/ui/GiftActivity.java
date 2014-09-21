package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

/**
 * Base class for all GiftData UI activities.
 * 
 * This class provides convenience functions for switching between the various
 * activities in the application.
 * 
 */
@SuppressLint("Registered")
abstract class GiftActivity extends Activity {

    private static final String LOG_TAG = GiftActivity.class.getCanonicalName();

    public void openLoginActivity() {
        Log.d(LOG_TAG, "openLoginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openEditGiftActivity(final long index) {
        Log.d(LOG_TAG, "openEditGiftActivity(" + index + ")");
        Intent intent = new Intent();
        intent.setClass(this, EditGiftActivity.class);
        intent.putExtra(EditGiftActivity.rowIdentifyerTAG, index);
        startActivity(intent);
    }

    public void openCreateGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, CreateGiftActivity.class);
        startActivity(intent);
    }

    public void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        startActivity(intent);
    }
    
    public static String editTextToString(EditText et) {
        return String.valueOf(et.getText().toString());
    }
    
    public static String uriToString(Uri u) {
        return u != null ? u.toString() : "";
    }
    
    public static Uri stringToUri(String s) {
        return !s.isEmpty() ? Uri.parse(s) : null;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // action with ID action_refresh was selected
        case R.id.action_new:
            openCreateGiftActivity();
            break;
        case R.id.action_me:
            // TODO
            break;
        case R.id.action_top_gift_givers:
            // TODO
            break;
        case R.id.action_settings:
            // TODO
            break;
        case R.id.action_grid:
            // TODO
            break;
        default:
            break;
        }

        return true;
    }
    
    protected void createActionBar() {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }
}
