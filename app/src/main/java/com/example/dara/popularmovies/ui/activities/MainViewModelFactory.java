package com.example.dara.popularmovies.ui.activities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.dara.popularmovies.MoviesRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesRepository mRepository;

    public MainViewModelFactory(MoviesRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
