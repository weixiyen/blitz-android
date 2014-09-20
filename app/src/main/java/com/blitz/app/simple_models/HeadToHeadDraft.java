package com.blitz.app.simple_models;

import java.util.ArrayList;
import java.util.List;

/**
 * An immutable model for two-player head to head drafts.
 *
 * Created by Nate on 9/7/14.
 */
public class HeadToHeadDraft {

    private final String mPlayer1Name;
    private final float  mPlayer1Score;
    private final int    mPlayer1RatingChange;
    private final List<String> mPlayer1Picks;

    private final String mPlayer2Name;
    private final float  mPlayer2Score;
    private final int    mPlayer2RatingChange;
    private final List<String> mPlayer2Picks;

    private final String mStatus;

    public HeadToHeadDraft(String player1Name, List<String> player1Picks, float player1Score, int player1RatingChange,
                           String player2Name, List<String> player2Picks, float player2Score, int player2RatingChange,
                           String status) {

        mPlayer1Name  = player1Name;
        mPlayer1Score = player1Score;
        mPlayer1Picks = player1Picks;
        mPlayer1RatingChange = player1RatingChange;


        mPlayer2Name  = player2Name;
        mPlayer2Score = player2Score;
        mPlayer2Picks = player2Picks;
        mPlayer2RatingChange = player2RatingChange;

        mStatus       = status;
    }

    public String getPlayer1Name() {
        return mPlayer1Name;
    }

    public float getPlayer1Score() {
        return mPlayer1Score;
    }

    public int getPlayer1RatingChange() {
        return mPlayer1RatingChange;
    }

    public int getPlayer2RatingChange() {
        return mPlayer2RatingChange;
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

    public List<String> getPlayer1Picks() {
        return mPlayer1Picks;
    }

    public List<String> getPlayer2Picks() {
        return mPlayer2Picks;
    }
}
