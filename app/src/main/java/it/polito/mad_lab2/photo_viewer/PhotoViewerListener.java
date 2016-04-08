package it.polito.mad_lab2.photo_viewer;

import android.graphics.Bitmap;

/**
 * Created by f.germano on 06/04/2016.
 */
public interface PhotoViewerListener {

    /**
     * Invoked when photo changes
     */
    void OnPhotoChanged(Bitmap thumb, Bitmap large);
    Bitmap OnPhotoViewerActivityStarting();
    void OnPhotoRemoved();
}
