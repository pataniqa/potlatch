package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GiftChain {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="gift_id")
    private long giftChainID;
	
    public String giftChainName;

    public long getGiftChainID() {
        return giftChainID;
    }

    @Override
    public String toString() {
        return "GiftChain [giftChainID=" + giftChainID + ", giftChainName=" + giftChainName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (giftChainID ^ (giftChainID >>> 32));
        result = prime * result + ((giftChainName == null) ? 0 : giftChainName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GiftChain other = (GiftChain) obj;
        if (giftChainID != other.giftChainID)
            return false;
        if (giftChainName == null) {
            if (other.giftChainName != null)
                return false;
        } else if (!giftChainName.equals(other.giftChainName))
            return false;
        return true;
    }

}
