package com.example.popularmoviesapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "favorites")
public class Movie {
    @PrimaryKey
    private int mMovieId;
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private String mMovieRating;
    private String mMovieDescription;
    private String mMoviePoster;

    //Constructor used for DetailActivity.
    public Movie(String movieTitle, String movieReleaseDate, String movieRating, String movieDescription, String moviePoster, int movieId){
        mMovieTitle = movieTitle;
        mMovieReleaseDate = movieReleaseDate;
        mMovieRating = movieRating;
        mMovieDescription = movieDescription;
        mMoviePoster = moviePoster;
        mMovieId = movieId;
    }

    public String getMovieTitle(){
        return mMovieTitle;
    }
    public String getMovieReleaseDate(){
        return mMovieReleaseDate;
    }
    public String getMovieRating(){
        return mMovieRating;
    }
    public String getMovieDescription(){
        return mMovieDescription;
    }
    public String getMoviePoster(){
        return mMoviePoster;
    }
    public int getMovieId(){
        return mMovieId;
    }
}
