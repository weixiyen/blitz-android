package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelPlayer;
import com.blitz.app.object_models.ObjectModelUser;
import com.blitz.app.screens.draft.DraftScreen;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.blitz.app.utilities.date.DateUtils;
import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.logging.LogHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrkcsc on 8/24/14. Copyright 2014 Blitz Studios
 */
public class ViewModelDraft extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // Suspended flag.
    private static final String STATE_DRAFT_SUSPENDED = "stateDraftSuspended";

    // State of the draft.
    public enum DraftState {
        DRAFT_PREVIEW, DRAFT_DRAFTING, DRAFT_COMPLETE
    }

    // Runs the main draft update loop.
    private Handler  mGameLoopHandler;
    private Runnable mGameLoopRunnable;

    // Reference to latest draft model.
    private ObjectModelDraft mDraftModel;

    // Current draft state.
    private DraftState mState;

    // String of the current round.
    private String mRoundAndPosition;

    // Prevents spamming of picks.
    private boolean mPickingLocked;

    // Choice related structures.
    private List<ObjectModelPlayer> mCurrentPlayerChoices;
    private Date mCurrentPlayerChoicesShowTime;

    // Round time information.
    private int mRoundTimeRemaining;
    private boolean mRoundTimeRemainingHidden;

    // Are the choices hidden.
    private boolean mChoicesViewHidden;

    // Picks so far.
    private List<ObjectModelDraft.Pick> mCurrentPicks;

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

        // Start comet.
        cometCallbacksStart();

        // Fetch users.
        syncUsers();

        // Start loop.
        gameLoopStart();
    }

    /**
     * Stop the view model.
     */
    @Override
    public void stop() {
        super.stop();

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

        // Draft is now suspended.
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

        // Sync the draft if needed.
        if (savedInstanceState != null &&
            savedInstanceState.getBoolean(STATE_DRAFT_SUSPENDED)) {

            syncDraft();
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

        // TODO: Put back true client locking behavior.

        // If player id provided, and we are allowed to draft a player.
        if (playerId != null && mDraftModel != null) {

            mPickingLocked = true;

            ObjectModelDraft.pickPlayer(null, mDraftModel.getId(), playerId,
                    new ObjectModelDraft.DraftCallback() {

                        @Override
                        public void onSuccess(ObjectModelDraft draft) {

                            // Picking no longer locked.
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
    private void syncDraft() {

        // TODO: Implement me.
    }

    /**
     * When coming back from a sleep state, we might
     * not have received choice data. Need to
     * make a rest call to retrieve it.
     *
     * @param choices Choice id's to sync.
     */
    private void syncChoicesWithoutData(ArrayList<String> choices) {

        // TODO: Implement me.
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
                                    .cometHandleAction(message);
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
    private void cometHandleAction(JsonObject message) {

        // Get the action identifier.
        String action = message.get("action").getAsString();

        if (action == null) {

            // Log with amplitude.
            LogHelper.log("Unknown action.");

        } else if (action.equals("sync_to_server_time")) {

            // Fetch last server time.
            Date lastServerTime = DateUtils.getDateInGMT
                    (message.get("last_server_time").getAsString());

            mDraftModel.setLastServerTime(lastServerTime);

        } else if (action.equals("show_choices")) {

            // Get array of choices.
            JsonArray choicesJson = message.get("choices").getAsJsonArray();

            // List of choice ids.
            ArrayList<String> choiceIds = new ArrayList<String>();

            for (JsonElement choiceJson : choicesJson) {

                // Fetch as json object.
                JsonObject choiceJsonObject = choiceJson.getAsJsonObject();

                // Add to list of choice ids.
                choiceIds.add(JsonHelper.parseString(choiceJsonObject.get("id")));

                // Add to draft model.
                mDraftModel.addChoice(ObjectModelPlayer
                        .fetchPlayerFromCometJson(choiceJsonObject));
            }

            // Add to choices.
            mDraftModel.addChoices(choiceIds);

        } else if (action.equals("pick_player")) {

            // Create a new pick object.
            ObjectModelDraft.Pick pick = new ObjectModelDraft.Pick(
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
                    resolveState();
                    resolveCurrentRoundAndPosition();
                    resolveRoundTimeRemaining();
                    resolveRoundComplete();
                    resolveChoices();
                    resolvePicks();

                    // Continue running the loop on a 100ms delay.
                    mGameLoopHandler.postDelayed(mGameLoopRunnable, 250);
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
     * Fetch relevant user information
     * for the users that belong to this
     * draft and fire off relevant callbacks.
     */
    private void syncUsers() {

        // Users associated with this draft.
        List<String> draftUserIds = mDraftModel.getUsers();

        // Fetch associated user objects.
        ObjectModelUser.getUsers(mActivity, draftUserIds, new ObjectModelUser.CallbackUsers() {

            @Override
            public void onSuccess(final List<ObjectModelUser> users) {

                final ArrayList<String> userAvatarItemIds =
                        new ArrayList<String>();

                // Get list of item ids.
                for (ObjectModelUser user : users) {

                    userAvatarItemIds.add(user.getAvatarId());
                }

                // Need to fetch each users item object to complete the sync.
                ObjectModelItem.fetchItems(mActivity, userAvatarItemIds,
                        new ObjectModelItem.CallbackItems() {

                    @Override
                    public void onSuccess(List<ObjectModelItem> items) {

                        HashMap<String, ObjectModelItem> itemsIds =
                                new HashMap<String, ObjectModelItem>();

                        for (ObjectModelItem item : items) {

                            itemsIds.put(item.getId(), item);
                        }

                        for (ObjectModelUser user : users) {

                            // User is synced, may have avatar
                            // information as well.
                            userSyncedCallback(user,
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
    private void userSyncedCallback(ObjectModelUser user, ObjectModelItem userAvatarItem) {

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
     * Given a new list of player choices, update
     * the current choices and emit relevant
     * information through the view model callbacks.
     *
     * @param playerChoices Player choice objects.
     */
    private void updatePlayerChoices(List<ObjectModelPlayer> playerChoices) {

        // Update player choices and timestamp.
        mCurrentPlayerChoices= playerChoices;
        mCurrentPlayerChoicesShowTime
                = DateUtils.getDateInGMT();

        List<String> playerIds       = new ArrayList<String>();
        List<String> playerPhotoUrls = new ArrayList<String>();
        List<String> playerFullNames = new ArrayList<String>();
        List<String> playerPositions = new ArrayList<String>();
        List<String> playerOpponents = new ArrayList<String>();

        for (ObjectModelPlayer playerChoice : mCurrentPlayerChoices) {

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

        if (mState != state) {
            mState  = state;

            // Draft state has changed.
            if (mCallbacks != null) {
                mCallbacks.onDraftStateChanged(mState);
            }
        }
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
     * Resolve the time remaining in the round
     * and some additional choice information regarding round time.
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
        boolean isInPickWindow = roundTimeRemaining > 0 && roundTimeRemaining <
                mDraftModel.getTimePerPick();

        // Is round complete.
        boolean isRoundComplete =
                mDraftModel.getChoices() != null && mDraftModel.getPicks() != null &&
                        mDraftModel.getChoices().size() == mDraftModel.getPicks().size() / 2;

        // Are we in post pick window.
        boolean isInPostView = roundTimeRemaining -
                mDraftModel.getTimePerPreview() > mDraftModel.getTimePerPick();

        // Is the choices view hidden.
        boolean isChoicesViewHidden = !(isInPickWindow || isInPostView) ||
                mDraftModel.getCurrentPlayerChoices() == null;

        updateRoundTimeRemaining(roundTimeRemaining,
                !isInPickWindow || isRoundComplete, isChoicesViewHidden);
    }

    private void resolveRoundComplete() {

        // TODO: Implement me.
    }

    /**
     * Resolve the choices the player has to
     * choose from.
     */
    private void resolveChoices() {

        // Fetch current choice ids.
        ArrayList<String> playerChoiceIds = mDraftModel.getCurrentPlayerChoices();

        if (playerChoiceIds != null) {

            ArrayList<ObjectModelPlayer> playerChoices =
                    new ArrayList<ObjectModelPlayer>();

            ArrayList<String> playerChoicesToSync =
                    new ArrayList<String>();

            for (String playerId : playerChoiceIds) {

                if (mDraftModel.getPlayerDataMap().containsKey(playerId)) {

                    // Already been populated view comet server.
                    playerChoices.add(mDraftModel.getPlayerDataMap().get(playerId));

                } else {

                    // Need to fetch player data.
                    playerChoicesToSync.add(playerId);
                }
            }

            // Fetch additional choice information.
            if (playerChoices.size() != playerChoiceIds.size()) {

                syncChoicesWithoutData(playerChoicesToSync);

            } else if (!playerChoices.equals(mCurrentPlayerChoices)) {

                updatePlayerChoices(playerChoices);
            }
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

        List<ObjectModelDraft.Pick> currentPicks = new ArrayList<ObjectModelDraft.Pick>();

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
     * Update round time remaining, whether or not that
     * timer is hidden, and if the choices view should be hidden.
     *
     * @param roundTimeRemaining Current round time remaining.
     * @param roundTimeRemainingHidden Round time remaining is hidden.
     * @param choicesViewHidden Choices view hidden.
     */
    private void updateRoundTimeRemaining(int roundTimeRemaining,
                                          boolean roundTimeRemainingHidden,
                                          boolean choicesViewHidden) {

        // Update round time remaining if needed.
        if (mRoundTimeRemaining != roundTimeRemaining) {
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

        // If we are still in the draft.
        if (mDraftModel.getCurrentRound() <= mDraftModel.getRounds() &&
                mCurrentPlayerChoicesShowTime != null && choicesViewHidden) {

            int secondsSinceChoicesShownTime = (int)(DateUtils
                    .getTimeSinceDateInGMTAsMilliseconds(mCurrentPlayerChoicesShowTime)) / 1000;

            // Keep choices showing for a bit longer
            // to fit the post view time window.
            if (secondsSinceChoicesShownTime < mDraftModel.getTimePerPostview()) {

                choicesViewHidden = false;
            }
        }

        // Update choices view hidden if needed.
        if (mChoicesViewHidden != choicesViewHidden) {
            mChoicesViewHidden = choicesViewHidden;

            if (mCallbacks != null) {
                mCallbacks.onChoicesViewHiddenChanged(mChoicesViewHidden);
            }
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
    private void updatePicks(List<ObjectModelDraft.Pick> currentPicks) {

        // If at least one pick and different than current picks.
        if (currentPicks.size() > 0 && !currentPicks.equals(mCurrentPicks)) {

            // Update current picks.
            mCurrentPicks = currentPicks;

            List<String> playerIds = new ArrayList<String>();
            List<String> userIds = new ArrayList<String>();

            // Serialize the player and user ids.
            for (ObjectModelDraft.Pick pick : currentPicks) {

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

        public void onUserSynced(String userId, String userName,
                int rating, int wins, int losses, int ties, String itemAvatarUrl);

        public void onDraftStateChanged(DraftState state);

        public void onRoundAndPositionChanged(String roundAndPosition);

        public void onPlayerChoicesChanged(
                List<String> playerIds,
                List<String> playerPhotoUrls,
                List<String> playerFullNames,
                List<String> playerPositions,
                List<String> playerOpponents);

        public void onPicksChanged(
                List<String> playerIds,
                List<String> userIds);

        public void onRoundTimeRemainingChanged(int roundTimeRemaining);
        public void onRoundTimeRemainingHiddenChanged(boolean roundTimeRemainingHidden);

        public void onChoicesViewHiddenChanged(boolean onChoicesViewHidden);
    }

    // endregion
}