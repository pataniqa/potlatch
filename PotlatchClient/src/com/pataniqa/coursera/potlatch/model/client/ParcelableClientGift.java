package com.pataniqa.coursera.potlatch.model.client;

import com.pataniqa.coursera.potlatch.model.ClientGift;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableClientGift extends ClientGift implements Parcelable {

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
        dest.writeLong(created);
        dest.writeLong(userID);
        dest.writeLong(giftChainID);
        dest.writeString(giftChainName);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeLong(likes);
        dest.writeByte((byte) (flagged ? 1 : 0));
        dest.writeLong(userLikes);
        dest.writeString(username);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<ParcelableClientGift> CREATOR = new Parcelable.Creator<ParcelableClientGift>() {
        public ParcelableClientGift createFromParcel(Parcel in) {
            return new ParcelableClientGift(in);
        }

        public ParcelableClientGift[] newArray(int size) {
            return new ParcelableClientGift[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private ParcelableClientGift(Parcel in) {
        super(in.readLong(), in.readString(), in.readString(), in.readString(), in.readString(), in
                .readLong(), in.readLong(), in.readByte() != 0, in.readByte() != 0, in.readLong(),
                in.readByte() != 0, in.readLong(), in.readString(), in.readLong(), in.readString());

    }
}