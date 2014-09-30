package com.pataniqa.coursera.potlatch.store.remote;

public class ResourceStatus {

        public enum ResourceState {
            READY, PROCESSING
        }

        private ResourceState state;

        public ResourceStatus(ResourceState state) {
            this.state = state;
        }

        public ResourceState getState() {
            return state;
        }

        public void setState(ResourceState state) {
            this.state = state;
        }

    }