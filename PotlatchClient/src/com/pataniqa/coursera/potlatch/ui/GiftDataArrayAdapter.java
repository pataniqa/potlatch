package com.pataniqa.coursera.potlatch.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftResult;

/**
 * This is an ArrayAdapter for an array of GiftData.
 */
public class GiftDataArrayAdapter extends ArrayAdapter<GiftResult> {

    private static final String LOG_TAG = GiftDataArrayAdapter.class.getCanonicalName();

    private final LayoutInflater inflater;
    private final ListGiftsCallback giftChainCallback;
    private int resource;

    public GiftDataArrayAdapter(Context context,
            int resource,
            List<GiftResult> items,
            ListGiftsCallback giftChainCallback) {
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
        
        // TODO use Picasso to cache image loading
        
//        Picasso.with(this.getContext())
//        .load(url)
//        .placeholder(R.drawable.placeholder)
//        .error(R.drawable.error)
//        .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
//        .centerInside()
//        .tag(context)
//        .into(holder.image);

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
        Button giftChainButton;

        View view;
        private final ListGiftsCallback giftChainCallback;

        public ViewHolder(View view, ListGiftsCallback giftChainCallback) {
            ButterKnife.inject(this, view);
            this.view = view;
            this.giftChainCallback = giftChainCallback;
        }

        public void setGiftData(final GiftResult gift) {
           Log.i(LOG_TAG, gift.getVideoUri() + " " + gift.getImageUri() + " " + gift.getGiftChainName());

            if (gift.getVideoUri() != null && !gift.getVideoUri().isEmpty()) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(gift.getVideoUri(),
                        MediaStore.Images.Thumbnails.MICRO_KIND);
                image.setImageBitmap(thumb);
            } else {
                image.setImageURI(Uri.parse(gift.getImageUri()));
            }
            image.setVisibility(View.VISIBLE);
            image.setScaleType(ScaleType.FIT_CENTER);

            if (gift.getGiftChainName() != null && !gift.getGiftChainName().isEmpty()) {
                giftChainButton.setText(gift.getGiftChainName());
            } else {
                giftChainButton.setVisibility(Button.INVISIBLE);
            }

            likeButton.setImageResource(gift.isLike() ? R.drawable.ic_fa_heart
                    : R.drawable.ic_fa_heart_o);
            flagButton
                    .setImageResource(gift.isFlag() ? R.drawable.ic_fa_flag : R.drawable.ic_fa_flag_o);

            title.setText(gift.getTitle());
            description.setText(gift.getDescription());
            likes.setText("" + gift.getLikes());

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.setLike(!gift.isLike());
                    gift.setLikes(gift.getLikes() + (gift.isLike() ? 1 : 0)) ;
                    giftChainCallback.setLike(gift);
                    likeButton.setImageResource(gift.isLike() ? R.drawable.ic_fa_heart
                            : R.drawable.ic_fa_heart_o);
                    likes.setText("" + gift.getLikes());
                }
            });

            flagButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gift.setFlag(!gift.isFlag());                    
                    giftChainCallback.setFlag(gift);
                    flagButton.setImageResource(gift.isFlag() ? R.drawable.ic_fa_flag
                            : R.drawable.ic_fa_flag_o);
                }
            });

            giftChainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giftChainCallback.showGiftChain(gift.getGiftChainID());
                }
            });
        }
    }
}