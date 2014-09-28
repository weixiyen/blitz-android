package com.blitz.app.simple_models;

import com.blitz.app.object_models.ObjectModelPlayer;

/**
 * Created by spiff on 9/21/14.
 */
public class WeeklyStat {


    private final ObjectModelPlayer mPlayer;
    private final Game mGame;
    private final float mScore;

    public WeeklyStat(ObjectModelPlayer player, Game game, float score) {
        this.mPlayer = player;
        this.mGame = game;
        this.mScore = score;
    }

    public ObjectModelPlayer getPlayer() {
        return mPlayer;
    }

    public Game getGame() {
        return mGame;
    }

    public float getScore() {
        return mScore;
    }
}
