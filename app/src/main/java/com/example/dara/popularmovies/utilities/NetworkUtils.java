package com.example.dara.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.dara.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String POPULAR_MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated";


    private static final String API_KEY_VALUE = BuildConfig.ApiKey;

    //Query parameter strings
    private static final String API_KEY = "api_key";

    /**
     * Returns new URL object based on popularity
     */
    public static URL popularMoviesUrl () {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_URL)
                .buildUpon()
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
     * Returns new URL object based on top rated
     */
    public static URL topRatedMoviesUrl () {
        URL queryUrl = null;

        Uri builtUri = Uri.parse(TOP_RATED_MOVIES_BASE_URL)
                .buildUpon()
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
