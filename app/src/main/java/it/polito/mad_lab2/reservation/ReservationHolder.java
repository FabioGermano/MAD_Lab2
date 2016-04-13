package it.polito.mad_lab2.reservation;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
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
public class ReservationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private Reservation reservation;
    private TextView username, type, time;
    private LinearLayout offerListLayout, dishListLayout;
    private ImageButton expandeCollapseButton;
    private LinearLayout childLayout;
    private boolean state = true;
    private View containerView;

    public ReservationHolder(View v, Context context) {
        super(v);

        this.context = context;

        containerView = v;
        username = (TextView) v.findViewById(R.id.username);
        type = (TextView) v.findViewById(R.id.type);
        time = (TextView) v.findViewById(R.id.time);
        childLayout = (LinearLayout)v.findViewById(R.id.childLayout);

        offerListLayout = (LinearLayout)v.findViewById(R.id.offerListLayout);
        dishListLayout = (LinearLayout)v.findViewById(R.id.dishListLayout);
        expandeCollapseButton = (ImageButton)v.findViewById(R.id.expandeCollapseRow);
        expandeCollapseButton.setOnClickListener(this);
    }

    public void setData(Reservation reservation){
        this.reservation=reservation;
        type.setText(reservation.getType());
        username.setText(reservation.getUser().getName());
        time.setText(reservation.getTime());

        ArrayList<ReservedDish> reservedDish = reservation.getReservedDishes(false);
        ArrayList<ReservedDish> reservedOffers = reservation.getReservedDishes(true);

        if(reservation.getPlaces()==null) {
            ((TextView) childLayout.findViewById(R.id.seats_number)).setVisibility(View.GONE);
            ((TextView) childLayout.findViewById(R.id.seats)).setVisibility(View.GONE);
        }

        if(reservedDish.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.BOLD );
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.BOLD);
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
        else
        {
            ((TextView) childLayout.findViewById(R.id.dishes)).setVisibility(View.GONE);
            ((LinearLayout) childLayout.findViewById(R.id.dishListLayout)).setVisibility(View.GONE);
        }

        if(reservedOffers.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.BOLD);
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.BOLD);
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
        else
        {
            ((TextView) childLayout.findViewById(R.id.offers)).setVisibility(View.GONE);
            ((LinearLayout) childLayout.findViewById(R.id.offerListLayout)).setVisibility(View.GONE);
        }
    }

    private void setBorder(View v)
    {
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(border);
        } else {
            v.setBackground(border);
        }
    }

    private void expandOrCollapse(final View v,String exp_or_colpse, int height) {
        TranslateAnimation anim = null;
        if(exp_or_colpse.equals("expand"))
        {
            anim = new TranslateAnimation(0.0f, 0.0f, -height, 0.0f);
            anim.setDuration(150);
            v.setVisibility(View.VISIBLE);
        }
        else{
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -height);
            anim.setDuration(150);
            Animation.AnimationListener collapselistener= new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            anim.setAnimationListener(collapselistener);
        }

        // To Collapse
        //

        anim.setDuration(200);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        if(!state){
            (containerView.findViewById(R.id.expandeCollapseRow)).setBackgroundResource(android.R.drawable.arrow_up_float);
            expandOrCollapse(childLayout, "collapse", childLayout.getHeight());
            //expandOrCollapse(containerView, "collapse", containerView.getHeight() - childLayout.getHeight());
            state = true;
        }
        else
        {
            (containerView.findViewById(R.id.expandeCollapseRow)).setBackgroundResource(android.R.drawable.arrow_down_float);
            expandOrCollapse(childLayout, "expand", childLayout.getHeight());
            //expandOrCollapse(containerView, "expand", containerView.getHeight() - childLayout.getHeight());
            state = false;
        }
    }
}
