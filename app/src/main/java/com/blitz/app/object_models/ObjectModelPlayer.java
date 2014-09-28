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

    @SuppressWarnings("unused") @SerializedName("id")
    private String id;
    @SuppressWarnings("unused") @SerializedName("full_name")
    private String full_name;
    @SuppressWarnings("unused") @SerializedName("first_name")
    private String first_name;
    @SuppressWarnings("unused") @SerializedName("last_name")
    private String last_name;
    @SuppressWarnings("unused") @SerializedName("team")
    private String team;
    @SuppressWarnings("unused") @SerializedName("position")
    private String position;
    @SuppressWarnings("unused") @SerializedName("opponent")
    private String opponent;

    @SuppressWarnings("unused") @SerializedName("is_home_team")
    private boolean mIsHomeTeam;

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

    @SuppressWarnings("unused")
    public String fetchPhotoUrl() {

        return null;
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
