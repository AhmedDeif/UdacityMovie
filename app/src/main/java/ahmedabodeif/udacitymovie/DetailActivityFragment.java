package ahmedabodeif.udacitymovie;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    Item item;
    Movie currentMovie;


    public static DetailActivityFragment newInstance(Item item) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    /*

     Movie tmp = new Movie();
                tmp.setMovieId(intent.getStringExtra("id"));
                tmp.setOverview(intent.getStringExtra("description"));
                tmp.setRealseDate(intent.getStringExtra("movieDate"));
                tmp.setRating(intent.getStringExtra("movieRating"));
                tmp.setTitle(intent.getStringExtra("movieTitle"));
                tmp._image = intent.getByteArrayExtra("image");
                // try creating a bitmap here to see the byte arry from intent
                ByteArrayInputStream imageStream = new ByteArrayInputStream(tmp._image);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                DatabaseHandler db = new DatabaseHandler(mContext);
                db.addMovie(tmp);

     */
    public static DetailActivityFragment newInstance(String id, String description,
                                                     String movieDate, String movieRating,
                                                     String movieTitle, byte[] posterArray ) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        //args.putSerializable("item", item);
        args.putString("id",id);
        args.putString("description",description);
        args.putString("movieDate",movieDate);
        args.putString("movieRating",movieRating);
        args.putString("movieTitle",movieTitle);
        args.putByteArray("image",posterArray);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    public static DetailActivityFragment newInstance(Movie movie) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        //args.putSerializable("item", item);
        args.putString("id",movie.getMovieId());
        args.putString("description",movie.getOverview());
        args.putString("movieDate",movie.getRealseDate());
        args.putString("movieRating",movie.getRating());
        args.putString("movieTitle",movie.getTitle());
        args.putByteArray("image",movie._image);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("item");
        Bundle bundle = getArguments();
        // retrieve movie data from bundle
        currentMovie = new Movie();
        currentMovie.setMovieId(bundle.getString("id"));
        currentMovie.setOverview(bundle.getString("id"));
        currentMovie.setRealseDate(bundle.getString("id"));
        currentMovie.setRating(bundle.getString("id"));
        currentMovie.setTitle(bundle.getString("id"));
        currentMovie.setImage(bundle.getByteArray("image"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        //TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        //TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        //tvTitle.setText(item.getTitle());
        //tvBody.setText(item.getBody());

        return view;
    }
}
