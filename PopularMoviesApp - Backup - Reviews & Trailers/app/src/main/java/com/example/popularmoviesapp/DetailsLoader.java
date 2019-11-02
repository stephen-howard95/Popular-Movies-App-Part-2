package com.example.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DetailsLoader extends AsyncTaskLoader<Details> {
    private String mUrl;

    public DetailsLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    //This is on a background thread
    @Override
    public Details loadInBackground(){
        if(mUrl == null){
            return null;
        }
        List<Review> reviews = JsonUtils.fetchMovieReviews(mUrl);
        List<Trailer> trailers = JsonUtils.fetchMovieTrailers(mUrl);
        Details details = new Details(reviews, trailers);

        return details;
    }
}
