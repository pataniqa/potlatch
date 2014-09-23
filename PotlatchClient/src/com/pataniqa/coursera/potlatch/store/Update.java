package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface Update<T> extends Query<T> {
    /**
     * Update a <T> object with a specific rowID.
     * 
     * @param data
     * @return number of rows altered
     * @throws RemoteException
     */
    int update(T data) throws RemoteException;
}
