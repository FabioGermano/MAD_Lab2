package it.polito.mad_lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import it.polito.mad_lab2.common.PhotoManager;
import it.polito.mad_lab2.common.PhotoType;
import it.polito.mad_lab2.photo_viewer.PhotoViewer;
import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

public class EditRestaurantProfile extends BaseActivity implements PhotoViewerListener {

    private String logoThumbPath;
    private String[] coversThumbPath, coversLargePath;
    private PhotoManager logoManager;
    private PhotoManager[] coverManagers;
    private PhotoViewer logoPhotoViewer;
    private PhotoViewer[] coversPhotoViewer;

    private final String id_logo_photo = "logo_photo";
    private final String[] ids_cover_photo = new String[]{
            "cover_photo_1",
            "cover_photo_2",
            "cover_photo_3",
            "cover_photo_4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleTextView(getResources().getString(R.string.edit_restaurant_profile));
        SetSaveButtonVisibility(true);
        SetCalendarButtonVisibility(false);

        setContentView(R.layout.activity_edit_restaurant_profile);
        // Construct the data source

        this.coversThumbPath = new String[4];
        this.coversLargePath = new String[4];

        readData();

        initializePhotoManagement();
    }

    private void initializePhotoManagement()
    {
        logoPhotoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.logo_fragment);
        this.coversPhotoViewer = new PhotoViewer[4];
        this.coversPhotoViewer[0] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment1);
        this.coversPhotoViewer[1] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment2);
        this.coversPhotoViewer[2] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment3);
        this.coversPhotoViewer[3] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment4);

        logoManager = new PhotoManager(getApplicationContext(), PhotoType.PROFILE, this.logoThumbPath, null);
        coverManagers = new PhotoManager[4];
        for(int i = 0; i<4; i++) {
            coverManagers[i] = new PhotoManager(getApplicationContext(), PhotoType.PROFILE, this.coversThumbPath[i], this.coversLargePath[i]);
        }

        logoPhotoViewer.setThumbBitmap(BitmapFactory.decodeFile(logoManager.getThumb(id_logo_photo)));
        for(int i = 0; i<4; i++) {
            this.coversPhotoViewer[i].setThumbBitmap(BitmapFactory.decodeFile(coverManagers[i].getThumb(ids_cover_photo[i])));
        }
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

            // read logo path
            this.logoThumbPath = jsonObject.optString("logoThumbPath");
            if(this.logoThumbPath.equals("")){
                this.logoThumbPath = null;
            }
            //this.logoLargePath = jsonObject.optString("logoLargePath");
            //if(this.logoLargePath.equals("")){
            //    this.logoLargePath = null;
            //}

            // read cover path
            for(int i = 0; i<4; i++){
                this.coversThumbPath[i] = jsonObject.optString("coversThumbPath_"+i);
                if(this.coversThumbPath[i].equals("")){
                    this.coversThumbPath[i] = null;
                }
            }
            for(int i = 0; i<4; i++){
                this.coversLargePath[i] = jsonObject.optString("coversLargePath_"+i);
                if(this.coversLargePath[i].equals("")){
                    this.coversLargePath[i] = null;
                }
            }

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

                //save logo paths
                if(this.logoThumbPath != null)
                {
                    jsonObject.put("logoThumbPath", this.logoThumbPath);
                }
               // if(this.logoLargePath != null)
                //{
                //    jsonObject.put("logoLargePath", this.logoLargePath);
                //}

                //save cover path
                for(int i = 0; i<4; i++){
                    if(this.coversThumbPath[i] != null) {
                        jsonObject.put("coversThumbPath_" + i, this.coversThumbPath[i]);
                    }
                }
                for(int i = 0; i<4; i++){
                    if(this.coversLargePath[i] != null) {
                        jsonObject.put("coversLargePath_" + i, this.coversLargePath[i]);
                    }
                }

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

    private void commitPhotos() {
        this.logoThumbPath = this.logoManager.commitThumb(id_logo_photo);
        //this.logoLargePath = this.photoManager.commitLarge(id_logo_photo);

        for(int i = 0; i<4; i++){
            this.coversThumbPath[i] = this.coverManagers[i].commitThumb(ids_cover_photo[i]);
            this.coversLargePath[i] = this.coverManagers[i].commitLarge(ids_cover_photo[i]);
        }
    }

    @Override
    protected void OnSaveButtonPressed() {
        commitPhotos();
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
        if(fragmentId == R.id.logo_fragment)
        {
            this.logoManager.saveThumb(thumb, id_logo_photo);
            //this.photoManager.saveLarge(large, id_logo_photo);
        }
        else if(fragmentId == R.id.cover_fragment1)
        {
            this.coverManagers[0].saveThumb(thumb, ids_cover_photo[0]);
            this.coverManagers[0].saveLarge(large, ids_cover_photo[0]);
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            this.coverManagers[1].saveThumb(thumb, ids_cover_photo[1]);
            this.coverManagers[1].saveLarge(large, ids_cover_photo[1]);
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            this.coverManagers[2].saveThumb(thumb, ids_cover_photo[2]);
            this.coverManagers[2].saveLarge(large, ids_cover_photo[2]);
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            this.coverManagers[3].saveThumb(thumb, ids_cover_photo[3]);
            this.coverManagers[3].saveLarge(large, ids_cover_photo[3]);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        //if(fragmentId == R.id.logo_fragment) {
        //    return BitmapFactory.decodeFile(this.photoManager.getLarge(id_logo_photo));
        //}
        if(fragmentId == R.id.cover_fragment1)
        {
            return BitmapFactory.decodeFile(this.coverManagers[0].getLarge(ids_cover_photo[0]));
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            return BitmapFactory.decodeFile(this.coverManagers[1].getLarge(ids_cover_photo[1]));
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            return BitmapFactory.decodeFile(this.coverManagers[2].getLarge(ids_cover_photo[2]));
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            return BitmapFactory.decodeFile(this.coverManagers[3].getLarge(ids_cover_photo[3]));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if(fragmentId == R.id.logo_fragment) {
            this.logoManager.removeThumb(id_logo_photo);
        }
        else if(fragmentId == R.id.cover_fragment1)
        {
            this.coverManagers[0].removeThumb(ids_cover_photo[0]);
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            this.coverManagers[1].removeThumb(ids_cover_photo[1]);
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            this.coverManagers[2].removeThumb(ids_cover_photo[2]);
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            this.coverManagers[3].removeThumb(ids_cover_photo[3]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.logoManager.destroy(id_logo_photo);
        for(int i = 0; i < 4; i++){
            this.coverManagers[i].destroy(ids_cover_photo[i]);
        }
    }
}