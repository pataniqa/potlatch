package com.pataniqa.coursera.potlatch.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

public class ClientGift extends Gift implements Parcelable, HasID {

    public boolean like;
    public boolean flag;
    public long likes;
    public boolean flagged;
    public long userLikes;

    /**
     * Constructor
     * 
     * @param keyID
     * @param title
     * @param description
     * @param videoUri
     * @param imageUri
     * @param created
     * @param userID
     * @param like
     * @param flag
     * @param likes
     * @param flagged
     * @param giftChainID
     * @param giftChainName
     * @param userLikes;
     */
    public ClientGift(long keyID,
            String title,
            String description,
            String videoUri,
            String imageUri,
            Time created,
            long userID,
            boolean like,
            boolean flag,
            long likes,
            boolean flagged,
            long giftChainID,
            String giftChainName,
            long userLikes) {
        super(keyID, title, description, videoUri, imageUri, created, userID, giftChainID, giftChainName);
        this.like = like;
        this.flag = flag;
        this.likes = likes;
        this.flagged = flagged;
        this.userLikes = userLikes;
    }
    
    @Override
    public String toString() {
        return "ClientGift [keyID=" + keyID + ", like=" + like + ", flag=" + flag + ", likes="
                + likes + ", flagged=" + flagged + ", giftChainID=" + giftChainID
                + ", giftChainName=" + giftChainName + ", userLikes=" + userLikes + "]";
    }

    /**
     * Clone this object into a new GiftData
     */
    public ClientGift clone() {
        return new ClientGift(-1, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainID, giftChainName, userLikes);
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
        dest.writeLong(giftChainID);
        dest.writeString(giftChainName);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeLong(likes);
        dest.writeByte((byte) (flagged ? 1 : 0));
        dest.writeLong(userLikes);
    }

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    public static final Parcelable.Creator<ClientGift> CREATOR = new Parcelable.Creator<ClientGift>() {
        public ClientGift createFromParcel(Parcel in) {
            return new ClientGift(in);
        }

        public ClientGift[] newArray(int size) {
            return new ClientGift[size];
        }
    };

    /**
     * Used for writing a copy of this object to a Parcel, do not manually call.
     */
    private ClientGift(Parcel in) {
        super(in.readLong(), in.readString(), in.readString(), in.readString(), in.readString(), readTime(in
                .readString()), in.readLong(), in.readLong(), in.readString());
        like = in.readByte() != 0;
        flag = in.readByte() != 0;
        likes = in.readLong();
        flagged = in.readByte() != 0;
        userLikes = in.readLong();
    }

    static Time readTime(String s) {
        Time time = new Time();
        time.parse(s);
        return time;
    }
}