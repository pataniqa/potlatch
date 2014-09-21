package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.store.IPotlatchStore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * Base class for all GiftData UI activities.
 * 
 * This class provides convenience functions for switching between the various
 * activities in the application.
 * 
 */
@SuppressLint("Registered")
abstract class GiftActivity extends Activity {
    
    public final static String ROW_IDENTIFIER_TAG = "row_index";
    public final static String TITLE_QUERY_TAG = "title_query";
    public final static String DEFAULT_TITLE_QUERY = "";
    
    protected IPotlatchStore resolver;

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
        intent.putExtra(ROW_IDENTIFIER_TAG, index);
        startActivity(intent);
    }

    public void openCreateGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, CreateGiftActivity.class);
        startActivity(intent);
    }

    public void openListGiftActivity(String titleQuery) {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        intent.putExtra(TITLE_QUERY_TAG, titleQuery);
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
        Log.d(LOG_TAG, "onOptionsItemSelected");
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
        Log.d(LOG_TAG, "createActionBar");
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                openListGiftActivity(query);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                openListGiftActivity(query);
                return true;
            }

        });

        return true;
    }
}
