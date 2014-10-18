package com.pataniqa.coursera.potlatch.store.local;

interface LocalGiftMetadata {

    Boolean setLike(long giftID, boolean like);

    Boolean setFlag(long giftID, boolean flag);

}
