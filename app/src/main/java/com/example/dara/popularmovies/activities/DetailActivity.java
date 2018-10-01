package com.example.dara.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;
import com.example.dara.popularmovies.utilities.MoviesAsyncTask;
import com.example.dara.popularmovies.utilities.MoviesJsonUtils;
import com.example.dara.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

import static android.view.View.GONE;

public class DetailActivity extends AppCompatActivity implements MoviesAsyncTask.OnTaskCompleted, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    //Declare UI elements
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private TextView mReleaseDate;
    private RatingBar mRating;
    private TextView mOverview;
    private TextView mTrailersLabel;
    private TextView mReviewsLabel;
    private ProgressBar mLoadingIndicator;
    private TextView mReview1;
    private ExpandableTextView mReview2;
    private ExpandableTextView mReview3;
    private HorizontalScrollView mTrailersView;
    private LinearLayout mReviewsView;

    private Movie mMovie;

    private URL trailer1;
    private URL trailer2;
    private URL trailer3;

    private ImageButton mTrailer1Btn;
    private ImageButton mTrailer2Btn;
    private ImageButton mTrailer3Btn;

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
        mTrailersLabel = findViewById(R.id.trailer_label);
        mReviewsLabel = findViewById(R.id.reviews_label);

        mReview1 = findViewById(R.id.tv_review1);
        mReview2 = findViewById(R.id.tv_review2);
        mReview3 = findViewById(R.id.tv_review3);

        mTrailersView = findViewById(R.id.trailers_scroll_view);
        mReviewsView = findViewById(R.id.reviews_layout);

        mTrailer1Btn = findViewById(R.id.trailer_1);
        mTrailer2Btn = findViewById(R.id.trailer_2);
        mTrailer3Btn = findViewById(R.id.trailer_3);

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

    private void getReviews() {
        URL reviewsUrl = NetworkUtils.reviewsUrl(mMovie);
        new MoviesAsyncTask(this, mLoadingIndicator).execute(reviewsUrl);

    }

    @Override
    public void onTaskCompleted(String result) {
        mLoadingIndicator.setVisibility(GONE);
        if (result != null && !result.equals("")) {
            if (result.contains("id") && result.contains("results")) {
                List<Trailer> trailers = MoviesJsonUtils.extractTrailersFromJson(result);
                if (trailers == null) {
                    mTrailersView.setVisibility(GONE);
                    mTrailersLabel.setVisibility(GONE);
                } else if (trailers.size() > 0) {
                    trailer1 = NetworkUtils.buildYouTubeLink(trailers.get(0));
                    if (trailers.size() > 1) {
                        mTrailer2Btn.setVisibility(View.VISIBLE);
                        trailer2 = NetworkUtils.buildYouTubeLink(trailers.get(1));
                    }
                    if (trailers.size() > 2) {
                        mTrailer3Btn.setVisibility(View.VISIBLE);
                        trailer3 = NetworkUtils.buildYouTubeLink(trailers.get(2));
                    }
                }
            }
            if (result.contains("id") && result.contains("page") && result.contains("results")) {
                List<Review> reviews = MoviesJsonUtils.extractReviewsFromJson(result);
                if (reviews == null) {
                    mReviewsView.setVisibility(GONE);
                    mReviewsLabel.setVisibility(GONE);
                } else if (reviews.size() > 0) {
                    String review1 = reviews.get(0).getContent();
                    mReview1.setText(review1);
                    if (reviews.size() > 1) {
                        mReview2.setVisibility(View.VISIBLE);
                        String review2 = reviews.get(1).getContent();
                        mReview2.setText(review2);
                    }
                    if (reviews.size() > 2) {
                        mReview3.setVisibility(View.VISIBLE);
                        String review3 = reviews.get(2).getContent();
                        mReview3.setText(review3);
                    }
                }
            }
        }

    }

    private void populateUI(Movie movie) {
        if (movie == null) return;
        getTrailers();
        getReviews();

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

