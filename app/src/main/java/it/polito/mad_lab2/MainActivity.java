package it.polito.mad_lab2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity implements PhotoViewerListener {

    private PhotoViewer photoViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }
        setContentView(R.layout.activity_main);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);

        SetAlertDelatilsView(R.id.alertDetailsView);

        PhotoViewer currentFragment = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.ad_fragment);
        try {
            currentFragment.initPhotoViewer(this);
        } catch (Exception e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        }

        checkDB();
    }

    @Override
    protected void OnSaveButtonPressed()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnAlertButtonPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Alert pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void OnCalendarButtonPressed() {
        throw new UnsupportedOperationException();
    }

    public void eseguiActivityModificaMenu(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneMenu.class);
        startActivity(intent);
    }

    @Override
    public void OnPhotoChanged() {
        Toast toast = Toast.makeText(getApplicationContext(), "Listener", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**** created by Roby on 07/04/2016 *****/
    public void checkDB(){
        String FILENAME = "database";
        StringBuffer str = new StringBuffer("");

        //deleteFile(FILENAME);

        try {
            File f = new File(getFilesDir(), FILENAME);
            if (!f.exists()){

                InputStream dbFile = getResources().openRawResource(R.raw.database);
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                int ch;

                while ((ch = dbFile.read()) != -1) {
                    str.append((char)ch);
                }

                fos.write(str.toString().getBytes());

                fos.close();
                dbFile.close();
                System.out.println("***** DB CREATO *****");
            }
            else {
                System.out.println("***** DB ESISTENTE *****");

                /*FileInputStream fis = openFileInput(FILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                fis.close();
                System.out.println(out.toString());*/
            }

        } catch (IOException e) {
            System.out.println("***** ECCEZIONE! "+ e.getMessage()+" *****");
        }
    }
    /***************************************************************/
}