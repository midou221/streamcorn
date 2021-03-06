package com.materight.streamcorn.tmdb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TmdbEpisode {
    @Expose
    @SerializedName("episode_number")
    private int number;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("overview")
    private String overview;

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

}