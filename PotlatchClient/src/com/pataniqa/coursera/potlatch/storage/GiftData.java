package com.pataniqa.coursera.potlatch.storage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom ORM container class, for Story Data.
 * <p>
 * This class is meant as a helper class for those working with the
 * ContentProvider and SQLiteDatabase. The use of this class is completely optional.
 * <p>
 * ORM = Object Relational Mapping
 * http://en.wikipedia.org/wiki/Object-relational_mapping
 * <p>
 * This class is a simple one-off POJO class with some simple ORM additions that
 * allow for conversion between the incompatible types of the POJO java classes,
 * the 'ContentValues', and the 'Cursor' classes from the use with
 * ContentProviders or SQLiteDatabases.
 * 
 */
public class GiftData implements Parcelable {

	public final long KEY_ID;
	public String key;
	public String href;
	public long loginId;
	public long giftId;
	public String title;
	public String body;
	public String audioLink;
	public String videoLink;
	public String imageName;
	public String imageLink;
	public String tags;
	public long creationTime;
	public long giftTime;
	public double latitude;
	public double longitude;

	/**
	 * Constructor WITHOUT _id, this creates a new object for insertion into the
	 * ContentProvider
	 * 
	 * @param loginId
	 * @param giftId
	 * @param title
	 * @param body
	 * @param audioLink
	 * @param videoLink
	 * @param imageName
	 * @param imageLink
	 * @param tags
	 * @param creationTime
	 * @param giftTime
	 * @param latitude
	 * @param longitude
	 */
	public GiftData(long loginId, long giftId, String title, String body,
			String audioLink, String videoLink, String imageName,
			String imageMetaData, String tags, long creationTime,
			long giftTime, double latitude, double longitude) {
		this.KEY_ID = -1;
		this.loginId = loginId;
		this.giftId = giftId;
		this.title = title;
		this.body = body;
		this.audioLink = audioLink;
		this.videoLink = videoLink;
		this.imageName = imageName;
		this.imageLink = imageMetaData;
		this.tags = tags;
		this.creationTime = creationTime;
		this.giftTime = giftTime;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Constructor WITH _id, this creates a new object for use when pulling
	 * already existing object's information from the ContentProvider
	 * 
	 * @param KEY_ID
	 * @param loginId
	 * @param giftId
	 * @param title
	 * @param body
	 * @param audioLink
	 * @param videoLink
	 * @param imageName
	 * @param imageLink
	 * @param tags
	 * @param creationTime
	 * @param giftTime
	 * @param latitude
	 * @param longitude
	 */
	public GiftData(long KEY_ID, long loginId, long giftId, String title,
			String body, String audioLink, String videoLink, String imageName,
			String imageLink, String tags, long creationTime, long giftTime,
			double latitude, double longitude) {
		this.KEY_ID = KEY_ID;
		this.loginId = loginId;
		this.giftId = giftId;
		this.title = title;
		this.body = body;
		this.audioLink = audioLink;
		this.videoLink = videoLink;
		this.imageName = imageName;
		this.imageLink = imageLink;
		this.tags = tags;
		this.creationTime = creationTime;
		this.giftTime = giftTime;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Override of the toString() method, for testing/logging
	 */
	@Override
	public String toString() {
		return " loginId: " + loginId + " giftId: " + giftId + " title: "
				+ title + " body: " + body + " audioLink: " + audioLink
				+ " videoLink: " + videoLink + " imageName: " + imageName
				+ " imageLink: " + imageLink + " tags: " + tags
				+ " creationTime: " + creationTime + " giftTime: " + giftTime
				+ " latitude: " + latitude + " longitude: " + longitude
				+ " href: " + href + " key: " + key;
	}

	/**
	 * Helper Method that allows easy conversion of object's data into an
	 * appropriate ContentValues
	 * 
	 * @return contentValues A new ContentValues object
	 */
	public ContentValues getCV() {
		return GiftCreator.getCVfromStory(this);
	}

	/**
	 * Clone this object into a new StoryData
	 */
	public GiftData clone() {
		return new GiftData(loginId, giftId, title, body, audioLink,
				videoLink, imageName, imageLink, tags, creationTime, giftTime,
				latitude, longitude);
	}

	// these are for parcelable interface
	@Override
	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public int describeContents() {
		return 0;
	}

	@Override
	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(KEY_ID);
		dest.writeLong(loginId);
		dest.writeLong(giftId);
		dest.writeString(title);
		dest.writeString(body);
		dest.writeString(audioLink);
		dest.writeString(videoLink);
		dest.writeString(imageName);
		dest.writeString(imageLink);
		dest.writeString(tags);
		dest.writeLong(creationTime);
		dest.writeLong(giftTime);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	public static final Parcelable.Creator<GiftData> CREATOR = new Parcelable.Creator<GiftData>() {
		public GiftData createFromParcel(Parcel in) {
			return new GiftData(in);
		}

		public GiftData[] newArray(int size) {
			return new GiftData[size];
		}
	};

	/**
	 * Used for writing a copy of this object to a Parcel, do not manually call.
	 */
	private GiftData(Parcel in) {
		KEY_ID = in.readLong();
		loginId = in.readLong();
		giftId = in.readLong();
		title = in.readString();
		body = in.readString();
		audioLink = in.readString();
		videoLink = in.readString();
		imageName = in.readString();
		imageLink = in.readString();
		tags = in.readString();
		creationTime = in.readLong();
		giftTime = in.readLong();
		latitude = in.readDouble();
		longitude = in.readDouble();
	}

	/**
	 * Creates a StoryData object from a JSONObject
	 * 
	 * @param jsonObject
	 * @throws JSONException
	 */
	public static GiftData createObjectFromJSON(JSONObject jsonObject)
			throws JSONException {

		boolean hasKeyID = false;
		String key = null;
		if (jsonObject.has("key")) {
			key = (String) jsonObject.get("key");
			hasKeyID = true;
		}

		String href = null;
		if (!jsonObject.isNull("href")) {
			href = (String) jsonObject.get("href");
		}

		long loginId = jsonObject.getLong("loginId");
		long storyId = jsonObject.getLong("storyId");
		String title = (String) jsonObject.get("title");
		JSONObject bodyJson = (JSONObject) jsonObject.get("body");
		String body = (String) bodyJson.get("value");
		String audioLink = (String) jsonObject.get("audioLink");
		String videoLink = (String) jsonObject.get("videoLink");
		String imageName = (String) jsonObject.get("imageName");
		String imageMetaData = (String) jsonObject.get("imageLink");
		String tags = (String) jsonObject.get("tags");
		long creationTime = jsonObject.getLong("creationTime");
		long storyTime = jsonObject.getLong("storyTime");
		double latitude = jsonObject.getDouble("latitude");
		double longitude = jsonObject.getDouble("longitude");

		GiftData rValue = null;
		if (hasKeyID == true) {
			rValue = new GiftData(key, href, loginId, storyId, title, body,
					audioLink, videoLink, imageName, imageMetaData, tags,
					creationTime, storyTime, latitude, longitude);
		} else {
			rValue = new GiftData(loginId, storyId, title, body, audioLink,
					videoLink, imageName, imageMetaData, tags, creationTime,
					storyTime, latitude, longitude);
		}
		return rValue;
	}

	public UrlEncodedFormEntity getUrlEncodedFormEntity()
			throws UnsupportedEncodingException {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("loginId", "" + loginId));
		params.add(new BasicNameValuePair("storyId", "" + giftId));
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("body", body));
		params.add(new BasicNameValuePair("audioLink", audioLink));
		params.add(new BasicNameValuePair("videoLink", videoLink));
		params.add(new BasicNameValuePair("imageName", imageName));
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("creationTime", "" + creationTime));
		params.add(new BasicNameValuePair("storyTime", "" + giftTime));
		params.add(new BasicNameValuePair("latitude", "" + latitude));
		params.add(new BasicNameValuePair("longitude", "" + longitude));
		return new UrlEncodedFormEntity(params);
	}

	/**
	 * Constructor WITHOUT _id, this creates a new object for insertion into the
	 * ContentProvider
	 * 
	 * @param loginId
	 * @param storyId
	 * @param title
	 * @param body
	 * @param audioLink
	 * @param videoLink
	 * @param imageName
	 * @param imageLink
	 * @param tags
	 * @param creationTime
	 * @param storyTime
	 * @param latitude
	 * @param longitude
	 */
	public GiftData(String key, String href, long loginId, long storyId,
			String title, String body, String audioLink, String videoLink,
			String imageName, String imageMetaData, String tags,
			long creationTime, long storyTime, double latitude, double longitude) {
		this.href = href;
		this.key = key;
		this.KEY_ID = -1;
		this.loginId = loginId;
		this.giftId = storyId;
		this.title = title;
		this.body = body;
		this.audioLink = audioLink;
		this.videoLink = videoLink;
		this.imageName = imageName;
		this.imageLink = imageMetaData;
		this.tags = tags;
		this.creationTime = creationTime;
		this.giftTime = storyTime;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}