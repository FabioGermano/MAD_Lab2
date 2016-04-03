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
    private String type = "";

    public Oggetto_piatto(String name, int cost, String path, String type){
        this.piatto_name = name;
        this.photo_path = path;
        this.cost = cost;
        this.type = type;
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

    public void setDishType(String arg){
        this.type = arg;
    }

    public String getDishType(){
        return this.type;
    }

}
