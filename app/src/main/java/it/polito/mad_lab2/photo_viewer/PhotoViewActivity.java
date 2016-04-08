package it.polito.mad_lab2.photo_viewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.File;

import it.polito.mad_lab2.EditableBaseActivity;
import it.polito.mad_lab2.R;

public class PhotoViewActivity extends EditableBaseActivity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);
        SetAlertButtonVisibility(true);

        setContentView(R.layout.activity_photo_view);

        InitializeFABButtons(true, true, false);

        touchImageView = (TouchImageView)findViewById(R.id.photoView);

        getBitmap(getIntent().getExtras());

    }

    private void getBitmap(Bundle savedInstanceState)
    {
        String path = savedInstanceState.getString("photoPath");

        if(path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            touchImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void OnSaveButtonPressed() {

    }

    @Override
    protected void OnAlertButtonPressed() {

    }

    @Override
    protected void OnCalendarButtonPressed() {

    }

    @Override
    protected void OnBackButtonPressed() {
        finish();
    }

    @Override
    protected void OnDeleteButtonPressed() {

    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

    }
}
