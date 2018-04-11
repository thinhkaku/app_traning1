/*
package net.mcc3si.common;

import android.app.Activity;

import net.mcc3si.databases.entities.Setting;
import net.mcc3si.databases.manager.DatabaseManager;
import net.mcc3si.fragments.SyncV4Fragment;
import net.mcc3si.fragments.SyncV3Fragment;
import net.mcc3si.utilities.DatabaseUtils;
import net.mcc3si.utilities.StorageUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

*/
/**
 * Created by tuan.na on 7/30/2015.
 *//*

public class Backup {

    private static final String BACKUP_FOLDER = "Backup";
    private FTPClient mFtpClient;
    private Activity mActivity;
    private DatabaseManager mDatabaseManager;

    public Backup(Activity pActivity){
        mActivity = pActivity;
        mDatabaseManager = new DatabaseManager(mActivity);
    }

    public boolean backupNow(){
       */
/* String mSyncID = mDatabaseManager.getSetting(SettingsConst.SYNC_ID).getData();
        if (mSyncID.length() == 15){
            SyncV3Fragment fragment = new SyncV3Fragment();
            try {
                String backupFileName = String.format(MCCPilotLogConst.BACK_UP_FILE_NAME, mSyncID);
                File backupFile = new File(StorageUtils.getStorageRootFolder(mActivity), backupFileName);
                backupFile.delete();
                mFtpClient = new FTPClient();
                mFtpClient.connect(MCCPilotLogConst.FTP_BASE_IP);
                mFtpClient.enterLocalPassiveMode();
                if (!mFtpClient.login(ftpUserName(), ftpPassword())) {
                    mFtpClient.logout();
                }
                int replyCode = mFtpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    mFtpClient.disconnect();
                }

                StorageUtils.copyFile(new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES + DatabaseManager.DATABASE_PILOT), backupFile);
                FileInputStream is = new FileInputStream(backupFile);
                mFtpClient.storeFile(backupFileName, is);
                is.close();
                mFtpClient.logout();
                mFtpClient.disconnect();
                backupFile.delete();
            }catch (Exception e){
//                e.printStackTrace();
                return false;
            }
        }else if (mSyncID.length() == 16){
            try {
                String backupFileName = String.format(MCCPilotLogConst.BACK_UP_FILE_NAME_V4, mSyncID);
                File backupFile = new File(StorageUtils.getStorageRootFolder(mActivity), backupFileName);
                backupFile.delete();
                mFtpClient = new FTPClient();
                mFtpClient.connect(MCCPilotLogConst.FTP_BASE_IP);
                mFtpClient.enterLocalPassiveMode();
                if (!mFtpClient.login(SyncV4Fragment.FTP_USER_NAME, SyncV4Fragment.FTP_PASSWORD)) {
                    mFtpClient.logout();
                }
                int replyCode = mFtpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    mFtpClient.disconnect();
                }

                StorageUtils.zipFileAtPath(new File(StorageUtils.getStorageRootFolder(mActivity) + DatabaseUtils.DATABASES + DatabaseManager.DATABASE_PILOT).getAbsolutePath(), backupFile.getAbsolutePath());
                FileInputStream is = new FileInputStream(backupFile);
                mFtpClient.storeFile(backupFileName, is);
                is.close();
                mFtpClient.logout();
                mFtpClient.disconnect();
                backupFile.delete();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }*//*

        return true;
    }

    private String ftpUserName() {
       */
/* Setting setting = mDatabaseManager.getSetting(SettingsConst.SYNC_ID);
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

    private String ftpPassword() {
       */
/* Setting setting = mDatabaseManager.getSetting(SettingsConst.SYNC_ID);
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
}
*/
