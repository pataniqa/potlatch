package com.pataniqa.coursera.potlatch.model.client;

import java.util.Date;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.model.IGift;

public class Gift implements IGift, HasID {

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

    @Override
    public long getGiftID() {
        return giftID;
    }

    @Override
    public void setGiftID(long giftID) {
        this.giftID = giftID;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getVideoUri() {
        return videoUri;
    }

    @Override
    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Override
    public long getGiftChainID() {
        return giftChainID;
    }

    @Override
    public void setGiftChainID(long giftChainID) {
        this.giftChainID = giftChainID;
    }

}
