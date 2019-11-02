package com.example.popularmoviesapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "Favorites List";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        //allowMainThreadQueries is only temporary. To make sure it's working properly.
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
