package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;

/**
 * This is an ArrayAdapter for an array of GiftData. It is used by the ListView
 * present in ListGiftActivity to display each GiftData object in the array as
 * a row in a GUI list.
 * 
 * This is an example of the Adapter pattern. In this case, this class wraps
 * some plain data (GiftData) into an interface that can be recognized and used
 * by the ListView Object.
 */
public class GiftDataArrayAdaptor extends ArrayAdapter<GiftData> {

    private static final String LOG_TAG = GiftDataArrayAdaptor.class.getCanonicalName();

    // The resource ID of a Layout used for instantiating Views
    private int resource;

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
        Log.v(LOG_TAG, "constructor");
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
        LinearLayout giftView = null;

        try {
            // Get the data from the ArrayList
            GiftData item = getItem(position);

            // If there's no View to be recycled, instantiate a new View
            if (convertView == null) {
                giftView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, giftView, true);
            } else {
                // Otherwise, use the View that's already been instantiated
                giftView = (LinearLayout) convertView;
            }
            
            Bitmap thumbnail = (BitmapFactory.decodeFile(item.imageUri));
            ImageView imageView = (ImageView) giftView.findViewById(R.id.gift_listview_custom_row_img);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(thumbnail);
            imageView.setScaleType(ScaleType.FIT_CENTER);

            TextView titleTV = (TextView) giftView.findViewById(R.id.gift_listview_custom_row_title_textView);
            titleTV.setText("" + item.title);
            
            TextView descriptionTV = (TextView) giftView.findViewById(R.id.gift_listview_custom_row_description_textView);
            descriptionTV.setText("" + item.description);

        } catch (Exception e) {
            Toast.makeText(getContext(), "exception in ArrayAdpter: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return giftView;
    }

}