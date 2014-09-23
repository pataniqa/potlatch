package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.RemoteException;
import android.text.format.Time;

import com.pataniqa.coursera.potlatch.model.ClientGift;
import com.pataniqa.coursera.potlatch.store.GiftQuery;

public class LocalGiftQuery extends BaseQuery<ClientGift> implements GiftQuery {

    public LocalGiftQuery(LocalDatabase helper) {
        creator = new ClientGiftCreator();
        tableName = LocalSchema.Gift.TABLE_NAME;
        this.helper = helper;
    }

    String sortOrder(ResultOrder resultOrder, ResultOrderDirection resultOrderDirection) {
        String sortCol = resultOrder == ResultOrder.LIKES ? LocalSchema.Cols.LIKES
                : LocalSchema.Cols.CREATED;
        String order = resultOrderDirection == ResultOrderDirection.ASCENDING ? "ASC" : "DESC";
        return sortCol + " " + order;
    }

    @Override
    public ArrayList<ClientGift> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {

        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        if (title == null || title.isEmpty())
            return query(null, null, null, sortOrder);
        else {
            String filterWord = "%" + title + "%";
            return query(null,
                    LocalSchema.Cols.TITLE + " LIKE ? ",
                    new String[] { filterWord },
                    sortOrder);
        }
    }

    @Override
    public ArrayList<ClientGift> queryByUser(long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        String[] selectionArgs = { String.valueOf(userID) };
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        return query(null, LocalSchema.Cols.USER_ID + "= ?", selectionArgs, sortOrder);
    }

    @Override
    public ArrayList<ClientGift> queryByTopGiftGivers(ResultOrderDirection resultOrderDirection)
            throws RemoteException {
        String order = resultOrderDirection == ResultOrderDirection.ASCENDING ? "ASC" : "DESC";
        String sortOrder  = LocalSchema.Cols.USER_LIKES + " " + order;
        return query(null, null, null, sortOrder);
    }

    @Override
    public ArrayList<ClientGift> queryByGiftChain(long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) throws RemoteException {
        String[] selectionArgs = { String.valueOf(giftChainID) };
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        return query(null, LocalSchema.Cols.GIFT_CHAIN_ID + "= ?", selectionArgs, sortOrder);
    }

}

/**
 * GiftCreator is a helper class that does convenience functions for converting
 * between the Custom ORM objects (such as GiftData), ContentValues, and
 * Cursors.
 */
class ClientGiftCreator extends BaseCreator<ClientGift> implements Creator<ClientGift> {

    @Override
    public ContentValues getCV(ClientGift data) {
        ContentValues rValue = new ContentValues();
        rValue.put(LocalSchema.Cols.TITLE, data.title);
        rValue.put(LocalSchema.Cols.DESCRIPTION, data.description);
        rValue.put(LocalSchema.Cols.VIDEO_URI, data.videoUri);
        rValue.put(LocalSchema.Cols.IMAGE_URI, data.imageUri);
        rValue.put(LocalSchema.Cols.CREATED, data.created.format2445());
        rValue.put(LocalSchema.Cols.USER_ID, data.userID);
        rValue.put(LocalSchema.Cols.LIKE, data.like);
        rValue.put(LocalSchema.Cols.FLAG, data.flag);
        rValue.put(LocalSchema.Cols.LIKES, data.likes);
        rValue.put(LocalSchema.Cols.FLAGGED, data.flagged);
        rValue.put(LocalSchema.Cols.GIFT_CHAIN_ID, data.giftChainID);
        rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, data.giftChainName);
        rValue.put(LocalSchema.Cols.USER_LIKES, data.userLikes);
        rValue.put(LocalSchema.Cols.USER_NAME, data.username);
        return rValue;
    }

    @Override
    public ClientGift getFromCursor(Cursor cursor) {
        long rowID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
        String title = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.TITLE));
        String description = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.DESCRIPTION));
        String videoUri = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.VIDEO_URI));
        String imageUri = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.IMAGE_URI));
        Time created = new Time();
        created.parse(cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.CREATED)));
        long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_ID));
        boolean like = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.LIKE)) > 0;
        boolean flag = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.FLAG)) > 0;
        long likes = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.LIKES));
        boolean flagged = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.FLAGGED)) > 0;
        long giftChainID = cursor.getLong(cursor
                .getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_ID));
        String giftChainName = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_NAME));
        long userLikes = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_LIKES));
        String username = cursor.getString(cursor
                .getColumnIndex(LocalSchema.Cols.USER_NAME));

        return new ClientGift(rowID, title, description, videoUri, imageUri, created, userID, like,
                flag, likes, flagged, giftChainID, giftChainName, userLikes, username);
    }
}
