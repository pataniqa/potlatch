package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * GiftResult is a de-normalized version of the data to make it easy to present
 * in the user interface.
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GiftResult extends Gift implements HasID {

    @Getter @Setter private boolean like;
    @Getter @Setter private boolean flag;
    @Getter @Setter private long likes;
    @Getter @Setter private boolean flagged;
    @Getter private long userLikes;
    @Getter private String username;
    @Getter private String giftChainName;

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