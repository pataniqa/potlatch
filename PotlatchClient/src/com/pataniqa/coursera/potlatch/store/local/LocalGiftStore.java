package com.pataniqa.coursera.potlatch.store.local;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.store.Store;

/**
 * Stores GiftData in a local SQLite Database that is hosted by the device.
 * 
 */
public class LocalGiftStore extends BaseStore<Gift> implements Store<Gift> {

    public LocalGiftStore(DatabaseHelper helper) {
        creator = new GiftCreator();
        id = PotlatchSchema.Cols.ID;
        tableName = PotlatchSchema.Gift.TABLE_NAME;
        this.helper = helper;
    }

    @Override
    public void finalize() {
        if (helper != null)
            helper.close();
    }

}