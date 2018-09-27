package com.example.dara.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.Trailer;
import com.example.dara.popularmovies.utilities.MoviesAsyncTask;
import com.example.dara.popularmovies.utilities.MoviesJsonUtils;
import com.example.dara.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MoviesAsyncTask.OnTaskCompleted, View.OnClickListener {

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    //Declare UI elements
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private TextView mReleaseDate;
    private RatingBar mRating;
    private TextView mOverview;
    private ProgressBar mLoadingIndicator;

    private Movie mMovie;

    private URL trailer1;
    private URL trailer2;
    private URL trailer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPosterImageView = findViewById(R.id.movie_poster);
        mBackdropImageView = findViewById(R.id.movie_backdrop);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mRating = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        ImageButton mTrailer1Btn = findViewById(R.id.trailer_1);
        ImageButton mTrailer2Btn = findViewById(R.id.trailer_2);
        ImageButton mTrailer3Btn = findViewById(R.id.trailer_3);

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ID);

        populateUI(mMovie);

        mTrailer1Btn.setOnClickListener(this);
        mTrailer2Btn.setOnClickListener(this);
        mTrailer3Btn.setOnClickListener(this);
    }

    private void getTrailers() {
        URL trailersUrl = NetworkUtils.trailersUrl(mMovie);
        new MoviesAsyncTask(this, mLoadingIndicator).execute(trailersUrl);

    }

    @Override
    public void onTaskCompleted(String result) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (result != null && !result.equals("")) {
            List<Trailer> trailers = MoviesJsonUtils.extractTrailersFromJson(result);
            assert trailers != null;
            trailer1 = NetworkUtils.buildYouTubeLink(trailers.get(0));
            trailer2 = NetworkUtils.buildYouTubeLink(trailers.get(1));
            trailer3 = NetworkUtils.buildYouTubeLink(trailers.get(2));

        }

    }

    private void populateUI(Movie movie) {
        if (movie == null) return;
        getTrailers();

        //Set movie release year
        String releaseDate = movie.getReleaseDate();
        String[] dateStrings = releaseDate.split("-");
        String releaseYear = dateStrings[0];
        mReleaseDate.setText(releaseYear);

        //Set movie title
        mTitleTextView.setText(movie.getTitle());

        //Set movie rating
        int voteAverage = movie.getVoteAverage();
        mRating.setRating(voteAverage);

        //Set movie overview
        mOverview.setText(movie.getOverview());

        //Display movie poster
        Picasso.get()
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.poster_error)
                .into(mPosterImageView);

        //Display movie backdrop
        Picasso.get()
                .load(movie.getPBackdropUrl())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.poster_error)
                .into(mBackdropImageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trailer_1:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer1.toString()));
                startActivity(intent1);
            case R.id.trailer_2:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer2.toString()));
                startActivity(intent2);
            case R.id.trailer_3:
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer3.toString()));
                startActivity(intent3);
        }

    }
}

