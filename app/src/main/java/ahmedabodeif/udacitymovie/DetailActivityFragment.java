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


    public static DetailActivityFragment newInstance(Item item) {
        DetailActivityFragment fragmentDemo = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,
                container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        tvTitle.setText(item.getTitle());
        tvBody.setText(item.getBody());
        return view;
    }
}
