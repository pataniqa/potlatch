package com.pataniqa.coursera.potlatch.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.GiftQuery.QueryType;
import com.pataniqa.coursera.potlatch.store.GiftQuery.ResultOrder;
import com.pataniqa.coursera.potlatch.store.GiftQuery.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Service;
import com.pataniqa.coursera.potlatch.store.local.LocalService;

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
    public final static String VIEW_MODE_TAG = "view_mode";
    public final static String RESULT_ORDER_TAG = "result_order_tag";
    public final static String RESULT_ORDER_DIRECTION_TAG = "result_order_direction";
    public final static String QUERY_TYPE_TAG = "query_type";
    public final static String DEFAULT_TITLE_QUERY = "";
    public final static String GIFT_CHAIN_ID_TAG = "gift_chain_id";
    public final static String USER_ID_TAG = "user_id";

    private static final String LOG_TAG = GiftActivity.class.getCanonicalName();

    Service service;
    Map<String, Long> giftChains = new HashMap<String, Long>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        service = new LocalService(this);
        updateGiftChains();
    }
    
    void updateGiftChains() {
        try {
            List<GiftChain> results = service.giftChains().query();
            for (GiftChain result : results)
                giftChains.put(result.giftChainName, result.giftChainID);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error connecting to Content Provider" + e.getMessage(), e);
        }
    }

    void openLoginActivity() {
        Log.d(LOG_TAG, "openLoginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    void openPreferenceActivity() {
        Log.d(LOG_TAG, "openPreferencesActivity");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    void openEditGiftActivity(final long index) {
        Log.d(LOG_TAG, "openEditGiftActivity(" + index + ")");
        Intent intent = new Intent();
        intent.setClass(this, EditGiftActivity.class);
        intent.putExtra(ROW_IDENTIFIER_TAG, index);
        startActivity(intent);
    }

    void openCreateGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, CreateGiftActivity.class);
        startActivity(intent);
    }

    void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        startActivity(intent);
    }

    void openListGiftActivity(String titleQuery,
            final ResultOrder resultOrder,
            final ResultOrderDirection resultOrderDirection,
            final QueryType queryType,
            final long giftChainID) {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        intent.putExtra(TITLE_QUERY_TAG, titleQuery);
        intent.putExtra(RESULT_ORDER_TAG, resultOrder);
        intent.putExtra(RESULT_ORDER_DIRECTION_TAG, resultOrderDirection);
        intent.putExtra(QUERY_TYPE_TAG, queryType);
        intent.putExtra(GIFT_CHAIN_ID_TAG, giftChainID);
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
