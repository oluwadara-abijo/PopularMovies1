package com.example.dara.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.MovieAdapter;
import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.MovieViewModel;
import com.example.dara.popularmovies.utilities.MovieLoader;
import com.example.dara.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageTextView;

    private MovieAdapter mAdapter;
    private Parcelable mLayoutState;
    private GridLayoutManager mGridLayoutManager;
    private final String BUNDLE_STATE = "state";

    private URL mQueryUrl;

    private static final int POPULAR_LOADER_ID = 1;
    private static final int TOP_RATED_LOADER_ID = 2;


    private boolean favView;
    private MovieViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);

        mErrorMessageTextView = findViewById(R.id.tv_error_message);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        List<Movie> mList = new ArrayList<>();

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

        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRecyclerView.setAdapter(null);
                        mRecyclerView.setAdapter(mAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        loadPopularMoviesData();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(BUNDLE_STATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLayoutState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(BUNDLE_STATE, mLayoutState);
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
        mAdapter = new MovieAdapter(new ArrayList<Movie>(), this);
        mViewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setFavouriteMovies(movies);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadPopularMoviesData() {
        if (isNetworkAvailable()) {
            getSupportLoaderManager().initLoader(POPULAR_LOADER_ID, null, this);
        } else {
            showError();
            mErrorMessageTextView.setText(R.string.network_error_message);
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private void loadTopRatedMoviesData() {
        if (isNetworkAvailable()) {
            getSupportLoaderManager().initLoader(TOP_RATED_LOADER_ID, null, this);
        } else {
            showError();
            mErrorMessageTextView.setText(R.string.network_error_message);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.GONE);
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

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == POPULAR_LOADER_ID) {
            mQueryUrl = NetworkUtils.popularMoviesUrl();
            return new MovieLoader(this, mQueryUrl);
        } else if (id == TOP_RATED_LOADER_ID) {
            mQueryUrl = NetworkUtils.topRatedMoviesUrl();
            return new MovieLoader(this, mQueryUrl);
        }
        return new MovieLoader(this, mQueryUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (movies != null && !movies.isEmpty()) {
            mAdapter = new MovieAdapter(movies, this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            showData();
        } else {
            showError();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

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
                mSwipeRefreshLayout.setRefreshing(true);
                break;
            case R.id.action_sort_by_popularity:
                favView = false;
                this.setTitle(R.string.app_name);
                mRecyclerView.setAdapter(null);
                loadPopularMoviesData();
                break;
            case R.id.action_sort_by_rating:
                favView = false;
                this.setTitle(R.string.sort_by_rating_label);
                mRecyclerView.setAdapter(null);
                loadTopRatedMoviesData();
                break;
            case R.id.action_sort_by_favourites:
                favView = true;
                this.setTitle(R.string.favourites_label);
                mRecyclerView.setAdapter(null);
                loadFavouriteMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
