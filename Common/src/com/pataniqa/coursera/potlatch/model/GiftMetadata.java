package com.pataniqa.coursera.potlatch.model;

public class GiftMetadata {

    public long giftID;
    public long userID;
    public boolean like;
    public boolean flag;

    public GiftMetadata(long giftID, long userID, boolean like, boolean flag) {
        this.giftID = giftID;
        this.userID = userID;
        this.like = like;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "GiftMetadata [giftID=" + giftID + ", userID=" + userID + ", like=" + like
                + ", flag=" + flag + "]";
    }

}
