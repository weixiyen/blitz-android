package com.blitz.app.simple_models;

/**
 * Immutable model of Player (i.e. Football player with performance info, etc.)
 *
 * Created by Nate on 9/10/14.
 */
public class Player {
    private final String full_name;
    private final String team;
    private final String position;
    private final String id;

    public Player(String id, String name, String team, String position, float score) {
        this.id = id;
        full_name = name;
        this.team = team;
        this.position = position;
    }

    public String getFullName() {
        return full_name;
    }

    public String getTeamName() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }
}
