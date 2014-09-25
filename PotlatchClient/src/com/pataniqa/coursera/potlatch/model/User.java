package com.pataniqa.coursera.potlatch.model;


public class User implements HasID {

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

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
