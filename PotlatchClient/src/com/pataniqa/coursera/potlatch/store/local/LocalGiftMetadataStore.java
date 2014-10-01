package com.pataniqa.coursera.potlatch.store.local;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;

public class LocalGiftMetadataStore implements GiftMetadata {

    private final String tableName = LocalSchema.Gift.TABLE_NAME;
    private final SQLiteOpenHelper helper;
    private final LocalGiftQuery localGiftStore;

    public LocalGiftMetadataStore(LocalDatabase helper) {
        this.helper = helper;
        localGiftStore = new LocalGiftQuery(helper);
    }

    @Override
    public void setLike(long giftID, boolean like) throws RemoteException {
        GiftResult gift = localGiftStore.findOne(giftID);
        gift.setLike(like);
        gift.setLikes(gift.isLike() ? 1 : 0);
        update(gift);
    }

    @Override
    public void setFlag(long giftID, boolean flag) throws RemoteException {
        GiftResult gift = localGiftStore.findOne(giftID);
        gift.setFlag(flag);
        gift.setFlagged(flag);
        update(gift);
    }
    
    private void update(GiftResult gift) {
        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getId()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator().getCV(gift), selection, selectionArgs);
        db.close();
    }

}