package com.example.dara.popularmovies.network;

import android.support.annotation.NonNull;

import com.example.dara.popularmovies.database.Movie;

/**
 * Movie response from The MovieDb
 */
public class MovieResponse {
    @NonNull
    private final Movie[] mMovies;

    public MovieResponse(@NonNull final Movie[] movies) {
        mMovies = movies;
    }

    public Movie[] getWeatherForecast() {
        return mMovies;
    }

}
