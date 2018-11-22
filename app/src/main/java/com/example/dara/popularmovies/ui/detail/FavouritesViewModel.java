package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.Movie;

import java.util.List;

public class FavouritesViewModel extends ViewModel {
    //Current favourite movie
    private final LiveData<Movie> mMovie;

    private final MoviesRepository mRepository;
    //Movie title
    private String mTitle;

    //Constructor
    public FavouritesViewModel(MoviesRepository repository, String title) {
        mTitle = title;
        mRepository = repository;
        mMovie = mRepository.getMovieByTitle(mTitle);
    }

    //Getter method
    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public void addToFavourites (Movie movie) {
        mRepository.addToFavourites(movie);
    }

    public void removeFromFavourites (Movie movie) {
        mRepository.removeFromFavourites(movie);
    }

}
