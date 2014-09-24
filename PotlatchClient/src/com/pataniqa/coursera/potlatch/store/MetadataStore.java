package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface MetadataStore {

    void like(long giftID, long userID) throws RemoteException;

    void unlike(long giftID, long userID) throws RemoteException;

    void flag(long giftID, long userID) throws RemoteException;

    void unflag(long giftID, long userID) throws RemoteException;

}
