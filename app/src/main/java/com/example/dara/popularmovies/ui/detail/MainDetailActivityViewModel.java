package com.example.dara.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dara.popularmovies.MoviesRepository;
import com.example.dara.popularmovies.database.Movie;
import com.example.dara.popularmovies.model.Review;
import com.example.dara.popularmovies.model.Trailer;

import java.util.List;

public class MainDetailActivityViewModel extends ViewModel {

    private final MoviesRepository mRepository;
    private final Movie mMovie;

    public MainDetailActivityViewModel(MoviesRepository repository, Movie movie) {
        mRepository = repository;
        mMovie = movie;
    }

    public LiveData<List<Review>> getMovieReviews() {
        return mRepository.getReviews(mMovie);
    }

    public LiveData<List<Trailer>> getMovieTrailers() {
        return mRepository.getTrailers(mMovie);
    }

    public void addToFavourites() {
        mRepository.addToFavourites(mMovie);
    }

    public void removeFromFavourites() {
        mRepository.removeFromFavourites(mMovie);
    }


}
