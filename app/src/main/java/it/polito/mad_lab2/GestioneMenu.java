package it.polito.mad_lab2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GestioneMenu extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = null;
    private String fileName = "database";
    private JSONObject  jsonRootObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);
        hideShadow(true);
        setTitleTextView(getResources().getString(R.string.manu_edit_title));
        setContentView(R.layout.activity_gestione_menu);

        InitializeFABButtons(false, false, true);

        try {
            //recupero eventuali modifiche apportate ad un piatto
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lista_menu = (Oggetto_menu) extras.getSerializable("dish_list");
                extras.clear();

            } else {
                //altrimenti carico info dal server (o da locale)
                boolean ris = readData();
            }

            //setUpRecyclerView();

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
            MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), GestioneMenu.this);
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
        } catch (NullPointerException e_null){
            System.out.println("Eccezione: " + e_null.getMessage());
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private boolean readData(){
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        Bundle b = new Bundle();
        b.putSerializable("dish_list", lista_menu);
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
                    // sono nei primi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.PRIMI, this.context);
                    return menuFragment;
                case 1:
                    // sono nei secondi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.SECONDI, this.context);
                    return menuFragment;
                case 2:
                    // sono nei contorni
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setValue(lista_menu, Oggetto_piatto.type_enum.DESSERT, this.context);
                    return menuFragment;
                case 3:
                    // sono in altro
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

    @Override
    protected void OnBackButtonPressed() {

    }

}

