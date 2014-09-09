package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelDraft;
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

    // region Member Variables
    // =============================================================================================

    // Queue model.
    private ObjectModelQueue mModelQueue;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelMain(Activity activity, ViewModelCallbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup comet.
     */
    @Override
    public void initialize() {

        // Setup callbacks.
        setupCometCallbacks();
    }

    // endregion

    // region Public Methods
    // =============================================================================================

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

    // endregion

    // region Private Methods
    // =============================================================================================

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

                ObjectModelDraft objectModelDraft = new ObjectModelDraft();

                // Set and sync the draft.
                objectModelDraft.setDraftId(draftId);
                objectModelDraft.sync(mActivity, new Runnable() {

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

                // Set it as the current draft.
                AuthHelper.instance().setCurrentDraft(objectModelDraft);
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

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelMainCallbacks extends ViewModelCallbacks {

        public void onConfirmDraft(ViewModelMain viewModel);
        public void onConfirmQueue(ViewModelMain viewModel);
        public void    onLeftQueue(ViewModelMain viewModel);
        public void   onEnterDraft(ViewModelMain viewModel);
    }

    // endregion
}