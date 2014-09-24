package com.pataniqa.coursera.potlatch.server.repository;

import java.io.Serializable;

public class GiftMetadataId implements Serializable {
    
    private long giftID;
    private long userID;
    
    public long getGiftID() {
        return giftID;
    }
    public void setGiftID(long giftID) {
        this.giftID = giftID;
    }
    public long getUserID() {
        return userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
    

}
