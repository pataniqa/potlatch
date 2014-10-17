package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.utils.ImageUtils;
import com.pataniqa.coursera.potlatch.utils.PicassoFactory;

/**
 * This is an ArrayAdapter for an array of GiftData.
 */
public class GiftDataArrayAdapter extends ArrayAdapter<GiftResult> {

    private static final String LOG_TAG = GiftDataArrayAdapter.class.getCanonicalName();

    private final LayoutInflater inflater;
    private final ListGiftsCallback listGiftsCallback;
    private int resource;
    private final PicassoFactory picasso;

    public GiftDataArrayAdapter(Context context,
            int resource,
            List<GiftResult> items,
            ListGiftsCallback listGiftsCallback,
            PicassoFactory picasso) {
        super(context, resource, items);
        Log.v(LOG_TAG, "constructor");
        this.resource = resource;
        inflater = LayoutInflater.from(context);
        this.listGiftsCallback = listGiftsCallback;
        this.picasso = picasso;
    }

    /**
     * Returns a View that represents an ArrayList of GiftData.
     * 
     * @param position The position of the item in the ArrayList.
     * @param view Used to recycle views.
     * @param parent The parent ViewGroup.
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Log.d(LOG_TAG, "getView");
        ViewHolder holder;

        View view = null;
        try {
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
                view = convertView;
            } else {
                view = inflater.inflate(resource, parent, false);
                holder = new ViewHolder(convertView, listGiftsCallback, picasso);
                view.setTag(holder);
            }
            holder.setGiftData(getItem(position));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.gift_listview_custom_row_img) ImageView image;
        @InjectView(R.id.gift_listview_custom_row_title_textView) TextView title;
        @InjectView(R.id.gift_listview_custom_row_description_textView) TextView description;
        @InjectView(R.id.gift_listview_custom_row_like) ImageButton likeButton;
        @InjectView(R.id.gift_listview_custom_row_num_Likes_textView) TextView likes;
        @InjectView(R.id.gift_listview_custom_row_warning) ImageButton flagButton;
        @InjectView(R.id.gift_listview_custom_row_link) ImageButton giftChainButton;
        @InjectView(R.id.gift_listview_custom_row_user) ImageButton moreFromThisUserButton;

        private final ListGiftsCallback listGiftsCallback;
        private final View view;
        private final PicassoFactory picasso;

        public ViewHolder(View view, ListGiftsCallback listGiftsCallback, PicassoFactory picasso) {
            ButterKnife.inject(this, view);
            this.view = view;
            this.listGiftsCallback = listGiftsCallback;
            this.picasso = picasso;
        }

        public void setGiftData(final GiftResult gift) {
            Log.d(LOG_TAG,
                    gift.getVideoUri() + " " + gift.getImageUri() + " " + gift.getGiftChainName());

            image.setVisibility(View.VISIBLE);
            
            WindowManager windowManager = (WindowManager) view.getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int maxsize = ImageUtils.getMaxSize(windowManager);
            
            picasso.load(view.getContext(), gift.getImageUri()).resize(maxsize, maxsize)
                    .placeholder(R.drawable.ic_fa_image).centerInside().into(image);

            if (gift.getGiftChainName() != null && !gift.getGiftChainName().isEmpty()) {
                giftChainButton.setVisibility(View.VISIBLE);
            } else {
                giftChainButton.setVisibility(View.INVISIBLE);
            }

            likeButton.setImageResource(gift.isLike() ? R.drawable.ic_fa_heart
                    : R.drawable.ic_fa_heart_o);
            flagButton.setImageResource(gift.isFlag() ? R.drawable.ic_fa_flag
                    : R.drawable.ic_fa_flag_o);

            title.setText(gift.getTitle());
            description.setText(gift.getDescription());
            likes.setText("" + gift.getLikes());

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.setLike(!gift.isLike());
                    gift.setLikes(gift.getLikes() + (gift.isLike() ? 1 : 0));
                    listGiftsCallback.setLike(gift);
                    likeButton.setImageResource(gift.isLike() ? R.drawable.ic_fa_heart
                            : R.drawable.ic_fa_heart_o);
                    likes.setText("" + gift.getLikes());
                }
            });

            flagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.setFlag(!gift.isFlag());
                    listGiftsCallback.setFlag(gift);
                    flagButton.setImageResource(gift.isFlag() ? R.drawable.ic_fa_flag
                            : R.drawable.ic_fa_flag_o);
                }
            });

            giftChainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listGiftsCallback.createGiftChainQuery(gift.getGiftChainID(),
                            gift.getGiftChainName());
                }
            });

            moreFromThisUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listGiftsCallback.createUserQuery(gift.getUserID(), gift.getUsername());
                }
            });
        }
    }
}