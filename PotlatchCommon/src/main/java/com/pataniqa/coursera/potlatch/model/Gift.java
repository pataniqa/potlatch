package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A gift.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Gift implements HasId {

    /**
     * The unique ID.
     */
    @Setter private long id = HasId.UNDEFINED_ID;
    
    /**
     * The gift title. 
     */
    private String title;
    
    /**
     * The gift description. 
     */
    private String description;
    
    /**
     * The URL of the image associated with the gift. 
     */
    private String imageUri;
    
    /**
     * The URL of the video associated with the gift, if any.
     */
    private String videoUri;
    

    /**
     * The time stamp when the gift was created.
     */
    private Date created;
    
    /**
     * The user ID of the gift creator.
     */
    private long userID;
    
    
    /**
     * The ID of the associated gift chain.
     */
    private long giftChainID;
    
}
