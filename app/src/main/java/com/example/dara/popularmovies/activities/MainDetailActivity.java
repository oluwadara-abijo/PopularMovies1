package com.example.dara.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;
import com.example.dara.popularmovies.utilities.MoviesAsyncTask;
import com.example.dara.popularmovies.utilities.MoviesJsonUtils;
import com.example.dara.popularmovies.utilities.NetworkUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static android.view.View.GONE;

public class MainDetailActivity extends AppCompatActivity implements MoviesAsyncTask.OnTaskCompleted,
        View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    // Extra for the movie ID to be received in the intent
    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    //Declare UI elements
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private ImageView mBackdropImageView;
    private TextView mReleaseDate;
    private RatingBar mRating;
    private ExpandableTextView mOverview;
    private TextView mTrailersLabel;
    private TextView mReviewsLabel;
    private ProgressBar mLoadingIndicator;
    private ExpandableTextView mReview1;
    private ExpandableTextView mReview2;
    private ExpandableTextView mReview3;
    private LinearLayout mReviewsView;
    private ImageView mFavouritesButton;
    private ImageView mShareTrailerBtn;

    private Movie mMovie;

    private URL trailer1Url;
    private URL trailer2Url;
    private URL trailer3Url;

    private ImageButton mTrailer1Btn;
    private ImageButton mTrailer2Btn;
    private ImageButton mTrailer3Btn;

    private FavouritesDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

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
        mFavouritesButton = findViewById(R.id.favourites_button);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mTrailersLabel = findViewById(R.id.trailer_label);
        mReviewsLabel = findViewById(R.id.reviews_label);
        mShareTrailerBtn = findViewById(R.id.share_trailer);

        mReview1 = findViewById(R.id.tv_review1);
        mReview2 = findViewById(R.id.tv_review2);
        mReview3 = findViewById(R.id.tv_review3);

        mReviewsView = findViewById(R.id.reviews_layout);

        mTrailer1Btn = findViewById(R.id.trailer_1);
        mTrailer2Btn = findViewById(R.id.trailer_2);
        mTrailer3Btn = findViewById(R.id.trailer_3);

        mDb = FavouritesDatabase.getInstance(getApplicationContext());

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ID);

        mTrailer1Btn.setOnClickListener(this);
        mTrailer2Btn.setOnClickListener(this);
        mTrailer3Btn.setOnClickListener(this);

        //Set on click listener on share button
        mShareTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, trailer1Url.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        //Set on click listener on favourites button
        View.OnClickListener mFavButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tag = (Integer) mFavouritesButton.getTag();
                tag = tag == null ? 0 : tag;
                switch (tag) {
                    case R.drawable.ic_baseline_star_24px:
                        mFavouritesButton.setImageResource(R.drawable.ic_baseline_star_border_24px);
                        mFavouritesButton.setTag(R.drawable.ic_baseline_star_border_24px);
                        Toast.makeText(MainDetailActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                        mDb.favouritesDao().removeFromFavourites(mMovie);
                        break;
                    case R.drawable.ic_baseline_star_border_24px:
                    default:
                        mFavouritesButton.setImageResource(R.drawable.ic_baseline_star_24px);
                        mFavouritesButton.setTag(R.drawable.ic_baseline_star_24px);
                        Toast.makeText(MainDetailActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                        mDb.favouritesDao().addToFavourites(mMovie);
                        break;
                }
            }
        };
        mFavouritesButton.setOnClickListener(mFavButtonListener);

        checkIfFav();
        getTrailers();
        getReviews();
        populateUI();

    }

    private void getTrailers() {
        URL trailersUrl = NetworkUtils.trailersUrl(mMovie);
        new MoviesAsyncTask(this, mLoadingIndicator).execute(trailersUrl);

    }

    private void getReviews() {
        URL reviewsUrl = NetworkUtils.reviewsUrl(mMovie);
        new MoviesAsyncTask(this, mLoadingIndicator).execute(reviewsUrl);

    }

    private String createThumbnail(Trailer trailer) {
        String videoId = trailer.getKey();
        String thumbnail = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
        Log.d("TAG>>", thumbnail);
        return thumbnail;
    }

    @Override
    public void onTaskCompleted(String result) {
        mLoadingIndicator.setVisibility(GONE);
        if (result != null && !result.equals("")) {
            if (result.contains("total_pages") && result.contains("total_results")) {
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
            } else {
                List<Trailer> trailers = MoviesJsonUtils.extractTrailersFromJson(result);
                if (trailers == null) {
                    mShareTrailerBtn.setVisibility(GONE);
                    mTrailersLabel.setVisibility(GONE);
                } else if (trailers.size() > 0) {
                    mTrailer1Btn.setVisibility(View.VISIBLE);
                    trailer1Url = NetworkUtils.buildYouTubeLink(trailers.get(0));
                    Trailer trailer1 = trailers.get(0);
                    Picasso.get().load(createThumbnail(trailer1)).into(mTrailer1Btn);
                    if (trailers.size() > 1) {
                        mTrailer2Btn.setVisibility(View.VISIBLE);
                        trailer2Url = NetworkUtils.buildYouTubeLink(trailers.get(1));
                        Trailer trailer2 = trailers.get(1);
                        Picasso.get().load(createThumbnail(trailer2)).into(mTrailer2Btn);
                    }
                    if (trailers.size() > 2) {
                        mTrailer3Btn.setVisibility(View.VISIBLE);
                        trailer3Url = NetworkUtils.buildYouTubeLink(trailers.get(2));
                        Trailer trailer3 = trailers.get(2);
                        Picasso.get().load(createThumbnail(trailer3)).into(mTrailer3Btn);
                    }
                }
            }
        }

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

    private void checkIfFav() {
        List<String> favTitles = mDb.favouritesDao().getFavouriteTitles();
        Log.d(TAG, String.valueOf(favTitles));
        for (String title : favTitles) {
            if (title.equals(mMovie.getTitle())) {
                mFavouritesButton.setImageResource(R.drawable.ic_baseline_star_24px);
            } else {
                mFavouritesButton.setImageResource(R.drawable.ic_baseline_star_border_24px);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trailer_1:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer1Url.toString()));
                startActivity(intent1);
                break;
            case R.id.trailer_2:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer2Url.toString()));
                startActivity(intent2);
                break;
            case R.id.trailer_3:
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer3Url.toString()));
                startActivity(intent3);
                break;
        }
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

