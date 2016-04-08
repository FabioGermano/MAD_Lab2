package it.polito.mad_lab2;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eugenio on 07/04/2016.
 */
public class Oggetto_menu implements Serializable {
    private ArrayList<Oggetto_piatto> primi = null;
    private ArrayList<Oggetto_piatto> secondi = null;
    private ArrayList<Oggetto_piatto> dessert = null;
    private ArrayList<Oggetto_piatto> altro = null;

    public Oggetto_menu(){
        this.primi = new ArrayList<>();
        this.secondi = new ArrayList<>();
        this.dessert = new ArrayList<>();
        this.altro = new ArrayList<>();
    }

    public ArrayList<Oggetto_piatto> getPrimi(){
        return this.primi;
    }

    public ArrayList<Oggetto_piatto> getSecondi(){
        return this.secondi;
    }

    public ArrayList<Oggetto_piatto> getDessert(){
        return this.dessert;
    }

    public ArrayList<Oggetto_piatto> getAltro(){
        return this.altro;
    }

    public void addPrimo(Oggetto_piatto obj){
        if(this.primi == null){
            this.primi = new ArrayList<>();
        }

        this.primi.add(obj);
    }

    public void addSecondo(Oggetto_piatto obj){
        if(this.secondi == null){
            this.secondi = new ArrayList<>();
        }

        this.secondi.add(obj);
    }

    public void addDessert(Oggetto_piatto obj){
        if(this.dessert == null){
            this.dessert = new ArrayList<>();
        }

        this.dessert.add(obj);
    }

    public void addAltro(Oggetto_piatto obj){
        if(this.altro == null){
            this.altro = new ArrayList<>();
        }

        this.altro.add(obj);
    }
}
