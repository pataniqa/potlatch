package com.pataniqa.coursera.potlatch.model.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.TimeUtils;

public class ParcelableClientGift extends GiftResult implements Parcelable {

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
        dest.writeLong(getId());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeString(getVideoUri());
        dest.writeString(getImageUri());
        dest.writeLong(TimeUtils.toLong(getCreated()));
        dest.writeLong(getUserID());
        dest.writeByte((byte) (isLike() ? 1 : 0));
        dest.writeByte((byte) (isFlag() ? 1 : 0));
        dest.writeLong(getLikes());
        dest.writeByte((byte) (isFlagged() ? 1 : 0));
        dest.writeLong(getGiftChainID());
        dest.writeString(getGiftChainName());
        dest.writeLong(getUserLikes());
        dest.writeString(getUsername());
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
        super(in.readLong(), in.readString(), in.readString(), in.readString(), in.readString(),
                TimeUtils.toDate(in.readLong()), in.readLong(), in.readByte() != 0, in.readByte() != 0, in
                        .readLong(), in.readByte() != 0, in.readLong(), in.readString(), in.readLong(), in
                        .readString());

    }
}