package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.model.GiftResult;

public interface ListGiftsCallback {
    
    void showGiftChain(long giftChainID);
    
    void setLike(GiftResult gift);
    
    void setFlag(GiftResult gift);
    
}