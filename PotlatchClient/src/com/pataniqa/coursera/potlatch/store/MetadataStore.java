package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface MetadataStore {

    void setLike(long giftID, boolean like) throws RemoteException;

    void setFlag(long giftID, boolean flag) throws RemoteException;

}
