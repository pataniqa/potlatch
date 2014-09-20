package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
 * present in ListGiftActivity to display each GiftData object in the array as a
 * row in a GUI list.
 * 
 * This is an example of the Adapter pattern. In this case, this class wraps
 * some plain data (GiftData) into an interface that can be recognized and used
 * by the ListView Object.
 */
public class GiftDataArrayAdaptor extends ArrayAdapter<GiftData> {

    private static final String LOG_TAG = GiftDataArrayAdaptor.class.getCanonicalName();

    private int resource;

    public GiftDataArrayAdaptor(Context context, int resource, List<GiftData> items) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor");
        this.resource = resource;
    }

    /**
     * Called by components that need to get a View that represents an ArrayList
     * of GiftData.
     * 
     * @param position The position of the item in the ArrayList.
     * @param convertView Used to recycle views.
     * @param parent The parent ViewGroup.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The view we need to fill out with data
        LinearLayout giftView = null;

        try {
            // If there's no View to be recycled, instantiate a new View
            if (convertView == null) {
                giftView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, giftView, true);
            } else {
                // Otherwise, use the View that's already been instantiated
                giftView = (LinearLayout) convertView;
            }

            GiftData item = getItem(position);
            ImageView imageView = (ImageView) giftView
                    .findViewById(R.id.gift_listview_custom_row_img);
            imageView.setImageURI(Uri.parse(item.imageUri));
            imageView.setVisibility(View.VISIBLE);
            imageView.setScaleType(ScaleType.FIT_CENTER);

            TextView titleTV = (TextView) giftView
                    .findViewById(R.id.gift_listview_custom_row_title_textView);
            titleTV.setText("" + item.title);

            TextView descriptionTV = (TextView) giftView
                    .findViewById(R.id.gift_listview_custom_row_description_textView);
            descriptionTV.setText("" + item.description);

        } catch (Exception e) {
            Toast.makeText(getContext(), "exception in ArrayAdpter: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return giftView;
    }

}