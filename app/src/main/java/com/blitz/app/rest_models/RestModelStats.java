package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
public class RestModelStats extends RestModel {

    // region Member Variables
    // ============================================================================================================
    @SuppressWarnings("unused") @SerializedName("player_id") private String mPlayerId;
    @SuppressWarnings("unused") @SerializedName("type")      private String mType;
    @SuppressWarnings("unused") @SerializedName("value")     private float mValue;
    @SuppressWarnings("unused") @SerializedName("week")      private int mWeek;
    @SuppressWarnings("unused") @SerializedName("year")      private int mYear;

    private static final String UNSUPPORTED = "Unsupported";

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch stats for a given list of players.
     */
    public static void fetchStatsForPlayers(Activity activity, List<String> playerIds, int year, int week,
                                            @NonNull RestResults<RestModelStats> callback) {

        List<String> keys = new ArrayList<>(playerIds.size());

        for (String id: playerIds) {

            keys.add(id + "_" + year + "_" + week);
        }

        mRestAPI.stats_get(keys, "player_year_week_index", null, 100, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    @SuppressWarnings("unused")
    public String getPlayerId() {

        return mPlayerId;
    }

    @SuppressWarnings("unused")
    public String getStatName() {

        switch (mType) {
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
    public float getPoints() {

        switch (mType) {
            case "PASS_YDS":
                return 0.04f * mValue;
            case "PASS_TDS":
                return 6.0f * mValue;
            case "PASS_INT":
                return -2.0f * mValue;
            case "PASS_2PM":
                return 2.0f * mValue;
            case "RUSH_YDS":
                return 0.10f * mValue;
            case "RUSH_TDS":
                return 6.0f * mValue;
            case "RUSH_2PM":
                return 2.0f * mValue;
            case "REC_CAT":
                return 1.0f * mValue;
            case "REC_YDS":
                return 0.10f * mValue;
            case "REC_TDS":
                return 6.0f * mValue;
            case "REC_2PM":
                return 2.0f * mValue;
            case "FG_MADE":
                return 3.0f * mValue;
            case "FG_MISS":
                return -1.0f * mValue;
            case "XP_MADE":
                return 1.0f * mValue;
            case "XP_MISS":
                return -1.0f * mValue;
            case "FUMBLES_LOST":
                return -2.0f * mValue;
            case "DEF_SACK":
                return 1.0f * mValue;
            case "DEF_INT":
                return 2.0f * mValue;
            case "DEF_FUM_REC":
                return 2.0f * mValue;
            case "DEF_TDS":
                return 6.0f * mValue;
            case "DEF_SAFE":
                return 2.0f * mValue;
            case "KICK_RET_TDS":
                return 6.0f * mValue;
            case "PUNT_RET_TDS":
                return 6.0f * mValue;
            case "DEF_PTS_ALLOW":
                if (mValue >= 35.0f) {
                    return -4.0f;
                } else if (mValue >= 28.0f) {
                    return -1.0f;
                } else if (mValue >= 21.0f) {
                    return 0.f;
                } else if (mValue >= 14.0f) {
                    return 1.0f;
                } else if (mValue >= 7.0f) {
                    return 4.0f;
                } else if (mValue >= 1.0f) {
                    return 7.0f;
                } else {
                    return 10.f;
                }
            default:
                return 0.f * mValue;
        }
    }

    @SuppressWarnings("unused")
    public float getValue() {

        return mValue;
    }

    @SuppressWarnings("unused")
    public boolean isSupported() {

        return !UNSUPPORTED.equals(getStatName());
    }

    // endregion
}
