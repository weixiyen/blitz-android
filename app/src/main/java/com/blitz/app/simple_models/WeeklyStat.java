package com.blitz.app.simple_models;

import com.blitz.app.rest_models.RestModelPlayer;

/**
 * Created by spiff on 9/21/14.
 */
public class WeeklyStat {


    private final RestModelPlayer mPlayer;
    private final Game mGame;
    private final float mScore;

    public WeeklyStat(RestModelPlayer player, Game game, float score) {
        this.mPlayer = player;
        this.mGame = game;
        this.mScore = score;
    }

    public RestModelPlayer getPlayer() {
        return mPlayer;
    }

    public Game getGame() {
        return mGame;
    }

    public float getScore() {
        return mScore;
    }
}
