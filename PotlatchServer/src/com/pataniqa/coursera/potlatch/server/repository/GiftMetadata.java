package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;

@Entity
public class GiftMetadata {

    public long giftID;
    public long userID;
    public boolean like;
    public boolean flag;

}
