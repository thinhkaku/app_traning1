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
import net.mcc3si.sync.SyncData;
import net.mcc3si.sync.SyncStatus;
import net.mcc3si.sync.SyncViewModel;
import net.mcc3si.tasks.DownloadAsync;
import net.mcc3si.utilities.DatabaseUtils;
import net.mcc3si.utilities.LogUtils;
import net.mcc3si.utilities.NetworkUtils;
import net.mcc3si.utilities.StorageUtils;
import net.mcc3si.utilities.Utils;
import net.mcc3si.widgets.MccDialog;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;

*/
/**
 * Created by tuan.na on 8/17/2015.
 * Sync v3 screen and logic
 *//*

public class SyncV3Fragment extends BaseMCCFragment {

    @Bind(R.id.tv_send_log)
    TextView mTvSendLog;
    @Bind(R.id.btn_sync)
    ActionProcessButton mBtnSync;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tv_network)
    TextView mTvNetworkStatus;
    */
/*@Bind(R.id.tv_sync_id)
    TextView mTvSyncId;*//*

    @Bind(R.id.tv_sync_status)
    TextView mTvSyncStatus;
    @Bind(R.id.tv_sync_log)
    TextView mTvSyncLog;
    @Bind(R.id.progress_bar)
    ProgressBar mHorizontalProgressBar;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.sw_sync_incomplete_flights)
    Switch mSwSyncIncompleteFlights;
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
    @Bind(R.id.tv_version)
    TextView mTvVersion;

    public static final int CURRENT_XML_VERSION = 8;

    private boolean isSyncing = false;
    private boolean syncIncompleteFlights = false;
    public int mFoundFileNumber = 0, currentFile = 0, pcProgress;

    public int intPilots, intAircraft, intAirfields, intFlights, intICal, intLogbook, intCurrencies;
    private String currentPicture;
    private int currentPictNumber = 0;

    private boolean isError;
    //private String syncID = "";
    public float delta = 0;
    public int iStatus = 0;
    private AlertDialog mNoCalendarDialog;
    private boolean isDebugMode = true; //2016-01-04 Changed by TuanPV
    private SynchronizeTask mSyncTask;
    private List<CurrentFileName> arrFileNames = new ArrayList<>();
    private static String mNetworkType;
    private Thread mThread;
    private int sColorBlue, sColorRed;
    private int CYAN = Color.rgb(85, 142, 191);
    private int GRAY = Color.rgb(128, 128, 128);
    private SyncData syncData;

    @Override
    protected int getHeaderResId() {
        return isTablet() ? R.layout.layout_action_bar_sync_tablet : R.layout.layout_sync_action_bar;
    }

    @Override
    protected int getContentResId() {
        return isTablet() ? R.layout.fragment_sync_tablet : R.layout.fragment_sync3;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        sColorBlue = getResources().getColor(R.color.mcc_blue);
        sColorRed = getResources().getColor(android.R.color.holo_red_light);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    */
/**
     * Init view
     *//*

    private void initView() {
        syncData = new SyncData(mActivity, SyncV3Fragment.this);
        if (isTablet()) {
            updateWidthLayoutTablet();
        }
        mTvVersion.setText(R.string.sync_title_v37);
        mBtnSync.setMode(ActionProcessButton.Mode.ENDLESS);
       */
/* Setting setting = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting != null) {
            syncID = setting.getData();
        }*//*

        //mTvSyncId.setText(syncID);
        if (isHavingNetwork()) {
            mTvNetworkStatus.setText(NetworkUtils.getNetworkType(mActivity));
            mTvNetworkStatus.setTextColor(sColorBlue);
        } else {
            mTvNetworkStatus.setTextColor(sColorRed);
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
                    mTvNetworkStatus.setTextColor(sColorRed);
                } else {
                    mTvNetworkStatus.setTextColor(sColorBlue);
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

    @OnClick({R.id.btn_sync, R.id.tv_send_log, R.id.ibMenu})
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
                    MccDialog.getYesNoAlertDialog(mActivity, getString(R.string.message_interrupt_title), getString(R.string.message_interrupt_content), new MccDialog.MCCDialogCallBack() {
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
            default:
                break;
        }
    }

    public void killSyncV3AsyncTask() {
        if (mSyncTask != null) {
            mSyncTask.cancel(true);
        }
    }

    @OnLongClick(R.id.textTitleSyncLog)
    public boolean onLongClick() {
        isDebugMode = true;
        updateStatus("Debug mode is enabled!", Color.BLACK, 0, true);
        return true;
    }

    */
/**
     * Method to start sync process
     *//*

    private void synchronize() {
        //if (!TextUtils.isEmpty(syncID)) {
            if (!isHavingNetwork()) {
                MccDialog.getOkAlertDialog(mActivity, R.string.no_internet_connection).show();
                return;
            }
        */
/*} else {
            MccDialog.getOkAlertDialog(mActivity, R.string.error_sync_id_invalid_sync_screen).show();
            return;
        }*//*

        mTvSyncLog.setText(MCCPilotLogConst.STRING_EMPTY);
        clearValues();
        syncData.isCalcel = false;
        mBtnSync.setProgress(1);
        mBtnSync.setEnabled(false);
        mTvSendLog.setEnabled(false);
        isSyncing = true;
        mActivity.setSyncing(true);
        mSyncTask = new SynchronizeTask() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                scrollSyncLog();
                isSyncing = false;

                mActivity.setSyncing(false);
                mBtnSync.setProgress(0);
                mBtnSync.setEnabled(true);
                mTvSendLog.setEnabled(true);
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

    */
/**
     * Scroll sync log text view to the end
     *//*

    private void scrollSyncLog() {
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    */
/**
     * Update sync status and completion percentage
     *
     * @param text     text to be displayed
     * @param color    color of text
     * @param progress progress percentage
     * @param isBold   font style bold or not
     *//*

    private void updateStatus(final String text, final int color, final int progress, final boolean isBold) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (color == Color.BLACK || color == GRAY || color == CYAN) {

                    if (progress > 0) {
                        String file = "";
                        if (currentFile >= 0) {
                            String numb = "  ";
                            if (currentFile > 0)
                                numb = Integer.toString(currentFile);
                            file = "File: " + numb + "                                     ";

                        } else if (!currentPicture.equalsIgnoreCase(""))
                            file = "Picture: " + Integer.toString(currentPictNumber) +
                                    "       ";
                        mTvSyncStatus.setText(file
                                + Integer.toString(progress) + "%");

                    }
                    mTvSyncStatus.setTextColor(Color.BLACK);

                } else if (color == Color.parseColor(MCCPilotLogConst.COLOR_DARK_GREEN)) {
                    mTvSyncStatus.setText(text);
                    mTvSyncStatus.setTextColor(color);

                } else if (color == Color.RED) {
                    mTvSyncStatus.setText("ERROR");
                    mTvSyncStatus.setTextColor(color);
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


    class SynchronizeTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            FTPClient ftp = null;
            String time = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            String urlS = MCCPilotLogConst.SEARCH_LK + "syncID" + "&time=" + time;

            sendStatusMessage(SyncStatus.CONNECTING);
            String connectToServer = DownloadAsync.downloadString(urlS);
            if (isDebugMode) {
                LogUtils.deleteLogFile();
                LogUtils.writeLogToFile(time);
                LogUtils.writeLogToFile(connectToServer);
                LogUtils.writeLogToFile("\n\nContent of sync log:\nConnecting to mccCLOUD");
            }
            String rest = MCCPilotLogConst.STRING_EMPTY;
            int index = 0;
            try {
                if (!connectToServer.contains("TOTAL")) {
                    sendStatusMessage(SyncStatus.ERROR);
                    return null;
                }
                if (connectToServer.contains("javascript") || connectToServer.contains(".js") || connectToServer.contains("script") || connectToServer.contains("html") ||
                        connectToServer.contains("/>")) {
                    sendStatusMessage(SyncStatus.NO_INTERNET_ACCESS);
                    return null;
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
            */
/*SyncData syncData = new SyncData(mActivity, SyncV3Fragment.this);*//*

            if (rest.length() > 0 && connectToServer != null && connectToServer.length() > (index + 7)) {
                connectToServer = connectToServer.substring(index + 7);
                connectToServer = connectToServer.replace("|", "##");
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
                sendStatusMessage(SyncStatus.X_FILES_FOUND);
                if (isCancelled()) {
                    return null;
                }
                intCurrencies = 0;
                for (CurrentFileName currFileName : arrFileNames) {
                    try {
                        ftp = new FTPClient();
                        ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                        ftp.enterLocalPassiveMode();
                        if (!ftp.isConnected()) {
                            return null;
                        }
                        if (!ftp.login(ftpUserName(), ftpPassword())) {
                            ftp.logout();
                            sendStatusMessage(SyncStatus.FAILED);
                            return null;
                        }
                        int replyCode = ftp.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(replyCode)) {
                            ftp.disconnect();
                            sendStatusMessage(SyncStatus.FAILED);
                            return null;
                        }
                        currentFile = currFileName.fileNumber;
                        boolean parseOK = syncData.syncFromPC(currFileName, ftp);
                        if (parseOK || (iStatus == SyncStatus.XML_TOO_OLD)) {
                            try {
                                ftp.deleteFile(currFileName.fileName);
                            } catch (SocketException se) {
                                se.printStackTrace();
                                //When delete file get error then create new FTPClient again.
                                ftp = new FTPClient();
                                ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                                ftp.enterLocalPassiveMode();
                                if (ftp.isConnected() && ftp.login(ftpUserName(), ftpPassword()) && FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                                    ftp.deleteFile(currFileName.fileName);
                                }
                            }
                        }
                        if (!parseOK) {
                            sendStatusMessage(SyncStatus.ERROR_DATA_PROCESSING, currFileName);
                        }

                        ftp.logout();
                        ftp.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isCancelled()) {
                        return null;
                    }
                } //end for
            } else {
                sendStatusMessage(SyncStatus.X_FILES_FOUND);
                if (isCancelled()) {
                    return null;
                }
            }

            */
/*Create xml *//*

            ArrayList<String[]> imgFiles;
            String localFileName, uploadFileName;
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
            final String dateTime = sdf1.format(Calendar.getInstance().getTime());

            String fileName = "mobile." + "syncID" + "." + dateTime + ".xml";
            try {
                sendStatusMessage(SyncStatus.CREATING_XML_FILE);
                if (isCancelled()) {
                    return null;
                }
                int number;
                int lastLogFile = StorageUtils.getIntFromSharedPref(mActivity, StateKey.LAST_SYNC_LOG_FILE, 0);
                if (lastLogFile < 20) {// maximum 20 files
                    number = lastLogFile + 1;
                } else {
                    number = 1;
                }
                imgFiles = syncData.createUploadFile(mActivity, fileName, number, syncIncompleteFlights);
                sendStatusMessage(SyncStatus.CREATEING_XML_FILE_DONE);
            } catch (Exception e) {
                e.printStackTrace();
                sendStatusMessage(SyncStatus.ERROR_CREATING_XML_FILE);
                return null;
            }
            if (isCancelled()) {
                return null;
            }
            */
/*Uploading XML *//*

            try {
                sendStatusMessage(SyncStatus.UPLOADING_XML_FILE);
                if (isCancelled()) {
                    return null;
                }
                ftp = new FTPClient();
                ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                ftp.enterLocalPassiveMode();
                if (!ftp.login(ftpUserName(), ftpPassword())) {
                    ftp.logout();
                    sendStatusMessage(SyncStatus.FAILED_FTP_UPLOAD);
                }
                int replyCode = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    ftp.disconnect();
                    sendStatusMessage(SyncStatus.FAILED_FTP_UPLOAD);
                    return null;
                }

                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                File[] xmlFiles = new File(StorageUtils.getStorageRootFolder(mActivity)).listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".xml");
                    }
                });

                for (File f : xmlFiles) {
                    FileInputStream inStream = new FileInputStream(f.getAbsolutePath());
                    ftp.storeFile(f.getName(), inStream);
                    //sendStatusMessage(SyncStatus.SUCCESS);
                    f.delete();
                    if (isCancelled()) {
                        return null;
                    }
                }
                sendStatusMessage(SyncStatus.SUCCESS);

            } catch (IOException e) {
                e.printStackTrace();
                sendStatusMessage(SyncStatus.FAILED_FTP_UPLOAD);
            }
            if (isCancelled()) {
                return null;
            }
            try {
                for (int i = 0; i < imgFiles.size(); i++) {
                    final String[] name = imgFiles.get(i);
                    localFileName = name[0];
                    uploadFileName = name[1];
                    currentPicture = name[2];
                    sendStatusMessage(SyncStatus.UPLOAD_NEXT_PICTURE);
                    if (!TextUtils.isEmpty(uploadFileName)) {
                        File uploadPicture = new File(StorageUtils.getStorageRootFolder(mActivity), uploadFileName);
                        StorageUtils.copyFile(new File(localFileName), uploadPicture);
                        FileInputStream inStream = new FileInputStream(uploadPicture);
                        ftp.storeFile(uploadFileName, inStream);
                        inStream.close();
                        uploadPicture.delete();
                        sendStatusMessage(SyncStatus.UPLOAD_PICTURE_SUCCESS);
                        if (localFileName.contains("sign")) {
                            new File(localFileName).delete();
                        }
                        if (currentPicture.contains("pilot")) {
                            String pilotCode = currentPicture.replace("pilot.", MCCPilotLogConst.STRING_EMPTY);
                            pilotCode = pilotCode.replace(".jpg", MCCPilotLogConst.STRING_EMPTY);
                            Pilot pilot = DatabaseManager.getInstance(mActivity).getPilotByPilotCode(pilotCode);
                            if (pilot != null) {
                                //pilot.setSyncPict(SyncConst.SYNC_PICT_NO);
                                DatabaseManager.getInstance(mActivity).updatePilot(pilot);
                            }
                        }
                        currentPictNumber++;
                    }
                    if (isCancelled()) {
                        return null;
                    }
                }
                updateDb();
            } catch (Exception e) {
                e.printStackTrace();
                sendStatusMessage(SyncStatus.FAILED_FTP_UPLOAD);
            }

            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException ignored) {
            }
            if (isCancelled()) {
                return null;
            }
            sendStatusMessage(SyncStatus.SYNC_COMPLETED);
            currentFile = 0;
            currentPicture = MCCPilotLogConst.STRING_EMPTY;
            System.gc();
            return null;
        }
    }

    private void updateDb() {
        SyncViewModel.updateDbAfterSync(mActivity, syncIncompleteFlights);
        SyncViewModel.deleteSyncedFlights(mActivity, syncIncompleteFlights);
    }

    */
/**
     * Reset counter variables
     *//*

    private void clearValues() {
//        intPilots = 0;
        intAirfields = 0;
        intAircraft = 0;
        intFlights = 0;
        intICal = 0;
        intPilots = 0;
        intCurrencies = 0;
        intLogbook = 0;
        mTvSyncLog.setText(MCCPilotLogConst.STRING_EMPTY);
        mTvSyncStatus.setText(MCCPilotLogConst.STRING_EMPTY);
        mFoundFileNumber = 0;
        currentPicture = MCCPilotLogConst.STRING_EMPTY;
        iStatus = -1;
        isError = false;
    }

    public void sendStatusMessage(int pStatusMessage, CurrentFileName pCurrentFile) {
        String text = MCCPilotLogConst.STRING_EMPTY;
        switch (pStatusMessage) {

            case SyncStatus.SUCCESS:
                text = "SUCCESS\n";
                updateStatus(text, CYAN, -1, false);
                break;
            case SyncStatus.CONNECTING:
                text = "Connect to mccCLOUD";
                updateStatus(text, Color.BLACK, 0, true);
                break;
            case SyncStatus.SEARCHING_FILES:
                text = "Searching files on mccCLOUD";
                updateStatus(text, GRAY, 0, false);
                break;
            case SyncStatus.X_FILES_FOUND:
                final String sFoundFiles = Integer.toString(mFoundFileNumber);
                if (mFoundFileNumber == 1) {
//                    text = sFoundFiles + " file found\n";
                    text =  sFoundFiles + " file found\n";
                } else {
//                    text = sFoundFiles + " files found\n";
                    text =  sFoundFiles + " files found\n";
                }
                updateStatus(text, CYAN, 0, false);
                break;

            case SyncStatus.DOWNLOAD_FILE:
                try {
                    text = */
/*"\nDownload File "*//*
"\nDownload File " + Integer.toString(pCurrentFile.fileNumber);
                } catch (Exception ignored) {
                }
                updateStatus(text, Color.BLACK, 20, true);
                break;
            case SyncStatus.PROCESS_FILE:
                try {
                    text = */
/*"Process File " *//*
"Process File " + Integer.toString(pCurrentFile.fileNumber);
                } catch (Exception ignored) {
                }
                updateStatus(text, Color.BLACK, 30, true);
                break;
            case SyncStatus.UPDATE_PROGRESS:
                pcProgress += delta;
                if (pcProgress < 77) {
                    updateStatus("", Color.BLACK, pcProgress, false);
                } else if (pcProgress > 99) {
                    updateStatus("", Color.BLACK, 0, false);
                }
                break;
            case SyncStatus.PROCESSING_AIRFIELDS:
                text = "\t Processing Airfields";
                updateStatus(text, GRAY, 35, false);
                break;
            case SyncStatus.PROCESSING_AIRCRAFT:
                text = "\t Processing Aircraft";
                updateStatus(text, GRAY, 40, false);
                break;
            case SyncStatus.PROCESSING_PILOT:
                text = "\t Processing Pilots";
                updateStatus(text, GRAY, 45, false);
                break;
            case SyncStatus.PROCESSING_LOGBOOK_FLIGHTS:
                text = "\t Processing Logbook Flights";
                updateStatus(text, GRAY, 50, false);
                break;
            case SyncStatus.PROCESSING_FLIGHT:
                text = "\t Processing Flight";
                updateStatus(text, GRAY, 55, false);
                break;
            case SyncStatus.CLEANING_RECORDS:
                text = "\t Cleaning Records";
                updateStatus(text, CYAN, 60, false);
                break;
            case SyncStatus.UPDATING_TOTALS:
                text = "\t Updating Totals";
                updateStatus(text, GRAY, 65, false);
                break;
            case SyncStatus.UPDATING_GRAND_TOTALS:
                text = "\t Updating Grand Totals";
                updateStatus(text, GRAY, 70, false);
                break;
            case SyncStatus.PROCESSING_CURRENCIES:
                text = "\t Processing Currencies";
                updateStatus(text, GRAY, 75, false);
                break;
            case SyncStatus.PROCESSING_ICAL_DATA:
                text = "\t Processing iCal data";
                updateStatus(text, GRAY, 83, false);
                break;
            case SyncStatus.ERROR_CALENDAR_NAME:
                String defCal = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.DEFAULT_CALENDAR).getData();
//                text = "Export to Calendar \n  Calendar " + defCal + " does no longer exist";
                text = "Export to Calendar \n  Calendar " + defCal + " does no longer exist";
                updateStatus(text, Color.BLACK, 84, true);
                break;
            case SyncStatus.ERROR_CALENDAR_NO_NAME:
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
                break;
            case SyncStatus.CREATING_XML_FILE:
//                text = "\nCreating XML data file";
                text = "\nCreating XML data file";
                updateStatus(text, Color.BLACK, pcProgress, true);
                break;
            case SyncStatus.UPLOADING_XML_FILE:
//                text = "\nUploading XML file to mccCloud";
                text = "\nUploading XML file to mccCLOUD";
                updateStatus(text, Color.BLACK, 87, true);
                break;
            case SyncStatus.UPLOAD_NEXT_PICTURE:
//                text = "\nUpload picture " + currentPicture;
                text = "\nUpload picture " + currentPicture;
                updateStatus(text, Color.BLACK, 90, true);
                break;
            case SyncStatus.UPLOAD_PICTURE_SUCCESS:
                text = "SUCCESS";
                updateStatus(text, CYAN, 90, false);
                break;
            case SyncStatus.SYNC_COMPLETED:
                setRecordsFromPC();
                text = "SYNC COMPLETED";
//                text = "-> SYNC COMPLETED";
                if (!isError) {
                    updateStatus(text, Color.parseColor(MCCPilotLogConst.COLOR_DARK_GREEN), 100, true);
                } else {
                    updateStatus("", Color.RED, 0, false);
                }
                break;
            case SyncStatus.NO_INTERNET:
                text = "FAILED: No internet connection\n";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatus.NO_INTERNET_ACCESS:
                text = "FAILED \n\n No internet access :\n open the web browser and enter your Internet Access Key\n";
                updateStatus(text, Color.RED, 0, false);
                break;

            case SyncStatus.ERROR_DATA_PROCESSING:
                text = "FAILED file: \n" +
                        "" + pCurrentFile.fileName + "\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;
            case SyncStatus.ERROR_EOF:
                text = "ERROR  XML file: \n " +
                        pCurrentFile.fileName + "\n broken, some data may be missing\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;

            case SyncStatus.ERROR_ADD_LOGBOOK_FLIGHTS:
                text = "FAILED Adding LogbookFlights to local DataBase" +
                        "file: " + pCurrentFile.fileName + "\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;

            case SyncStatus.ERROR_CREATING_XML_FILE:
                text = "ERROR: Creating XML File\n";
                isError = true;
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatus.ERROR:
                text = "ERROR\n";
                isError = true;
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatus.FAILED_FTP_UPLOAD:
                text = "FAILED: mccCLOUD upload not successful\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;
            case SyncStatus.NEED_UPDATE:
                text = "XML file " */
/*"-> XML file "*//*
 + pCurrentFile.fileName + " cannot be parsed - You must first install latest update for this app\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;
            case SyncStatus.XML_TOO_OLD:
                text = "XML file " */
/*"-> XML file "*//*
 + pCurrentFile.fileName + " too old for parsing\n";
                updateStatus(text, Color.RED, 0, false);
                isError = true;
                break;
            case SyncStatus.FAILED:
                text = "FAILED\n";
                isError = true;
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatus.ERROR_FILE_DAMAGED:
                text = "FAILED \nCannot download XML file. File appears to be damaged.\n" + pCurrentFile.fileName + " (deleted)";
                updateStatus(text, Color.RED, 0, false);
                break;
            case SyncStatus.CREATEING_XML_FILE_DONE:
                text = "DONE";
                updateStatus(text, CYAN, 0, false);
                break;
        }
        if (pStatusMessage != SyncStatus.CONNECTING) {
            LogUtils.writeLogToFile(text);
        }
    }

    public void sendStatusMessage(int pMessage) {
        sendStatusMessage(pMessage, null);
    }

    public class CurrentFileName {
        public String fileName;
        public int fileNumber;
    }


    private void setRecordsFromPC() {
        final String text = "\n\nRecords from PC" +
                "\n       Pilots: " + Integer.toString(intPilots) +
                "\n       Aircraft: " + Integer.toString(intAircraft) +
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

    private void mailLog() {
        //String syncId = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);//ACTION_SEND

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

    public String ftpUserName() {
        */
/*Setting setting = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting == null) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        String syncID = setting.getData();
        String userName = "";
        if (syncID.length() > 0) {
            char lv = syncID.charAt(0);
            userName = Integer.toString(164 - lv);
            userName = "PilotXF_" + lv + userName;
        }
        return userName;*//*

        return "";
    }

    public String ftpPassword() {
       */
/* Setting setting = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID);
        if (setting == null) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        String syncID = setting.getData();
        String pass = "";
        if (syncID.length() > 0) {
            char nm = syncID.charAt(0);
            pass = Integer.toString(1967 - nm);
            pass = "clouD_" + nm + "|" + pass;
        }
        return pass;*//*

        return "";
    }

    @Override
    public void onDestroyView() {
        if (mSyncTask != null && mSyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSyncTask.cancel(true);
        }
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isTablet()) {
            updateWidthLayoutTablet();
        }
    }

    */
/**
     * update again width layout tablet when rotate screen.
     *//*

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
    public void onDestroy() {
        super.onDestroy();
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }
}
*/
