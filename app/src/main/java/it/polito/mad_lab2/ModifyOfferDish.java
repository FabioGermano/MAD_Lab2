package it.polito.mad_lab2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab2.common.PhotoManager;
import it.polito.mad_lab2.common.PhotoType;
import it.polito.mad_lab2.photo_viewer.PhotoViewer;
import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

/**
 * Created by Euge on 10/04/2016.
 */
public class ModifyOfferDish extends EditableBaseActivity implements PhotoViewerListener{

    private Oggetto_offerta offer = null;
    private ArrayList<Oggetto_offerta> offer_list = null;

    private int position = -1;
    private boolean newOffer = false;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager imageManager;
    private PhotoViewer imageViewer;
    private String id_image;

    private int newID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);
        //setTitleTextView(getResources().getString(R.string.title_activity_edit_offer));
        //setContentView(R.layout.activity_modify_offer);
        SetSaveButtonVisibility(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        readData();
        initializePhotoManagement();
    }


    private void initializePhotoManagement()
    {
        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageOffer_modifyOffer);
        imageManager = new PhotoManager(getApplicationContext(), PhotoType.OFFER, this.imageThumb, this.imageLarge);

        id_image = "image_"+ offer.getId();

        imageViewer.setThumbBitmap(BitmapFactory.decodeFile(imageManager.getThumb(id_image)));
    }

    private void readData(){
        try {
            boolean error = false;
            //recupero l'offerta da modificare
            Bundle extras = getIntent().getExtras();

            if (extras == null){
                //ERRORE! Da verificare
                this.finish();
            }
            else {
                offer_list = (ArrayList<Oggetto_offerta>) extras.getSerializable("offer_list");
                newOffer = (boolean) extras.getBoolean("new");
                if (offer_list != null)
                    if (newOffer){
                        //è una nuova offerta --> AGGIUNTA
                        /*
                            settaggio dinamico del titolo
                         */
                        setContentView(R.layout.activity_modify_offer);
                        setTitleTextView(getResources().getString(R.string.title_activity_new_offer));
                        offer = new Oggetto_offerta(null, -1, null);
                        //recupero l'id da usare
                        for (Oggetto_offerta o : offer_list){
                            if (newID < o.getId())
                                newID = o.getId();
                        }
                        newID++;
                        offer.setId(newID);

                        extras.clear();
                        return;
                    }
                    else{
                        //è una modifica
                        /*
                            settaggio dinamico del titolo
                         */

                        setContentView(R.layout.activity_modify_offer);
                        setTitleTextView(getResources().getString(R.string.title_activity_edit_offer));

                        position = extras.getInt("position");
                        extras.clear();

                        if (position != -1) {
                            offer = offer_list.get(position);

                            EditText editName = (EditText) findViewById(R.id.edit_offerName_modifyOffer);
                            EditText editPrice = (EditText) findViewById(R.id.edit_offerPrice_modifyOffer);
                            EditText editNotes = (EditText) findViewById(R.id.edit_offerNote_modifyOffer);

                            ToggleButton lunBtn = (ToggleButton) findViewById(R.id.lun_Button);
                            ToggleButton marBtn = (ToggleButton) findViewById(R.id.mar_Button);
                            ToggleButton merBtn = (ToggleButton) findViewById(R.id.mer_Button);
                            ToggleButton gioBtn = (ToggleButton) findViewById(R.id.gio_Button);
                            ToggleButton venBtn = (ToggleButton) findViewById(R.id.ven_Button);
                            ToggleButton sabBtn = (ToggleButton) findViewById(R.id.sab_Button);
                            ToggleButton domBtn = (ToggleButton) findViewById(R.id.dom_Button);


                            if (editName != null) {
                                editName.setText(offer.getName());
                            }

                            if (editPrice != null) {
                                editPrice.setText(String.valueOf(offer.getCost()));
                            }

                            if (editNotes != null){
                                editNotes.setText(offer.getNote());
                            }

                            if (lunBtn != null){ lunBtn.setChecked(offer.getDays()[0]); }
                            if (marBtn != null){ marBtn.setChecked(offer.getDays()[1]); }
                            if (merBtn != null){ merBtn.setChecked(offer.getDays()[2]); }
                            if (gioBtn != null){ gioBtn.setChecked(offer.getDays()[3]); }
                            if (venBtn != null){ venBtn.setChecked(offer.getDays()[4]); }
                            if (sabBtn != null){ sabBtn.setChecked(offer.getDays()[5]); }
                            if (domBtn != null){ domBtn.setChecked(offer.getDays()[6]); }

                            imageThumb = offer.getPhoto()[0];
                            imageLarge = offer.getPhoto()[1];
                        }
                        else {
                            //qualche errore durante la lettura / modifica
                            // DA VERIFICARE!!!
                            this.finish();
                        }
                    }
                else {
                    //ERRORE! Da verificare
                    // non esiste la lista! Ci deve sempre essere!!
                    this.finish();
                }
            }
        } catch (Exception e){
            System.out.print("Eccezione: " + e.getMessage());
        }
    }


    private boolean saveInfo(){

        // aggiorno l'oggetto offerta con tutte le nuove informazioni e passo indietro, all'activity di modifica offerta principale, l'intera lista
        try {
            EditText editName = (EditText) findViewById(R.id.edit_offerName_modifyOffer);
            EditText editPrice = (EditText) findViewById(R.id.edit_offerPrice_modifyOffer);
            EditText editNotes = (EditText) findViewById(R.id.edit_offerNote_modifyOffer);
            ToggleButton lunBtn = (ToggleButton) findViewById(R.id.lun_Button);
            ToggleButton marBtn = (ToggleButton) findViewById(R.id.mar_Button);
            ToggleButton merBtn = (ToggleButton) findViewById(R.id.mer_Button);
            ToggleButton gioBtn = (ToggleButton) findViewById(R.id.gio_Button);
            ToggleButton venBtn = (ToggleButton) findViewById(R.id.ven_Button);
            ToggleButton sabBtn = (ToggleButton) findViewById(R.id.sab_Button);
            ToggleButton domBtn = (ToggleButton) findViewById(R.id.dom_Button);

            /*
                piccola modifica per integrità codice, l'oggetto lo modifico solo se ho letto
                correttamente tutti i campi, senza nessun errore.
                Altrimenti, teoricamente, posso riempire alcuni campi si e altri no (nulla di che)
             */
            String nomeO;
            int priceO;
            String notesO;

            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (editName != null) {
                nomeO = editName.getText().toString();
                if(nomeO.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                //else
                    //offer.setName(text);
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    priceO = Integer.parseInt(price);
                    //offer.setCost(cost);
                }
                else {
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (editNotes != null) {
                notesO = editNotes.getText().toString();
                if(notesO.compareTo("")==0) {
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                //else
                    //offer.setNote(notes);
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            boolean[] days = new boolean[7];

            if (lunBtn != null){ days[0] = lunBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (marBtn != null){ days[1] = marBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (merBtn != null){ days[2] = merBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (gioBtn != null){ days[3] = gioBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (venBtn != null){ days[4] = venBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (sabBtn != null){ days[5] = sabBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (domBtn != null){ days[6] = domBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            offer.setName(nomeO);
            offer.setCost(priceO);
            offer.setNote(notesO);

            offer.setDays(days);

            offer.setPhoto(imageThumb, imageLarge);


            /* ##################################
                        Gestione Database
               ##################################
             */

            GestioneDB DB = new GestioneDB();

            if (newOffer){
                // è una nuova offerta
                //aggiorno il db locale

                JSONObject newOfferObj = new JSONObject();

                newOfferObj.put("id", newID);
                newOfferObj.put("nome", offer.getName());
                newOfferObj.put("prezzo", offer.getCost());
                newOfferObj.put("note", offer.getNote());
                newOfferObj.put("lun", days[0]);
                newOfferObj.put("mar", days[1]);
                newOfferObj.put("mer", days[2]);
                newOfferObj.put("gio", days[3]);
                newOfferObj.put("ven", days[4]);
                newOfferObj.put("sab", days[5]);
                newOfferObj.put("dom", days[6]);

                if (offer.getPhoto()[0] == null)
                    newOfferObj.put("foto_thumb", "null");
                else
                    newOfferObj.put("foto_thumb", offer.getPhoto()[0]);

                if (offer.getPhoto()[1] == null)
                    newOfferObj.put("foto_large", "null");
                else
                    newOfferObj.put("foto_large", offer.getPhoto()[1]);

                String db = DB.leggiDB(this, "db_offerte");

                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.getJSONArray("lista_offerte");
                jsonArray.put(newOfferObj);

                DB.updateDB(this, jsonRootObject, "db_offerte");

                //aggiorno la lista in RAM
                offer_list.add(offer);

            }
            else {
                // modifica offerta!
                //gestione db locale

                String db = DB.leggiDB(this, "db_offerte");

                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.getJSONArray("lista_offerte");

                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id_JSON = Integer.parseInt(jsonObject.optString("id").toString());
                    if(offer.getId() == id_JSON){
                        jsonObject.put("nome", offer.getName());
                        jsonObject.put("prezzo", offer.getCost());
                        jsonObject.put("note", offer.getNote());
                        jsonObject.put("lun", days[0]);
                        jsonObject.put("mar", days[1]);
                        jsonObject.put("mer", days[2]);
                        jsonObject.put("gio", days[3]);
                        jsonObject.put("ven", days[4]);
                        jsonObject.put("sab", days[5]);
                        jsonObject.put("dom", days[6]);

                        if (offer.getPhoto()[0] == null)
                            jsonObject.put("foto_thumb", "null");
                        else
                            jsonObject.put("foto_thumb", offer.getPhoto()[0]);

                        if (offer.getPhoto()[1] == null)
                            jsonObject.put("foto_large", "null");
                        else
                            jsonObject.put("foto_large", offer.getPhoto()[1]);
                        break;
                    }
                }
                DB.updateDB(this, jsonRootObject, "db_offerte");
            }

            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            printAlert(getResources().getString(R.string.exceptionError));

            //O torniamo con tutte le schermate: questa, EditResturantProfile, EditAvailability, ModifyMunuDish
            //o con nessuna no?
            /*Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            startActivity(intent);*/
            return false;
        }
    }

    private void printAlert(String msg){
        System.out.println(msg);
        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(msg);

        //titolo personalizzato
        TextView title = new TextView(this);
        title.setText(getResources().getString(R.string.attenzione));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        miaAlert.setCustomTitle(title);

        AlertDialog alert = miaAlert.create();
        alert.show();

        //centrare il messaggio
        TextView messageView = (TextView)alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
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

    private void commitPhotos() {
        this.imageThumb = this.imageManager.commitThumb(id_image);
        this.imageLarge = this.imageManager.commitLarge(id_image);
    }

    @Override
    protected void OnSaveButtonPressed() {

        commitPhotos();
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            this.imageManager.destroy(id_image);

            Bundle b = new Bundle();
            b.putSerializable("offer_list", offer_list);
            Intent intent = new Intent(getApplicationContext(), GestioneOfferte.class);
            intent.putExtras(b);
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
        if (fragmentId == R.id.imageOffer_modifyOffer){
            this.imageManager.saveThumb(thumb, id_image);
            this.imageManager.saveLarge(large, id_image);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        if (fragmentId == R.id.imageOffer_modifyOffer){
            return BitmapFactory.decodeFile(this.imageManager.getLarge(id_image));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if (fragmentId == R.id.imageOffer_modifyOffer){
            this.imageManager.removeThumb(id_image);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imageManager.destroy(id_image);
    }

    private void checkStoragePermission(){
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    finish();
                    startActivity(getIntent());

                } else {
                    printAlert("Negando i permessi l'app non funzionerà correttamente");

                }
                return;
            }
        }
    }
}
