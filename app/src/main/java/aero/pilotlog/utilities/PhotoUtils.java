package aero.pilotlog.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tuan.na on 1/28/2015.
 * Image processes
 */
public class PhotoUtils {

    public static int PICK_IMAGE = 1;
    public static int TAKE_IMAGE = 2;

    private Fragment mFragment;

    public PhotoUtils(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public void takePhotoFromCameraForFragment(Context context, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(StorageUtils.getStorageRootFolder(context), fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        mFragment.startActivityForResult(intent, TAKE_IMAGE);
    }

    public void pickPhotoFromGalleryForFragment() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        mFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception ignored) {
            return contentUri.getPath();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Get mime type of photo (file in general)
     *
     * @param context    context
     * @param contentUri uri of file
     * @return string mime type
     */
    public static String getMimeTypeFromURI(Context context, Uri contentUri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(contentUri.toString());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (type == null) {
            ContentResolver cR = context.getContentResolver();
            return cR.getType(contentUri);
        }
        return type;
    }


    /**
     * Scales the provided bitmap to have the height and width provided.
     * since Bitmap.createScaledBitmap(...) produces bad quality bitmaps.)
     *
     * @param bitmap    is the bitmap to scale.
     * @param newWidth  is the desired width of the scaled bitmap. here in use new width 768
     * @param newHeight is the desired height of the scaled bitmap.
     * @return the scaled bitmap.
     */
    public static Bitmap createScaledBitmap(Bitmap bitmap, int newWidth, int newHeight, String filePath) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

//        newWidth = 768;
//        newHeight = (bitmap.getHeight()/bitmap.getWidth()) * 768;

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        if (filePath != null) {
            Bitmap bm = rotateImage(scaledBitmap, filePath);
            if (bm != null) {
                return bm;
            }
        }
        return scaledBitmap;
    }

    /**
     * Short overloaded method for createScaledBitmap with null filePath.
     *
     * @param bitmap    bitmap
     * @param newWidth  new width
     * @param newHeight new height
     * @return bitmap
     */
    public static Bitmap createScaledBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return createScaledBitmap(bitmap, newWidth, newHeight, null);
    }

    /**
     * Resize bitmap
     *
     * @param mActivity activity
     * @param file      file
     * @return file after resize
     */
    public static File resizeBitmap(Activity mActivity, File file) {
        Uri uri = Uri.fromFile(file);
        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 1024 * 512; // 1/2MB
            in = mActivity.getContentResolver().openInputStream(uri);
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            Bitmap originalBitmap = decodeBitmapFromFile(file.getAbsolutePath());

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Bitmap b = originalBitmap;
            in = mActivity.getContentResolver().openInputStream(uri);
            if (scale > 1) {
//                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                if (b == null) {
                    b = BitmapFactory.decodeStream(in, null, o);
                }
                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;
                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
            return makeImageFile(mActivity, rotateImage(b, file.getAbsolutePath()), file.getName());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * Make file from bitmap
     *
     * @param bitmap bitmap
     * @return file
     */
    public static File makeImageFile(Activity mActivity, Bitmap bitmap, String fileName) {
        OutputStream fOut;
        File file = null;
        file = new File(StorageUtils.getStorageRootFolder(mActivity), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create file from bitmap
     *
     * @param bitmap
     * @param pFile
     */
    public static void makeImageFile(Bitmap bitmap, File pFile) {
        OutputStream fOut;
        if (pFile.exists()) {
            pFile.delete();
        }
        try {
            fOut = new FileOutputStream(pFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
//            return pFile;
        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
    }

    /**
     * Rotate bitmap
     *
     * @param bitmap   bitmap
     * @param filePath filePath
     * @return bitmap
     */
    public static Bitmap rotateImage(Bitmap bitmap, String filePath) {
        Bitmap resultBitmap = bitmap;
        try {
            final ExifInterface exifInterface = new ExifInterface(filePath);
            final int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            float angle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                default:
                    break;
            }
            // Rotate the bitmap
            if (orientation != ExifInterface.ORIENTATION_NORMAL) {
                resultBitmap = rotateBitmap(bitmap, angle);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return resultBitmap;
    }

    public static boolean getPhotoOrientation(String filePath) {
        boolean isPortrait = false;
        try {
            Bitmap b = decodeBitmapFromFile(filePath);
            if (b != null) {
                isPortrait = b.getHeight() > b.getWidth();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPortrait;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle) throws OutOfMemoryError {
        final Matrix matrix = new Matrix();
        matrix.setTranslate(source.getWidth() / 2, source.getHeight() / 2);
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }


    /**
     * Return the image file that taken from camera
     *
     * @return file
     */
    public static File getImageFileTakenFromCamera(Context context) {
//        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
//        String imageTimeStamp = (String) CacheUtils.getCache(Constants.IMAGE_TIME_STAMP);
//        if (imageTimeStamp == null){
//            imageTimeStamp = StorageUtils.getStringFromSharedPref(context, Constants.IMAGE_TIME_STAMP);
//        }
//        for (File temp : f.listFiles()) {
//            if (temp.getName().equals(Constants.IMAGE_FILENAME + imageTimeStamp + Constants.JPEG)) {
//                f = temp;
//                break;
//            }
//        }
        return null;
    }

    /**
     * @param mActivity activity
     * @param imageUri  uri of image
     */
    public static void saveTakenImageToGallery(Activity mActivity, Uri imageUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        mActivity.sendBroadcast(mediaScanIntent);
    }

    /**
     * @param mActivity activity
     * @param imageUri  uri of image
     * @return bitmap
     * @throws IOException
     */
    public static Bitmap decodeBitmapFromUri(Activity mActivity, Uri imageUri) throws IOException {
        Bitmap selectedImage;
        InputStream imageStream;
        try {
            imageStream = mActivity.getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);
        } catch (OutOfMemoryError e) {
            //decode stream to bitmap without options can create OutOfMemory error
            //so we resize bitmap with bitmap options
            System.gc();
            imageStream = mActivity.getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);

            options.inSampleSize = calculateInSampleSize(options);
            options.inJustDecodeBounds = false;
            selectedImage = BitmapFactory.decodeStream(imageStream, null, options);

            //if decode stream didn't work, try decode file
            if (selectedImage == null) {
                File f = new File(getRealPathFromURI(mActivity, imageUri));
                selectedImage = decodeBitmapFromFile(f.getAbsolutePath());
            }
        }
        imageStream.close();
        return selectedImage;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        int reqWidth = 1024;
        int reqHeight = 1024 * (height / width);

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeBitmapFromFile(String filePath) {
        try {
            return BitmapFactory.decodeFile(filePath);
        } catch (OutOfMemoryError e) {
            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            options.inSampleSize = calculateInSampleSize(options);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }
    }

}
