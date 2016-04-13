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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.jar.Manifest;

import it.polito.mad_lab2.common.PhotoManager;
import it.polito.mad_lab2.common.PhotoType;
import it.polito.mad_lab2.photo_viewer.PhotoViewer;
import it.polito.mad_lab2.photo_viewer.PhotoViewerListener;

public class ModifyMenuDish extends EditableBaseActivity implements PhotoViewerListener {

    private Oggetto_piatto dish = null;
    private Oggetto_menu dish_list= null;
    private int position = -1;
    private boolean newDish = false;
    private Oggetto_piatto.type_enum initialType = null;
    private Oggetto_piatto.type_enum modifiedType = null;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager imageManager;
    private PhotoViewer imageViewer;
    private String id_image;

    private  Spinner spinner;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);
        //setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));
        //setContentView(R.layout.activity_modify_menu_dish);

        SetSaveButtonVisibility(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        readData();
        initializePhotoManagement();

    }

    private void initializePhotoManagement()
    {
        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageDish_modifyMenu);
        imageManager = new PhotoManager(getApplicationContext(), PhotoType.MENU, this.imageThumb, this.imageLarge);

        id_image = "image_"+ dish.getId();

        imageViewer.setThumbBitmap(BitmapFactory.decodeFile(imageManager.getThumb(id_image)));


    }

    private void readData(){
        try {
            String spinnerType;
            boolean error = false;
            //recupero il piatto da modificare
            Bundle extras = getIntent().getExtras();


            if (extras == null){
                //ERRORE! Da verificare
                this.finish();
            }
            else {
                Oggetto_piatto.type_enum dish_type = (Oggetto_piatto.type_enum) getIntent().getSerializableExtra("type_enum");
                dish_list = (Oggetto_menu) extras.getSerializable("dish_list");
                if (dish_list != null)
                    if (dish_type == null){
                        //è un nuovo piatto --> AGGIUNTA
                        /*
                            settaggio dinamico del titolo
                         */
                        setTitleTextView(getResources().getString(R.string.title_activity_new_dish));
                        setContentView(R.layout.activity_modify_menu_dish);
                        creaSpinner();

                        dish = new Oggetto_piatto(null, -1, null);
                        dish.setId(dish_list.getNewId());
                        modifiedType = Oggetto_piatto.type_enum.PRIMI; //valore di default
                        newDish = true;
                        extras.clear();
                        return;
                    }
                    else{
                        /*
                            settaggio dinamico del titolo
                         */
                        setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));
                        setContentView(R.layout.activity_modify_menu_dish);
                        creaSpinner();

                        //è una modifica
                        position = extras.getInt("position");
                        extras.clear();

                        initialType = dish_type;

                        if (position != -1) {

                            //ricavo l'Oggetto_piatto corretto
                            switch (dish_type) {
                                case PRIMI:
                                    dish = dish_list.getPrimi().get(position);
                                    spinnerType = getResources().getString(R.string.first);
                                    break;
                                case SECONDI:
                                    dish = dish_list.getSecondi().get(position);
                                    spinnerType = getResources().getString(R.string.second);
                                    break;
                                case DESSERT:
                                    dish = dish_list.getDessert().get(position);
                                    spinnerType = getResources().getString(R.string.dessert);
                                    break;
                                case ALTRO:
                                    dish = dish_list.getAltro().get(position);
                                    spinnerType = getResources().getString(R.string.other);
                                    break;
                                default:
                                    System.out.println("Typology unknown");
                                    error = true;
                                    return;
                            }

                            modifiedType = dish.getDishType(); //inizializzo al valore corrente del piatto da modificare

                            if (!error) {
                                //carico le informazioni nella pagina di modifica
                                EditText editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
                                EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);

                                imageThumb = dish.getPhoto()[0];
                                imageLarge = dish.getPhoto()[1];

                                if (editName != null) {
                                    editName.setText(dish.getName());
                                }

                                if (editPrice != null) {
                                    editPrice.setText(String.valueOf(dish.getCost()));
                                }

                                //imposto lo spinner al valore corretto del piatto
                                if (spinner != null)
                                    spinner.setSelection(adapter.getPosition(spinnerType));

                            }
                        }
                        else
                            error = true;

                    }
                else {
                    //ERRORE! Da verificare
                    // non esiste la lista! Ci deve sempre essere!!
                    this.finish();
                }
            }

            if (error){
                //qualche errore durante la lettura/modifica
                // DA VERIFICARE!!!
                this.finish();
            }
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void creaSpinner(){
        //gestisco il menu a tendina per la tipologia del piatto
        spinner = (Spinner)findViewById(R.id.list_dishType_modifyMenu);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert), getResources().getString(R.string.other)}
        );
        if(spinner != null) {
            spinner.setAdapter(adapter);

            //listener per salvare la selezione dell'utente
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    if(dish != null) {
                        String selezione = (String) adapter.getItemAtPosition(pos);

                        if(selezione.compareTo(getResources().getString(R.string.first)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.PRIMI);
                            modifiedType = Oggetto_piatto.type_enum.PRIMI;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.second)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.SECONDI);
                            modifiedType = Oggetto_piatto.type_enum.SECONDI;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.dessert)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.DESSERT);
                            modifiedType = Oggetto_piatto.type_enum.DESSERT;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.other)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.ALTRO);
                            modifiedType = Oggetto_piatto.type_enum.ALTRO;
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    private boolean saveInfo(){

        // aggiorno l'oggetto piatto con tutte le nuove informazioni e passo indietro, all'activity di modifica menu principale, l'intere tabelle
        try {
            EditText editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
            EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);

            /*
                piccola modifica per integrità codice, l'oggetto lo modifico solo se ho letto
                correttamente tutti i campi, senza nessun errore.
                Altrimenti, teoricamente, posso riempire alcuni campi si e altri no (nulla di che)
             */
            String nomeD;
            int priceD;

            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (editName != null) {
                nomeD = editName.getText().toString();
                if(nomeD.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                //else
                    //dish.setName(nomeD);
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }


            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    priceD = Integer.parseInt(editPrice.getText().toString());
                    //dish.setCost(priceD);
                }
                else{
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

            dish.setName(nomeD);
            dish.setCost(priceD);
            dish.setDishType(modifiedType);
            dish.setPhoto(imageThumb, imageLarge);

            /* ##################################
                        Gestione Database
               ##################################
             */
            GestioneDB DB = new GestioneDB();
            if (newDish){
                // nuovo piatto - devo aggiungere entry al db

                JSONObject newDishObj = new JSONObject();

                newDishObj.put("id", dish_list.getNewId());
                newDishObj.put("nome", dish.getName());
                newDishObj.put("prezzo", dish.getCost());
                if (dish.getDishType() == Oggetto_piatto.type_enum.PRIMI){
                    newDishObj.put("tipo", "Primo");
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.SECONDI){
                    newDishObj.put("tipo", "Secondo");
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.DESSERT){
                    newDishObj.put("tipo", "Dessert");
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.ALTRO){
                    newDishObj.put("tipo", "Altro");
                }

                if (dish.getPhoto()[0] == null)
                    newDishObj.put("foto_thumb", "null");
                else
                    newDishObj.put("foto_thumb", dish.getPhoto()[0]);

                if (dish.getPhoto()[1] == null)
                    newDishObj.put("foto_large", "null");
                else
                    newDishObj.put("foto_large", dish.getPhoto()[1]);


                String db = DB.leggiDB(this, "db_menu");

                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.getJSONArray("lista_piatti");
                jsonArray.put(newDishObj);

                DB.updateDB(this, jsonRootObject, "db_menu");

                //aggiorno la lista in RAM
                if (dish.getDishType() == Oggetto_piatto.type_enum.PRIMI){
                    dish_list.addPrimo(dish);
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.SECONDI){
                    dish_list.addSecondo(dish);
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.DESSERT){
                    dish_list.addDessert(dish);
                }
                else if (dish.getDishType() == Oggetto_piatto.type_enum.ALTRO){
                    dish_list.addAltro(dish);
                }
            }
            else {
                //gestione db locale
                String db = DB.leggiDB(this, "db_menu");


                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.getJSONArray("lista_piatti");

                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id_JSON = Integer.parseInt(jsonObject.optString("id").toString());
                    if(dish.getId() == id_JSON){
                        jsonObject.put("nome", dish.getName());
                        jsonObject.put("prezzo", dish.getCost());

                        if (dish.getDishType() == Oggetto_piatto.type_enum.PRIMI){
                            jsonObject.put("tipo", "Primo");
                        }
                        else if (dish.getDishType() == Oggetto_piatto.type_enum.SECONDI){
                            jsonObject.put("tipo", "Secondo");
                        }
                        else if (dish.getDishType() == Oggetto_piatto.type_enum.DESSERT){
                            jsonObject.put("tipo", "Dessert");
                        }
                        else if (dish.getDishType() == Oggetto_piatto.type_enum.ALTRO){
                            jsonObject.put("tipo", "Altro");
                        }

                        if (dish.getPhoto()[0] == null)
                            jsonObject.put("foto_thumb", "null");
                        else
                            jsonObject.put("foto_thumb", dish.getPhoto()[0]);

                        if (dish.getPhoto()[1] == null)
                            jsonObject.put("foto_large", "null");
                        else
                            jsonObject.put("foto_large", dish.getPhoto()[1]);


                        break;
                    }
                }
                DB.updateDB(this, jsonRootObject, "db_menu");

                //implementazione cambio tipologia piatto --> cambia array in ram!
                if(initialType != dish.getDishType()){
                    //rimuovo piatto dalla lista vecchia
                    if (initialType == Oggetto_piatto.type_enum.PRIMI){
                        dish_list.getPrimi().remove(position);
                    }
                    else if (initialType == Oggetto_piatto.type_enum.SECONDI){
                        dish_list.getSecondi().remove(position);
                    }
                    else if (initialType == Oggetto_piatto.type_enum.DESSERT){
                        dish_list.getDessert().remove(position);
                    }
                    else if (initialType == Oggetto_piatto.type_enum.ALTRO){
                        dish_list.getAltro().remove(position);
                    }

                    //aggiungo piatto alla nuova lista
                    if (dish.getDishType() == Oggetto_piatto.type_enum.PRIMI){
                        dish_list.getPrimi().add(dish);
                    }
                    else if (dish.getDishType() == Oggetto_piatto.type_enum.SECONDI){
                        dish_list.getSecondi().add(dish);
                    }
                    else if (dish.getDishType() == Oggetto_piatto.type_enum.DESSERT){
                        dish_list.getDessert().add(dish);
                    }
                    else if (dish.getDishType() == Oggetto_piatto.type_enum.ALTRO){
                        dish_list.getAltro().add(dish);
                    }
                }
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

    private void commitPhotos() {
            this.imageThumb = this.imageManager.commitThumb(id_image);
            this.imageLarge = this.imageManager.commitLarge(id_image);

    }

    @Override
    protected void OnSaveButtonPressed() {
        //salvo le info e torno alla schermata di gestione menu principale
        commitPhotos();
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            Bundle b = new Bundle();
            b.putSerializable("dish_list", dish_list);

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }

    @Override
    protected void OnCalendarButtonPressed() {


    }

    @Override
    protected void OnBackButtonPressed() {
    }

    @Override
    protected void OnDeleteButtonPressed() {
        // TO DO
    }

    @Override
    protected void OnEditButtonPressed() {
        throw  new UnsupportedOperationException();
    }

    @Override
    protected void OnAddButtonPressed() {
        throw  new UnsupportedOperationException();
    }


    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            this.imageManager.saveThumb(thumb, id_image);
            this.imageManager.saveLarge(large, id_image);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            return BitmapFactory.decodeFile(this.imageManager.getLarge(id_image));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            this.imageManager.removeThumb(id_image);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imageManager.destroy(id_image);
    }


    private void checkPermessi(){
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ;
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (camera != PackageManager.PERMISSION_GRANTED || storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
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
