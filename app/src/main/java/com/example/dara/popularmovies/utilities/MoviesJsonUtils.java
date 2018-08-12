package com.example.dara.popularmovies.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.dara.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesJsonUtils {

    public static String buildImageUrl(String posterPath) {
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
        String imageUrl = IMAGE_BASE_URL+IMAGE_SIZE+posterPath;
        return imageUrl;
    }

    /**
     * Return a Movie book object by parsing information
     * about the movies from the input bookJSON string
     */
    public static List<Movie> extractMoviesFromJson(String moviesJSON) {
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can add movies to
        List<Movie> movies = new ArrayList<>();

        //Create JSONObject from the JSON response string
        try {
            JSONObject jsonRootObject = new JSONObject(moviesJSON);

            //Extract "results" JSONArray
            JSONArray resultsArray = jsonRootObject.optJSONArray("results");

            //Loop through each item in the array
            for (int i = 0; i < resultsArray.length(); i++) {

                //Get movie JSONObject at position i
                JSONObject currentMovie = resultsArray.optJSONObject(i);

                //Extract "vote_average" int
                int voteAverage = currentMovie.optInt("vote_average");

                //Extract "title" for movie title
                String title = currentMovie.optString("title");

                //Extract "poster_path" for movie poster
                String posterFilePath = currentMovie.optString("poster_path");
                String posterUrl = buildImageUrl(posterFilePath);

                //Extract "overview" for movie overview
                String overview = currentMovie.optString("overview");

                //Extract "release_date" for release date
                String releaseDate = currentMovie.optString("release_date");

                //Add the new Movie to the list of movies
                movies.add(new Movie(title, posterUrl, overview, releaseDate, voteAverage));
            }

        } catch (JSONException e) {
            Log.e("Query Utils", "Problem parsing the book JSON results", e);
        }

        return movies;

    }

}
