package com.example.popularmoviesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import static com.example.popularmoviesapp.MainActivity.sort_by;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        RadioButton popularRb = findViewById(R.id.radio_popular);
        popularRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sort_by = "popular";
                }
            }
        });
        RadioButton ratingRb = findViewById(R.id.radio_rating);
        ratingRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sort_by = "top_rated";
                }
            }
        });
        RadioButton favoritesRb = findViewById(R.id.radio_favorites);
        favoritesRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sort_by = "favorites";
                }
            }
        });

        if(sort_by.equals("popular")){
            popularRb.setChecked(true);
        } else if(sort_by.equals("top_rated")){
            ratingRb.setChecked(true);
        } else if(sort_by.equals("favorites")){
            favoritesRb.setChecked(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
