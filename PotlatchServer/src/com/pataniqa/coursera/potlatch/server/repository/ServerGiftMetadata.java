package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
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

    private ServerGiftMetadataPk pk = new ServerGiftMetadataPk();

    private boolean userLike;
    private boolean userFlagged;

    @EmbeddedId
    public ServerGiftMetadataPk getPk() {
        return pk;
    }

    private void setPk(ServerGiftMetadataPk pk) {
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

    public boolean isUserLike() {
        return userLike;
    }

    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }

    public boolean isUserFlagged() {
        return userFlagged;
    }

    public void setUserFlagged(boolean userFlagged) {
        this.userFlagged = userFlagged;
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
        return "ServerGiftMetadata [pk=" + pk + ", userLike=" + userLike + ", userFlagged="
                + userFlagged + "]";
    }

}
