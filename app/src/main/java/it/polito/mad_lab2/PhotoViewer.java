package it.polito.mad_lab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.germano on 06/04/2016.
 */
public class PhotoViewer extends RelativeLayout implements PhotoDialogListener {

    private ImageView imgPhoto;
    private ImageButton editButton;
    private Context context;

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private int initialImage = -1;
    private boolean isBitmapSetted = false;

    private List<PhotoViewerListener> listeners = new ArrayList<PhotoViewerListener>();
    private Activity callingActivity = null;

    public PhotoViewer(Context context) {
        super(context);
        initControl(context);
    }

    //public EditablePhoto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    //   super(context, attrs, defStyleAttr, defStyleRes);
    //}

    public PhotoViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    public PhotoViewer(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhotoViewer);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.PhotoViewer_initialBackground:
                    this.initialImage = a.getResourceId(attr, -1);
                    break;
            }
        }
        a.recycle();

        initControl(context);
    }


    /**
     * Init the control. The activity must implement the PhotoViewerListener interface.
     *
     * @param activity
     * @throws Exception
     */
    public void initPhotoViewer(Activity activity) throws Exception {
        if(activity instanceof PhotoViewerListener) {
            this.callingActivity = activity;
            listeners.add((PhotoViewerListener) activity);
        }
        else
        {
            throw new Exception("The argument activity must implement the PhotoViewerListener interface.");
        }
    }

    private void notifyPhotoChanged()
    {
        for(PhotoViewerListener i : listeners)
        {
            i.OnPhotoChanged();
        }
    }

    private void initControl(Context context)
    {
        this.context = context;

        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.photo_viewer, this);

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
        PhotoDialog dialog = new PhotoDialog(this.context, this.isBitmapSetted);
        dialog.addListener(this);
    }

    /**
     * To be called in order to set the bitmap (to be intended as a thumb one - of little dimensions) to the ImageView.
     *
     * @param bitmap
     */
    public void setThumbBitmap(Bitmap bitmap) {
        Drawable oldDrawable = this.imgPhoto.getDrawable();
        if (oldDrawable != null) {
            Bitmap old = ((BitmapDrawable)oldDrawable).getBitmap();
            if(old != null)
            {
                old.recycle();
            }
        }

        this.imgPhoto.setImageBitmap(bitmap);
        this.isBitmapSetted = true;
    }

    /**
     * To be called in the onActivityResult method of the owner activity.
     * The PhotoViewer must be initialized by calling the initPhotoViewer method.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void managePhotoResult(int requestCode, int resultCode, Intent data) {
        this.setThumbBitmap((Bitmap) data.getExtras().get("data"));
        notifyPhotoChanged();
    }

    /* dialog management */
    @Override
    public void OnCameraButtonPressed() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.callingActivity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void OnGalleryButtonPressed() {
        Toast toast = Toast.makeText(context, "gallery pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void OnRemoveButtonListener() {

    }
    /* end dialog management */
}
