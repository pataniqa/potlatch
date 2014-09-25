package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

public class Gift implements HasID {

    private long giftID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public Date created;
    public long userID;
    public long giftChainID;
    
    /**
     * Constructor.
     * 
     * @param giftID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     * @param giftChainID
     */
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

}
