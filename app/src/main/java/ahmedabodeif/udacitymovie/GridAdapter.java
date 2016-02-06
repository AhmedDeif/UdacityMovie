package ahmedabodeif.udacitymovie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedabodeif1 on 1/21/16.
 */
public class GridAdapter extends ArrayAdapter<Item> {


    Context context;
    ArrayList<Movie> data;
    int layoutResourceId;

    ArrayList<Item> itemsTemp;



    public GridAdapter(Context context, int resource, ArrayList<Item> data) {
        super(context, resource, data);
        this.layoutResourceId = resource;
        this.context = context;
        this.itemsTemp = data;
    }




    public void setGridData(ArrayList<Movie> mGridData) {
        this.data = mGridData;
        notifyDataSetChanged();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Row holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new Row();
            holder.imgView = (ImageView) row.findViewById(R.id.moviePoster);
            row.setTag(holder);
        } else {
            holder = (Row) row.getTag();
        }
        final Item item = itemsTemp.get(position);

        final Row finalHolder = holder;

        return row;
    }

    static class Row {
        ImageView imgView;
    }
}

