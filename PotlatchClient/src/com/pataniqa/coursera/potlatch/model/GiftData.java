package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class GiftData extends Gift implements Parcelable, HasID {

    public final long keyID;

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
        super(title, description, videoUri, imageUri, created, userID);
        this.keyID = keyID;
    }

    @Override
    public String toString() {
        return "GiftData [keyID=" + keyID + ", title=" + title + ", description=" + description
                + ", videoUri=" + videoUri + ", imageUri=" + imageUri + ", created=" + created
                + ", userID=" + userID + "]";
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
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(videoUri);
        dest.writeString(imageUri);
        dest.writeString(created.format2445());
        dest.writeLong(userID);
        dest.writeLong(keyID);
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
        super(in.readString(), in.readString(), in.readString(), in.readString(), readTime(in
                .readString()), in.readLong());
        keyID = in.readLong();
    }

    private static Time readTime(String s) {
        Time time = new Time();
        time.parse(s);
        return time;
    }

    @Override
    public long getID() {
        return keyID;
    }

}