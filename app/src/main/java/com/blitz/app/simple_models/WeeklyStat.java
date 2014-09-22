package com.blitz.app.simple_models;

/**
 * Created by spiff on 9/21/14.
 */
public class WeeklyStat {


    private final Player mPlayer;
    private final Game mGame;
    private final float mScore;

    public WeeklyStat(Player player, Game game, float score) {
        this.mPlayer = player;
        this.mGame = game;
        this.mScore = score;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Game getGame() {
        return mGame;
    }

    public float getScore() {
        return mScore;
    }
}
