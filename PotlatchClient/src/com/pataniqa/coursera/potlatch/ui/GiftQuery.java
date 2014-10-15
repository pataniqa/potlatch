package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import android.content.SharedPreferences;

import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.ui.GiftActivity.QueryType;

class GiftQuery {

    public final static String GIFT_CHAIN_ID_TAG = "gift_chain_ID";
    public final static String GIFT_CHAIN_NAME_TAG = "gift_chain_name";
    public final static String QUERY_USER_ID_TAG = "query_user_id";
    public final static String QUERY_USER_NAME_TAG = "query_user_name";
    public final static String QUERY_TYPE_TAG = "query_type";
    public final static String TITLE_QUERY_TAG = "title_query";
    public final static String RESULT_ORDER_TAG = "result_order_tag";
    public final static String RESULT_ORDER_DIRECTION_TAG = "result_order_direction";
    public final static String DEFAULT_QUERY = "";

    @Getter @Setter private String title = DEFAULT_QUERY;
    @Getter @Setter private QueryType queryType = QueryType.ALL;
    @Getter private ResultOrder resultOrder = ResultOrder.TIME;
    @Getter private ResultOrderDirection resultDirection = ResultOrderDirection.DESCENDING;
    private long giftChainID;
    private String giftChainName;
    private long userID;
    private String username;

    void setChainQuery(long giftChainID, String giftChainName) {
        this.queryType = QueryType.CHAIN;
        this.giftChainID = giftChainID;
        this.giftChainName = giftChainName;
    }

    void setUserQuery(long userID, String username) {
        this.queryType = QueryType.USER;
        this.userID = userID;
        this.username = username;
    }
    
    void rotateQueryType() {
        queryType = QueryType.values()[(queryType.ordinal() + 1) % 3];
    }

    void changeResultDirection() {
        resultDirection = resultDirection == ResultOrderDirection.DESCENDING ? ResultOrderDirection.ASCENDING
                : ResultOrderDirection.DESCENDING;
    }

    void changeResultOrder() {
        resultOrder = resultOrder == ResultOrder.LIKES ? ResultOrder.TIME : ResultOrder.LIKES;
    }

    String getDescription() {
        switch (queryType) {
        case USER:
            return "User: " + username;
        case TOP_GIFT_GIVERS:
            return "Top gift givers";
        case CHAIN:
            return "Gift chain: " + giftChainName;
        default:
            return "All gifts";
        }
    }

    Observable<ArrayList<GiftResult>> query(Gifts gifts, boolean hide) {
        switch (queryType) {
        case USER:
            return gifts.queryByUser(title, userID, resultOrder, resultDirection, hide);
        case TOP_GIFT_GIVERS:
            return gifts.queryByTopGiftGivers(title, resultDirection, hide);
        case CHAIN:
            return gifts.queryByGiftChain(title, giftChainID, resultOrder, resultDirection, hide);
        default:
            return gifts.queryByTitle(title, resultOrder, resultDirection, hide);
        }
    }

    static GiftQuery fromSharedPreferences(GiftQuery query, SharedPreferences prefs) {
        if (prefs.contains(TITLE_QUERY_TAG))
            query.setTitle(prefs.getString(TITLE_QUERY_TAG, query.getTitle()));
        if (prefs.contains(RESULT_ORDER_TAG))
            query.resultOrder = ResultOrder.values()[prefs.getInt(RESULT_ORDER_TAG, query
                    .resultOrder.ordinal())];
        if (prefs.contains(RESULT_ORDER_DIRECTION_TAG))
            query.resultDirection = ResultOrderDirection.values()[prefs
                    .getInt(RESULT_ORDER_DIRECTION_TAG, query.resultDirection.ordinal())];
        if (prefs.contains(QUERY_TYPE_TAG))
            query.setQueryType(QueryType.values()[prefs.getInt(QUERY_TYPE_TAG, query.getQueryType()
                    .ordinal())]);
        return query;
    }

    static void saveToSharedPreferences(GiftQuery query, SharedPreferences prefs) {
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(TITLE_QUERY_TAG, query.getTitle());
        ed.putInt(RESULT_ORDER_TAG, query.resultOrder.ordinal());
        ed.putInt(RESULT_ORDER_DIRECTION_TAG, query.resultDirection.ordinal());
        ed.putInt(QUERY_TYPE_TAG, query.getQueryType().ordinal());
        ed.commit();
    }

}
