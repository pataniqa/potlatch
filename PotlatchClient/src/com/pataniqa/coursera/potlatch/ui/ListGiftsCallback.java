package com.pataniqa.coursera.potlatch.ui;

import com.pataniqa.coursera.potlatch.model.ClientGift;

public interface ListGiftsCallback {
    
    void showGiftChain(long giftChainID);
    
    void updateGift(ClientGift gift);
}