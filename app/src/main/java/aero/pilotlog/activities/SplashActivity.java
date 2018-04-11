package aero.pilotlog.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.utilities.OrientationUtils;
import aero.pilotlog.utilities.StorageUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/07/06.
 * Splash Activity
 */
public class SplashActivity extends Activity {

    @Bind(R.id.ivSplashLogo)
    ImageView mIvSplashLogo;

    private static final int SPLASH_SCREEN = 1500;
    private SplashActivity mActivity;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private File mPrefFile, mImageDir, mPilotLogDBFile, mLogbookDBFile;
    private CopyDataTask mCopyDataTask;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = SplashActivity.this;
        setContentView(MCCPilotLogConst.sIsTablet ? R.layout.activity_splash_tablet : R.layout.activity_splash);
        ButterKnife.bind(this);

        if (MCCPilotLogConst.sIsTablet) {
           /* if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mIvSplashLogo.setImageResource(R.drawable.bg_splash_tablet_2);
            } else {
                mIvSplashLogo.setImageResource(R.drawable.bg_splash_2);
            }*/
        } else {
            OrientationUtils.lockOrientationPortrait(mActivity);
        }

        //TODO test
//        createDataTest();

        /*mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage("Copying data to new mccPILOTLOG...");
        mDialog.setCancelable(false);

        mPrefFile = new File(getApplicationContext().getFilesDir().getParent() + "/shared_prefs/PilotLogSettings.xml");
        mImageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "mccPILOTS" *//*+ File.separator*//*);
        mPilotLogDBFile = mActivity.getFileStreamPath("mccPilotDB.db");
        mLogbookDBFile = mActivity.getFileStreamPath("Logbook.db");
        if (mPrefFile.exists() || mImageDir.exists() || mPilotLogDBFile.exists() || mLogbookDBFile.exists()) {
            mCopyDataTask = new CopyDataTask();
            mCopyDataTask.execute();
        } else {*/
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent;
                   /* if (MCCPilotLogConst.sIsTablet) {
                        intent = new Intent(mActivity, MainTabletActivity.class);
                    } else {
                        intent = new Intent(mActivity, MainActivity.class);
                    }*/
                    intent = new Intent(mActivity, MainLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN);
        //}
        setStatusBar();
    }

    private void setStatusBar() {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mcc_color_dark));
        }
    }

    private void createDataTest() {
        try {
//            SharedPreferences oldSettings = getSharedPreferences("PilotLogSettings", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = oldSettings.edit();
//
//            editor.putString("CompanyPrefix", "ABCDEF");
//            editor.putString("NightSS", "0");
//            editor.putString("SignIn", "20");
//            editor.putString("SignOut", "60");
//            editor.putBoolean("OffOnICAO", false);
//            editor.putBoolean("LogDeparture", false);
//            editor.putBoolean("OffOnBlock", false);
//            editor.putBoolean("OffOnBlockUtc", false);
//            editor.putBoolean("OffOnFlightList", false);
//            editor.putBoolean("FlightDecimal", false);
//            editor.putBoolean("saveToContactList", false);
//            editor.putString("DefaultCalendarName", "TuanPV");
//            editor.putBoolean("bAutoBackup", false);
//            editor.putString("SyncID", "PDEBULCPMLPG-4E");
//            editor.commit();

            File fileLogbook = this.getFileStreamPath("Logbook.db");
            File filePilot = this.getFileStreamPath("mccPilotDB.db");
//            mPrefFile = new File(getApplicationContext().getFilesDir().getParent() + "/shared_prefs/PilotLogSettings.xml");
//            File preOldFile = new File("/data/user/0/net.mccpilotlog/shared_prefs/PilotLogSettings.xml");
//            StorageUtils.copyFile(preOldFile, mPrefFile);
            if (!filePilot.exists())//|| firstTime
            {
                // The name of the database to use from the bundled assets.
                InputStream input = this.getAssets().open("database_test/mccPilotDB.db", Context.MODE_PRIVATE);

                FileOutputStream output = this.openFileOutput("mccPilotDB.db", Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                // Write!
                output.getFD().sync();
                output.close();
                input.close();
                //firstTime = false;
            }

            if (!fileLogbook.exists())//|| firstTime
            {
                // The name of the database to use from the bundled assets.
                InputStream input = this.getAssets().open("database_test/Logbook.db", Context.MODE_PRIVATE);

                FileOutputStream output = this.openFileOutput("Logbook.db", Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                // Write!
                output.getFD().sync();
                output.close();
                input.close();
                //firstTime = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyAllPictureFromOldApp() {
        if (mImageDir.exists()) {
            showToastCoping(getString(R.string.message_coping_picture));

            File newPilotPicture = new File(StorageUtils.getStorageRootFolder(this));
            File newSignPicture = new File(StorageUtils.getStorageRootFolder(this) + File.separator + MCCPilotLogConst.SIGN_FOLDER);
            if (!newSignPicture.exists()) {
                newSignPicture.mkdirs();
            }

            File[] oldPilotPictureFiles1 = new File(mImageDir.getPath()).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().startsWith("img.") && pathname.getName().endsWith(".jpg");
                }
            });

//            File[] oldPilotPictureFiles2 = new File(mImageDir.getPath()).listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File pathname) {
//                    return pathname.getName().startsWith("pilot.") && pathname.getName().endsWith(".jpg");
//                }
//            });

            File[] oldSignPictureFiles = new File(mImageDir.getPath()).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().startsWith("img.") && pathname.getName().endsWith(".jpg");
                }
            });

            for (File file : oldPilotPictureFiles1) {
                File newFile = new File(newPilotPicture, file.getName());
                try {
                    StorageUtils.copyFile(file, newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            for (File file : oldPilotPictureFiles2) {
//                File newFile = new File(newPilotPicture, file.getName());
//                try {
//                    StorageUtils.copyFile(file, newFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            for (File file : oldSignPictureFiles) {
                File newFile = new File(newSignPicture, file.getName());
                try {
                    StorageUtils.copyFile(file, newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Delete folder
            if (mImageDir.isDirectory()) {
                String[] children = mImageDir.list();
                for (int i = 0; i < children.length; i++) {
                    try {

                        new File(mImageDir, children[i]).delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                mImageDir.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CopyDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Intent intent;
            if (MCCPilotLogConst.sIsTablet) {
                intent = new Intent(mActivity, MainTabletActivity.class);
            } else {
                intent = new Intent(mActivity, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mDatabaseManagerV5 = new aero.pilotlog.databases.manager.DatabaseManager(SplashActivity.this);
                String oldPath = mActivity.getApplicationInfo().dataDir + "/files";
                copyAllPictureFromOldApp();

                Thread.sleep(SPLASH_SCREEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void showToastCoping(final String pMessage) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, pMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mIvSplashLogo.setImageResource(R.drawable.bg_splash_tablet);
        } else {
            mIvSplashLogo.setImageResource(R.drawable.bg_splash);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCopyDataTask != null) {
            mCopyDataTask.cancel(true);
        }
    }
}
