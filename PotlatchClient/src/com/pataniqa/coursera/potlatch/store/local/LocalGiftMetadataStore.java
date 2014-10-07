package com.pataniqa.coursera.potlatch.store.local;

import rx.Observable;
import rx.functions.Func1;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public Observable<Boolean> setLike(final long giftID, final boolean like) {
        Observable<GiftResult> gift = localGiftStore.findOne(giftID);
        return gift.map(new Func1<GiftResult, Boolean>() {
            @Override
            public Boolean call(GiftResult gift) {
                gift.setLike(like);
                gift.setLikes(gift.isLike() ? 1 : 0);
                update(gift);
                return true;
            }
        });
    }

    @Override
    public Observable<Boolean> setFlag(final long giftID, final boolean flag) {
        Observable<GiftResult> gift = localGiftStore.findOne(giftID);
        return gift.map(new Func1<GiftResult, Boolean>() {
            @Override
            public Boolean call(GiftResult gift) {
                gift.setFlag(flag);
                gift.setFlagged(flag);
                update(gift);
                return true;
            }});
    }

    private void update(GiftResult gift) {
        String selection = LocalSchema.Cols.ID + " = ? ";
        String[] selectionArgs = { String.valueOf(gift.getId()) };
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, localGiftStore.creator().getCV(gift), selection, selectionArgs);
        db.close();
    }

}