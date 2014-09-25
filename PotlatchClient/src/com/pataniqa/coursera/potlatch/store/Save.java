package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

public interface Save <T> {
    /**
     * Save the <T> object.
     * 
     * @param <T> object to be saved.
     * @return The object that was saved.
     * @throws RemoteException
     */
    <S extends T> S save(S data) throws RemoteException;
}
