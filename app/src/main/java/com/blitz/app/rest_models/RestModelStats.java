package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelStats extends RestModel {

    // region Member Variables
    // ============================================================================================================

    // Constant for unsupported stat.
    private static final String UNSUPPORTED = "Unsupported";

    @SerializedName("player_id") @Getter private String playerId;
    @SerializedName("type")      @Getter private String type;

    @SerializedName("value")     @Getter private float value;

    @SerializedName("week")      @Getter private int week;
    @SerializedName("year")      @Getter private int year;

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch stats for a given list of players.
     */
    public static void fetchStatsForPlayers(Activity activity, List<String> playerIds, int year, int week,
                                            @NonNull RestResults<RestModelStats> callback) {

        List<String> keys = new ArrayList<>(playerIds.size());

        for (String id : playerIds) {

            keys.add(id + "_" + year + "_" + week);
        }

        restAPI.stats_get(keys, "player_year_week_index", null, 100, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    @SuppressWarnings("unused")
    public String getTypeName() {

        switch (type) {
            case "PASS_YDS":
                return "Pass Yards";
            case "PASS_TDS":
                return "Pass TDs";
            case "PASS_INT":
                return "Interceptions";
            case "PASS_2PM":
                return "Pass 2pt Conversions";
            case "RUSH_YDS":
                return "Rush Yards";
            case "RUSH_TDS":
                return "Rush TDs";
            case "RUSH_2PM":
                return "Rush 2pt Conversions";
            case "REC_CAT":
                return "Receptions";
            case "REC_YDS":
                return "Reception Yards";
            case "REC_TDS":
                return "Reception TDs";
            case "REC_2PM":
                return "Rec 2pt Conversions";
            case "FG_MADE":
                return "FG Made";
            case "FG_MISS":
                return "FG Missed";
            case "XP_MADE":
                return "XP Made";
            case "XP_MISS":
                return "XP Missed";
            case "FUMBLES_LOST":
                return "Fumbles Lost";
            case "DEF_SACK":
                return "Sacks";
            case "DEF_INT":
                return "Interceptions";
            case "DEF_FUM_REC":
                return "Fumble Rec";
            case "DEF_TDS":
                return "Defense TDs";
            case "DEF_SAFE":
                return "Defense Safety";
            case "KICK_RET_TDS":
                return "Kick Return TDs";
            case "PUNT_RET_TDS":
                return "Punt Return TDs";
            case "DEF_PTS_ALLOW":
                return "Points Allowed";
        }

        return UNSUPPORTED;
    }

    @SuppressWarnings("unused")
    public float getTypePoints() {

        switch (type) {
            case "PASS_YDS":
                return 0.04f * value;
            case "PASS_TDS":
                return 6.0f * value;
            case "PASS_INT":
                return -2.0f * value;
            case "PASS_2PM":
                return 2.0f * value;
            case "RUSH_YDS":
                return 0.10f * value;
            case "RUSH_TDS":
                return 6.0f * value;
            case "RUSH_2PM":
                return 2.0f * value;
            case "REC_CAT":
                return 1.0f * value;
            case "REC_YDS":
                return 0.10f * value;
            case "REC_TDS":
                return 6.0f * value;
            case "REC_2PM":
                return 2.0f * value;
            case "FG_MADE":
                return 3.0f * value;
            case "FG_MISS":
                return -1.0f * value;
            case "XP_MADE":
                return 1.0f * value;
            case "XP_MISS":
                return -1.0f * value;
            case "FUMBLES_LOST":
                return -2.0f * value;
            case "DEF_SACK":
                return 1.0f * value;
            case "DEF_INT":
                return 2.0f * value;
            case "DEF_FUM_REC":
                return 2.0f * value;
            case "DEF_TDS":
                return 6.0f * value;
            case "DEF_SAFE":
                return 2.0f * value;
            case "KICK_RET_TDS":
                return 6.0f * value;
            case "PUNT_RET_TDS":
                return 6.0f * value;
            case "DEF_PTS_ALLOW":
                if (value >= 35.0f) {
                    return -4.0f;
                } else if (value >= 28.0f) {
                    return -1.0f;
                } else if (value >= 21.0f) {
                    return 0.f;
                } else if (value >= 14.0f) {
                    return 1.0f;
                } else if (value >= 7.0f) {
                    return 4.0f;
                } else if (value >= 1.0f) {
                    return 7.0f;
                } else {
                    return 10.f;
                }
            default:
                return 0.f * value;
        }
    }

    @SuppressWarnings("unused")
    public boolean isSupported() {

        return !UNSUPPORTED.equals(getTypeName());
    }

    // endregion
}
