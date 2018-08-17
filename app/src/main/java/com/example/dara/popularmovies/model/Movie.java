package com.example.dara.popularmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {

    //Title of movie
    private final String mTitle;

    //Movie poster path
    private final String mPosterUrl;

    //Movie overview
    private final String mOverview;

    //Movie release date
    private final String mReleaseDate;

    //Movie vote average
    private final int mVoteAverage;

    //Constructor which creates an object of the Movies object
     public Movie (String title, String posterUrl, String overview, String releaseDate,
                  int voteAverage) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }
}
