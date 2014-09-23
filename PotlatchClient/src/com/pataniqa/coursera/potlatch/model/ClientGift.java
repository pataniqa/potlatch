package com.pataniqa.coursera.potlatch.model;

/**
 * ClientGift is a de-normalized version of the data to make it easy to present in the user interface.
 */
public class ClientGift extends Gift implements HasID {

    public boolean like;
    public boolean flag;
    public long likes;
    public boolean flagged;
    public long userLikes;
    public String giftChainName;
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
     * @param giftChainID
     * @param giftChainName
     * @param userLikes
     * @param username
     */
    public ClientGift(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            long created,
            long userID,
            boolean like,
            boolean flag,
            long likes,
            boolean flagged,
            long giftChainID,
            String giftChainName,
            long userLikes,
            String username) {
        super(keyID, title, description, videoUri, imageUri, created, userID, giftChainID);
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
                + flagged + ", userLikes=" + userLikes + ", giftChainName=" + giftChainName
                + ", username=" + username + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public ClientGift clone() {
        return new ClientGift(-1, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainID, giftChainName, userLikes, username);
    }
}