package it.polito.mad_lab2.photo_viewer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import it.polito.mad_lab2.R;

/**
 * Created by f.germano on 07/04/2016.
 */
public class PhotoViewer extends Fragment  implements PhotoDialogListener {

    private ImageView imgPhoto;
    private ImageButton editButton;

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private int initialImage = -1;
    private boolean isBitmapSetted = false, isLogo = false;
    private String pictureImagePath;

    private PhotoViewerListener listener;

    public PhotoViewer()
    {

    }

    private void notifyPhotoChanged(Bitmap thumb, Bitmap large)
    {
        listener.OnPhotoChanged(thumb, large);
    }

    private void notifyPhotoRemoved()
    {
        listener.OnPhotoRemoved();
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
        this.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPressed();
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
                case R.styleable.PhotoViewer_isLogo:
                    this.isLogo = a.getBoolean(attr, false);
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

    private void photoPressed()
    {
        if(!this.isLogo && this.isBitmapSetted)
        {
            Bitmap large = listener.OnPhotoViewerActivityStarting();

            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, "image-tran.jpg");
            try {
                fOut = new FileOutputStream(file);

                large.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();

                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

                Intent intent = new Intent(getActivity(), it.polito.mad_lab2.photo_viewer.PhotoViewActivity.class);
                intent.putExtra("photoPath", file.getAbsolutePath());

                startActivity(intent);

            } catch (FileNotFoundException e) {
                Log.d(e.getMessage(), e.getMessage(), e);
            } catch (IOException e) {
                Log.d(e.getMessage(), e.getMessage(), e);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PhotoViewerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PhotoViewerListener");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(this.isLogo) {
            if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                Bitmap thumb = (Bitmap) data.getExtras().get("data");
                thumb = resizeBitmap(thumb, 196);
                this.setThumbBitmap(thumb);
                notifyPhotoChanged(thumb, null);
            }
            else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    Bitmap thumb = resizeBitmap(bitmap, 196);
                    //bitmap.recycle();
                    this.setThumbBitmap(thumb);
                    notifyPhotoChanged(thumb, null);
                } catch (IOException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                }
            }
        }
        else
        {
            if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                File imgFile = new  File(pictureImagePath);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Bitmap thumb = resizeBitmap(myBitmap, 196);
                    Bitmap large = resizeBitmap(myBitmap, 1024);
                    //myBitmap.recycle();
                    this.setThumbBitmap(thumb);
                    notifyPhotoChanged(thumb, large);
                }
            }
            else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    Bitmap thumb = resizeBitmap(bitmap, 196);
                    Bitmap large = resizeBitmap(bitmap, 1024);
                    //bitmap.recycle();
                    this.setThumbBitmap(thumb);
                    notifyPhotoChanged(thumb, large);
                } catch (IOException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                }
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitMap, int new_size)
    {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        int scale = 1;

        while (width / scale >= new_size && height / scale >= new_size)
            scale *= 2;

        width = width / scale;
        height = height / scale;

        return Bitmap.createScaledBitmap(bitMap, width, height, true);
    }

    /* dialog management */
    @Override
    public void OnCameraButtonPressed() {
        if(this.isLogo) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        else
        {
            manageCameraForLargePhoto();
        }
    }

    private void manageCameraForLargePhoto()
    {
        String imageFileName = "temp.jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void OnGalleryButtonPressed() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void OnRemoveButtonListener() {
        this.imgPhoto.setImageResource(initialImage);
        notifyPhotoRemoved();
    }
    /* end dialog management */
}