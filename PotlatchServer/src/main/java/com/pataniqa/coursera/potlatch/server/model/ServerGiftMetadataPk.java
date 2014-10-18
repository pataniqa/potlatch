package com.pataniqa.coursera.potlatch.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A primary key for the metadata table.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
@Setter
public class ServerGiftMetadataPk implements Serializable {

    private static final long serialVersionUID = -707960766740593473L;
    
    private ServerUser user;
    private ServerGift gift;    

    @ManyToOne
    public ServerUser getUser() {
        return user;
    }
    
    @ManyToOne
    public ServerGift getGift() {
        return gift;
    }
}
