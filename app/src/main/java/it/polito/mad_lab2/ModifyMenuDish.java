package it.polito.mad_lab2;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class ModifyMenuDish extends BaseActivity {

    private Oggetto_piatto dish = null;
    private int position = -1;
    private boolean newDish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_menu_dish);

        try {

            //gestisco il menu a tendina per la tipologia del piatto
            Spinner spinner = (Spinner)findViewById(R.id.list_dishType);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    new String[]{getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert), getResources().getString(R.string.other)}
            );
            if(spinner != null) {
                spinner.setAdapter(adapter);

                //listener per salvare la selezione dell'utente
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                        if(dish != null)
                            dish.setDishType((String) adapter.getItemAtPosition(pos));
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            //recupero il piatto da modificare
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                dish = (Oggetto_piatto) extras.getSerializable("dish");
                position = extras.getInt("position");

                extras.clear();

                if (dish != null && position != -1) {
                    //carico le informazioni nella pagina di modifica
                    ImageView imageDish = (ImageView) findViewById(R.id.imageDish);
                    EditText editName = (EditText) findViewById(R.id.edit_dishName);
                    EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice);

                    if(imageDish != null){
                        //carico l'immagine e setto OnClickListener

                        imageDish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                modifyPhoto();
                            }
                        });
                    }

                    if(editName != null){
                        editName.setText(dish.getName());
                    }

                    if(editPrice != null){
                        editPrice.setText(String.valueOf(dish.getCost()));
                    }

                    if(spinner != null)
                        spinner.setSelection(adapter.getPosition(dish.getDishType()));

                } else {
                    System.out.println(" No Dish passed ");
                }
            }
            //no extras, creazione di un nuovo piatto, lascio la configurazione di default
            else {
                //utile per differenziare le due funzionalità quando salvo e aggiorno la lista nella
                //schermata principale
                newDish = true;
                //debug
                System.out.println("Creazione di un nuovo piatto");
                dish = new Oggetto_piatto(null, -1, null, null);
            }

        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private void saveInfo(){
        // aggiorno l'oggetto piatto con tutte le nuove informazioni e lo passo indietro all'activity di modifica menu principale
        try {
            EditText editName = (EditText) findViewById(R.id.edit_dishName);
            EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice);

            if (editName != null) {
                String text = editName.getText().toString();
                if(text.compareTo("")==0)
                    dish.setName(null);
                else
                    dish.setName(text);
            }

            if (editPrice != null) {
                int cost = Integer.parseInt(editPrice.getText().toString());
                if(cost < 0)
                    dish.setCost(-1);
                else
                    dish.setCost(cost);
            }

            //la foto può essere null (default)
            if(dish.getName() == null || dish.getCost() == -1 || dish.getDishType() == null){
                AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
                miaAlert.setTitle(getResources().getString(R.string.error));
                miaAlert.setMessage(getResources().getString(R.string.error_complete));
                AlertDialog alert = miaAlert.create();
                alert.show();

                return;
            }

            Bundle b = new Bundle();
            b.putSerializable("dish", dish);

            if(!newDish){
                b.putString("type", "modify");
                b.putInt("position", position);
            } else {
                b.putString("type", "new");
            }

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            intent.putExtras(b);
            startActivity(intent);

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());

            AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
            miaAlert.setTitle(getResources().getString(R.string.error));
            miaAlert.setMessage(getResources().getString(R.string.error_complete));
            AlertDialog alert = miaAlert.create();
            alert.show();
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_dish) {
            saveInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void OnSaveButtonPressed() {
        //salvo le info e torno alla schermata di gestione menu principale
        saveInfo();
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }

    @Override
    protected void OnDeleteButtonPressed() {
        throw  new UnsupportedOperationException();
    }

    private void modifyPhoto(){

    }
}
