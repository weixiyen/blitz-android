package com.blitz.app.simple_models;

/**
 * Created by Nate on 9/21/14.
 */
public class Game {

    private String home_team;
    private String away_team;
    private int score_home;
    private int score_away;
    private String status;

    public String getHomeTeamName() {
        return home_team;
    }

    public String getAwayTeamName() {
        return away_team;
    }

    public int getHomeTeamScore() {
        return  score_home;
    }

    public int getAwayTeamScore() {
        return score_away;
    }

    public GameStatus getStatus() {
        if(status == GameStatus.FINAL.name()) {
            return GameStatus.FINAL;
        } else if(status == GameStatus.IN_PROGRESS.name()) {
            return GameStatus.IN_PROGRESS;
        } else {
            return GameStatus.PREGAME;
        }
    }


}
