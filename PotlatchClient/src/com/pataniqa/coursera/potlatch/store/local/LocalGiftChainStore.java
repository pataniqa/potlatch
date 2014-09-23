package com.pataniqa.coursera.potlatch.store.local;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.store.GiftChainStore;

public class LocalGiftChainStore extends BaseStore<GiftChain> implements GiftChainStore {
    
    public LocalGiftChainStore(DatabaseHelper helper) {
        creator = new GiftChainCreator();
        id = PotlatchSchema.Cols.ID;
        tableName = PotlatchSchema.GiftChain.TABLE_NAME;
        this.helper = helper;
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }
}
