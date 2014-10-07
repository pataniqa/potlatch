package com.pataniqa.coursera.potlatch.store;

public interface GiftMetadata {

    void setLike(long giftID, boolean like);

    void setFlag(long giftID, boolean flag);

}
