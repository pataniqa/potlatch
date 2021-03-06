package com.pataniqa.coursera.potlatch.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.pataniqa.coursera.potlatch.model.User;

/**
 * A user.
 */
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "name", "likes" })
@ToString
@Entity
@Getter
public class ServerUser {

    public static final String ID = "user_id";
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = ID) private long id;

    @Setter private String name;

    public static final String USER_LIKES = "user_likes";
    @Column(name = USER_LIKES) private long likes = 0;

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        this.likes -= 1;
    }
    
    public ServerUser(String name) {
        this.name = name;
    }

    public ServerUser(User user) {
        this(user.getName());
    }

    public ServerUser update(User user) {
        this.name = user.getName();
        return this;
    }
}
