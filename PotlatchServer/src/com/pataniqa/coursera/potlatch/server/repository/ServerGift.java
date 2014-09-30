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

import com.pataniqa.coursera.potlatch.model.Gift;

@Entity
@Table(name="gift")
public class ServerGift {
    
    public static final String LIKES = "likes";
    public static final String CREATED = "created";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gift_id")
    private long id;

    private String title;
    private String description;
    private String videoUri;
    private String imageUri;
    @Column(name=LIKES)
    private long likes;
    private boolean flagged;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name=CREATED)
    private Date created;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private ServerUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "giftchain_id", referencedColumnName = "giftchain_id")
    private ServerGiftChain giftChain;
    
    public ServerGift() {

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
        this.flagged = false;
    }

    public Gift toClient() {
        return new Gift(id, title, description, videoUri, imageUri, created, user.getId(),
                giftChain.getGiftChainID());
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Gift [id=" + id + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", user=" + user + ", giftChain=" + giftChain + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        ServerGift other = (ServerGift) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public long getID() {
        return id;
    }

    public long getGiftID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getUserID() {
        return user.getId();
    }

    public long getGiftChainID() {
        return giftChain.getGiftChainID();
    }

    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }

    public ServerGiftChain getGiftChain() {
        return giftChain;
    }

    public void setGiftChain(ServerGiftChain giftChain) {
        this.giftChain = giftChain;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
