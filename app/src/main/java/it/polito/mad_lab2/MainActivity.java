package it.polito.mad_lab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import it.polito.mad_lab2.photo_viewer.PhotoViewer;
import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

public class MainActivity extends BaseActivity implements PhotoViewerListener {

    private PhotoViewer photoViewer;
    private Bitmap largeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }

        SetSaveButtonVisibility(true);
        SetCalendarButtonVisibility(false);
        SetBackButtonVisibility(false);
        SetSaveButtonVisibility(false);
        setTitleTextView(getResources().getString(R.string.app_name));
        setContentView(R.layout.activity_main);

        PhotoViewer currentFragment = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.ad_fragment);

        checkDB();
    }

    @Override
    protected void OnSaveButtonPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Save pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void OnAlertButtonPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Alert pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void OnCalendarButtonPressed() {

        Toast toast = Toast.makeText(getApplicationContext(), "Calendar pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void OnBackButtonPressed() {

    }

    public void eseguiActivityModificaMenu(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneMenu.class);
        startActivity(intent);
    }

    public void eseguiActivityModificaOfferte(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneOfferte.class);
        startActivity(intent);
    }

    @Override
    public void OnPhotoChanged(Bitmap thumb, Bitmap large) {
        // Salvo bitmap a DB (serializzate)
        largeBitmap = large;
        Toast toast = Toast.makeText(getApplicationContext(), "Listener", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting() {
        // leggi bitmap "large" da db
        return largeBitmap;
    }

    @Override
    public void OnPhotoRemoved() {
        Toast toast = Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**** created by Roby on 07/04/2016 *****/
    public void checkDB(){
        GestioneDB DB = new GestioneDB();
        //DB.deleteDB(this, "db_menu");
        //DB.deleteDB(this, "db_offerte");
        DB.creaDB(this);
    }
}