/*
package net.mcc3si.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.res.AssetManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import net.mcc3si.R;
import net.mcc3si.common.MCCPilotLogConst;
import net.mcc3si.common.SettingsConst;
import net.mcc3si.common.StateKey;
import net.mcc3si.databases.entities.Aircraft;
import net.mcc3si.databases.entities.Airfield;
import net.mcc3si.databases.entities.AirfieldPilot;
import net.mcc3si.databases.entities.Currencies;
import net.mcc3si.databases.entities.FlightPilot;
import net.mcc3si.databases.entities.Pilot;
import net.mcc3si.databases.entities.Setting;
import net.mcc3si.databases.entities.Totals;
import net.mcc3si.databases.manager.DatabaseManager;
import net.mcc3si.fragments.SyncV3Fragment;
import net.mcc3si.models.AccountCalendarModel;
import net.mcc3si.utilities.CalendarAccess;
import net.mcc3si.utilities.LogUtils;
import net.mcc3si.utilities.StorageUtils;
import net.mcc3si.utilities.Utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

*/
/**
 * Created by tuan.na on 8/18/2015.
 * Sync logic for v3
 *//*

public class SyncData {

    private Context mContext;
    private AutoPilot mAutoPilot;
    private VTDNav mVTDNavCurrency;
    private VTDNav mNavigation;
    public boolean isCalcel = false;
    public static int CURRENT_XML_VERSION = 8;
    private float val = 0;
    private int version = 0;
    private SyncV3Fragment mFragment;
    private boolean isErrorEOF = false, isExportCalendar;
    private int mIndex = 0;

    public SyncData(Context pContext, SyncV3Fragment pFragment) {
        mContext = pContext;
        mFragment = pFragment;
        isExportCalendar = StorageUtils.getBooleanFromSharedPref(mContext, StateKey.EXPORT_CALENDAR_KEY, true);
    }

    public boolean syncFromPC(SyncV3Fragment.CurrentFileName pCurrentFileName, FTPClient pFtpClient) {
        isErrorEOF = false;
        mIndex++;
        try {
            mFragment.sendStatusMessage(SyncStatus.DOWNLOAD_FILE, pCurrentFileName);
            InputStream is = pFtpClient.retrieveFileStream(pCurrentFileName.fileName);
            if (is == null) {
                mFragment.sendStatusMessage(SyncStatus.ERROR_SYNC_PC);
                return false;
            }
            //PL-254
           */
/* byte[] xmlFileByte = Utils.getBytes(is, pCurrentFileName.fileName, mContext);
            if(xmlFileByte.length==0){
                mFragment.sendStatusMessage(SyncStatus.ERROR_FILE_SIZE_ZERO, pCurrentFileName);
                return true;
            }*//*

            //End PL-254
            //Pl-507
            VTDGen vg = new VTDGen();
            try {

                vg.setDoc(Utils.getBytes(is, pCurrentFileName.fileName, mContext));
                vg.parse(true);
            } catch (Exception e) {
                mFragment.sendStatusMessage(SyncStatus.ERROR_FILE_DAMAGED, pCurrentFileName);
                return true;
            }
            //End PL-507
            mNavigation = vg.getNav();
            mAutoPilot = new AutoPilot(mNavigation);

            String xmlContent = new String(mNavigation.getXML().getBytes(), "UTF-8");
//            String xmlContent = new String(mNavigation.getXML().getBytes(), "windows-1252");

            if (!xmlContent.contains("dataroot")) {
                mFragment.sendStatusMessage(SyncStatus.ERROR_SYNC_PC, pCurrentFileName);
                return false;
            }

            //region: read XML tag
            mFragment.sendStatusMessage(SyncStatus.PROCESS_FILE, pCurrentFileName);
            if (!readTagVersion()) {
                return false;
            }
            //check has currency or not?
            if (xmlContent.contains("<Currency>")) {
                mVTDNavCurrency = mNavigation;
            }
            if (isCalcel)
                return true;
            readTagUserFields();
            if (isCalcel)
                return true;
            readTagDelete();
            if (isCalcel)
                return true;
            readTagAircraft();
            if (isCalcel)
                return true;
            readTagAirfield();
            if (isCalcel)
                return true;
            readTagA4Airfield();
            if (isCalcel)
                return true;
            readTagPilot();
            if (isCalcel)
                return true;
            readTagFlight();
            if (isCalcel)
                return true;
            readTagTotals();
            if (isCalcel)
                return true;
            readTagGrandTotals();
            if (isCalcel)
                return true;
            readTagLogbook();
            if (isCalcel)
                return true;
            if (mIndex == mFragment.mFoundFileNumber && mVTDNavCurrency != null) {
                readTagCurrencies(mVTDNavCurrency);
            }
            if (isCalcel)
                return true;
            //add condition ExportCalendar = true following JIRA PL-380
            if (mIndex == mFragment.mFoundFileNumber && xmlContent.contains("<CA>") && isExportCalendar) {
                readTagCalendar();
            }
            //end region
        } catch (Exception e) {
            e.printStackTrace();
            mAutoPilot.resetXPath();
            mFragment.sendStatusMessage(SyncStatus.ERROR_SYNC_PC, pCurrentFileName);
            return false;
        }
        mAutoPilot.resetXPath();
        if (isErrorEOF) {
            mFragment.sendStatusMessage(SyncStatus.ERROR_EOF, pCurrentFileName);
        }

        return true;
    }

    */
/**
     * Test function
     *
     * @param xmlContent xml content, passed as a string for testing
     * @param mFragment  fragment
     * @return boolean
     *//*

//    public boolean syncFromPC(String xmlContent, SyncV3Fragment mFragment) {
//        try {
//            VTDGen vg = new VTDGen();
//            vg.setDoc(xmlContent.getBytes("UTF-8"));
//            vg.parse(true);
//            mNavigation = vg.getNav();
//            mAutoPilot = new AutoPilot(mNavigation);
//
//            mFragment.sendStatusMessage(SyncStatus.PROCESS_FILE);
//            if (!readTagVersion()) {
//                return false;
//            }
//            readTagUserFields();
//            readTagDelete();
//            readTagAircraft();
//            readTagAirfield();
//            readTagA4Airfield();
//            readTagPilot();
//            readTagFlight();
//            readTagTotals();
//            readTagGrandTotals();
//            readTagLogbook();
//            readTagCurrencies();
//            readTagCalendar();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mAutoPilot.resetXPath();
//            mFragment.sendStatusMessage(SyncStatus.ERROR_SYNC_PC);
//            return false;
//        }
//        mAutoPilot.resetXPath();
//        return true;
//    }

    */
/**
     * Method to read version tag. Remember to reset XPATH after calling read tag method
     *
     * @return return true if read success, otherwise return false
     *//*

    private boolean readTagVersion() {
        try {
            mAutoPilot.selectXPath("/dataroot/version");
            while (mAutoPilot.evalXPath() != -1) {
                version = Integer.parseInt(mNavigation.toNormalizedString(mNavigation.getText()));
                LogUtils.e("/version ==> " + version);
                if (version > CURRENT_XML_VERSION) {
                    mFragment.sendStatusMessage(SyncStatus.NEED_UPDATE);
                    return false;
                } else if (version < CURRENT_XML_VERSION - 1) {
                    mFragment.iStatus = SyncStatus.XML_TOO_OLD;
                    mFragment.sendStatusMessage(SyncStatus.XML_TOO_OLD);
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mAutoPilot.resetXPath();
            return false;
        }
        mAutoPilot.resetXPath();

        return true;
    }

    */
/**
     * Method to read tag userfields. Remember to reset xpath
     *//*

    private void readTagUserFields() {
        String usr = null;
        try {
            mAutoPilot.selectXPath("/dataroot/userfields");
            while (mAutoPilot.evalXPath() != -1) {
                usr = mNavigation.toNormalizedString(mNavigation.getText());
            }
            if (usr == null) {
                mAutoPilot.resetXPath();
                return;
            }

            usr = usr.replace(MCCPilotLogConst.NEW_LINE_CHARACTER, MCCPilotLogConst.STRING_EMPTY);
            usr = usr.replace("|", "##");
            String[] data = usr.split("##");

            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U1, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U2, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U3, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U4, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N1, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N2, MCCPilotLogConst.STRING_EMPTY);
            StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N3, MCCPilotLogConst.STRING_EMPTY);

            //PL-459
            if (data.length > 0) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U1, data[0]);
            }
            if (data.length > 1) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U2, data[1]);
            }
            if (data.length > 2) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U3, data[2]);
            }
            if (data.length > 3) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_U4, data[3]);
            }

            if (data.length > 4) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N1, data[4]);
            }
            if (data.length > 5) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N2, data[5]);
            }
            if (data.length > 6) {
                StorageUtils.writeStringToSharedPref(mContext, StateKey.TITLE_USER_N3, data[6]);
            }
            //end PL-459
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }
        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read tag delete.
     *//*

    private void readTagDelete() {
        String tableName = null, recordId = null, replaceBy = null;
        try {
            mAutoPilot.selectXPath("//DL/delete");
            while (mAutoPilot.evalXPath() != -1) {
                if (isCalcel)
                    break;
                AutoPilot ap1 = new AutoPilot(mNavigation);
                ap1.selectXPath(".*/
/*");
//                mFragment.sendStatusMessage(SyncStatus.CLEANING_RECORDS);//Remove message as require of customer
                while (ap1.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                        case "TableName":
                            tableName = mNavigation.toNormalizedString(mNavigation.getText());
                            break;
                        case "RecordID":
                            recordId = mNavigation.toNormalizedString(mNavigation.getText());
                            break;
                        case "ReplaceBy":
                            replaceBy = mNavigation.toNormalizedString(mNavigation.getText());
                            break;
                        default:
                            break;
                    }
                }
                if ((MCCPilotLogConst.STRING_EMPTY.equalsIgnoreCase(replaceBy) || MCCPilotLogConst.NULL.equalsIgnoreCase(replaceBy))) {
                    SyncViewModel.deleteFromTable(mContext, tableName, recordId);
                } else {
                    SyncViewModel.updateTable(mContext, tableName, recordId, replaceBy);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }
        mAutoPilot.resetXPath();
    }

    */
/**
     * Read tag aircraft. Remember to reset xpath
     *//*

    private void readTagAircraft() {
        Aircraft mAircraft = new Aircraft();
        String value;
        try {
            mAutoPilot.selectXPath("/dataroot/AC");
            if (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_AIRCRAFT);
                AutoPilot ap1 = new AutoPilot(mNavigation);
                ap1.selectXPath("/dataroot/AC/aircraft");
                while (ap1.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    AutoPilot ap = new AutoPilot(mNavigation);
                    ap.selectXPath(".*/
/*");
                    while (ap.evalXPath() != -1) {
                        if (isCalcel)
                            break;
                        value = mNavigation.toNormalizedString(mNavigation.getText());
                        switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                            case "Category":
                                mAircraft.setCategory(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Class":
                                if (value.equalsIgnoreCase(MCCPilotLogConst.NULL)) {
                                    mAircraft.setClassAircraft(null);
                                    break;
                                }
                                mAircraft.setClassAircraft(Integer.parseInt(value));
                                break;
                            case "AircraftCode":
                                mAircraft.setAircraftCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Company":
                                mAircraft.setCompany(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "DefaultLog":
                                mAircraft.setDefaultLog(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "DeviceCode":
                                mAircraft.setDeviceCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "CondLog":
                                mAircraft.setCondLog(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Model":
                                mAircraft.setModel(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value);
                                break;
                            case "Power":
                                mAircraft.setPower(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Reference":
                                mAircraft.setReference(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "RefSearch":
                                mAircraft.setRefSearch(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "SubModel":
                                mAircraft.setSubModel(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "DefaultApp":
                                mAircraft.setDefaultApp(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "DefaultLaunch":
                                mAircraft.setDefaultLaunch(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Seats":
                                mAircraft.setSeats(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                                break;
                            default:
                                break;
                        }
                    }
                    mFragment.intAircraft++;
                    val += mFragment.delta;
                    if (val > 1) {
                        mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                        val = 0;
                    }
                    boolean b = SyncViewModel.addNewAircraft(mContext, mAircraft);
                    LogUtils.e(b + MCCPilotLogConst.STRING_EMPTY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    private void readTagAirfield() {
        AirfieldPilot mAirfield;
        String value;
        try {
            mAutoPilot.selectXPath("/dataroot/AF");
            if (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_AIRFIELDS);
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath("/dataroot/AF/airfield");
                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    mAirfield = new AirfieldPilot();
//                    mFragment.intAirfields++;//TuanPV comment
                    val += mFragment.delta;
                    if (val > 1) {
                        mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                        val = 0;
                    }
                    AutoPilot ap1 = new AutoPilot(mNavigation);
                    ap1.selectXPath(".*/
/*");

                    while (ap1.evalXPath() != -1) {
                        if (isCalcel)
                            break;
                        value = mNavigation.toNormalizedString(mNavigation.getText());
                        switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                            case "AFCode":
                                mAirfield.setAFCode(mNavigation.toNormalizedString(mNavigation.getText()));
                                break;
                            case "AFCountry":
                                mAirfield.setAFCountry(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFIata":
                                mAirfield.setAFIata(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFIcao":
                                mAirfield.setAFIcao(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFName":
                                mAirfield.setAFName(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Latitude":
                                mAirfield.setLatitude(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Longitude":
                                mAirfield.setLongitude(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Notes":
                                mAirfield.setNotes(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            default:
                                break;
                        }
                    }
                    mAirfield.setPC(SyncConst.PC);
                    boolean b = false;
                    if (mAirfield.getAFCode() != null && !mAirfield.getAFCode().equals(MCCPilotLogConst.ZERO_STRING)) {
                        b = SyncViewModel.addNewAirfield(mContext, mAirfield);
                    }
                    if (b) {
                        mFragment.intAirfields++;
                    }
                    LogUtils.e(b + MCCPilotLogConst.STRING_EMPTY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    */
/**
     * Read tag airfield. A4 is for big db
     *//*

    private void readTagA4Airfield() {
        Airfield mAirfield;
        String value;
        try {
            mAutoPilot.selectXPath("/dataroot/A4");
            if (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_AIRFIELDS);
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath("/dataroot/A4/airfield");
                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    mAirfield = new Airfield();
//                    mFragment.intAirfields++;//TuanPV comment
                    val += mFragment.delta;
                    if (val > 1) {
                        mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                        val = 0;
                    }
                    AutoPilot ap1 = new AutoPilot(mNavigation);
                    ap1.selectXPath(".*/
/*");

                    while (ap1.evalXPath() != -1) {
                        if (isCalcel)
                            break;
                        value = mNavigation.toNormalizedString(mNavigation.getText());
                        switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                            case "AFCode":
                                mAirfield.setAFCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFCountry":
                                mAirfield.setAFCountry(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                                break;
                            case "AFIata":
                                mAirfield.setAFIATA(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFIcao":
                                mAirfield.setAFICAO(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "AFName":
                                mAirfield.setAFName(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Latitude":
                                mAirfield.setLatitude(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Longitude":
                                mAirfield.setLongitude(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            default:
                                break;
                        }
                    }
                    boolean b = false;
                    if (mAirfield.getAFCode() != null && !mAirfield.getAFCode().equals(MCCPilotLogConst.ZERO_STRING)) {
                        b = SyncViewModel.addNewAirfieldToBigDb(mContext, mAirfield);
                    }
                    if (b) {
                        mFragment.intAirfields++;
                    }
                    LogUtils.e(b + MCCPilotLogConst.STRING_EMPTY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read tag currency
     *//*

    private void readTagCurrencies(VTDNav pVTDNav) {
        AutoPilot autoPilot = new AutoPilot(pVTDNav);
        Currencies mCurrencies;
        try {
            autoPilot.selectXPath("/dataroot/CU");
            if (autoPilot.evalXPath() != -1 && pVTDNav.getText() == -1) { */
/* getText() = -1 means no text *//*

                AutoPilot ap = new AutoPilot(pVTDNav);
                ap.selectXPath("/dataroot/CU/Currency");
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_CURRENCIES);
                */
/**
                 * Create 2 loops, outer loops all the currency parent tag, inside loops all currency attributes likes expdate, text, ...
                 *//*

                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    AutoPilot ap1 = new AutoPilot(pVTDNav);
                    ap1.selectXPath(".*/
/*");
                    mCurrencies = new Currencies();
                    mFragment.intCurrencies++;

                    while (ap1.evalXPath() != -1) {
                        if (isCalcel)
                            break;
                        switch (pVTDNav.toString(pVTDNav.getCurrentIndex())) {
                            case "text":
                                mCurrencies.setText(pVTDNav.toNormalizedString(pVTDNav.getText()));
                                break;
                            case "colorcode":
                                mCurrencies.setColorCode(pVTDNav.toNormalizedString(pVTDNav.getText()));
                                break;
                            case "expdate":
                                mCurrencies.setExpDate(pVTDNav.toNormalizedString(pVTDNav.getText()));
                                break;
                            case "warnperiod":
                                mCurrencies.setWarnPeriod(pVTDNav.toNormalizedString(mNavigation.getText()));
                                break;
                            default:
                                break;

                        }
                    }
                    StorageUtils.writeLongToSharedPref(mContext, MCCPilotLogConst.PREF_TIME_LAST_UPDATE, Calendar.getInstance().getTimeInMillis());
                    boolean b = SyncViewModel.addCurrencies(mContext, mCurrencies);
                    Utils.saveDateFlagCurrencyToMemory(mContext, false);
                    LogUtils.e(b + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        autoPilot.resetXPath();
    }

    */
/**
     * Method to read tag pilot
     *//*

    private void readTagPilot() {
        Pilot mPilot;
        String value;
        List<Pilot> mPilotList = new ArrayList<>();
        try {
            mAutoPilot.selectXPath("/dataroot/PT");
            if (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) { */
/* getText() = -1 means no text *//*

                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath("/dataroot/PT/pilot");
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_PILOT);
                */
/**
                 * Create 2 loops, outer loops all the currency parent tag, inside loops all currency attributes likes expdate, text, ...
                 *//*

                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    AutoPilot ap1 = new AutoPilot(mNavigation);
                    ap1.selectXPath(".*/
/*");
                    mPilot = new Pilot();
                    while (ap1.evalXPath() != -1) {
                        if (isCalcel)
                            break;
                        value = mNavigation.toNormalizedString(mNavigation.getText());
                        switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                            case "Company":
                                mPilot.setCompany(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "Notes":
                                mPilot.setNotes(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "PilotCode":
                                mPilot.setPilotCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "PilotEMail":
                                mPilot.setPilotEMail(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "PilotName":
                                mPilot.setPilotName(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "PilotPhone":
                                mPilot.setPilotPhone(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            case "PilotRef":
                                mPilot.setPilotRef(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                                break;
                            default:
                                break;
                        }
                    }

                    val += mFragment.delta;
                    if (val > 1) {
                        mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                        val = 0;
                    }
                    mPilot.setPC("PC");
                    boolean b = false;
                    if (mPilot.getPilotCode() != null && !mPilot.getPilotCode().equals(MCCPilotLogConst.ZERO_STRING)) {
                        b = SyncViewModel.syncPilot(mContext, mPilot);
                    }
                    if (b) {
                        mPilotList.add(mPilot);
                        mFragment.intPilots++;
                    }
                    LogUtils.e(b + MCCPilotLogConst.STRING_EMPTY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        if (DatabaseManager.getInstance(mContext).getSetting(SettingsConst.AUTO_EXPORT_PILOT).getData().equals(SettingsConst.SETTING_CHECKED)) {
            for (Pilot p : mPilotList) {
                exportToContact(p);
            }
        }

        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read tag flight
     *//*

    private void readTagFlight() {
        FlightPilot mFlight;
        String value;
        boolean isFirstFlight = false;
        try {
            mAutoPilot.selectXPath("//flight");
            while (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                if (isCalcel)
                    break;
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath(".*/
/*");
                mFlight = new FlightPilot();
                if (!isFirstFlight) {
                    isFirstFlight = true;
                    mFragment.sendStatusMessage(SyncStatus.PROCESSING_FLIGHT);
                }
                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    value = mNavigation.toNormalizedString(mNavigation.getText());
                    switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                        case "AircraftCode":
                            mFlight.setAircraftCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "ArrCode":
                            mFlight.setArrCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "ArrTime":
                            mFlight.setArrTime(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "Autoland":
                            mFlight.setAutoland(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "DepCode":
                            mFlight.setDepCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "DepTime":
                            mFlight.setDepTime(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "FlightCode":
                            mFlight.setFlightCode(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "FlightDate":
                            mFlight.setFlightDate(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "FlightNumber":
                            mFlight.setFlightNumber(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "FlightReport":
                            if (!TextUtils.isEmpty(value) && !value.equalsIgnoreCase(MCCPilotLogConst.NULL)) {
                                value = value.replaceAll("<", ":");
                                value = value.replaceAll(">", ":");
                            } else {
                                value = MCCPilotLogConst.STRING_EMPTY;
                            }
                            mFlight.setFlightReport(value);
                            break;
                        case "Holding":
                            mFlight.setHolding(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "minIMT":
                            mFlight.setMinIMT(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "LdgDay":
                            mFlight.setLdgDay(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "LdgNight":
                            mFlight.setLdgNight(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "LiftSW":
                            mFlight.setLiftSW(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "minCoP":
                            mFlight.setMinCoP(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minDual":
                            mFlight.setMinDual(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minExam":
                            mFlight.setMinExam(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minIFR":
                            mFlight.setMinIFR(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        //TuanPV begin add new
                        case "minREL":
                            mFlight.setMinREL(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value);
                            break;
                        //TuanPV end add new
                        case "minU1":
                            mFlight.setMinU1(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minU2":
                            mFlight.setMinU2(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minU3":
                            mFlight.setMinU3(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "minU4":
                            mFlight.setMinU4(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? "0" : value));
                            break;
                        case "userN1":
                            mFlight.setUserN1(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "userN2":
                            mFlight.setUserN2(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "userN3":
                            mFlight.setUserN3(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "minInstr":
                            mFlight.setMinInstr(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minNight":
                            mFlight.setMinNight(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minPIC":
                            mFlight.setMinPIC(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minPICus":
                            mFlight.setMinPICus(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minSFR":
                            mFlight.setMinSFR(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minTotal":
                            mFlight.setMinTotal(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "minXC":
                            mFlight.setMinXC(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "P1Code":
                            mFlight.setP1Code(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "P2Code":
                            mFlight.setP2Code(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "P3Code":
                            mFlight.setP3Code(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "P4Code":
                            mFlight.setP4Code(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "DepTimeSCH":
                            mFlight.setDepTimeSCH(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "ArrTimeSCH":
                            mFlight.setArrTimeSCH(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "PF":
                            Log.d("TASK_PF_PM",value);
                            if (value.equals("True") || value.equals("-1")) {
                                mFlight.setPF(MCCPilotLogConst.TASK_PF);
                                Log.d("TASK_PF_PM", "PF");
                            } else {
                                mFlight.setPF(MCCPilotLogConst.TASK_PM);
                            }
                            break;
                        case "Remarks":
                            if (!TextUtils.isEmpty(value) && !value.equalsIgnoreCase(MCCPilotLogConst.NULL)) {
                                value = value.replaceAll("<", ":");
                                value = value.replaceAll(">", ":");
                            } else {
                                value = MCCPilotLogConst.STRING_EMPTY;
                            }
                            mFlight.setRemarks(value);
                            break;
                        case "SignBox":
                            mFlight.setSignBox(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TODay":
                            mFlight.setTODay(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TONight":
                            mFlight.setTONight(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TypeOfApp1":
                            mFlight.setTypeOfApp1(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TypeOfApp2":
                            mFlight.setTypeOfApp2(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TypeOfApp3":
                            mFlight.setTypeOfApp3(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TypeOfInstr":
                            if (!TextUtils.isEmpty(value) && !value.equalsIgnoreCase(MCCPilotLogConst.NULL)) {
                                value = value.replaceAll("<", ":");
                                value = value.replaceAll(">", ":");
                            } else {
                                value = MCCPilotLogConst.STRING_EMPTY;
                            }
                            mFlight.setTypeOfInstr(value);
                            break;
                        case "TypeOfLaunch":
                            mFlight.setTypeOfLaunch(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "DeIce":
                            mFlight.setDeice(Integer.parseInt(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.ZERO_STRING : value));
                            break;
                        case "Delay":
                            mFlight.setDelay(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "Pax":
                            mFlight.setPax(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "Fuel":
                            mFlight.setFuel(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "FuelUsed":
                            mFlight.setFuelUsed(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TimeTO":
                            mFlight.setTimeTO(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        case "TimeLDG":
                            mFlight.setTimeLdg(value.equalsIgnoreCase(MCCPilotLogConst.NULL) ? MCCPilotLogConst.STRING_EMPTY : value);
                            break;
                        default:
                            break;
                    }
                }
                mFlight.setPC(SyncConst.PC);
                mFlight.setRecStatus(MCCPilotLogConst.REC_STATUS_3);
                boolean acceptedSaveFlight = SyncViewModel.acceptSaveFlight(mFlight.getFlightDate());
                if (!MCCPilotLogConst.ZERO_STRING.equals(mFlight.getFlightCode()) && acceptedSaveFlight) {
//                    SyncViewModel.addFlightFromPc(mFlight);//
                    SyncViewModel.addFlightFromPc(mContext, mFlight);//TuanPV add new
                    mFragment.intFlights++;
                    val += mFragment.delta;
                    if (val > 1) {
                        mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                        val = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }
        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read tag totals
     *//*

    private void readTagTotals() {
        Totals mTotals;
        String value;
        try {
            mAutoPilot.selectXPath("//totals");
            while (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                if (isCalcel)
                    break;
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath(".*/
/*");
                mTotals = new Totals();
                mFragment.sendStatusMessage(SyncStatus.UPDATING_TOTALS);

                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    value = mNavigation.toNormalizedString(mNavigation.getText());
                    switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                        case "sumPIC":
                            mTotals.setSumPIC(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumCoP":
                            mTotals.setSumCoP(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPICus":
                            mTotals.setSumPICus(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumDual":
                            mTotals.setSumDual(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumInstr":
                            mTotals.setSumInstr(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumExam":
                            mTotals.setSumExam(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumIFR":
                            mTotals.setSumIFR(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumSFR":
                            mTotals.setSumSFR(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumIMT":
                            mTotals.setSumIMT(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumREL":
                            mTotals.setSumREL(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumNight":
                            mTotals.setSumNight(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumXC":
                            mTotals.setSumXC(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumU1":
                            mTotals.setSumU1(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumU2":
                            mTotals.setSumU2(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumU3":
                            mTotals.setSumU3(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumU4":
                            mTotals.setSumU4(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumClass1":
                            mTotals.setSumClass1(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumClass2":
                            mTotals.setSumClass2(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumClass3":
                            mTotals.setSumClass3(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumClass4":
                            mTotals.setSumClass4(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumClass5":
                            mTotals.setSumClass5(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower0":
                            mTotals.setSumPower0(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower1":
                            mTotals.setSumPower1(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower2":
                            mTotals.setSumPower2(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower3":
                            mTotals.setSumPower3(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower4":
                            mTotals.setSumPower4(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower5":
                            mTotals.setSumPower5(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumPower6":
                            mTotals.setSumPower6(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumTODay":
                            mTotals.setSumTODay(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumTONight":
                            mTotals.setSumTONight(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumLdgDay":
                            mTotals.setSumLdgDay(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumLdgNight":
                            mTotals.setSumLdgNight(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumHolding":
                            mTotals.setSumHolding(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumAutoland":
                            mTotals.setSumAutoland(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        default:
                            break;
                    }
                }
                mTotals.setTotalsID(1);
                SyncViewModel.updateTotalsFromPc(mContext, mTotals);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read tag grand totals.
     *//*

    private void readTagGrandTotals() {
        Totals mTotals = DatabaseManager.getInstance(mContext).getTotals();
        String value;
        try {
            mAutoPilot.selectXPath("//grandtotals");
            while (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                if (isCalcel)
                    break;
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath(".*/
/*");
                mFragment.sendStatusMessage(SyncStatus.UPDATING_GRAND_TOTALS);

                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    value = mNavigation.toNormalizedString(mNavigation.getText());
                    switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                        case "sumACFT":
                            mTotals.setSumACFT(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        case "sumSIM":
                            mTotals.setSumSIM(MCCPilotLogConst.NULL.equalsIgnoreCase(value) ? 0 : Integer.parseInt(value));
                            break;
                        default:
                            break;
                    }
                }
                mTotals.setTotalsID(1);
                SyncViewModel.updateTotalsFromPc(mContext, mTotals);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    */
/**
     * Method to read logbook tag
     *//*

    private void readTagLogbook() {
        String value;
        String logbookFlights = MCCPilotLogConst.STRING_EMPTY;
        List<String> logbookFlightList = new ArrayList<>();
        try {
            AutoPilot apHasData = new AutoPilot(mNavigation);
            apHasData.selectXPath("//LB");
            if (apHasData.evalXPath() != -1 && mNavigation.getText() == -1) {
                mFragment.sendStatusMessage(SyncStatus.PROCESSING_LOGBOOK_FLIGHTS);
            }
            mAutoPilot.selectXPath("//logbook");
            while (mAutoPilot.evalXPath() != -1 && mNavigation.getText() == -1) {
                if (isCalcel)
                    break;
                AutoPilot ap = new AutoPilot(mNavigation);
                ap.selectXPath(".*/
/*");

                while (ap.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    value = mNavigation.toNormalizedString(mNavigation.getText());
                    if (!value.equalsIgnoreCase("No Records")) {
                        switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                            case "FR0":
                                logbookFlights += value + "|";
                                break;
                            case "FR1":
                                logbookFlights += value + "|";
                                break;
                            case "FR2":
                                logbookFlights += value + "|";
                                break;
                            case "FR3":
                                logbookFlights += value;
                                logbookFlightList.add(logbookFlights);
                                mFragment.intLogbook++;
                                logbookFlights = MCCPilotLogConst.STRING_EMPTY;
                                val += mFragment.delta;
                                if (val > 1) {
                                    mFragment.sendStatusMessage(SyncStatus.UPDATE_PROGRESS);
                                    val = 0;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
//                SyncViewModel.addLogbookFromPc(mContext, logbookFlightList, version);
            }
            //TuanPV add new
            if (!logbookFlightList.isEmpty()) {
                LogUtils.e("size logbookFlightList: " + logbookFlightList.size());
                SyncViewModel.addLogbookFromPc(mContext, logbookFlightList, version);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }

        mAutoPilot.resetXPath();
    }

    private void readTagCalendar() {
        boolean isFirstCalendar = false;
        ArrayList<AccountCalendarModel> listAccountCalendarModel = new ArrayList<>();
        AccountCalendarModel mAccountCalendar;
        String value = MCCPilotLogConst.STRING_EMPTY;
        try {
            mAutoPilot.selectXPath("//iCal");
            while (mAutoPilot.evalXPath() != -1) {
                if (isCalcel)
                    break;
                if (!isFirstCalendar) {
                    isFirstCalendar = true;
                    mFragment.sendStatusMessage(SyncStatus.PROCESSING_ICAL_DATA);
                }
                AutoPilot ap1 = new AutoPilot(mNavigation);
                ap1.selectXPath(".*/
/*");
                mAccountCalendar = new AccountCalendarModel();

                while (ap1.evalXPath() != -1) {
                    if (isCalcel)
                        break;
                    if (mNavigation.getText() != -1) {
                        value = mNavigation.toNormalizedString(mNavigation.getText());
                    }
                    switch (mNavigation.toString(mNavigation.getCurrentIndex())) {
                        case "subject":
                            mAccountCalendar.setSubject(value);
                            break;
                        case "body":
                            mAccountCalendar.setBody(value + "#mccPILOTLOG");
                            String newValue = MCCPilotLogConst.STRING_EMPTY;
                            if (!value.equals(MCCPilotLogConst.STRING_EMPTY)) {
                                value = value.replace("|||||", "||");
                                value = value.replace("||||", "||");
                                value = value.replace("|||", "||");
                                String result[] = value.split("[|]");
                                for (String r : result) {
                                    r += "\n";
                                    newValue = newValue + r;
                                }
                            }
                            mAccountCalendar.setBody(newValue + "\n#mccPILOTLOG");
                            break;
                        case "startdate":
                            mAccountCalendar.setStartdate(value);
                            break;
                        case "starttime":
                            mAccountCalendar.setStarttime(value);
                            break;
                        case "duration":
                            mAccountCalendar.setDuration(value);
                            break;
                        case "category":
                            mAccountCalendar.setCategory(value);
                            break;
                        case "location": */
/*location maybe a closed tag *//*

                            mAccountCalendar.setLocation(mNavigation.getText() != -1 ? value : MCCPilotLogConst.STRING_EMPTY);
                            break;
                        case "reminder":
                            mAccountCalendar.setReminder(value);
                            break;
                        case "remindersound":
                            mAccountCalendar.setRemindersound(value);
                            break;
                        default:
                            break;
                    }
                }

                mFragment.intICal++;
                listAccountCalendarModel.add(mAccountCalendar);
//                if (TextUtils.isEmpty(DatabaseManager.getInstance(mContext).getSetting(SettingsConst.DEFAULT_CALENDAR).getData())) {
//                    mFragment.sendStatusMessage(SyncStatus.ERROR_CALENDAR_NO_NAME);
//                } else {
//                    new CalendarAccess(mContext).makeNewCalendarEntry(mAccountCalendar);
//                }
            }

            if (!listAccountCalendarModel.isEmpty()) {
                Setting setting = DatabaseManager.getInstance(mContext).getSetting(SettingsConst.DEFAULT_CALENDAR);
                String defaultCalender = "";
                if (setting != null) {
                    defaultCalender = setting.getData();
                }
                if (TextUtils.isEmpty(defaultCalender)) {
                    mFragment.sendStatusMessage(SyncStatus.ERROR_CALENDAR_NO_NAME);
                } else {
                    //Delete all event if exist from start date
                    long startTime, endTime;
                    Calendar startCal = Calendar.getInstance(TimeZone.getDefault());
//                try {
//                startCal.setTime(sdf.parse(listAccountCalendarModel.get(0).getStartdate()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                    startCal.set(Calendar.HOUR, 0);
                    startCal.set(Calendar.MINUTE, 0);
                    startCal.set(Calendar.MILLISECOND, 0);
                    startTime = startCal.getTimeInMillis();

                    //Sort asc
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
                    if (listAccountCalendarModel.size() >= 2) {
                        Collections.sort(listAccountCalendarModel, new Comparator<AccountCalendarModel>() {
                            @Override
                            public int compare(AccountCalendarModel left, AccountCalendarModel right) {
                                Calendar calendarLeft = Calendar.getInstance();
                                Calendar calendarRight = Calendar.getInstance();
                                try {
                                    calendarLeft.setTime(sdf.parse(left.getStartdate()));
                                    calendarRight.setTime(sdf.parse(right.getStartdate()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return calendarLeft.getTime().compareTo(calendarRight.getTime());
                            }
                        });
                    }
                    try {
                        startCal.setTime(sdf.parse(listAccountCalendarModel.get(listAccountCalendarModel.size() - 1).getStartdate()));
                        startCal.add(Calendar.DATE, 8);//until 7 days after the last event.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startCal.set(Calendar.HOUR, 0);
                    startCal.set(Calendar.MINUTE, 0);
                    startCal.set(Calendar.MILLISECOND, 0);
                    endTime = startCal.getTimeInMillis();

                    CalendarAccess calendarAccess = new CalendarAccess(mContext);
                    if (calendarAccess.getCalendarId() != 0) {
                        CalendarAccess.deleteEventsFromGoogleCalendar(mContext, calendarAccess.getEventCalendar(startTime, endTime, calendarAccess.getCalendarId()));
                    } else {
                        Utils.showToast(mContext, R.string.calendar_not_found);
                    }

                    //Export to OS Calendar
                    for (AccountCalendarModel model : listAccountCalendarModel) {
                        new CalendarAccess(mContext).makeNewCalendarEntry(model);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isErrorEOF = true;
        }
        mAutoPilot.resetXPath();
    }

    public ArrayList<String[]> createUploadFile(Context pContext, String fileName, int logNumber, boolean bNotComplete) {
        Log.d("LOG","Start upload file");
        List<FlightPilot> flightsToSync = SyncViewModel.getAllFlightToSync(pContext, bNotComplete);
        List<Pilot> pilotsToSync = SyncViewModel.getAllPilotToSync(pContext, false);//PHONE
        List<AirfieldPilot> airfieldsToSync = SyncViewModel.getAllAirfieldToSync(pContext);//PHONE

        List<Pilot> pilotsWithPicturesToSync = SyncViewModel.getAllPilotToSync(pContext, true);//get pictures
        List<String> signaturePicturesToSync = SyncViewModel.getAllSignatures(pContext);//pictures

        StringBuilder xmlFile = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<dataroot>\r\n<version>" + Integer.toString(SyncV3Fragment.CURRENT_XML_VERSION) + "</version>\r\n");
        Log.d("LOG","Start upload file 1");
        if (flightsToSync.isEmpty()) {
            xmlFile.append("<flight>\r\n");
            xmlFile.append("No Records\r\n");
            xmlFile.append("</flight>\r\n");
        }
        Log.d("LOG","Start upload file 2");
        Log.d("LOG",String.valueOf(flightsToSync.size()));
        for (FlightPilot flight : flightsToSync) {
            Log.d("LOG","Start upload file 3");
            xmlFile.append("<flight>\r\n");
            xmlFile.append(String.format("<FlightCode>%s</FlightCode>\r\n",
                    TextUtils.isEmpty(flight.getFlightCode()) ? MCCPilotLogConst.NULL : flight.getFlightCode()));
            xmlFile.append(String.format("<FlightDate>%s</FlightDate>\r\n",
                    TextUtils.isEmpty(flight.getFlightDate()) ? MCCPilotLogConst.NULL : flight.getFlightDate()));
            Aircraft aircraft = DatabaseManager.getInstance(pContext).getAircraftByAircraftCode(flight.getAircraftCode());
            if (aircraft != null && aircraft.getClassAircraft() == -1) {
                xmlFile.append(String.format("<AircraftCode>%s</AircraftCode>\r\n",
                        TextUtils.isEmpty(aircraft.getReference()) ? MCCPilotLogConst.NULL : aircraft.getReference()));
            } else {
                xmlFile.append(String.format("<AircraftCode>%s</AircraftCode>\r\n",
                        TextUtils.isEmpty(flight.getAircraftCode()) ? MCCPilotLogConst.NULL : flight.getAircraftCode()));
            }
            xmlFile.append(String.format("<DepCode>%s</DepCode>\r\n",
                    TextUtils.isEmpty(flight.getDepCode()) ? "0" : flight.getDepCode()));
            xmlFile.append(String.format("<ArrCode>%s</ArrCode>\r\n",
                    TextUtils.isEmpty(flight.getArrCode()) ? "0" : flight.getArrCode()));
            xmlFile.append(String.format("<FlightNumber>%s</FlightNumber>\r\n", TextUtils.isEmpty(flight.getFlightNumber()) ||
                    MCCPilotLogConst.ZERO_STRING.equalsIgnoreCase(flight.getFlightNumber()) ? MCCPilotLogConst.NULL : flight.getFlightNumber()));

            xmlFile.append(String.format("<DepTime>%s</DepTime>\r\n", TextUtils.isEmpty(flight.getDepTime()) ? MCCPilotLogConst.NULL : flight.getDepTime()));
            xmlFile.append(String.format("<ArrTime>%s</ArrTime>\r\n", TextUtils.isEmpty(flight.getArrTime()) ? MCCPilotLogConst.NULL : flight.getArrTime()));
            if (flight.getDepTimeSCH() != null) {
                xmlFile.append(String.format("<DepTimeSCH>%s</DepTimeSCH>\r\n",
                        flight.getDepTimeSCH().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : flight.getDepTimeSCH()));
            }
            if (flight.getArrTimeSCH() != null) {
                xmlFile.append(String.format("<ArrTimeSCH>%s</ArrTimeSCH>\r\n",
                        flight.getArrTimeSCH().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : flight.getArrTimeSCH()));
            }
            if (flight.getMinTotal() != null) {
                xmlFile.append(String.format("<minTotal>%s</minTotal>\r\n", flight.getMinTotal()));
            } else {
                xmlFile.append(String.format("<minTotal>%s</minTotal>\r\n", MCCPilotLogConst.NULL));
            }
            if (flight.getMinPIC() != null) {
                xmlFile.append(String.format("<minPIC>%s</minPIC>\r\n", flight.getMinPIC()));
            } else {
                xmlFile.append(String.format("<minPIC>%s</minPIC>\r\n", MCCPilotLogConst.NULL));
            }

            xmlFile.append(String.format("<minCoP>%s</minCoP>\r\n", flight.getMinCoP()));
            xmlFile.append(String.format("<minDual>%s</minDual>\r\n", flight.getMinDual()));
            xmlFile.append(String.format("<minInstr>%s</minInstr>\r\n", flight.getMinInstr()));
            xmlFile.append(String.format("<minPICus>%s</minPICus>\r\n", flight.getMinPICus()));
            xmlFile.append(String.format("<minExam>%s</minExam>\r\n", flight.getMinExam()));
            if (flight.getMinXC() != null) {   //fixed null
                xmlFile.append(String.format("<minXC>%s</minXC>\r\n", flight.getMinXC()));
            } else {
                xmlFile.append(String.format("<minXC>%s</minXC>\r\n", MCCPilotLogConst.NULL));
            }
            if (flight.getMinNight() != null) {
                xmlFile.append(String.format("<minNight>%s</minNight>\r\n", flight.getMinNight()));
            } else {
                xmlFile.append(String.format("<minNight>%s</minNight>\r\n", MCCPilotLogConst.NULL));
            }
            xmlFile.append(String.format("<minIFR>%s</minIFR>\r\n", flight.getMinIFR()));
            xmlFile.append(String.format("<minSFR>%s</minSFR>\r\n", flight.getMinSFR()));


            xmlFile.append(String.format("<minU1>%s</minU1>\r\n", flight.getMinU1()));


            xmlFile.append(String.format("<minU2>%s</minU2>\r\n", flight.getMinU2()));


            xmlFile.append(String.format("<minU3>%s</minU3>\r\n", flight.getMinU3()));


            xmlFile.append(String.format("<minU4>%s</minU4>\r\n", flight.getMinU4()));


            xmlFile.append(String.format("<userN1>%s</userN1>\r\n", TextUtils.isEmpty(flight.getUserN1())
                    || flight.getUserN1().equalsIgnoreCase("0") ? MCCPilotLogConst.NULL : flight.getUserN1()));


            xmlFile.append(String.format("<userN2>%s</userN2>\r\n", TextUtils.isEmpty(flight.getUserN2())
                    || flight.getUserN2().equalsIgnoreCase("0") ? MCCPilotLogConst.NULL : flight.getUserN2()));


            xmlFile.append(String.format("<userN3>%s</userN3>\r\n", TextUtils.isEmpty(flight.getUserN3()) ? "0" : flight.getUserN3()));


            xmlFile.append(String.format("<DeIce>%s</DeIce>\r\n", flight.getDeice()));


            xmlFile.append(String.format("<Delay>%s</Delay>\r\n", TextUtils.isEmpty(flight.getDelay()) ? MCCPilotLogConst.NULL : flight.getDelay()));
            Log.d("LOG DELAY", TextUtils.isEmpty(flight.getDelay()) ? MCCPilotLogConst.NULL : flight.getDelay());

            xmlFile.append(String.format("<Pax>%s</Pax>\r\n", TextUtils.isEmpty(flight.getPax()) ? MCCPilotLogConst.NULL : flight.getPax()));
            Log.d("LOG PAX", TextUtils.isEmpty(flight.getPax()) ? MCCPilotLogConst.NULL : flight.getPax());


            xmlFile.append(String.format("<Fuel>%s</Fuel>\r\n", TextUtils.isEmpty(flight.getFuel()) ? MCCPilotLogConst.NULL : flight.getFuel()));


            xmlFile.append(String.format("<FuelUsed>%s</FuelUsed>\r\n", TextUtils.isEmpty(flight.getFuelUsed()) ? MCCPilotLogConst.NULL : flight.getFuelUsed()));


            xmlFile.append(String.format("<minIMT>%s</minIMT>\r\n", (TextUtils.isEmpty(flight.getMinIMT()) ? MCCPilotLogConst.NULL : flight.getMinIMT())));
            xmlFile.append(String.format("<minREL>%s</minREL>\r\n", (TextUtils.isEmpty(flight.getMinREL()) ? MCCPilotLogConst.NULL : flight.getMinREL())));

            xmlFile.append(String.format("<LiftSW>%s</LiftSW>\r\n", TextUtils.isEmpty(flight.getLiftSW()) ? MCCPilotLogConst.NULL : flight.getLiftSW()));

            if (flight.getPF() == MCCPilotLogConst.TASK_PF) {
                xmlFile.append(String.format("<TODay>%s</TODay>\r\n", TextUtils.isEmpty(flight.getTODay()) ? MCCPilotLogConst.NULL : flight.getTODay()));
                xmlFile.append(String.format("<TONight>%s</TONight>\r\n", TextUtils.isEmpty(flight.getTONight()) ? MCCPilotLogConst.NULL : flight.getTONight()));
                xmlFile.append(String.format("<LdgDay>%s</LdgDay>\r\n", TextUtils.isEmpty(flight.getLdgDay()) ? MCCPilotLogConst.NULL : flight.getLdgDay()));
                xmlFile.append(String.format("<LdgNight>%s</LdgNight>\r\n", TextUtils.isEmpty(flight.getLdgNight()) ? MCCPilotLogConst.NULL : flight.getLdgNight()));
            } else {
                xmlFile.append(String.format("<TODay>%s</TODay>\r\n", TextUtils.isEmpty(flight.getTODay()) ? MCCPilotLogConst.NULL : MCCPilotLogConst.ZERO_STRING));
                xmlFile.append(String.format("<TONight>%s</TONight>\r\n", TextUtils.isEmpty(flight.getTONight()) ? MCCPilotLogConst.NULL : MCCPilotLogConst.ZERO_STRING));
                xmlFile.append(String.format("<LdgDay>%s</LdgDay>\r\n", TextUtils.isEmpty(flight.getLdgDay()) ? MCCPilotLogConst.NULL : MCCPilotLogConst.ZERO_STRING));
                xmlFile.append(String.format("<LdgNight>%s</LdgNight>\r\n", TextUtils.isEmpty(flight.getLdgNight()) ? MCCPilotLogConst.NULL : MCCPilotLogConst.ZERO_STRING));
            }
            xmlFile.append(String.format("<Autoland>%s</Autoland>\r\n", TextUtils.isEmpty(flight.getAutoland()) ? MCCPilotLogConst.NULL : flight.getAutoland()));
            xmlFile.append(String.format("<Holding>%s</Holding>\r\n", TextUtils.isEmpty(flight.getHolding()) ? MCCPilotLogConst.NULL : flight.getHolding()));
            xmlFile.append(String.format("<TypeOfInstr><![CDATA[%s]]></TypeOfInstr>\r\n", TextUtils.isEmpty(flight.getTypeOfInstr()) ? MCCPilotLogConst.NULL : flight.getTypeOfInstr()));

            xmlFile.append(String.format("<TypeOfApp1>%s</TypeOfApp1>\r\n", TextUtils.isEmpty(flight.getTypeOfApp1()) ? MCCPilotLogConst.NULL : flight.getTypeOfApp1()));

            xmlFile.append(String.format("<TypeOfApp2>%s</TypeOfApp2>\r\n", TextUtils.isEmpty(flight.getTypeOfApp2()) ? MCCPilotLogConst.NULL : flight.getTypeOfApp2()));

            xmlFile.append(String.format("<TypeOfApp3>%s</TypeOfApp3>\r\n", TextUtils.isEmpty(flight.getTypeOfApp3()) ? MCCPilotLogConst.NULL : flight.getTypeOfApp3()));
            xmlFile.append(String.format("<TypeOfLaunch>%s</TypeOfLaunch>\r\n", TextUtils.isEmpty(flight.getTypeOfLaunch()) ? MCCPilotLogConst.NULL : flight.getTypeOfLaunch()));

            xmlFile.append(String.format("<P1Code>%s</P1Code>\r\n", TextUtils.isEmpty(flight.getP1Code()) ? MCCPilotLogConst.NULL : flight.getP1Code()));
            xmlFile.append(String.format("<P2Code>%s</P2Code>\r\n", TextUtils.isEmpty(flight.getP2Code()) ? MCCPilotLogConst.NULL : flight.getP2Code()));
            xmlFile.append(String.format("<P3Code>%s</P3Code>\r\n", TextUtils.isEmpty(flight.getP3Code()) ? MCCPilotLogConst.NULL : flight.getP3Code()));
            xmlFile.append(String.format("<P4Code>%s</P4Code>\r\n", TextUtils.isEmpty(flight.getP4Code()) ? MCCPilotLogConst.NULL : flight.getP4Code()));

            xmlFile.append(String.format("<PF>%d</PF>\r\n", flight.getPF()));

            xmlFile.append(String.format("<Remarks><![CDATA[%s]]></Remarks>\r\n", TextUtils.isEmpty(flight.getRemarks()) ? MCCPilotLogConst.NULL : flight.getRemarks()));
            xmlFile.append(String.format("<FlightReport><![CDATA[%s]]></FlightReport>\r\n", TextUtils.isEmpty(flight.getFlightReport()) ? MCCPilotLogConst.NULL : flight.getFlightReport()));


            xmlFile.append(String.format("<TimeTO>%s</TimeTO>\r\n", TextUtils.isEmpty(flight.getTimeTO()) ? MCCPilotLogConst.NULL : flight.getTimeTO()));

            xmlFile.append(String.format("<TimeLDG>%s</TimeLDG>\r\n", TextUtils.isEmpty(flight.getTimeLdg()) ? MCCPilotLogConst.NULL : flight.getTimeLdg()));


            xmlFile.append(String.format("<SignBox>%s</SignBox>\r\n", TextUtils.isEmpty(flight.getSignBox()) ? MCCPilotLogConst.NULL : flight.getSignBox()));
            xmlFile.append("</flight>\r\n");
        }

        if (pilotsToSync.isEmpty()) {
            xmlFile.append("<pilot>\r\n");
            xmlFile.append("No Records\r\n");
            xmlFile.append("</pilot>\r\n");
        } else {
            for (Pilot pilot : pilotsToSync) {
                xmlFile.append("<pilot>\r\n");
                xmlFile.append(String.format("<PilotCode>%s</PilotCode>\r\n", pilot.getPilotCode() == null ||
                        pilot.getPilotCode().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getPilotCode()));
                xmlFile.append(String.format("<Company>%s</Company>\r\n", pilot.getCompany() == null ||
                        pilot.getCompany().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getCompany()));
                xmlFile.append(String.format("<PilotRef>%s</PilotRef>\r\n", pilot.getPilotRef() == null ||
                        pilot.getPilotRef().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getPilotRef()));
                xmlFile.append(String.format("<PilotName><![CDATA[%s]]></PilotName>\r\n", pilot.getPilotName() == null ||
                        pilot.getPilotName().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getPilotName()));
                xmlFile.append(String.format("<PilotPhone>%s</PilotPhone>\r\n", pilot.getPilotPhone() == null ||
                        pilot.getPilotPhone().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getPilotPhone()));
                xmlFile.append(String.format("<PilotEMail>%s</PilotEMail>\r\n", pilot.getPilotEMail() == null ||
                        pilot.getPilotEMail().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getPilotEMail()));
                xmlFile.append(String.format("<Notes><![CDATA[%s]]></Notes>\r\n", pilot.getNotes() == null ||
                        pilot.getNotes().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : pilot.getNotes()));
                xmlFile.append("</pilot>\r\n");
            }
        }
        if (airfieldsToSync.isEmpty()) {
            xmlFile.append("<airfield>\r\n");
            xmlFile.append("No Records\r\n");
            xmlFile.append("</airfield>\r\n");
        } else {
            for (AirfieldPilot airfield : airfieldsToSync) {
                xmlFile.append("<airfield>\r\n");
                xmlFile.append(String.format("<AFCode>%s</AFCode>\r\n", airfield.getAFCode() == null ||
                        airfield.getAFCode().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : airfield.getAFCode()));
                xmlFile.append(String.format("<Notes><![CDATA[%s]]></Notes>\r\n", airfield.getNotes() == null ||
                        airfield.getNotes().equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY) ? MCCPilotLogConst.NULL : airfield.getNotes()));
                xmlFile.append("</airfield>\r\n");
            }
        }
        xmlFile.append("</dataroot>\r\n");
        final String xmlStr = xmlFile.toString();
        ArrayList<String[]> pictureList = new ArrayList<>();
        try {
            File rootDir = new File(StorageUtils.getStorageRootFolder(pContext));

            for (Pilot pilot : pilotsWithPicturesToSync) {
//                final String localImageName = String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V3, pilot.getPilotCode());//pilot.
                final String localImageName = String.format(MCCPilotLogConst.PILOT_IMAGE_FILE_NAME_V4, pilot.getPilotCode());//pilot.
                String dir = rootDir.getAbsolutePath() + "/";
                String fileNamePic = dir + localImageName;
                File pilotImgFile = new File(fileNamePic);
                boolean exist = pilotImgFile.exists();

                if (exist) {
                    //final String imgName = String.format("mobile.%s.img.%s.jpg", DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData(), pilot.getPilotCode());
                    final String imgName = String.format("img.%s.jpg", pilot.getPilotCode());
                    String[] imgStr = {fileNamePic, imgName, localImageName};
                    pictureList.add(imgStr);
                }
            }
            for (String sign : signaturePicturesToSync) {
                String fileNamePic = rootDir.getAbsolutePath() + File.separator + pContext.getString(R.string.sign_folder) + File.separator + sign;
                File signImgFile = new File(fileNamePic);
                if (signImgFile.exists()) {
                    //final String imgName = String.format("mobile.%s.%s", DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData(), sign);
                    final String imgName = String.format("mobile.%s", sign);
                    String[] imgStr = {fileNamePic, imgName, sign};
                    pictureList.add(imgStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flightsToSync.isEmpty() || !pilotsToSync.isEmpty() || !airfieldsToSync.isEmpty()) {
            File file = new File(StorageUtils.getStorageRootFolder(pContext), fileName);
            File xmlFolder = new File(StorageUtils.getStorageRootFolder(pContext) + File.separator + MCCPilotLogConst.XML_FOLDER);
            if (!xmlFolder.exists()) {
                xmlFolder.mkdir();
            }
            File logFile = new File(StorageUtils.getStorageRootFolder(pContext) + File.separator + MCCPilotLogConst.XML_FOLDER, fileName);
            try {
                file.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(xmlStr);
                out.close();
            } catch (IOException ignored) {
            }
            try {

                final int numberOfLogFile = StorageUtils.getIntFromSharedPref(pContext, StateKey.LAST_SYNC_LOG_FILE, 0);
                {
                    if (numberOfLogFile >= 20) {
                        File f = new File(StorageUtils.getStorageRootFolder(pContext) + File.separator + MCCPilotLogConst.XML_FOLDER);
                        if (f.exists() && f.isDirectory()) {
                            while (f.listFiles().length < 20) {
                                f.listFiles()[0].delete();
                            }
                        }
                    }
                }
                StorageUtils.writeIntToSharedPref(pContext, StateKey.LAST_SYNC_LOG_FILE, logNumber);
                logFile.createNewFile();
                BufferedWriter outL = new BufferedWriter(new FileWriter(logFile));
                outL.write(xmlStr);
                outL.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pictureList;
    }


    private boolean exportToContact(Pilot pPilot) {
        if (pPilot == null) {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (!TextUtils.isEmpty(pPilot.getPilotName())) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            pPilot.getPilotName()).build());
        }

        //------------------------------------------------------ Work Numbers
        if (!TextUtils.isEmpty(pPilot.getPilotPhone())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, pPilot.getPilotPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (!TextUtils.isEmpty(pPilot.getPilotEMail())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, pPilot.getPilotEMail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!TextUtils.isEmpty(pPilot.getCompany())) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, pPilot.getCompany())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, MCCPilotLogConst.STRING_EMPTY)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
*/
