package ahmedabodeif.udacitymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements MoviesActivityFragment.OnListItemSelectedListener {

    private boolean isTwoPane = false;
    public GridAdapter gridAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        determinePaneLayout();
        Log.e("Tablet",isTwoPane+"");
    }

    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.flDetailContainer);
        // If there is a second pane for details
        if (fragmentItemDetail != null) {
            isTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public void onItemSelected(Movie item) {
        if (isTwoPane) { // single activity with list and detail
            // Replace framelayout with new detail fragment
            DetailActivityFragment fragmentItem = DetailActivityFragment.newInstance(item);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.commit();
        } else { // go to separate activity
            // launch detail activity using intent
            Intent i = new Intent(this, DetailActivity.class);
           // i.putExtra("item", item); Movie tmp = (Movie) gridData.get(position);
            i.putExtra("movieTitle",item.getTitle());
            i.putExtra("movieRating",item.getRating());
            i.putExtra("moviePoster",item.getPosterURL());
            i.putExtra("movieDate",item.getRealseDate());
            i.putExtra("movieLength",item.getLength());
            i.putExtra("description",item.getOverview());
            i.putExtra("id", item.getMovieId());
            i.putExtra("image",item._image);

            if(item._image == null)
                item.setMoviePoster(item.getMoviePoster());
            i.putExtra("image",item._image);
            // put movie details here
            startActivity(i);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if(id == R.id.refresh) {
        }



        return super.onOptionsItemSelected(item);
    }
}
