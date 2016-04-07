package it.polito.mad_lab2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

        photoViewer = (PhotoViewer)findViewById(R.id.photo);
        try {
            photoViewer.initPhotoViewer(this);
        } catch (Exception e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoViewer.managePhotoResult(requestCode, resultCode, data);
    }
}