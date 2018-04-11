/*
package net.mcc3si.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mcc3si.BuildConfig;
import net.mcc3si.R;
import net.mcc3si.adapters.SyncHistoryAdapter;
import net.mcc3si.common.MCCPilotLogConst;
import net.mcc3si.common.SettingsConst;
import net.mcc3si.databases.manager.DatabaseManager;
import net.mcc3si.models.SyncHistoryModel;
import net.mcc3si.sync.SyncV4Functions;
import net.mcc3si.tasks.DownloadAsync;
import net.mcc3si.utilities.DateTimeUtils;
import net.mcc3si.utilities.StorageUtils;
import net.mcc3si.utilities.Utils;
import net.mcc3si.widgets.IndexableListView;
import net.mcc3si.widgets.MccDialog;
import net.mcc3si.widgets.PopupSelectionMenu;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

*/
/**
 * Created by tuan.na on 8/17/2015.
 * Sync history
 *//*

public class SyncHistoryFragment {*/
/* extends BaseMCCFragment {*//*


    */
/*@Bind(R.id.tvTitle)
    TextView mTvTitleRoot;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.lv_history)
    IndexableListView mLvHistory;
    @Bind(R.id.btnSyncAgain)
    Button mBtnSyncAgain;
    @Bind(R.id.btnViewImage)
    Button mBtnViewImage;
    @Bind(R.id.btnSendMail)
    Button mBtnSendMail;
    @Bind(R.id.btnDeleteAll)
    Button mBtnDeleteAll;
    @Bind(R.id.rlBottomBar)
    LinearLayout mLlBottomBar;

    public static final String SAVE_FILE_XML = "historyXML.txt";
    public static final String SAVE_FILE_IMG = "historyIMG.txt";
    private List<SyncHistoryModel> mSyncHistoryXMLList = new ArrayList<>();
    private List<SyncHistoryModel> mSyncHistoryIMGList = new ArrayList<>();
    private List<SyncHistoryModel> mSyncHistoryList = new ArrayList<>();
    private SyncHistoryModel mSyncHistoryModel;
    //private String syncID;
    private SyncHistoryAdapter mAdapter;
    private PopupSelectionMenu mPopupSelectionMenu;
    private File mRootDirXMLFile, mRootDirPilotPicture, mRootDirSignPicture;
    private boolean mIsDeleteAll;
    private boolean mIsNotAddSixMonths = false;
    private ProgressDialog mDialog;

    private static final int BUFFER = 1024;
    private static final String ZIP_FOLDER = "ZipFolder";
    private static final String ZIP_FOLDER_XML = "XmlZip.zip";
    private static final String ZIP_FOLDER_PILOT = "PilotPictureZip.zip";
    private static final String ZIP_FOLDER_SIGN = "SignPictureZip.zip";

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_sync_history;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar_sync_history;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);

        mIbMenu.setVisibility(View.GONE);
        mIbLeft.setVisibility(View.VISIBLE);
        mTvTitleRoot.setText(R.string.sync_history_title);
        //syncID = DatabaseManager.getInstance(mActivity).getSetting(SettingsConst.SYNC_ID).getData();
        try {
            loadHistory(true);
        } catch (Exception ignored) {
        }
        try {
            loadHistory(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSyncHistoryList.addAll(mSyncHistoryXMLList);
        mSyncHistoryList.addAll(mSyncHistoryIMGList);
        //sort desc follow date time
        Collections.sort(mSyncHistoryList, new Comparator<SyncHistoryModel>() {
            @Override
            public int compare(SyncHistoryModel itemLeft, SyncHistoryModel itemRight) {
                Date dateLeft = DateTimeUtils.getDateFromTimestamp(itemLeft.getTimestamp(), mIsNotAddSixMonths);
                Date dateRight = DateTimeUtils.getDateFromTimestamp(itemRight.getTimestamp(), mIsNotAddSixMonths);
                if (dateLeft != null && dateRight != null) {
                    return dateRight.compareTo(dateLeft);
                }

                return 0;
            }
        });

        mAdapter = new SyncHistoryAdapter(mSyncHistoryList, getActivity());
        mLvHistory.setAdapter(mAdapter);
        mLvHistory.setDrawRightBar(false);

        mBtnSyncAgain.setEnabled(false);
        mBtnViewImage.setEnabled(false);
        if (mSyncHistoryList.size() < 1) {
            mBtnSendMail.setEnabled(false);
            mBtnDeleteAll.setEnabled(false);
        }

        mRootDirXMLFile = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.XML_FOLDER);
        mRootDirPilotPicture = new File(StorageUtils.getStorageRootFolder(mActivity));
        mRootDirSignPicture = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.SYNCED_SIGN_FOLDER);

        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage("upload file to mccCLOUD...");
        mDialog.setCancelable(false);
    }

    @OnItemClick(R.id.lv_history)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        mAdapter.removeSelectedItems();
        mAdapter.setSelection(pPosition);
        mSyncHistoryModel = mSyncHistoryList.get(pPosition);
        switch (mSyncHistoryModel.getTypeHistoryFile()) {
            case MCCPilotLogConst.HISTORY_PILOT_PICTURE:
            case MCCPilotLogConst.HISTORY_SIGN_PICTURE:
                mBtnSyncAgain.setEnabled(true);
                mBtnViewImage.setEnabled(true);
                break;
            default:
                mBtnSyncAgain.setEnabled(true);
                mBtnViewImage.setEnabled(false);
                break;
        }
    }

    @OnClick({R.id.btnSyncAgain, R.id.btnViewImage, R.id.btnSendMail, R.id.btnDeleteAll, R.id.rlBackIcon})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btnSyncAgain:
                if (mSyncHistoryModel != null) {
                    if (isHavingNetwork()) {
                        new syncAgainTask().execute();
                    } else {
                        MccDialog.getOkAlertDialog(mActivity, "Connection was lost!").show();
                    }
                }
                break;
            case R.id.btnViewImage:
                previewImage();
                break;
            case R.id.btnSendMail:
                if (mSyncHistoryList != null && !mSyncHistoryList.isEmpty()) {
                    sendHistoryFiles();
                }
                break;
            case R.id.btnDeleteAll:
                showOptionDeleteHistoryFiles();
                break;
            case R.id.rlBackIcon:
                finishFragment();
                break;
            default:
                break;
        }
    }


    class syncAgainTask extends AsyncTask<Void, Integer, Void> {
        File pictureDir, syncedSignDir;
        File pilotPictureUpload, signPictureUpload;

        public syncAgainTask() {
            pictureDir = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.PICTURE_FOLDER);
            if (!pictureDir.exists()) {
                pictureDir.mkdir();
            }
            syncedSignDir = new File(StorageUtils.getStorageRootFolder(mActivity), MCCPilotLogConst.SIGN_FOLDER);
            if (!syncedSignDir.exists()) {
                syncedSignDir.mkdir();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FTPClient ftp = null;
            try {
                SyncV4Functions functions = new SyncV4Functions(mActivity);
                ftp = new FTPClient();
                ftp.connect(MCCPilotLogConst.FTP_BASE_IP);
                ftp.enterLocalPassiveMode();
                if (!ftp.login(SyncV4Fragment.FTP_USER_NAME, SyncV4Fragment.FTP_PASSWORD)) {
                    ftp.logout();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            MccDialog.getOkAlertDialog(mActivity, "Connect to mccCLOUD failed!").show();
                        }
                    });
                }
                int replyCode = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    ftp.disconnect();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            MccDialog.getOkAlertDialog(mActivity, "Connect to mccCLOUD failed!").show();
                        }
                    });
                    return null;
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                String strFileCheck = "";
                FileInputStream inStream;
                switch (mSyncHistoryModel.getTypeHistoryFile()) {
                    case MCCPilotLogConst.HISTORY_FLIGHT_RECORD:
                        File file = new File(mRootDirXMLFile, mSyncHistoryModel.getRealFile());
                        inStream = new FileInputStream(file.getAbsolutePath());
                        ftp.storeFile(file.getName(), inStream);
                        strFileCheck = mSyncHistoryModel.getRealFile();
                        break;
                    case MCCPilotLogConst.HISTORY_PILOT_PICTURE:
                        File pilotPicture = new File(mRootDirPilotPicture, mSyncHistoryModel.getRealFile());
                        File tempPictureFile = new File(pictureDir, mSyncHistoryModel.getRealFile());
                        StorageUtils.copyFile(pilotPicture, tempPictureFile);
                        StorageUtils.zipFileAtPath(tempPictureFile.getAbsolutePath(), new File(pictureDir, mSyncHistoryModel.getFileNameUpload()).getAbsolutePath());
                        tempPictureFile.delete();

                        pilotPictureUpload = new File(pictureDir, mSyncHistoryModel.getFileNameUpload());
                        if (pilotPictureUpload.exists()) {
                            inStream = new FileInputStream(pilotPictureUpload);
                            ftp.storeFile(pilotPictureUpload.getName(), inStream);
                            inStream.close();

                            strFileCheck = mSyncHistoryModel.getFileNameUpload();
                        }
                        break;
                    case MCCPilotLogConst.HISTORY_SIGN_PICTURE:
                        File signPicture = new File(mRootDirSignPicture, mSyncHistoryModel.getRealFile());
                        File tempSignPictureFile = new File(syncedSignDir, mSyncHistoryModel.getRealFile());
                        StorageUtils.copyFile(signPicture, tempSignPictureFile);
                        StorageUtils.zipFileAtPath(tempSignPictureFile.getAbsolutePath(), new File(syncedSignDir, mSyncHistoryModel.getFileNameUpload()).getAbsolutePath());
                        tempSignPictureFile.delete();

                        signPictureUpload = new File(syncedSignDir, mSyncHistoryModel.getFileNameUpload());
                        if (signPictureUpload.exists()) {
                            inStream = new FileInputStream(signPictureUpload);
                            ftp.storeFile(signPictureUpload.getName(), inStream);
                            inStream.close();

                            strFileCheck = mSyncHistoryModel.getFileNameUpload();
                        }
                        break;
                    default:
                        break;
                }


                String storeFile = DownloadAsync.downloadString(functions.storeFile(strFileCheck));
                if (!storeFile.contains("STORED")) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            MccDialog.getOkAlertDialog(mActivity, "ERROR while storing file").show();
                        }
                    });
                    return null;
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Utils.showToast(mActivity, "Upload file to mccCLOUD successful!");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        MccDialog.getOkAlertDialog(mActivity, "Connect to mccCLOUD failed!").show();
                    }
                });
                return null;
            } finally {
                try {
                    if (ftp != null) {
                        ftp.logout();
                        ftp.disconnect();
                    }
                } catch (IOException ignored) {
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (mSyncHistoryModel.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_PILOT_PICTURE) {
                if (pilotPictureUpload != null) {
                    pilotPictureUpload.delete();
                }
            } else if (mSyncHistoryModel.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_SIGN_PICTURE) {
                if (signPictureUpload != null) {
                    signPictureUpload.delete();
                }
            }
        }
    }

    private void previewImage() {
        if (mSyncHistoryModel != null) {
            Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.layout_view_image);

            ImageView imageView = (ImageView) dialog.findViewById(R.id.ivPreviewImage);

            File file;
            if (mSyncHistoryModel.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_PILOT_PICTURE) {
                file = new File(StorageUtils.getStorageRootFolder(mActivity), mSyncHistoryModel.getRealFile());
            } else {
                file = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNCED_SIGN_FOLDER, mSyncHistoryModel.getRealFile());
            }
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }

            dialog.show();
        }
    }

    private boolean checkHasData(int pIndex) {
        for (SyncHistoryModel item : mSyncHistoryList) {
            if (item.getTypeHistoryFile() == pIndex) {
                return true;
            }
        }

        return false;
    }

    private void sendHistoryFiles() {
        //zip files
        File zipFolder = new File(StorageUtils.getStorageRootFolder(mActivity), ZIP_FOLDER);
        if (!zipFolder.exists()) {
            zipFolder.mkdir();
        }
        deleteFilesZipExists(zipFolder);

        File destinationXML = new File(zipFolder, ZIP_FOLDER_XML);
        File destinationPilot = new File(zipFolder, ZIP_FOLDER_PILOT);
        File destinationSign = new File(zipFolder, ZIP_FOLDER_SIGN);
        ZipOutputStream outXML = null, outPilotPicture = null, outSignPicture = null;
        try {
            if (checkHasData(MCCPilotLogConst.HISTORY_FLIGHT_RECORD)) {
                outXML = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destinationXML)));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            outXML = null;
        }

        try {
            if (checkHasData(MCCPilotLogConst.HISTORY_PILOT_PICTURE)) {
                outPilotPicture = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destinationPilot)));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            outPilotPicture = null;
        }

        try {
            if (checkHasData(MCCPilotLogConst.HISTORY_SIGN_PICTURE)) {
                outSignPicture = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destinationSign)));
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            outSignPicture = null;
        }

        for (SyncHistoryModel item : mSyncHistoryList) {
            switch (item.getTypeHistoryFile()) {
                case MCCPilotLogConst.HISTORY_PILOT_PICTURE:
                    if (outPilotPicture != null) {
                        writeMoreIntoFileZip(outPilotPicture, mRootDirPilotPicture, item.getRealFile());
                    }
                    break;
                case MCCPilotLogConst.HISTORY_SIGN_PICTURE:
                    if (outSignPicture != null) {
                        writeMoreIntoFileZip(outSignPicture, mRootDirSignPicture, item.getRealFile());
                    }
                    break;
                case MCCPilotLogConst.HISTORY_FLIGHT_RECORD:
                    if (outXML != null) {
                        writeMoreIntoFileZip(outXML, mRootDirXMLFile, item.getRealFile());
                    }
                    break;
                default:
                    break;
            }
        }
        try {
            if (outPilotPicture != null) {
                outPilotPicture.close();
            }
            if (outXML != null) {
                outXML.close();
            }
            if (outSignPicture != null) {
                outSignPicture.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //send mail to help desk
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"helpdesk@mccpilotlog.net"});//
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        String lineSep = System.getProperty(MCCPilotLogConst.TEXT_LINE_SEPARATOR);
        *//*
*/
/*String header = MCCPilotLogConst.TEXT_SYNC_ID + syncID + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + BuildConfig.VERSION_NAME + lineSep +
                        MCCPilotLogConst.TEXT_DEVICE + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + lineSep +
                        MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE;*//*
*/
/*
        String header = MCCPilotLogConst.MCC_APP_NAME + lineSep +
                MCCPilotLogConst.TEXT_APP_VERSION + BuildConfig.VERSION_NAME + "  (" + String.valueOf(BuildConfig.VERSION_CODE) + ")" + lineSep +
                MCCPilotLogConst.TEXT_DEVICE + MCCPilotLogConst.TEXT_OS + android.os.Build.VERSION.RELEASE + "  (" + android.os.Build.MANUFACTURER + "  " + android.os.Build.MODEL + ")" + lineSep +
                MCCPilotLogConst.TEXT_SYNC_ID + MCCPilotLogConst.LINE_EMAIL_HEADER + lineSep + MCCPilotLogConst.WRITE_MESSAGE_HERE + lineSep + lineSep + lineSep;
        intent.putExtra(Intent.EXTRA_TEXT, header);
        intent.setType(MCCPilotLogConst.INTENT_TYPE);
        ArrayList<Uri> uris = new ArrayList<>();
        String packageName = getApplication().getPackageName();

        Uri fileUri;
        File xmlZipFile = new File(zipFolder, ZIP_FOLDER_XML);
        if (xmlZipFile.exists() && xmlZipFile.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, xmlZipFile);
            uris.add(fileUri);
        }

        File pilotZipFile = new File(zipFolder, ZIP_FOLDER_PILOT);
        if (pilotZipFile.exists() && pilotZipFile.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, pilotZipFile);
            uris.add(fileUri);
        }

        File signZipFile = new File(zipFolder, ZIP_FOLDER_SIGN);
        if (signZipFile.exists() && signZipFile.length() > 0) {
            fileUri = FileProvider.getUriForFile(mActivity, packageName, signZipFile);
            uris.add(fileUri);
        }

        if (!uris.isEmpty()) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mActivity, "Sorry, There are no email clients installed on the device!");
        }
    }

    private void deleteFilesZipExists(File file) {
        File xmlZipFile = new File(file, ZIP_FOLDER_XML);
        File pilotZipFile = new File(file, ZIP_FOLDER_PILOT);
        File signZipFile = new File(file, ZIP_FOLDER_SIGN);
        //Delete all file zip have just created.
        if (xmlZipFile.exists()) {
            xmlZipFile.delete();
        }
        if (pilotZipFile.exists()) {
            pilotZipFile.delete();
        }
        if (signZipFile.exists()) {
            signZipFile.delete();
        }
    }

    private void writeMoreIntoFileZip(ZipOutputStream pOutputStream, File pDirFile, String pFileName) {
        try {
            BufferedInputStream origin;
            File fileTemp = new File(pDirFile, pFileName);
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(fileTemp);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(pFileName);
            pOutputStream.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                pOutputStream.write(data, 0, count);
            }
            origin.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void showOptionDeleteHistoryFiles() {
        LinearLayout mCustomLayout = (LinearLayout) safeInflater(getLayoutInflater(), null, R.layout.menu_option_delete_sync_history);

        PopupSelectionMenu.OnClickMenuListener onClickMenuListener = new PopupSelectionMenu.OnClickMenuListener() {
            @Override
            public void onClickMenu(int type) {
                if (mSyncHistoryList != null && !mSyncHistoryList.isEmpty()) {
                    if (type == MCCPilotLogConst.SYNC_HISTORY_DELETE_ALL) {
                        MccDialog.getOkCancelAlertDialog(mActivity, R.string.sync_history_title_dialog_delete_all, R.string.sync_history_content_dialog_delete_all, new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    mIsDeleteAll = true;
                                    deleteAllFilesHistory(mSyncHistoryList);
                                    //Update again adapter
                                    mSyncHistoryList.clear();
                                    mAdapter.updateAdapter(mSyncHistoryList);
                                    mBtnSendMail.setEnabled(false);
                                    mBtnDeleteAll.setEnabled(false);
                                    mBtnSyncAgain.setEnabled(false);
                                    mBtnViewImage.setEnabled(false);
                                }
                            }
                        }).show();
                    } else {
                        mIsDeleteAll = false;
                        deleteFilesHistoryOlder();
                    }
                }
            }
        };
        mPopupSelectionMenu = new PopupSelectionMenu(mActivity);
        mPopupSelectionMenu.setOnClickMenuListenerOb(onClickMenuListener);
        mPopupSelectionMenu.show(mCustomLayout, mLlBottomBar);
    }

    private void deleteAllFilesHistory(List<SyncHistoryModel> pListHistory) {
        //read all text from history file
        File fileXML = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNC_HISTORY_FOLDER, SAVE_FILE_XML);
        List<String> listFileXML = readAllText(fileXML);

        File fileIMG = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNC_HISTORY_FOLDER, SAVE_FILE_IMG);
        List<String> listFileIMG = readAllText(fileIMG);

        File fileDelete;
        for (SyncHistoryModel item : pListHistory) {
            if (!mIsDeleteAll) {
                if (item.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_PILOT_PICTURE || item.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_SIGN_PICTURE) {
                    for (String str : listFileIMG) {
                        if (str.contains(item.getFileNameUpload())) {
                            listFileIMG.remove(str);
                            break;
                        }
                    }
                } else {
                    for (String str : listFileXML) {
                        if (str.contains(item.getFileNameUpload())) {
                            listFileXML.remove(str);
                            break;
                        }
                    }
                }
            }
            if (item.getTypeHistoryFile() == MCCPilotLogConst.HISTORY_FLIGHT_RECORD) {
                //Remove real file
                fileDelete = new File(mRootDirXMLFile, item.getRealFile());
                fileDelete.delete();
            }
        }

        //Remove virtual file
      *//*
*/
/*  if (mIsDeleteAll) {
            List<String> listTemp = copyList(listFileIMG);
            for (String str : listFileIMG) {
                if (str.contains(syncID.split("-")[0])) {
                    listTemp.remove(str);
                }
            }
            listFileIMG = listTemp;

            List<String> listTempXML = copyList(listFileXML);
            for (String str : listFileXML) {
                if (str.contains(syncID)) {
                    listTempXML.remove(str);
                }
            }
            listFileXML = listTempXML;
        }*//*
*/
/*

        //Write again into file
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(fileXML, false));
            if (listFileXML.size() == 0) {
                writer.write(STRING_EMPTY);
            } else {
                for (int size = listFileXML.size(), i = 0; i < size; i++) {
                    String line = listFileXML.get(i);
//                    if (i != size - 1) {
                    writer.write(line + "\n");
//                    } else {
//                        writer.write(line);
//                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(fileIMG, false));
            if (listFileIMG.size() == 0) {
                writer.write(STRING_EMPTY);
            } else {
                for (int size = listFileIMG.size(), i = 0; i < size; i++) {
                    String line = listFileIMG.get(i);
//                    if (i != size - 1) {
                    writer.write(line + "\n");
//                    } else {
//                        writer.write(line);
//                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void deleteFilesHistoryOlder() {
        Date dateCurrent = DateTimeUtils.getCurrentDate();
        Date dateHistoryFile;
        //copy list data
        List<SyncHistoryModel> syncHistoryList = new ArrayList<>();

        for (SyncHistoryModel item : mSyncHistoryList) {
            boolean mIsAddSixMonths = true;
            dateHistoryFile = DateTimeUtils.getDateFromTimestamp(item.getTimestamp(), mIsAddSixMonths);
            if (dateHistoryFile != null && dateCurrent != null && dateHistoryFile.compareTo(dateCurrent) < 0) {
                syncHistoryList.add(item);
            }
        }

        //remove from syncHistoryList
        if (syncHistoryList.size() > 0) {
            deleteAllFilesHistory(syncHistoryList);
            //Update again adapter
            for (SyncHistoryModel item : syncHistoryList) {
                mSyncHistoryList.remove(item);
            }
            if (mSyncHistoryList.size() > 1) {
                mAdapter.removeSelectedItems();
                mBtnSyncAgain.setEnabled(false);
                mBtnViewImage.setEnabled(false);
            }
            mAdapter.updateAdapter(mSyncHistoryList);
        }
    }

    private List<String> copyList(List<String> pList) {
        List<String> listString = new LinkedList<>();
        for (String str : pList) {
            listString.add(str);
        }

        return listString;
    }

    private List<String> readAllText(File pFile) {
        List<String> lines = new LinkedList<>();
        try {
            final Scanner reader = new Scanner(new FileInputStream(pFile), "UTF-8");
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return lines;
    }

    private void loadHistory(boolean pIsXML) throws Exception {
      *//*
*/
/*  File historyFile = new File(StorageUtils.getStorageRootFolder(mActivity) + File.separator + MCCPilotLogConst.SYNC_HISTORY_FOLDER,
                pIsXML ? SAVE_FILE_XML : SAVE_FILE_IMG);
        if (historyFile.exists()) {
            FileInputStream fis = new FileInputStream(historyFile.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line;
            String[] temp;
            int lineNumber = 0;
            if (pIsXML) {
                while ((line = reader.readLine()) != null && lineNumber < 21) {
                    lineNumber++;
                    temp = line.split("##");
                    if (temp.length >= 2 && temp[0].contains(syncID)) {
                        mSyncHistoryXMLList.add(new SyncHistoryModel(MCCPilotLogConst.HISTORY_FLIGHT_RECORD, temp[0], temp[0], temp[0], temp[1]));
                    }
                }
                fis.close();
                if (lineNumber > 20) {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(historyFile)));
                    for (SyncHistoryModel sh : mSyncHistoryXMLList) {
                        pw.println(sh.getFileNameUpload() + "##" + sh.getTimestamp());
                    }
                    pw.flush();
                    pw.close();
                }
            } else {
                while ((line = reader.readLine()) != null && lineNumber < 11) {
                    lineNumber++;
                    temp = line.split("##");
                    if (temp.length >= 2 && temp[0].contains(syncID.split("-")[0])) {
                        int type;
                        String[] zipName = temp[0].split("\\.");
                        String displayName, realName;
                        if (temp[0].contains("img.")) {
                            type = MCCPilotLogConst.HISTORY_PILOT_PICTURE;
                            displayName = "mobile." + syncID + ".img." + zipName[2] + ".jpg";
                            realName = "img." + zipName[2] + ".jpg";
                        } else {
                            type = MCCPilotLogConst.HISTORY_SIGN_PICTURE;
                            displayName = "mobile." + syncID + ".img." + zipName[2] + ".jpg";
                            realName = "img." + zipName[2] + ".jpg";
                        }
                        mSyncHistoryXMLList.add(new SyncHistoryModel(type, displayName, temp[0], realName, temp[1]));
                    }
                }
                fis.close();
                if (lineNumber > 10) {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(historyFile)));
                    for (SyncHistoryModel sh : mSyncHistoryIMGList) {
                        pw.println(sh.getFileNameUpload() + "##" + sh.getTimestamp());
                    }
                    pw.flush();
                    pw.close();
                }
            }
        }*//*
*/
/*
    }

    //TuanPV for portrait tablet
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isTablet() && mPopupSelectionMenu != null) {
            mPopupSelectionMenu.dissmissPopup();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (mActivity.getCurrentFocus() != null && mActivity.getCurrentFocus().getWindowToken() != null) {
//            inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*//*

}
*/
