package com.pataniqa.coursera.potlatch.model;

public interface HasID {
    
    static final long UNDEFINED_ID = -1;
    
    long getId();
    
    void setId(long id);

}
