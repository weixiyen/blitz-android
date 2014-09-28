package com.blitz.app.simple_models;

/**
 * Created by Nate on 9/22/14.
 */
public class Stat {

    private String player_id;
    private String type;
    private float value;
    private int week;
    private int year;

    public String getPlayerId() {
        return player_id;
    }

    public String getStatName() {
        
            if (type.equals("PASS_YDS")) {
                return "Pass Yards";
            } else if (type.equals("PASS_TDS")) {
                return "Pass TDs";
            } else if (type.equals("PASS_INT")) {
                return "Interceptions";
            } else if (type.equals("PASS_2PM")) {
                return "Pass 2pt Conversions";
            } else if (type.equals("RUSH_YDS")) {
                return "Rush Yards";
            } else if (type.equals("RUSH_TDS")) {
                return "Rush TDs";
            } else if (type.equals("RUSH_2PM")) {
                return "Rush 2pt Conversions";
            } else if (type.equals("REC_CAT")) {
                return "Receptions";
            } else if (type.equals("REC_YDS")) {
                return "Reception Yards";
            } else if (type.equals("REC_TDS")) {
                return "Reception TDs";
            } else if (type.equals("REC_2PM")) {
                return "Rec 2pt Conversions";
            } else if (type.equals("FG_MADE")) {
                return "FG Made";
            } else if (type.equals("FG_MISS")) {
                return "FG Missed";
            } else if (type.equals("XP_MADE")) {
                return "XP Made";
            } else if (type.equals("XP_MISS")) {
                return "XP Missed";
            } else if (type.equals("FUMBLES_LOST")) {
                return "Fumbles Lost";
            } else if (type.equals("DEF_SACK")) {
                return "Sacks";
            } else if (type.equals("DEF_INT")) {
                return "Interceptions";
            } else if (type.equals("DEF_FUM_REC")) {
                return "Fumble Rec";
            } else if (type.equals("DEF_TDS")) {
                return "Defense TDs";
            } else if (type.equals("DEF_SAFE")) {
                return "Defense Safety";
            } else if (type.equals("KICK_RET_TDS")) {
                return "Kick Return TDs";
            } else if (type.equals("PUNT_RET_TDS")) {
                return "Punt Return TDs";
            } else if (type.equals("DEF_PTS_ALLOW")) {
                return "Points Allowed";
            }

            return "Unsupported";

    }

    public float getPoints() {
        if (type.equals("PASS_YDS")) {
            return 0.04f * value;
        } else if (type.equals("PASS_TDS")) {
            return 6.0f * value;
        } else if (type.equals("PASS_INT")) {
            return -2.0f * value;
        } else if (type.equals("PASS_2PM")) {
            return 2.0f * value;
        } else if (type.equals("RUSH_YDS")) {
            return 0.10f * value;
        } else if (type.equals("RUSH_TDS")) {
            return 6.0f * value;
        } else if (type.equals("RUSH_2PM")) {
            return 2.0f * value;
        } else if (type.equals("REC_CAT")) {
            return 1.0f * value;
        } else if (type.equals("REC_YDS")) {
            return 0.10f * value;
        } else if (type.equals("REC_TDS")) {
            return 6.0f * value;
        } else if (type.equals("REC_2PM")) {
            return 2.0f * value;
        } else if (type.equals("FG_MADE")) {
            return 3.0f * value;
        } else if (type.equals("FG_MISS")) {
            return -1.0f * value;
        } else if (type.equals("XP_MADE")) {
            return 1.0f * value;
        } else if (type.equals("XP_MISS")) {
            return -1.0f * value;
        } else if (type.equals("FUMBLES_LOST")) {
            return -2.0f * value;
        } else if (type.equals("DEF_SACK")) {
            return 1.0f * value;
        } else if (type.equals("DEF_INT")) {
            return 2.0f * value;
        } else if (type.equals("DEF_FUM_REC")) {
            return 2.0f * value;
        } else if (type.equals("DEF_TDS")) {
            return 6.0f * value;
        } else if (type.equals("DEF_SAFE")) {
            return 2.0f * value;
        } else if (type.equals("KICK_RET_TDS")) {
            return 6.0f * value;
        } else if (type.equals("PUNT_RET_TDS")) {
            return 6.0f * value;
        } else if (type.equals("DEF_PTS_ALLOW")) {
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
        } else {
            return 0.f * value;
        }
    }

    public float getValue() {
        return value;
    }
}
