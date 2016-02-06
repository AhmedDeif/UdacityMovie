package ahmedabodeif.udacitymovie;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment {

    private ArrayAdapter<Item> adapterItems;
    private ListView lvItems;
    private OnListItemSelectedListener listener;

    public interface OnListItemSelectedListener {
        public void onItemSelected(Item item);
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
        // Create arraylist from item fixtures
        ArrayList<Item> items = Item.getItems();
        // Create adapter based on items
        adapterItems = new ArrayAdapter<Item>(getActivity(),
                android.R.layout.simple_list_item_activated_1, items);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_movies,
                container, false);
        // Attach adapter to listview
        lvItems = (ListView) view.findViewById(R.id.lvItems);
        lvItems.setAdapter(adapterItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item,
                                    int position, long rowId) {
                // Retrieve item based on position
                Item ite = adapterItems.getItem(position);
                // Fire selected listener event with item
                listener.onItemSelected(ite); // <--------------
            }
        });
        // Return view
        return view;
    }
}
