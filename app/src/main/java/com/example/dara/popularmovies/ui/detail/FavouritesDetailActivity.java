package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.database.Movie;
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
    private ImageView mFavButton;

    //Declare database object
    private FavouritesDatabase mDb;

    //Current movie
    private Movie mMovie;

    //ViewModel object
    private FavouritesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Instantiate UI elements
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mPosterImageView = findViewById(R.id.movie_poster);
        mBackdropImageView = findViewById(R.id.movie_backdrop);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mRating = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);
        mFavButton = findViewById(R.id.favourites_button);

        //Create an instance of the database
        mDb = FavouritesDatabase.getInstance(getApplicationContext());

        //Create new FavouritesDetailActivityViewModel instance
        mViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        //Observe the LiveData
        mViewModel.getMovie().observe(this, movie -> {
            if (movie != null) {
                populateUI(movie);
            }
        });

        //Get the Movie object from the intent extra
        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE_ID);

        //Set on click listener on favourites button
        mFavButton.setOnClickListener(v -> {
            //Un-mark movie as favourite and remove movie from favourites database
            mFavButton.setImageResource(R.drawable.ic_baseline_star_border_24px);
            mDb.favouritesDao().removeFromFavourites(mMovie);
            Toast.makeText(FavouritesDetailActivity.this, "Removed from favourites",
                    Toast.LENGTH_SHORT).show();
        });

    }

    private void populateUI(Movie movie) {

        //Mark movie as favourite
        mFavButton.setImageResource(R.drawable.ic_baseline_star_24px);

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
                .load(movie.getBackdropUrl())
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
