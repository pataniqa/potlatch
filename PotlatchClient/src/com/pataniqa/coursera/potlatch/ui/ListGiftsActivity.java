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

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;
import com.pataniqa.coursera.potlatch.store.IPotlatchStore;
import com.pataniqa.coursera.potlatch.store.local.PotlatchResolver;

public class ListGiftsActivity extends GiftActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = ListGiftsActivity.class.getCanonicalName();

    // A resolver that helps us store/retrieve data from the database
    private IPotlatchStore resolver;

    // Used as a native container for the stories we retrieve from the database
    private ArrayList<GiftData> giftData;

    // An adapter that lets the ListView correctly display the data in our
    // ArrayList.
    private GiftDataArrayAdaptor arrayAdapter;

    private String giftQuery = "";

    private SwipeRefreshLayout swipeLayout;

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

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Instantiate the resolver and the ArrayList
        resolver = new PotlatchResolver(this);
        giftData = new ArrayList<GiftData>();

        // Instantiate the adapter using our local GiftData ArrayList.
        arrayAdapter = new GiftDataArrayAdaptor(this, R.layout.gift_listview_custom_row, giftData);

        // Update our GiftData ArrayList with data from the database
        updateGifts();

        // Tell the ListView which adapter to use to display the data.
        ListView listView = (ListView) findViewById(android.R.id.list);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                setGiftQuery(query);
                updateGifts();
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                setGiftQuery(query);
                updateGifts();
                return true;
            }

        });

        return true;
    }

    private void setGiftQuery(String query) {
        this.giftQuery = query;
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

    public void updateGifts() {
        Log.d(LOG_TAG, "updateGiftData");
        try {
            // Clear our local cache of GiftData
            giftData.clear();

            // Add all of them to our local ArrayList
            giftData.addAll(resolver.getGiftsThatMatchTitle(giftQuery));

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