package com.pataniqa.coursera.potlatch.server.model;

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
import lombok.NoArgsConstructor;

@EqualsAndHashCode(exclude = { "liked", "flagged" })
@NoArgsConstructor
@ToString
@Entity
@Table(name = "gift_metadata")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = ServerUser.ID)),
        @AssociationOverride(name = "pk.gift", joinColumns = @JoinColumn(name = ServerGift.ID)) })
public class ServerGiftMetadata {

    @Setter private ServerGiftMetadataPk pk = new ServerGiftMetadataPk();

    public static final String LIKED = "user_liked";
    @Getter @Setter @Column(name = LIKED) private boolean liked = false;
    
    public static final String FLAGGED = "user_flagged";
    @Getter @Setter @Column(name = FLAGGED) private boolean flagged = false;

    @EmbeddedId
    public ServerGiftMetadataPk getPk() {
        return pk;
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