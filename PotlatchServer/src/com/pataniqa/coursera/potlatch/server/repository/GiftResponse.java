package com.pataniqa.coursera.potlatch.server.repository;

abstract class GiftResponse {

	private long giftId;
	private String username;

	public long getGiftId() {
		return giftId;
	}

	public void setGiftId(long giftId) {
		this.giftId = giftId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
