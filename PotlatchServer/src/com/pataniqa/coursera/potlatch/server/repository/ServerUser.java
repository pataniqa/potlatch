package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    private String username;
    private long userLikes;

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

    @Override
    public String toString() {
        return "ServerUser [id=" + id + ", username=" + username + ", userLikes=" + userLikes + "]";
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
        ServerUser other = (ServerUser) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
