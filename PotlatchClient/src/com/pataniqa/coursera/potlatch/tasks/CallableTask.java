package com.pataniqa.coursera.potlatch.tasks;

import java.util.concurrent.Callable;


import android.os.AsyncTask;
import android.util.Log;

public class CallableTask<T> extends AsyncTask<Void, Double, T> {

    private static final String LOG_TAG = CallableTask.class.getName();

    public static <V> void invoke(Callable<V> call, TaskCallback<V> callback) {
        new CallableTask<V>(call, callback).execute();
    }

    private final Callable<T> callable;

    private final TaskCallback<T> callback;

    private Exception error;

    public CallableTask(Callable<T> callable, TaskCallback<T> callback) {
        this.callable = callable;
        this.callback = callback;
    }

    @Override
    protected T doInBackground(Void... ts) {
        T result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error invoking callable in AsyncTask callable: " + callable, e);
            error = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(T r) {
        if (error != null)
            callback.error(error);
        else
            callback.success(r);
    }
}
