package it.polito.mad_lab2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Giovanna on 11/04/2016.
 */
public class EditAvailability extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = null;
    private String fileName = "database";
    private JSONObject jsonRootObject;
    private ArrayList<Oggetto_offerta> lista_offerte = null;
    private boolean availability_mode=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(true);
        SetCalendarButtonVisibility(false);
        hideShadow(true);
        setTitleTextView(getResources().getString(R.string.edit_availability));
        setContentView(R.layout.activity_gestione_menu);

        boolean ris = readMenuData();
        ris = readOfferData();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), EditAvailability.this);

        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);

            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(pagerAdapter.getTabView(i));
                }
            }
        }

    }

    @Override
    protected void OnSaveButtonPressed() {

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
    protected void OnDeleteButtonPressed() {

    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private int NumOfPage = 5;
        String tabTitles[] = new String[] { getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert),
                getResources().getString(R.string.other), getResources().getString(R.string.offers)};
        Context context;

        public MyPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }


        @Override
        public Fragment getItem(int position) {
            BlankMenuFragment menuFragment;
            BlankOfferFragment offerFragment;
            Bundle bundle = new Bundle();
            bundle.putBoolean("availability", availability_mode);
            switch (position) {
                case 0:
                    // sono nei primi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.PRIMI, this.context);
                    return menuFragment;
                case 1:
                    // sono nei secondi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.SECONDI, this.context);
                    return menuFragment;
                case 2:
                    // sono nei contorni
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.DESSERT, this.context);
                    return menuFragment;
                case 3:
                    // sono in altro
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.ALTRO, this.context);
                    return menuFragment;
                case 4:
                    offerFragment = new BlankOfferFragment();
                    offerFragment.setArguments(bundle);
                    offerFragment.setValue(lista_offerte, this.context);
                    return offerFragment;

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
            View tab = LayoutInflater.from(EditAvailability.this).inflate(R.layout.titolo_tab_pageradapter, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }

    }
    private boolean readMenuData(){
        try{
            Oggetto_piatto obj;
            lista_menu = new Oggetto_menu();


            GestioneDB DB = new GestioneDB();
            String db = DB.leggiDB(this, "db_menu");

            jsonRootObject = new JSONObject(db);

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

                //lista_menu.setJson(jsonRootObject);

            }
            return true;
        } catch (JSONException e)
        {
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }
    private boolean readOfferData(){
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
}