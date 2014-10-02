package com.pataniqa.coursera.potlatch.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements SetId {

    @Getter @Setter private long id = GetId.UNDEFINED_ID;
    @Getter private String name;
    
    public User(String name) {
        this.name = name;
    }

}
