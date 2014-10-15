package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface ListGiftsCallback {
    
    void createGiftChainQuery(long giftChainID, String giftChainName);
    
    void setLike(GiftResult gift);
    
    void setFlag(GiftResult gift);

    void createUserQuery(long userID, String queryName);
    
}