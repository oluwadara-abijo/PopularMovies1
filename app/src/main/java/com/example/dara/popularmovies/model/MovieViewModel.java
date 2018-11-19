package com.example.dara.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.database.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> favMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        FavouritesDatabase database = FavouritesDatabase.getInstance(this.getApplication());
        favMovies = database.favouritesDao().getAllFavouriteMovies();
    }

    public LiveData<List<Movie>> getFavMovies() {
        return favMovies;
    }
}
