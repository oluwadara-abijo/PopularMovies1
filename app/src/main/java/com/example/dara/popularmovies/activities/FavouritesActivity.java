package com.example.dara.popularmovies.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.database.FavouritesDatabase;
import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.MovieAdapter;

import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private static final String TAG = FavouritesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Get an instance of the database
        FavouritesDatabase mDatabase = FavouritesDatabase.getInstance(getApplicationContext());

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        List<Movie> mList = mDatabase.favouritesDao().getAllFavouriteMovies();
        MovieAdapter mAdapter = new MovieAdapter(mList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onItemClickListener(Movie movie) {

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
