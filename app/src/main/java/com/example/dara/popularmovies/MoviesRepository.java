package com.example.dara.popularmovies;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.dara.popularmovies.database.FavouritesDao;
import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;
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

    public LiveData<List<Movie>> getPopularMovies() {
        MoviesNetworkDataSource networkDataSource = InjectorUtils
                .provideNetworkDataSource(mContext);
        return networkDataSource.getPopularMovies();
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        MoviesNetworkDataSource networkDataSource = InjectorUtils
                .provideNetworkDataSource(mContext);
        return networkDataSource.getTopRatedMovies();
    }

    public LiveData<List<Review>> getReviews(Movie movie) {
        MoviesNetworkDataSource networkDataSource = InjectorUtils
                .provideNetworkDataSource(mContext);
        return networkDataSource.getReviews(movie);
    }

    public LiveData<List<Trailer>> getTrailers(Movie movie) {
        MoviesNetworkDataSource networkDataSource = InjectorUtils
                .provideNetworkDataSource(mContext);
        return networkDataSource.getTrailers(movie);
    }

    /**
     * Database related operations
     **/
    public LiveData<Movie> getMovieByTitle (String title) {
        return mFavouritesDao.getMovieByTitle(title);
    }

    //Add movie to favourites
    public void addToFavourites(Movie movie) {
        mFavouritesDao.addToFavourites(movie);

    }

    //Remove movie from favourites
    public void removeFromFavourites (Movie movie) {
        mFavouritesDao.removeFromFavourites(movie);
    }

    //Get all favourite movies
    public LiveData<List<Movie>> getFavouriteMovies () {
        return mFavouritesDao.getAllFavouriteMovies();
    }

}
