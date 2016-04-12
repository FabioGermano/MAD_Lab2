package it.polito.mad_lab2.reservation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import junit.framework.Test;

import it.polito.mad_lab2.R;
import it.polito.mad_lab2.data.reservation.Reservation;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class ReservationHolder extends RecyclerView.ViewHolder {

   private Reservation reservation;
    private TextView username, type, time;

    public ReservationHolder(View v) {
        super(v);
        username = (TextView) v.findViewById(R.id.username);
        type = (TextView) v.findViewById(R.id.type);
        time = (TextView) v.findViewById(R.id.time);
    }

    public void setData(Reservation reservation){
       this.reservation=reservation;
        type.setText(reservation.getType());
        username.setText(reservation.getUser().getName());
        time.setText(reservation.getDate());
    }
}
