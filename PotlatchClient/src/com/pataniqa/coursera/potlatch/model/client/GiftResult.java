package com.pataniqa.coursera.potlatch.model.client;

import java.util.Date;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.model.IGift;
import com.pataniqa.coursera.potlatch.model.IGiftResult;

/**
 * ClientGift is a de-normalized version of the data to make it easy to present in the user interface.
 */
public class GiftResult extends Gift implements HasID, IGiftResult, IGift {

    private boolean like;
    private boolean flag;
    private long likes;
    private boolean flagged;
    private long userLikes;
    private String username;
    private String giftChainName;

    public GiftResult() {}
    
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

    @Override
    public String toString() {
        return "ClientGift [like=" + like + ", flag=" + flag + ", likes=" + likes + ", flagged="
                + flagged + ", userLikes=" + userLikes + ", username=" + username
                + ", giftChainName=" + giftChainName + ", toString()=" + super.toString()
                + ", getID()=" + getID() + ", getGiftID()=" + getGiftID() + ", getTitle()="
                + getTitle() + ", getDescription()=" + getDescription() + ", getVideoUri()="
                + getVideoUri() + ", getImageUri()=" + getImageUri() + ", getCreated()="
                + getCreated() + ", getUserID()=" + getUserID() + ", getGiftChainID()="
                + getGiftChainID() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftResult clone() {
        return new GiftResult(HasID.UNDEFINED_ID, getTitle(), getDescription(), getVideoUri(), getImageUri(), getCreated(), getUserID(), like,
                flag, likes, flagged, getGiftChainID(), giftChainName, userLikes, username);
    }

    @Override
    public boolean isLike() {
        return like;
    }

    @Override
    public void setLike(boolean like) {
        this.like = like;
    }

    @Override
    public boolean isFlag() {
        return flag;
    }

    @Override
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public long getLikes() {
        return likes;
    }

    @Override
    public void setLikes(long likes) {
        this.likes = likes;
    }

    @Override
    public boolean isFlagged() {
        return flagged;
    }

    @Override
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    @Override
    public long getUserLikes() {
        return userLikes;
    }

    @Override
    public void setUserLikes(long userLikes) {
        this.userLikes = userLikes;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getGiftChainName() {
        return giftChainName;
    }

    @Override
    public void setGiftChainName(String giftChainName) {
        this.giftChainName = giftChainName;
    }
    
    
}