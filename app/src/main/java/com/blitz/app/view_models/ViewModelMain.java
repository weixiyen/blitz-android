package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelQueue;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class ViewModelMain extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // Queue model.
    private RestModelQueue mModelQueue;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelMain(BaseActivity activity, ViewModel.Callbacks callbacks) {
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

                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class).onConfirmQueue(ViewModelMain.this);
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
        final Callbacks callbacks = getCallbacks(Callbacks.class);

        if (callbacks != null) {

            if (action.equals("confirm_draft")) {

                callbacks.onConfirmDraft(this);

            } else if (action.equals("left_queue")) {

                callbacks.onLeftQueue(this);

            } else if (action.equals("enter_draft")) {

                // Fetch the draft id.
                final String draftId = message.get("draft_id").getAsString();

                // Fetch the associated draft object.
                RestModelDraft.fetchSyncedDraft(mActivity, draftId,
                        new RestModelCallback<RestModelDraft>() {

                            @Override
                            public void onSuccess(RestModelDraft draft) {

                                // Set it as the current draft.
                                AuthHelper.instance().setCurrentDraft(draft);

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
    private RestModelQueue getModelQueue() {

        // Lazy load the model.
        if (mModelQueue == null) {
            mModelQueue = new RestModelQueue();
        }

        return mModelQueue;
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onConfirmDraft(ViewModelMain viewModel);
        public void onConfirmQueue(ViewModelMain viewModel);
        public void    onLeftQueue(ViewModelMain viewModel);
        public void   onEnterDraft(ViewModelMain viewModel);
    }

    // endregion
}