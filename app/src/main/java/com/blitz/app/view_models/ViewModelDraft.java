package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.screens.draft.DraftScreen;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.blitz.app.utilities.date.DateUtils;
import com.blitz.app.utilities.json.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrkcsc on 8/24/14. Copyright 2014 Blitz Studios
 */
public class ViewModelDraft extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // Suspended flag.
    private static final String STATE_DRAFT_SUSPENDED = "stateDraftSuspended";

    // Tracks paused state of the view model.
    private boolean mWasPaused;

    // State of the draft.
    public enum DraftState {
        DRAFT_PREVIEW, DRAFT_DRAFTING, DRAFT_COMPLETE
    }

    // Runs the main draft update loop.
    private Handler  mGameLoopHandler;
    private Runnable mGameLoopRunnable;

    // Reference to latest draft model.
    private RestModelDraft mDraftModel;

    // Current draft state.
    private DraftState mState;

    // String of the current round.
    private String mRoundAndPosition;

    // Prevents spamming of picks.
    private boolean mPickingLocked;
    private boolean mSyncingChoicesLocked;

    // Choice related structures.
    private List<RestModelPlayer> mCurrentPlayerChoices;

    // Round time information.
    private int mRoundTimeRemaining;
    private boolean mRoundTimeRemainingHidden;
    private boolean mRoundCompleteHidden;

    // Picks so far.
    private List<RestModelDraft.Pick> mCurrentPicks;

    // Callbacks.
    private ViewModelDraftCallbacks mCallbacks;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelDraft(Activity activity, ViewModelCallbacks callbacks) {
        super(activity, callbacks);

        // Set the draft model.
        mDraftModel = AuthHelper.instance().getCurrentDraft();

        // Set the callbacks.
        mCallbacks = getCallbacks(ViewModelDraftCallbacks.class);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Start the view model.
     */
    @Override
    public void initialize() {

        // If we are coming back from a paused state,
        // the draft model may be null or out of date.
        // We also may have missed comet events.  Explicitly
        // re-sync the draft state in this case, otherwise
        // normal initialization flow runs immediately.
        if (mWasPaused) {
            mWasPaused = false;

            syncDraft(new Runnable() {

                @Override
                public void run() {

                    // Make another call to initialize
                    // once we are synced with server.
                    initialize();
                }
            });
        } else {

            // Start comet.
            cometCallbacksStart();

            // Fetch users.
            syncUsers();

            // Start loop.
            gameLoopStart();
        }
    }

    /**
     * This is called when the associated
     * activity is thrown into a paused state.  Before
     * that happens, set it as paused, and
     * cleanup any running callbacks or loops.
     */
    @Override
    public void stop() {
        super.stop();

        // Now paused, under normal conditions, the
        // view model object will remain in memory,
        // causing a re-sync when initialized is called
        // when the user returns to the app.
        mWasPaused = true;

        // Stop comet.
        cometCallbacksStop();

        // Stop loop.
        gameLoopStop();
    }

    /**
     * Save a flag to note that we
     * have suspended the state.
     *
     * @param savedInstanceState State bundle.
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        super.saveInstanceState(savedInstanceState);

        // Draft is now suspended, however most of the time,
        // the activity will simply be resumed, and so the
        // saved instance state will not be required.
        savedInstanceState.putBoolean(STATE_DRAFT_SUSPENDED, true);
    }

    /**
     * Sync the draft when instance state
     * is restored.
     *
     * @param savedInstanceState State bundle.
     */
    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        super.restoreInstanceState(savedInstanceState);

        // This can only happen if the device goes into a low
        // memory situation and the associated activity is
        // destroyed and the user re-enters the application
        // directly into the draft screen instead of the
        // normal loading float - unlikely but possible on some
        // lower end devices.
        if (savedInstanceState != null &&
            savedInstanceState.getBoolean(STATE_DRAFT_SUSPENDED)) {

            // Simulate a paused state.
            mWasPaused = true;

            // Re-initialize normally.
            initialize();
        }
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Pick a player.
     *
     * @param playerId Player id.
     */
    public void pickPlayer(final String playerId) {

        // If player id provided, and we are allowed to draft a player.
        if (playerId != null && mDraftModel != null
                && mDraftModel.getCanUserDraft() && !mPickingLocked) {

            // Picking now locked.
            mPickingLocked = true;

            RestModelDraft.pickPlayer(null, mDraftModel.getId(), playerId,
                    new RestModelCallback<RestModelDraft>() {

                        @Override
                        public void onSuccess(RestModelDraft object) {

                            mPickingLocked = false;
                        }

                        @Override
                        public void onFailure() {
                            super.onFailure();

                            mPickingLocked = false;
                        }
                    });
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * If the draft gets suspended and we return,
     * manually sync the draft model.
     */
    private void syncDraft(final Runnable onSynced) {

        // Clear current choices.
        mCurrentPlayerChoices = null;

        RestModelDraft.fetchSyncedDraft(mActivity, mDraftModel.getId(),
                new RestModelCallback<RestModelDraft>() {

                    @Override
                    public void onSuccess(RestModelDraft draft) {

                        // Update draft model.
                        mDraftModel = draft;

                        if (mDraftModel.getIsDraftComplete()) {

                            // Draft is now complete.
                            updateState(DraftState.DRAFT_COMPLETE);

                        } else {

                            // If picks are available.
                            if (mDraftModel.getPicks() != null) {

                                List<String> playerIds = new ArrayList<String>();

                                for (RestModelDraft.Pick pick : mDraftModel.getPicks()) {

                                    playerIds.add(pick.getPlayerId());
                                }

                                // Update all player picks with actual data - these are
                                // all also choices so this method will work.
                                updateChoicesWithoutData(playerIds);
                            }

                            if (onSynced != null) {
                                onSynced.run();
                            }
                        }
                    }
                });
    }

    /**
     * Fetch relevant user information
     * for the users that belong to this
     * draft and fire off relevant callbacks.
     */
    private void syncUsers() {

        // Users associated with this draft.
        List<String> draftUserIds = mDraftModel.getUsers();

        // Fetch associated user objects.
        RestModelUser.getUsers(mActivity, draftUserIds, new RestModelUser.CallbackUsers() {

            @Override
            public void onSuccess(final List<RestModelUser> users) {

                final List<String> userAvatarItemIds = new ArrayList<String>();

                // Get list of item ids.
                for (RestModelUser user : users) {

                    userAvatarItemIds.add(user.getAvatarId());
                }

                // Need to fetch each users item object to complete the sync.
                RestModelItem.fetchItems(mActivity, userAvatarItemIds,
                        new RestModelItem.CallbackItems() {

                            @Override
                            public void onSuccess(List<RestModelItem> items) {

                                Map<String, RestModelItem> itemsIds =
                                        new HashMap<String, RestModelItem>();

                                for (RestModelItem item : items) {

                                    itemsIds.put(item.getId(), item);
                                }

                                for (RestModelUser user : users) {

                                    // User is synced, may have avatar
                                    // information as well.
                                    syncUsersCallback(user,
                                            itemsIds.containsKey(user.getAvatarId()) ?
                                                    itemsIds.get(user.getAvatarId()) : null);
                                }
                            }
                        });
            }
        });
    }

    /**
     * Given a user and avatar, make callback.
     *
     * @param user User object.
     * @param userAvatarItem User avatar object.
     */
    private void syncUsersCallback(RestModelUser user, RestModelItem userAvatarItem) {

        if (mCallbacks != null) {
            mCallbacks
                    .onUserSynced(
                            user.getId(),
                            user.getUsername(),
                            user.getRating(),
                            user.getWins(),
                            user.getLosses(),
                            user.getTies(),
                            userAvatarItem == null ? null : userAvatarItem.getDefaultImgPath());
        }
    }

    /**
     * Start all callbacks.
     */
    private void cometCallbacksStart() {

            // Subscribe.
            CometAPIManager
                    .subscribeToChannel("draft:" + mDraftModel.getId())
                    .addCallback(DraftScreen.class, new CometAPICallback<DraftScreen>() {

                        @Override
                        public void messageReceived(DraftScreen receivingClass, JsonObject message) {

                            // Handle the action.
                            ((ViewModelDraft) receivingClass.onFetchViewModel())
                                    .cometCallbacksHandleAction(message);
                        }
                    }, "draftGameCallback");
    }

    /**
     * Stop all comet callbacks.
     */
    private void cometCallbacksStop() {

        // Unsubscribe.
        CometAPIManager
                .unsubscribeFromChannel("draft:" + mDraftModel.getId());
    }

    /**
     * Handle action received from comet.
     *
     * @param message Json message.
     */
    private void cometCallbacksHandleAction(JsonObject message) {

        // Get the action identifier.
        String action = message.get("action").getAsString();

        if (action.equals("sync_to_server_time")) {

            // Fetch last server time.
            Date lastServerTime = DateUtils.getDateInGMT
                    (message.get("last_server_time").getAsString());

            mDraftModel.setLastServerTime(lastServerTime);

        } else if (action.equals("show_choices")) {

            // Get array of choices.
            JsonArray choicesJson = message.get("choices").getAsJsonArray();

            // List of choice ids.
            List<String> choiceIds = new ArrayList<String>();

            for (JsonElement choiceJson : choicesJson) {

                // Fetch as json object.
                JsonObject choiceJsonObject = choiceJson.getAsJsonObject();

                // Add to list of choice ids.
                choiceIds.add(JsonHelper.parseString(choiceJsonObject.get("id")));

                // Add to draft model.
                mDraftModel.addChoice(RestModelPlayer
                        .fetchPlayerFromCometJson(choiceJsonObject));
            }

            // Add to choices.
            mDraftModel.addChoices(choiceIds);

        } else if (action.equals("pick_player")) {

            // Create a new pick object.
            RestModelDraft.Pick pick = new RestModelDraft.Pick(
                    message.get("player_id").getAsString(),
                    message.get("user_id").getAsString());

            mDraftModel.addPick(pick);
        }

        // Look for last round complete time json.
        JsonElement lastRoundCompleteTimeJson = message.get("last_round_complete_time");

        if (lastRoundCompleteTimeJson != null &&
           !lastRoundCompleteTimeJson.isJsonNull()) {

            // Fetch as string.
            String lastRoundCompleteTime = lastRoundCompleteTimeJson.getAsString();

            if (!lastRoundCompleteTime.equals("None")) {

                mDraftModel.setLastRoundCompleteTime
                        (DateUtils.getDateInGMT(lastRoundCompleteTime));
            }
        }

        // Execute the game loop immediately so the UI
        // can instantly respond to comet events that
        // change the draft model.
        gameLoopExecute();
    }

    /**
     * Starts the game loop.
     */
    private void gameLoopStart() {

        if (mGameLoopHandler == null) {
            mGameLoopHandler = new Handler();
        }

        if (mGameLoopRunnable == null) {
            mGameLoopRunnable = new Runnable() {

                @Override
                public void run() {

                    // Sync state.
                    gameLoopExecute();

                    // Continue running the loop on a 100ms delay.
                    mGameLoopHandler.postDelayed(mGameLoopRunnable, 100);
                }
            };
        }

        mGameLoopHandler.post(mGameLoopRunnable);
    }

    /**
     * Stop the game loop.
     */
    private void gameLoopStop() {

        if (mGameLoopHandler  != null &&
            mGameLoopRunnable != null) {

            // Stop the timer.
            mGameLoopHandler.removeCallbacks(mGameLoopRunnable);
        }
    }

    /**
     * Raw code of what happens on
     * each game loop.
     */
    private void gameLoopExecute() {

        // Sync state.
        resolveState();
        resolveCurrentRoundAndPosition();
        resolveRoundTimeRemaining();
        resolveRoundComplete();
        resolveChoices();
        resolvePicks();
    }

    // endregion

    // region Resolve Methods
    // =============================================================================================

    /**
     * Resolve the state of the draft.
     */
    private void resolveState() {

        DraftState state;

        // If draft complete and enough time has passed for the
        // draft to be saved to disk, set state to complete.
        if (mDraftModel.getCurrentRound() > mDraftModel.getRounds() &&
            mDraftModel.getSecondsSinceLastRoundCompleteTime() > mDraftModel.getTimePerPostview()) {

            state = DraftState.DRAFT_COMPLETE;

        } else if (mDraftModel.getSecondsSinceStarted() < mDraftModel.getDraftStartBuffer()) {

            state = DraftState.DRAFT_PREVIEW;

        } else {

            state = DraftState.DRAFT_DRAFTING;
        }

        updateState(state);
    }

    /**
     * Set the round and position string identifier
     * and emit the value any time it changes.
     */
    private void resolveCurrentRoundAndPosition() {

        // All round have been completed.
        if (mDraftModel.getCurrentRound() > mDraftModel.getRounds()) {

            return;
        }

        // Start with just round identifier.
        String roundAndPosition = "Round " + mDraftModel.getCurrentRound();

        // Add position if that string is available.
        if (mDraftModel.getPositionsRequired() != null &&
            mDraftModel.getPositionsRequired().get(mDraftModel.getCurrentRound() - 1) != null) {

            roundAndPosition += " - " +
                    mDraftModel.getPositionsRequired().get(mDraftModel.getCurrentRound() - 1);
        }

        if (mDraftModel.getCurrentRound() == mDraftModel.getRounds()) {

            // Last round (FLEX pick).
            roundAndPosition = "Final Round!";
        }

        updateCurrentRoundAndPosition(roundAndPosition);
    }

    /**
     * Resolve the time remaining in the round.
     */
    private void resolveRoundTimeRemaining() {

        int secondsElapsedThisRound;

        if (mDraftModel.getLastRoundCompleteTime() != null) {

            // Seconds since last round completed, minus post/pre view time.
            secondsElapsedThisRound = mDraftModel.getSecondsSinceLastRoundCompleteTime()
                    - mDraftModel.getTimePerPostview() - mDraftModel.getTimePerPreview();

        } else {

            // Otherwise, seconds for the first round elapsed time.
            secondsElapsedThisRound = mDraftModel.getSecondsSinceStarted()
                    - mDraftModel.getDraftStartBuffer() - mDraftModel.getTimePerPreview();
        }

        int roundTimeRemaining = mDraftModel.getTimePerPick() - secondsElapsedThisRound;

        // Are we currently in the pick window.
        boolean isInPickWindow = roundTimeRemaining > 0 && roundTimeRemaining <=
                mDraftModel.getTimePerPick();

        // Is round complete.
        boolean isRoundComplete =
                mDraftModel.getChoices() != null && mDraftModel.getPicks() != null &&
                        mDraftModel.getChoices().size() == mDraftModel.getPicks().size() / 2;

        updateRoundTimeRemaining(roundTimeRemaining,
                !isInPickWindow || isRoundComplete);
    }

    /**
     * Calculate the boolean if should
     * show or hide the round complete time.
     */
    private void resolveRoundComplete() {

        boolean roundCompleteHidden;

        if (mDraftModel.getLastRoundCompleteTime() == null ||
            mDraftModel.getChoices()               == null ||
            mDraftModel.getPicks()                 == null) {

            roundCompleteHidden = true;
        } else {


            boolean roundComplete = mDraftModel.getChoices().size()
                    == mDraftModel.getPicks().size() / 2;

            roundCompleteHidden = !(mDraftModel.getSecondsSinceLastRoundCompleteTime() <
                    mDraftModel.getTimePerPostview() && roundComplete);
        }

        updateRoundComplete(roundCompleteHidden);
    }

    /**
     * Resolve the choices the player has to
     * choose from.
     */
    private void resolveChoices() {

        // Fetch current choice ids.
        List<String> playerChoiceIds = mDraftModel.getCurrentPlayerChoices();

        if (playerChoiceIds != null) {

            List<RestModelPlayer> playerChoices = new ArrayList<RestModelPlayer>();

            List<String> playerChoicesToSync = new ArrayList<String>();

            for (String playerId : playerChoiceIds) {

                if (mDraftModel.getPlayerDataMap().containsKey(playerId)) {

                    // Already been populated view comet server.
                    playerChoices.add(mDraftModel.getPlayerDataMap().get(playerId));

                } else {

                    // Need to fetch player data.
                    playerChoicesToSync.add(playerId);
                }
            }

            updateChoices(playerChoices, playerChoicesToSync);
        }
    }

    /**
     * Resolve and emit the player picks.
     */
    private void resolvePicks() {

        // No choices yet received.
        if (mDraftModel.getChoices() == null ||
            mDraftModel.getChoices().size() == 0) {

            return;
        }

        // No picks yet received.
        if (mDraftModel.getPicks() == null) {

            return;
        }

        List<RestModelDraft.Pick> currentPicks = new ArrayList<RestModelDraft.Pick>();

        // If both round picks have been made.
        if (mDraftModel.getChoices().size() ==
                mDraftModel.getPicks().size() / 2 &&
                mDraftModel.getPicks().size() % 2 == 0) {

            // Both picks are current.
            currentPicks.add(mDraftModel.getPicks().get(mDraftModel.getPicks().size() - 1));
            currentPicks.add(mDraftModel.getPicks().get(mDraftModel.getPicks().size() - 2));

        } else if (mDraftModel.getChoices().size() >
                mDraftModel.getPicks().size() / 2 &&
                mDraftModel.getPicks().size() % 2 == 1) {

            // Only one pick made.
            currentPicks.add(mDraftModel.getPicks().get(mDraftModel.getPicks().size() - 1));
        }

        // Update picks.
        updatePicks(currentPicks);
    }

    // endregion

    // region Update Methods
    // =============================================================================================

    /**
     * Update the state variable if it changes.
     *
     * @param state Current draft state.
     */
    private void updateState(DraftState state) {

        if (mState != state) {
            mState  = state;

            // Draft state has changed.
            if (mCallbacks != null) {
                mCallbacks.onDraftStateChanged(mState);
            }
        }
    }

    /**
     * Update the round and position string when
     * it changes.
     *
     * @param roundAndPosition Current round and position.
     */
    private void updateCurrentRoundAndPosition(String roundAndPosition) {

        if (!roundAndPosition.equals(mRoundAndPosition) && mState != DraftState.DRAFT_PREVIEW) {

            if (mDraftModel.getCurrentRound() == 1 ||
                    mDraftModel.getSecondsSinceLastRoundCompleteTime() >
                            mDraftModel.getTimePerPostview()) {

                mRoundAndPosition = roundAndPosition;

                if (mCallbacks != null) {
                    mCallbacks.onRoundAndPositionChanged(mRoundAndPosition);
                }
            }
        }
    }

    /**
     * Update round time remaining, whether or not that
     * timer is hidden.
     *
     * @param roundTimeRemaining Current round time remaining.
     * @param roundTimeRemainingHidden Round time remaining is hidden.
     */
    private void updateRoundTimeRemaining(int roundTimeRemaining,
                                          boolean roundTimeRemainingHidden) {

        // Update round time remaining if changed and not hidden.
        if (mRoundTimeRemaining != roundTimeRemaining && !roundTimeRemainingHidden) {
            mRoundTimeRemaining = roundTimeRemaining;

            if (mCallbacks != null) {
                mCallbacks.onRoundTimeRemainingChanged(mRoundTimeRemaining);
            }
        }

        // Update round time remaining hidden if needed.
        if (mRoundTimeRemainingHidden != roundTimeRemainingHidden) {
            mRoundTimeRemainingHidden = roundTimeRemainingHidden;

            if (mCallbacks != null) {
                mCallbacks.onRoundTimeRemainingHiddenChanged(mRoundTimeRemainingHidden);
            }
        }
    }

    /**
     * Update round complete hidden flag
     * and emit event when changed.
     *
     * @param roundCompleteHidden Is round complete hidden.
     */
    private void updateRoundComplete(boolean roundCompleteHidden) {

        if (mRoundCompleteHidden != roundCompleteHidden) {
            mRoundCompleteHidden = roundCompleteHidden;

            if (mCallbacks != null) {
                mCallbacks.onRoundCompleteHiddenChanged(roundCompleteHidden);
            }
        }
    }

    /**
     * Given the current set of player choices and
     * the player choices that need syncing, either
     * sync the missing choices, or emit the new
     * choices if they have changed.
     *
     * @param playerChoices Loaded player choices.
     * @param playerChoicesToSync Player ids for choices that need to be synced.
     */
    private void updateChoices(List<RestModelPlayer> playerChoices,
                               List<String> playerChoicesToSync) {

        if (!playerChoicesToSync.isEmpty()) {

            // Need to fetch any missing player choices before
            // we can emit the player choices.
            updateChoicesWithoutData(playerChoicesToSync);

        } else if (!playerChoices.equals(mCurrentPlayerChoices)) {

            // Update player choices and timestamp.
            mCurrentPlayerChoices= playerChoices;

            List<String> playerIds       = new ArrayList<String>();
            List<String> playerPhotoUrls = new ArrayList<String>();
            List<String> playerFullNames = new ArrayList<String>();
            List<String> playerPositions = new ArrayList<String>();
            List<String> playerOpponents = new ArrayList<String>();

            for (RestModelPlayer playerChoice : mCurrentPlayerChoices) {

                // List of player id's.
                playerIds.add(playerChoice.getId());

                // Rest of player information.
                playerPhotoUrls.add(playerChoice.getPhotoUrl());
                playerFullNames.add(playerChoice.getFullName());
                playerPositions.add(playerChoice.getPosition() + " - " + playerChoice.getTeam());
                playerOpponents.add(playerChoice.getOpponent());
            }

            if (mCallbacks != null) {
                mCallbacks.onPlayerChoicesChanged(playerIds, playerPhotoUrls,
                        playerFullNames, playerPositions, playerOpponents);
            }
        }
    }

    /**
     * When coming back from a sleep state, we might
     * not have received choice data. Need to
     * make a rest call to retrieve it.
     *
     * @param choices Choice id's to sync.
     */
    private void updateChoicesWithoutData(List<String> choices) {

        if (choices == null || choices.isEmpty()) {

            return;
        }

        if (!mSyncingChoicesLocked) {
             mSyncingChoicesLocked = true;

            RestModelPlayer.fetchPlayers(null, choices,
                    new RestModelPlayer.CallbackPlayers() {

                        @Override
                        public void onSuccess(List<RestModelPlayer> players) {

                            // TODO: Implement me.
                        }
                    });
        }
    }

    /**
     * Given a list of the current picks, see if they
     * have changed from the cached current picks.
     *
     * If so, serialize the data and emit it via
     * the callback interface.
     *
     * @param currentPicks Current picks.
     */
    private void updatePicks(List<RestModelDraft.Pick> currentPicks) {

        // If at least one pick and different than current picks.
        if (currentPicks.size() > 0 && !currentPicks.equals(mCurrentPicks)) {

            // Update current picks.
            mCurrentPicks = currentPicks;

            List<String> playerIds = new ArrayList<String>();
            List<String> userIds = new ArrayList<String>();

            // Serialize the player and user ids.
            for (RestModelDraft.Pick pick : currentPicks) {

                playerIds.add(pick.getPlayerId());
                  userIds.add(pick.getUserId());
            }

            if (mCallbacks != null) {
                mCallbacks.onPicksChanged(playerIds, userIds);
            }
        }
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    /**
     * Drafting related callbacks.
     */
    public interface ViewModelDraftCallbacks extends ViewModelCallbacks {

        // User information received.
        public void onUserSynced(String userId, String userName,
                int rating, int wins, int losses, int ties, String itemAvatarUrl);

        // Draft state updated.
        public void onDraftStateChanged(DraftState state);

        // New round string.
        public void onRoundAndPositionChanged(String roundAndPosition);

        // New list of player choices.
        public void onPlayerChoicesChanged(
                List<String> playerIds,
                List<String> playerPhotoUrls,
                List<String> playerFullNames,
                List<String> playerPositions,
                List<String> playerOpponents);

        // New pick received.
        public void onPicksChanged(
                List<String> playerIds,
                List<String> userIds);

        // Round time remaining update.
        public void onRoundTimeRemainingChanged(int roundTimeRemaining);
        public void onRoundTimeRemainingHiddenChanged(boolean roundTimeRemainingHidden);

        // Is the round complete UI hidden.
        public void onRoundCompleteHiddenChanged(boolean completeHidden);
    }

    // endregion
}