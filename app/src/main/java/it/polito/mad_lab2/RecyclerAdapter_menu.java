package it.polito.mad_lab2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Euge on 05/04/2016.
 */
public class RecyclerAdapter_menu extends RecyclerView.Adapter<RecyclerAdapter_menu.MyViewHolder> {

    private Oggetto_menu dish_list;
    private LayoutInflater myInflater;
    private Oggetto_piatto.type_enum menu_type;
    //accesso veloce alla lista in esame ??
    private ArrayList<Oggetto_piatto> current_list;
    private boolean availability_mode;


    public RecyclerAdapter_menu(Context context, Oggetto_menu data, Oggetto_piatto.type_enum type, boolean availability_mode){
        this.dish_list = data;
        this.availability_mode=availability_mode;
        myInflater = LayoutInflater.from(context);
        this.menu_type = type;
        switch(type){
            case PRIMI:
                current_list= data.getPrimi();
                break;
            case SECONDI:
                current_list = data.getSecondi();
                break;
            case DESSERT:
                current_list = data.getDessert();
                break;
            case ALTRO:
                current_list = data.getAltro();
                break;
            default:
                System.out.println("Typology unknown");
                break;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.riga_lista, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        holder.setListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Oggetto_piatto currentObj = current_list.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return current_list.size();

    }


    class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private Oggetto_piatto current;
        private int position;
        private ImageView dish_img;
        private TextView dish_name;
        private TextView dish_price;
        private ImageButton dish_delete;
        private ImageButton dish_modify;
        private Switch dish_availability;
        private Context context;


        public MyViewHolder(View itemView) {
            super(itemView);
            dish_img = (ImageView) itemView.findViewById(R.id.image_dish_menu);
            dish_name = (TextView) itemView.findViewById(R.id.dish_name_menu);
            dish_price = (TextView) itemView.findViewById(R.id.dish_price_menu);
            dish_delete = (ImageButton) itemView.findViewById(R.id.img_delete_menu);
            dish_modify = (ImageButton) itemView.findViewById(R.id.img_modify_menu);
            dish_availability = (Switch) itemView.findViewById(R.id.switch1);
            if(availability_mode){
                dish_delete.setVisibility(View.GONE);
                dish_modify.setVisibility(View.GONE);
                dish_availability.setVisibility(View.VISIBLE);

            }
            context = itemView.getContext();
        }

        public void setData(Oggetto_piatto currentObj, int position) {
            this.position = position;
            this.current = currentObj;
            if(dish_name != null)
                this.dish_name.setText(currentObj.getName());
            if(dish_price != null) {
                String tmp = String.valueOf(currentObj.getCost()) + " " + context.getResources().getString(R.string.money_value);
                this.dish_price.setText(tmp);
            }
            if(availability_mode){
                if(dish_availability != null){
                    dish_availability.setChecked(currentObj.isAvailable());
                }
            }
            //carico foto
            if(dish_img != null){
                String path = currentObj.getPhoto()[0];
                if (path != null){
                    try {
                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        if(bmp != null)
                            dish_img.setImageBitmap(bmp);
                    } catch (Exception e){
                        System.out.println("Errore creazione bitmap");
                    }
                }

            }
        }

        public void setListeners(){
            if (availability_mode)
                dish_availability.setOnClickListener(MyViewHolder.this);
            else {
                dish_delete.setOnClickListener(MyViewHolder.this);
                dish_modify.setOnClickListener(MyViewHolder.this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_delete_menu:
                    removeItem();
                    break;
                case R.id.img_modify_menu:
                    modifyItem();
                    break;
                case R.id.switch1:
                    updateAvailability();
                    break;
            }
        }

        private void updateAvailability(){
            try {
                current_list.get(position).setTmpAv(dish_availability.isChecked());
            }
            catch (Exception e){
                System.out.println("Eccezione: " + e.getMessage());
            }


        }


        //rimuovo piatto
        private void removeItem(){
            try {
                //id del piatto da rimuovere
                int id = current_list.get(position).getId();

                current_list.remove(position);

                GestioneDB DB = new GestioneDB();

                String db  = DB.leggiDB(context, "db_menu");
                JSONObject jsonRootObject = new JSONObject(db);
                JSONArray jsonArray = jsonRootObject.optJSONArray("lista_piatti");

                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id_JSON = Integer.parseInt(jsonObject.optString("id").toString());
                    if(id == id_JSON){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            jsonArray.remove(i);
                        }
                    }
                }

                DB.updateDB(context, jsonRootObject, "db_menu");

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dish_list.getPrimi().size());

            } catch (JSONException e) {
                System.out.println("Eccezione: " + e.getMessage());
            } catch (Exception e){
                System.out.println("Eccezione: " + e.getMessage());
            }
        }

        //modifico piatto
        private void modifyItem(){
            Bundle b = new Bundle();
            b.putSerializable("dish_list", dish_list);
            b.putInt("position", position);

            Intent intent = new Intent(context, ModifyMenuDish.class);
            intent.putExtras(b);
            intent.putExtra("type_enum", menu_type);
            //per leggerlo: result = (type_enum) intent.getSerializableExtra("type_enum");
            context.startActivity(intent);
        }
    }
}
