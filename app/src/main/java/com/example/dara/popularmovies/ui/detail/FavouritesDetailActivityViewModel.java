package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.example.dara.popularmovies.database.Movie;

public class FavouritesDetailActivityViewModel extends ViewModel {
    //Current favourite movie
    private Movie mMovie;

    //Empty constructor
    public FavouritesDetailActivityViewModel () {}

    //Getter and setter

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
    }
}
