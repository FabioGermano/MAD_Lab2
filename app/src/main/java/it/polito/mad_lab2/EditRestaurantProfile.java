package it.polito.mad_lab2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

public class EditRestaurantProfile extends BaseActivity implements PhotoViewerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleTextView(getResources().getString(R.string.edit_restaurant_profile));
        SetSaveButtonVisibility(true);
        SetCalendarButtonVisibility(false);

        setContentView(R.layout.activity_edit_restaurant_profile);
        // Construct the data source

        readData();

    }

    private void readData(){
        try{
            GestioneDB DB = new GestioneDB();
            String profilo = DB.leggiDB(this, "db_profilo");

            JSONObject jsonRootObject = new JSONObject(profilo);

            JSONArray jsonArray = jsonRootObject.optJSONArray("profilo");

            JSONObject info = jsonArray.getJSONObject(0);
            JSONObject orari = jsonArray.getJSONObject(1);


            String name = info.optString("nome").toString();
            String address = info.optString("indirizzo").toString();
            String phone = info.optString("telefono");
            String email =info.optString("email");
            String description = info.optString("descrizione");


            String lun = getResources().getString(R.string.monday).toUpperCase();
            if (orari.optString("lun").compareTo("null") != 0){
                lun += "\n" + orari.optString("lun");
            }
            String mar = getResources().getString(R.string.tuesday).toUpperCase();
            if (orari.optString("mar").compareTo("null") != 0){
                mar += "\n" + orari.optString("mar");
            }
            String mer = getResources().getString(R.string.wednesday).toUpperCase();
            if (orari.optString("mer").compareTo("null") != 0){
                mer += "\n" + orari.optString("mer");
            }
            String gio = getResources().getString(R.string.thursday).toUpperCase();
            if (orari.optString("gio").compareTo("null") != 0){
                gio += "\n" + orari.optString("gio");
            }
            String ven = getResources().getString(R.string.friday).toUpperCase();
            if (orari.optString("ven").compareTo("null") != 0){
                ven += "\n" + orari.optString("ven");
            }
            String sab = getResources().getString(R.string.saturday).toUpperCase();
            if (orari.optString("sab").compareTo("null") != 0){
                sab += "\n" + orari.optString("sab");
            }
            String dom = getResources().getString(R.string.sunday).toUpperCase();
            if (orari.optString("dom").compareTo("null") != 0){
                dom += "\n" + orari.optString("dom");
            }



            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);

            Button lunBtn = (Button) findViewById(R.id.monday_butt);
            Button marBtn = (Button) findViewById(R.id.tuesday_butt);
            Button merBtn = (Button) findViewById(R.id.wednesday_butt);
            Button gioBtn = (Button) findViewById(R.id.thursday_butt);
            Button venBtn = (Button) findViewById(R.id.friday_butt);
            Button sabBtn = (Button) findViewById(R.id.saturday_butt);
            Button domBtn = (Button) findViewById(R.id.sunday_butt);


            if (edit_name != null){
                edit_name.setText(name);
            }
            if (edit_address != null){
                edit_address.setText(address);
            }
            if (edit_phone != null){
                edit_phone.setText(phone);
            }
            if (edit_email != null){
                edit_email.setText(email);
            }
            if (edit_description != null){
                edit_description.setText(description);
            }


            if (lunBtn != null){ lunBtn.setText(lun); }
            if (marBtn != null){ marBtn.setText(mar); }
            if (merBtn != null){ merBtn.setText(mer); }
            if (gioBtn != null){ gioBtn.setText(gio); }
            if (venBtn != null){ venBtn.setText(ven); }
            if (sabBtn != null){ sabBtn.setText(sab); }
            if (domBtn != null){ domBtn.setText(dom); }

        }
        catch(Exception e){

            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            System.out.println("Eccezione: "+e.getMessage());
        }
    }

    private boolean saveInfo(){

        try {
            String name, address, phone, email, description, tmp;
            String lun, mar, mer, gio, ven, sab, dom;

            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);

            Button lunBtn = (Button) findViewById(R.id.monday_butt);
            Button marBtn = (Button) findViewById(R.id.tuesday_butt);
            Button merBtn = (Button) findViewById(R.id.wednesday_butt);
            Button gioBtn = (Button) findViewById(R.id.thursday_butt);
            Button venBtn = (Button) findViewById(R.id.friday_butt);
            Button sabBtn = (Button) findViewById(R.id.saturday_butt);
            Button domBtn = (Button) findViewById(R.id.sunday_butt);


            if (lunBtn != null){
                tmp = lunBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.monday)) == 0){
                    lun = "";
                }
                else
                    lun = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                lun = null;

            if (marBtn != null){
                tmp = marBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.tuesday)) == 0){
                    mar = "";
                }
                else
                    mar = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                mar = null;


            if (merBtn != null) {
                tmp = merBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.wednesday)) == 0) {
                    mer = "";
                } else
                    mer = tmp.substring(tmp.indexOf("\n") + 1, tmp.length());
            }
            else
                mer = null;

            if (gioBtn != null){
                tmp = gioBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.thursday)) == 0){
                    gio = "";
                }
                else
                    gio = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                gio = null;

            if (venBtn != null){
                tmp = venBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.friday)) == 0){
                    ven = "";
                }
                else
                    ven = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                ven = null;

            if (sabBtn != null){
                tmp = sabBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.saturday)) == 0){
                    sab = "";
                }
                else
                    sab = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                sab = null;

            if (domBtn != null){
                tmp = domBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.sunday)) == 0){
                    dom = "";
                }
                else
                    dom = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else
                dom = null;


            if (edit_name != null) {
                name = edit_name.getText().toString();
            }
            else
                name = null;
            if (edit_address != null) {
                address = edit_address.getText().toString();
            }
            else
                address = null;
            if (edit_phone != null) {
                phone = edit_phone.getText().toString();
            }
            else
                phone = null;
            if (edit_email != null) {
                email = edit_email.getText().toString();
            }
            else
                email = null;
            if (edit_description != null) {
                description = edit_description.getText().toString();
            }
            else
                description = null;

            if (name != null && address != null && phone != null && email != null && description != null && lun != null && mar != null && mer != null && gio != null && ven != null && sab != null && dom != null){

                if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || description.isEmpty() || lun.isEmpty() || mar.isEmpty() || mer.isEmpty() || gio.isEmpty() || ven.isEmpty() || sab.isEmpty() || dom.isEmpty()){
                    AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                    miaAlert.setTitle(getResources().getString(R.string.error));
                    miaAlert.setMessage(getResources().getString(R.string.error_complete));
                    AlertDialog alert = miaAlert.create();
                    alert.show();
                    return false;
                }

                GestioneDB DB = new GestioneDB();
                String profilo = DB.leggiDB(this, "db_profilo");

                JSONObject jsonRootObject = new JSONObject(profilo);
                JSONArray jsonArray = jsonRootObject.getJSONArray("profilo");
                JSONObject info = jsonArray.getJSONObject(0);
                JSONObject orari = jsonArray.getJSONObject(1);

                info.put("nome", name);
                info.put("indirizzo", address);
                info.put("telefono", phone);
                info.put("email", email);
                info.put("descrizione", description);
                orari.put("lun", lun);
                orari.put("mar", mar);
                orari.put("mer", mer);
                orari.put("gio", gio);
                orari.put("ven", ven);
                orari.put("sab", sab);
                orari.put("dom", dom);

                DB.updateDB(this, jsonRootObject, "db_profilo");
                return true;
            }
            else{
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getResources().getString(R.string.error));
                miaAlert.setMessage(getResources().getString(R.string.exceptionError));
                AlertDialog alert = miaAlert.create();
                alert.show();
                return false;
            }

        }
        catch (Exception e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }


    }

    @Override
    protected void OnSaveButtonPressed() {
        System.out.println("ORARIO LUNEDI: "/* + monday.getText()*/);

        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void OnAlertButtonPressed() {

    }

    @Override
    protected void OnCalendarButtonPressed() {

    }

    @Override
    protected void OnBackButtonPressed() {

    }

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {

    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {

    }
}