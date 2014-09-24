package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface MetadataStore {

    void setLike(long giftID, long userID, boolean like) throws RemoteException;

    void setFlag(long giftID, long userID, boolean flag) throws RemoteException;

}
