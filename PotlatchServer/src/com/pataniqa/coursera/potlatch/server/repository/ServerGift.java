package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServerGift extends com.pataniqa.coursera.potlatch.shared.Gift {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private long userId;

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
