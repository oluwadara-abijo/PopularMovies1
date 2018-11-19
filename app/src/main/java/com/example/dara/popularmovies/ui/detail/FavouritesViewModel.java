package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dara.popularmovies.database.Movie;

public class FavouritesViewModel extends ViewModel {
    //Current favourite movie
    private MutableLiveData<Movie> mMovie;

    //Constructor
    public FavouritesViewModel() {
        mMovie = new MutableLiveData<>();
    }

    //Getter and setter
    public MutableLiveData<Movie> getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        mMovie.postValue(movie);
    }
}
