package com.pataniqa.coursera.potlatch.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A gift chain.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class GiftChain implements HasId {

    /**
     * The unique ID.
     */
    @Setter private long id = HasId.UNDEFINED_ID;
    
    /**
     * The name of the gift chain. 
     */
    private String name;

    public GiftChain(String name) {
        this.name = name;
    }
}
