package com.example.dara.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouritesDao {

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<Movie>> getAllFavouriteMovies();

    @Query("SELECT title FROM favourite_movies")
    List<String> getFavouriteTitles();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void addToFavourites(Movie movie);

    @Delete
    void removeFromFavourites(Movie movie);
}
