package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrder;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrderDirection;

interface LocalGifts extends LocalQuery<GiftResult>, LocalSaveDelete<Gift> {

    ArrayList<GiftResult> queryByTitle(String title,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    ArrayList<GiftResult> queryByUser(String title,
            long userID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection, 
            boolean hideFlaggedContent);

    ArrayList<GiftResult> queryByTopGiftGivers(String title,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);

    ArrayList<GiftResult> queryByGiftChain(String title,
            long giftChainID,
            ResultOrder resultOrder,
            ResultOrderDirection resultOrderDirection,
            boolean hideFlaggedContent);
}
