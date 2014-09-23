package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Gift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public long created; // TODO use better time representation
    public long userID;
    public long giftChainID;

	public long getId() {
		return id;
	}

}
