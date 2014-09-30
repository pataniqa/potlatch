package com.pataniqa.coursera.potlatch.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class GiftChain implements HasID {

    @Getter @Setter private long id;
    @Getter private String name;

    public GiftChain() {
        // zero args constructor
    }

    public GiftChain(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftChain clone() {
        return new GiftChain(HasID.UNDEFINED_ID, name);
    }
}
