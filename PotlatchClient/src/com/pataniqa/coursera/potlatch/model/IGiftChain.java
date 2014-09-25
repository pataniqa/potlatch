package com.pataniqa.coursera.potlatch.model;

public interface IGiftChain extends HasID {

    long getGiftChainID();

    void setGiftChainID(long giftChainID);

    String getGiftChainName();

    void setGiftChainName(String giftChainName);

}