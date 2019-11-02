package com.example.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(@NonNull Context context, ArrayList<Review> resource) {
        //TODO: possibly rename DetailsAdapter and include trailers too?? See if you can just get reviews first, then try for Trailers.
        super(context, 0, resource);
    }
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View reviewView = convertView;
        if(reviewView == null){
            reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        Review currentReview = getItem(position);

        TextView authorTextView = reviewView.findViewById(R.id.review_author);
        String author = currentReview.getAuthor();
        authorTextView.setText("- " + author);

        TextView contentTextView = reviewView.findViewById(R.id.review_content);
        String content = currentReview.getContent();
        contentTextView.setText(content);

        return reviewView;
    }
}
