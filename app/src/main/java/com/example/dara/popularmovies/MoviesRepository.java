package com.example.dara.popularmovies;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.dara.popularmovies.database.FavouritesDao;
import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.network.MoviesNetworkDataSource;
import com.example.dara.popularmovies.utilities.InjectorUtils;

import java.util.List;

/**
 * Handles data operations in Popular Movies. Acts as mediator between FavouritesDao
 * and WeatherDataSource
 */
public class MoviesRepository {
    private static final String LOG_TAG = MoviesRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesRepository sInstance;
    private final FavouritesDao mFavouritesDao;
    private final MoviesNetworkDataSource mMoviesNetworkDataSource;
    private final AppExecutors mExecutors;
    private Context mContext;
    private boolean mInitialized = false;

    //Constructor
    private MoviesRepository(FavouritesDao favouritesDao,
                             MoviesNetworkDataSource moviesNetworkDataSource,
                             AppExecutors executors, Context context) {
        mFavouritesDao = favouritesDao;
        mMoviesNetworkDataSource = moviesNetworkDataSource;
        mExecutors = executors;
        mContext = context;

    }

    public synchronized static MoviesRepository getInstance(
            FavouritesDao weatherDao, MoviesNetworkDataSource weatherNetworkDataSource,
            AppExecutors executors, Context context) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesRepository(weatherDao, weatherNetworkDataSource,
                        executors, context);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    public synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        // COMPLETED Finish this method when instructed
        getMovies("most_popular");
    }

    public void addToFavourites(Movie movie) {
        LiveData<List<Movie>> favMovies = mFavouritesDao.getAllFavouriteMovies();
        favMovies.observeForever(favouriteMovies -> mExecutors.diskIO().execute(() -> {
            mFavouritesDao.addToFavourites (movie);
            Log.d(LOG_TAG, "New values inserted");
        }));

    }

    private void getMovies(String sort_by) {
        MoviesNetworkDataSource networkDataSource = InjectorUtils
                .provideNetworkDataSource(mContext);
        networkDataSource.getMovies(sort_by);
    }
}
