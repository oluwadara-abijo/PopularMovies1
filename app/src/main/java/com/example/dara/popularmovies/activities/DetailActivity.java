package com.example.dara.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dara.popularmovies.R;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;

    private ImageView mMoviePoster;

    private TextView mReleaseDate;

    private TextView mRating;

    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mMoviePoster = findViewById(R.id.image_view_movie_poster);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mRating = findViewById(R.id.tv_vote_average);
        mOverview = findViewById(R.id.tv_overview);
    }
}

