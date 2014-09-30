package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Gift implements HasID {

    @Getter @Setter private long id;
    @Getter private String title;
    @Getter private String description;
    @Getter private String videoUri;
    @Getter private String imageUri;
    @Getter private Date created;
    @Getter private long userID;
    @Getter private long giftChainID;

    public Gift() {
        // zero args constructor
    }
    
    public Gift(long id,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Date created,
            long userID,
            long giftChainID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
        this.giftChainID = giftChainID;
    }

}
