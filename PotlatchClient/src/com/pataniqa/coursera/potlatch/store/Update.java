package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface Update<T> {
    /**
     * Update a <T> object with a specific rowID.
     * 
     * @param data
     * @throws RemoteException
     */
    void update(T data) throws RemoteException;
}
