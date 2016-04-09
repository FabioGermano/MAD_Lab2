package it.polito.mad_lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GestioneOfferte extends EditableBaseActivity {
    private ArrayList<Oggetto_offerta> lista_offerte = null;
    private JSONObject jsonRootObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            SetSaveButtonVisibility(false);
            SetCalendarButtonVisibility(false);
            SetAlertButtonVisibility(true);
            setContentView(R.layout.activity_gestione_offerte);

            InitializeFABButtons(false, false, true);


            //recupero eventuali modifiche apportate ad un piatto
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lista_offerte = (ArrayList<Oggetto_offerta>) extras.getSerializable("lista_offerte");
                extras.clear();
                if (lista_offerte == null) {
                    boolean ris = readData();
                }

            } else {
                //altrimenti carico info dal server (o da locale)
                boolean ris = readData();
            }

            setUpRecyclerView();
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private boolean readData(){
        try{

            lista_offerte = new ArrayList<>();

            GestioneDB DB = new GestioneDB();
            String db = DB.leggiDB(this, "db_offerte");

            if (db != null){
                System.out.println("Leggo le offerte");
                jsonRootObject = new JSONObject(db);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray arrayDebug = jsonRootObject.optJSONArray("lista_offerte");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < arrayDebug.length(); i++) {
                    JSONObject jsonObject = arrayDebug.getJSONObject(i);

                    String nome = jsonObject.optString("nome").toString();
                    int prezzo = Integer.parseInt(jsonObject.optString("prezzo").toString());
                    String note = jsonObject.optString("note".toString());
                    //creo la lista delle offerte
                    Oggetto_offerta obj = new Oggetto_offerta(nome, prezzo, null);
                    obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                    obj.setNote(note);
                    lista_offerte.add(obj);
                    System.out.println("Offerta aggiunta");
                }
                if(lista_offerte.isEmpty())
                    System.out.println("La lista è vuota");
                return true;
            }
            else {
                return false;
            }

        }catch (JSONException e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }

    //imposto la lista di tutte le offerte
    private void setUpRecyclerView(){
        System.out.println("Imposto CardView e adapter");
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_offerte);
        RecyclerAdapter_offerte myAdapter = new RecyclerAdapter_offerte(this, lista_offerte);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    protected void OnSaveButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }

    @Override
    protected void OnCalendarButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnBackButtonPressed() {

    }

    @Override
    protected void OnDeleteButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnEditButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAddButtonPressed() {
        //debug
        System.out.println("Aggiungo nuovo piatto");
    }
}