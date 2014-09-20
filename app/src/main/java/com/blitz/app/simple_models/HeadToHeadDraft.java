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
    private final float mPlayer1Score;
    private final String mPlayer2Name;
    private final float mPlayer2Score;
    private final List<String> picks;
    private final String mStatus;

    public HeadToHeadDraft(String player1Name, List<String> player1Picks, float player1Score,
                           String player2Name, List<String> player2Picks, float player2Score,
                           String status) {
        mPlayer1Name  = player1Name;
        mPlayer1Score = player1Score;
        mPlayer2Name  = player2Name;
        mPlayer2Score = player2Score;

        mStatus       = status;

        player1Picks.addAll(player2Picks);
        picks = player1Picks;
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

    public List<String> getPlayer1Picks() {
        List<String> p1Picks = new ArrayList<String>();
        for(int i=0; i < picks.size(); i+=2) {
            p1Picks.add(picks.get(i));
        }
        return p1Picks;
    }

    public List<String> getPlayer2Picks() {
        List<String> p2Picks = new ArrayList<String>();
        for(int i=1; i < picks.size(); i+=2) {
            p2Picks.add(picks.get(i));
        }
        return p2Picks;
    }
}
