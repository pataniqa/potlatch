package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.*;


@Entity
@IdClass(GiftMetadataId.class)
public class GiftMetadata {
    @Id
    private long giftID;
    @Id
    private long userID;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="gift_id", referencedColumnName="gift_id")
    public ServerGift gift;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="user_id", referencedColumnName="user_id")
    public ServerUser user;
    
    public boolean like;
    public boolean flag;

}
