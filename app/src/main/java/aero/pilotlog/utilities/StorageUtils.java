package aero.pilotlog.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import aero.pilotlog.common.MCCPilotLogConst;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by tuan.na on 7/10/2015.
 * Storage Utils
 */
public class StorageUtils {

    public static final String TAG = "StorageUtils";
    private static final int BUFFER = 1024;
    public static String MCCPILOTLOG_ROOT_FOLDER = "PILOTLOG";

    public static String readInternalFile(Context context, String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file;
            if (filePath.startsWith("/")) {
                file = new File(context.getFilesDir() + filePath);
            } else {
                file = new File(context.getFilesDir() + "/" + filePath);
            }
            if (!file.exists()) {
                return MCCPilotLogConst.STRING_EMPTY;
            }
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + filePath);
        } catch (OutOfMemoryError | IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static boolean writeHashMapLongToSharedPref(Context context, HashMap<String, Long> backupValue, String key) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(key, 0).edit();

            for (Map.Entry entry : backupValue.entrySet()) {
                editor.putLong(entry.getKey().toString(), (long) entry.getValue());
            }
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static HashMap<String, Long> getHashMapLongFromSharedPref(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, 0);
        HashMap<String, Long> backupValue = new HashMap<>();
        for( Map.Entry entry : sharedPref.getAll().entrySet() )
            backupValue.put(entry.getKey().toString(), (long)entry.getValue() );
        return backupValue;
    }


    public static boolean writeIntToSharedPref(Context context, String key, int value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putInt(key, value).apply();
        return true;
    }

    public static boolean writeLongToSharedPref(Context context, String key, long value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putLong(key, value).apply();
        return true;
    }

    public static boolean writeStringToSharedPref(Context context, String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(key, value).apply();
        return true;
    }

    public static boolean writeBooleanToSharedPref(Context context, String key, Boolean value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putBoolean(key, value).apply();
        return true;
    }

    public static int getIntFromSharedPref(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, 0);
    }

    public static int getIntFromSharedPref(Context context, String key, int defValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, defValue);
    }

    public static long getLongFromSharedPref(Context context, String key, long defValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getLong(key, defValue);
    }

    public static boolean getBooleanFromSharedPref(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public static String getStringFromSharedPref(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, MCCPilotLogConst.STRING_EMPTY);
    }

    public static String getStringFromSharedPref(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, defaultValue);
    }

    public static boolean isFileExistInternal(Context context, String fileName) {
        File file = new File(context.getFilesDir().toString() + MCCPilotLogConst.SLASH + fileName);
        return file.exists();
    }

    public static boolean isFileExistExternalPrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);
        return file.exists();
    }

    public static boolean deleteFileInternal(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.delete();
    }

    public static boolean deleteFileInExternalPrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);
        return file.delete();
    }

    public static void deleteFolderRecursive(File file) {

        if (file.isDirectory()) {
            if (file.listFiles() != null)
                for (File child : file.listFiles()) {
                    deleteFolderRecursive(child);
                }
        }
        file.delete();
    }

    /**
     * Checks if external storage is available for read and write
     *
     * @return true if writable, false if not
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if external storage is available to at least read
     *
     * @return true if readable, false if not
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File copyFileToExternalFilesDir(Context context, File source) {
        File destination = new File(context.getExternalFilesDir(null), source.getName());
        return copyFileToExternalFilesDir(context, source, destination);
    }

    public static File copyFileToExternalFilesDir(Context context, File source, File destination) {
        if (destination == null) {
            destination = new File(context.getExternalFilesDir(null), source.getName());
        }
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(destination);
            byte[] buf = new byte[BUFFER];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return destination;
    }

    public static File compressFolder(Context context, File sourceDir) {
        String folderName = sourceDir.getName();
        File destination = new File(getStorageRootFolder(context), folderName + ".zip");
        BufferedInputStream origin;
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
            byte data[] = new byte[BUFFER];
            for (File sourceFile : sourceDir.listFiles()) {
                FileInputStream fi = new FileInputStream(sourceFile);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(folderName + File.separator + sourceFile.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            return null;
        }
        return destination;
    }

    public static File compressFile(Context context, File sourceDir) {
        String folderName = sourceDir.getName();
        File destination = new File(getStorageRootFolder(context), folderName.split("\\.")[0] + ".zip");
        BufferedInputStream origin;
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
            byte data[] = new byte[BUFFER];
            //for (File sourceFile : sourceDir.listFiles()) {
                FileInputStream fi = new FileInputStream(sourceDir);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(folderName + File.separator + sourceDir.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
           // }
            out.close();
        } catch (Exception e) {
            return null;
        }
        return destination;
    }

    public static boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;
        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void zipSubFolder(ZipOutputStream out, File folder,
                                     int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /**
     * gets the last path component
     * <p/>
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        return segments[segments.length - 1];
    }

    /**
     * Root folder of application. Will be used for saving and accessing files such as XML, image files, etc.
     *
     * @return file path
     */
    public static String getStorageRootFolder(Context pContext) {
        String mPath = pContext.getExternalFilesDir(null)
                + File.separator + MCCPILOTLOG_ROOT_FOLDER;
        if (!new File(mPath).exists()) {
            new File(mPath).mkdirs();
        }
        return mPath;
    }

    public static void unzip(String pZipFile, String pUnzipLocation) {
        try {
            FileInputStream fin = new FileInputStream(pZipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                LogUtils.e("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    File f = new File(pUnzipLocation + ze.getName());
                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                } else {
                    FileOutputStream fout = new FileOutputStream(pUnzipLocation + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
        }
        System.gc();
    }

    public static void unzip(String pZipFile) {
        File f = new File(pZipFile);
        unzip(pZipFile, f.getParent());
        System.gc();
    }

    /**
     * Copy file to another destination
     *
     * @param src file source
     * @param dst file destination
     * @throws IOException
     */
    public static void copyFile(File src, File dst) throws IOException {
        if (!dst.exists()) {
            dst.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(src).getChannel();
            destination = new FileOutputStream(dst).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}