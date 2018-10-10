package com.example.dara.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.MovieAdapter;
import com.example.dara.popularmovies.model.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new MovieAdapter(new ArrayList<Movie>(), this);

        MovieViewModel mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        mViewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setFavouriteMovies(movies);
            }
        });

        //Set up recycler view
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = new Intent(FavouritesActivity.this, FavouritesDetailActivity.class);
        intent.putExtra(MainDetailActivity.EXTRA_MOVIE_ID, movie);
        startActivity(intent);

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
