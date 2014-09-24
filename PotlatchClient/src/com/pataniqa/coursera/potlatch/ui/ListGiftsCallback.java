package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface ListGiftsCallback {
    
    void showGiftChain(String giftChainName);
    
    void like(ClientGift gift);
    
    void unlike(ClientGift gift);
    
    void flag(ClientGift gift);
    
    void unflag(ClientGift gift);
}