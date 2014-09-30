package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "gift_metadata")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.gift", joinColumns = @JoinColumn(name = "gift_id")) })
public class ServerGiftMetadata {
    
    public static final String LIKES = "user_likes";
    public static final String FLAGGED = "user_flagged";

    private ServerGiftMetadataPk pk = new ServerGiftMetadataPk();

    @Column(name = LIKES)
    private boolean likes = false;
    
    @Column(name = FLAGGED)
    private boolean flagged = false;

    @EmbeddedId
    public ServerGiftMetadataPk getPk() {
        return pk;
    }
    
    public ServerGiftMetadata() {
        // no-args constructor
    }
    
    public ServerGiftMetadata(ServerGiftMetadataPk pk) {
        this.pk = pk;
    }

    @Transient
    public ServerUser getUser() {
        return getPk().getUser();
    }

    public void setUser(ServerUser user) {
        getPk().setUser(user);
    }

    @Transient
    public ServerGift getGift() {
        return getPk().getGift();
    }

    public void setGift(ServerGift gift) {
        getPk().setGift(gift);
    }

    public boolean likes() {
        return likes;
    }

    public void setLikes(boolean userLike) {
        this.likes = userLike;
    }

    public boolean hasFlagged() {
        return flagged;
    }

    public void setFlagged(boolean userFlagged) {
        this.flagged = userFlagged;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        ServerGiftMetadata other = (ServerGiftMetadata) obj;
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServerGiftMetadata [pk=" + pk + ", userLike=" + likes + ", userFlagged="
                + flagged + "]";
    }

}
