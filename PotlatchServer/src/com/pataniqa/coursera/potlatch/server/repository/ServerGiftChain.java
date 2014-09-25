package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.Objects;
import com.pataniqa.coursera.potlatch.model.GiftChain;

@Entity
public class ServerGiftChain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gift_id")
    private long giftChainID;

    public String giftChainName;

    public long getGiftChainID() {
        return giftChainID;
    }
    
    public ServerGiftChain() {
        
    }
    
    @Override
    public String toString() {
        return "GiftChain [giftChainID=" + giftChainID + ", giftChainName=" + giftChainName + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(giftChainID, giftChainName);
    }

    /**
     * Two Videos are considered equal if they have exactly the same values for
     * their name, url, and duration.
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerGiftChain) {
            ServerGiftChain other = (ServerGiftChain) obj;
            return giftChainID == other.giftChainID
                    && Objects.equal(giftChainName, other.giftChainName);
        } else {
            return false;
        }
    }
    
    public ServerGiftChain(GiftChain giftChain) {
        this.giftChainID = giftChain.getID();
        this.giftChainName = giftChain.getGiftChainName();
    }
    
    public GiftChain toClient() {
        return new GiftChain(giftChainID, giftChainName);
    }

}
