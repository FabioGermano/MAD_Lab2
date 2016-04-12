package it.polito.mad_lab2.reservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab2.R;
import it.polito.mad_lab2.data.reservation.Reservation;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class RecyclerAdapterReservations extends RecyclerView.Adapter<ReservationHolder> {

    private LayoutInflater myInflater;
    private Context context;
    ArrayList<Reservation> data;

    public RecyclerAdapterReservations(Context context, ArrayList<Reservation> data) {
        this.data = data;
        this.myInflater = LayoutInflater.from(context);
    }

    @Override
    public ReservationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.reservation_row, parent, false);
        ReservationHolder holder = new ReservationHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReservationHolder holder, int position) {
        holder.setData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
