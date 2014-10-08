package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;

public class ListGiftsActivity extends GiftActivity implements
        SwipeRefreshLayout.OnRefreshListener, ListGiftsCallback {

    private static final String LOG_TAG = ListGiftsActivity.class.getCanonicalName();

    private ArrayList<GiftResult> giftData;
    private GiftDataArrayAdapter arrayAdapter;

    @InjectView(R.id.list_gifts_swipe_container) SwipeRefreshLayout swipeLayout;

    @InjectView(R.id.list_gifts_list_view) ListView listView;

    private String titleQuery = getTitleQuery();
    private QueryType queryType = getQueryType();
    private ResultOrder resultOrder = getResultOrder();
    private ResultOrderDirection resultDirection = getResultOrderDirection();
    private long giftChainID = getGiftChainID();
    private long userID = 0; // FIXME

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        // Set up the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.list_gifts_activity);
        getActionBar().show();

        ButterKnife.inject(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Instantiate the resolver and the ArrayList
        giftData = new ArrayList<GiftResult>();

        // Instantiate the adapter using our local GiftData ArrayList.
        arrayAdapter = new GiftDataArrayAdapter(this,
                R.layout.gift_listview_custom_row,
                giftData,
                this);

        loadPreferences();
        updateGifts();

        // Tell the ListView which adapter to use to display the data.
        listView.setAdapter(arrayAdapter);

        // Set the click listener for the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d(LOG_TAG, "onListItemClick");
                Log.d(LOG_TAG, "position: " + position + "id = " + (giftData.get(position)).getId());

                // When an item is clicked, open the ViewGiftActivity so the
                // user can view it in full screen
                openEditGiftActivity((giftData.get(position)).getId());
            }
        });
    }

    void loadPreferences() {
        Log.d(LOG_TAG, "loadPreferences");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.contains(TITLE_QUERY_TAG))
            titleQuery = prefs.getString(TITLE_QUERY_TAG, titleQuery);
        if (prefs.contains(RESULT_ORDER_TAG))
            resultOrder = ResultOrder.values()[prefs
                    .getInt(RESULT_ORDER_TAG, resultOrder.ordinal())];
        if (prefs.contains(RESULT_ORDER_DIRECTION_TAG))
            resultDirection = ResultOrderDirection.values()[prefs
                    .getInt(RESULT_ORDER_DIRECTION_TAG, resultDirection.ordinal())];
        if (prefs.contains(QUERY_TYPE_TAG))
            queryType = QueryType.values()[prefs.getInt(QUERY_TYPE_TAG, queryType.ordinal())];
    }

    void savePreferences() {
        Log.d(LOG_TAG, "savePreferences");
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(TITLE_QUERY_TAG, titleQuery);
        ed.putInt(RESULT_ORDER_TAG, resultOrder.ordinal());
        ed.putInt(RESULT_ORDER_DIRECTION_TAG, resultDirection.ordinal());
        ed.putInt(QUERY_TYPE_TAG, queryType.ordinal());
        ed.commit();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        updateGifts();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        savePreferences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
        // action with ID action_refresh was selected
        case R.id.action_new:
            openCreateGiftActivity();
            break;
        case R.id.action_query_type:
            queryType = QueryType.values()[(queryType.ordinal() + 1) % 3];
            updateQueryType(item);
            updateGifts();
            break;
        case R.id.action_result_order:
            resultOrder = resultOrder == ResultOrder.LIKES ? ResultOrder.TIME : ResultOrder.LIKES;
            updateResultOrder(item);
            updateGifts();
            break;
        case R.id.action_result_order_direction:
            resultDirection = resultDirection == ResultOrderDirection.DESCENDING ? ResultOrderDirection.ASCENDING
                    : ResultOrderDirection.DESCENDING;
            updateResultOrderDirection(item);
            updateGifts();
            break;
        case R.id.action_settings:
            openPreferenceActivity();
            break;
        default:
            break;
        }

        return true;
    }

    void updateQueryType(MenuItem item) {
        // TODO because there are four options
        // this would be better as a spinner
        // http://developer.android.com/guide/topics/ui/actionbar.html#Dropdown

        switch (queryType) {
        case USER:
            item.setIcon(R.drawable.ic_action_person);
            break;
        case TOP_GIFT_GIVERS:
            item.setIcon(R.drawable.ic_fa_trophy);
            break;
        case CHAIN:
            item.setIcon(R.drawable.ic_fa_link);
            break;
        default:
            item.setIcon(R.drawable.ic_fa_group);
            break;
        }
    }

    void updateResultOrder(MenuItem item) {
        item.setIcon(resultOrder == ResultOrder.LIKES ? R.drawable.ic_fa_heart
                : R.drawable.ic_fa_clock_o);
    }

    void updateResultOrderDirection(MenuItem item) {
        item.setIcon(resultDirection == ResultOrderDirection.DESCENDING ? R.drawable.ic_fa_sort_amount_desc
                : R.drawable.ic_fa_sort_amount_asc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        updateQueryType(menu.findItem(R.id.action_query_type));
        updateResultOrder(menu.findItem(R.id.action_result_order));
        updateResultOrderDirection(menu.findItem(R.id.action_result_order_direction));

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                Log.d(LOG_TAG, "onQueryTextChange: " + query);
                updateGifts(query);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(LOG_TAG, "onQueryTextSubmit: " + query);
                updateGifts(query);
                search.clearFocus();
                return false;
            }

        });

        return true;
    }

    void updateGifts(String query) {
        titleQuery = query;
        updateGifts();
    }

    void updateGifts() {
        Log.d(LOG_TAG, "updateGifts");

        // query the server

        Gifts gifts = service.gifts();
        boolean hide = prefs.getBoolean(SettingsActivity.HIDE_FLAGGED_CONTENT, true);
        Observable<ArrayList<GiftResult>> results = null;
        switch (queryType) {
        case USER:
            results = gifts.queryByUser(titleQuery, userID, resultOrder, resultDirection, hide);
            break;
        case TOP_GIFT_GIVERS:
            results = gifts.queryByTopGiftGivers(titleQuery, resultDirection, hide);
            break;
        case CHAIN:
            results = gifts.queryByGiftChain(titleQuery,
                    giftChainID,
                    resultOrder,
                    resultDirection,
                    hide);
            break;
        default:
            results = gifts.queryByTitle(titleQuery, resultOrder, resultDirection, hide);
            break;
        }

        // update the display

        results.forEach(new Action1<ArrayList<GiftResult>>() {
            @Override
            public void call(ArrayList<GiftResult> results) {
                giftData.clear();
                if (results != null) {
                    giftData.addAll(results);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showGiftChain(long giftChainID) {
        queryType = QueryType.CHAIN;
        this.giftChainID = giftChainID;
        updateGifts();
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "onRefresh");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                updateGifts();
            }
        });
    }

    @Override
    public void setLike(GiftResult gift) {
        service.giftMetadata().setLike(gift.getId(), gift.isLike());
    }

    @Override
    public void setFlag(GiftResult gift) {
        service.giftMetadata().setFlag(gift.getId(), gift.isFlag());
    }

    private String getTitleQuery() {
        String title = getIntent() != null ? getIntent().getStringExtra(TITLE_QUERY_TAG) : null;
        return title != null ? title : DEFAULT_QUERY;
    }

    private ResultOrder getResultOrder() {
        ResultOrder resultOrder = getIntent() != null ? (ResultOrder) getIntent()
                .getSerializableExtra(RESULT_ORDER_TAG) : null;
        return resultOrder != null ? resultOrder : ResultOrder.TIME;
    }

    private ResultOrderDirection getResultOrderDirection() {
        ResultOrderDirection resultOrderDirection = getIntent() != null ? (ResultOrderDirection) getIntent()
                .getSerializableExtra(RESULT_ORDER_DIRECTION_TAG) : null;
        return resultOrderDirection != null ? resultOrderDirection
                : ResultOrderDirection.DESCENDING;
    }

    private QueryType getQueryType() {
        QueryType queryType = getIntent() != null ? (QueryType) getIntent()
                .getSerializableExtra(QUERY_TYPE_TAG) : null;
        return queryType != null ? queryType : QueryType.ALL;
    }

    private long getGiftChainID() {
        return getIntent() != null ? getIntent().getLongExtra(GIFT_CHAIN_ID_TAG, 0) : 0;
    }
}