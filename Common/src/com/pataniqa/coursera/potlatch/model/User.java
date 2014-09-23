package com.pataniqa.coursera.potlatch.model;

public class User {
    
    public long userID;
    public String username;

    public User(long userID) {
        super();
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User [userID=" + userID + ", username=" + username + "]";
    }

}
