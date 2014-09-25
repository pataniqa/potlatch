package com.pataniqa.coursera.potlatch.model.client;

import com.pataniqa.coursera.potlatch.model.HasID;
import com.pataniqa.coursera.potlatch.model.IUser;

public class User implements HasID, IUser {

    private long userID;
    private String username;

    public User() {

    }

    public User(long userID) {
        super();
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User [userID=" + userID + ", username=" + username + "]";
    }

    @Override
    public long getID() {
        return userID;
    }

    @Override
    public void setID(long id) {
        userID = id;
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

}
