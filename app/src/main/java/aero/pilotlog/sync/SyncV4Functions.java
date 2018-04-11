/*
package net.mcc3si.sync;

import android.content.Context;

import net.mcc3si.common.SettingsConst;
import net.mcc3si.databases.manager.DatabaseManager;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

*/
/**
 * Created by tuan.na on 8/27/2015.
 * API Functions of Sync v4
 *//*

public class SyncV4Functions {

   */
/* private String deviceId;
    private String userId;
    private String syncId;

    public SyncV4Functions(Context pContext){
      *//*
*/
/*  if (deviceId == null){
            deviceId = DatabaseManager.getInstance(pContext).getSetting(SettingsConst.SYNC_ID).getData();
        }
        if (userId == null){
            userId = deviceId.substring(0, deviceId.length() - 4);
        }
        if (syncId == null){
            syncId = deviceId.substring(deviceId.length() - 3, deviceId.length());
        }*//*
*/
/*
    }

    public String getDateTime() {

        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);

        return sdf.format(calendar.getTime());
    }


    public String clearFile(String fileName) {
        final String sFixed = "880909";

        final String sDT = getDateTime();
        final String cF = "mobile";
        final String url_CS = calcCS2(userId, sDT, fileName, sFixed);

        return "http://www.mccCLOUD.net/mccPILOTLOG/clearFile.asp?CF=" + cF +
                "&ID=" + userId + "&FN=" + fileName + "&DT=" + sDT + "&CS="
                + url_CS;

    }

    public String deleteXML(String fileName) {

        final String sDT = getDateTime();

        final String sFixed = "979695";
        final String url_CS = calcCS2(userId, sDT, fileName, sFixed);

        return "http://www.mccCLOUD.net/mccPILOTLOG/deleteXML.asp?ID=" +
                userId + "&FN=" + fileName + "&DT=" + sDT + "&CS="
                + url_CS;

    }

    public String getTablesID() {
        final String sDT = getDateTime();
        final String sFixed = "313703";
        final String url_CS = calcCS1(userId, sDT, sFixed);
        return "http://www.mccCLOUD.net/mccPILOTLOG/getTablesID.asp?ID=" +
                userId + "&SI=" + syncId + "&DT=" + sDT + "&CS="
                + url_CS;
    }


    public String setTablesID(String fC, String pC) {

        final String sDT = getDateTime();
        final String sFixed = "313700";
        final String url_CS = calcCS3(userId, sDT, syncId, sFixed);

        return "http://www.mccCLOUD.net/mccPILOTLOG/setTablesID.asp?ID=" +
                userId + "&SI=" + syncId + "&DT=" + sDT + "&FC=" + fC + "&PC=" + pC + "&CS="
                + url_CS;

    }


    public String getFile(String fileName) {
        final String sDT = getDateTime();
        final String cF = "mobile";
        final String sFixed = "516041";
        final String url_CS = calcCS2(userId, sDT, fileName, sFixed);
        return "http://www.mccCLOUD.net/mccPILOTLOG/getFile.asp?CF=" + cF
                + "&ID=" + userId + "&FN=" + fileName + "&DT=" + sDT + "&CS="
                + url_CS;

    }

    public String storeFile(String fileName) {
        final String sDT = getDateTime();
        final String cF = "mobile";

        final String sFixed = "960524";
        final String url_CS = calcCS2(userId, sDT, fileName, sFixed);
        return "http://www.mccCLOUD.net/mccPILOTLOG/storeFile.asp?CF=" + cF
                + "&ID=" + userId + "&FN=" + fileName + "&DT=" + sDT + "&CS="
                + url_CS;
    }

    public String listXML() {
        final String sDT = getDateTime();
        final String cF = "mobile";
        final String otherAP = "pc.";//
        final String sFixed = "260614";
        final String url_CS = calcCS3(userId, sDT, syncId, sFixed);

        return "http://www.mccCLOUD.net/mccPILOTLOG/listXML.asp?CF=" + cF
                + "&ID=" + userId + "&TP=" + otherAP + "&SI=" + syncId + "&DT=" + sDT + "&CS="
                + url_CS;
    }

    public String listDirectory(String dirName) {
        final String sDT = getDateTime();

        final String sFixed = "241296";
        final String url_CS = calcCS2(userId, sDT, dirName, sFixed);

        return "http://www.mccCLOUD.net/mccPILOTLOG/listDirectory.asp?ID="
                + userId + "&DT=" + sDT + "&DR=" + dirName + "&CS=" + url_CS;
    }

    public static String calcCS1(String sID, String sDT, String sFix) {

        String a = (sDT.substring(sDT.length() - 5, sDT.length() - 2));// - 4- 1
        int ai = getInt2(a);
        //(Asc
        int b1 = getInt(sDT.substring(sDT.length() - 1)) + 1;
        String b = (sID.substring(b1 - 1, b1));
        byte[] bb = EncodingUtils.getAsciiBytes(b);
        int bi = bb[0];
        int ci = 33;
        String d = sFix.substring(0, 1);
        int di = getInt(d);
        String e = sDT.substring(6, 6 + 2);
        int ei = getInt2(e) * 7;
        String f = sDT.substring(sDT.length() - 2);
        int fi = getInt2(f) * 2;
        int k = getInt2(sFix);
        int resd = ai * bi * ci * di + ei + fi + k;
        final String res = Integer.toString(resd);
        return res;
    }

    public static String calcCS2(String sID, String sDT, String sFN, String sFix) {
        String a = (sDT.substring(sDT.length() - 5, sDT.length() - 2));// - 4- 1
        int ai = getInt2(a);
        //(Asc
        int b1 = getInt(sDT.substring(sDT.length() - 1)) + 1;
        String b = (sID.substring(b1 - 1, b1));
        byte[] bb = EncodingUtils.getAsciiBytes(b);
        int bi = bb[0];
        int ci = sFN.length();
        String d = sFix.substring(0, 1);
        int di = getInt(d);
        String e = sDT.substring(6, 6 + 2);
        int ei = getInt2(e) * 7;
        String f = sDT.substring(sDT.length() - 2);
        int fi = getInt2(f) * 2;
        int k = getInt2(sFix);
        int resd = ai * bi * ci * di + ei + fi + k;
        final String res = Integer.toString(resd);

        return res;
    }


    public static String calcCS3(String sID, String sDT, String sP1, String sFix) {
        String a = (sDT.substring(sDT.length() - 5, sDT.length() - 2));// - 4- 1
        int ai = getInt2(a);
        //(Asc
        int b1 = getInt(sDT.substring(sDT.length() - 1)) + 1;
        String b = (sID.substring(b1 - 1, b1));
        byte[] bb = EncodingUtils.getAsciiBytes(b);
        int bi = bb[0];
        String c = sP1.substring(sP1.length() - 1);
        byte[] cb = EncodingUtils.getAsciiBytes(c);
        int ci = cb[0];
        String d = sFix.substring(0, 1);
        int di = getInt(d);
        String e = sDT.substring(6, 6 + 2);
        int ei = getInt2(e) * 7;
        String f = sDT.substring(sDT.length() - 2);
        int fi = getInt2(f) * 2;
        int k = getInt2(sFix);
        int resd = ai * bi * ci * di + ei + fi + k;

        return Integer.toString(resd);
    }

    public static int getInt(String si) {
        int i = 0;
        try {
            i = Integer.parseInt(si);
        } catch (Exception ignored) {
        }
        return i;
    }

    public static int getInt2(String si) {
        while (si.length() > 0 && si.substring(0, 1).equalsIgnoreCase("0"))
            si = si.substring(1, si.length());
        return getInt(si);
    }*//*

}
*/
