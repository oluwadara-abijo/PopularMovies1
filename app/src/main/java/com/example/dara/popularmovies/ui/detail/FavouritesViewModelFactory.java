package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.dara.popularmovies.MoviesRepository;

public class FavouritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesRepository mRepository;
    private final String mTitle;

    public FavouritesViewModelFactory (MoviesRepository repository, String title) {
        this.mRepository = repository;
        this.mTitle = title;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavouritesViewModel(mRepository, mTitle);
    }
}
