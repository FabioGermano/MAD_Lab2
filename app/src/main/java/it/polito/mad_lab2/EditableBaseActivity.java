package it.polito.mad_lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * To be extended if actvoty works with floating action buttons (add, edit, delete)
 *
 * @author f.germano
 */
public abstract class EditableBaseActivity extends BaseActivity {

    private LinearLayout FAB_layout;
    private ImageButton fab_edit, fab_remove, fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void InitializeFABButtons(boolean editVisibility, boolean removeViibility, boolean addVisibility)
    {
        FAB_layout = (LinearLayout)findViewById(R.id.FAB_layout);
        FAB_layout.bringToFront();

        fab_edit = (ImageButton)findViewById(R.id.fab_edit);
        fab_add = (ImageButton)findViewById(R.id.fab_add);
        fab_remove = (ImageButton)findViewById(R.id.fab_remove);

        if(editVisibility)
        {
            this.fab_edit.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_edit.setVisibility(View.GONE);
        }

        if(removeViibility)
        {
            this.fab_remove.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_remove.setVisibility(View.GONE);
        }

        if(addVisibility)
        {
            this.fab_add.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_add.setVisibility(View.GONE);
        }

        fab_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDeleteButtonPressed();
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAddButtonPressed();
            }
        });
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEditButtonPressed();
            }
        });
    }

    public void SetDeleteFABVisibility(boolean visibility)
    {
        if(visibility)
        {
            this.fab_remove.setVisibility(View.VISIBLE);
        }
        else
        {
            this.fab_remove.setVisibility(View.GONE);
        }
    }

    protected abstract void OnDeleteButtonPressed();
    protected abstract void OnEditButtonPressed();
    protected abstract void OnAddButtonPressed();
}
