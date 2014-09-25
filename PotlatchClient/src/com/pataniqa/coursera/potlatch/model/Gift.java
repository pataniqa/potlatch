package com.pataniqa.coursera.potlatch.model;

import java.util.Date;


public class Gift implements HasID {

    private long giftID;
    private String title;
    private String description;
    private String videoUri;
    private String imageUri;
    private Date created;
    private long userID;
    private long giftChainID;

    public Gift(long giftID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Date created,
            long userID,
            long giftChainID) {
        this.giftID = giftID;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
        this.giftChainID = giftChainID;
    }

    public Gift() {
    }

    public String toString() {
        return "Gift [giftID=" + giftID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + ", giftChainID=" + giftChainID + "]";
    }

    public long getID() {
        return giftID;
    }

    public void setID(long id) {
        giftID = id;
    }

    public long getGiftID() {
        return giftID;
    }

    public void setGiftID(long giftID) {
        this.giftID = giftID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public Date getCreated() {
        return created;
    }

    public long getUserID() {
        return userID;
    }

    public long getGiftChainID() {
        return giftChainID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setGiftChainID(long giftChainID) {
        this.giftChainID = giftChainID;
    }

}
