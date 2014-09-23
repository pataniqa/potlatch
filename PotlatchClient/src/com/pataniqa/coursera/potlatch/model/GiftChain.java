package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftChain implements Parcelable, HasID {

    public long giftChainID;
    public String giftChainName;

    public GiftChain(long giftChainID, String giftChainName) {
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }

    @Override
    public String toString() {
        return "GiftChain [keyID=" + giftChainID + ", giftChainName=" + giftChainName + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public GiftChain clone() {
        return new GiftChain(-1, giftChainName);
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
        dest.writeString(giftChainName);
        dest.writeLong(giftChainID);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<GiftChain> CREATOR = new Parcelable.Creator<GiftChain>() {
        public GiftChain createFromParcel(Parcel in) {
            return new GiftChain(in);
        }

        public GiftChain[] newArray(int size) {
            return new GiftChain[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private GiftChain(Parcel in) {
        giftChainName = in.readString();
        giftChainID = in.readLong();
    }

    @Override
    public long getID() {
        return giftChainID;
    }

}
