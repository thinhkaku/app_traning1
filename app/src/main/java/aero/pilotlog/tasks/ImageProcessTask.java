package aero.pilotlog.tasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by tuan.na on 7/22/2015.
 */
public abstract class ImageProcessTask extends AsyncTask<File, Void, Bitmap> {

    private Activity mActivity;
    private String mFileName;
    private Boolean isCopyFromGallery;
    private File f;

    public ImageProcessTask(Activity pActivity, String pFileName, boolean pIsCopyFromGallery) {
        mActivity = pActivity;
        mFileName = pFileName;
        isCopyFromGallery = pIsCopyFromGallery;
    }

    public ImageProcessTask(Activity pActivity) {
        mActivity = pActivity;
    }

    @Override
    protected Bitmap doInBackground(File... files) {
        try {
            if (mFileName != null && isCopyFromGallery != null) {
                f = new File(StorageUtils.getStorageRootFolder(mActivity), mFileName);
                if (isCopyFromGallery) {
                    f.delete();
                    Utils.copy(files[0], f);
                }
                f = PhotoUtils.resizeBitmap(mActivity, f);
            } else {
                f = files[0];
            }
            return PhotoUtils.decodeBitmapFromUri(mActivity, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        handleBitmap(bitmap);
    }

    public abstract void handleBitmap(Bitmap bitmap);
}
