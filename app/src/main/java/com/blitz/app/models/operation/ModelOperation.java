package com.blitz.app.models.operation;

import android.util.Log;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public abstract class ModelOperation implements ModelOperationInterface {

    @Override
    public void start() {
        Log.e("ModelOperation", "Start - lock the UI");
    }

    @Override
    public void finish() {
        Log.e("ModelOperation", "Finish - unlock the UI");
    }

    @Override
    public abstract void success();

    @Override
    public void failure() {
        Log.e("ModelOperation", "Operation failure.");
    }
}