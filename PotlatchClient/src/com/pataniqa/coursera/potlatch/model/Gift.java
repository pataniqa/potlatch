package com.pataniqa.coursera.potlatch.model;

import android.text.format.Time;

public class Gift {
    
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public Time created = new Time();
    public long userID;
    
    /**
     * Constructor.
     * 
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     */
    public Gift(String title,
            String description,
            String videoUri,
            String imageUri,
            Time created,
            long userID) {
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
    }

}
