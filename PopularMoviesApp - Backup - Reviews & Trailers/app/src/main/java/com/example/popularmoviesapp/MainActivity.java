package com.example.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static String sort_by = "popular";
    //Insert your TMDB API key in the String below
    public static final String API_KEY = "";

    public static final int MOVIE_LOADER_ID = 1;

    private MovieAdapter mAdapter;
    private LiveData<List<Movie>> favoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        ListView movieListView = findViewById(R.id.list);

        movieListView.setAdapter(mAdapter);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchDetailActivity((Movie) parent.getItemAtPosition(position));
            }
        });

        final MovieDatabase mDb = MovieDatabase.getInstance(getApplicationContext());
        favoriteMovies = mDb.movieDao().loadAllMovies();
        favoriteMovies.observeForever(new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(sort_by.equals("favorites")) {
                    mAdapter.clear();
                    mAdapter.addAll(movies);
                }
            }
        });
    }

    private void launchDetailActivity(Movie movie) {

        boolean isFavorite = false;

        List<Movie> movies = favoriteMovies.getValue();

        if(movies != null) {
            for(Movie m : movies) {
                if(movie.getMovieId() == m.getMovieId()) {
                    isFavorite = true;
                }
            }
        }

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_TITLE, movie.getMovieTitle());
        intent.putExtra(DetailActivity.RELEASE_DATE, movie.getMovieReleaseDate());
        intent.putExtra(DetailActivity.MOVIE_RATING, movie.getMovieRating());
        intent.putExtra(DetailActivity.DESCRIPTION, movie.getMovieDescription());
        intent.putExtra(DetailActivity.MOVIE_POSTER, movie.getMoviePoster());
        intent.putExtra(DetailActivity.MOVIE_ID_KEY, movie.getMovieId());
        intent.putExtra(DetailActivity.IS_FAVORITE, isFavorite);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        //final URL = "http://api.themoviedb.org/3/movie/{sort_by}?api_key={API_KEY}"
        Uri baseUri = Uri.parse(TMDB_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(sort_by);
        uriBuilder.appendQueryParameter("api_key", API_KEY);

        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Movie>> loader, List<Movie> movies) {
        mAdapter.clear();

        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
        }

        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Movie>> loader) {
        mAdapter.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sort_by.equals("favorites")){
            mAdapter.clear();
            List<Movie> movies = favoriteMovies.getValue();
            if(movies != null && !movies.isEmpty()){
                mAdapter.addAll(movies);
            }

        } else{
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {
                // Get a reference to the LoaderManager, in order to interact with loaders.
                android.app.LoaderManager loaderManager = getLoaderManager();

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
