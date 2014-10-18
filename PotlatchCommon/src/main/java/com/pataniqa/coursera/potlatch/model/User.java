package com.pataniqa.coursera.potlatch.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A User.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class User implements HasId {

    /**
     * The unique ID.
     */
    @Setter private long id = HasId.UNDEFINED_ID;
    
    /**
     * The name of the user.
     */
    private String name;
    
    public User(String name) {
        this.name = name;
    }

}
