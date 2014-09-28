package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Miguel on 9/27/2014. Copyright 2014 Blitz Studios
 */
public class ObjectModelPlayer extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    @SuppressWarnings("unused") @SerializedName("id")
    private String mId;
    @SuppressWarnings("unused") @SerializedName("full_name")
    private String mFullName;
    @SuppressWarnings("unused") @SerializedName("first_name")
    private String mFirstName;
    @SuppressWarnings("unused") @SerializedName("last_name")
    private String mLastName;
    @SuppressWarnings("unused") @SerializedName("team")
    private String mTeam;
    @SuppressWarnings("unused") @SerializedName("position")
    private String mPosition;
    @SuppressWarnings("unused") @SerializedName("opponent")
    private String mOpponent;

    @SuppressWarnings("unused") @SerializedName("is_home_team")
    private boolean mIsHomeTeam;

    // endregion

    // region REST Methods
    // =============================================================================================

    @SuppressWarnings("unused")
    public static void fetchPlayers(Activity activity,
                                    ArrayList<String> playerIds,
                                    final CallbackPlayers callback) {

    }

    /**
     * Fetch an NFL player.
     *
     * @param activity Used for dialogs.
     * @param playerId Player id.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void fetchPlayer(Activity activity,
                                   String playerId,
                                   final CallbackPlayer callback) {

        if (playerId == null) {
            return;
        }

        RestAPICallback<RestAPIResult<ObjectModelPlayer>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelPlayer>>(activity) {

                    @Override
                    public void success(RestAPIResult<ObjectModelPlayer> jsonObject) {

                        // Now left queue.
                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResult());
                        }
                    }
                };

        // Make api call.
        mRestAPI.nfl_player_get(playerId, operation);
    }

    @SuppressWarnings("unused")
    public static void fetchPlayerFromCometJson(Activity activity,
                                                JsonObject cometJson,
                                                final CallbackPlayer callback) {

    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch player id.
     *
     * @return Player id.
     */
    @SuppressWarnings("unused")
    public String getId() {

        return mId;
    }

    /**
     * Fetch full name of player or team.
     *
     * @return Full name.
     */
    @SuppressWarnings("unused")
    public String getFullName() {

        return mFullName;
    }

    /**
     * Fetch first name.
     *
     * @return First name.
     */
    @SuppressWarnings("unused")
    public String getFirstName() {

        return mFirstName;
    }

    /**
     * Fetch last name.
     *
     * @return Last name.
     */
    @SuppressWarnings("unused")
    public String getLastName() {

        return mLastName;
    }

    /**
     * Fetch team name.
     *
     * @return Team name.
     */
    @SuppressWarnings("unused")
    public String getTeam() {
        return mTeam;
    }

    /**
     * Fetch player position.
     *
     * @return Player position.
     */
    @SuppressWarnings("unused")
    public String getPosition() {

        return mPosition;
    }

    /**
     * Fetch opponent.
     *
     * @return Opponent.
     */
    @SuppressWarnings("unused")
    public String getOpponent() {

        return mOpponent;
    }

    /**
     * Fetch url of player photo.
     *
     * @return Player photo url.
     */
    @SuppressWarnings("unused")
    public String getPhotoUrl() {

        return null; // TODO: Implement
    }

    /**
     * Is this a home team.
     *
     * @return Is home team.
     */
    @SuppressWarnings("unused")
    public boolean getIsHomeTeam() {

        return mIsHomeTeam;
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface CallbackPlayer {

        public void onSuccess(ObjectModelPlayer draft);
    }

    public interface CallbackPlayers {

        public void onSuccess(ObjectModelPlayer draft);
    }

    // endregion
}
