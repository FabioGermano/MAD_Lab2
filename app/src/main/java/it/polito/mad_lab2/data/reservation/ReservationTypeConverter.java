package it.polito.mad_lab2.data.reservation;

import it.polito.mad_lab2.common.PhotoType;

/**
 * Created by f.germano on 11/04/2016.
 */
public final class ReservationTypeConverter
{

    public static ReservationType fromTabPosition(int position){
        switch (position) {
            case 0:
                return ReservationType.PENDING;
            case 1:
                return ReservationType.ACCEPTED;
            case 2:
                return ReservationType.DELETED;
            case 3:
                return ReservationType.REJECTED;
        }
        return  null;
    }
    public static String toString(ReservationType reservationType)
    {
        if(reservationType == ReservationType.PENDING){
            return "Pending";
        }
        else if(reservationType == ReservationType.ACCEPTED){
            return "Accepted";
        }
        else if(reservationType == ReservationType.DELETED){
            return "Deleted";
        }
        else if(reservationType == ReservationType.REJECTED)
        {
            return "Rejected";
        }

        return null;
    }
}
