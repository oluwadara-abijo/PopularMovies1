package com.example.dara.popularmovies.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.model.MovieAdapter;
import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.ui.detail.FavouritesDetailActivity;
import com.example.dara.popularmovies.ui.detail.MainDetailActivity;
import com.example.dara.popularmovies.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageTextView;

    private MovieAdapter mAdapter;
    private Parcelable mLayoutState;
    private GridLayoutManager mGridLayoutManager;

    private final String BUNDLE_STATE = "state";
    private final String BUNDLE_SORTING_STATE = "sorting_state";

    private String sort_by;
    private static final String SORT_BY_POPULARITY = "most_popular";
    private static final String SORT_BY_RATING = "top_rated";

    private boolean favView;
    private MainActivityViewModel mViewModel;
    private List<Movie> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageTextView = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mList = new ArrayList<>();

        mAdapter = new MovieAdapter(mList, this);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager = new
                    GridLayoutManager(this, 2);
        } else {
            mGridLayoutManager = new
                    GridLayoutManager(this, 4);
        }

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        MainViewModelFactory viewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);

        mSwipeRefreshLayout.setOnRefreshListener(
                () -> {
                    mRecyclerView.setAdapter(null);
                    loadMovies();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
        );

        if (savedInstanceState == null) {
            sort_by = SORT_BY_POPULARITY;
        } else {
            mGridLayoutManager.onRestoreInstanceState(mLayoutState);
        }


        if (isNetworkAvailable()) {
            loadMovies();
        } else {
            loadFavouriteMovies();
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(BUNDLE_STATE);
            sort_by = savedInstanceState.getString(BUNDLE_SORTING_STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLayoutState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(BUNDLE_STATE, mLayoutState);
        outState.putString(BUNDLE_SORTING_STATE, sort_by);
    }

    @Override
    protected void onResume() {
        if (mLayoutState != null) {
            mGridLayoutManager.onRestoreInstanceState(mLayoutState);
        }
        super.onResume();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager)
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void loadFavouriteMovies() {
        mViewModel.getFavouriteMovies().observe(this, movies -> mAdapter.setMovies(movies));
        mLoadingIndicator.setVisibility(GONE);
        mRecyclerView.setAdapter(mAdapter);
        if (mLayoutState != null) {
            mGridLayoutManager.onRestoreInstanceState(mLayoutState);
        }
    }

    private void loadMovies() {
        switch (sort_by) {
            case SORT_BY_POPULARITY:
                mViewModel.getPopularMovies().observe(this, movies -> {
                    mLoadingIndicator.setVisibility(GONE);
                    mAdapter.setMovies(movies);
                    mRecyclerView.setAdapter(mAdapter);
                });
                break;
            case SORT_BY_RATING:
                mViewModel.getTopRatedMovies().observe(this, movies -> {
                    mLoadingIndicator.setVisibility(GONE);
                    mAdapter.setMovies(movies);
                    mRecyclerView.setAdapter(mAdapter);
                });
                break;

        }

    }

    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(GONE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClickListener(Movie movie) {
        if (favView) {
            Intent intent = new Intent(MainActivity.this, FavouritesDetailActivity.class);
            intent.putExtra(FavouritesDetailActivity.EXTRA_MOVIE_ID, movie);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, MainDetailActivity.class);
            intent.putExtra(MainDetailActivity.EXTRA_MOVIE_ID, movie);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                mRecyclerView.setAdapter(null);
                loadMovies();
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case R.id.action_sort_by_popularity:
                favView = false;
                sort_by = "most_popular";
                this.setTitle(R.string.app_name);
                mRecyclerView.setAdapter(null);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                loadMovies();
                break;
            case R.id.action_sort_by_rating:
                favView = false;
                sort_by = "top_rated";
                this.setTitle(R.string.sort_by_rating_label);
                mRecyclerView.setAdapter(null);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                loadMovies();
                break;
            case R.id.action_sort_by_favourites:
                favView = true;
                this.setTitle(R.string.favourites_label);
                loadFavouriteMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
