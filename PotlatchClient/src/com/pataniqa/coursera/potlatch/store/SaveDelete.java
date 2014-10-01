package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.GetId;

public interface SaveDelete <T extends GetId> {
    
    /**
     * Delete a <T> object.
     * 
     * @param id
     * @throws RemoteException
     */
    void delete(long id) throws RemoteException;
    
    /**
     * Save the <T> object.
     * 
     * @param <T> object to be saved.
     * @return The object that was saved.
     * @throws RemoteException
     */
    <S extends T> S save(S data) throws RemoteException;
}
