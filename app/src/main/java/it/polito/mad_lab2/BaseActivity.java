package it.polito.mad_lab2;

import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * To be extended by every activity.
 *
 * @author f.germano
 */
public abstract class BaseActivity extends AppCompatActivity{

    private ImageButton saveImageButton, alertButton, calendarButton;
    private TextView titleTextView, alertCountView;
    protected RelativeLayout alertDetailsView;
    String activityTitle =  "Lab2";

    private boolean hideToolbar=false, save_visibility=false, calendar_visibility=false, alert_visibility = true, backbutton_visibility=true;;

    private int alertCount = 0;
    private boolean isAlertExpanded = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean useToolbar() {
        return true;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureToolbar(view);
        super.setContentView(view);
    }
    protected void hideToolbar(boolean bool){
        hideToolbar = bool;

    }

    private void configureToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (useToolbar()) {

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(backbutton_visibility);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
                if(hideToolbar) {
                    //getSupportActionBar().hide();
                    titleTextView.setVisibility(View.GONE);
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                }
                // Get access to the custom title view
                titleTextView.setText(activityTitle);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    protected void setTitleTextView(String title){
        if(title!=null)
            activityTitle=title;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        toolbar.inflateMenu(R.menu.action_bar);
        MenuItem notification = menu.findItem(R.id.menu_notify);
        notification.setVisible(alert_visibility);
        if(alert_visibility){

            RelativeLayout notificationLayout = (RelativeLayout) notification.getActionView();
            alertButton = (ImageButton) notificationLayout.findViewById(R.id.alertButton);
            alertCountView = (TextView) notificationLayout.findViewById(R.id.alertCountView);
            SetAlertCount(alertCount);
            // Define the listener
            MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when action item collapses
                    return true;  // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;  // Return true to expand action view
                }
            };
            // Assign the listener to that action item
            MenuItemCompat.setOnActionExpandListener(notification, expandListener);

        }

        menu.findItem(R.id.menu_save).setVisible(save_visibility);
        menu.findItem(R.id.menu_calendar).setVisible(calendar_visibility);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                OnBackButtonPressed();
            case R.id.menu_save:
                OnSaveButtonPressed();
                break;
            case R.id.menu_calendar:
                OnCalendarButtonPressed();
                break;
            case R.id.menu_notify:
                OnAlertButtonPressed();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    protected void SetCalendarButtonVisibility(boolean visible)
    {
        calendar_visibility=visible;
    }

    protected void SetSaveButtonVisibility(boolean visible)
    {
        save_visibility=visible;
    }

    protected void SetAlertButtonVisibility(boolean visible)
    {
        alert_visibility=visible;
    }

    protected void SetBackButtonVisibility(boolean visible)
    {
        backbutton_visibility=visible;
    }

    protected void SetAlertCount(int count)
    {
        this.alertCount = count;
        this.alertCountView.setText(String.valueOf(count));
    }

    protected abstract void OnSaveButtonPressed();
    protected abstract void OnAlertButtonPressed();
    protected abstract void OnCalendarButtonPressed();
    protected abstract void OnBackButtonPressed();

    protected void SetAlertDelatilsView(int id)
    {
        findViewById(id).bringToFront();
        this.alertDetailsView =(RelativeLayout)findViewById(id);
    }

    public void expandOrCollapse(final View v,String exp_or_colpse) {
        TranslateAnimation anim = null;
        if(exp_or_colpse.equals("expand"))
        {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            anim.setDuration(200);
            v.setVisibility(View.VISIBLE);
        }
        else{
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
            anim.setDuration(200);
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

        anim.setDuration(300);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }
}
