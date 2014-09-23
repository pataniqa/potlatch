package com.pataniqa.coursera.potlatch.model;

import android.text.format.Time;

public class Gift implements HasID {

    public final long keyID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public Time created = new Time();
    public long userID;
    public long giftChainID;
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
     * @param giftChainID
     * @param giftChainName
     */
    public Gift(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Time created,
            long userID,
            long giftChainID,
            String giftChainName) {
        this.keyID = keyID;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }
    
    @Override
    public String toString() {
        return "Gift [keyID=" + keyID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + ", giftChainID=" + giftChainID + ", giftChainName="
                + giftChainName + "]";
    }

    @Override
    public long getID() {
        return keyID;
    }

}
