package it.polito.mad_lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetSaveButtonVisibility(true);
        SetBackButtonVisibility(false);
        SetAlertCount(5);
    }

    @Override
    protected void OnSaveButtonPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Save pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void OnBackButtonPressed()
    {
        return;
    }

    @Override
    protected void OnAlertButtonPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Alert pressed", Toast.LENGTH_SHORT);
        toast.show();
    }
}
