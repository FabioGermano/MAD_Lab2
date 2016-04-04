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


public abstract class BaseActivity extends AppCompatActivity{

    private ImageButton saveImageButton, alertButton;
    private TextView titleTextView, alertCountView;
    protected RelativeLayout alertDetailsView;

    private LinearLayout FAB_layout;
    private ImageButton fab_edit, fab_remove, fab_add;

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
        SetAlertCount(0);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    protected void SetSaveButtonVisibility(boolean visible)
    {
        if(!visible)
            this.saveImageButton.setVisibility(View.INVISIBLE);
        else
            this.saveImageButton.setVisibility(View.VISIBLE);
    }

    protected void SetAlertButtonVisibility(boolean visible)
    {
        if(!visible)
            this.alertButton.setVisibility(View.INVISIBLE);
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
    protected abstract void OnDeleteButtonPressed();

    protected void SetAlertDelatilsView(int id)
    {
        findViewById(id).bringToFront();
        this.alertDetailsView =(RelativeLayout)findViewById(id);
    }

    protected void InitializeFABButtons(boolean editVisibility, boolean removeViibility, boolean addVisibility)
    {
        FAB_layout = (LinearLayout)findViewById(R.id.FAB_layout);
        FAB_layout.bringToFront();

        fab_edit = (ImageButton)findViewById(R.id.fab_edit);
        fab_add = (ImageButton)findViewById(R.id.fab_add);
        fab_remove = (ImageButton)findViewById(R.id.fab_remove);

        if(editVisibility)
        {
           this.fab_edit.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_edit.setVisibility(View.INVISIBLE);
        }

        if(removeViibility)
        {
            this.fab_remove.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_remove.setVisibility(View.INVISIBLE);
        }

        if(addVisibility)
        {
            this.fab_add.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_add.setVisibility(View.INVISIBLE);
        }

        fab_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDeleteButtonPressed();
            }
        });
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
