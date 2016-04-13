package it.polito.mad_lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giovanna on 11/04/2016.
 */
public class ListAdapter extends ArrayAdapter<ListElement>{

    Context context;
    int layoutResourceId;

    public ListAdapter(Context context, ArrayList<ListElement> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListElement element = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_row, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.food_name);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        // Populate the data into the template view using the data object

        name.setText(element.getName());
        quantity.setText(element.getQuantity());

        // Return the completed view to render on screen
        return convertView;
    }
}
