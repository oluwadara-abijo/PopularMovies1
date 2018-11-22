package com.example.dara.popularmovies.utilities;

import android.content.Context;

import com.example.dara.popularmovies.AppExecutors;
import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.network.MoviesNetworkDataSource;
import com.example.dara.popularmovies.ui.activities.MainViewModelFactory;
import com.example.dara.popularmovies.ui.detail.FavouritesViewModelFactory;
import com.example.dara.popularmovies.ui.detail.MainDetailViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    public static MoviesRepository provideRepository(Context context) {
        FavouritesDatabase database = FavouritesDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MoviesNetworkDataSource networkDataSource =
                MoviesNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return MoviesRepository.getInstance(database.favouritesDao(), networkDataSource, executors, context);
    }

    public static MoviesNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return MoviesNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static FavouritesViewModelFactory provideFavouritesViewModelFactory(Context context, String title) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new FavouritesViewModelFactory(repository, title);
    }

    public static MainDetailViewModelFactory provideMainDetailViewModelFactory(Context context, Movie movie) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainDetailViewModelFactory(repository, movie);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MoviesRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

}