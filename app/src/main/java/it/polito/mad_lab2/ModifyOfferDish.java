package it.polito.mad_lab2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

/**
 * Created by Euge on 10/04/2016.
 */
public class ModifyOfferDish extends EditableBaseActivity implements PhotoViewerListener{

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

        readData();
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
                        //è un nuovo piatto --> AGGIUNTA
                        offer = new Oggetto_offerta(null, -1, null, null);
                        extras.clear();
                        return;
                    }
                    else{
                        //è una modifica
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


            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (editName != null) {
                String text = editName.getText().toString();
                if(text.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    offer.setName(text);
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    int cost = Integer.parseInt(price);
                    offer.setCost(cost);
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
                String notes = editNotes.getText().toString();
                if(notes.compareTo("")==0) {
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    offer.setNote(notes);;
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

            offer.setDays(days);

            //la foto può essere null (default)
            /*if(errore){
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getResources().getString(R.string.error));
                miaAlert.setMessage(getResources().getString(R.string.exceptionError));
                AlertDialog alert = miaAlert.create();
                alert.show();
                return false;
            }

            if(campoVuoto){
                System.out.println(R.string.error_complete);
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getResources().getString(R.string.error));
                miaAlert.setMessage(getResources().getString(R.string.error_complete));
                AlertDialog alert = miaAlert.create();
                alert.show();
                return false;
            }*/

            /* ##################################
                        Gestione Database
               ##################################
             */

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
                newDishObj.put("lun", days[0]);
                newDishObj.put("mar", days[1]);
                newDishObj.put("mer", days[2]);
                newDishObj.put("gio", days[3]);
                newDishObj.put("ven", days[4]);
                newDishObj.put("sab", days[5]);
                newDishObj.put("dom", days[6]);

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
                        jsonObject.put("lun", days[0]);
                        jsonObject.put("mar", days[1]);
                        jsonObject.put("mer", days[2]);
                        jsonObject.put("gio", days[3]);
                        jsonObject.put("ven", days[4]);
                        jsonObject.put("sab", days[5]);
                        jsonObject.put("dom", days[6]);
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

    @Override
    protected void OnSaveButtonPressed() {
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

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

    private void modifyPhoto(){

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
