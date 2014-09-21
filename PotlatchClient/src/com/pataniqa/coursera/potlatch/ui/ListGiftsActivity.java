package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
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

        //swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        ButterKnife.inject(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
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

    private String getTitleQuery() {
        return getIntent().getStringExtra(TITLE_QUERY_TAG);
    }
}