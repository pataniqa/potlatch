package com.pataniqa.coursera.potlatch.store.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.MetadataStore;

public class LocalGiftMetadataStore extends BaseQuery<GiftMetadata> implements MetadataStore {
    
    LocalGiftQuery localGiftStore;

    public LocalGiftMetadataStore(LocalDatabase helper) {
        creator = new GiftMetadataCreator();
        tableName = LocalSchema.GiftChain.TABLE_NAME;
        this.helper = helper;
        localGiftStore = new LocalGiftQuery(helper);
    }

    @Override
    public void update(GiftMetadata data) throws RemoteException {
        long rowID = data.giftID;
        ClientGift gift = localGiftStore.get(rowID);
        
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(gift.getID()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator.getCV(gift), selection, selectionArgs);
        db.close();
    }
}

class GiftMetadataCreator extends BaseCreator<GiftMetadata> implements Creator<GiftMetadata> {

    @Override
    public ContentValues getCV(GiftMetadata data) {
        ContentValues rValue = new ContentValues();
        rValue.put(LocalSchema.Cols.GIFT_ID, data.giftID);
        rValue.put(LocalSchema.Cols.USER_ID, data.userID);
        rValue.put(LocalSchema.Cols.LIKE, data.like);
        rValue.put(LocalSchema.Cols.FLAG, data.flag);
        return rValue;
    }

    @Override
    public GiftMetadata getFromCursor(Cursor cursor) {
        long giftID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.GIFT_ID));
        long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_ID));
        boolean like = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.LIKE)) > 0;
        boolean flag = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.FLAG)) > 0;
        return new GiftMetadata(giftID, userID, like, flag);
    }
}