package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.provider.PotlatchSchema;
import com.pataniqa.coursera.potlatch.storage.PotlatchResolver;
import com.pataniqa.coursera.potlatch.storage.StoryData;

/**
 * This activity lists all the stories currently stored in the database
 */
public class ListStoryActivity extends StoryActivityBase {
	
	// A tag used for debugging with Logcat
    private static final String LOG_TAG = ListStoryActivity.class
            .getCanonicalName();
    
    // A resolver that helps us store/retrieve data from the database
	PotlatchResolver resolver;
	
	// Used as a native container for the stories we retrieve from the database
	ArrayList<StoryData> mStoryData;
	
	// An adapter that lets the ListView correctly display the data in our ArrayList. 
	private StoryDataArrayAdaptor aa;

	// The EditText used to filter the stories listed based on the tags they have
	EditText filterET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        
        // Set up the UI
        setContentView(R.layout.list_story_activity);	
        
        // Instantiate the resolver and the ArrayList
        resolver = new PotlatchResolver(this);
		mStoryData = new ArrayList<StoryData>();
		
		// Get the ListView that will be displayed
		ListView lv = (ListView) findViewById(android.R.id.list);

		filterET = (EditText) findViewById(R.id.story_listview_tags_filter);

		// customize the ListView in whatever desired ways.
		lv.setBackgroundColor(Color.GRAY);	
		
		// Instantiate the adapter using our local StoryData ArrayList.
		aa = new StoryDataArrayAdaptor(this,
				R.layout.story_listview_custom_row, mStoryData);	

		// Update our StoryData ArrayList with data from the database
		updateStoryData();

		// Tell the ListView which adapter to use to display the data.
		lv.setAdapter(aa);
		
		// Set the click listener for the list view
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		    	Log.d(LOG_TAG, "onListItemClick");
				Log.d(LOG_TAG,
						"position: " + position + "id = "
								+ (mStoryData.get(position)).KEY_ID);
				
				// When an item is clicked, open the ViewStoryActivity so the user can view it in full screen
				openViewStoryActivity((mStoryData.get(position)).KEY_ID);
		    }
		});
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.list_story_activity_menu, menu);
//        
////        MenuItem searchItem = menu.findItem(R.id.action_search);
////        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        // Configure the search info and add any event listeners
//        return super.onCreateOptionsMenu(menu);
//    }

    // When the refresh button is clicked, update our local StoryData ArrayList with data from the database
    public void refreshButtonClicked (View v) {
    	updateStoryData();
    }
    
    // When the Create button is clicked, open the CreateStoryActivity
    public void createButtonClicked(View v) {
    	openCreateStoryActivity();
    }
    
    // When the Login button is clicked, open the LoginActivity
    public void loginButtonClicked(View v) {
    	openLoginActivity();
    }
    
    // Update mStoryData with the data currently in the database
    public void updateStoryData() {
		Log.d(LOG_TAG, "updateStoryData");
		try {
			// Clear our local cache of StoryData
			mStoryData.clear();

			String filterWord = filterET.getText().toString();

			// create String that will match with 'like' in query
			filterWord = "%" + filterWord + "%";

			// Get all the StoryData in the database
			ArrayList<StoryData> currentList2 = resolver.queryStoryData(null,
					PotlatchSchema.Story.Cols.TAGS + " LIKE ? ",
					new String[] { filterWord }, null);

			// Add all of them to our local ArrayList
			mStoryData.addAll(currentList2);	
			
			// Let the ArrayAdaptor know that we changed the data in its array.
			aa.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Error connecting to Content Provider" + e.getMessage());
			e.printStackTrace();
		}
	}
}