package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores information about gifts.
 * 
 */
public class GiftData implements Parcelable {

    public final long KEY_ID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;

    /**
     * Constructor WITH _id, this creates a new object for use when pulling
     * already existing object's information from the ContentProvider
     * 
     * @param KEY_ID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     */
    public GiftData(long KEY_ID, String title, String description, String videoUri, String imageUri) {
        this.KEY_ID = KEY_ID;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
    }

    /**
     * Constructor WITHOUT _id, this creates a new object for insertion into the
     * ContentProvider
     * 
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     */
    public GiftData(String title, String description, String videoUri, String imageUri) {
        this(-1, title, description, videoUri, imageUri);
    }

    /**
     * Override of the toString() method, for testing/logging
     */
    @Override
    public String toString() {
        return " title: " + title + " description: " + description + " videoUri: " + videoUri
                + " imageUri: " + imageUri;
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftData clone() {
        return new GiftData(title, description, videoUri, imageUri);
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
        title = in.readString();
        description = in.readString();
        videoUri = in.readString();
        imageUri = in.readString();
    }

}