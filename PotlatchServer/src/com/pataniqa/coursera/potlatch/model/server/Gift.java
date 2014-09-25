package com.pataniqa.coursera.potlatch.model.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.pataniqa.coursera.potlatch.model.IGift;
import com.pataniqa.coursera.potlatch.server.repository.GiftChain;
import com.pataniqa.coursera.potlatch.server.repository.User;

import java.util.Date;

@Entity
public class Gift implements IGift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="gift_id")
	private long id;
	
    private String title;
    private String description;
    private String videoUri;
    private String imageUri;
    
    @Temporal(TemporalType.TIMESTAMP) 
    private Date created;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id",referencedColumnName="user_id")
    private User user;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="giftchain_id",referencedColumnName="giftchain_id")
    private GiftChain giftChain;

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
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((giftChain == null) ? 0 : giftChain.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((imageUri == null) ? 0 : imageUri.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((videoUri == null) ? 0 : videoUri.hashCode());
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
        Gift other = (Gift) obj;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (giftChain == null) {
            if (other.giftChain != null)
                return false;
        } else if (!giftChain.equals(other.giftChain))
            return false;
        if (id != other.id)
            return false;
        if (imageUri == null) {
            if (other.imageUri != null)
                return false;
        } else if (!imageUri.equals(other.imageUri))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (videoUri == null) {
            if (other.videoUri != null)
                return false;
        } else if (!videoUri.equals(other.videoUri))
            return false;
        return true;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public long getGiftID() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getVideoUri() {
        return videoUri;
    }

    @Override
    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public long getUserID() {
        return user.getId();
    }

    @Override
    public long getGiftChainID() {
        return giftChain.getId();
    }
	
}
