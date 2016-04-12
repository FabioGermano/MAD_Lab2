package it.polito.mad_lab2;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.polito.mad_lab2.data.reservation.ReservationEntity;

/**
 * Created by Roby on 09/04/2016.
 */
public class GestioneDB {

    private String filename_menu = "db_menu";
    private String filename_offerte = "db_offerte";
    private String filename_profilo = "db_profilo";
    private final String filename_reservation = "db_reservation";

    public boolean creaDB(Context context){
        int ch;

        File f1 = new File(context.getFilesDir(), filename_menu);
        File f2 = new File(context.getFilesDir(), filename_offerte);
        File f3 = new File(context.getFilesDir(), filename_profilo);
        File f4 = new File(context.getFilesDir(), filename_reservation);

        try {
            StringBuffer str;
            InputStream dbFile;
            FileOutputStream fos;

            if (!f1.exists()) {
                //creazione file locale db menu
                str = new StringBuffer("");
                dbFile = context.getResources().openRawResource(R.raw.db_menu);
                fos = context.openFileOutput(filename_menu, Context.MODE_PRIVATE);

                while ((ch = dbFile.read()) != -1) {
                    str.append((char) ch);
                }
                fos.write(str.toString().getBytes());
                fos.close();
                dbFile.close();
                System.out.println("***** DB MENU CREATO *****");
            }
            else {
                System.out.println("***** DB MENU ESISTENTE *****");
            }

            if (!f2.exists()) {
                //creazione file locale db offerte
                str = new StringBuffer("");
                dbFile = context.getResources().openRawResource(R.raw.db_offerte);
                fos = context.openFileOutput(filename_offerte, Context.MODE_PRIVATE);

                while ((ch = dbFile.read()) != -1) {
                    str.append((char) ch);
                }
                fos.write(str.toString().getBytes());

                fos.close();
                dbFile.close();
                System.out.println("***** DB OFFERTE CREATO *****");

            }
            else {
                System.out.println("***** DB OFFERTE ESISTENTE *****");
            }

            if (!f3.exists()) {
                //creazione file locale db profilo
                str = new StringBuffer("");
                dbFile = context.getResources().openRawResource(R.raw.db_profilo);
                fos = context.openFileOutput(filename_profilo, Context.MODE_PRIVATE);

                while ((ch = dbFile.read()) != -1) {
                    str.append((char) ch);
                }
                fos.write(str.toString().getBytes());

                fos.close();
                dbFile.close();
                System.out.println("***** DB PROFILO CREATO *****");
            }
            else {
                System.out.println("***** DB PROFILO ESISTENTE *****");
            }

            if (!f4.exists()) {
                //creazione file locale db reservation
                str = new StringBuffer("");
                dbFile = context.getResources().openRawResource(R.raw.db_reservation);
                fos = context.openFileOutput(filename_reservation, Context.MODE_PRIVATE);

                while ((ch = dbFile.read()) != -1) {
                    str.append((char) ch);
                }
                fos.write(str.toString().getBytes());

                fos.close();
                dbFile.close();
                System.out.println("***** DB PROFILO CREATO *****");
            }
            else {
                System.out.println("***** DB PROFILO ESISTENTE *****");
            }

            return true;
        }
        catch (FileNotFoundException e) {
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
        catch (IOException e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
    }


    public String leggiDB(Context context, String db){
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(db);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            fis.close();
            return str.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Eccezione:" + e.getMessage());
            return null;
        }catch (IOException e){
            System.out.println("Eccezione:" + e.getMessage());
            return null;
        }
    }

    public boolean updateDB(Context context, JSONObject jsonRootObject, String db){

        try {
            FileOutputStream fos = context.openFileOutput(db, Context.MODE_PRIVATE);
            String newDB = jsonRootObject.toString();
            fos.write(newDB.getBytes());
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
        catch (IOException e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
    }

    public boolean deleteDB(Context context, String db){

        File f = new File(context.getFilesDir(), db);

        if (f.exists())
            context.deleteFile(db);

        return true;
    }

    public int[] getOrario(Context context, int id){
        try{
            int[] orario = new int[4];
            String tmp;

            String db = leggiDB(context, "db_profilo");;
            JSONObject jsonRootObject = new JSONObject(db);
            JSONArray jsonArray = jsonRootObject.optJSONArray("profilo");
            JSONObject orari = jsonArray.getJSONObject(1);

            switch (id){
                case 0:
                    tmp = orari.optString("lun").toString();
                    break;
                case 1:
                    tmp = orari.optString("mar").toString();
                    break;
                case 2:
                    tmp = orari.optString("mer").toString();
                    break;
                case 3:
                    tmp = orari.optString("gio").toString();
                    break;
                case 4:
                    tmp = orari.optString("ven").toString();
                    break;
                case 5:
                    tmp = orari.optString("sab").toString();
                    break;
                case 6:
                    tmp = orari.optString("dom").toString();
                    break;
                default:
                    return null;
            }

            if (tmp.compareTo("null") == 0 || tmp.compareTo("CLOSED") == 0){
                return null;
            }

            int n1  = tmp.indexOf(":");
            int n2 = tmp.indexOf(" - ");
            int n3 = tmp.indexOf(":", n2);

            orario[0] = Integer.parseInt(tmp.substring(0, n1));
            orario[1] = Integer.parseInt(tmp.substring(n1+1, n2));
            orario[2] = Integer.parseInt(tmp.substring(n2+3, n3));
            orario[3] = Integer.parseInt(tmp.substring(n3+1, tmp.length()));

            return orario;
        }
        catch (Exception e){
            System.out.println("Eccezione:" + e.getMessage());
            return null;
        }
    }

    public ReservationEntity getAllReservations(Context context)
    {
        String jsonReservations = leggiDB(context, filename_reservation);
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.fromJson(jsonReservations, ReservationEntity.class);
    }

    public boolean UpdateReservations(Context context, ReservationEntity reservationEntity){
        Gson gson = new GsonBuilder().serializeNulls().create();

        String jsonReservations = gson.toJson(reservationEntity);

        try {
            FileOutputStream fos = context.openFileOutput(filename_reservation, Context.MODE_PRIVATE);
            fos.write(jsonReservations.getBytes());
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
        catch (IOException e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
    }
}



