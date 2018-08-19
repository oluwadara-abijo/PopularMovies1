package com.example.dara.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

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
    public Movie(String title, String posterUrl, String overview, String releaseDate,
                 int voteAverage) {
        mTitle = title;
        mPosterUrl = posterUrl;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    private Movie(Parcel in) {
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPosterUrl);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeInt(mVoteAverage);

    }
}
