package com.example.dara.popularmovies.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ReviewsLoader extends AsyncTaskLoader<List<Review>> {

    //Query url
    private final URL mQueryUrl;

    //List of movies
    private List<Review> mReviews;

    //Constructor
    public ReviewsLoader(@NonNull Context context, URL queryUrl) {
        super(context);
        this.mQueryUrl = queryUrl;
    }

    @Nullable
    @Override
    public List<Review> loadInBackground() {
        if (mQueryUrl == null) {
            return null;
        }
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(mQueryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MoviesJsonUtils.extractReviewsFromJson(jsonResponse);
    }

    @Override
    protected void onStartLoading() {
        if (mReviews != null) {
            deliverResult(mReviews);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable List<Review> data) {
        mReviews = data;
        super.deliverResult(data);
    }
}
