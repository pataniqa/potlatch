package com.pataniqa.coursera.potlatch.tasks;

public interface TaskCallback<T> {

    void success(T result);

    void error(Exception e);

}
