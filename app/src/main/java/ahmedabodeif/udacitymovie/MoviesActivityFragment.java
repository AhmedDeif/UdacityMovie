package ahmedabodeif.udacitymovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment {

    private ArrayAdapter<Movie> adapterItems;
    private GridAdapter gridAdapter;
    private GridView movieGrid;
    private ArrayList<Movie> gridData = new ArrayList<>();
    private ListView lvItems;
    private ProgressBar mProgressBar;
    private Context mCotext;
    private SharedPreferences mSharedPrefs;
    private String oldSetting;
    View rootView;



    private OnListItemSelectedListener listener;

    public interface OnListItemSelectedListener {
        public void onItemSelected(Movie item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) activity;
        } else {
            throw new ClassCastException(
                    activity.toString()
                            + " must implement ItemsListFragment.OnListItemSelectedListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        rootView = inflater.inflate(R.layout.fragment_movies,
                container, false);
        mCotext = this.getContext();
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCotext);
        oldSetting = mSharedPrefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_label_popular));


        if(oldSetting.equals(getString(R.string.pref_sort_fav))) {

            DatabaseHandler db = new DatabaseHandler(this.getActivity());
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
            gridData = new ArrayList<Movie>();
            gridData = db.getAll();
            // get data from db
            gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item,gridData);
            movieGrid.setAdapter(gridAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View item,
                                        int position, long rowId) {
                    // Retrieve item based on position
                    Movie tmp = gridAdapter.getItem(position);

                    // Fire selected listener event with item
                    listener.onItemSelected(tmp); // <--------------
                }
            });
        }

        //  Data fetched from movidb API.
        else {

            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
            gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item,gridData);
            movieGrid.setAdapter(gridAdapter);

            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View item,
                                        int position, long rowId) {
                    // Retrieve item based on position
                    Movie tmp = gridAdapter.getItem(position);

                    // Fire selected listener event with item
                    listener.onItemSelected(tmp); // <--------------
                }
            });
            FetChMoviesApiRequest api = new FetChMoviesApiRequest();
            api.execute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        // Return view
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        View rootView = this.getView();
        if(!(mSharedPrefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_label_popular)).equals(oldSetting))) {

            if(mSharedPrefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_label_popular))
                    .equals(getString(R.string.pref_sort_fav))) {

                DatabaseHandler db = new DatabaseHandler(this.getActivity());
                movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
                gridData = new ArrayList<Movie>();
                gridData = db.getAll();
                // get data from db
                gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item,gridData);
                movieGrid.setAdapter(gridAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item,
                                            int position, long rowId) {
                        // Retrieve item based on position
                        Movie tmp = gridAdapter.getItem(position);

                        // Fire selected listener event with item
                        listener.onItemSelected(tmp); // <--------------
                    }
                });
            }
            else {

                // Since setting changed must update grid with new data.
                movieGrid.invalidateViews();
                mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
                gridData = new ArrayList<Movie>();
                gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item, gridData);
                movieGrid.setAdapter(gridAdapter);
                mProgressBar.setVisibility(View.VISIBLE);

                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item,
                                            int position, long rowId) {
                        // Retrieve item based on position
                        Movie tmp = gridAdapter.getItem(position);

                        // Fire selected listener event with item
                        listener.onItemSelected(tmp); // <--------------
                    }
                });
                FetChMoviesApiRequest api = new FetChMoviesApiRequest();
                api.execute();
            }
        }
        if(mSharedPrefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_label_popular))
                .equals(getString(R.string.pref_sort_fav))){
            DatabaseHandler db = new DatabaseHandler(this.getActivity());
            movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
            gridData = new ArrayList<Movie>();
            gridData = db.getAll();
            // get data from db
            gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item,gridData);
            movieGrid.setAdapter(gridAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View item,
                                        int position, long rowId) {
                    // Retrieve item based on position
                    Movie tmp = gridAdapter.getItem(position);

                    // Fire selected listener event with item
                    listener.onItemSelected(tmp); // <--------------
                }
            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.refresh) {
            if(!(mSharedPrefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_label_popular))
                    .equals(getString(R.string.pref_sort_fav)))){
                movieGrid.invalidateViews();
                Log.e("debug", "detected refresh button click");
                gridData = new ArrayList<Movie>();
                mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
                gridAdapter = new GridAdapter(this.getActivity(), R.layout.movie_grid_item,gridData);
                movieGrid.setAdapter(gridAdapter);

                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item,
                                            int position, long rowId) {
                        // Retrieve item based on position
                        Movie tmp = gridAdapter.getItem(position);

                        // Fire selected listener event with item
                        listener.onItemSelected(tmp); // <--------------
                    }
                });
                FetChMoviesApiRequest api = new FetChMoviesApiRequest();
                api.execute();
                mProgressBar.setVisibility(View.VISIBLE);
            }
            else{
                DatabaseHandler db = new DatabaseHandler(this.getActivity());
                movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
                gridData = new ArrayList<Movie>();
                gridData = db.getAll();
                // get data from db
                gridAdapter = new GridAdapter(mCotext, R.layout.movie_grid_item,gridData);
                movieGrid.setAdapter(gridAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item,
                                            int position, long rowId) {
                        // Retrieve item based on position
                        Movie tmp = gridAdapter.getItem(position);

                        // Fire selected listener event with item
                        listener.onItemSelected(tmp); // <--------------
                    }
                });
            }
        }

        return super.onOptionsItemSelected(item);
    }


    protected class FetChMoviesApiRequest extends AsyncTask<String,Void,Integer> {
        //  trailers link
        //  http://api.themoviedb.org/3/movie/122917/videos?api_key=005b0025bac9ea712583f4c6e318909b
        //  reviews link
        //  http://api.themoviedb.org/3/movie/122917/reviews?api_key=005b0025bac9ea712583f4c6e318909b
        final String baseURI = "http://api.themoviedb.org/3";
        final String discover = "discover/movie";
        final String movie = "movie";
        final String video = "videos";
        final String reviews = "reviews";
        final String SORT_PARAM = "sort_by";
        final String API_KEY = "api_key";
        final String apiKey = getString(R.string.api_key);

        private BufferedReader bufferedReader = null;
        private HttpURLConnection urlConnection = null;

        protected void getTrailers(String movieId){
            String response;
            Uri uri = Uri.parse(baseURI).buildUpon().
                    appendEncodedPath(movie).
                    appendEncodedPath(movieId).
                    appendEncodedPath(video).
                    appendQueryParameter(API_KEY,apiKey).build();
            URL url = null;
            try {
                url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                    //  stop
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    buffer.append(line + "/n");
                }
                response = buffer.toString();

                // process for trailers
                processResponse(response);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void getReviews(String movieId){
            Uri uri = Uri.parse(baseURI).buildUpon().
                    appendEncodedPath(movie).
                    appendEncodedPath(movieId).
                    appendEncodedPath(reviews).
                    appendQueryParameter(API_KEY,apiKey).build();
        }

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 1;
            String sortParam = mSharedPrefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_label_popular));
            String response = null;
            Uri uri = Uri.parse(baseURI).buildUpon().
                    appendEncodedPath(discover).
                    appendQueryParameter(SORT_PARAM,sortParam).
                    appendQueryParameter(API_KEY,apiKey).build();
            URL url = null;
            try {
                url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                    return null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    buffer.append(line + "/n");
                }
                response = buffer.toString();
                processResponse(response);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                result = 0;
            }
            catch (IOException e) {
                e.printStackTrace();
                result = 0;
            } catch (JSONException e) {
                e.printStackTrace();
                result =  0;
            }

            result = 1;
            return result;
        }

        private void processResponse(String response) throws JSONException,IOException {

            JSONObject ob = new JSONObject(response);
            JSONArray results = ob.getJSONArray("results");
            Movie movie;
            for (int i=0; i<results.length();i++){
                JSONObject tempJSON = results.getJSONObject(i);
                movie = new Movie();
                movie.setTitle(tempJSON.getString("original_title"));
                movie.setPosterURL(tempJSON.getString("poster_path"));
                movie.setOverview(tempJSON.getString("overview"));
                movie.setRealseDate(tempJSON.getString("release_date"));
                movie.setRating(tempJSON.getString("vote_average"));
                movie.setMovieId(tempJSON.getString("id"));
                gridData.add(movie);
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                gridAdapter.setGridData(gridData);
            }
            mProgressBar.setVisibility(View.GONE);
        }

    }


}
