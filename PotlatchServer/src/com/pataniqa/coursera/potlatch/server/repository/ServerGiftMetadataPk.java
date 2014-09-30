package com.pataniqa.coursera.potlatch.server.repository;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ServerGiftMetadataPk implements Serializable {

    private static final long serialVersionUID = -707960766740593473L;
    
    private ServerUser user;
    private ServerGift gift;
    
    public ServerGiftMetadataPk() {
        // zero-args constructor
    }
    
    public ServerGiftMetadataPk(ServerUser user, ServerGift gift) {
        this.user = user;
        this.gift = gift;
    }

    @ManyToOne
    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }

    @ManyToOne
    public ServerGift getGift() {
        return gift;
    }

    public void setGift(ServerGift gift) {
        this.gift = gift;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gift == null) ? 0 : gift.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        ServerGiftMetadataPk other = (ServerGiftMetadataPk) obj;
        if (gift == null) {
            if (other.gift != null)
                return false;
        } else if (!gift.equals(other.gift))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServerGiftMetadataPk [user=" + user + ", gift=" + gift + "]";
    }

}
