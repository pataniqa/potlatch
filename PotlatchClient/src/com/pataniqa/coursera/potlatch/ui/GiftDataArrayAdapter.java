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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewSwitcher;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.ClientGift;

/**
 * This is an ArrayAdapter for an array of GiftData.
 */
public class GiftDataArrayAdapter extends ArrayAdapter<ClientGift> {

    private static final String LOG_TAG = GiftDataArrayAdapter.class.getCanonicalName();

    private final LayoutInflater inflater;
    private final ShowGiftChainCallback giftChainCallback;
    private int resource;

    public GiftDataArrayAdapter(Context context,
            int resource,
            List<ClientGift> items,
            ShowGiftChainCallback giftChainCallback) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor");
        this.resource = resource;
        inflater = LayoutInflater.from(context);
        this.giftChainCallback = giftChainCallback;
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
                holder = new ViewHolder(convertView, giftChainCallback);
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
        @InjectView(R.id.gift_listview_custom_row_viewswitcher)
        ViewSwitcher viewSwitcher;
        @InjectView(R.id.gift_listview_custom_row_video)
        VideoView video;

        View view;
        private final ShowGiftChainCallback giftChainCallback;

        public ViewHolder(View view, ShowGiftChainCallback giftChainCallback) {
            ButterKnife.inject(this, view);
            this.view = view;
            this.giftChainCallback = giftChainCallback;
        }

        public void setGiftData(final ClientGift gift) {

            if (gift.videoUri != null && !gift.videoUri.isEmpty()) {
                if (viewSwitcher.getCurrentView() != video)
                    viewSwitcher.showNext();
                MediaController mediaController = new MediaController(view.getContext());
                mediaController.setAnchorView(video);
                video.setMediaController(mediaController);
                video.setVideoURI(Uri.parse(gift.videoUri));
            } else {
                if (viewSwitcher.getCurrentView() != image)
                    viewSwitcher.showPrevious();
                image.setImageURI(Uri.parse(gift.imageUri));
                image.setVisibility(View.VISIBLE);
                image.setScaleType(ScaleType.FIT_CENTER);
            }

            title.setText("" + gift.title);
            description.setText("" + gift.description);
            likes.setText("" + gift.likes);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.like = !gift.like;
                    if (gift.like)
                        gift.likes += 1;
                    else
                        gift.likes -= 1;
                    likeButton.setImageResource(gift.like ? R.drawable.ic_fa_heart
                            : R.drawable.ic_fa_heart_o);
                }
            });

            flagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.flag = !gift.flag;
                    flagButton.setImageResource(gift.flag ? R.drawable.ic_fa_flag
                            : R.drawable.ic_fa_flag_o);
                }
            });

            giftChainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giftChainCallback.showGiftChain(gift.giftChainID);
                }
            });
        }
    }
}