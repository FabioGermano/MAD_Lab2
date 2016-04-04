package it.polito.mad_lab2;

import android.content.Intent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        ListView list_menu = (ListView) findViewById(R.id.listView_primi);
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

            BaseAdapter a = new BaseAdapter() {
                @Override
                public int getCount() {

                    return list_piatti.size();
                }

                @Override
                public Object getItem(int position) {

                    return list_piatti.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        //devo creare una nuova View, poichè non ne è stata specificata nessuna da reciclare
                        LayoutInflater inflater = LayoutInflater.from(GestioneMenu.this);
                        convertView = inflater.inflate(R.layout.riga_lista, parent, false);
                    }
                    ImageView image_piatto = (ImageView) convertView.findViewById(R.id.image_p);
                    TextView text_name = (TextView) convertView.findViewById(R.id.text_name);
                    ImageButton imageB_delete = (ImageButton) convertView.findViewById(R.id.imageB_delete);
                    ImageButton imageB_modify = (ImageButton) convertView.findViewById(R.id.imageB_modify);

                    //carico la foto del piatto
                    if (image_piatto != null) {
                    }

                    //carico il nome ed il costo del piatto
                    if (text_name != null) {
                        text_name.setText(list_piatti.get(position).getName() + " - " + list_piatti.get(position).getCost() + "euro");
                    }

                    //imposto la cancellazione del piatto
                    if (imageB_delete != null) {
                        imageB_delete.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //rimuovi piatto del menu e aggiorna l'informazione sul server (o in locale)
                                boolean ris = removeDish(position);

                                //se necessario controllo se l'operazione è avvenuta con successo
                                if (ris) {
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }

                    //imposto la modifica del piatto
                    if(imageB_modify != null){
                        imageB_modify.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                //apro una nuova activity per la modifica del piatto
                                modifyDish(position);
                            }
                        });
                    }

                    return convertView;
                }
            };

            if(list_menu != null)
                list_menu.setAdapter(a);

        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    //rimuovo piatto
    private boolean removeDish(int num){
        try{
            list_piatti.remove(num);
            //cancello anche del server (o da locale)
            return true;
        } catch(Exception e){
            return false;
        }
    }

    //aggiungo piatto
    public void addDish(View v){
        //debug
        System.out.println("Aggiungo nuovo piatto");

        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        startActivity(intent);
    }

    //modifico piatto
    private void modifyDish(int position){

        Bundle b = new Bundle();
        b.putSerializable("dish", list_piatti.get(position));
        b.putInt("position", position);

        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        intent.putExtras(b);
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

}

