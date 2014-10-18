package com.pataniqa.coursera.potlatch.store.local;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.model.GiftResult;

class LocalGiftMetadataStore implements LocalGiftMetadata {

    private final String tableName = LocalSchema.Gift.TABLE_NAME;
    private final SQLiteOpenHelper helper;
    private final LocalGiftQuery localGiftStore;

    LocalGiftMetadataStore(LocalDatabase helper, LocalGiftQuery localGiftStore) {
        this.helper = helper;
        this.localGiftStore = localGiftStore;
    }

    @Override
    public Boolean setLike(final long giftID, final boolean like) {
        GiftResult gift = localGiftStore.findOne(giftID);
        gift.setLike(like);
        gift.setLikes(gift.isLike() ? 1 : 0);
        update(gift);
        return true;
    }

    @Override
    public Boolean setFlag(final long giftID, final boolean flag) {
        GiftResult gift = localGiftStore.findOne(giftID);
        gift.setFlag(flag);
        gift.setFlagged(flag);
        update(gift);
        return true;
    }

    private void update(GiftResult gift) {
        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getId()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator().getCV(gift), selection, selectionArgs);
        db.close();
    }

}