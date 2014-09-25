package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

public interface IGift extends HasID {

    long getGiftID();

    void setGiftID(long giftID);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    String getVideoUri();

    void setVideoUri(String videoUri);

    String getImageUri();

    void setImageUri(String imageUri);

    Date getCreated();

    void setCreated(Date created);

    long getUserID();

    void setUserID(long userID);

    long getGiftChainID();

    void setGiftChainID(long giftChainID);

}