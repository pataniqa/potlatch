package com.pataniqa.coursera.potlatch.store.local;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.client.GiftResult;
import com.pataniqa.coursera.potlatch.store.MetadataStore;

public class LocalGiftMetadataStore implements MetadataStore {

    String tableName;
    SQLiteOpenHelper helper;
    LocalGiftQuery localGiftStore;

    public LocalGiftMetadataStore(LocalDatabase helper) {
        tableName = LocalSchema.Gift.TABLE_NAME;
        this.helper = helper;
        localGiftStore = new LocalGiftQuery(helper);
    }

    void update(GiftResult gift) {
        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator.getCV(gift), selection, selectionArgs);
        db.close();
    }

    @Override
    public void setLike(long giftID, long userID, boolean like) throws RemoteException {
        long rowID = giftID;
        GiftResult gift = localGiftStore.findOne(rowID);
        gift.setLike(like);

        // we make the simplifying assumption that the local database will only
        // ever have one user
        // so we will only every have one like

        gift.setLikes(gift.isLike() ? 1 : 0);
        update(gift);
    }

    @Override
    public void setFlag(long giftID, long userID, boolean flag) throws RemoteException {
        long rowID = giftID;
        GiftResult gift = localGiftStore.findOne(rowID);
        gift.setFlag(flag);
        gift.setFlagged(flag);
        update(gift);
    }

}