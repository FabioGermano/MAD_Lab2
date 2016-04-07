package it.polito.mad_lab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.germano on 07/04/2016.
 */
public class PhotoViewer extends Fragment  implements PhotoDialogListener {

    private ImageView imgPhoto;
    private ImageButton editButton;

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private int initialImage = -1;
    private boolean isBitmapSetted = false;

    private List<PhotoViewerListener> listeners = new ArrayList<PhotoViewerListener>();

    public PhotoViewer()
    {

    }

    private void notifyPhotoChanged()
    {
        for(PhotoViewerListener i : listeners)
        {
            i.OnPhotoChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.photo_viewer, container, false);

        this.imgPhoto = (ImageView)rootView.findViewById(R.id.epImgPhoto);
        this.editButton = (ImageButton)rootView.findViewById(R.id.epEditButton);

        this.editButton.setOnClickListener(new View.OnClickListener() {
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

        return rootView;
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the arguments we handle.
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoViewer);
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
    }

    private void editButtonPressed()
    {
        PhotoDialog dialog = new PhotoDialog(getContext(), this.isBitmapSetted);
        dialog.addListener(this);
    }

    /**
     * Init the control. The activity must implement the PhotoViewerListener interface.
     *
     * @param activity
     * @throws Exception
     */
    public void initPhotoViewer(Activity activity) throws Exception {
        if(activity instanceof PhotoViewerListener) {
            listeners.add((PhotoViewerListener) activity);
        }
        else
        {
            throw new Exception("The argument activity must implement the PhotoViewerListener interface.");
        }
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


    /* dialog management */
    @Override
    public void OnCameraButtonPressed() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void OnGalleryButtonPressed() {
        Toast toast = Toast.makeText(getActivity(), "gallery pressed", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void OnRemoveButtonListener() {

    }
    /* end dialog management */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.setThumbBitmap((Bitmap) data.getExtras().get("data"));
        notifyPhotoChanged();
    }
}
