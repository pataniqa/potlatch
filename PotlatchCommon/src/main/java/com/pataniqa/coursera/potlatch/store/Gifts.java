package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observable;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;

/**
 * The interface for manipulating gifts in the store. This is slightly different
 * from the other interfaces as when the user creates gifts they are Gifts but
 * when they receive they they are GiftResults.
 */
public interface Gifts extends Query<GiftResult>, SaveDelete<Gift> {

    /**
     * Indicates the result order by creation time or by number of likes.
     */
    public enum ResultOrder {
        TIME(0), LIKES(1);

        private final int val;

        private ResultOrder(int v) {
            val = v;
        }

        public int getVal() {
            return val;
        }
        
        public static ResultOrder toEnum(int v) {
            return ResultOrder.values()[v];
        }
    };
    
    /**
     * Indicates if results should be ascending or descending.
     */
    public enum ResultOrderDirection {
        ASCENDING(0), DESCENDING(1);

        private final int val;

        private ResultOrderDirection(int v) {
            val = v;
        }

        public int getVal() {
            return val;
        }

        public static ResultOrderDirection toEnum(int v) {
            return ResultOrderDirection.values()[v];
        }
    };
    
    /**
     * Query gifts by title.
     * 
     * @param title The gift title.
     * @param resultOrder How to order results (likes, creation time).
     * @param resultOrderDirection How to order results (ascending, descending).
     * @param hideFlaggedContent Whether to hide flagged content.
     * @return Indicate the operation has finished.
     */
    Observable<ArrayList<GiftResult>> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    /**
     * Query gifts by user.
     * 
     * @param title The gift title.
     * @param userID The user ID.
     * @param resultOrder How to order results (likes, creation time).
     * @param resultOrderDirection How to order results (ascending, descending).
     * @param hideFlaggedContent Whether to hide flagged content.
     * @return Indicate the operation has finished.
     */
    Observable<ArrayList<GiftResult>> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    /**
     * Query gifts by top gift givers.
     * 
     * @param title The gift title.
     * @param resultOrderDirection How to order results (ascending, descending).
     * @param hideFlaggedContent Whether to hide flagged content.
     * @return Indicate the operation has finished.
     */
    Observable<ArrayList<GiftResult>> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    /**
     * Query gifts by gift chain.
     * 
     * @param title The gift title.
     * @param giftChainID The gift chain ID.
     * @param resultOrder How to order results (likes, creation time).
     * @param resultOrderDirection How to order results (ascending, descending).
     * @param hideFlaggedContent Whether to hide flagged content.
     * @return Indicate the operation has finished.
     */
    Observable<ArrayList<GiftResult>> queryByGiftChain(String title,
            long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    /**
     * Set the image data associated with a gift.
     * 
     * @param id The gift ID.
     * @param imageData The image data. 
     * @return Indicate the operation has finished.
     */
    Observable<Boolean> setImageData(long id, TypedFile imageData);

    /**
     * Get the image data associated with a gift.
     * 
     * @param id The gift ID.
     * @return The image data.
     */
    Observable<Response> getImageData(long id);

    /**
     * Set the video data associated with a gift.
     * 
     * @param id The gift ID.
     * @param videoData The video data.
     * @return Indicate the operation has finished.
     */
    Observable<Boolean> setVideoData(long id, TypedFile videoData);

    /**
     * Get the video data associated with a gift.
     * 
     * @param id The gift ID.
     * @return The video data.
     */
    Observable<Response> getVideoData(long id);
}
