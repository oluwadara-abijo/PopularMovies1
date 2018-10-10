package com.example.dara.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.dara.popularmovies.BuildConfig;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.Trailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY_VALUE = BuildConfig.ApiKey;

    //Query parameter strings
    private static final String API_KEY = "api_key";

    /**
     * Build URL for most popular endpoint
     */
    public static URL popularMoviesUrl () {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath("popular")
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(">>>", "URL" + queryUrl);

        return queryUrl;

    }

    /**
     * RBuild URL for top rated endpoint
     */
    public static URL topRatedMoviesUrl () {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath("top_rated")
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryUrl;

    }

    /**
     * Builds URL for reviews endpoint
     */
    public static URL reviewsUrl(Movie movie) {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath(String.valueOf(movie.getMovieId()))
                .appendPath("reviews")
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryUrl;
    }

    /**
     * Builds URL for videos endpoint
     */
    public static URL trailersUrl(Movie movie) {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath(String.valueOf(movie.getMovieId()))
                .appendPath("videos")
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryUrl;
    }

    /**
     * Builds YouTube URL for trailer
     */
    public static  URL buildYouTubeLink (Trailer trailer) {
        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";

        URL queryUrl = null;

        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL)
                .buildUpon()
                .appendQueryParameter("v", trailer.getKey())
                .build();

        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
