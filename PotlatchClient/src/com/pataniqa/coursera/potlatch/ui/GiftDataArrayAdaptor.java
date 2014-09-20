package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import com.pataniqa.coursera.potlatch.storage.GiftCreator;
import com.pataniqa.coursera.potlatch.storage.GiftData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pataniqa.coursera.potlatch.R;

/**
 * This is an ArrayAdapter for an array of StoryData. It is used by the ListView
 * present in ListStoryActivity to display each StoryData object in the array as
 * a row in a GUI list.
 * 
 * This is an example of the Adapter pattern. In this case, this class wraps
 * some plain data (StoryData) into an interface that can be recognized and used
 * by the ListView Object.
 */
public class GiftDataArrayAdaptor extends ArrayAdapter<GiftData> {

    private static final String LOG_TAG = GiftDataArrayAdaptor.class.getCanonicalName();

    // The resource ID of a Layout used for instantiating Views
    int resource;

    /**
     * Constructs this adapter
     * 
     * @param _context A context to create the Views in
     * @param _resource The resource ID of a TextView to use when instantiating
     *            Views
     * @param _items The actual objects we're representing
     */
    public GiftDataArrayAdaptor(Context context, int resource, List<GiftData> items) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor()");
        this.resource = resource;
    }

    /**
     * Called when a component using this adapter (like a ListView) needs to get
     * a View that represents an object in the ArrayList.
     * 
     * @param position The position in the ArrayList of the object we want a
     *            representation of
     * @param convertView A view that has already been instantiated but is
     *            destined to be garbage collected. Use this for recycling Views
     *            and faster performance.
     * @param parent The ViewGroup that the returned view will be a child of.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The view we need to fill out with data
        LinearLayout todoView = null;

        try {
            // Get the data from the ArrayList
            GiftData item = getItem(position);

            // The ID of the data in the database
            long KEY_ID = item.KEY_ID;
            String title = item.title;
            long storyDate = item.giftTime;

            // If there's no View to be recycled, instantiate a new View
            if (convertView == null) {
                todoView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, todoView, true);
            } else {
                // Otherwise, use the View that's already been instantiated
                todoView = (LinearLayout) convertView;
            }

            TextView KEY_IDTV = (TextView) todoView.findViewById(R.id.story_listview_custom_row_KEY_ID_textView);
            KEY_IDTV.setText("Key ID: " + KEY_ID);
            
            TextView tagsTV = (TextView) todoView.findViewById(R.id.story_listview_custom_row_tags_textView);
            tagsTV.setText("" + item.tags.toString());
            
            TextView titleTV = (TextView) todoView.findViewById(R.id.story_listview_custom_row_title_textView);
            titleTV.setText("" + title);
            
            TextView creationTimeTV = (TextView) todoView
                    .findViewById(R.id.story_listview_custom_row_creation_time_textView);
            creationTimeTV.setText("" + GiftCreator.getStringDate(storyDate));

        } catch (Exception e) {
            Toast.makeText(getContext(), "exception in ArrayAdpter: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return todoView;
    }

}