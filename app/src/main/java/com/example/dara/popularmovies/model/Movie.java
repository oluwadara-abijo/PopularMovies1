package com.example.dara.popularmovies.model;

public class Movie {

    //Title of movie
    private String mTitle;

    //Movie poster path
    private String mPosterUrl;

    //Movie overview
    private String mOverview;

    //Movie release date
    private String mReleaseDate;

    //Movie vote average
    private int mVoteAverage;

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

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPosterUrl(String mPosterPath) {
        this.mPosterUrl = mPosterPath;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }
}
