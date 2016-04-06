package it.polito.mad_lab2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements EditablePhotoListener{

    private EditablePhoto editablePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetSaveButtonVisibility(false);

        SetAlertDelatilsView(R.id.alertDetailsView);

        editablePhoto = (EditablePhoto)findViewById(R.id.photo);
        editablePhoto.addListener(this);
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

    public void eseguiActivityModificaMenu(View v){
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.GestioneMenu.class);
        startActivity(intent);
    }

    @Override
    public void OnPhotoChanged() {
        Toast toast = Toast.makeText(getApplicationContext(), "Listener", Toast.LENGTH_SHORT);
        toast.show();
    }
}
