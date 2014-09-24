package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface ListGiftsCallback {
    
    void showGiftChain(String giftChainName);
    
    void setLike(ClientGift gift);
    
    void setFlag(ClientGift gift);
    
}