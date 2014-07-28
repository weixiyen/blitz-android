package com.blitz.app.models.views;

import android.os.Bundle;

import com.blitz.app.models.comet.CometAPICallback;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.models.objects.ObjectModelQueue;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.logging.LogHelper;
import com.google.gson.JsonObject;

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

        // Setup callbacks.
        setupCometCallbacks();
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

    /**
     * Join the draft queue.
     */
    public void confirmQueue() {

        // Join the draft.
        getModelQueue().confirmQueue(mActivity, null);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Setup callbacks for the draft
     * powered by the comet manager.
     */
    private void setupCometCallbacks() {

        // Fetch comet channel for this user.
        String userCometChannel = "user:" + AppDataObject.userId.getString();

        // Subscribe to channel, set callback.
        CometAPIManager

                // Subscribe to user channel.
                .subscribeToChannel(userCometChannel)

                // Set callback action.
                .addCallback(MainScreen.class, new CometAPICallback<MainScreen>() {

                    @Override
                    public void messageReceived(MainScreen receivingClass, JsonObject message) {

                        receivingClass.getViewModel().handleDraftAction(receivingClass, message);
                    }
                }, "draftUserCallbackMainScreen");
    }

    /**
     * Handle a draft callback action.  Either
     * show or hide a confirmation dialog, or
     * simply enter the draft.
     *
     * @param receivingClass Instance of this activity.
     * @param message Json message sent.
     */
    private void handleDraftAction(MainScreen receivingClass, JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        if (action.equals("confirm_draft")) {

            // Present confirmation dialog to user.
            receivingClass.showConfirmDraftDialog();

        } else if (action.equals("left_queue")) {

            // Dismiss dialog.
            receivingClass.hideConfirmDraftDialog();

        } else if (action.equals("enter_draft")) {

            // View model, enter the draft
            // draft model contains an
            // active draft singleton (or auth manager).
            LogHelper.log("Action: " + action);
        }
    }


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
