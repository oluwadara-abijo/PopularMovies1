package com.example.dara.popularmovies.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesJsonUtils {

    //Method to build url for poster path
    private static String buildPosterUrl(String posterPath) {
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
        return IMAGE_BASE_URL+IMAGE_SIZE+posterPath;
    }

    //Method to build url for backdrop path
    private static String buildBackdropUrl(String posterPath) {
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w342";
        return IMAGE_BASE_URL + IMAGE_SIZE + posterPath;
    }

    /**
     * Returns a list of Trailer objects
     */
    public static List<Trailer> extractTrailersFromJson(String trailersJson) {
        if (TextUtils.isEmpty(trailersJson)) {
            return null;
        }

        //Create an empty ArrayList that trailers will be added to
        List<Trailer> trailers = new ArrayList<>();

        //Create a JSONObject from the response string
        try {
            JSONObject jsonObject = new JSONObject(trailersJson);

            //Extract results array from the object
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            if (jsonArray.length() == 0) {
                Log.d("MoviesJsonUtils", "No trailers");
                return null;
            }

            //Loop trough the array
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject currentTrailer = jsonArray.getJSONObject(i);

                //Extract the trailer key
                String trailer_key = currentTrailer.getString("key");

                trailers.add(new Trailer(trailer_key));
                Log.d("MoviesJsonUtils", String.valueOf(trailers));


            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MoviesJsonUtils", "Problem parsing the book JSON results", e);
        }

        return trailers;
    }

    /**
     * Returns a list of Trailer objects
     */
    public static List<Review> extractReviewsFromJson(String reviewsJson) {
        if (TextUtils.isEmpty(reviewsJson)) {
            return null;
        }

        //Create an empty ArrayList that reviews will be added to
        List<Review> reviews = new ArrayList<>();

        //Create a JSONObject from the response string
        try {
            JSONObject jsonObject = new JSONObject(reviewsJson);

            //Extract results array from the object
            JSONArray jsonArray = jsonObject.optJSONArray("results");

            if (jsonArray.length() == 0) {
                Log.d("MoviesJsonUtils", "No reviews");
                return null;
            }

            //Loop trough the array
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject currentReview = jsonArray.getJSONObject(i);

                //Extract the review content
                String review_content = currentReview.getString("content");

                reviews.add(new Review(review_content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MoviesJsonUtils", "Problem parsing the book JSON results", e);
        }

        return reviews;
    }


    /**
     * Returns a Movie object by parsing information
     * about the movies from the input moviesJSON string
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

                //Extract "id" int
                int movieId = currentMovie.optInt("id");

                //Extract "vote_average" int
                int voteAverage = currentMovie.optInt("vote_average");

                //Extract "title" for movie title
                String title = currentMovie.optString("title");

                //Extract "poster_path" for movie poster
                String posterFilePath = currentMovie.optString("poster_path");
                String posterUrl = buildPosterUrl(posterFilePath);

                //Extract "backdrop_path" for movie backdrop
                String backdropFilePath = currentMovie.optString("backdrop_path");
                String backdropUrl = buildBackdropUrl(backdropFilePath);

                //Extract "overview" for movie overview
                String overview = currentMovie.optString("overview");

                //Extract "release_date" for release date
                String releaseDate = currentMovie.optString("release_date");

                //Add the new Movie to the list of movies
                movies.add(new Movie(movieId, title, posterUrl, backdropUrl, overview, releaseDate, voteAverage));
            }

        } catch (JSONException e) {
            Log.e("MoviesJsonUtils", "Problem parsing the movie JSON results", e);
        }

        return movies;

    }

}
