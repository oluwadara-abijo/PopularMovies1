package com.example.dara.popularmovies.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dara.popularmovies.model.Movie;
import com.example.dara.popularmovies.model.MovieAdapter;
import com.example.dara.popularmovies.R;
import com.example.dara.popularmovies.utilities.MoviesJsonUtils;
import com.example.dara.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageTextView;

    private List<Movie> mList;

    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);

        mErrorMessageTextView = findViewById(R.id.tv_error_message);

        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mList = new ArrayList<>();

        mAdapter = new MovieAdapter(mList);

        GridLayoutManager layoutManager = new
                GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        loadMoviesData();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void loadMoviesData() {
        if (!isNetworkAvailable()) {
            showError();
            mErrorMessageTextView.setText(R.string.network_error_message);
        } else {
            URL moviesUrl = NetworkUtils.popularMoviesUrl();
            new MoviesAsyncTask().execute(moviesUrl);
        }

    }

    public void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.GONE);
    }

    public void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class MoviesAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResponse = null;
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                Log.d(">>>>", "RESPONSE" + jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null && !result.equals("")) {
                mList = MoviesJsonUtils.extractMoviesFromJson(result);
                mAdapter = new MovieAdapter(mList);
                mRecyclerView.setAdapter(mAdapter);
                showData();
            } else {
                showError();
            }
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

        if (id == R.id.action_refresh) {
            loadMoviesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
