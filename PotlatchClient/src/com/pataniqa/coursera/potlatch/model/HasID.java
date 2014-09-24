package com.pataniqa.coursera.potlatch.model;

public interface HasID {
    
    public static final long UNDEFINED_ID = -1;
    
    long getID();
    
    void setID(long id);

}
