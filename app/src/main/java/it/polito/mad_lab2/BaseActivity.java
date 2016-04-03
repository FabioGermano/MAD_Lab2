package it.polito.mad_lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public abstract class BaseActivity extends AppCompatActivity{

    private ImageButton saveImageButton, backImageButton, alertButton;
    private TextView titleTextView, alertCountView;

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

        backImageButton = (ImageButton) mCustomView.findViewById(R.id.backButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackButtonPressed();
                finish();
            }
        });

        alertButton = (ImageButton) mCustomView.findViewById(R.id.alertButton);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAlertButtonPressed();
            }
        });

        alertCountView = (TextView) mCustomView.findViewById(R.id.alertCountView);

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

    protected void SetBackButtonVisibility(boolean visible)
    {
        if(!visible)
            this.backImageButton.setVisibility(View.INVISIBLE);
        else
            this.backImageButton.setVisibility(View.VISIBLE);
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
        this.alertCountView.setText(String.valueOf(count));
    }

    protected abstract void OnSaveButtonPressed();
    protected abstract void OnBackButtonPressed();
    protected abstract void OnAlertButtonPressed();
}
