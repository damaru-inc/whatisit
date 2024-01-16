package com.damaru.whereisit.model;

public interface Saveable {

    Long getId();
    
    public default boolean isPersisted() {
        return getId() != null;
    }
    

}
