package com.example.popularmoviesapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static final String LOG_TAG = MainActivity.class.getName();

    public static List<Movie> extractListFromJson(String MovieJson){
        //TODO In DetailActivity, make a new Loader/new service request to a new URL.

        if(TextUtils.isEmpty(MovieJson)){
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(MovieJson);

            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

            for(int i=0; i<resultsArray.length(); i++){
                JSONObject currentMovie = resultsArray.getJSONObject(i);

                String title = currentMovie.getString("original_title");

                StringBuilder moviePoster = new StringBuilder("https://image.tmdb.org/t/p/")
                        .append("w185")
                        .append(currentMovie.getString("poster_path"));

                String rating = currentMovie.getString("vote_average");

                String description = currentMovie.getString("overview");

                String releaseDate = currentMovie.getString("release_date");

                int movieId = currentMovie.getInt("id");

                Movie movie = new Movie(title, releaseDate, rating, description, moviePoster.toString(), movieId);

                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
    public static List<Review> extractMovieReviewsFromJson(String MovieJson){
        if(TextUtils.isEmpty(MovieJson)){
            return null;
        }
        List<Review> reviewsList = new ArrayList<>();
        /*Because the above extractFeatureFromJson method involves a list, the individual JSON Strings are simply added to the list to populate the UI
        * But because the second extractFeatureFromJson method isn't a list of Movies, but just 1 Movie, variable scope is preventing me from creating
        * the constructor and filling it with the JSON Strings the way I want to. I could potentially make several variables above and set them to the
        * "movieDetails" object, and within the try/catch block, set them equal to each other, but I feel like that's a waste of memory. Ask dad maybe*/

        try{
            JSONObject currentMovie = new JSONObject(MovieJson);

            JSONObject reviews = currentMovie.getJSONObject("reviews");

            JSONArray reviewsArray = reviews.getJSONArray("results");

            for(int j=0; j<reviewsArray.length(); j++){
                JSONObject currentReview = reviewsArray.getJSONObject(j);

                String author = currentReview.getString("author");

                String userReview = currentReview.getString("content");

                Review review = new Review(author, userReview);

                reviewsList.add(review);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        return reviewsList;
    }
    public static List<Trailer> extractMovieTrailersFromJson(String MovieJson){
        if(TextUtils.isEmpty(MovieJson)){
            return null;
        }
        List<Trailer> trailersList = new ArrayList<>();

        try{
            JSONObject currentMovie = new JSONObject(MovieJson);

            JSONObject trailers = currentMovie.getJSONObject("videos");

            JSONArray trailersArray = trailers.getJSONArray("results");

            for(int i=0; i<trailersArray.length(); i++){
                JSONObject chosenTrailer = trailersArray.getJSONObject(i);

                String title = chosenTrailer.getString("name");

                StringBuilder trailerUrl = new StringBuilder("https://www.youtube.com/watch?v=")
                        .append(chosenTrailer.getString("key"));
                //https://www.youtube.com/watch?v=xRjvmVaFHkk <--(Joker final trailer)
                //https://www.youtube.com/watch?v={key} <--(final URL for the youtube intent)

                Trailer trailer = new Trailer(title, trailerUrl.toString());

                trailersList.add(trailer);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
        return trailersList;
    }

    public static List<Movie> fetchMovieData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Movie> moviesJson = extractListFromJson(jsonResponse);

        return moviesJson;
    }
    public static List<Review> fetchMovieReviews(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Review> ReviewsJson = extractMovieReviewsFromJson(jsonResponse);

        return ReviewsJson;
    }
    public static List<Trailer> fetchMovieTrailers(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Trailer> TrailersJson = extractMovieTrailersFromJson(jsonResponse);

        return TrailersJson;
    }
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
