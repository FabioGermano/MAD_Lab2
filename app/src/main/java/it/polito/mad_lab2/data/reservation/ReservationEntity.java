package it.polito.mad_lab2.data.reservation;

import java.util.ArrayList;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservationEntity {
    public ReservationEntity() {
        this.reservations = new ArrayList<Reservation>();
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public ArrayList<Reservation> reservations;
}
