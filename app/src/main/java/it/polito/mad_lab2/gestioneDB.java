package it.polito.mad_lab2;

import android.content.Context;
import android.os.Build;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Roby on 09/04/2016.
 */
public class GestioneDB {

    private String filename_menu = "db_menu";
    private String filename_offerte = "db_offerte";

    public boolean creaDB(Context context){
        int ch;

        File f1 = new File(context.getFilesDir(), filename_menu);
        File f2 = new File(context.getFilesDir(), filename_offerte);


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
















}


