package com.pataniqa.coursera.potlatch.server.repository;

import javax.persistence.Entity;

@Entity
public class GiftMetadata {

	private long giftId;
	private long touched;
	private boolean inappropriate;

	public long getTouched() {
		return touched;
	}

	public void setTouched(long touched) {
		this.touched = touched;
	}

	public boolean isInappropriate() {
		return inappropriate;
	}

	public void setInappropriate(boolean inappropriate) {
		this.inappropriate = inappropriate;
	}

	public long getGiftId() {
		return giftId;
	}

	public void setGiftId(long giftId) {
		this.giftId = giftId;
	}

}
