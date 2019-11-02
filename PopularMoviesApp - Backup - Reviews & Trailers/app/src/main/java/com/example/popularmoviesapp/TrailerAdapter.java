package com.example.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(@NonNull Context context, ArrayList<Trailer> resource) {
        super(context, 0, resource);
    }
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View trailerView = convertView;
        if(trailerView == null){
            trailerView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        Trailer currentTrailer = getItem(position);

        TextView trailerButton = trailerView.findViewById(R.id.trailer_text_view);
        String title = currentTrailer.getTitle();
        trailerButton.setText(title);

        //TODO debug to make sure the link is created properly. Check to see in the other app where it's made and how the onClick will work.

        return trailerView;
    }
}
