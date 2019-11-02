package com.example.popularmoviesapp;

public class Trailer {
    private String mTitle;
    private String mVideoKey;

    public Trailer(String title, String videoKey){
        mTitle = title;
        mVideoKey = videoKey;
    }

    public String getTitle(){
        return mTitle;
    }
    public String getVideoKey(){
        return mVideoKey;
    }
}
