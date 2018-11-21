package com.example.dara.popularmovies.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.dara.popularmovies.AppExecutors;
import com.example.dara.popularmovies.database.Movie;
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

    //Getter method for fetched movies
    public MutableLiveData<Movie[]> getCurrentMovies() {
        return mMovies;
    }

    //Get movies data
    public void getMovies(String sort_by) {
        Log.d(LOG_TAG, "Fetch weather started");

        mExecutors.networkIO().execute(() -> {
            try {
                URL queryUrl = null;
                //Build the url based on the preferred sorting
                if (sort_by.equals("most_popular")) {
                    queryUrl = NetworkUtils.popularMoviesUrl();
                } else if (sort_by.equals("top_rated")) {
                    queryUrl = NetworkUtils.topRatedMoviesUrl();
                }

                //Get json response from url
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);

                //Parse json response into a list of movies
                List<Movie> movieResponse = MoviesJsonUtils.extractMoviesFromJson(jsonResponse);
                Log.d(LOG_TAG, "JSON Parsing finished");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
