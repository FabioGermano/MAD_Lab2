package it.polito.mad_lab2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Euge on 10/04/2016.
 */
public class ModifyOfferDish extends EditableBaseActivity {

    private Oggetto_offerta offer = null;
    private ArrayList<Oggetto_offerta> offer_list = null;

    private int position = -1;
    private boolean newOffer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);
        setTitleTextView(getResources().getString(R.string.menu_edit_offer));
        setContentView(R.layout.activity_modify_offer);
        SetSaveButtonVisibility(true);

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
                        //è un nuovo piatto --> AGGIUNTA
                        offer = new Oggetto_offerta(null, -1, null);
                        extras.clear();
                        return;
                    }
                    else{
                        //è una modifica
                        position = extras.getInt("position");
                        extras.clear();

                        if (position != -1) {
                            offer = offer_list.get(position);

                            ImageView imageOffer = (ImageView) findViewById(R.id.imageOffer_modifyOffer);
                            EditText editName = (EditText) findViewById(R.id.edit_offerName_modifyOffer);
                            EditText editPrice = (EditText) findViewById(R.id.edit_offerPrice_modifyOffer);
                            EditText editNotes = (EditText) findViewById(R.id.edit_offerNote_modifyOffer);

                            if (imageOffer != null) {
                                //carico l'immagine e setto OnClickListener
                                imageOffer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        modifyPhoto();
                                    }
                                });
                            }

                            if (editName != null) {
                                editName.setText(offer.getName());
                            }

                            if (editPrice != null) {
                                editPrice.setText(String.valueOf(offer.getCost()));
                            }

                            if (editNotes != null){
                                editNotes.setText(offer.getNote());
                            }
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

            //leggo i campi dalla schermata
            if (editName != null) {
                String text = editName.getText().toString();
                if(text.compareTo("")==0)
                    offer.setName(null);
                else
                    offer.setName(text);
            }

            if (editPrice != null) {
                int cost = Integer.parseInt(editPrice.getText().toString());
                if(cost < 0)
                    offer.setCost(-1);
                else
                    offer.setCost(cost);
            }

            if (editNotes != null) {
                String notes = editNotes.getText().toString();
                if(notes.compareTo("")==0)
                    offer.setNote(null);
                else
                    offer.setNote(notes);;
            }


            //la foto può essere null (default)
            if(offer.getName() == null || offer.getCost() == -1 || offer.getNote() == null){
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getResources().getString(R.string.error));
                miaAlert.setMessage(getResources().getString(R.string.error_complete));
                AlertDialog alert = miaAlert.create();
                alert.show();
                return false;
            }

            GestioneDB DB = new GestioneDB();
            if (newOffer){
                // è una nuova offerta
                //aggiorno il db locale

                //recupero l'id da usare
                int id = 1;
                for (Oggetto_offerta o : offer_list){
                    if (id < o.getId())
                        id = o.getId();
                }
                id++;

                offer.setId(id);

                JSONObject newDishObj = new JSONObject();

                newDishObj.put("id", id);
                newDishObj.put("nome", offer.getName());
                newDishObj.put("prezzo", offer.getCost());
                newDishObj.put("note", offer.getNote());

                String db = DB.leggiDB(this, "db_offerte");

                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.getJSONArray("lista_offerte");
                jsonArray.put(newDishObj);

                DB.updateDB(this, jsonRootObject, "db_offerte");

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
                        break;
                    }
                }
                DB.updateDB(this, jsonRootObject, "db_offerte");
            }

            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exceptionError, Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            startActivity(intent);
            return false;
        }



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

    @Override
    protected void OnSaveButtonPressed() {
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            Bundle b = new Bundle();
            b.putSerializable("offer_list", offer_list);

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
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

    private void modifyPhoto(){

    }
}
