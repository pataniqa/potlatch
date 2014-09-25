package com.pataniqa.coursera.potlatch.model;

public class GiftChain implements HasID, IGiftChain {

    private long giftChainID;
    private String giftChainName;

    public GiftChain() {

    }

    public GiftChain(long giftChainID, String giftChainName) {
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }

    @Override
    public String toString() {
        return "GiftChain [giftChainID=" + giftChainID + ", giftChainName=" + giftChainName + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftChain clone() {
        return new GiftChain(HasID.UNDEFINED_ID, giftChainName);
    }

    @Override
    public long getID() {
        return giftChainID;
    }

    @Override
    public void setID(long id) {
        giftChainID = id;
    }

    @Override
    public long getGiftChainID() {
        return giftChainID;
    }

    @Override
    public void setGiftChainID(long giftChainID) {
        this.giftChainID = giftChainID;
    }

    @Override
    public String getGiftChainName() {
        return giftChainName;
    }

    @Override
    public void setGiftChainName(String giftChainName) {
        this.giftChainName = giftChainName;
    }
    
}
