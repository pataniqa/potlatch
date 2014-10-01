package com.pataniqa.coursera.potlatch.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Service;
import com.pataniqa.coursera.potlatch.store.local.LocalService;

/**
 * Base class for all GiftData UI activities.
 * 
 * This class provides convenience functions for switching between the various
 * activities in the application.
 */
@SuppressLint("Registered")
abstract class GiftActivity extends Activity {
    
    enum QueryType {
        USER, TOP_GIFT_GIVERS, ALL, CHAIN
    };

    public final static String ROW_IDENTIFIER_TAG = "row_index";
    public final static String TITLE_QUERY_TAG = "title_query";
    public final static String VIEW_MODE_TAG = "view_mode";
    public final static String RESULT_ORDER_TAG = "result_order_tag";
    public final static String RESULT_ORDER_DIRECTION_TAG = "result_order_direction";
    public final static String QUERY_TYPE_TAG = "query_type";
    public final static String DEFAULT_TITLE_QUERY = "";
    public final static String GIFT_CHAIN_NAME_TAG = "gift_chain_name";
    public final static String USER_ID_TAG = "user_id";
    public final static String USER_NAME_TAG ="user_name";

    private static final String LOG_TAG = GiftActivity.class.getCanonicalName();

    Service service;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        service = new LocalService(this);
    }

    void openLoginActivity() {
        Log.d(LOG_TAG, "openLoginActivity");
        startActivity(new Intent(this, LoginActivity.class));
    }

    void openPreferenceActivity() {
        Log.d(LOG_TAG, "openPreferencesActivity");
        startActivity(new Intent(this, SettingsActivity.class));
    }

    void openEditGiftActivity(final long index) {
        Log.d(LOG_TAG, "openEditGiftActivity(" + index + ")");
        Intent intent = new Intent(this, EditGiftActivity.class);
        intent.putExtra(ROW_IDENTIFIER_TAG, index);
        startActivity(intent);
    }

    void openCreateGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        startActivity(new Intent(this, CreateGiftActivity.class));
    }

    void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        startActivity(new Intent(this, ListGiftsActivity.class));
    }

    void openListGiftActivity(String titleQuery,
            final ResultOrder resultOrder,
            final ResultOrderDirection resultOrderDirection,
            final QueryType queryType,
            final long giftChainID) {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent(this, ListGiftsActivity.class);
        intent.putExtra(TITLE_QUERY_TAG, titleQuery);
        intent.putExtra(RESULT_ORDER_TAG, resultOrder);
        intent.putExtra(RESULT_ORDER_DIRECTION_TAG, resultOrderDirection);
        intent.putExtra(QUERY_TYPE_TAG, queryType);
        intent.putExtra(GIFT_CHAIN_NAME_TAG, giftChainID);
        startActivity(intent);
    }

    long getRowIdentifier() {
        return getIntent().getLongExtra(ROW_IDENTIFIER_TAG, 0);
    }

    // Utility methods

    static String editTextToString(EditText et) {
        return String.valueOf(et.getText().toString());
    }

    static String uriToString(Uri u) {
        return u != null ? u.toString() : "";
    }

    static Uri stringToUri(String s) {
        return !s.isEmpty() ? Uri.parse(s) : null;
    }
}
