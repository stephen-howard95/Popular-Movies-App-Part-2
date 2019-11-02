package com.example.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import static com.example.popularmoviesapp.MainActivity.API_KEY;

public class DetailActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Details> {

    public static final String MOVIE_TITLE = "movie_title";
    public static final String RELEASE_DATE = "release_date";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String DESCRIPTION = "description";
    public static final String MOVIE_POSTER = "movie_poster";
    public static final String MOVIE_ID_KEY = "movie_id";
    public static final String IS_FAVORITE = "is_favorite";

    private int movieId = 0;
    public static final String TMDB_DETAIL_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final int REVIEW_LOADER_ID = 1;
    public static final int TRAILER_LOADER_ID = 2;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String movieTitle = getIntent().getExtras().getString(MOVIE_TITLE);
        String releaseDate = getIntent().getExtras().getString(RELEASE_DATE);
        String movieRating = getIntent().getExtras().getString(MOVIE_RATING);
        String description = getIntent().getExtras().getString(DESCRIPTION);
        String moviePoster = getIntent().getExtras().getString(MOVIE_POSTER);
        boolean isFavorite = getIntent().getExtras().getBoolean(IS_FAVORITE);
        movieId = getIntent().getExtras().getInt(MOVIE_ID_KEY);

        final Movie thisMovie = new Movie(movieTitle, releaseDate, movieRating, description, moviePoster, movieId);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(REVIEW_LOADER_ID, null, this);
            loaderManager.initLoader(TRAILER_LOADER_ID, null, this);
        }

        mReviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        ListView reviewListView = findViewById(R.id.reviews);
        reviewListView.setAdapter(mReviewAdapter);

        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        ListView trailerListView = findViewById(R.id.trailers);
        trailerListView.setAdapter(mTrailerAdapter);

        ImageView posterImageView = findViewById(R.id.movie_poster);

        Movie chosenMovie = new Movie(movieTitle, releaseDate, movieRating, description, moviePoster, movieId);

        if (chosenMovie == null) {
            closeOnError();
            return;
        }
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) parent.getItemAtPosition(position);
                LaunchMovieTrailer(trailer);
            }
        });

        populateUI(chosenMovie);
        Picasso.get()
                .load(chosenMovie.getMoviePoster())
                .into(posterImageView);

        final MovieDatabase mDb = MovieDatabase.getInstance(getApplicationContext());
        final CheckBox checkBox = findViewById(R.id.favorite_cb);
        checkBox.setChecked(isFavorite);
        checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mDb.movieDao().insertMovie(thisMovie);
                } else{
                    mDb.movieDao().deleteMovie(thisMovie);
                }
            }
        });
    }
    private void LaunchMovieTrailer(Trailer trailer){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getVideoKey())));
    }
    private void closeOnError() {
        finish();
    }
    private void populateUI(Movie movie){
        TextView titleTextView = findViewById(R.id.movie_title);
        titleTextView.setText(movie.getMovieTitle());
        TextView releaseDateTextView = findViewById(R.id.release_date);
        releaseDateTextView.setText(movie.getMovieReleaseDate());
        TextView ratingTextView = findViewById(R.id.rating);
        ratingTextView.setText(movie.getMovieRating());
        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(movie.getMovieDescription());
    }

    @Override
    public Loader<Details> onCreateLoader(int id, Bundle args) {
        //final URL = https://api.themoviedb.org/3/movie/{movie_id}?api_key={API_KEY}&append_to_response=videos,reviews
        Uri baseUri = Uri.parse(TMDB_DETAIL_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(String.valueOf(movieId));
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.appendQueryParameter("append_to_response", "videos,reviews");

        return new DetailsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<Details> loader, Details data) {
        mReviewAdapter.clear();
        mTrailerAdapter.clear();
        if (data != null) {
            List<Review> reviews = data.getReviews();
            List<Trailer> trailers = data.getTrailers();
            mReviewAdapter.addAll(reviews);
            mTrailerAdapter.addAll(trailers);
        }

        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<Details> loader) {
        mReviewAdapter.clear();
        mTrailerAdapter.clear();
    }
}
