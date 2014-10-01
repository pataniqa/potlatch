package com.pataniqa.coursera.potlatch.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class ServerGiftMetadataPk implements Serializable {

    private static final long serialVersionUID = -707960766740593473L;
    
    @Setter private ServerUser user;
    @Setter private ServerGift gift;    

    @ManyToOne
    public ServerUser getUser() {
        return user;
    }
    
    @ManyToOne
    public ServerGift getGift() {
        return gift;
    }
}
