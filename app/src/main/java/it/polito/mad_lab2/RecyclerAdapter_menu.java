package it.polito.mad_lab2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Euge on 05/04/2016.
 */
public class RecyclerAdapter_menu extends RecyclerView.Adapter<RecyclerAdapter_menu.MyViewHolder> {

    private List<Oggetto_piatto> dish_list;
    private LayoutInflater myInflater;

    public RecyclerAdapter_menu(Context context, List<Oggetto_piatto> data){
        this.dish_list = data;
        myInflater = LayoutInflater.from(context);
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
        Oggetto_piatto currentObj = dish_list.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.dish_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Oggetto_piatto current;
        private int position;
        private ImageView dish_img;
        private TextView dish_name;
        private TextView dish_price;
        private ImageButton dish_delete;
        private ImageButton dish_modify;
        private Context context;



        public MyViewHolder(View itemView) {
            super(itemView);
            dish_img = (ImageView) itemView.findViewById(R.id.image_dish_menu);
            dish_name = (TextView) itemView.findViewById(R.id.dish_name_menu);
            dish_price = (TextView) itemView.findViewById(R.id.dish_price_menu);
            dish_delete = (ImageButton) itemView.findViewById(R.id.img_delete_menu);
            dish_modify = (ImageButton) itemView.findViewById(R.id.img_modify_menu);

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
            //carico foto
            if(dish_img != null){

            }

        }

        public void setListeners(){
            dish_delete.setOnClickListener(MyViewHolder.this);
            dish_modify.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_delete_menu:
                    removeItem(position);
                    break;
                case R.id.img_modify_menu:
                    modifyItem(current, position);
                    break;
            }
        }

        //rimuovo piatto
        private void removeItem(int position){
            dish_list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dish_list.size());
        }

        //modifico piatto
        private void modifyItem(Oggetto_piatto obj, int position){
            Bundle b = new Bundle();
            b.putSerializable("dish", obj);
            b.putInt("position", position);

            Intent intent = new Intent(context, ModifyMenuDish.class);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}
