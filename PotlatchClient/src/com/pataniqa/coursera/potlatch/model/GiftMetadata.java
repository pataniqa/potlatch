package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftMetadata implements Parcelable {

    public final long keyID;
    public final long giftID;
    public boolean like;
    public boolean flag;
    public long likes;
    public boolean flagged;

    /**
     * Constructor WITH _id, this creates a new object for use when pulling
     * already existing object's information from the ContentProvider
     * 
     * @param keyID
     */
    public GiftMetadata(long keyID,
            long giftID,
            boolean like,
            boolean flag,
            long likes,
            boolean flagged) {
        this.keyID = keyID;
        this.giftID = giftID;
        this.like = like;
        this.flag = flag;
        this.likes = likes;
        this.flagged = flagged;
    }

    @Override
    public String toString() {
        return "GiftMetadata [keyID=" + keyID + ", giftID=" + giftID + ", like=" + like + ", flag="
                + flag + ", likes=" + likes + ", flagged=" + flagged + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftMetadata clone() {
        return new GiftMetadata(-1, giftID, like, flag, likes, flagged);
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
        dest.writeLong(giftID);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeLong(likes);
        dest.writeByte((byte) (flagged ? 1 : 0));
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<GiftMetadata> CREATOR = new Parcelable.Creator<GiftMetadata>() {
        public GiftMetadata createFromParcel(Parcel in) {
            return new GiftMetadata(in);
        }

        public GiftMetadata[] newArray(int size) {
            return new GiftMetadata[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private GiftMetadata(Parcel in) {
        keyID = in.readLong();
        giftID = in.readLong();
        like = in.readByte() != 0;
        flag = in.readByte() != 0;
        likes = in.readLong();
        flagged = in.readByte() != 0;
    }

}
