package com.example.dara.popularmovies.utilities;

import android.content.Context;

import com.example.dara.popularmovies.AppExecutors;
import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.network.MoviesNetworkDataSource;

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

//    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Date date) {
//        SunshineRepository repository = provideRepository(context.getApplicationContext());
//        return new DetailViewModelFactory(repository, date);
//    }
//
//    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
//        SunshineRepository repository = provideRepository(context.getApplicationContext());
//        return new MainViewModelFactory(repository);
//    }

}