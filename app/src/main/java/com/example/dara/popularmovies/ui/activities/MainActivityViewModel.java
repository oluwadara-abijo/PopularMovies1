package com.example.dara.popularmovies.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.Movie;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private final MoviesRepository mRepository;

    public MainActivityViewModel (MoviesRepository repository) {
        mRepository = repository;
    }

    public LiveData<List<Movie>> getPopularMovies () {
        return mRepository.getPopularMovies();
    }

    public LiveData<List<Movie>> getTopRatedMovies () {
        return mRepository.getTopRatedMovies();
    }

    public LiveData<List<Movie>> getFavouriteMovies () {
        return mRepository.getFavouriteMovies();
    }

}
