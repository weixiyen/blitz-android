package com.blitz.app.models.operation;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public interface ModelOperationInterface {

    void start();
    void finish();

    void success();
    void failure();
}