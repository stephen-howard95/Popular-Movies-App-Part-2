package com.example.popularmoviesapp;

import java.util.List;

public class Details {
    List<Review> mReviews;
    List<Trailer> mTrailers;

    public Details(List<Review> reviews, List<Trailer> trailers){
        mReviews = reviews;
        mTrailers = trailers;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }
}
