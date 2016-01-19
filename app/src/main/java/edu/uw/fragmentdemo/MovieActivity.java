package edu.uw.fragmentdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = "MovieActivity";

    private ArrayAdapter<Movie> adapter; //adapter for list view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button)findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "searching...");

                MovieDownloadTask task = new MovieDownloadTask();
                EditText searchBox = (EditText)findViewById(R.id.txtSearch);
                String searchTerm = searchBox.getText().toString();
                task.execute(searchTerm);
            }
        });

        /** List View **/
        //model (starts out empty)
        ArrayList<Movie> list = new ArrayList<Movie>();

        //controller
        adapter = new ArrayAdapter<Movie>(
                this, R.layout.list_item, R.id.txtItem, list);

        //support ListView or GridView
        AdapterView listView = (AdapterView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //respond to item clicking
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Log.i(TAG, "selected: " + movie.toString());

            }
        });

    }

    /**
     * A background task to search for movie data on OMDB
     */
    public class MovieDownloadTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        protected ArrayList<Movie> doInBackground(String... params){

            String movie = params[0];

            //construct the url for the omdbapi API
            String urlString = "";
            try {
                urlString = "http://www.omdbapi.com/?s=" + URLEncoder.encode(movie, "UTF-8") + "&type=movie";
            }catch(UnsupportedEncodingException uee){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            ArrayList<Movie> movies = new ArrayList<Movie>();

            try {

                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                String results = buffer.toString();

                movies = parseMovieJSONData(results); //convert JSON results into Movie objects
            }
            catch (IOException e) {
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e) {
                    }
                }
            }

            return movies;
        }

        protected void onPostExecute(ArrayList<Movie> movies){
            if(movies != null) {
                adapter.clear();
                adapter.addAll(movies);
            }
        }

        /**
         * Parses a JSON-format String (from OMDB search results) into a list of Movie objects
         */
        public ArrayList<Movie> parseMovieJSONData(String json){
            ArrayList<Movie> movies = new ArrayList<Movie>();

            try {
                JSONArray moviesJsonArray = new JSONObject(json).getJSONArray("Search"); //get array from "search" key
                for(int i=0; i<moviesJsonArray.length(); i++){
                    JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i); //get ith object from array
                    Movie movie = new Movie();
                    movie.title = movieJsonObject.getString("Title"); //get title from object
                    movie.year = Integer.parseInt(movieJsonObject.getString("Year")); //get year from object
                    movie.imdbId = movieJsonObject.getString("imdbID"); //get imdb from object
                    movie.posterUrl = movieJsonObject.getString("Poster"); //get poster from object

                    movies.add(movie);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing json", e);
                return null;
            }

            return movies;
        }
    }
}
