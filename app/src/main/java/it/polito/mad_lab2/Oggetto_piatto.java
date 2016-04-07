package it.polito.mad_lab2;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Euge on 02/04/2016.
 */
public class Oggetto_piatto implements Serializable {

    private String piatto_name = "";
    private String photo_path = "";
    private int cost = 0;
    private type_enum type = null;
    private int id;
    public enum type_enum {PRIMI, SECONDI, DESSERT, ALTRO }

    public Oggetto_piatto(String name, int cost, String path, type_enum type){
        this.piatto_name = name;
        this.photo_path = path;
        this.cost = cost;
        this.type = type;
    }

    public void setId(int num){
        this.id = num;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String arg){
        this.piatto_name = arg;
    }

    public String getName(){
        return this.piatto_name;
    }

    public void setCost(int arg){
        this.cost = arg;
    }

    public int getCost(){
        return this.cost;
    }

    public void setPhoto(String path){
        this.photo_path = path;
    }

    public String getPhoto(){
        return this.photo_path;
    }

    public void setDishType(type_enum arg){
        this.type = arg;
    }

    public type_enum getDishType(){
        return this.type;
    }

}
