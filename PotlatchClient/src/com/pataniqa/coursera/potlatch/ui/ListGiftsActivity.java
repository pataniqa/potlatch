package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class ListGiftsActivity extends GiftActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = ListGiftsActivity.class.getCanonicalName();

    private ArrayList<GiftData> giftData;
    private GiftDataArrayAdapter arrayAdapter;

    @InjectView(R.id.list_gifts_swipe_container)
    SwipeRefreshLayout swipeLayout;
    @InjectView(R.id.list_gifts_list_view)
    ListView listView;

    private QueryType queryType = getQueryType();
    private ResultOrder resultOrder = getResultOrder();
    private ResultOrderDirection resultOrderDirection = getResultOrderDirection();
    private ViewMode viewMode = getViewMode();

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

        // swipeLayout = (SwipeRefreshLayout)
        // findViewById(R.id.swipe_container);

        ButterKnife.inject(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Instantiate the resolver and the ArrayList
        resolver = new PotlatchResolver(this);
        giftData = new ArrayList<GiftData>();

        // Instantiate the adapter using our local GiftData ArrayList.
        arrayAdapter = new GiftDataArrayAdapter(this, R.layout.gift_listview_custom_row, giftData);

        updateGifts();

        // Tell the ListView which adapter to use to display the data.
        listView.setAdapter(arrayAdapter);

        // Set the click listener for the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d(LOG_TAG, "onListItemClick");
                Log.d(LOG_TAG, "position: " + position + "id = " + (giftData.get(position)).KEY_ID);

                // When an item is clicked, open the ViewGiftActivity so the
                // user can view it in full screen
                openEditGiftActivity((giftData.get(position)).KEY_ID);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGifts();
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
            if (queryType == QueryType.ALL) {
                queryType = QueryType.USER;
                item.setIcon(R.drawable.ic_action_person);
            } else if (queryType == QueryType.USER) {
                queryType = QueryType.TOP_GIFT_GIVERS;
                item.setIcon(R.drawable.ic_fa_trophy);
            } else {
                queryType = QueryType.ALL;
                item.setIcon(R.drawable.ic_fa_group);
            }
            updateGifts();
            break;
        case R.id.action_result_order:
            if (resultOrder == ResultOrder.LIKES) {
                resultOrder = ResultOrder.TIME;
                item.setIcon(R.drawable.ic_fa_clock_o);
            } else {
                resultOrder = ResultOrder.LIKES;
                item.setIcon(R.drawable.ic_fa_heart);
            }
            updateGifts();
            break;
        case R.id.action_result_order_direction:
            if (resultOrderDirection == ResultOrderDirection.DESCENDING) {
                resultOrderDirection = ResultOrderDirection.ASCENDING;
                item.setIcon(R.drawable.ic_fa_sort_amount_asc);
            } else {
                resultOrderDirection = ResultOrderDirection.DESCENDING;
                item.setIcon(R.drawable.ic_fa_sort_amount_desc);
            }
            updateGifts();
            break;
            
            // Leave ViewMode until later 
            
//        case R.id.action_view_mode:
//            if (viewMode == ViewMode.GRID_VIEW) {
//                viewMode = ViewMode.LIST_VIEW;
//                item.setIcon(R.drawable.ic_fa_list);
//            } else {
//                viewMode = ViewMode.GRID_VIEW;
//                item.setIcon(R.drawable.ic_action_select_all);
//            }
//            updateGifts();
//            break;
            
        case R.id.action_settings:
            openPreferenceActivity();
            break;
        default:
            break;
        }

        return true;
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

    public void updateGifts() {
        Log.d(LOG_TAG, "updateGiftData");
        try {
            // Clear our local cache of GiftData
            giftData.clear();

            // Add all of them to our local ArrayList
            giftData.addAll(resolver.getGiftsThatMatchTitle(getTitleQuery()));

            // Let the ArrayAdaptor know that we changed the data in its array.
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error connecting to Content Provider" + e.getMessage(), e);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                updateGifts();
            }
        });
    }
}