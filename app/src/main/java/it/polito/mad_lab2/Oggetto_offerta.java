package it.polito.mad_lab2;

import java.io.Serializable;

/**
 * Created by Euge on 08/04/2016.
 */
public class Oggetto_offerta implements Serializable {

    private String piatto_name = "";
    private String photo_path = "";
    private int cost = 0;
    private int id;
    private String note = "";
    private boolean[] days = null;
    private boolean availability;
    private boolean tmpAv;

    public Oggetto_offerta(String name, int cost, String path, boolean[] days){
        this.piatto_name = name;
        this.photo_path = path;
        this.cost = cost;
        this.days = days;
        this.availability = true;
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

    public boolean[] getDays() { return days; }
    public void setDays(boolean[] days) { this.days = days; }

    public void setPhoto(String path){
        this.photo_path = path;
    }

    public String getPhoto(){
        return this.photo_path;
    }

    public void setNote(String arg){
        this.note = arg;
    }

    public String getNote(){
        return this.note;
    }

    public boolean isAvailable(){
        return availability;
    }

    public void setAvailability(boolean arg){
        this.availability = arg;
    }

    public void setTmpAv(boolean av){
        this.tmpAv = av;
    }

    public boolean getTmpAv(){
        return tmpAv;
    }
}
