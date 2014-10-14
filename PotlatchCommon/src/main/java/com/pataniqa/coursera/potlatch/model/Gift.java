package com.pataniqa.coursera.potlatch.model;

import java.util.Date;

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
public class Gift implements SetId {

    @Getter @Setter private long id = GetId.UNDEFINED_ID;
    @Getter private String title;
    @Getter private String description;
    @Getter private String videoUri;
    @Getter private String imageUri;
    @Getter private Date created;
    @Getter private long userID;
    @Getter private long giftChainID;
    
}
