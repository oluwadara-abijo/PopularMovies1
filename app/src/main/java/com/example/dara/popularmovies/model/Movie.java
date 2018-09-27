package com.example.dara.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    //Movie id
    private final int mMovieId;

    //Title of movie
    private final String mTitle;

    //Movie poster path
    private final String mPosterUrl;

    //Movie backdrop path
    private final String mBackdropUrl;

    //Movie overview
    private final String mOverview;

    //Movie release date
    private final String mReleaseDate;

    //Movie vote average
    private final int mVoteAverage;

    //Constructor which creates an object of the Movies object
    public Movie(int movieId, String title, String posterUrl, String backdropUrl, String overview, String releaseDate,
                 int voteAverage) {
        mMovieId = movieId;
        mTitle = title;
        mPosterUrl = posterUrl;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mBackdropUrl = backdropUrl;
    }

    private Movie(Parcel in) {
        mMovieId = in.readInt();
        mTitle = in.readString();
        mPosterUrl = in.readString();
        mBackdropUrl = in.readString();
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

    //Getter methods for fields
    public int getMovieId () { return mMovieId;}
    public String getTitle() {
        return mTitle;
    }
    public String getPosterUrl() {
        return mPosterUrl;
    }
    public String getPBackdropUrl() {
        return mBackdropUrl;
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
        parcel.writeInt(mMovieId);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterUrl);
        parcel.writeString(mBackdropUrl);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeInt(mVoteAverage);

    }
}
