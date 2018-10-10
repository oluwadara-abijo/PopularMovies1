package com.example.dara.popularmovies.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;


public class FavouritesDetailActivity extends AppCompatActivity {

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    //Declare UI elements
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private TextView mReleaseDate;
    private RatingBar mRating;
    private TextView mOverview;

    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPosterImageView = findViewById(R.id.movie_poster);
        mBackdropImageView = findViewById(R.id.movie_backdrop);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mRating = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ID);

        populateUI();
    }

    private void populateUI() {
        if (mMovie == null) return;

        //Set movie release year
        String releaseDate = mMovie.getReleaseDate();
        String[] dateStrings = releaseDate.split("-");
        String releaseYear = dateStrings[0];
        mReleaseDate.setText(releaseYear);

        //Set movie title
        mTitleTextView.setText(mMovie.getTitle());

        //Set movie rating
        int voteAverage = mMovie.getVoteAverage();
        mRating.setRating(voteAverage);

        //Set movie overview
        mOverview.setText(mMovie.getOverview());

        //Display movie poster
        Picasso.get()
                .load(mMovie.getPosterUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.poster_error)
                .into(mPosterImageView);

        //Display movie backdrop
        Picasso.get()
                .load(mMovie.getBackdropUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.poster_error)
                .into(mBackdropImageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
