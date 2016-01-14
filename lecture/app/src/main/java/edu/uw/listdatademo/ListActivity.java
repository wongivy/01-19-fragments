package edu.uw.listdatademo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //model!
        String[] data = new String[99];
        for(int i=99; i>0; i--){
            data[99-i] = i+ " bottles of beer on the wall";
        }

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(data)); //convert to ArrayList (so modifiable)
        //String[] data = downloadMovieData("Die Hard");



        //controller
        adapter = new ArrayAdapter<String>(
                this, R.layout.list_item, R.id.txtItem, list);

        //support ListView or GridView
        AdapterView listView = (AdapterView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        MovieDownloadTask task = new MovieDownloadTask();
        task.execute("Die Hard");

    }

    public class MovieDownloadTask extends AsyncTask<String, Void, String[]> {

        protected String[] doInBackground(String... params){

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

            String movies[] = null;

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
                results = results.replace("{\"Search\":[","");
                results = results.replace("]}","");
                results = results.replace("},", "},\n");
                Log.v("ListActivity", results);
                movies = results.split("\n");

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


        protected void onPostExecute(String[] movies){

            if(movies != null) {
                adapter.clear();
                for (String movie : movies) {
                    adapter.add(movie);
                }
            }

        }

    }
}
