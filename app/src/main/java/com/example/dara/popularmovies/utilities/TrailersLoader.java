package com.example.dara.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.dara.popularmovies.model.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TrailersLoader extends AsyncTaskLoader<List<Trailer>> {

    //Query url
    private final URL mQueryUrl;

    //List of movies
    private List<Trailer> mTrailers;

    //Constructor
    public TrailersLoader(@NonNull Context context, URL queryUrl) {
        super(context);
        this.mQueryUrl = queryUrl;
    }

    @Nullable
    @Override
    public List<Trailer> loadInBackground() {
        if (mQueryUrl == null) {
            return null;
        }
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(mQueryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MoviesJsonUtils.extractTrailersFromJson(jsonResponse);
    }

    @Override
    protected void onStartLoading() {
        if (mTrailers != null) {
            deliverResult(mTrailers);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable List<Trailer> data) {
        mTrailers = data;
        super.deliverResult(data);
    }
}
