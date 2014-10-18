package com.pataniqa.coursera.potlatch.model;

/**
 * Classes that have a unique ID.
 */
public interface HasId {
    
    /**
     * Indicates that a unique ID has not yet been assigned to this class.
     */
    static final long UNDEFINED_ID = -1;
    
    /**
     * @return Get the primary key of the record.
     */
    long getId();
    
    /**
     * Set the primary key of the record.
     * 
     * @param id
     */
    void setId(long id);
    
}
