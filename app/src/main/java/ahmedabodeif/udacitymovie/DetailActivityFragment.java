package ahmedabodeif.udacitymovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
public class DetailActivityFragment extends Fragment {
    Item item;
    Movie movie = new Movie();

    private Activity mActivity;
    private Context mContext;
    private PackageManager mPackageManger;
    SharedPreferences mSharedPrefs;

    ArrayList<Trailer> trailerList = new ArrayList<Trailer>();
    ArrayList<MovieReview> reviewsList = new ArrayList<MovieReview>();


    public static DetailActivityFragment newInstance(Item item) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    public static DetailActivityFragment newInstance(Movie item) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putString("id",item.getMovieId());
        args.putString("description",item.getOverview());
        args.putString("movieDate",item.getRealseDate());
        args.putString("movieRating",item.getRating());
        args.putString("movieTitle", item.getTitle());
        args.putString("ovieLength", item.getLength());
        args.putByteArray("image", item._image);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //item = (Item) getArguments().getSerializable("item");
        movie.setLength(getArguments().getString("movieLength"));
        movie.setMovieId(getArguments().getString("id"));
        movie.setOverview(getArguments().getString("description"));
        movie.setRealseDate(getArguments().getString("movieDate"));
        movie.setRating(getArguments().getString("movieRating"));
        movie.setTitle(getArguments().getString("movieTitle"));
        movie.setImage(getArguments().getByteArray("image"));
        movie._image = getArguments().getByteArray("image");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        mActivity = this.getActivity();
        mContext = this.getContext();
        mPackageManger = this.getActivity().getPackageManager();
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);





        //TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        //TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        //tvTitle.setText(item.getTitle());
        //tvBody.setText(item.getBody());
        view = phoneCreateView(view);


        return view;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    public View phoneCreateView(View rootView){

        TextView movieTitle = (TextView) rootView.findViewById(R.id.movieTitle);
        movieTitle.setText("\t"+movie.getTitle());
        // setting movie rating
        TextView movieRating = (TextView) rootView.findViewById(R.id.ratingText);
        movieRating.setText(movie.getRating());
        //ImageView image = (ImageView) findViewById(R.id.imageView);
        //Picasso.with(this.getBaseContext()).load(intent.getStringExtra("moviePoster")).into(image);
        // setting movie date
        TextView movieDate = (TextView) rootView.findViewById(R.id.movieProductionYear);
        movieDate.setText(movie.getRealseDate());
        //setting movie description
        TextView movieDescription = (TextView) rootView.findViewById(R.id.movieDescription);
        movieDescription.setText(movie.getOverview());

        Button bt = (Button) rootView.findViewById(R.id.favouriteButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  insert into db
                Movie tmp = new Movie();
                tmp.setMovieId(movie.getMovieId());
                tmp.setOverview(movie.getOverview());
                tmp.setRealseDate(movie.getRealseDate());
                tmp.setRating(movie.getRating());
                tmp.setTitle(movie.getTitle());
                tmp._image = movie._image;
                // try creating a bitmap here to see the byte arry from intent
                ByteArrayInputStream imageStream = new ByteArrayInputStream(tmp._image);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                DatabaseHandler db = new DatabaseHandler(mContext);
                db.addMovie(tmp);
            }
        });
        String[] params = {"hi"};
        if(mSharedPrefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_label_popular)).
                equals(getString(R.string.pref_sort_fav))){
            ByteArrayInputStream imageStream = new ByteArrayInputStream(movie._image);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            ImageView image = (ImageView) rootView.findViewById(R.id.imageView);
            image.setImageBitmap(theImage);
            params[0] = movie.getMovieId();
            GetMovieDetailsApiRequest task = (GetMovieDetailsApiRequest) new GetMovieDetailsApiRequest().execute(params);
        }
        else {
            ImageView image = (ImageView) rootView.findViewById(R.id.imageView);
            //Picasso.with(mContext).load(movie.getPosterURL()).into(image);
            image.setImageBitmap(movie.getMoviePoster());
            params[0] = movie.getMovieId();
            GetMovieDetailsApiRequest task = (GetMovieDetailsApiRequest) new GetMovieDetailsApiRequest().execute(params);
        }
        return rootView;
    }

    private class GetMovieDetailsApiRequest extends AsyncTask<String,Void,Void> {

        //  trailers link
        //  http://api.themoviedb.org/3/movie/122917/videos?api_key=005b0025bac9ea712583f4c6e318909b
        //  reviews link
        //  http://api.themoviedb.org/3/movie/122917/reviews?api_key=005b0025bac9ea712583f4c6e318909b
        final String baseURI = "http://api.themoviedb.org/3";
        final String movie = "movie";
        final String video = "videos";
        final String reviews = "reviews";
        final String API_KEY = "api_key";
        final String apiKey = getString(R.string.api_key);
        String movieID = "";

        private BufferedReader bufferedReader = null;
        private HttpURLConnection urlConnection = null;

        private void getMovieReviews(String movieID){
            String response;

            Uri uri = Uri.parse(baseURI).buildUpon().
                    appendEncodedPath(movie).
                    appendEncodedPath(movieID).
                    appendEncodedPath(reviews).
                    appendQueryParameter(API_KEY,apiKey).
                    build();
            URL url = null;
            try {
                url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                    return;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    buffer.append(line + "/n");
                }
                response = buffer.toString();

                // process for trailers
                processResponseReviews(response);
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
                    return;
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
        protected Void doInBackground(String... params) {
            getTrailers(params[0]);
            getMovieReviews(params[0]);
            movieID = params[0];
            return null;
        }

        private void processResponse(String response) throws JSONException {


            JSONObject ob = new JSONObject(response);
            JSONArray results = ob.getJSONArray("results");
            Trailer trailer;
            for (int i=0; i<results.length();i++){
                JSONObject tempJSON = results.getJSONObject(i);
                trailer = new Trailer();
                trailer.setURL(tempJSON.getString("key"));
                trailerList.add(trailer);
            }
        }

        private void processResponseReviews(String response) throws JSONException{
            JSONObject ob = new JSONObject(response);
            JSONArray results = ob.getJSONArray("results");
            MovieReview review;
            for (int i=0; i<results.length();i++){
                JSONObject tempJSON = results.getJSONObject(i);
                review = new MovieReview();
                review.setAuthorName(tempJSON.getString("author"));
                review.setReviewBody(tempJSON.getString("content"));
                reviewsList.add(review);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Download complete. Let us update UI
            LayoutInflater vi = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout lin = (LinearLayout) mActivity.findViewById(R.id.movie_detail);

            for(int i=0;i<trailerList.size();i++){
                final Trailer tempTrailer = trailerList.get(i);
                View custom = vi.inflate(R.layout.trailers_list_item,null);
                lin.addView(custom);
                TextView temp = (TextView) custom.findViewById(R.id.trailerText);
                ImageButton ib = (ImageButton) custom.findViewById(R.id.PlayTrailer);
                ib.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Uri webpage = Uri.parse(tempTrailer.getURL());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(webpage);
                        if (intent.resolveActivity(mPackageManger) != null) {
                            startActivity(intent);
                        }
                    }
                });

                temp.setText("Trailer " + (i+1));
                temp.setId(View.generateViewId());
            }
            /*
            if(!trailerList.isEmpty() && mShareActionProvider!=null) {
                mMovieString = trailerList.get(0).getURL();
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }else{mMovieString = null;}*/

            LayoutInflater vil = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout li = (LinearLayout) mActivity.findViewById(R.id.movie_detail);
            for(int i=0; i<reviewsList.size();i++){
                final MovieReview tempReview = reviewsList.get(i);
                View custom = vil.inflate(R.layout.review_list_item,null);
                li.addView(custom);
                TextView author = (TextView) custom.findViewById(R.id.authorName);
                author.setText(tempReview.getAuthorName());
                TextView content = (TextView) custom.findViewById(R.id.reviewContent);
                content.setText(tempReview.getReviewBody());
            }
        }
    }


}
