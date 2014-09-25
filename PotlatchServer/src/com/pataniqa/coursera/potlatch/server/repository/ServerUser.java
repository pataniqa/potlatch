package com.pataniqa.coursera.potlatch.server.repository;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.pataniqa.coursera.potlatch.model.Gift;

@Entity
public class ServerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private long id;
    
    private String username;
    private long userLikes;
    
    // Maybe get rid of this one
    
//    @OneToMany(mappedBy="user",fetch=FetchType.LAZY)
//    private List<Gift> gifts;
    
    // need to take the approach here to solve the many to many problem
    
    // http://giannigar.wordpress.com/2009/09/04/mapping-a-many-to-many-join-table-with-extra-column-using-jpa/
    
    @ManyToMany
    @JoinTable(name="user_likes", 
    joinColumns={@JoinColumn(name="user_id", referencedColumnName="user_id")},
    inverseJoinColumns={@JoinColumn(name="gift_id", referencedColumnName="gift_id")})
    private List<Gift> likes;
    
    @ManyToMany
    @JoinTable(name="user_flags", 
    joinColumns={@JoinColumn(name="user_id", referencedColumnName="user_id")},
    inverseJoinColumns={@JoinColumn(name="gift_id", referencedColumnName="gift_id")})
    private List<Gift> flagged;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(long userLikes) {
        this.userLikes = userLikes;
    }

    public long getId() {
        return id;
    }
}
