package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.json.JsonHelper;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by Miguel on 9/27/2014. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelPlayer extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("id")           @Getter private String id;
    @SerializedName("full_name")    @Getter private String fullName;
    @SerializedName("first_name")   @Getter private String firstName;
    @SerializedName("last_name")    @Getter private String lastName;
    @SerializedName("team")         @Getter private String team;
    @SerializedName("position")     @Getter private String position;
    @SerializedName("opponent")     @Getter private String opponent;
    @SerializedName("abbr")         @Getter private String teamAbbreviation;
    @SerializedName("is_home_team") @Getter private boolean isHomeTeam;

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch NFL Players.
     */
    @SuppressWarnings("unused")
    public static void fetchPlayers(Activity activity,
                                    @NonNull List<String> playerIds,
                                    @NonNull RestResults<RestModelPlayer> callback) {

        restAPI.nfl_players_get(playerIds, "id", new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
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
                                   @NonNull String playerId,
                                   @NonNull RestResult<RestModelPlayer> callback) {

        restAPI.nfl_player_get(playerId, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Manually parse a JsonObject into
     * a player object.  Should only ever really
     * happen if comet send the client json.
     *
     * @param cometJson Json representing a player object.
     *
     * @return Player object.
     */
    @SuppressWarnings("unused")
    public static RestModelPlayer fetchPlayer(JsonObject cometJson) {

        RestModelPlayer player = new RestModelPlayer();

        player.id         = JsonHelper.parseString(cometJson.get("id"));
        player.opponent   = JsonHelper.parseString(cometJson.get("opponent"));
        player.position   = JsonHelper.parseString(cometJson.get("position"));
        player.team       = JsonHelper.parseString(cometJson.get("team"));
        player.fullName   = JsonHelper.parseString(cometJson.get("full_name"));
        player.isHomeTeam = JsonHelper.parseBool(cometJson.get("is_home_team"));

        // Team abbreviation.
        player.teamAbbreviation = JsonHelper.parseString(cometJson.get("abbr"));

        return player;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch url of player photo.
     */
    @SuppressWarnings("unused")
    public String getPhotoUrl() {

        String baseUrl = "players/";

        if (teamAbbreviation != null) {

            // Url for teams.
            return baseUrl + "def/" + teamAbbreviation.toLowerCase() + ".jpg";
        }

        if (fullName != null && position != null && team != null) {

            // Fetch photo url components.
            String componentName = fullName.replace(" ", "_").replace(".", "").toLowerCase();
            String componentPosition = position.toLowerCase();
            String componentTeam = team.toLowerCase();

            // Construct path.
            String path = componentName + "_" + componentPosition + "_" + componentTeam;

            return baseUrl + "off/" + path + ".jpg";
        }

        return null;
    }

    // endregion
}