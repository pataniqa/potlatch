package com.pataniqa.coursera.potlatch.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class User implements HasID {

    @Getter @Setter private long id;
    @Getter private String username;

    public User() {
        // zero args constructor
    }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
}
