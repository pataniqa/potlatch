package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GiftChain {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long giftChainID;
	
    public String giftChainName;

}
