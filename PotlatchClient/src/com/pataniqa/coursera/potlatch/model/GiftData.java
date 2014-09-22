package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class GiftData implements Parcelable {

    public final long keyID;
    public String title;
    public String description;
    public String videoUri;
    public String imageUri;
    public Time created = new Time();
    public long userID;

    /**
     * Constructor WITH _id, this creates a new object for use when pulling
     * already existing object's information from the ContentProvider
     * 
     * @param keyID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     */
    public GiftData(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Time created,
            long userID) {
        this.keyID = keyID;
        this.title = title;
        this.description = description;
        this.videoUri = videoUri;
        this.imageUri = imageUri;
        this.created = created;
        this.userID = userID;
    }

    /**
     * Override of the toString() method, for testing/logging
     */
    @Override
    public String toString() {
        return " title: " + title + " description: " + description + " videoUri: " + videoUri
                + " imageUri: " + imageUri + " created: " + created.format2445() + " userID: "
                + userID;
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftData clone() {
        return new GiftData(-1, title, description, videoUri, imageUri, created, userID);
    }

    // Parcelable interface

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
        dest.writeLong(keyID);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(videoUri);
        dest.writeString(imageUri);
        dest.writeString(created.format2445());
        dest.writeLong(userID);
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
        keyID = in.readLong();
        title = in.readString();
        description = in.readString();
        videoUri = in.readString();
        imageUri = in.readString();
        created.parse(in.readString());
        userID = in.readLong();
    }

}