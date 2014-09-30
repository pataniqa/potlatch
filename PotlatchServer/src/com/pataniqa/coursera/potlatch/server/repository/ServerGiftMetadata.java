package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(exclude = { "likes", "flagged" })
@ToString
@Entity
@Table(name = "gift_metadata")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.gift", joinColumns = @JoinColumn(name = "gift_id")) })
public class ServerGiftMetadata {

    public static final String LIKED = "user_liked";
    public static final String FLAGGED = "user_flagged";

    private ServerGiftMetadataPk pk = new ServerGiftMetadataPk();

    @Getter @Setter @Column(name = LIKED) private boolean liked = false;

    @Getter @Setter @Column(name = FLAGGED) private boolean flagged = false;

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

}
