package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.pataniqa.coursera.potlatch.model.Gift;

@EqualsAndHashCode(exclude = { "title", "description", "videoUri", "imageUri", "likes", "flagged", "created", "user", "giftChain" })
@ToString
@Entity
@Table(name = "gift")
public class ServerGift {

    public static final String ID = "gift_id";
    @Getter @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Getter @Setter private String title;
    @Getter @Setter private String description;
    @Getter @Setter private String videoUri;
    @Getter @Setter private String imageUri;

    public static final String LIKES = "gift_likes";
    @Getter @Column(name = LIKES) private long likes;
    
    public static final String FLAGGED = "gift_flagged";
    @Column(name = FLAGGED) private long flagged;

    public static final String CREATED = "created";
    @Getter @Temporal(TemporalType.TIMESTAMP) @Column(name = CREATED) private Date created;

    @Getter @ManyToOne(optional = false) 
    @JoinColumn(name = ServerUser.ID, referencedColumnName = ServerUser.ID) 
    private ServerUser user;

    @Getter @ManyToOne(optional = false) 
    @JoinColumn(name = ServerGiftChain.ID, referencedColumnName = ServerGiftChain.ID) 
    private ServerGiftChain giftChain;

    public ServerGift() {
        // zero args constructor
    }

    public ServerGift(Gift gift, ServerUser user, ServerGiftChain giftChain) {
        this.title = gift.getTitle();
        this.description = gift.getDescription();
        this.videoUri = gift.getVideoUri();
        this.imageUri = gift.getImageUri();
        this.created = gift.getCreated();
        this.user = user;
        this.giftChain = giftChain;
        this.likes = 0;
        this.flagged = 0;
    }

    public Gift toClient() {
        return new Gift(id,
                title,
                description,
                videoUri,
                imageUri,
                created,
                user.getId(),
                giftChain.getId());
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
