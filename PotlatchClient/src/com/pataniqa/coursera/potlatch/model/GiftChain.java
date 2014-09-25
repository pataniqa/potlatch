package com.pataniqa.coursera.potlatch.model;


public class GiftChain implements HasID {

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

    public long getGiftChainID() {
        return giftChainID;
    }

    public void setGiftChainID(long giftChainID) {
        this.giftChainID = giftChainID;
    }

    public String getGiftChainName() {
        return giftChainName;
    }

    public void setGiftChainName(String giftChainName) {
        this.giftChainName = giftChainName;
    }
    
}
