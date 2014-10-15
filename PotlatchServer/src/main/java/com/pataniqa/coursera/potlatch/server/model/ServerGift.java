package com.pataniqa.coursera.potlatch.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.pataniqa.coursera.potlatch.model.GetId;
import com.pataniqa.coursera.potlatch.model.Gift;

@NoArgsConstructor
@EqualsAndHashCode(exclude = { "title", "description", "likes", "flagged", "created", "user", "giftChain" })
@ToString
@Entity
@Table(name = "gift")
public class ServerGift implements GetId {

    public static final String ID = "gift_id";
    @Getter @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Getter @Setter private String title;
    @Getter @Setter private String description;
    @Getter @Setter private String videoUri;
    @Getter @Setter private String imageUri;

    public static final String LIKES = "gift_likes";
    @Getter @Column(name = LIKES) private long likes = 0;
    
    public static final String FLAGGED = "gift_flagged";
    @Column(name = FLAGGED) private long flagged = 0;

    public static final String CREATED = "created";
    @Getter @Temporal(TemporalType.TIMESTAMP) @Column(name = CREATED) private Date created;

    @Getter @ManyToOne 
    @PrimaryKeyJoinColumn(name = ServerUser.ID, referencedColumnName = ServerUser.ID) 
    private ServerUser user;

    @Getter @ManyToOne 
    @PrimaryKeyJoinColumn(name = ServerGiftChain.ID, referencedColumnName = ServerGiftChain.ID) 
    private ServerGiftChain giftChain;

    public ServerGift(Gift gift, ServerUser user, ServerGiftChain giftChain) {
        this.title = gift.getTitle();
        this.description = gift.getDescription();
        this.created = gift.getCreated();
        this.user = user;
        this.giftChain = giftChain;
    }
    
    public ServerGift update(Gift gift, ServerUser user, ServerGiftChain giftChain) {
        this.title = gift.getTitle();
        this.description = gift.getDescription();
        this.created = gift.getCreated();
        this.user = user;
        this.giftChain = giftChain;
        return this;
    }

    public boolean isFlagged() {
        return flagged > 0;
    }

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        this.likes -= 1;
    }

    public void decrementFlagged() {
        this.flagged -= 1;
    }

    public void incrementFlagged() {
        this.flagged += 1;
    }
}
