package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
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

    void saveLike(long giftID, boolean like) throws RemoteException {
        long rowID = giftID;
        ClientGift gift = localGiftStore.findOne(rowID);
        gift.like = like;

        // we make the simplifying assumption that the local database will only
        // ever have one user

        // so we will only every have one like

        gift.likes = gift.like ? 1 : 0;

        // and we don't have a separate table for metadata, instead we update
        // the ClientGift table

        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator.getCV(gift), selection, selectionArgs);
        db.close();
    }
    
    void saveFlag(long giftID, boolean flag) throws RemoteException {
        long rowID = giftID;
        ClientGift gift = localGiftStore.findOne(rowID);
        gift.flag = flag;
        gift.flagged = flag;

        // and we don't have a separate table for metadata, instead we update
        // the ClientGift table

        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator.getCV(gift), selection, selectionArgs);
        db.close();
    }

    @Override
    public void like(long giftID, long userID) throws RemoteException {
        saveLike(giftID, true);
    }

    @Override
    public void unlike(long giftID, long userID) throws RemoteException {
        saveLike(giftID, false);
    }

    @Override
    public void flag(long giftID, long userID) throws RemoteException {
        saveFlag(giftID, true);
    }

    @Override
    public void unflag(long giftID, long userID) throws RemoteException {
        saveFlag(giftID, false);
    }
}