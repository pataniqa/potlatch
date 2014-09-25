package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

public class Gift implements HasID {

    private long keyID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public Date created;
    public long userID;
    public String giftChainName;

    /**
     * Constructor.
     * 
     * @param keyID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     * @param giftChainName
     */
    public Gift(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Date created,
            long userID,
            String giftChainName) {
        this.keyID = keyID;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
        this.giftChainName = giftChainName;
    }

    @Override
    public String toString() {
        return "Gift [keyID=" + keyID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + ", giftChainName=" + giftChainName + "]";
    }

    @Override
    public long getID() {
        return keyID;
    }
    
    @Override
    public void setID(long id) {
        keyID = id;
    }

}
