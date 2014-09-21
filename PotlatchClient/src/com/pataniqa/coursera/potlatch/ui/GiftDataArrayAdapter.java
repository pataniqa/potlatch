package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftData;

/**
 * This is an ArrayAdapter for an array of GiftData.
 */
public class GiftDataArrayAdapter extends ArrayAdapter<GiftData> {

    private static final String LOG_TAG = GiftDataArrayAdapter.class.getCanonicalName();

    private int resource;
    private final LayoutInflater inflater;

    public GiftDataArrayAdapter(Context context, int resource, List<GiftData> items) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor");
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Called by components that need to get a View that represents an ArrayList
     * of GiftData.
     * 
     * @param position The position of the item in the ArrayList.
     * @param view Used to recycle views.
     * @param parent The parent ViewGroup.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "getView");
        ViewHolder holder;
        
        try {
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(resource, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            GiftData item = getItem(position);
            holder.image.setImageURI(Uri.parse(item.imageUri));
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setScaleType(ScaleType.FIT_CENTER);
            holder.title.setText("" + item.title);
            holder.description.setText("" + item.description);

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.gift_listview_custom_row_img)
        ImageView image;
        @InjectView(R.id.gift_listview_custom_row_title_textView)
        TextView title;
        @InjectView(R.id.gift_listview_custom_row_description_textView)
        TextView description;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}