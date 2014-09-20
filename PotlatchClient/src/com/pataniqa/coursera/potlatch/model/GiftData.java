package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom ORM container class, for Gift Data.
 * <p>
 * This class is meant as a helper class for those working with the
 * ContentProvider and SQLiteDatabase. The use of this class is completely
 * optional.
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
    public long giftId;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;

    /**
     * Constructor WITH _id, this creates a new object for use when pulling
     * already existing object's information from the ContentProvider
     * 
     * @param KEY_ID
     * @param giftId
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     */
    public GiftData(long KEY_ID, long giftId, String title, String description,
            String videoUri, String imageUri) {
        this.KEY_ID = KEY_ID;
        this.giftId = giftId;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
    }

    /**
     * Constructor WITHOUT _id, this creates a new object for insertion into the
     * ContentProvider
     * 
     * @param giftId
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     */
    public GiftData(long giftId, String title, String description, String videoUri,
            String imageUri) {
        this(-1, giftId, title, description, videoUri, imageUri);
    }

    /**
     * Constructor WITHOUT _id, this creates a new object for insertion into the
     * ContentProvider
     * 
     * @param giftId
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     */
    public GiftData(String key, String href, long loginId, long giftId, String title, String description,
            String videoUri, String imageUri) {
        this(-1, giftId, title, description, videoUri, imageUri);
        this.href = href;
        this.key = key;
    }

    /**
     * Override of the toString() method, for testing/logging
     */
    @Override
    public String toString() {
        return " giftId: " + giftId + " title: " + title + " description: "
                + description + " videoUri: " + videoUri + " imageUri: " + imageUri + " href: " + href
                + " key: " + key;
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftData clone() {
        return new GiftData(giftId, title, description, videoUri, imageUri);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(KEY_ID);
        dest.writeLong(giftId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(videoUri);
        dest.writeString(imageUri);
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
        giftId = in.readLong();
        title = in.readString();
        description = in.readString();
        videoUri = in.readString();
        imageUri = in.readString();
    }

}