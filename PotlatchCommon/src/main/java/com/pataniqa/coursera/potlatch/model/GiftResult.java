package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * GiftResult is a de-normalized version of the gift / gift chain / user data to make it easy to present
 * in the user interface.
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class GiftResult extends Gift implements HasId {

    /**
     * Has the user liked this gift?
     */
    @Setter private boolean like;
    
    /**
     * Has this user flagged the gift as inappropriate? 
     */
    @Setter private boolean flag;
    
    /**
     * The total number of likes received by this gift.
     */
    @Setter private long likes;
    
    /**
     * Has any user flagged this gift as inappropriate?
     */
    @Setter private boolean flagged;
    
    /**
     * How many likes has the creator of this gift received?
     */
    private long userLikes;
    
    /**
     * The gift creator user name. 
     */
    private String username;
    
    /**
     * The name of the gift chain associated with this gift.
     */
    private String giftChainName;

    public GiftResult(long giftID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Date created,
            long userID,
            boolean like,
            boolean flag,
            long likes,
            boolean flagged,
            long giftChainID,
            String giftChainName,
            long userLikes,
            String username) {
        super(giftID, title, description, videoUri, imageUri, created, userID, giftChainID);
        this.giftChainName = giftChainName;
        this.like = like;
        this.flag = flag;
        this.likes = likes;
        this.flagged = flagged;
        this.userLikes = userLikes;
        this.username = username;
    }

}