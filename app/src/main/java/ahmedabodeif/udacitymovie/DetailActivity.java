package ahmedabodeif.udacitymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Fetch the item to display from bundle
        //Item item = (Item) getIntent().getSerializableExtra("item");
        Intent intent = getIntent();
        Movie tmp = new Movie();
        tmp.setMovieId(savedInstanceState.getString("id"));
        tmp.setOverview(savedInstanceState.getString("id"));
        tmp.setRealseDate(savedInstanceState.getString("id"));
        tmp.setRating(savedInstanceState.getString("id"));
        tmp.setTitle(savedInstanceState.getString("id"));
        tmp.setImage(savedInstanceState.getByteArray("image"));

        if (savedInstanceState == null) {
            // Insert detail fragment based on the item passed
            DetailActivityFragment fragmentItemDetail = DetailActivityFragment.newInstance(tmp); // <-------
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItemDetail);
            ft.commit();
        }
    }
}
