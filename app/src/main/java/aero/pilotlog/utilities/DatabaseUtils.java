package aero.pilotlog.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tuan.pv on 2015/07/03.
 */
public class DatabaseUtils {

    public static final String DATABASES = "/databases/";

    /**
     * Copy existing database file in system
     */
    public static void copyDataBase(Context pContext, String pName) throws Exception {
        int length;
        byte[] buffer = new byte[1024];
        String path = StorageUtils.getStorageRootFolder(pContext) + DATABASES;
        Log.d("copy database", path + " " +pName);
        InputStream databaseInputFile = pContext.getAssets().open(pName);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        OutputStream databaseOutputFile = new FileOutputStream(path + pName);

        while ((length = databaseInputFile.read(buffer)) > 0) {
            Log.d("length buffer", String.valueOf(length));
            databaseOutputFile.write(buffer, 0, length);
            databaseOutputFile.flush();
        }
        databaseInputFile.close();
        databaseOutputFile.close();
    }

    /**
     * Check Database if it exists
     */
    public static boolean databaseExists(Context pContext, String pName) {
        SQLiteDatabase sqliteDatabase = null;
        String path = StorageUtils.getStorageRootFolder(pContext) + DATABASES;
        try {
            sqliteDatabase = SQLiteDatabase.openDatabase(path + pName, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }

        return sqliteDatabase != null;
    }
}
