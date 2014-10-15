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
import android.widget.TextView;
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

    @InjectView(R.id.query_description) TextView queryDescription;

    private GiftQuery query;
    private Menu menu;
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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        query = new GiftQuery(getUserID(), getUserName(), prefs);
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

    void savePreferences() {
        Log.d(LOG_TAG, "savePreferences");
        query.saveToSharedPreferences(prefs);
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
            query.rotateQueryType();
            updateGifts();
            break;
        case R.id.action_result_order:
            query.changeResultOrder();
            updateGifts();
            break;
        case R.id.action_result_order_direction:
            query.changeResultDirection();
            updateGifts();
            break;
        case R.id.action_settings:
            openPreferenceActivity();
            break;
        default:
            break;
        }
        updateMenu();

        return true;
    }

    void updateMenu() {
        MenuItem queryTypeMenu = menu.findItem(R.id.action_query_type);
        switch (query.getQueryType()) {
        case USER:
            queryTypeMenu.setIcon(R.drawable.ic_action_person);
            break;
        case TOP_GIFT_GIVERS:
            queryTypeMenu.setIcon(R.drawable.ic_fa_trophy);
            break;
        case CHAIN:
            queryTypeMenu.setIcon(R.drawable.ic_fa_link);
            break;
        default:
            queryTypeMenu.setIcon(R.drawable.ic_fa_group);
            break;
        }

        menu.findItem(R.id.action_result_order)
                .setIcon(query.getResultOrder() == ResultOrder.LIKES ? R.drawable.ic_fa_heart
                        : R.drawable.ic_fa_clock_o);

        menu.findItem(R.id.action_result_order_direction)
                .setIcon(query.getResultDirection() == ResultOrderDirection.DESCENDING ? R.drawable.ic_fa_sort_amount_desc
                        : R.drawable.ic_fa_sort_amount_asc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        updateMenu();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String title) {
                Log.d(LOG_TAG, "onQueryTextChange: " + title);
                query.setTitle(title);
                updateGifts();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String title) {
                Log.d(LOG_TAG, "onQueryTextSubmit: " + title);
                query.setTitle(title);
                updateGifts();
                search.clearFocus();
                return false;
            }

        });

        return true;
    }

    void processResults(Observable<ArrayList<GiftResult>> results) {
        results.forEach(new Action1<ArrayList<GiftResult>>() {
            @Override
            public void call(ArrayList<GiftResult> results) {
                giftData.clear();
                if (results != null)
                    giftData.addAll(results);
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    void updateGifts() {
        Log.d(LOG_TAG, "updateGifts");
        Gifts gifts = service.gifts();
        boolean hide = prefs.getBoolean(SettingsActivity.HIDE_FLAGGED_CONTENT, true);
        processResults(query.query(gifts, hide));
        queryDescription.setText(query.getDescription());
    }

    @Override
    public void createGiftChainQuery(long giftChainID, String giftChainName) {
        query.setChainQuery(giftChainID, giftChainName);
        updateGifts();
        updateMenu();
    }

    @Override
    public void createUserQuery(long queryUserID, String queryUserName) {
        query.setUserQuery(queryUserID, queryUserName);
        updateGifts();
        updateMenu();
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
}