package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.blitz.app.object_models.ObjectModelQueue;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
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

    /**
     * Setup comet.
     *
     * @param activity Target activity.
     * @param callbacks Target callbacks.
     */
    @Override
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {
        super.initialize(activity, callbacks);

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
        getModelQueue().confirmQueue(mActivity, new Runnable() {

            @Override
            public void run() {

                if (getCallbacks(ViewModelMainCallbacks.class) != null) {
                    getCallbacks(ViewModelMainCallbacks.class).onConfirmQueue(ViewModelMain.this);
                }
            }
        });
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
        String userCometChannel = "user:" + AppDataObject.userId.get();

        // Subscribe to channel, set callback.
        CometAPIManager

                // Subscribe to user channel.
                .subscribeToChannel(userCometChannel)

                // Set callback action.
                .addCallback(MainScreen.class, new CometAPICallback<MainScreen>() {

                    @Override
                    public void messageReceived(MainScreen receivingClass, JsonObject message) {

                        ((ViewModelMain)receivingClass.onFetchViewModel())
                                .handleDraftAction(message);

                    }
                }, "draftUserCallbackMainScreen");
    }

    /**
     * Handle a draft callback action.  Either
     * show or hide a confirmation dialog, or
     * simply enter the draft.
     *
     * @param message Json message sent.
     */
    private void handleDraftAction(JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        // Fetch callbacks.
        final ViewModelMainCallbacks callbacks = getCallbacks(ViewModelMainCallbacks.class);

        if (callbacks != null) {

            if (action.equals("confirm_draft")) {

                callbacks.onConfirmDraft(this);

            } else if (action.equals("left_queue")) {

                callbacks.onLeftQueue(this);

            } else if (action.equals("enter_draft")) {

                // Fetch the draft id.
                final String draftId = message.get("draft_id").getAsString();

                // Setup and sync the current draft.
                AuthHelper.getCurrentDraft().setDraftId(draftId);
                AuthHelper.getCurrentDraft().sync(mActivity, new Runnable() {

                    @Override
                    public void run() {

                        // Fetch comet channel for this user.
                        String userCometChannel = "user:" + AppDataObject.userId.get();

                        // Unsubscribe them from it.
                        CometAPIManager.unsubscribeFromChannel(userCometChannel);

                        // Run UI callback.
                        callbacks.onEnterDraft(ViewModelMain.this);
                    }
                });
            }
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

    //==============================================================================================
    // Callbacks Interface
    //==============================================================================================

    public interface ViewModelMainCallbacks extends ViewModelCallbacks {

        public void onConfirmDraft(ViewModelMain viewModel);
        public void onConfirmQueue(ViewModelMain viewModel);
        public void    onLeftQueue(ViewModelMain viewModel);
        public void   onEnterDraft(ViewModelMain viewModel);
    }
}