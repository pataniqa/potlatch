package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;
import com.pataniqa.coursera.potlatch.storage.GiftData;
import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;

/**
 * This activity lists all the stories currently stored in the database
 */
public class ListStoryActivity extends StoryActivityBase implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = ListStoryActivity.class.getCanonicalName();

    // A resolver that helps us store/retrieve data from the database
    private PotlatchResolver resolver;

    // Used as a native container for the stories we retrieve from the database
    private ArrayList<GiftData> giftData;

    // An adapter that lets the ListView correctly display the data in our
    // ArrayList.
    private GiftDataArrayAdaptor arrayAdapter;

    // The EditText used to filter the stories listed based on the tags they
    // have
    private EditText filterGifts;
    
    private SwipeRefreshLayout swipeLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        // Set up the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.list_story_activity);
        
        getActionBar().show();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);

        // Instantiate the resolver and the ArrayList
        resolver = new PotlatchResolver(this);
        giftData = new ArrayList<GiftData>();

        // Get the ListView that will be displayed
        ListView lv = (ListView) findViewById(android.R.id.list);

        filterGifts = (EditText) findViewById(R.id.story_listview_tags_filter);

        // customize the ListView in whatever desired ways.
        lv.setBackgroundColor(Color.GRAY);

        // Instantiate the adapter using our local StoryData ArrayList.
        arrayAdapter = new GiftDataArrayAdaptor(this, R.layout.story_listview_custom_row, giftData);

        // Update our StoryData ArrayList with data from the database
        updateStoryData();

        // Tell the ListView which adapter to use to display the data.
        lv.setAdapter(arrayAdapter);

        // Set the click listener for the list view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d(LOG_TAG, "onListItemClick");
                Log.d(LOG_TAG, "position: " + position + "id = " + (giftData.get(position)).KEY_ID);

                // When an item is clicked, open the ViewStoryActivity so the
                // user can view it in full screen
                openViewStoryActivity((giftData.get(position)).KEY_ID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_story_activity_actions, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      // action with ID action_refresh was selected
      case R.id.action_new:
          openCreateStoryActivity();
        break;
      default:
        break;
      }

      return true;
    } 

    // Update mStoryData with the data currently in the database
    public void updateStoryData() {
        Log.d(LOG_TAG, "updateStoryData");
        try {
            // Clear our local cache of StoryData
            giftData.clear();

            // create String that will match with 'like' in query
            String filterWord = String.format("\\%%s\\%", filterGifts.getText().toString());

            // Get all the StoryData in the database
            ArrayList<GiftData> currentList2 = resolver.queryStoryData(null, PotlatchSchema.Story.Cols.TAGS
                    + " LIKE ? ", new String[] { filterWord }, null);

            // Add all of them to our local ArrayList
            giftData.addAll(currentList2);

            // Let the ArrayAdaptor know that we changed the data in its array.
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error connecting to Content Provider" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
                updateStoryData();
            }
        }, 5000);
        
    }
}