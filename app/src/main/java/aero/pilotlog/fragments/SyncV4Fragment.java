/*
package net.mcc3si.fragments;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

import net.mcc3si.BuildConfig;
import net.mcc3si.R;
import net.mcc3si.common.MCCPilotLogConst;
import net.mcc3si.common.SettingsConst;
import net.mcc3si.common.StateKey;
import net.mcc3si.databases.entities.Pilot;
import net.mcc3si.databases.entities.Setting;
import net.mcc3si.databases.manager.DatabaseManager;
import net.mcc3si.sync.SyncConst;
import net.mcc3si.sync.SyncData4;
import net.mcc3si.sync.SyncStatusV4;
import net.mcc3si.sync.SyncV4Functions;
import net.mcc3si.sync.SyncViewModel;
import net.mcc3si.tasks.CheckInternetTask;
import net.mcc3si.tasks.DownloadAsync;
import net.mcc3si.utilities.DatabaseUtils;
import net.mcc3si.utilities.LogUtils;
import net.mcc3si.utilities.MCCApplication;
import net.mcc3si.utilities.NetworkUtils;
import net.mcc3si.utilities.StorageUtils;
import net.mcc3si.utilities.Utils;
import net.mcc3si.widgets.MccDialog;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;

*/
/**
 * Created by tuan.na on 8/27/2015.
 * Sync 4 screen and logic
 *//*

public class SyncV4Fragment {*/
/*} extends BaseMCCFragment {*//*


   */
/* @Bind(R.id.tv_send_log)
    TextView mTvSendLog;
    @Bind(R.id.btn_sync)
    ActionProcessButton mBtnSync;

    @Bind(R.id.tv_network)
    TextView mTvNetworkStatus;
    *//*
*/
/*@Bind(R.id.tv_sync_id)
    TextView mTvSyncId;*//*
*/
/*
    @Bind(R.id.tv_sync_status)
    TextView mTvSyncStatusV4;
    @Bind(R.id.tv_sync_log)
    TextView mTvSyncLog;
    @Bind(R.id.tvHistory)
    TextView mTvHistory;
    @Bind(R.id.progress_bar)
    ProgressBar mHorizontalProgressBar;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.sw_sync_incomplete_flights)
    Switch mSwSyncIncompleteFlights;
    @Bind(R.id.tv_version)
    TextView mTvVersion;
    @Nullable
    @Bind(R.id.leftBarFragment)
    LinearLayout mLeftBarFragment;
    @Nullable
    @Bind(R.id.rightBarFragment)
    LinearLayout mRightBarFragment;
    @Nullable
    @Bind(R.id.rightContentFragment)
    LinearLayout mRightContentFragment;
    @Nullable
    @Bind(R.id.leftContentFragment)
    LinearLayout mLeftContentFragment;

    public static final int CURRENT_XML_VERSION = 9;

    private boolean isSyncing = false;
    private boolean syncIncompleteFlights = false;
    public int mFoundFileNumber = 0;

    public int intPilots, intAircrafts, intAirfields, intFlights, intICal, intLogbook, intCurrencies, intPicture;

    private boolean isError;
    //private String syncID;
    public int iStatus = 0;
    private AlertDialog mNoCalendarDialog;
    private int mBlueColor, mRedColor;
    private SynchronizeTask mSyncTask;
    private CountDownLatch mCountDownLatch;
    private List<CurrentFileName> arrFileNames = new ArrayList<>();
    private String mNetworkType;
    private Thread mThread;
    private static final String GET_TABLE_ID_FILE = "mccPILOTLOG_getTableId.txt";
    private static final String SET_TABLE_ID_FILE = "mccPILOTLOG_setTableId.txt";
    private int CYAN = Color.rgb(85, 142, 191);
    private int GRAY = Color.rgb(128, 128, 128);
    private SyncData4 syncData;
    @Override
    protected int getHeaderResId() {
        return isTablet() ? R.layout.layout_action_bar_sync_tablet_v4 : R.layout.layout_sync_action_bar_v4;
    }

    @Override
    protected int getContentResId() {
        return isTablet() ? R.layout.fragment_sync_tablet : R.layout.fragment_sync4;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
        mBlueColor = getResources().getColor(R.color.mcc_blue);
        mRedColor = getResources().getColor(android.R.color.holo_red_light);
    }

    *//*
*/
/**
     * Initialize view
     *//*
*/
/*
    private void initView() {
        syncData = new SyncData4(mActivity, SyncV4Fragment.this);
        if (isTablet()) {
            updateWidthLayoutTablet();
        }
        mTvVersion.setText(R.string.sync_title_v4);
        mBtnSync.setMode(ActionProcessButton.Mode.ENDLESS);
       *//*
*/
/* Setting setting = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting != null) {
            syncID = setting.getData();
        }*//*
*/
/*
        //mTvSyncId.setText(syncID);

        if (isHavingNetwork()) {
            mTvNetworkStatus.setText(NetworkUtils.getNetworkType(mActivity));
            mTvNetworkStatus.setTextColor(mBlueColor);
        } else {
            mTvNetworkStatus.setTextColor(mRedColor);
            mTvNetworkStatus.setText(NetworkUtils.getNetworkType(mActivity));
        }
        //check status internet each 2 second.
        mThread = new Thread(new Task());
        mThread.start();

        mNoCalendarDialog = MccDialog.getOkAlertDialog(mActivity, R.string.error, R.string.error_no_calendar_message);
        mSwSyncIncompleteFlights.setChecked(StorageUtils.getBooleanFromSharedPref(mActivity, MCCPilotLogConst.SYNC_INCOMPLETE_FLIGHT, false));
    }

    private void threadMsg(String msg) {
        if (msg != null && !msg.equals("")) {
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", msg);
            msgObj.setData(b);
            handler.sendMessage(msgObj);
        }
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String aResponse = msg.getData().getString("message");
            if ((null != aResponse)) {
                if (mNetworkType.equals("NO Network") || mNetworkType.equals("UNKNOWN")) {
                    mTvNetworkStatus.setTextColor(mRedColor);
                } else {
                    mTvNetworkStatus.setTextColor(mBlueColor);
                }
                mTvNetworkStatus.setText(mNetworkType);
            }
        }
    };

    class Task implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(2000);
                    mNetworkType = NetworkUtils.getNetworkType(mActivity);
                    threadMsg("check");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @OnCheckedChanged(R.id.sw_sync_incomplete_flights)
    public void onCheckChanged(CompoundButton pCompoundButton, boolean pIsChecked) {
        if (pCompoundButton.getId() == R.id.sw_sync_incomplete_flights) {
            syncIncompleteFlights = pIsChecked;
            StorageUtils.writeBooleanToSharedPref(mActivity, MCCPilotLogConst.SYNC_INCOMPLETE_FLIGHT, pIsChecked);
        }
    }

    @OnClick({R.id.btn_sync, R.id.tv_send_log, R.id.ibMenu, R.id.tvHistory})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btn_sync:
                if (!isSyncing) {
                    synchronize();
                }
                break;
            case R.id.tv_send_log:
                mailLog();
                break;
            case R.id.ibMenu:
                if (isSyncing) {
                    MccDialog.getOkCancelAlertDialog(mActivity, R.string.message_interrupt_title, R.string.message_interrupt_content, new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == Dialog.BUTTON_POSITIVE && mSyncTask != null && mSyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                                mSyncTask.cancel(true);
                                syncData.isCalcel = true;
                                mActivity.setSyncing(false);
                                clearValues();
                                isSyncing = false;
                                mBtnSync.setProgress(0);
                                mBtnSync.setEnabled(true);
                                mTvSendLog.setEnabled(false);
                                mHorizontalProgressBar.setProgress(0);

                                toggleMenu();
                            }
                        }
                    }).show();
                } else {
                    toggleMenu();
                }
                break;
            case R.id.tvHistory:
                if (isTablet()) {
                    replaceFragmentTablet(R.id.llRootMainTablet, SyncHistoryFragment.class, null, FLAG_ADD_STACK);
                } else {
                    replaceFragment(R.id.fragmentMain, SyncHistoryFragment.class, null, FLAG_ADD_STACK);
                }
                break;
            default:
                break;
        }
    }

    public void killSyncV4AsyncTask() {
        if (mSyncTask != null) {
            mSyncTask.cancel(true);
        }
    }

    @OnLongClick(R.id.textTitleSyncLog)
    public boolean onLongClick() {
        boolean isDebugMode = true;
        updateStatus("Debug mode is enabled!", Color.BLACK, 0, true);
        return true;
    }

    *//*
*/
/**
     * Method to start sync process
     *//*
*/
/*
    private void synchronize() {
        //if (!TextUtils.isEmpty(syncID)) {
            if (!isHavingNetwork()) {
                MccDialog.getOkAlertDialog(mActivity, R.string.no_internet_connection).show();
                return;
            }
        *//*
*/
/*} else {
            MccDialog.getOkAlertDialog(mActivity, R.string.error_sync_id_invalid_sync_screen).show();
            return;
        }*//*
*/
/*
        mTvSyncLog.setText(MCCPilotLogConst.STRING_EMPTY);
        clearValues();
        syncData.isCalcel = false;
        mBtnSync.setProgress(1);
        mBtnSync.setEnabled(false);
        mTvSendLog.setEnabled(false);
//        mIbMenu.setEnabled(false);
        mTvHistory.setEnabled(false);

        isSyncing = true;
        mSyncTask = new SynchronizeTask() {

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                onPostExecute(null);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                isSyncing = false;
                mBtnSync.setProgress(0);
                mBtnSync.setEnabled(true);
                mTvSendLog.setEnabled(true);
//                mIbMenu.setEnabled(true);
                mTvHistory.setEnabled(true);
            }
        };
        mSyncTask.execute();
    }

    @Override
    public void onKeyBackPress() {
        if (isSyncing) {
            MccDialog.getOkCancelAlertDialog(mActivity, R.string.confirm_interrupt_sync, R.string.interrupt_sync_message, new MccDialog.MCCDialogCallBack() {
                @Override
                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                    if (pDialogType == Dialog.BUTTON_POSITIVE && mSyncTask != null && mSyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        mSyncTask.cancel(true);
                        syncData.isCalcel = true;
                        mActivity.setSyncing(false);
                        clearValues();
                        isSyncing = false;
                        mBtnSync.setProgress(0);
                        mBtnSync.setEnabled(true);
                        mTvSendLog.setEnabled(false);
                        mHorizontalProgressBar.setProgress(0);
                    }
                }
            }).show();
        } else {
            super.onKeyBackPress();
//            mActivity.finish();
        }
    }

    *//*
*/
/**
     * Scroll sync log text view to the end
     *//*
*/
/*
    private void scrollSyncLog() {
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    *//*
*/
/**
     * Update sync status and completion percentage
     *
     * @param text     text to be displayed
     * @param color    color of text
     * @param progress progress percentage
     *//*
*/
/*
    private void updateStatus(final String text, final int color, final int progress, final String syncStatus, final boolean isBold) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!TextUtils.isEmpty(syncStatus)) {
                    mTvSyncStatusV4.setText(syncStatus);
                    mTvSyncStatusV4.setTextColor(mBlueColor);
                }

                if (color == Color.BLACK || color == GRAY || color == CYAN) {
                    mTvSyncStatusV4.setTextColor(Color.BLACK);

                } else if (color == Color.parseColor(MCCPilotLogConst.COLOR_DARK_GREEN)) {
                    mTvSyncStatusV4.setText(text);
                    mTvSyncStatusV4.setTextColor(color);

                } else if (color == Color.RED) {
                    mTvSyncStatusV4.setText("ERROR");
                    mTvSyncStatusV4.setTextColor(color);
                }
                if (!TextUtils.isEmpty(text)) {
                    if (mTvSyncLog.getText().toString().equalsIgnoreCase("")) {
                        Utils.appendColoredText(mTvSyncLog, text, color, isBold);
                    } else {
                        Utils.appendColoredText(mTvSyncLog, "\n" + text, color, isBold);
                    }
                }

                if (progress >= 0) {
                    mHorizontalProgressBar.setProgress(progress);
                }
                scrollSyncLog();
            }
        });
    }

    private void updateStatus(final String text, final int color, final int progress, final boolean isBold) {
        updateStatus(text, color, progress, "", isBold);
    }

    class SynchronizeTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            FTPClient ftp = new FTPClient();
            SyncV4Functions functions = new SyncV4Functions(mActivity);
            sendStatusMessage(SyncStatusV4.CONNECT);
            String connectToServer = DownloadAsync.downloadString(functions.listXML());
            String rest = MCCPilotLogConst.STRING_EMPTY;

            //write file log
            String time = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            LogUtils.deleteLogFile();
            LogUtils.writeLogToFile(time);
            LogUtils.writeLogToFile(connectToServer);
            LogUtils.writeLogToFile("\n\nContent of sync log:\nConnecting to mccCLOUD");

            int index = 0;
            try {
                if (TextUtils.isEmpty(connectToServer)) {
                    sendStatusMessage(SyncStatusV4.ERROR);
                    return null;
                }
                if (!connectToServer.contains("TOTAL")) {
                    if (connectToServer.contains("wifi") || connectToServer.contains("login") || connectToServer.contains("access") || connectToServer.contains("key")) {
                        sendStatusMessage(SyncStatusV4.NO_CONNECTION);
                        return null;
                    } else if (connectToServer.contains("NO USER DIR")) {
//                        MccDialog.getOkAlertDialog(mActivity, "Unable to find sync directory. Please contact HelpDesk for assistance").show();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MccDialog.getOkAlertDialog(mActivity, "Unable to find sync directory. Please contact HelpDesk for assistance").show();
                            }
                        });
                        sendStatusMessage(SyncStatusV4.ERROR);
                        return null;
                    } else if (connectToServer.contains("INVALID")) {
//                        MccDialog.getOkAlertDialog(mActivity, "Unable to connect to mccCLOUD. Verify your device clock Date!" +
//                                "If the problem persist, please contact HelpDesk for assistance").show();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MccDialog.getOkAlertDialog(mActivity, "Unable to connect to mccCLOUD. Verify your device clock Date!" +
                                        "If the problem persist, please contact HelpDesk for assistance").show();
                            }
                        });
                        sendStatusMessage(SyncStatusV4.ERROR);
                        return null;
                    } else {
                        sendStatusMessage(SyncStatusV4.SLOW_CONNECTION);
                        return null;
                    }
                }
                index = connectToServer.lastIndexOf("[END]");
                String rpl = connectToServer.substring(index);
                connectToServer = connectToServer.replace(rpl, MCCPilotLogConst.STRING_EMPTY);
                connectToServer = connectToServer.replace(MCCPilotLogConst.TAB_CHARACTER, MCCPilotLogConst.STRING_EMPTY);

                index = connectToServer.lastIndexOf("[LIST]");
                rest = connectToServer.substring(index + 6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            }
            //SyncData4 syncData = new SyncData4(mActivity, SyncV4Fragment.this);
            if (rest.length() > 0 && connectToServer != null && connectToServer.length() > (index + 7)) {
                connectToServer = connectToServer.substring(index + 6);
                connectToServer = connectToServer.replace("<br />", "##");
                String[] fileArray = connectToServer.split("##");

                arrFileNames.clear();
                int curNumb = 0;
                for (String fileName : fileArray) {
                    if (!TextUtils.isEmpty(fileName) && !fileName.equals(MCCPilotLogConst.CARRIAGE_CHARACTER + MCCPilotLogConst.NEW_LINE_CHARACTER)) {
                        deleteAllCurrencies();
                        String name = fileName.replace(MCCPilotLogConst.CARRIAGE_CHARACTER + MCCPilotLogConst.NEW_LINE_CHARACTER, MCCPilotLogConst.STRING_EMPTY);
                        name = name.replace(MCCPilotLogConst.NEW_LINE_CHARACTER, MCCPilotLogConst.STRING_EMPTY);
                        curNumb++;
                        CurrentFileName cfName = new CurrentFileName();
                        cfName.fileName = name;
                        cfName.fileNumber = curNumb;
                        arrFileNames.add(cfName);

                    }
                }
                mFoundFileNumber = curNumb;
                intCurrencies = 0;
                sendStatusMessage(SyncStatusV4.CONNECT_SUCCESS);
                if (isCancelled()) {
                    return null;
                }
//                syncData.syncFromPC(SyncData4.test);
                for (CurrentFileName currFileName : arrFileNames) {
                    try {
                        String moveFile = DownloadAsync.downloadString(functions.getFile(currFileName.fileName));
                        if (moveFile.isEmpty() || moveFile.contains("NO USER DIR") || moveFile.contains("UNKNOWN FILE") || moveFile.contains("FILE NOT FOUND")
                                || moveFile.contains("FAILED") || moveFile.contains("INVALID")) {
                            sendStatusMessage(SyncStatusV4.FAILED_TO_MOVE_FILE, currFileName.fileName);
                            continue;
                        }
                        boolean parseOK = syncData.syncFromPC(currFileName);
                        if (parseOK || (iStatus == SyncStatusV4.XML_TOO_OLD)) {
                            String deleteFile = DownloadAsync.downloadString(functions.deleteXML(currFileName.fileName));
                            if (deleteFile != null && !deleteFile.contains("DONE")) {
                                sendStatusMessage(SyncStatusV4.FAILED_TO_DELETE_FILE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isCancelled()) {
                        return null;
                    }
                } //end for
            } else {
                sendStatusMessage(SyncStatusV4.CONNECT_SUCCESS);
                if (isCancelled()) {
                    return null;
                }
            }

            *//*
*/
/*Download pictures *//*
*/
/*
            if (intPicture > 0) {
                ArrayList<String> downloadPictureList = syncData.getDownloadPictureList();
                for (final String picture : downloadPictureList) {
                    try {
                        sendStatusMessage(SyncStatusV4.DOWNLOAD_PICTURE, picture);
                        String getFile = DownloadAsync.downloadString(functions.getFile(picture));
                        if (!getFile.contains("FILE READY")) {
                            sendStatusMessage(SyncStatusV4.FAILED_TO_MOVE_FILE, picture);
                            continue;
                        }
                        String pilotPicture = String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, getPilotCodeFromZipFile(picture));
                        final File f = new File(StorageUtils.getStorageRootFolder(mActivity), pilotPicture);
                        if (f.exists()) {
                            f.delete();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DownloadAsync.downloadFile(SyncConst.OUTBOX_V4 + picture, f.getAbsolutePath());
                                StorageUtils.unzip(StorageUtils.getStorageRootFolder(mActivity), picture);
                                File zipFile = new File(StorageUtils.getStorageRootFolder(mActivity), picture);
                                zipFile.delete();
                                mCountDownLatch.countDown();
                            }
                        }).run();
                        mCountDownLatch = new CountDownLatch(1);
                        mCountDownLatch.await();
                        DownloadAsync.downloadString(functions.clearFile(picture));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendStatusMessage(SyncStatusV4.FAILED_TO_DOWNLOAD_FILE, picture);
                    }
                    if (isCancelled()) {
                        return null;
                    }
                }
            }

            *//*
*/
/**
             * Upload Part
             *//*
*/
/*
            if (isHavingNetwork()) {
                *//*
*/
/*get and set table ID *//*
*/
/*
                sendStatusMessage(SyncStatusV4.CREATE_TABLE_ID);
                if (isCancelled()) {
                    return null;
                }
                final String urlGetTablesID = functions.getTablesID();
                String tableID = DownloadAsync.downloadString(urlGetTablesID);
                if (tableID == null || tableID.contains("NO USER DIR")) {
                    sendStatusMessage(SyncStatusV4.FAILED_TO_GET_TABLE_ID);
                    return null;
                }

                int lastFlightCode = -1, lastPilotCode = -1;
                String[] PFCode = tableID.split("<br />");
                if (PFCode.length >= 3) {
                    if (PFCode[1].contains("FC") && PFCode[1].contains("="))
                        lastFlightCode = Integer.parseInt(PFCode[1].split("=")[1]);
                    if (PFCode[2].contains("PC") && PFCode[2].contains("="))
                        lastPilotCode = Integer.parseInt(PFCode[2].split("=")[1].replace(MCCPilotLogConst.NEW_LINE_CHARACTER, STRING_EMPTY));
                }

                //call getTableID- make sure ASP Checksum is correct- verify FC and PC are above last used values (stored locally)
                //(make sure app does not crash on first run, when these values are not there yet)
                int lastPilotCodeLocal = StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.SYNC_LAST_PILOT_CODE_KEY, -1);
                int lastFlightCodeLocal = StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.SYNC_LAST_Flight_CODE_KEY, -1);

                //If FC or PC value is below locally stored values, then do following
                if (lastFlightCode < lastFlightCodeLocal || lastPilotCode < lastPilotCodeLocal) {
                    //drop message in Sync Log
                    String errorMessage = "Error in TableID" +
                            "\n       FC/PC (local) :  " + lastFlightCodeLocal + "/" + lastPilotCodeLocal +
                            "\n       FC/PC (server) :  " + lastFlightCode + "/" + lastPilotCode;
                    sendStatusMessage(SyncStatusV4.ERROR_IN_TABLE_ID, errorMessage);
                    writeTableIdFile(tableID, true);
                    //display a pop-up message
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MccDialog.getOkAlertDialog(mActivity, R.string.error_sync_in_tablet_title, R.string.error_sync_in_tablet_content, new MccDialog.MCCDialogCallBack() {
                                @Override
                                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                    //immediately open an email
                                    mailErrorInTabletId(urlGetTablesID, true);
                                }
                            }).show();
                        }
                    });

                    return null;
                }

                if (lastPilotCode != -1) {
                    lastPilotCode = SyncViewModel.updateLatestPilotCode(mActivity, lastPilotCode);
                }

                if (lastFlightCode != -1) {
                    lastFlightCode = SyncViewModel.updateLatestFlightCode(mActivity, lastFlightCode, syncIncompleteFlights);
                }
                if (isCancelled()) {
                    return null;
                }
                *//*
*/
/*Changed pilot picture's name *//*
*/
/*
                if (!SyncViewModel.changedPilotCode.isEmpty()) {

                    File[] localFiles = new File(StorageUtils.getStorageRootFolder(mActivity)).listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().startsWith("img.") && pathname.getName().endsWith(".jpg");
                        }
                    });
                    for (File f : localFiles) {
                        String _tempFileName;
                        //pilot picture
                        _tempFileName = f.getName().replace("img.", MCCPilotLogConst.STRING_EMPTY).replace(".jpg", MCCPilotLogConst.STRING_EMPTY);
                        if (SyncViewModel.changedPilotCode.containsKey(_tempFileName)) {
                            String newPictureName = "img." + SyncViewModel.changedPilotCode.get(_tempFileName) + ".jpg";
                            try {
                                f.renameTo(new File(StorageUtils.getStorageRootFolder(mActivity), newPictureName));
                            } catch (Exception ignored) {
                            }
                        }
                        if (isCancelled()) {
                            return null;
                        }
                    }
                }
                Pilot p;
                if (SyncViewModel.changedPilotCode.keySet() != null && !SyncViewModel.changedPilotCode.keySet().isEmpty()) {
                    Iterator<String> iterator = SyncViewModel.changedPilotCode.keySet().iterator();
                    while (iterator.hasNext()) {
                        String oldPilotCode = iterator.next();
                        p = DatabaseManager.getInstance(mActivity).getPilotByPilotCode(String.valueOf(SyncViewModel.changedPilotCode.get(oldPilotCode)));
                        if (p != null) {
                            iterator.remove();
                        }
                    }
                }
                if (isCancelled()) {
                    return null;
                }
                sendStatusMessage(SyncStatusV4.CREATE_TABLE_ID_SUCCESS);

                sendStatusMessage(SyncStatusV4.UPDATE_TABLE_ID, " first time");
                if (isCancelled()) {
                    return null;
                }
                String setTableID = "";
                int redoIndex = 1;
                //call setTabletID- make sure ASP checksum is correct- if not, redo the call 3 times, with 1 second spacing,
                // until OK- alert helpdesk if not OK- store locally these FC and PC for next call verification
                final String urlSetTableID = functions.setTablesID(String.valueOf(lastFlightCode), String.valueOf(lastPilotCode));
                while (redoIndex <= 3 && !setTableID.contains("DONE")) {
                    if (redoIndex == 2) {
                        sendStatusMessage(SyncStatusV4.UPDATE_TABLE_ID, " second time");
                    }
                    if (redoIndex == 3) {
                        sendStatusMessage(SyncStatusV4.UPDATE_TABLE_ID, " third time");
                    }
                    setTableID = DownloadAsync.downloadString(urlSetTableID);
                    redoIndex++;
                    try {
                        if (!setTableID.contains("DONE") && redoIndex <= 3) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isCancelled()) {
                        return null;
                    }
                }
                if (!setTableID.contains("DONE")) {
                    StorageUtils.writeIntToSharedPref(mActivity, MCCPilotLogConst.SYNC_LAST_PILOT_CODE_KEY, lastPilotCode);
                    StorageUtils.writeIntToSharedPref(mActivity, MCCPilotLogConst.SYNC_LAST_Flight_CODE_KEY, lastFlightCode);
                    //Drop message in Sync Log
                    sendStatusMessage(SyncStatusV4.ERROR_SET_TABLE_ID);
                    writeTableIdFile(setTableID, false);
                    //display a pop-up message
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MccDialog.getOkAlertDialog(mActivity, R.string.error_sync_in_tablet_title, R.string.error_sync_in_tablet_content, new MccDialog.MCCDialogCallBack() {
                                @Override
                                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                    //immediately open an email
                                    mailErrorInTabletId(urlSetTableID, false);
                                }
                            }).show();
                        }
                    });
                } else {
                    sendStatusMessage(SyncStatusV4.SET_TABLE_ID_SUCCESS);
                }
                if (isCancelled()) {
                    return null;
                }
                sendStatusMessage(SyncStatusV4.CREATE_XML_FILE);
                ArrayList<File> imgFiles;
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
                final String dateTime = sdf1.format(Calendar.getInstance().getTime());
                String fileName = "mobile." + "syncID" + "." + dateTime + ".xml";

                try {
                    int number;
                    int lastLogFile = StorageUtils.getIntFromSharedPref(mActivity, StateKey.LAST_SYNC_LOG_FILE, 0);
                    if (lastLogFile < 20) {// maximum 20 files
                        number = lastLogFile + 1;
                    } else {
                        number = 1;
                    }

                    List<String> pictureList = SyncViewModel.getPictureList(mActivity);
                    sendStatusMessage(SyncStatusV4.CREATEING_XML_FILE_DONE);
                    if (syncData.createUploadFile(mActivity, fileName, number, syncIncompleteFlights, pictureList)) {
                        sendStatusMessage(SyncStatusV4.LOGIN_TO_CLOUD);
                        ftp = new FTPClient();
                        ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                        ftp.enterLocalPassiveMode();
                        if (!ftp.login(FTP_USER_NAME, FTP_PASSWORD)) {
                            ftp.logout();
                            sendStatusMessage(SyncStatusV4.FAILED_TO_LOG_IN_TO_CLOUD);
                        }
                        int replyCode = ftp.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(replyCode)) {
                            ftp.disconnect();
                            sendStatusMessage(SyncStatusV4.FAILED_TO_LOG_IN_TO_CLOUD);
                            return null;
                        }
                        sendStatusMessage(SyncStatusV4.LOGIN_TO_CLOUD_SUCCESS);
                        if (isCancelled()) {
                            return null;
                        }
                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        File[] xmlFiles = new File(StorageUtils.getStorageRootFolder(mActivity)).listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                return pathname.getName().endsWith(".xml");
                            }
                        });
                        sendStatusMessage(SyncStatusV4.UPLOAD_XML);
                        for (File f : xmlFiles) {
                            FileInputStream inStream = new FileInputStream(f.getAbsolutePath());
                            ftp.storeFile(f.getName(), inStream);
                            f.delete();
                            if (isCancelled()) {
                                return null;
                            }
                        }
                        try {
                            saveHistoryToFile(fileName);
                        } catch (Exception ignored) {
                        }

                        String storeFile = DownloadAsync.downloadString(functions.storeFile(fileName));
                        if (!storeFile.contains("STORED")) {
                            sendStatusMessage(SyncStatusV4.FAILED_TO_STORE_FILE, storeFile.replace("<br />", "\n"));
                        }
                        sendStatusMessage(SyncStatusV4.UPLOAD_XML_SUCCESS);
                    } else {
                        sendStatusMessage(SyncStatusV4.NO_RECORD_XML);
                    }
                    if (isCancelled()) {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendStatusMessage(SyncStatusV4.ERROR_CREATING_XML_FILE);
                    return null;
                } finally {
                    try {
                        ftp.logout();
                        ftp.disconnect();
                    } catch (IOException ignored) {
                    }

                }
                if (isCancelled()) {
                    return null;
                }
            *//*
*/
/*Upload picture *//*
*/
/*
                imgFiles = syncData.getUploadPictureList();
                String currentPicture;
                try {
                    if (imgFiles != null && !imgFiles.isEmpty()) {
                        sendStatusMessage(SyncStatusV4.UPLOAD_PILOT_PICTURE);
                        ftp = new FTPClient();
                        ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                        ftp.enterLocalPassiveMode();
                        if (!ftp.login(FTP_USER_NAME, FTP_PASSWORD)) {
                            ftp.logout();
                            sendStatusMessage(SyncStatusV4.ERROR);
                            return null;
                        }
                        int replyCode = ftp.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(replyCode)) {
                            ftp.disconnect();
                            sendStatusMessage(SyncStatusV4.ERROR);
                            return null;
                        }

                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                        for (int i = 0; i < imgFiles.size(); i++) {
                            File currentFile = imgFiles.get(i);
                            currentPicture = currentFile.getName();
                            sendStatusMessage(SyncStatusV4.UPLOADING_PILOT_PICTURE, currentPicture);
                            try {
                                if (!TextUtils.isEmpty(currentFile.getAbsolutePath())) {
                                    FileInputStream inStream = new FileInputStream(currentFile);
                                    ftp.storeFile(currentPicture, inStream);
                                    inStream.close();

                                    String storeFile = DownloadAsync.downloadString(functions.storeFile(currentPicture));
                                    if (!storeFile.contains("STORED")) {
                                        sendStatusMessage(SyncStatusV4.FAILED_TO_STORE_FILE, currentPicture + "\n" + storeFile.replace("<br />", "\n"));
                                        continue;
                                    }
                                    if (isCancelled()) {
                                        return null;
                                    }
                                    updateSyncStatus(currentPicture);
                                    if (currentPicture.startsWith("img")) {
                                        String imgName = String.format("img.%s.jpg", getSignatureCodeFromZipFile(currentPicture));
                                        File sign = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SIGN_FOLDER, imgName);
                                        StorageUtils.copyFile(sign, new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator +
                                                MCCPilotLogConst.SYNCED_SIGN_FOLDER, imgName));
                                        sign.delete();
                                        currentFile.delete();
                                    }
                                    try {
                                        saveHistoryToFile(currentPicture);
                                    } catch (Exception ignored) {
                                    }
                                    sendStatusMessage(SyncStatusV4.UPLOAD_PICTURE_SUCCESS);
                                    if (isCancelled()) {
                                        return null;
                                    }
                                }
                                System.gc();
                            } catch (Exception e) {
                                e.printStackTrace();
                                sendStatusMessage(SyncStatusV4.FAILED_TO_UPLOAD_PICTURE);
                            }
                            if (isCancelled()) {
                                return null;
                            }
                        }
                    }
                    updateDb();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendStatusMessage(SyncStatusV4.ERROR);
                }

                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException ignored) {
                }
                if (isCancelled()) {
                    return null;
                }
//                currentFile = 0;
//                currentPicture = MCCPilotLogConst.STRING_EMPTY;
            } else {
                sendStatusMessage(SyncStatusV4.LOST_CONNECTION);
            }
            sendStatusMessage(SyncStatusV4.SYNC_COMPLETE);
            System.gc();
            return null;
        }
    }

    private void updateDb() {
        SyncViewModel.updateDbAfterSync(mActivity, syncIncompleteFlights);
        SyncViewModel.deleteSyncedFlights(mActivity, syncIncompleteFlights);
    }

    private void updateSyncStatus(String pFileName) {
        if (!pFileName.contains("pict")) {
            return;
        }
        Pilot pilot = DatabaseManager.getInstance(mActivity).getPilotByPilotCode(getPilotCodeFromZipFile(pFileName));
        if (pilot != null) {
            //pilot.setSyncPict(SyncConst.SYNC_PICT_NO);
            pilot.setPC(SyncConst.PC);
            DatabaseManager.getInstance(mActivity).updatePilot(pilot);
        }
        new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.PICTURE_FOLDER, pFileName).delete();
    }

    private String getPilotCodeFromZipFile(String zipFile) {
        String pilotCode = zipFile.replace("img.", STRING_EMPTY);
        pilotCode = pilotCode.replace(".zip", STRING_EMPTY);
        pilotCode = pilotCode.split("\\.")[1];
        return pilotCode;
    }

    private String getSignatureCodeFromZipFile(String zipFile) {
        String signatureCode = zipFile.replace("img.", STRING_EMPTY);
        signatureCode = signatureCode.replace(".zip", STRING_EMPTY);
        signatureCode = signatureCode.split("\\.")[1];
        return signatureCode;
    }

    *//*
*/
/**
     * Reset counter variables
     *//*
*/
/*
    private void clearValues() {
        intPilots = 0;
        intAirfields = 0;
        intAircrafts = 0;
        intFlights = 0;
        intICal = 0;
        intPilots = 0;
        intCurrencies = 0;
        intLogbook = 0;
        intPicture = 0;
        mTvSyncLog.setText(MCCPilotLogConst.STRING_EMPTY);
        mTvSyncStatusV4.setText(MCCPilotLogConst.STRING_EMPTY);
        isError = false;
        iStatus = -1;
    }

    public void sendStatusMessage(int pStatusMessage, CurrentFileName pCurrentFile, String pAdditionalInfo) {
        String text = STRING_EMPTY;
        switch (pStatusMessage) {
            case SyncStatusV4.CONNECT:
                text = "Connect to mccCLOUD...";
                updateStatus(text, Color.BLACK, 0, "Connect", true);
                break;
            case SyncStatusV4.NO_CONNECTION:
                isError = true;
                text = "FAILED\nVerify your internet connection(login key)\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.LOST_CONNECTION:
                isError = true;
                text = "ERROR\nConnection was lost\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.SLOW_CONNECTION:
                isError = true;
                text = "FAILED\nNo connection with mccCLOUD\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.CONNECT_SUCCESS:
                if (mFoundFileNumber == 0) {
                    text = "SUCCESS\n";
                    updateStatus(text, CYAN, 10, false);
                    text = "No files for download\n";
                    updateStatus(text, CYAN, 10, false);
                } else {
                    text = "SUCCESS\n";
                    updateStatus(text, CYAN, 10, false);
                    text = "Found " + mFoundFileNumber + " file(s) on mccCLOUD\n";
                    updateStatus(text, CYAN, 10, false);
                }
                break;
            case SyncStatusV4.DOWNLOAD_FILE:
                text = "\nDownload file " + pCurrentFile.fileNumber + "";
                updateStatus(text, Color.BLACK, 20, "Download Files", true);
                break;
            case SyncStatusV4.FAILED_TO_MOVE_FILE:
                isError = true;
                text = "FAILED (ASP)\n" + pAdditionalInfo + "\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.FAILED_TO_DOWNLOAD_FILE:
                isError = true;
                text = "FAILED " + pAdditionalInfo + "\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.DOWNLOAD_FILE_SUCCESS:
                text = "SUCCESS\n";
                updateStatus(text, CYAN, 30, true);
                break;
            case SyncStatusV4.FAILED_TO_DELETE_FILE:
                isError = true;
                text = "ERROR while deleting file " + pCurrentFile.fileNumber + "\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.NEED_UPDATE:
                text = "XML file cannot be parsed - You must first install latest update for this app\n";
                updateStatus(text, Color.BLACK, 0, true);
                isError = true;
                break;
            case SyncStatusV4.XML_TOO_OLD:
                text = "XML file too old for parsing\n";
                updateStatus(text, Color.BLACK, 0, true);
                isError = true;
                break;
            case SyncStatusV4.PROCESS_FILE:
                text = "Process file " + pCurrentFile.fileNumber;
                updateStatus(text, Color.BLACK, 25, true);
                break;
            case SyncStatusV4.ERROR_PARSING_XML:
                text = "ERROR: Parsing XML File\n";
                isError = true;
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatusV4.DOWNLOAD_PICTURE_SUCCESS:
                text = "SUCCESS\n";
                updateStatus(text, CYAN, 40, true);
                break;
            case SyncStatusV4.DOWNLOAD_PICTURE:
                text = "Download picture " + pAdditionalInfo;
                updateStatus(text, Color.BLACK, 35, true);
                break;
            case SyncStatusV4.CREATE_TABLE_ID:
                text = "\nCreate Table ID";
                updateStatus(text, Color.BLACK, 45, "Prepare Upload", true);
                break;
            case SyncStatusV4.FAILED_TO_GET_TABLE_ID:
                text = "ERROR\n";
                isError = true;
                updateStatus(text, Color.RED, 100, false);
                break;
            case SyncStatusV4.CREATE_TABLE_ID_SUCCESS:
                text = "DONE\n";
                updateStatus(text, CYAN, 50, false);
                break;
            case SyncStatusV4.SET_TABLE_ID_SUCCESS:
                text = "DONE\n";
                updateStatus(text, CYAN, 50, false);
                break;
            case SyncStatusV4.ERROR_IN_TABLE_ID:
                text = pAdditionalInfo;
                isError = true;
                updateStatus(text, Color.RED, 100, false);
                break;
            case SyncStatusV4.UPDATE_TABLE_ID:
                text = "Update TableID " + pAdditionalInfo;
                updateStatus(text, Color.BLACK, 45, "Prepare Upload", true);
                break;
            case SyncStatusV4.ERROR_SET_TABLE_ID:
                text = "   >> ERROR \n" +
                        "   >> CS invalid\n";
                isError = true;
                updateStatus(text, Color.RED, 55, false);
                break;
            case SyncStatusV4.CREATE_XML_FILE:
                text = "Create XML file";
                updateStatus(text, Color.BLACK, 60, true);
                break;
            case SyncStatusV4.ERROR:
                text = "ERROR\n";
                isError = true;
                updateStatus(text, Color.RED, 100, false);
                break;
            case SyncStatusV4.LOGIN_TO_CLOUD:
                text = "Login to mccCLOUD";
                updateStatus(text, Color.BLACK, 65, "Upload Files", true);
                break;
            case SyncStatusV4.LOGIN_TO_CLOUD_SUCCESS:
                text = "CONNECTED\n";
                updateStatus(text, Color.BLACK, 70, true);
                break;
            case SyncStatusV4.FAILED_TO_LOG_IN_TO_CLOUD:
                text = "FAILED\n";
                updateStatus(text, Color.RED, 100, false);
                isError = true;
                break;
            case SyncStatusV4.UPLOAD_XML:
                text = "Upload XML file";
                updateStatus(text, Color.BLACK, 75, true);
                break;
            case SyncStatusV4.FAILED_TO_UPLOAD_XML:
                text = "FAILED\n";
                updateStatus(text, Color.RED, 75, false);
                isError = true;
                break;
            case SyncStatusV4.UPLOAD_XML_SUCCESS:
                text = "SUCCESS\n";
                updateStatus(text, CYAN, 80, true);
                break;
            case SyncStatusV4.FAILED_TO_STORE_FILE:
                text = "ERROR while storing file " + pAdditionalInfo + "\n";
                updateStatus(text, Color.RED, 80, false);
                isError = true;
                break;
            case SyncStatusV4.UPLOAD_PILOT_PICTURE:
                text = "Upload Pilot Picture";
                updateStatus(text, Color.BLACK, 85, true);
                break;
            case SyncStatusV4.UPLOADING_PILOT_PICTURE:
                text = "Uploading " + pAdditionalInfo + " ...";
                updateStatus(text, Color.BLACK, 87, true);
                break;
            case SyncStatusV4.UPLOAD_PICTURE_SUCCESS:
                text = "SUCCESS\n";
                updateStatus(text, CYAN, 90, true);
                break;
            case SyncStatusV4.FAILED_TO_UPLOAD_PICTURE:
                text = "ERROR during upload " + pAdditionalInfo + "\n";
                updateStatus(text, Color.RED, 87, false);
                isError = true;
                break;

            case SyncStatusV4.SYNC_COMPLETE:
                setRecordsFromPC();
                text = "SYNC COMPLETED";
                if (!isError) {
                    updateStatus(text, Color.parseColor(MCCPilotLogConst.COLOR_DARK_GREEN), 100, text, true);
                } else {
                    updateStatus(text, Color.RED, 100, false);
                }
                break;

            case SyncStatusV4.NO_RECORD_XML:
                text = "No data to upload\n";
                updateStatus(text, CYAN, 65, false);
                break;

            //processing detail
            case SyncStatusV4.CLEANING_RECORD:
                text = "\tCleaning Records";
                updateStatus(text, CYAN, 26, false);
                break;
            case SyncStatusV4.PROCESSING_AIRCRAFTS:
                text = "\tProcessing Aircraft";
                updateStatus(text, GRAY, 27, false);
                break;
            case SyncStatusV4.PROCESSING_AIRFIELDS:
                text = "\tProcessing Airfields";
                updateStatus(text, GRAY, 28, false);
                break;
            case SyncStatusV4.PROCESSING_PILOT:
                text = "\tProcessing Pilots";
                updateStatus(text, GRAY, 29, false);
                break;
            case SyncStatusV4.PROCESSING_FLIGHTS:
                text = "\tProcessing Flights";
                updateStatus(text, GRAY, 30, false);
                break;
            case SyncStatusV4.PROCESSING_LOGBOOK_FLIGHTS:
                text = "\tProcessing Logbook Flights";
                updateStatus(text, GRAY, 31, false);
                break;
            case SyncStatusV4.UPDATING_TOTALS:
                text = "\tUpdating Totals";
                updateStatus(text, GRAY, 32, false);
                break;
            case SyncStatusV4.UPDATING_GRAND_TOTALS:
                text = "\tUpdating Grand Totals";
                updateStatus(text, GRAY, 33, false);
                break;
            case SyncStatusV4.PROCESSING_CURRENCIES:
                text = "\tProcessing Currencies";
                updateStatus(text, GRAY, 34, false);
                break;
            case SyncStatusV4.PROCESSING_ICAL:
                text = "\tProcessing iCal data";
                updateStatus(text, GRAY, 35, false);
                break;
            case SyncStatusV4.ERROR_CALENDAR_NAME:
                String defCal = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.DEFAULT_CALENDAR).getData();
                text = "Export to Calendar \n Calendar " + defCal + " does no longer exist";
                updateStatus(text, Color.BLACK, 84, false);
                break;
            case SyncStatusV4.ERROR_CALENDAR_NO_NAME:
                if (mNoCalendarDialog != null && !mNoCalendarDialog.isShowing()) {
                    //2015-12-08 TuanPV begin edit
//                    mNoCalendarDialog.show();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNoCalendarDialog.show();
                        }
                    });
                    //2015-12-08 TuanPV end edit
                }
                //PL-254,507
            case SyncStatusV4.ERROR_FILE_DAMAGED:
                text = "FAILED \nCannot download XML file. File appears to be damaged.\n"+ pCurrentFile.fileName + " (deleted)";
                updateStatus(text, Color.RED, 0, false);
                break;
            //End PL-,507
            case SyncStatusV4.CREATEING_XML_FILE_DONE:
                text = "DONE\n";
                updateStatus(text, CYAN, 60, false);
                break;
            default:
                break;
        }

        if (pStatusMessage != SyncStatusV4.CONNECT) {
            LogUtils.writeLogToFile(text);
        }
    }

    public void sendStatusMessage(int pMessage) {
        sendStatusMessage(pMessage, null, "");
    }

    public void sendStatusMessage(int pMessage, String additionalInfo) {
        sendStatusMessage(pMessage, null, additionalInfo);
    }

    public void sendStatusMessage(int pMessage, CurrentFileName pCurrentFileName) {
        sendStatusMessage(pMessage, pCurrentFileName, "");
    }

    public class CurrentFileName {
        public String fileName;
        public int fileNumber;
    }

    private void setRecordsFromPC() {
        final String text = "\n\nRecords from PC" +
                "\n       Pilots: " + Integer.toString(intPilots) +
                "\n       Aircraft: " + Integer.toString(intAircrafts) +
                "\n       Airfields: " + Integer.toString(intAirfields) +
                "\n       Flights: " + Integer.toString(intFlights) +
                "\n       iCal: " + Integer.toString(intICal) +
                "\n       Logbook: " + Integer.toString(intLogbook) +
                "\n       Currencies: " + Integer.toString(intCurrencies) +
                "\n\n";
        LogUtils.writeLogToFile(text);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.appendColoredText(mTvSyncLog, text, CYAN, false);
            }
        });
    }

    private static synchronized void writeTableIdFile(final String log, boolean isGetTableID) {
        final File logFile = new File(StorageUtils.getStorageRootFolder(MCCApplication.getInstance()), isGetTableID ? GET_TABLE_ID_FILE : SET_TABLE_ID_FILE);
        if (logFile.exists()) {
            logFile.delete();
        }
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            pw.println(log);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }

    private void mailErrorInTabletId(String pUrlGetTablesID, boolean isGetTableID) {
       // String syncId = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
        pUrlGetTablesID = pUrlGetTablesID.replace("http://www.mccCLOUD.net/", "");
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{MCCPilotLogConst.MAIL_TO});
        intent.putExtra(Intent.EXTRA_SUBJECT, (isGetTableID ? " Error in getTableID" : " Error in setTableID"));
        String lineSep = System.getProperty(MCCPilotLogConst.TEXT_LINE_SEPARATOR);
        String header = MCCPilotLogConst.MCC_APP_NAME + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + BuildConfig.VERSION_NAME + "  (" + String.valueOf(BuildConfig.VERSION_CODE) + ")" + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE + "  (" + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + ")" + lineSep +
                MCCPilotLogConst.LINE_EMAIL_HEADER + lineSep + MCCPilotLogConst.WRITE_MESSAGE_HERE + lineSep + lineSep + lineSep;
        intent.putExtra(Intent.EXTRA_TEXT, "\nURL: " + pUrlGetTablesID + lineSep + lineSep + header);
        intent.setType(MCCPilotLogConst.INTENT_TYPE);
        Uri fileUri = null;
        String packageName = getApplication().getPackageName();

        File logFile = new File(StorageUtils.getStorageRootFolder(mActivity), isGetTableID ? GET_TABLE_ID_FILE : SET_TABLE_ID_FILE);
        if (logFile.exists() && logFile.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, logFile);
        }
        if (fileUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        }
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mActivity, "Sorry, There are no email clients installed on the device!");
        }
    }

    private void mailLog() {
        //String syncId = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{MCCPilotLogConst.MAIL_TO});
        intent.putExtra(Intent.EXTRA_SUBJECT, "mobile.log");
        String lineSep = System.getProperty(MCCPilotLogConst.TEXT_LINE_SEPARATOR);
        String header = MCCPilotLogConst.MCC_APP_NAME + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + BuildConfig.VERSION_NAME + "  (" + String.valueOf(BuildConfig.VERSION_CODE) + ")" + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE + "  (" + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + ")" + lineSep +
                MCCPilotLogConst.LINE_EMAIL_HEADER + lineSep + MCCPilotLogConst.WRITE_MESSAGE_HERE + lineSep + lineSep + lineSep;
        intent.putExtra(Intent.EXTRA_TEXT, header);
        intent.setType(MCCPilotLogConst.INTENT_TYPE);
        ArrayList<Uri> uris = new ArrayList<>();
        Uri fileUri;
        String packageName = getApplication().getPackageName();

        File xmlFolder = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.XML_FOLDER);
        File f = null;
        if (xmlFolder.exists()) {
            f = StorageUtils.compressFolder(mActivity, xmlFolder);
        }
        if (f != null && f.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, f);
            uris.add(fileUri);
        }

        File xmlPCFolder = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.XMLPC_FOLDER);
        File filePC = null;
        if (xmlPCFolder.exists()) {
            filePC = StorageUtils.compressFolder(mActivity, xmlPCFolder);
        }
        if (filePC != null && filePC.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, filePC);
            uris.add(fileUri);
        }

        //attach database: user database and logbook database
        File pilotFile = new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES + DatabaseManager.DATABASE_PILOT);
        if (pilotFile.exists()) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, pilotFile);
            uris.add(fileUri);
        }
        File logbookFile = new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES + DatabaseManager.DATABASE_LOG_BOOK);
        if (logbookFile.exists()) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, logbookFile);
            uris.add(fileUri);
        }

        File logFile = new File(StorageUtils.getStorageRootFolder(mActivity), LogUtils.LOG_FILE_NAME);
        if (logFile.exists() && logFile.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, logFile);
            uris.add(fileUri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mActivity, "Sorry, There are no email clients installed on the device!");
        }
    }

    public void deleteAllCurrencies() {
        DatabaseManager.getInstance(mActivity).deleteAllCurrencies();
    }

    public void saveHistoryToFile(String fileName) throws Exception {
        File syncHistoryFolder = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.SYNC_HISTORY_FOLDER);
        if (!syncHistoryFolder.exists()) {
            syncHistoryFolder.mkdir();
        }
        File syncHistoryXML = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNC_HISTORY_FOLDER, SyncHistoryFragment.SAVE_FILE_XML);
        File syncHistoryIMG = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNC_HISTORY_FOLDER, SyncHistoryFragment.SAVE_FILE_IMG);
        PrintWriter pw;
        String divider = "##";
        Calendar c = Calendar.getInstance();
        if (fileName.contains("xml") || fileName.contains("XML")) {
            if (!syncHistoryXML.exists()) {
                syncHistoryXML.createNewFile();
            }
            pw = new PrintWriter(new BufferedWriter(new FileWriter(syncHistoryXML, true)));
            pw.println(fileName + divider + c.getTimeInMillis());
            pw.flush();
            pw.close();
        } else {
            if (!syncHistoryIMG.exists()) {
                syncHistoryIMG.createNewFile();
            }

            pw = new PrintWriter(new BufferedWriter(new FileWriter(syncHistoryIMG, true)));
            pw.println(fileName + divider + c.getTimeInMillis());
            pw.flush();
            pw.close();
        }

    }

    public static final String FTP_USER_NAME = "mccUser_MOB";
    public static final String FTP_PASSWORD = "login%1967#MCC";

    *//*
*/
/**
     * update again width layout tablet when rotate screen.
     *//*
*/
/*
    private void updateWidthLayoutTablet() {
        if (mLeftBarFragment != null && mRightBarFragment != null && mLeftContentFragment != null && mRightContentFragment != null) {
            LinearLayout.LayoutParams lpLeft, lpRight;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            } else {
                lpLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lpRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            }
            mLeftBarFragment.setLayoutParams(lpLeft);
            mLeftContentFragment.setLayoutParams(lpLeft);
            mRightBarFragment.setLayoutParams(lpRight);
            mRightContentFragment.setLayoutParams(lpRight);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isTablet()) {
            updateWidthLayoutTablet();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*//*

}*/
