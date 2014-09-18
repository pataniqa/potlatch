package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InappropriateGift extends GiftResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	public long getId() {
		return id;
	}
}
