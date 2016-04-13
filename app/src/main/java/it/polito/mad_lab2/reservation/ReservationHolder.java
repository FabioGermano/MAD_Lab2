package it.polito.mad_lab2.reservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Test;

import it.polito.mad_lab2.R;
import it.polito.mad_lab2.data.reservation.Reservation;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class ReservationHolder extends RecyclerView.ViewHolder {

    private Context context;
    private Reservation reservation;
    private TextView username, type, time;
    private ListView reservedDishListview, reservedOfferListview;

    public ReservationHolder(View v, Context context) {
        super(v);

        this.context = context;

        username = (TextView) v.findViewById(R.id.username);
        type = (TextView) v.findViewById(R.id.type);
        time = (TextView) v.findViewById(R.id.time);

        reservedDishListview = (ListView)v.findViewById(R.id.listView_dishes);
        reservedOfferListview = (ListView)v.findViewById(R.id.listView_offers);
    }

    public void setData(Reservation reservation){
        this.reservation=reservation;
        type.setText(reservation.getType());
        username.setText(reservation.getUser().getName());
        time.setText(reservation.getDate());

        ReservedDishListAdapter listAdapterDish = new ReservedDishListAdapter(this.context, reservation.getReservedDishes(false));
        reservedDishListview.setAdapter(listAdapterDish);

        ReservedDishListAdapter listAdapterOffer = new ReservedDishListAdapter(this.context, reservation.getReservedDishes(true));
        reservedOfferListview.setAdapter(listAdapterOffer);
    }
}
