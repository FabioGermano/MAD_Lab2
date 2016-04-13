package it.polito.mad_lab2.reservation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Test;

import java.util.ArrayList;

import it.polito.mad_lab2.R;
import it.polito.mad_lab2.data.reservation.Reservation;
import it.polito.mad_lab2.data.reservation.ReservedDish;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class ReservationHolder extends RecyclerView.ViewHolder {

    private Context context;
    private Reservation reservation;
    private TextView username, type, time;
    private LinearLayout offerListLayout, dishListLayout;

    public ReservationHolder(View v, Context context) {
        super(v);

        this.context = context;

        username = (TextView) v.findViewById(R.id.username);
        type = (TextView) v.findViewById(R.id.type);
        time = (TextView) v.findViewById(R.id.time);

        offerListLayout = (LinearLayout)v.findViewById(R.id.offerListLayout);
        dishListLayout = (LinearLayout)v.findViewById(R.id.dishListLayout);
    }

    public void setData(Reservation reservation){
        this.reservation=reservation;
        type.setText(reservation.getType());
        username.setText(reservation.getUser().getName());
        time.setText(reservation.getTime());

        ArrayList<ReservedDish> reservedDish = reservation.getReservedDishes(false);
        ArrayList<ReservedDish> reservedOffers = reservation.getReservedDishes(true);

        if(reservedDish.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.NORMAL);
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.NORMAL);
            this.dishListLayout.addView(child);

            for (ReservedDish rd : reservedDish) {
                child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
                name = (TextView) child.findViewById(R.id.food_name);
                name.setText(rd.getName());
                quantity = (TextView) child.findViewById(R.id.quantity);
                quantity.setText(String.valueOf(rd.getQuantity()));

                this.dishListLayout.addView(child);
            }
        }

        if(reservedDish.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.NORMAL);
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.NORMAL);
            this.offerListLayout.addView(child);

            for (ReservedDish rd : reservedOffers) {
                child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
                name = (TextView) child.findViewById(R.id.food_name);
                name.setText(rd.getName());
                quantity = (TextView) child.findViewById(R.id.quantity);
                quantity.setText(String.valueOf(rd.getQuantity()));

                this.offerListLayout.addView(child);
            }
        }
    }
}
