package com.pataniqa.coursera.potlatch.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;

import com.pataniqa.coursera.potlatch.store.GiftStore;

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

    enum ResultOrder {
        TIME, LIKES
    };

    enum ResultOrderDirection {
        ASCENDING, DESCENDING
    };

    enum QueryType {
        USER, TOP_GIFT_GIVERS, ALL
    };

    protected GiftStore resolver;

    private static final String LOG_TAG = GiftActivity.class.getCanonicalName();

    public void openLoginActivity() {
        Log.d(LOG_TAG, "openLoginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    
    public void openPreferenceActivity() {
        Log.d(LOG_TAG, "openPreferencesActivity");
        Intent intent = new Intent(this,SettingsActivity.class);
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

    public void openListGiftActivity() {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        startActivity(intent);
    }

    public void openListGiftActivity(String titleQuery,
            final ResultOrder resultOrder,
            final ResultOrderDirection resultOrderDirection,
            final QueryType queryType) {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        intent.putExtra(TITLE_QUERY_TAG, titleQuery);
        intent.putExtra(RESULT_ORDER_TAG, resultOrder);
        intent.putExtra(RESULT_ORDER_DIRECTION_TAG, resultOrderDirection);
        intent.putExtra(QUERY_TYPE_TAG, queryType);
        startActivity(intent);
    }

    protected long getRowIdentifier() {
        return getIntent().getLongExtra(ROW_IDENTIFIER_TAG, 0);
    }

    protected String getTitleQuery() {
        String title = getIntent() != null ? getIntent().getStringExtra(TITLE_QUERY_TAG) : null;
        return title != null ? title : "";
    }

    protected ResultOrder getResultOrder() {
        ResultOrder resultOrder = getIntent() != null ? (ResultOrder) getIntent()
                .getSerializableExtra(RESULT_ORDER_TAG) : null;
        return resultOrder != null ? resultOrder : ResultOrder.TIME;
    }

    protected ResultOrderDirection getResultOrderDirection() {
        ResultOrderDirection resultOrderDirection = getIntent() != null ? (ResultOrderDirection) getIntent()
                .getSerializableExtra(RESULT_ORDER_DIRECTION_TAG) : null;
        return resultOrderDirection != null ? resultOrderDirection
                : ResultOrderDirection.DESCENDING;
    }

    protected QueryType getQueryType() {
        QueryType queryType = getIntent() != null ? (QueryType) getIntent()
                .getSerializableExtra(QUERY_TYPE_TAG) : null;
        return queryType != null ? queryType : QueryType.ALL;
    }

    // Utility methods

    public static String editTextToString(EditText et) {
        return String.valueOf(et.getText().toString());
    }

    public static String uriToString(Uri u) {
        return u != null ? u.toString() : "";
    }

    public static Uri stringToUri(String s) {
        return !s.isEmpty() ? Uri.parse(s) : null;
    }
}
