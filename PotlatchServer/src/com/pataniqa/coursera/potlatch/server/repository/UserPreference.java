package com.pataniqa.coursera.potlatch.server.repository;



public class UserPreference {

	private long userId;
	private TouchUpdatePreference touchUpdatePreference;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public TouchUpdatePreference getTouchUpdatePreference() {
		return touchUpdatePreference;
	}

	public void setTouchUpdatePreference(
			TouchUpdatePreference touchUpdatePreference) {
		this.touchUpdatePreference = touchUpdatePreference;
	}

}
