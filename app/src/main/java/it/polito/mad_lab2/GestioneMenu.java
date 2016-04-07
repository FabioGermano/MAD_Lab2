package it.polito.mad_lab2;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class GestioneMenu extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = null;
    private String fileName = "database";
    private JSONObject  jsonRootObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_menu);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);

        InitializeFABButtons(false, false, true);

        try {

            //carico info dal server (o da locale)
            /*Oggetto_piatto p1 = new Oggetto_piatto("Piatto1", 30, null, "primo");
            Oggetto_piatto p2 = new Oggetto_piatto("Piatto2", 23, null, "dolce");
            Oggetto_piatto p3 = new Oggetto_piatto("Piatto3", 12, null, "dolce");
            Oggetto_piatto p4 = new Oggetto_piatto("Piatto4", 11, null, "primo");
            Oggetto_piatto p5 = new Oggetto_piatto("Piatto5", 11, null, "secondo");
            list_piatti.add(p1);
            list_piatti.add(p2);
            list_piatti.add(p3);
            list_piatti.add(p4);
            list_piatti.add(p5);*/

            //recupero eventuali modifiche apportate ad un piatto
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String type = extras.getString("type");
                if (type != null) {
                    /*if (type.compareTo("modify") == 0) {
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
                    }*/
                }
            } else{
                //altrimenti carico info dal server (o da locale)
                boolean ris = readData();
            }

            //setUpRecyclerView();
            
            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
            MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), GestioneMenu.this);
            viewPager.setAdapter(pagerAdapter);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
            tabLayout.setupWithViewPager(viewPager);

            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }


        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private boolean readData(){
        try{
            Oggetto_piatto obj;
            lista_menu = new Oggetto_menu();

            /*InputStream db = getResources().getAssets().open(fileName, Context.MODE_PRIVATE);
            StringBuffer buffer = new StringBuffer("");
            byte[] b = new byte[1024];
            int n;
            while ((n = db.read(b)) != -1) {
                buffer.append(new String(b, 0, n));
            }
            db.close();*/

            /***** inserted by Roby on 07/04/2016 */
            FileInputStream fis = openFileInput("database");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder db = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                db.append(line);
            }
            fis.close();
            /****************************************/

            jsonRootObject = new JSONObject(db.toString());

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray arrayDebug = jsonRootObject.optJSONArray("lista_piatti");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < arrayDebug.length(); i++){
                JSONObject jsonObject = arrayDebug.getJSONObject(i);

                String nome = jsonObject.optString("nome").toString();
                int prezzo = Integer.parseInt(jsonObject.optString("prezzo").toString());
                String type = jsonObject.optString("tipo").toString();
                //creo le differenti liste
                switch(type){
                    case "Primo":
                        obj = new Oggetto_piatto(nome, prezzo, null, Oggetto_piatto.type_enum.PRIMI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        lista_menu.addPrimo(obj);
                        System.out.println("Aggiunto primo");
                        break;
                    case "Secondo":
                        obj = new Oggetto_piatto(nome, prezzo, null, Oggetto_piatto.type_enum.SECONDI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        lista_menu.addSecondo(obj);
                        System.out.println("Aggiunto secondo");
                        break;
                    case "Dessert":
                        obj = new Oggetto_piatto(nome, prezzo, null, Oggetto_piatto.type_enum.DESSERT);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        lista_menu.addDessert(obj);
                        System.out.println("Aggiunto dessert");
                        break;
                    case "Altro":
                        obj = new Oggetto_piatto(nome, prezzo, null, Oggetto_piatto.type_enum.ALTRO);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        lista_menu.addAltro(obj);
                        System.out.println("Aggiunto altro");
                        break;
                    default:
                        System.out.println("Typology unknown");
                        break;
                }

                lista_menu.setJson(jsonRootObject);

            }
            return true;
        } catch (JSONException e)
        {
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        } catch (FileNotFoundException e){
            System.out.println("Eccezione: file non trovato  " + e.getMessage());
            return false;
        } catch (IOException e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void OnDeleteButtonPressed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnEditButtonPressed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnAddButtonPressed() {
        //debug
        System.out.println("Aggiungo nuovo piatto");

        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        startActivity(intent);
    }

    //imposto la lista di tutti i piatti
    /*private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_menu);
        RecyclerAdapter_menu myAdapter = new RecyclerAdapter_menu(this, lista_menu, Oggetto_piatto.type_enum.PRIMI );
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }*/

    @Override
    protected void OnSaveButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////prova pageAdapter/////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class MyPageAdapter extends FragmentPagerAdapter {
        private int NumOfPage = 4;
        String tabTitles[] = new String[] { "Primi", "Secondi", "Dessert", "Altro" };
        Context context;

        public MyPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }


        @Override
        public Fragment getItem(int position) {
            BlankMenuFragment menuFragment;
            switch (position) {
                case 0:
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.PRIMI, this.context);
                    return menuFragment;
                case 1:
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.SECONDI, this.context);
                    return menuFragment;
                case 2:
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.DESSERT, this.context);
                    return menuFragment;
                case 3:
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.ALTRO, this.context);
                    return menuFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return NumOfPage;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(GestioneMenu.this).inflate(R.layout.titolo_tab_pageradapter, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }

    @Override
    protected void OnCalendarButtonPressed() {
        throw new UnsupportedOperationException();
    }

}

