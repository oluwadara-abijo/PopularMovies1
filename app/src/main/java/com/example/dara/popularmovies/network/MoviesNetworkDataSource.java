package com.example.dara.popularmovies.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.dara.popularmovies.AppExecutors;
import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;
import com.example.dara.popularmovies.utilities.MoviesJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MoviesNetworkDataSource {
    private static final String LOG_TAG = MoviesNetworkDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesNetworkDataSource sInstance;
    private final Context mContext;
    private final AppExecutors mExecutors;

    //LiveData storing favourite movies
    private final MutableLiveData<Movie[]> mMovies;

    //Constructor
    private MoviesNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mMovies = new MutableLiveData<>();
    }

    //Singleton for this class
    public static MoviesNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    //Get movies data
    public LiveData<List<Movie>> getPopularMovies() {
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        mExecutors.networkIO().execute(() -> {
            try {
                //Build the url based on the preferred sorting
                URL queryUrl = NetworkUtils.popularMoviesUrl();

                //Get json response from url
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

                //Parse json response into a list of movies
                List<Movie> movieResponse = MoviesJsonUtils.extractMoviesFromJson(jsonResponse);
                mutableLiveData.postValue(movieResponse);
                Log.d(LOG_TAG, "JSON Parsing finished");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mutableLiveData;
    }

    //Get movies data
    public LiveData<List<Movie>> getTopRatedMovies() {
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        mExecutors.networkIO().execute(() -> {
            try {
                //Build the url based on the preferred sorting
                URL queryUrl = NetworkUtils.topRatedMoviesUrl();

                //Get json response from url
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

                //Parse json response into a list of movies
                List<Movie> movieResponse = MoviesJsonUtils.extractMoviesFromJson(jsonResponse);
                mutableLiveData.postValue(movieResponse);
                Log.d(LOG_TAG, "JSON Parsing finished");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mutableLiveData;
    }

    //Get movie reviews
    public LiveData<List<Review>> getReviews(Movie movie) {
        MutableLiveData<List<Review>> mutableLiveData = new MutableLiveData<>();

        mExecutors.networkIO().execute(() -> {
            try {
                //Build the url for the movie review endpoint
                URL queryUrl = NetworkUtils.reviewsUrl(movie);

                //Get json response from url
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

                //Parse json response into a list of movies
                List<Review> movieReviews = MoviesJsonUtils.extractReviewsFromJson(jsonResponse);
                mutableLiveData.postValue(movieReviews);
                Log.d(LOG_TAG, "JSON Parsing finished");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mutableLiveData;
    }

    //Get movie trailers
    public LiveData<List<Trailer>> getTrailers(Movie movie) {
        MutableLiveData<List<Trailer>> mutableLiveData = new MutableLiveData<>();

        mExecutors.networkIO().execute(() -> {
            try {
                //Build the url for the movie review endpoint
                URL queryUrl = NetworkUtils.trailersUrl(movie);

                //Get json response from url
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

                //Parse json response into a list of movies
                List<Trailer> movieReviews = MoviesJsonUtils.extractTrailersFromJson(jsonResponse);
                mutableLiveData.postValue(movieReviews);
                Log.d(LOG_TAG, "JSON Parsing finished");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mutableLiveData;
    }

}
