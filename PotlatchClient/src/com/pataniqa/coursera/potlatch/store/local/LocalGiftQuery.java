package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.TimeUtils;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrder;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrderDirection;

class LocalGiftQuery extends BaseQuery<GiftResult> implements LocalGifts {

    private static String LIKE_QUERY = LocalSchema.Cols.TITLE + " LIKE ? ";

    private final LocalGiftStore store;

    LocalGiftQuery(LocalDatabase helper, LocalGiftStore store) {
        super(new GiftResultCreator(), LocalSchema.Gift.TABLE_NAME, helper);
        this.store = store;
    }

    @Override
    public ArrayList<GiftResult> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hide) {
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        if (title == null || title.isEmpty())
            return hideFlaggedContent(query(null, null, null, sortOrder), hide);
        else {
            String[] selectionArgs = new String[] { toLike(title) };
            return hideFlaggedContent(query(null, LIKE_QUERY, selectionArgs, sortOrder), hide);
        }
    }

    @Override
    public ArrayList<GiftResult> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hide) {
        return hideFlaggedContent(query(LocalSchema.Cols.USER_ID,
                String.valueOf(userID),
                title,
                resultOrder,
                resultOrderDirection),
                hide);
    }

    @Override
    public ArrayList<GiftResult> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection,
            boolean hide) {
        // don't implement this locally as there is only one user
        // just fake it for now
        return queryByTitle(title, ResultOrder.LIKES, resultOrderDirection, hide);
    }

    @Override
    public ArrayList<GiftResult> queryByGiftChain(String title,
            long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hide) {
        return hideFlaggedContent(query(LocalSchema.Cols.GIFT_CHAIN_ID,
                String.valueOf(giftChainID),
                title,
                resultOrder,
                resultOrderDirection),
                hide);
    }

    @Override
    public <S extends Gift> S save(S data) {
        return store.save(data);
    }

    @Override
    public Boolean delete(long id) {
        return store.delete(id);
    }

    private ArrayList<GiftResult> query(String queryProperty,
            String queryValue,
            String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection) {
        String sortOrder = sortOrder(resultOrder, resultOrderDirection);
        if (title == null || title.isEmpty()) {
            String[] selectionArgs = { queryValue };
            return query(null, queryProperty + "= ?", selectionArgs, sortOrder);
        } else {
            String[] selectionArgs = { queryValue, toLike(title) };
            return query(null, queryProperty + "= ? AND " + LIKE_QUERY, selectionArgs, sortOrder);
        }
    }

    private static String sortOrder(ResultOrder resultOrder, ResultOrderDirection resultOrderDirection) {
        String sortCol;
        if (resultOrder == ResultOrder.LIKES)
            sortCol = LocalSchema.Cols.LIKES;
        else if (resultOrder == ResultOrder.TIME)
            sortCol = LocalSchema.Cols.CREATED;
        else
            sortCol = LocalSchema.Cols.USER_LIKES;
        String order = direction(resultOrderDirection);
        return sortCol + " " + order;
    }

    private static String direction(ResultOrderDirection resultOrderDirection) {
        return resultOrderDirection == ResultOrderDirection.ASCENDING ? "ASC" : "DESC";
    }

    private static String toLike(String s) {
        return "%" + s + "%";
    }

    public static ArrayList<GiftResult> hideFlaggedContent(ArrayList<GiftResult> results,
            final boolean hide) {
        if (!hide)
            return results;
        ArrayList<GiftResult> out = new ArrayList<GiftResult>();
        for (GiftResult gift : results) {
            if (!gift.isFlagged())
                out.add(gift);
        }
        return out;
    }

    private static class GiftResultCreator extends BaseCreator<GiftResult> implements
            Creator<GiftResult> {

        @Override
        public ContentValues getCV(GiftResult data) {
            ContentValues rValue = new ContentValues();
            rValue.put(LocalSchema.Cols.ID, data.getId());
            rValue.put(LocalSchema.Cols.TITLE, data.getTitle());
            rValue.put(LocalSchema.Cols.DESCRIPTION, data.getDescription());
            rValue.put(LocalSchema.Cols.VIDEO_URI, data.getVideoUri());
            rValue.put(LocalSchema.Cols.IMAGE_URI, data.getImageUri());
            rValue.put(LocalSchema.Cols.CREATED, TimeUtils.toLong(data.getCreated()));
            rValue.put(LocalSchema.Cols.USER_ID, data.getUserID());
            rValue.put(LocalSchema.Cols.LIKE, data.isLike());
            rValue.put(LocalSchema.Cols.FLAG, data.isFlag());
            rValue.put(LocalSchema.Cols.LIKES, data.getLikes());
            rValue.put(LocalSchema.Cols.FLAGGED, data.isFlagged());
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_ID, data.getGiftChainID());
            rValue.put(LocalSchema.Cols.GIFT_CHAIN_NAME, data.getGiftChainName());
            rValue.put(LocalSchema.Cols.USER_LIKES, data.getUserLikes());
            rValue.put(LocalSchema.Cols.USER_NAME, data.getUsername());
            return rValue;
        }

        @Override
        public GiftResult getFromCursor(Cursor cursor) {
            long rowID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.ID));
            String title = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.TITLE));
            String description = cursor.getString(cursor
                    .getColumnIndex(LocalSchema.Cols.DESCRIPTION));
            String videoUri = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.VIDEO_URI));
            String imageUri = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.IMAGE_URI));
            Date created = TimeUtils.toDate(cursor.getLong(cursor
                    .getColumnIndex(LocalSchema.Cols.CREATED)));
            long userID = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_ID));
            boolean like = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.LIKE)) > 0;
            boolean flag = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.FLAG)) > 0;
            long likes = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.LIKES));
            boolean flagged = cursor.getInt(cursor.getColumnIndex(LocalSchema.Cols.FLAGGED)) > 0;
            long giftChainID = cursor
                    .getLong(cursor.getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_ID));
            String giftChainName = cursor.getString(cursor
                    .getColumnIndex(LocalSchema.Cols.GIFT_CHAIN_NAME));
            long userLikes = cursor.getLong(cursor.getColumnIndex(LocalSchema.Cols.USER_LIKES));
            String username = cursor.getString(cursor.getColumnIndex(LocalSchema.Cols.USER_NAME));

            return new GiftResult(rowID,
                    title,
                    description,
                    videoUri,
                    imageUri,
                    created,
                    userID,
                    like,
                    flag,
                    likes,
                    flagged,
                    giftChainID,
                    giftChainName,
                    userLikes,
                    username);
        }
    }
}
