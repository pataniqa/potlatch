package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

/**
 * ClientGift is a de-normalized version of the data to make it easy to present in the user interface.
 */
public class ClientGift extends Gift implements HasID {

    public boolean like;
    public boolean flag;
    public long likes;
    public boolean flagged;
    public long userLikes;
    public String username;

    /**
     * Constructor
     * 
     * @param keyID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     * @param like
     * @param flag
     * @param likes
     * @param flagged
     * @param giftChainName
     * @param userLikes
     * @param username
     */
    public ClientGift(long keyID,
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
            String giftChainName,
            long userLikes,
            String username) {
        super(keyID, title, description, videoUri, imageUri, created, userID, giftChainName);
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
                + flagged + ", userLikes=" + userLikes + ", username=" + username + ", keyID="
                + getID() + ", title=" + title + ", description=" + description + ", videoUri="
                + videoUri + ", imageUri=" + imageUri + ", created=" + created + ", userID="
                + userID + ", giftChainName=" + giftChainName + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public ClientGift clone() {
        return new ClientGift(HasID.UNDEFINED_ID, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainName, userLikes, username);
    }
}