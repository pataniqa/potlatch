package com.pataniqa.coursera.potlatch.model;


public class Gift implements HasID {

    public final long keyID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public long created; // TODO use better time representation
    public long userID;
    public long giftChainID;

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
     */
    public Gift(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            long created,
            long userID,
            long giftChainID) {
        this.keyID = keyID;
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
        return "Gift [keyID=" + keyID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + ", giftChainID=" + giftChainID + "]";
    }

    @Override
    public long getID() {
        return keyID;
    }

}
