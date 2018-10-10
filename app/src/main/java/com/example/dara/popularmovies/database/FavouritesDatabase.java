package com.example.dara.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.dara.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavouritesDatabase extends RoomDatabase {

    private static FavouritesDatabase sInstance;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourites";

    public static FavouritesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouritesDatabase.class, FavouritesDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavouritesDao favouritesDao();
}
