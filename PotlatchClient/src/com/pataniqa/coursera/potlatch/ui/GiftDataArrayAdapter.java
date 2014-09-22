package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.ClientGift;

/**
 * This is an ArrayAdapter for an array of GiftData.
 */
public class GiftDataArrayAdapter extends ArrayAdapter<ClientGift> {

    private static final String LOG_TAG = GiftDataArrayAdapter.class.getCanonicalName();

    private int resource;
    private final LayoutInflater inflater;

    public GiftDataArrayAdapter(Context context, int resource, List<ClientGift> items) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor");
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Returns a View that represents an ArrayList of GiftData.
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
            holder.setGiftData(getItem(position));
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
        @InjectView(R.id.gift_listview_custom_row_like)
        ImageButton likeButton;
        @InjectView(R.id.gift_listview_custom_row_num_Likes_textView)
        TextView likes;
        @InjectView(R.id.gift_listview_custom_row_warning)
        ImageButton flagButton;
        @InjectView(R.id.gift_listview_custom_row_link)
        ImageButton giftChainButton;

        boolean like = false;
        boolean flagged = false;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void setGiftData(ClientGift gift) {
            image.setImageURI(Uri.parse(gift.imageUri)); 
            image.setVisibility(View.VISIBLE);
            image.setScaleType(ScaleType.FIT_CENTER);
            // TODO need a thumbnail for a video

            title.setText("" + gift.title);
            description.setText("" + gift.description);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO need to react this button
                    like = !like;
                    likeButton.setImageResource(like ? R.drawable.ic_fa_heart : R.drawable.ic_fa_heart_o);
                }
            });

            flagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO need to react to this button
                    flagged = !flagged;
                    flagButton.setImageResource(flagged ? R.drawable.ic_fa_flag : R.drawable.ic_fa_flag_o);
                }
            });

            giftChainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO need to react this button
                }
            });
        }

    }

}