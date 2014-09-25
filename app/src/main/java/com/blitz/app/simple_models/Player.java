package com.blitz.app.simple_models;

/**
 * Immutable model of Player (i.e. Football player with performance info, etc.)
 *
 * Created by Nate on 9/10/14.
 */
public class Player {
    private final String full_name;
    private final String first_name;
    private final String last_name;
    private final String team;
    private final String position;
    private final String id;

    public Player(String id, String firstName, String lastName, String team, String position, float score) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        full_name = firstName + " " + lastName;
        this.team = team;
        this.position = position;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
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
