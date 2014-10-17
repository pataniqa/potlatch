package com.pataniqa.coursera.potlatch.utils;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import android.content.SharedPreferences;

import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.ui.SettingsActivity;

public class GiftQuery {

    public final static String GIFT_CHAIN_ID_TAG = "gift_chain_ID";
    public final static String GIFT_CHAIN_NAME_TAG = "gift_chain_name";
    public final static String QUERY_USER_ID_TAG = "query_user_id";
    public final static String QUERY_USER_NAME_TAG = "query_user_name";
    public final static String QUERY_TYPE_TAG = "query_type";
    public final static String TITLE_QUERY_TAG = "title_query";
    public final static String RESULT_ORDER_TAG = "result_order_tag";
    public final static String RESULT_ORDER_DIRECTION_TAG = "result_order_direction";
    public final static String DEFAULT_QUERY = "";

    private final SharedPreferences prefs;
    @Getter @Setter private String title = DEFAULT_QUERY;
    @Getter @Setter private QueryType queryType = QueryType.ALL;
    @Getter private ResultOrder resultOrder = ResultOrder.TIME;
    @Getter private ResultOrderDirection resultDirection = ResultOrderDirection.DESCENDING;
    private long giftChainID;
    private String giftChainName;
    private long queryUserID;
    private String queryUsername;
    private long userID;
    private String username;

    public GiftQuery(long userID, String username, SharedPreferences prefs) {
        this.prefs = prefs;
        this.userID = userID;
        this.username = username;
        this.queryUserID = userID;
        this.queryUsername = username;
        if (prefs.contains(TITLE_QUERY_TAG))
            setTitle(prefs.getString(TITLE_QUERY_TAG, getTitle()));
        if (prefs.contains(RESULT_ORDER_TAG))
            resultOrder = ResultOrder.values()[prefs
                    .getInt(RESULT_ORDER_TAG, resultOrder.ordinal())];
        if (prefs.contains(RESULT_ORDER_DIRECTION_TAG))
            resultDirection = ResultOrderDirection.values()[prefs
                    .getInt(RESULT_ORDER_DIRECTION_TAG, resultDirection.ordinal())];
        if (prefs.contains(QUERY_TYPE_TAG))
            queryType = QueryType.values()[prefs.getInt(QUERY_TYPE_TAG, getQueryType().ordinal())];
        if (prefs.contains(GIFT_CHAIN_NAME_TAG))
            giftChainName = prefs.getString(GIFT_CHAIN_NAME_TAG, null);
        if (prefs.contains(GIFT_CHAIN_ID_TAG))
            giftChainID = prefs.getLong(GIFT_CHAIN_ID_TAG, 0);
        if (prefs.contains(QUERY_USER_NAME_TAG))
            queryUsername = prefs.getString(QUERY_USER_NAME_TAG, null);
        if (prefs.contains(QUERY_USER_ID_TAG))
            queryUserID = prefs.getLong(QUERY_USER_ID_TAG, 0);
        if ((queryType == QueryType.USER && queryUsername == null)
                || (queryType == QueryType.CHAIN && giftChainName == null))
            queryType = QueryType.ALL;

    }

    public void clearTitle() {
        title = DEFAULT_QUERY;
    }

    public void setChainQuery(long giftChainID, String giftChainName) {
        this.queryType = QueryType.CHAIN;
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }

    public void setUserQuery(long userID, String username) {
        this.queryType = QueryType.USER;
        this.queryUserID = userID;
        this.queryUsername = username;
    }

    public void rotateQueryType() {
        queryType = QueryType.values()[(queryType.ordinal() + 1) % 3];
        if (queryType == QueryType.USER) {
            queryUserID = userID;
            queryUsername = username;
        }

    }

    public void changeResultDirection() {
        resultDirection = resultDirection == ResultOrderDirection.DESCENDING ? ResultOrderDirection.ASCENDING
                : ResultOrderDirection.DESCENDING;
    }

    public void changeResultOrder() {
        resultOrder = resultOrder == ResultOrder.LIKES ? ResultOrder.TIME : ResultOrder.LIKES;
    }

    public String getDescription() {
        switch (queryType) {
        case USER:
            return "User: " + queryUsername;
        case TOP_GIFT_GIVERS:
            return "Top gift givers";
        case CHAIN:
            return "Gift chain: " + giftChainName;
        default:
            if (title.isEmpty())
                return "All gifts";
            else
                return "Title query: " + title;
        }
    }
    
    public Observable<ArrayList<GiftResult>> query(Gifts gifts) {
        boolean hide = prefs.getBoolean(SettingsActivity.HIDE_FLAGGED_CONTENT, true);
        switch (queryType) {
        case USER:
            return gifts.queryByUser(title, queryUserID, resultOrder, resultDirection, hide);
        case TOP_GIFT_GIVERS:
            return gifts.queryByTopGiftGivers(title, resultDirection, hide);
        case CHAIN:
            return gifts.queryByGiftChain(title, giftChainID, resultOrder, resultDirection, hide);
        default:
            return gifts.queryByTitle(title, resultOrder, resultDirection, hide);
        }
    }

    public void saveToSharedPreferences(SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(TITLE_QUERY_TAG, getTitle());
        ed.putInt(RESULT_ORDER_TAG, resultOrder.ordinal());
        ed.putInt(RESULT_ORDER_DIRECTION_TAG, resultDirection.ordinal());
        ed.putInt(QUERY_TYPE_TAG, getQueryType().ordinal());
        ed.putString(GIFT_CHAIN_NAME_TAG, giftChainName);
        ed.putLong(GIFT_CHAIN_ID_TAG, giftChainID);
        ed.putString(QUERY_USER_NAME_TAG, queryUsername);
        ed.putLong(QUERY_USER_ID_TAG, queryUserID);
        ed.commit();
    }

}
