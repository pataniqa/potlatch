package com.pataniqa.coursera.potlatch.model;

public interface IGiftResult extends IGift, HasID {

    boolean isLike();

    void setLike(boolean like);

    boolean isFlag();

    void setFlag(boolean flag);

    long getLikes();

    void setLikes(long likes);

    boolean isFlagged();

    void setFlagged(boolean flagged);

    long getUserLikes();

    void setUserLikes(long userLikes);

    String getUsername();

    void setUsername(String username);

    String getGiftChainName();

    void setGiftChainName(String giftChainName);

}