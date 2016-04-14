package it.polito.mad_lab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import it.polito.mad_lab2.data.reservation.Reservation;
import it.polito.mad_lab2.data.reservation.ReservationEntity;
import it.polito.mad_lab2.data.reservation.ReservedDish;
import it.polito.mad_lab2.data.reservation.User;
import it.polito.mad_lab2.photo_viewer.PhotoViewer;
import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab2.reservation.ReservationsActivity;

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

        checkDB();

        //provaGson();
    }

    private void provaGson() {
        GestioneDB db = new GestioneDB();

        ReservationEntity res = db.getAllReservations(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
    public void eseguiActivityModificaProfilo(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.EditRestaurantProfile.class);
        startActivity(intent);
    }
    public void eseguiActivityModificaDisponibilita(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.EditAvailability.class);
        startActivity(intent);
    }

    public void eseguiActivityReservations(View v){
        Intent intent = new Intent(getApplicationContext(), ReservationsActivity.class);
        startActivity(intent);
    }

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {
        // Salvo bitmap a DB (serializzate)
        largeBitmap = large;
        Toast toast = Toast.makeText(getApplicationContext(), "Listener", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        // leggi bitmap "large" da db
        return largeBitmap;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        Toast toast = Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**** created by Roby on 07/04/2016 *****/
    public void checkDB(){
        GestioneDB DB = new GestioneDB();

        /*
        DB.deleteDB(this, "db_menu");
        DB.deleteDB(this, "db_offerte");
        DB.deleteDB(this, "db_profilo");
        DB.deleteDB(this, "db_reservation");
        */
        
        DB.creaDB(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == 6709) {
            Toast toast = Toast.makeText(getApplicationContext(), "infatti", Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }
}