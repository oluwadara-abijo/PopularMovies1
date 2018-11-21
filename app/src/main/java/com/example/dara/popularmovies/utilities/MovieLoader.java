package com.example.dara.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    //Query url
    private final URL mQueryUrl;

    //List of movies
    private List<Movie> mMovies;

    //Constructor
    public MovieLoader(@NonNull Context context, URL queryUrl) {
        super(context);
        this.mQueryUrl = queryUrl;
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mQueryUrl == null) {
            return null;
        }
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(mQueryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MoviesJsonUtils.extractMoviesFromJson(jsonResponse);
    }

    @Override
    protected void onStartLoading() {
        if (mMovies != null) {
            deliverResult(mMovies);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable List<Movie> data) {
        mMovies = data;
        super.deliverResult(data);
    }
}
