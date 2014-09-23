package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private long totalTouches;
    private TouchUpdatePreference touchUpdatePreference;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTotalTouches() {
        return totalTouches;
    }

    public void setTotalTouches(long totalTouches) {
        this.totalTouches = totalTouches;
    }

    public long getId() {
        return id;
    }
}
