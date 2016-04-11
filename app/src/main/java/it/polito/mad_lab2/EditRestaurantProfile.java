package it.polito.mad_lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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
            JSONArray jsonArray = jsonRootObject.getJSONArray("profilo");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String name = jsonObject.optString("nome").toString();
            String address = jsonObject.optString("indirizzo").toString();
            String phone = jsonObject.optString("telefono");
            String email =jsonObject.optString("email");
            String description = jsonObject.optString("descrizione");

            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);

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
        }
        catch(Exception e){
            System.out.println("Eccezione: "+e.getMessage());
        }
    }

    private boolean saveInfo(){

        try {
            String name, address, phone, email, description;

            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);


            if (edit_name != null) {
                name = edit_name.getText().toString();
            }
            else
                name= null;
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

            if (name != null && address != null && phone != null && email != null && description != null){
                GestioneDB DB = new GestioneDB();
                String profilo = DB.leggiDB(this, "db_profilo");

                JSONObject jsonRootObject = new JSONObject(profilo);
                JSONArray jsonArray = jsonRootObject.getJSONArray("profilo");
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                jsonObject.put("nome", name);
                jsonObject.put("indirizzo", address);
                jsonObject.put("telefono", phone);
                jsonObject.put("email", email);
                jsonObject.put("descrizione", description);

                DB.updateDB(this, jsonRootObject, "db_profilo");
                return true;
            }
            else
                return false;
        }
        catch (Exception e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }


    }

    @Override
    protected void OnSaveButtonPressed() {
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