package com.blitz.app.object_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Miguel on 9/27/2014. Copyright 2014 Blitz Studios
 */
public class ObjectModelPlayer extends ObjectModel {

    @SuppressWarnings("unused") @SerializedName("id")
    private String id;
    @SuppressWarnings("unused") @SerializedName("full_name")
    private String full_name;
    @SuppressWarnings("unused") @SerializedName("first_name")
    private String first_name;
    @SuppressWarnings("unused") @SerializedName("last_name")
    private String last_name;
    @SuppressWarnings("unused") @SerializedName("team")
    private String team;
    @SuppressWarnings("unused") @SerializedName("position")
    private String position;
    @SuppressWarnings("unused") @SerializedName("opponent")
    private String opponent;

    @SuppressWarnings("unused") @SerializedName("is_home_team")
    private boolean mIsHomeTeam;
}
