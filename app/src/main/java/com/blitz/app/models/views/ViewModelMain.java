package com.blitz.app.models.views;

import android.os.Bundle;

import com.blitz.app.models.objects.ObjectModelQueue;

/**
 * Created by mrkcsc on 7/27/14.
 */
public class ViewModelMain extends ViewModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Queue model.
    private ObjectModelQueue mModelQueue;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        // TODO: Restore.
    }

    @Override
    public Bundle saveInstanceState(Bundle savedInstanceState) {

        // TODO: Save.
        return null;
    }

    @Override
    public void initialize() {
        // TODO: Initialize.
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Leave the draft queue.
     */
    public void leaveQueue() {

        // Leave the queue.
        getModelQueue().leaveQueue(mActivity, null);
    }

    public void confirmQueue() {

        getModelQueue().confirmQueue(mActivity, null);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Fetch queue model instance.
     *
     * @return Queue model instance.
     */
    private ObjectModelQueue getModelQueue() {

        // Lazy load the model.
        if (mModelQueue == null) {
            mModelQueue = new ObjectModelQueue();
        }

        return mModelQueue;
    }
}
