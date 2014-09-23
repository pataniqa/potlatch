package com.pataniqa.coursera.potlatch.model;

public class GiftChain implements HasID {

    public long giftChainID;
    public String giftChainName;

    public GiftChain(long giftChainID, String giftChainName) {
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }

    @Override
    public String toString() {
        return "GiftChain [keyID=" + giftChainID + ", giftChainName=" + giftChainName + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftChain clone() {
        return new GiftChain(-1, giftChainName);
    }

    @Override
    public long getID() {
        return giftChainID;
    }

}