package it.polito.mad_lab2;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.germano on 06/04/2016.
 */
public class EditablePhoto extends RelativeLayout {

    private ImageView imgPhoto;
    private ImageButton editButton;
    private Context context;

    private int initialImage = -1;

    private List<EditablePhotoListener> listeners = new ArrayList<EditablePhotoListener>();

    public EditablePhoto(Context context) {
        super(context);
        initControl(context);
    }

    //public EditablePhoto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    //   super(context, attrs, defStyleAttr, defStyleRes);
    //}

    public EditablePhoto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    public EditablePhoto(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditablePhoto );
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.EditablePhoto_initialImage:
                    this.initialImage = a.getResourceId(attr, -1);
                    break;
            }
        }
        a.recycle();

        initControl(context);
    }

    public void addListener(EditablePhotoListener toAdd) {
        listeners.add(toAdd);
    }

    private void notifyPhotoChanged()
    {
        for(EditablePhotoListener i : listeners)
        {
            i.OnPhotoChanged();
        }
    }

    private void initControl(Context context)
    {
        this.context = context;

        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.editable_photo, this);

        this.imgPhoto = (ImageView)findViewById(R.id.epImgPhoto);
        this.editButton = (ImageButton)findViewById(R.id.epEditButton);

        this.editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonPressed();
            }
        });

        // consider attributes
        if(this.initialImage != -1)
        {
            this.imgPhoto.setImageResource(this.initialImage);
        }
    }

    private void editButtonPressed()
    {
        notifyPhotoChanged();
    }
}
