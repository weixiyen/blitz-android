package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.rest_models.RestModelQueue;
import com.blitz.app.rest_models.RestResult;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class ViewModelMain extends ViewModel {

    // region Constructor
    // ============================================================================================================

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
    // ============================================================================================================

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
    // ============================================================================================================

    /**
     * Leave the draft queue.
     */
    public void leaveQueue() {

        AuthHelper.instance().getPreferences(mActivity, false,
            new RestResult<RestModelPreferences>() {

                @Override
                public void onSuccess(RestModelPreferences object) {

                    // Leave the draft.
                    RestModelQueue.leaveQueue(mActivity, object.getCurrentActiveQueue(),
                        new RestResult<RestModelQueue>() {

                            @Override
                            public void onSuccess(RestModelQueue object) {

                            }
                        });
                }
            });
    }

    /**
     * Join the draft queue.
     */
    public void confirmQueue() {

        AuthHelper.instance().getPreferences(mActivity, false,
            new RestResult<RestModelPreferences>() {

                @Override
                public void onSuccess(RestModelPreferences object) {

                    // Join the draft.
                    RestModelQueue.confirmQueue(mActivity, object.getCurrentActiveQueue(),
                        new RestResult<RestModelQueue>() {

                            @Override
                            public void onSuccess(RestModelQueue object) {

                                if (getCallbacks(Callbacks.class) != null) {
                                    getCallbacks(Callbacks.class).onConfirmQueue(ViewModelMain.this);
                                }
                            }
                        });
                }
            });
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

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
                .addCallback(MainScreen.class, (MainScreen receivingClass, JsonObject message) ->
                        ((ViewModelMain)receivingClass.onFetchViewModel())
                                .handleDraftAction(message), "draftUserCallbackMainScreen");
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

            switch (action) {
                case "confirm_draft":

                    callbacks.onConfirmDraft(this);
                    break;
                case "left_queue":

                    callbacks.onLeftQueue(this);
                    break;
                case "enter_draft":

                    // Fetch the draft id.
                    final String draftId = message.get("draft_id").getAsString();

                    // Fetch the associated draft object.
                    RestModelDraft.fetchSyncedDraft(mActivity, draftId,
                            new RestResult<RestModelDraft>() {

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
                    break;
            }
        }
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onConfirmDraft(ViewModelMain viewModel);
        public void onConfirmQueue(ViewModelMain viewModel);
        public void    onLeftQueue(ViewModelMain viewModel);
        public void   onEnterDraft(ViewModelMain viewModel);
    }

    // endregion
}