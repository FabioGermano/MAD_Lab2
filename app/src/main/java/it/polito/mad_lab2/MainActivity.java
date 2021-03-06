package it.polito.mad_lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import it.polito.mad_lab2.data.reservation.ReservationEntity;
import it.polito.mad_lab2.reservation.ReservationsActivity;

public class MainActivity extends BaseBarraLaterale{

    /*private PhotoViewer photoViewer;
    private Bitmap largeBitmap;*/

    private String name, email;
    private Bitmap logo;


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
        
        View header = null;

        //inizializzo menu laterale
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(drawer != null) {
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if(navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);
                header = navigationView.getHeaderView(0);
            }
        }

        checkDB();
                 
        if(header != null){
            ImageView restaurant_logo = (ImageView) header.findViewById(R.id.nav_drawer_logo);
            TextView restaurant_name = (TextView) header.findViewById(R.id.nav_drawer_name);
            TextView restaurant_email = (TextView) header.findViewById(R.id.nav_drawer_email);

            if (restaurant_name != null && restaurant_email != null && restaurant_logo != null) {
                if (name != null && name.compareTo("") != 0)
                    restaurant_name.setText(name);
                if (email != null  && email.compareTo("") != 0)
                    restaurant_email.setText(email);
                if (logo != null)
                    restaurant_logo.setImageBitmap(logo);
            }
        }


        //provaGson();
    }

    /*private void provaGson() {
        GestioneDB db = new GestioneDB();

        ReservationEntity res = db.getAllReservations(getApplicationContext());
    }*/

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

    @Override
    protected void ModificaProfilo() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.EditRestaurantProfile.class);
        startActivity(intent);
    }

    @Override
    protected void ModificaMenu() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneMenu.class);
        startActivity(intent);
    }

    @Override
    protected void ModificaOfferte() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneOfferte.class);
        startActivity(intent);
    }

    @Override
    protected void ModificaDisponibilità() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.EditAvailability.class);
        startActivity(intent);
    }

    @Override
    protected void TestPrenotazioni() {
        Intent intent = new Intent(getApplicationContext(), ReservationsActivity.class);
        startActivity(intent);
    }

    /*public void eseguiActivityModificaMenu(View v){
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
    }*/

    /*@Override
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
    }*/

    public void checkDB(){
        GestioneDB DB = new GestioneDB();

        /*
        DB.deleteDB(this, "db_menu");
        DB.deleteDB(this, "db_offerte");
        DB.deleteDB(this, "db_profilo");
        DB.deleteDB(this, "db_reservation");
        */
        
        DB.creaDB(this);

        name = DB.getRestaurantName(this);
        email = DB.getRestaurantEmail(this);
        logo = DB.getRestaurantLogo(this);
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