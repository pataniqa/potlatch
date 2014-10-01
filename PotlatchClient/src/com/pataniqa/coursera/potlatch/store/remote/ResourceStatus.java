package com.pataniqa.coursera.potlatch.store.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ResourceStatus {

    public enum ResourceState {
        READY, PROCESSING
    }

    @Getter @Setter private ResourceState state;

}