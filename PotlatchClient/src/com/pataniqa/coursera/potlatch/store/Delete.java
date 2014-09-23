package com.pataniqa.coursera.potlatch.store;

import android.os.RemoteException;

import com.pataniqa.coursera.potlatch.model.HasID;

public interface Delete <T extends HasID> {
    
    /**
     * Delete a <T> object.
     * 
     * @param id
     * @throws RemoteException
     */
    void delete(long id) throws RemoteException;
}
