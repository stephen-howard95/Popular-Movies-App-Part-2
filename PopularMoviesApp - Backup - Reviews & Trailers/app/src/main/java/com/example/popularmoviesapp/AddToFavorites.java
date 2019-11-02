package com.example.popularmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

public class AddToFavorites extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    CheckBox mCheckBox;

    private int mMovieId = DEFAULT_TASK_ID;

    private MovieDatabase mDb;


}
