package com.pataniqa.coursera.potlatch.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(exclude = { "name", "likes" })
@ToString
@Entity
public class ServerUser {

    public static final String ID = "user_id";
    @Getter @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Getter @Setter private String name;

    public static final String USER_LIKES = "user_likes";
    @Getter @Column(name = USER_LIKES) private long likes;

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        this.likes -= 1;
    }
    
    // TODO where and how is this created?
}
