package it.polito.mad_lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

    private int alertCount = 0;
    private boolean isAlertExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.toolbar_view, null);
        titleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        titleTextView.setText("Lab2");

        calendarButton = (ImageButton) mCustomView.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnCalendarButtonPressed();
            }
        });

        saveImageButton = (ImageButton) mCustomView.findViewById(R.id.saveButton);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSaveButtonPressed();
            }
        });

        alertButton = (ImageButton) mCustomView.findViewById(R.id.alertButton);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAlertExpanded) {
                    expandOrCollapse(alertDetailsView, "expand");
                    isAlertExpanded = true;
                }
                else {
                    expandOrCollapse(alertDetailsView, "collapse");
                    isAlertExpanded = false;
                }

                OnAlertButtonPressed();
            }
        });

        alertCountView = (TextView) mCustomView.findViewById(R.id.alertCountView);
        SetAlertCount(22);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    protected void SetCalendarButtonVisibility(boolean visible)
    {
        if(!visible)
            this.calendarButton.setVisibility(View.GONE );
        else
            this.calendarButton.setVisibility(View.VISIBLE);
    }

    protected void SetSaveButtonVisibility(boolean visible)
    {
        if(!visible)
            this.saveImageButton.setVisibility(View.GONE);
        else
            this.saveImageButton.setVisibility(View.VISIBLE);
    }

    protected void SetAlertButtonVisibility(boolean visible)
    {
        if(!visible)
            this.alertButton.setVisibility(View.GONE);
        else
            this.alertButton.setVisibility(View.VISIBLE);
    }

    protected void SetAlertCount(int count)
    {
        this.alertCount = count;
        this.alertCountView.setText(String.valueOf(count));
    }

    protected abstract void OnSaveButtonPressed();
    protected abstract void OnAlertButtonPressed();
    protected abstract void OnCalendarButtonPressed();

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
