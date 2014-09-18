package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GiftChainEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private long giftChainId;
	private long giftId;

	public long getGiftChainId() {
		return giftChainId;
	}

	public void setGiftChainId(long giftChainId) {
		this.giftChainId = giftChainId;
	}

	public long getGiftId() {
		return giftId;
	}

	public void setGiftId(long giftId) {
		this.giftId = giftId;
	}

	public long getId() {
		return id;
	}

}
