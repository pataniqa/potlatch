package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.*;

@Entity
public class Gift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="gift_id")
	private long id;
	
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    
    @Temporal(TemporalType.TIMESTAMP) 
    private java.util.Date created;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id",referencedColumnName="user_id")
    public User user;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="giftchain_id",referencedColumnName="giftchain_id")
    public GiftChain giftChain;

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
	
}
