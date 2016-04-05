package it.polito.mad_lab2;

import android.content.Intent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GestioneMenu extends BaseActivity {

    private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private boolean firstTime = true; //debug

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_menu);

        SetSaveButtonVisibility(false);
        ImageButton dish_add = (ImageButton) findViewById(R.id.dish_add);
        if(dish_add != null) {
            dish_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDish();
                }
            });
        }

        try {

            if(firstTime) {
                //carico info dal server (o da locale)
                Oggetto_piatto p1 = new Oggetto_piatto("Piatto1", 30, null, "primo");
                Oggetto_piatto p2 = new Oggetto_piatto("Piatto2", 23, null, "dolce");
                Oggetto_piatto p3 = new Oggetto_piatto("Piatto3", 12, null, "dolce");
                Oggetto_piatto p4 = new Oggetto_piatto("Piatto4", 11, null, "primo");
                Oggetto_piatto p5 = new Oggetto_piatto("Piatto5", 11, null, "secondo");
                list_piatti.add(p1);
                list_piatti.add(p2);
                list_piatti.add(p3);
                list_piatti.add(p4);
                list_piatti.add(p5);

                firstTime = false;
            }

            //recupero eventuali modifiche apportate ad un piatto
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String type = extras.getString("type");
                if (type != null) {
                    if (type.compareTo("modify") == 0) {
                        //piatto modificato
                        Oggetto_piatto mod_dish = (Oggetto_piatto) extras.getSerializable("dish");
                        int position = extras.getInt("position");
                        extras.clear();

                        list_piatti.remove(position);
                        list_piatti.add(mod_dish);

                    } else if(type.compareTo("new")==0){
                        //piatto nuovo
                        Oggetto_piatto mod_dish = (Oggetto_piatto) extras.getSerializable("dish");
                        extras.clear();

                        list_piatti.add(mod_dish);
                    }
                }
            }

            InitializeFABButtons(false, false, true);

            setUpRecyclerView();

        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    //imposto la lista di tutti i piatti
    private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_menu);
        RecyclerAdapter_menu myAdapter = new RecyclerAdapter_menu(this, list_piatti);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    //aggiungo piatto
    public void addDish(){
        //debug
        System.out.println("Aggiungo nuovo piatto");

        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        startActivity(intent);
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
    protected void OnDeleteButtonPressed() {
        throw  new UnsupportedOperationException();
    }

    @Override
    protected void OnAddButtonPressed() {
        addDish();
    }

}

