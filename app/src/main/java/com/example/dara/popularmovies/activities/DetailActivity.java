package com.example.dara.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    private TextView mTitleTextView;

    private ImageView mMoviePoster;

    private TextView mReleaseDate;

    private TextView mRating;

    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mMoviePoster = findViewById(R.id.movie_poster);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mRating = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);

        Movie mMovie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE_ID);

        populateUI(mMovie);
    }

    void populateUI(Movie movie) {
        if (movie == null) return;

        String releaseDate = movie.getReleaseDate();
        String [] dateStrings = releaseDate.split("-");
        String releaseYear = dateStrings[0];

        mTitleTextView.setText(movie.getTitle());
        mReleaseDate.setText(releaseYear);
        mRating.setText(String.valueOf(movie.getVoteAverage()));
        mOverview.setText(movie.getOverview());

        Picasso.get()
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_sample)
                .error(R.drawable.poster_error)
                .into(mMoviePoster);
    }
}

