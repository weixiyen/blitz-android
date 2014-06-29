package com.blitz.app.models.operation;

import android.util.Log;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class ModelOperation implements ModelOperationInterface {

    @Override
    public void success() {
        Log.e("success", "success");
    }

    @Override
    public void failure() {
        Log.e("failure", "failure");
    }
}