package com.blitz.app.view_models;

/**
 * An immutable object model for two-player head to head drafts.
 *
 * Created by Nate on 9/7/14.
 */
public class MatchInfo {

    private final String mPlayer1Name;
    private final float mPlayer1Score;
    private final String mPlayer2Name;
    private final float mPlayer2Score;
    private final String mStatus;

    public MatchInfo(String player1Name, float player1Score,
                     String player2Name, float player2Score,
                     String status) {
        mPlayer1Name  = player1Name;
        mPlayer1Score = player1Score;
        mPlayer2Name  = player2Name;
        mPlayer2Score = player2Score;
        mStatus       = status;
    }

    public String getPlayer1Name() {
        return mPlayer1Name;
    }

    public float getPlayer1Score() {
        return mPlayer1Score;
    }

    public String getPlayer2Name() {
        return mPlayer2Name;
    }

    public float getPlayer2Score() {
        return mPlayer2Score;
    }

    public String getStatus() {
        return mStatus;
    }
}
