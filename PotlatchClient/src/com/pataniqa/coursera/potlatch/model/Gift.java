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

    @Override
    public String toString() {
        return "Gift [giftID=" + giftID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + ", giftChainID=" + giftChainID + "]";
    }

    @Override
    public long getID() {
        return giftID;
    }
    
    @Override
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getGiftChainID() {
        return giftChainID;
    }

    public void setGiftChainID(long giftChainID) {
        this.giftChainID = giftChainID;
    }

}
