package com.pataniqa.coursera.potlatch.model;

public interface GetId {
    
    /**
     * Indicates the database has not yet assigned an ID to this record.
     */
    static final long UNDEFINED_ID = -1;
    
    /**
     * @return Get the primary key of the record.
     */
    long getId();
    
}
