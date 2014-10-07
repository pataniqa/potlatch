package com.pataniqa.coursera.potlatch.store;

import rx.Observable;

public interface GiftMetadata {

    Observable<Boolean> setLike(long giftID, boolean like);

    Observable<Boolean> setFlag(long giftID, boolean flag);

}
