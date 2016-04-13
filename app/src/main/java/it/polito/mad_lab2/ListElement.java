package it.polito.mad_lab2;

/**
 * Created by Giovanna on 11/04/2016.
 */
public class ListElement {
    public String name;
    public int quantity;


    public ListElement(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


