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
    public final static String VIEW_MODE_TAG = "view_mode";
    public final static String RESULT_ORDER_TAG = "result_order_tag";
    public final static String RESULT_ORDER_DIRECTION_TAG = "result_order_direction";
    public final static String QUERY_TYPE_TAG = "query_type";
    public final static String DEFAULT_TITLE_QUERY = "";

    enum ViewMode {
        LIST_VIEW, GRID_VIEW
    };

    enum ResultOrder {
        TIME, LIKES
    };

    enum ResultOrderDirection {
        ASCENDING, DESCENDING
    };

    enum QueryType {
        USER, TOP_GIFT_GIVERS, ALL
    };

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

    public void openListGiftActivity(String titleQuery,
            final ViewMode viewMode,
            final ResultOrder resultOrder,
            final ResultOrderDirection resultOrderDirection,
            final QueryType queryType) {
        Log.d(LOG_TAG, "openCreateGiftActivity");
        Intent intent = new Intent();
        intent.setClass(this, ListGiftsActivity.class);
        intent.putExtra(TITLE_QUERY_TAG, titleQuery);
        intent.putExtra(VIEW_MODE_TAG, viewMode);
        intent.putExtra(RESULT_ORDER_TAG, resultOrder);
        intent.putExtra(RESULT_ORDER_DIRECTION_TAG, resultOrderDirection);
        intent.putExtra(QUERY_TYPE_TAG, queryType);
        startActivity(intent);
    }

    protected long getRowIdentifier() {
        return getIntent().getLongExtra(ROW_IDENTIFIER_TAG, 0);
    }

    protected String getTitleQuery() {
        String title = getIntent().getStringExtra(TITLE_QUERY_TAG);
        return title != null ? title : "";
    }

    protected ViewMode getViewMode() {
        ViewMode viewMode = (ViewMode) getIntent().getSerializableExtra(VIEW_MODE_TAG);
        return viewMode != null ? viewMode : ViewMode.LIST_VIEW;
    }

    protected ResultOrder getResultOrder() {
        ResultOrder resultOrder = (ResultOrder) getIntent().getSerializableExtra(RESULT_ORDER_TAG);
        return resultOrder != null ? resultOrder : ResultOrder.TIME;
    }

    protected ResultOrderDirection getResultOrderDirection() {
        ResultOrderDirection resultOrderDirection = (ResultOrderDirection) getIntent()
                .getSerializableExtra(RESULT_ORDER_DIRECTION_TAG);
        return resultOrderDirection != null ? resultOrderDirection
                : ResultOrderDirection.DESCENDING;
    }

    protected QueryType getQueryType() {
        QueryType queryType = (QueryType) getIntent().getSerializableExtra(QUERY_TYPE_TAG);
        return queryType != null ? queryType : QueryType.ALL;
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
            openListGiftActivity(getTitleQuery(),
                    getViewMode(),
                    getResultOrder(),
                    getResultOrderDirection(),
                    QueryType.USER);
            // TODO - need to give context
            break;
        case R.id.action_top_gift_givers:
            openListGiftActivity(getTitleQuery(),
                    getViewMode(),
                    getResultOrder(),
                    getResultOrderDirection(),
                    QueryType.TOP_GIFT_GIVERS);
            // TODO - need to give context
            // TODO - this is broken because there is no way to go back to QueryType.ALL
            // TODO - no way to select ASCENDING or DESCENDING
            break;
        case R.id.action_settings:
            // TODO
            break;
        case R.id.action_grid:
            ViewMode viewMode = getViewMode() == ViewMode.GRID_VIEW ? ViewMode.LIST_VIEW : ViewMode.GRID_VIEW;
            openListGiftActivity(getTitleQuery(),
                    viewMode,
                    getResultOrder(),
                    getResultOrderDirection(),
                    getQueryType());
            // TODO - need to give context
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
                openListGiftActivity(query,
                        getViewMode(),
                        getResultOrder(),
                        getResultOrderDirection(),
                        getQueryType());
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                openListGiftActivity(query,
                        getViewMode(),
                        getResultOrder(),
                        getResultOrderDirection(),
                        getQueryType());
                return true;
            }

        });

        return true;
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
