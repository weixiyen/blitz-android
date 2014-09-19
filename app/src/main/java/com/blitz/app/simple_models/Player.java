package com.blitz.app.simple_models;

/**
 * Immutable model of Player (i.e. Football player with performance info, etc.)
 *
 * Created by Nate on 9/10/14.
 */
public class Player {
    private final String full_name;
    private final String mTeam;
    private final String mPosition;
    private final float  mScore;

    public Player(String name, String team, String position, float score) {
        full_name = name;
        mTeam = team;
        mPosition = position;
        mScore = score;
    }

    public String getFullName() {
        return full_name;
    }

    public String getTeamName() {
        return mTeam;
    }

    public float getScore() {
        return mScore;
    }

    public String getPosition() {
        return mPosition;
    }
}
