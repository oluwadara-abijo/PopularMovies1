package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.Movie;

public class MainDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesRepository mRepository;
    private final Movie mMovie;

    public MainDetailViewModelFactory(MoviesRepository repository, Movie movie) {
        this.mRepository = repository;
        this.mMovie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainDetailActivityViewModel(mRepository, mMovie);
    }
}
