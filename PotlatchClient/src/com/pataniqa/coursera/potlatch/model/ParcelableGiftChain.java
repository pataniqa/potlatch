package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableGiftChain extends GiftChain implements Parcelable {

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
        dest.writeString(giftChainName);
        dest.writeLong(giftChainID);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<ParcelableGiftChain> CREATOR = new Parcelable.Creator<ParcelableGiftChain>() {
        public ParcelableGiftChain createFromParcel(Parcel in) {
            return new ParcelableGiftChain(in);
        }

        public ParcelableGiftChain[] newArray(int size) {
            return new ParcelableGiftChain[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private ParcelableGiftChain(Parcel in) {
        super(in.readLong(), in.readString());
    }
}
