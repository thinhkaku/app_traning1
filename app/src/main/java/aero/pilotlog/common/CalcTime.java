package aero.pilotlog.common;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalcTime {
    final static double kEpsilon = 0.00001;
    final static int firstWeek = 1;
    final static int lastWeek = -1;

    //Input:
    public static String CodeISO;
    public static long AirfieldLatitude;
    public static long AirfieldLongitude;
    public static String AirfieldName;

    //Output data
    public static double OffsetUTC;//
    public static double OffsetDST;//
    public static String TimeDisplay;
    public static String RuleDST;
    public static String RegionDST;
    public static String pa = "login%19";


    public static GregorianCalendar calendarCurrentUTC;
    public static GregorianCalendar calendarUTC;
    public static GregorianCalendar calendarLocal;

    public static GregorianCalendar calendarStartDST;
    public static GregorianCalendar calendarEndDST;
    public static GregorianCalendar calendarCurrentLocalAFTime;
    public static Calendar calendarChoose;
    public static boolean bDST_Time;
    static boolean eqDST; //below Equator DST is reversed

    public CalcTime() {
        Clear();
    }


    public void Clear() {
        AirfieldLongitude = 0;
        AirfieldLatitude = 0;
        AirfieldName = "";
        CodeISO = "";

        TimeDisplay = "";
        OffsetDST = 0;
        RuleDST = "";
        RegionDST = "";
        bDST_Time = false;
        eqDST = false;
        if (calendarCurrentUTC != null)
            calendarCurrentUTC.clear();
        if (calendarStartDST != null)
            calendarStartDST.clear();
        if (calendarEndDST != null)
            calendarEndDST.clear();
        if (calendarCurrentLocalAFTime != null)
            calendarCurrentLocalAFTime.clear();
        if (calendarUTC != null)
            calendarUTC.clear();
        if (calendarLocal != null)
            calendarLocal.clear();
        if (calendarChoose != null)
            calendarChoose = null;
    }


    public void GetTimeZone() {
        double LatA;
        double LongA;
        LongA = Math.abs(AirfieldLongitude) / 1000.0;
        LatA = Math.abs(AirfieldLatitude) / 1000.0;

        //Reset
        OffsetUTC = -99;   //flag
        eqDST = false;

        //TimeZone ------------------------------------------------------------------------------------------------------------------------------------------

        //Select Case CodeISO

        if (CodeISO.equalsIgnoreCase("IE") ||
                CodeISO.equalsIgnoreCase("BF") ||
                CodeISO.equalsIgnoreCase("JE") ||
                CodeISO.equalsIgnoreCase("SL") ||
                CodeISO.equalsIgnoreCase("MR") ||
                CodeISO.equalsIgnoreCase("CI") ||
                CodeISO.equalsIgnoreCase("ST") ||
                CodeISO.equalsIgnoreCase("LR") ||
                CodeISO.equalsIgnoreCase("TG") ||
                CodeISO.equalsIgnoreCase("GH") ||
                CodeISO.equalsIgnoreCase("SN") ||
                CodeISO.equalsIgnoreCase("MA") ||
                CodeISO.equalsIgnoreCase("SH") ||
                CodeISO.equalsIgnoreCase("EH") ||
                CodeISO.equalsIgnoreCase("GN") ||
                CodeISO.equalsIgnoreCase("GG") ||
                CodeISO.equalsIgnoreCase("GW") ||
                CodeISO.equalsIgnoreCase("GB") ||
                CodeISO.equalsIgnoreCase("GM") ||
                CodeISO.equalsIgnoreCase("FO") ||
                CodeISO.equalsIgnoreCase("IM") ||
                CodeISO.equalsIgnoreCase("ML") ||
                CodeISO.equalsIgnoreCase("IS")) {
            OffsetUTC = 0;
        }

//		Case "IE", "BF", "JE", "SL", "MR", "CI", "ST", "LR", "TG", "GH", "SN", "MA", "SH", "EH", "GN", "GG", "GW", "GB", "GM", "FO", "IM", "ML", "IS"
//		OffsetUTC = 0

        else if (CodeISO.equalsIgnoreCase("ES")) {
            //	Case "ES"   //Spain
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 1.0;
            } else if (LongA < 10.0) {
                OffsetUTC = 1.0;
            } else {
                OffsetUTC = 0;
            }
            //	End if
        }

//		Case "NE", "DK", "LI", "DE", "TD", "IT", "CZ", "GI", "NA", "NG", "PL", "FR", "GA", "LU", "RS", "SJ", "NL", "SM", "ES", "SE", "DZ", "TN", "SI", "SK", "NO", "HU", "CM", "BA", "BE", "MT", "BJ", "MC", "ME", "AT", "HR", "MK", "CF", "CG", "AO", "AL", "VA", "CH", "AD", "GQ"
        else if (CodeISO.equalsIgnoreCase("NE") ||
                CodeISO.equalsIgnoreCase("DK") ||
                CodeISO.equalsIgnoreCase("LI") ||
                CodeISO.equalsIgnoreCase("DE") ||
                CodeISO.equalsIgnoreCase("TD") ||
                CodeISO.equalsIgnoreCase("IT") ||
                CodeISO.equalsIgnoreCase("CZ") ||
                CodeISO.equalsIgnoreCase("GI") ||
                CodeISO.equalsIgnoreCase("NA") ||
                CodeISO.equalsIgnoreCase("NG") ||
                CodeISO.equalsIgnoreCase("PL") ||
                CodeISO.equalsIgnoreCase("FR") ||
                CodeISO.equalsIgnoreCase("GA") ||
                CodeISO.equalsIgnoreCase("LU") ||
                CodeISO.equalsIgnoreCase("RS") ||
                CodeISO.equalsIgnoreCase("SJ") ||
                CodeISO.equalsIgnoreCase("NL") ||
                CodeISO.equalsIgnoreCase("SM") ||
                CodeISO.equalsIgnoreCase("ES") ||
                CodeISO.equalsIgnoreCase("SE") ||
                CodeISO.equalsIgnoreCase("DZ") ||
                CodeISO.equalsIgnoreCase("TN") ||
                CodeISO.equalsIgnoreCase("SI") ||
                CodeISO.equalsIgnoreCase("SK") ||
                CodeISO.equalsIgnoreCase("NO") ||
                CodeISO.equalsIgnoreCase("HU") ||
                CodeISO.equalsIgnoreCase("CM") ||
                CodeISO.equalsIgnoreCase("BA") ||
                CodeISO.equalsIgnoreCase("BE") ||
                CodeISO.equalsIgnoreCase("MT") ||
                CodeISO.equalsIgnoreCase("BJ") ||
                CodeISO.equalsIgnoreCase("MC") ||
                CodeISO.equalsIgnoreCase("ME") ||
                CodeISO.equalsIgnoreCase("AT") ||
                CodeISO.equalsIgnoreCase("HR") ||
                CodeISO.equalsIgnoreCase("MK") ||
                CodeISO.equalsIgnoreCase("CF") ||
                CodeISO.equalsIgnoreCase("CG") ||
                CodeISO.equalsIgnoreCase("AO") ||
                CodeISO.equalsIgnoreCase("AL") ||
                CodeISO.equalsIgnoreCase("VA") ||
                CodeISO.equalsIgnoreCase("CH") ||
                CodeISO.equalsIgnoreCase("AD") ||
//new 
                CodeISO.equalsIgnoreCase("LY") ||
                CodeISO.equalsIgnoreCase("GQ")) {
            OffsetUTC = 1.0;
        } else if (CodeISO.equalsIgnoreCase("CD")) {
            //Congo
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 1;
            } else if ((LongA > 23) || ((LongA > 20) && (LatA > 2))) {
                OffsetUTC = 2;
            } else {
                OffsetUTC = 1;
            }
        }

        //Case "GR", "LV", "MZ", "MW", "MD", "IL",  "LB", "CY", "TR", "FI", "SZ", "BW", "UA", "BI", "BG", "JO", "AX", "ZW", "ZA", "ZM", "BY", "EE", "RO", "SY", "LT", "EG", "RW", "LS", "PS"
        else if (CodeISO.equalsIgnoreCase("GR") ||
                CodeISO.equalsIgnoreCase("LV") ||
                CodeISO.equalsIgnoreCase("MZ") ||
                CodeISO.equalsIgnoreCase("MW") ||
                CodeISO.equalsIgnoreCase("MD") ||
                CodeISO.equalsIgnoreCase("IL") ||

                CodeISO.equalsIgnoreCase("LB") ||
                CodeISO.equalsIgnoreCase("CY") ||
                CodeISO.equalsIgnoreCase("TR") ||
                CodeISO.equalsIgnoreCase("FI") ||
                CodeISO.equalsIgnoreCase("SZ") ||
                CodeISO.equalsIgnoreCase("BW") ||
                CodeISO.equalsIgnoreCase("UA") ||
                CodeISO.equalsIgnoreCase("BI") ||
                CodeISO.equalsIgnoreCase("BG") ||

                CodeISO.equalsIgnoreCase("AX") ||
                CodeISO.equalsIgnoreCase("ZW") ||
                CodeISO.equalsIgnoreCase("ZA") ||
                CodeISO.equalsIgnoreCase("ZM") ||
//                CodeISO.equalsIgnoreCase("BY") ||
                CodeISO.equalsIgnoreCase("EE") ||
                CodeISO.equalsIgnoreCase("RO") ||
                CodeISO.equalsIgnoreCase("SY") ||
                CodeISO.equalsIgnoreCase("LT") ||
                CodeISO.equalsIgnoreCase("EG") ||
                CodeISO.equalsIgnoreCase("RW") ||
                CodeISO.equalsIgnoreCase("LS") ||
                CodeISO.equalsIgnoreCase("JO") ||
                CodeISO.equalsIgnoreCase("PS")) {
            OffsetUTC = 2;
        }
        //Add following JIRA PL-153
        else if (CodeISO.equalsIgnoreCase("BY")) {
            OffsetUTC = 3;
        }
        //Case "IQ", "YE", "YT", "QA", "KW", "ET", "KM", "TZ", "BH", "KE", "ER", "UG", "SD", "SA", "MG", "SO", "DJ"
        else if (CodeISO.equalsIgnoreCase("IQ") ||
                CodeISO.equalsIgnoreCase("YE") ||
                CodeISO.equalsIgnoreCase("YT") ||
                CodeISO.equalsIgnoreCase("QA") ||
                CodeISO.equalsIgnoreCase("KW") ||
                CodeISO.equalsIgnoreCase("ET") ||
                CodeISO.equalsIgnoreCase("KM") ||
                CodeISO.equalsIgnoreCase("TZ") ||
                CodeISO.equalsIgnoreCase("BH") ||
                CodeISO.equalsIgnoreCase("KE") ||
                CodeISO.equalsIgnoreCase("ER") ||
                CodeISO.equalsIgnoreCase("UG") ||
                CodeISO.equalsIgnoreCase("SD") ||
                CodeISO.equalsIgnoreCase("SA") ||
                CodeISO.equalsIgnoreCase("MG") ||
                CodeISO.equalsIgnoreCase("SO") ||
                CodeISO.equalsIgnoreCase("DJ")) {
            OffsetUTC = 3;
        }

        //Case "IR"
        else if (CodeISO.equalsIgnoreCase("IR")) {
            OffsetUTC = 3.5;
        }

        //Case "MU", "AM", "AZ", "OM", "GE", "RE", "SC", "AE"
        else if (CodeISO.equalsIgnoreCase("MU") ||
//                CodeISO.equalsIgnoreCase("AM") ||
                CodeISO.equalsIgnoreCase("AZ") ||
                CodeISO.equalsIgnoreCase("OM") ||
//                CodeISO.equalsIgnoreCase("GE") ||
                CodeISO.equalsIgnoreCase("RE") ||
                CodeISO.equalsIgnoreCase("SC") ||
                CodeISO.equalsIgnoreCase("AE")) {
            OffsetUTC = 4;
        }
        //Add following JIRA PL-153
        else if (CodeISO.equalsIgnoreCase("AM") || CodeISO.equalsIgnoreCase("GE")) {
            OffsetUTC = 5;
        } else if (CodeISO.equalsIgnoreCase("AF")) {
            OffsetUTC = 4.5;
        } else if (CodeISO.equalsIgnoreCase("KZ")) {
            //Kazakhstan
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 6;
            } else if ((LongA > 63) || (LatA > 51) || ((LongA > 58.5) && (LatA < 47))) {
                OffsetUTC = 6;
            } else {
                OffsetUTC = 5;
            }
        } else if (CodeISO.equalsIgnoreCase("TF") ||
                CodeISO.equalsIgnoreCase("UZ") ||
                CodeISO.equalsIgnoreCase("TJ") ||
                CodeISO.equalsIgnoreCase("TM") ||
                CodeISO.equalsIgnoreCase("MV")
                || CodeISO.equalsIgnoreCase("PK")) {
            OffsetUTC = 5;
        } else if (CodeISO.equalsIgnoreCase("IN") ||
                CodeISO.equalsIgnoreCase("LK")) {
            OffsetUTC = 5.5;
        } else if (CodeISO.equalsIgnoreCase("NP")) {
            OffsetUTC = 5.75;
        } else if (CodeISO.equalsIgnoreCase("KG") ||
                CodeISO.equalsIgnoreCase("BT") ||
                CodeISO.equalsIgnoreCase("IO") ||
                CodeISO.equalsIgnoreCase("BD")) {
            OffsetUTC = 6;
        } else if (CodeISO.equalsIgnoreCase("CC") ||
                CodeISO.equalsIgnoreCase("MM")) {
            OffsetUTC = 6.5;
        } else if (CodeISO.equalsIgnoreCase("TH") ||
                CodeISO.equalsIgnoreCase("CX") ||
                CodeISO.equalsIgnoreCase("VN") ||
                CodeISO.equalsIgnoreCase("LA") ||
                CodeISO.equalsIgnoreCase("KH")) {
            OffsetUTC = 7;
        } else if (CodeISO.equalsIgnoreCase("RU")) {//Russia
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            }
            //(LongA > 152 And LatA > 73) Or (LongA > 142 And LatA > 62 And LatA < 73) Or (LongA > 147 And LatA < 62) Or (LongA > 142 And LatA < 59) Then
            else if (((LongA > 152) && (LatA > 73)) || ((LongA > 142) && (LatA > 62) && (LatA < 73)) || ((LongA > 147) && (LatA < 62)) || ((LongA > 142) && (LatA < 59))) {
                OffsetUTC = 12;
            } else if (((LongA > 132) && (LatA > 53.5)) || ((LongA > 132) && (LatA < 52)) || (LongA > 135) || ((LongA > 127) && (LatA > 66) && (LatA < 68))) {
                OffsetUTC = 11;
            }
            //(LongA > 112.5 And LatA > 59) Or (LongA > 106 And LatA > 64 And LatA < 70) Or
            //(LongA > 109 And LatA > 59 And LatA < 70) Or LongA > 118 Or (LongA > 116 And LatA < 57) Or
            //LatA < (0.56 * LongA - 10)
            else if (((LongA > 112.5) && (LatA > 59)) || ((LongA > 106) && (LatA > 64) && (LatA < 70)) ||
                    ((LongA > 109) && (LatA > 59) && (LatA < 70)) || (LongA > 118) || ((LongA > 116) && (LatA < 57)) ||
                    LatA < (0.56 * LongA - 10)) {
                OffsetUTC = 10;
            }
            //LongA > 106 Or (LatA < (0.67 * LongA - 10.7) And LatA > (-1 * LongA + 151))
            else if ((LongA > 106) || ((LatA < (0.67 * LongA - 10.7)) && (LatA > (-1 * LongA + 151)))) {
                OffsetUTC = 9;
            }
            //(LatA > (-1.64 * LongA +203.4) And LatA > 60) Or LongA > 89
            else if (((LatA > (-1.64 * LongA + 203.4)) && (LatA > 60)) || (LongA > 89)) {
                OffsetUTC = 8;
            }
            //(LatA < (0.79 * LongA) And LatA < 60.5)
            else if ((LatA < (0.79 * LongA)) && (LatA < 60.5)) {
                OffsetUTC = 7;
            }
            //LongA > 66 Or (LongA > 59.5 And LatA < (0.42 * LongA + 39.5)) Or (LongA > 55
            //And LatA < 61) Or (LatA < (0.63 * LongA + 19.6) And LatA > (-1.5 * LongA + 119.5))
            else if ((LongA > 66) || ((LongA > 59.5) && (LatA < (0.42 * LongA + 39.5))) || ((LongA > 55)
                    && (LatA < 61)) || ((LatA < (0.63 * LongA + 19.6)) && (LatA > (-1.5 * LongA + 119.5)))) {
                OffsetUTC = 6;
            } else if (LongA > 25) {
                OffsetUTC = 4;
            }
            //comment following JIRA PL-153
            if (OffsetUTC != -99) {
                OffsetUTC--;
            }
//            else {
//                OffsetUTC = 3;
//            }
        } else if (CodeISO.equalsIgnoreCase("AU")) {
            //Australia
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            } else if ((LongA > 126) && (LongA < 129) && (LatA > 31.5)) {
                OffsetUTC = 8.75;
            } else if (LongA < 129) {
                OffsetUTC = 8;
            } else if (LongA > 157) {
                OffsetUTC = 10.5;
            } else if ((LongA > 141) || ((LongA > 138) && (LatA < 26))) {
                OffsetUTC = 10;
            } else {
                OffsetUTC = 9.5;
            }
        } else if (CodeISO.equalsIgnoreCase("ID")) {
            //Indonesia
            if (Math.abs(LongA) <= 0) {
                OffsetUTC = 7;
            } else if (LongA > 124.5) {
                OffsetUTC = 9;
            } else if (LongA < 115) {
                OffsetUTC = 7;
            } else {
                OffsetUTC = 8;
            }
        } else if (CodeISO.equalsIgnoreCase("MN")) {
            //Mongolia
            if ((Math.abs(LongA) <= kEpsilon) || (LongA > 95.4) || ((LongA > 93.5) && (LatA < 49))) {
                OffsetUTC = 8;
            } else {
                OffsetUTC = 7;
            }
        } else if (CodeISO.equalsIgnoreCase("HK") ||
                CodeISO.equalsIgnoreCase("MY") ||
                CodeISO.equalsIgnoreCase("SG") ||
                CodeISO.equalsIgnoreCase("TW") ||
                CodeISO.equalsIgnoreCase("CN") ||
                CodeISO.equalsIgnoreCase("BN") ||
                CodeISO.equalsIgnoreCase("MO") ||
                CodeISO.equalsIgnoreCase("PH")) {
            OffsetUTC = 8;
        } else if (CodeISO.equalsIgnoreCase("PW") ||
                CodeISO.equalsIgnoreCase("KP") ||
                CodeISO.equalsIgnoreCase("TL") ||
                CodeISO.equalsIgnoreCase("KR") ||
                CodeISO.equalsIgnoreCase("JP")) {
            OffsetUTC = 9;
        } else if (CodeISO.equalsIgnoreCase("MP") ||
                CodeISO.equalsIgnoreCase("PG") ||
                CodeISO.equalsIgnoreCase("GU")) {
            OffsetUTC = 10;
        } else if (CodeISO.equalsIgnoreCase("FM")) {
            //Micronesia
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 10;
            } else if (LongA < 153) {
                OffsetUTC = 10;
            } else {
                OffsetUTC = 11;
            }
        } else if (CodeISO.equalsIgnoreCase("SB") ||
                CodeISO.equalsIgnoreCase("NC") ||
                CodeISO.equalsIgnoreCase("VU") ||
                CodeISO.equalsIgnoreCase("FM")) {
            OffsetUTC = 11;
        } else if (CodeISO.equalsIgnoreCase("NF")) {
            //PL-1162
            Date dateChoose = new Date();
            if (calendarChoose != null) {
                dateChoose = calendarChoose.getTime();
            }
            //Date 4/10/2015
            Calendar calendar = Calendar.getInstance();
            calendar.set(2015, Calendar.OCTOBER, 3);
            Date dateSpecial = calendar.getTime();
            if (dateChoose.before(dateSpecial)) {
                OffsetUTC = 11.5;
            } else {
                OffsetUTC = 11;
            }
            //End PL-1162
        } else if (CodeISO.equalsIgnoreCase("WF") ||
                CodeISO.equalsIgnoreCase("NR") ||
                CodeISO.equalsIgnoreCase("MH") ||
                CodeISO.equalsIgnoreCase("TV") ||
                CodeISO.equalsIgnoreCase("FJ")) {
            OffsetUTC = 12;
        } else if (CodeISO.equalsIgnoreCase("NZ")) {
            //New Zealand
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 12;
            } else if ((LongA > 180) && (LatA > 40)) {
                OffsetUTC = 12.75;
            } else {
                OffsetUTC = 12;
            }
        } else if (CodeISO.equalsIgnoreCase("TO")) {
            OffsetUTC = 13;
        } else if (CodeISO.equalsIgnoreCase("KI")) {
            //Kiribati
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            } else if (AirfieldLongitude < 0) {
                OffsetUTC = 12;
            } else if (LongA < 165) {
                OffsetUTC = 13;
            } else {
                OffsetUTC = 14;
            }
        } else if (CodeISO.equalsIgnoreCase("PT")) {
            //Portugal
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = 0;
            } else if (LongA > 20) {
                OffsetUTC = -1;
            } else {
                OffsetUTC = 0;
            }
        } else if (CodeISO.equalsIgnoreCase("CV")) {
            OffsetUTC = -1;
        } else if (CodeISO.equalsIgnoreCase("GS")) {
            OffsetUTC = -2;
        } else if (CodeISO.equalsIgnoreCase("AR") ||
                CodeISO.equalsIgnoreCase("UY") ||
                CodeISO.equalsIgnoreCase("SR") ||
                CodeISO.equalsIgnoreCase("GF") ||
                CodeISO.equalsIgnoreCase("PM")) {
            OffsetUTC = -3;
        }
        //Argentina San Luis is -4

        else if (CodeISO.equalsIgnoreCase("GL")) {
            //Greenland
//			if (Math.abs(LongA) <= kEpsilon)
//			{
//				OffsetUTC = -3;
//			}
//			else if (LatA < (0.93 * LongA + 16.4))
//			{
//				OffsetUTC = -4;
//			}
            if (LongA < 19)
                OffsetUTC = 0;
            else if (LongA > 65)
                OffsetUTC = -4;
            else if ((LongA < 29) && (LatA > 70) && (LatA < 74.5)) {
                OffsetUTC = -1;
            } else if ((LongA < 23.5) && (LatA > 75.5) && (LatA < 78)) {
                OffsetUTC = 0;
            } else {
                OffsetUTC = -3;
            }
        } else if (CodeISO.equalsIgnoreCase("BR")) {
            //Brazil
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            } else if (LongA < 34) {
                OffsetUTC = -2;
            } else if ((LongA < 52.6) || (LatA > 23)) {
                OffsetUTC = -3;
            } else {
                OffsetUTC = -4;
            }
        } else if (CodeISO.equalsIgnoreCase("CA")) {
            //Canada
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            } else if (LatA < (-2.13 * LongA + 173.3)) {
                OffsetUTC = -3.5;
            }
            //(LongA < 68 And (LatA > 61 Or LatA < 48 Or (LatA > 51.5 And LatA < 54.5))) Or (LongA < 64 And LatA > 52) Or LongA < 62 Then

            else if (((LongA < 68) && ((LatA > 61) || (LatA < 48) || ((LatA > 51.5) && (LatA < 54.5)))) || ((LongA < 64) && (LatA > 52)) || (LongA < 62)) {
                OffsetUTC = -4;
            } else if (((LongA < 85) && (LatA > 60)) || ((LongA < 86.5) && (LatA < 66)) || ((LongA < 90) && (LatA <= 60))) {
                OffsetUTC = -5;
            } else if (((LongA < 102) && (LatA > 60)) || ((LongA < 100) && (LatA <= 60))) {
                OffsetUTC = -6;
            }
            //LatA > (0.95  LongA - 60.9) And Not (LongA > 120 And LatA > 57 And LatA < 60) Then

            else if ((LatA > (0.95 * LongA - 60.9)) && !((LongA > 120) && (LatA > 57) && (LatA < 60))) {
                OffsetUTC = -7;
            } else {
                OffsetUTC = -8;
            }
        } else if (CodeISO.equalsIgnoreCase("US")) {
            //USA
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -99;
            }
            //LongA < 85 Or (LongA < 87.5 And LatA > (0.95  LongA - 44) And Not (LatA > 41 And LatA < 42 And LongA > 86.5)) Then

            else if ((LongA < 85.2) || ((LongA < 87.5) && (LatA > (0.95 * LongA - 44)) && !((LatA > 41) && (LatA < 42) && (LongA > 86.5)))) {
                OffsetUTC = -5;
            }
            //LongA < 101.5 Or (LongA < 103 And LatA < 37) Or (LongA < 104 And LatA > 47.5) Or (LongA < 105 And LatA < 32) Then

            else if ((LongA < 101.5) || ((LongA < 103) && (LatA < 37)) || ((LongA < 104) && (LatA > 47.5)) || ((LongA < 105) && (LatA < 32))) {
                OffsetUTC = -6;
            }
//	!!!!!!	changed	//LongA < 114 Or (LongA < 116 And LatA > 47) Or (LongA < 118 And LatA > 41.7 And LatA < 46) Or (LongA < 114.6 And LatA < 36) Then
//	!!!!!!	changed	else if ((LongA < 114) || ((LongA < 116) && (LatA > 47)) || ((LongA < 118) && (LatA > 41.7) && (LatA < 46)) || ((LongA < 114.6) && (LatA < 36)))
//ElseIf LatA > (1.56 * LongA - 133.1) Or (LongA < 117 And LatA > 41.9 And LatA < 45.7) Or
//			(LongA < 118 And LatA > 42.2 And LatA < 43.9) Or LongA < 114 Or (LongA < 114.6 And LatA < 36)

            else if ((LatA > (1.56 * LongA - 133.1)) || ((LongA < 117) && (LatA > 41.9) && (LatA < 45.7)) ||
                    ((LongA < 118) && (LatA > 42.2) && (LatA < 43.9)) || (LongA < 114) || ((LongA < 114.6) && (LatA < 36))) {
                OffsetUTC = -7;
            } else if (LongA < 127.5) {
                OffsetUTC = -8;
            } else if (!((LongA > 141) && (LatA < 55))) {
                OffsetUTC = -9;
            } else {
                OffsetUTC = -10;
            }
        } else if (CodeISO.equalsIgnoreCase("AQ") ||
                CodeISO.equalsIgnoreCase("PR") ||
                CodeISO.equalsIgnoreCase("BS") ||
                CodeISO.equalsIgnoreCase("AW") ||
                CodeISO.equalsIgnoreCase("MF") ||
                CodeISO.equalsIgnoreCase("BB") ||
                CodeISO.equalsIgnoreCase("BL") ||
                CodeISO.equalsIgnoreCase("BM") ||
                CodeISO.equalsIgnoreCase("MQ") ||
                CodeISO.equalsIgnoreCase("LC") ||
                CodeISO.equalsIgnoreCase("BO") ||
                CodeISO.equalsIgnoreCase("MS") ||
                CodeISO.equalsIgnoreCase("KN") ||
                CodeISO.equalsIgnoreCase("VC") ||
                CodeISO.equalsIgnoreCase("AN") ||
                CodeISO.equalsIgnoreCase("TT") ||
                CodeISO.equalsIgnoreCase("GP") ||
                CodeISO.equalsIgnoreCase("AG") ||
                CodeISO.equalsIgnoreCase("DM") ||
                CodeISO.equalsIgnoreCase("DO") ||
                CodeISO.equalsIgnoreCase("GD") ||
                CodeISO.equalsIgnoreCase("VI") ||
                CodeISO.equalsIgnoreCase("PY") ||
                CodeISO.equalsIgnoreCase("FK") ||
                CodeISO.equalsIgnoreCase("VG") ||
                CodeISO.equalsIgnoreCase("AI") ||
                CodeISO.equalsIgnoreCase("GY")) {
            OffsetUTC = -4;
        } else if (CodeISO.equalsIgnoreCase("CL")) {
            //Chili
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -4;
            } else if (LongA < 78) {
                OffsetUTC = -4;
            } else {
                OffsetUTC = -6;
            }
        } else if (CodeISO.equalsIgnoreCase("VE")) {
            Date dateChoose = new Date();
            if (calendarChoose != null) {
                dateChoose = calendarChoose.getTime();
            }
            //PL-940
            //Date 1/5/2016
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, Calendar.MAY, 1);
            Date dateSpecial = calendar.getTime();
            if (dateChoose.before(dateSpecial)) {
                OffsetUTC = -4.5;
            } else {
                OffsetUTC = -4;
            }
            //End PL-940
        } else if (CodeISO.equalsIgnoreCase("CU") ||
                CodeISO.equalsIgnoreCase("HT") ||
                CodeISO.equalsIgnoreCase("CO") ||
                CodeISO.equalsIgnoreCase("TC") ||
                CodeISO.equalsIgnoreCase("JM") ||
                CodeISO.equalsIgnoreCase("KY") ||
                CodeISO.equalsIgnoreCase("PE") ||
                CodeISO.equalsIgnoreCase("PA")) {
            OffsetUTC = -5;
        } else if (CodeISO.equalsIgnoreCase("EC")) {
            //Ecuador
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -5;
            } else if (LongA < 83) {
                OffsetUTC = -5;
            } else {
                OffsetUTC = -6;
            }
        } else if (CodeISO.equalsIgnoreCase("MX")) {
            //Mexico
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -6;
            } else if ((LongA > 112.7) && (LatA > 28) && (LatA < (1.78 * LongA - 171.6))) {
                OffsetUTC = -8;
            }
            //(LatA < (1.27  LongA - 110.4)) Or (LongA > 106 And LatA > 25.7) Or (LongA > 103.7 And LatA > 26.6 And LatA < 29.6) Or (LongA > 104.9 And LatA > 26.6) Then

            else if ((LatA < (1.27 * LongA - 110.4) && LatA > 21) || ((LongA > 106) && (LatA > 25.7)) || ((LongA > 103.7) && (LatA > 26.6) && (LatA < 29.6)) || ((LongA > 104.9) && (LatA > 26.6))) {
                OffsetUTC = -7;
            } else {
                OffsetUTC = -6;
            }
        } else if (CodeISO.equalsIgnoreCase("NI") ||
                CodeISO.equalsIgnoreCase("SV") ||
                CodeISO.equalsIgnoreCase("GT") ||
                CodeISO.equalsIgnoreCase("HN") ||
                CodeISO.equalsIgnoreCase("CR") ||
                CodeISO.equalsIgnoreCase("BZ")) {
            OffsetUTC = -6;
        } else if (CodeISO.equalsIgnoreCase("PN")) {
            OffsetUTC = -8;
        } else if (CodeISO.equalsIgnoreCase("PF")) {
            //French Polynesia
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -10;
            } else if ((LongA < 135) && (LatA > 22)) {
                OffsetUTC = -9;
            } else if ((LongA > 143) && (LatA < 14)) {
                OffsetUTC = -9.5;
            } else {
                OffsetUTC = -10;
            }
        } else if (CodeISO.equalsIgnoreCase("TK") ||
                CodeISO.equalsIgnoreCase("CK")) {
            OffsetUTC = -10;
        } else if (CodeISO.equalsIgnoreCase("WS") ||
                CodeISO.equalsIgnoreCase("NU") ||
                CodeISO.equalsIgnoreCase("AS")) {
            OffsetUTC = -11;
        } else if (CodeISO.equalsIgnoreCase("UM")) {
            //US Minor Outlying Islands
            if (Math.abs(LongA) <= kEpsilon) {
                OffsetUTC = -11;
            } else if (AirfieldLongitude < 0) {
                OffsetUTC = 12;
            } else {
                OffsetUTC = -11;
            }
        }

        if (OffsetUTC == -99) {
            return;
        }


        //DST Rules ----------DST Rules --------------DST Rules --------------DST Rules ----------------------------------------------------------------------------------------


        if (CodeISO.equalsIgnoreCase("AL") ||
                CodeISO.equalsIgnoreCase("AD") ||
                CodeISO.equalsIgnoreCase("AT") ||
                CodeISO.equalsIgnoreCase("BA") ||
                CodeISO.equalsIgnoreCase("BE") ||
                CodeISO.equalsIgnoreCase("BG") ||
                CodeISO.equalsIgnoreCase("HR") ||
                CodeISO.equalsIgnoreCase("CY") ||
                CodeISO.equalsIgnoreCase("CZ") ||
                CodeISO.equalsIgnoreCase("DK") ||
                CodeISO.equalsIgnoreCase("FO") ||
                CodeISO.equalsIgnoreCase("EE") ||
                CodeISO.equalsIgnoreCase("FI") ||
                CodeISO.equalsIgnoreCase("FR") ||
                CodeISO.equalsIgnoreCase("DE") ||
                CodeISO.equalsIgnoreCase("GI") ||
                CodeISO.equalsIgnoreCase("GR") ||
                CodeISO.equalsIgnoreCase("GG") ||
                CodeISO.equalsIgnoreCase("HU") ||
                CodeISO.equalsIgnoreCase("IM") ||
                CodeISO.equalsIgnoreCase("IE") ||
                CodeISO.equalsIgnoreCase("IT") ||
                CodeISO.equalsIgnoreCase("VA") ||
                CodeISO.equalsIgnoreCase("JE") ||
                CodeISO.equalsIgnoreCase("LV") ||
                CodeISO.equalsIgnoreCase("LI") ||
                CodeISO.equalsIgnoreCase("LT") ||
                CodeISO.equalsIgnoreCase("LU") ||
                CodeISO.equalsIgnoreCase("MK") ||
                CodeISO.equalsIgnoreCase("MT") ||
                CodeISO.equalsIgnoreCase("MD") ||
                CodeISO.equalsIgnoreCase("MC") ||
                CodeISO.equalsIgnoreCase("ME") ||
                CodeISO.equalsIgnoreCase("NL") ||
                CodeISO.equalsIgnoreCase("NO") ||
                CodeISO.equalsIgnoreCase("PL") ||
                CodeISO.equalsIgnoreCase("PT") ||
                CodeISO.equalsIgnoreCase("RO") ||
                CodeISO.equalsIgnoreCase("SM") ||
                CodeISO.equalsIgnoreCase("RS") ||
                CodeISO.equalsIgnoreCase("SK") ||
                CodeISO.equalsIgnoreCase("SI") ||
                CodeISO.equalsIgnoreCase("ES") ||
                CodeISO.equalsIgnoreCase("SE") ||
                CodeISO.equalsIgnoreCase("CH") ||
                //CodeISO.equalsIgnoreCase("TR") ||
                CodeISO.equalsIgnoreCase("GB") ||
                CodeISO.equalsIgnoreCase("UA") ||
                CodeISO.equalsIgnoreCase("UK") ||
                CodeISO.equalsIgnoreCase("SJ")) {
            EuropeRule();
        }
        //PL-949
        else if (CodeISO.equalsIgnoreCase("TR")) {
            Date dateChoose = new Date();
            if (calendarChoose != null) {
                dateChoose = calendarChoose.getTime();
            }
            //PL-949
            //Date 8/9/2016
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, Calendar.SEPTEMBER, 8);
            Date dateSpecial = calendar.getTime();
            if (dateChoose.before(dateSpecial)) {
                EuropeRule();
            } else {
                TurkeyRule();
            }
        }
        //End PL-949
        //////


        else if (CodeISO.equalsIgnoreCase("HT")) {
            //	    'Haiti
            //        'Mar 2nd Sun  2:00s
            //        'Nov 1st Sun 2:00s
            initCalendars(true, Calendar.MARCH, 2, Calendar.SUNDAY, 2, Calendar.NOVEMBER, 1, Calendar.SUNDAY, 2);

            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            //DST starts on the second Sunday of March (" & Format(DateTime1, "short date") & ")
            //at 02:00 Local" & vbCr2 & "DST ends on the first Sunday of November
            //(" & Format(DateTime2, "short date") & ")  at 02:00 Local"
            RuleDST = String.format("DST starts on the second Sunday of March  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of November  (%s)  at 02:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("AZ")) {
            //Azerbedjan    //Mar lastSun 4:00
            //Oct lastSun 5:00

            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.SUNDAY, 4, Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 5);

            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);

            RuleDST = String.format("DST starts on the last Sunday of March  (%s)  at 04:00 Local\n\nDST ends on the last Sunday of October  (%s)  at 05:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("GL")) {
            //Greenland
            if ((Math.abs(OffsetUTC + 3.0) <= kEpsilon) || (Math.abs(OffsetUTC + 1) <= kEpsilon)) {
                RegionDST = "Most locations (except the north-west part and the Danmarkshavn district) in ";
                EuropeRule();
            } else if (Math.abs(OffsetUTC) <= kEpsilon) {
                RegionDST = "The Danmarkshavn district of ";
                noDST();
            } else if (Math.abs(OffsetUTC + 4) <= kEpsilon) {
                RegionDST = "The north-west part of ";
                //Thule      //Apr Sun>=1  2:00
                //Oct lastSun 2:00
                initCalendars(true, Calendar.APRIL, firstWeek, Calendar.SUNDAY, 2, Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 2);

                String startDate = getFormattedShortDate(calendarStartDST);
                String endDate = getFormattedShortDate(calendarEndDST);

                RuleDST = String.format("DST starts on the first Sunday of April  (%s)  at 02:00 Local\n\nDST ends on the last Sunday of October  (%s)  at 02:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("AM") ||
                CodeISO.equalsIgnoreCase("GE") ||
                CodeISO.equalsIgnoreCase("BY")) {
            //Mar lastSun 2:00s
            //Oct lastSun 2:00s

            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.SUNDAY, 2, Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on the last Sunday of March  (%s)  at 02:00 Local\n\nDST ends on the last Sunday of October  (%s)  at 03:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("LY")) {
            /*
             *  RuleDST = "As from 2013, DST starts on the last Friday of March at 01:00 UTC"
	    	 *   "DST ends on the last Friday of October at 01:00 UTC"  at "  Local )"
	    	 */
            //Mar lastSun 2:00s
            //Oct lastSun 2:00s

            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.FRIDAY, 2, Calendar.OCTOBER, lastWeek, Calendar.FRIDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("As from 2013, DST starts on the last Friday of March  (%s)  at 01:00 UTC \n\nDST ends on the last Friday of October  (%s)  at 01:00 UTC", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("RU")) {//'Mar lastSun 2:00s
            // 'Russia decided to stay on summer time as from 2011
            noDST();
        } else if (CodeISO.equalsIgnoreCase("CA")) {
            //Canada
            //(OffsetUTC = -4 And LatA > 49.5 And LatA < 52) Or (OffsetUTC = -7 And LongA > 120) Or (OffsetUTC = -6 And LongA > 101.7) Or (LatA > 62 And LatA < (-1.2  LongA + 169.6) And LatA < 66 And LongA > 80.5)

            if (((Math.abs(OffsetUTC + 4) <= kEpsilon) && (LatA > 49.5) && (LatA < 52)) ||
                    ((Math.abs(OffsetUTC + 7) <= kEpsilon) && (LongA > 120)) ||
                    ((Math.abs(OffsetUTC + 6) <= kEpsilon) && (LongA > 101.7)) ||
                    ((LatA > 62) && (LatA < (-1.2 * LongA + 169.6)) && (LatA < 66) && (LongA > 80.5))) {
                RegionDST = "This part of ";
                noDST();
            } else if ((Math.abs(OffsetUTC + 3.5) <= kEpsilon) ||
                    ((Math.abs(OffsetUTC + 4) <= kEpsilon) && (LatA >= 52))) {
                RegionDST = "The Newfoundland and Labrador province in ";
                //StJohn                 //Mar 2week Sun 2:00
                //Nov 1 week Sun
                initCalendars(true, Calendar.MARCH, 2, Calendar.SUNDAY, 2, Calendar.NOVEMBER, firstWeek, Calendar.SUNDAY, 2);

                String startDate = getFormattedShortDate(calendarStartDST);
                String endDate = getFormattedShortDate(calendarEndDST);

                RuleDST = String.format("DST starts on the second Sunday of March  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of November  (%s)  at 02:00 Local", startDate, endDate);
            } else {
                RegionDST = "Most locations in ";
                CanadaUsRule();
            }
        } else if (CodeISO.equalsIgnoreCase("PM")) {
            //Saint Pierre and Miquelon
            CanadaUsRule();
        } else if (CodeISO.equalsIgnoreCase("US")) {
            //USA
            //OffsetUTC = -7 And ((LongA > 109 And LatA < 35.2) Or (LongA > 111.2 And LatA < 35.8) Or (LongA > 111.6 And LatA < 37) Or (LongA > 110 And LongA < 111 And LatA > 35.5 And LatA < 36.3))

            if ((Math.abs(OffsetUTC + 7) <= kEpsilon) &&
                    (((LongA > 109) && (LatA < 35.2)) ||
                            ((LongA > 111.2) && (LatA < 35.8)) ||
                            ((LongA > 111.6) && (LatA < 37)) ||
                            ((LongA > 110) && (LongA < 111) && (LatA > 35.5) && (LatA < 36.3)))) {
                RegionDST = "The state of Arizona - ";
                //Arizona
                noDST();
            } else if ((LongA > 150) && (LatA > 15) && (LatA < 30)) {
                RegionDST = "The Hawaiian Islands - ";
                //Hawaiian Islands
                noDST();
            } else {
                RegionDST = "Most locations (except Arizona and Hawai) in the ";
                CanadaUsRule();
            }
        } else if (CodeISO.equalsIgnoreCase("BS") ||
                CodeISO.equalsIgnoreCase("BM") ||
                CodeISO.equalsIgnoreCase("TC")) {
            //Bahamas, Bermuda, Turks and Caicos Islands
            CanadaUsRule();
        } else if (CodeISO.equalsIgnoreCase("CU")) {
            // Cuba
            //Oct lastSun 0:00s
            initCalendars(true, Calendar.MARCH, 2, Calendar.SUNDAY, 0, Calendar.NOVEMBER, firstWeek, Calendar.SUNDAY, 1);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on the second Sunday of March  (%s)  at 00:00 Local\n\nDST ends on the first Sunday of November  (%s)  at 01:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("MX")) {
            //Mexico    //Apr Sun>=1  2:00
            //Oct lastSun 2:00

            if ((Math.abs(OffsetUTC + 7) <= kEpsilon) && (LongA > 108.3) && (LatA > 26.4) && (LatA > (1.78 * LongA - 171.6))) {
                RegionDST = "The state of Sonora - ";
                //Sonora
                noDST();
            } else if ((LatA > (0.35 * LongA - 12.6)) && (LongA > 97)) {
                RegionDST = "The Northern municipalities (except Sonora) along the US border in ";
                initCalendars(true, Calendar.MARCH, 2, Calendar.SUNDAY, 2, Calendar.NOVEMBER, firstWeek, Calendar.SUNDAY, 2);
                String startDate = getFormattedShortDate(calendarStartDST);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the second Sunday of March  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of November  (%s)  at 02:00 Local", startDate, endDate);
            } else if ((LatA < (-0.67 * LongA + 79.1) && LongA < 89.4 && LatA > 18.2) || LongA < 87.5) {//2016-01-04 TuanPV begin add new
//                DateTime1 = DateSerial(2015,2,1);
//                DateTime2 = DateSerial(2099,12,31);
                RuleDST = "As from February 2015, the state of Quintana Roo remains on DST all year round.";
                bDST_Time = true;
            } else {//2016-01-04 TuanPV end add new
                RegionDST = "Central and southern states of ";
                initCalendars(true, Calendar.APRIL, firstWeek, Calendar.SUNDAY, 2, Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 2);
                String startDate = getFormattedShortDate(calendarStartDST);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the first Sunday of April  (%s)  at 02:00 Local\n\nDST ends on the last Sunday of October  (%s)  at 02:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("BR")) {
            //Brazil
            //(((LongA > 50 And LongA < 60) Or (LongA > 38 And LongA < 46)) And LatA > 9) Or LatA > 13
            if (((((LongA > 50) && (LongA < 60)) || ((LongA > 38) && (LongA < 46))) && (LatA > 9)) || (LatA > 13)) {
                RegionDST = "The southern part of ";
                //Oct Sun>=15 0:00
                //Feb Sun>=15 0:00  (next year)
                initCalendars(true);
                calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, 3, Calendar.SUNDAY, 0);
                String startDate = getFormattedShortDate(calendarStartDST);
                //Carnival Calendar
                //http://www.ipanema.com/carnival/dates_finder.php
                //Checked until 2020 --> only 2012 and 2015 is carnival
                boolean nowBeforeOctober;
                GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
                nowBeforeOctober = nowCalendar.before(calendarStartDST);

                int year = nowCalendar.get(Calendar.YEAR);
                eqDST = true;
                if (nowBeforeOctober) {
                    if ((year == 2012) || (year == 2015))//Carnaval year
                        calendarEndDST = getStartEndCalendar(Calendar.FEBRUARY, 4, Calendar.SUNDAY, 0);
                    else
                        calendarEndDST = getStartEndCalendar(Calendar.FEBRUARY, 4, Calendar.SUNDAY, 0);
                    AdjustDST();
                    String endDate = getFormattedShortDate(calendarEndDST);

                    RuleDST = String.format("DST ends on the third Sunday of February at 00:00 Local, or the next Sunday in case the third Sunday coincides with Carnival.  This year DST ends %s\n\nDST starts on the third Sunday of October  (%s)  at 00:00 Local", endDate, startDate);
                } else {
                    if ((year == 2011) || (year == 2014)) {
                        calendarEndDST = getStartEndCalendar(Calendar.FEBRUARY, 4, Calendar.SUNDAY, 0);
                        calendarEndDST.add(Calendar.YEAR, year + 1);
                    } else {
                        calendarEndDST = getStartEndCalendar(Calendar.FEBRUARY, 3, Calendar.SUNDAY, 0);
                        calendarEndDST.add(Calendar.YEAR, year + 1);
                    }
                    AdjustDST();

                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST starts on the third Sunday of October  (%s)  at 00:00 Local\n\nDST ends on the third Sunday of February at 00:00 Local, or the next Sunday in case the third Sunday coincides with Carnival  (%s)", startDate, endDate);
                }

            } else {
                RegionDST = "The northern part of ";
                noDST();
            }
        } else if (CodeISO.equalsIgnoreCase("AR")) {
            //Argentina
            RegionDST = "Most locations in ";
            noDST();
        } else if (CodeISO.equalsIgnoreCase("CL")) {

           /* OffsetDST = 1;

            RuleDST = "As from April 26th, 2015 Chile stays on Summer Time until further notice.";
            initCalendars(true);
            calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, (int) (OffsetDST * 60));
            bDST_Time = true;*/

            OffsetDST = 1;
            initCalendars(true);
            //calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, -(int) (OffsetDST * 60));
            GetChile();

        } else if (CodeISO.equalsIgnoreCase("FK")) {
            //Falklands     //Sep Sun>=1  2:00
            //Apr Sun>=15 2:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.SEPTEMBER, firstWeek, Calendar.SUNDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            nowBeforeOctober = nowCalendar.before(calendarStartDST);
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.APRIL, 3, Calendar.SUNDAY, 2);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the third Sunday of April  (%s)  at 02:00 Local\n\nDST starts on the first Sunday of September  (%s)  at 02:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the first Sunday of September  (%s)  at 02:00 Local\n\nDST ends on the third Sunday of April  (%s)  at 02:00 Local", startDate, endDate);
            }

        } else if (CodeISO.equalsIgnoreCase("PY")) {
            //Paraguay      //Oct Sun>=15 0:00
            //Mar Sun>=8  0:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, 3, Calendar.SUNDAY, 0);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            nowBeforeOctober = nowCalendar.before(calendarStartDST);
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.MARCH, 2, Calendar.SUNDAY, 0);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the second Sunday of March  (%s)  at 00:00 Local\n\nDST starts on the third Sunday of October  (%s)  at 00:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the third Sunday of October  (%s)  at 00:00 Local\n\nDST ends on the second Sunday of March  (%s)  at 00:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("UY")) {
            //Uruguay       //Oct Sun>=1 2:00
            //Mar Sun>=8 2:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, 1, Calendar.SUNDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            nowBeforeOctober = nowCalendar.before(calendarStartDST);
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.MARCH, 2, Calendar.SUNDAY, 2);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the second Sunday of March  (%s)  at 02:00 Local\n\nDST starts on the first Sunday of October  (%s)  at 02:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the first Sunday of October  (%s)  at 02:00 Local\n\nDST ends on the second Sunday of March  (%s)  at 02:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("IL")) {
            // Israel    //Last Friday before April 2nd (not included !)
            //Saturday night before the fast of Yom Kippur
            initCalendars(true);

            calendarStartDST = getStartEndCalendar(Calendar.APRIL, 2, Calendar.FRIDAY, 2);
            if (calendarStartDST.get(Calendar.DAY_OF_MONTH) < 2)
                calendarStartDST = getStartEndCalendar(Calendar.MARCH, lastWeek, Calendar.FRIDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);

            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            int year = nowCalendar.get(Calendar.YEAR);

            int month = 0, day = 0;
            switch (year - 2006) {
                case 1:
                    month = 9;
                    day = 16;
                    break;
                case 2:
                    month = 10;
                    day = 5;
                    break;
                case 3:
                    month = 9;
                    day = 27;
                    break;
                case 4:
                    month = 9;
                    day = 12;
                    break;
                case 5:
                    month = 10;
                    day = 5;
                    break;
                case 6:
                    month = 9;
                    day = 23;
                    break;
                case 7:
                    month = 9;
                    day = 8;
                    break;
                case 8:
                    month = 9;
                    day = 28;
                    break;
                case 9:
                    month = 9;
                    day = 20;
                    break;
                case 10:
                    month = 10;
                    day = 9;
                    break;
                case 11:
                    month = 9;
                    day = 24;
                    break;
                case 12:
                    month = 9;
                    day = 16;
                    break;
                case 13:
                    month = 10;
                    day = 6;
                    break;
                case 14:
                    month = 9;
                    day = 27;
                    break;
                case 15:
                    month = 9;
                    day = 12;
                    break;
                case 16:
                    month = 10;
                    day = 2;
                    break;
                case 17:
                    month = 9;
                    day = 24;
                    break;
                case 18:
                    month = 10;
                    day = 6;
                    break;
                case 19:
                    month = 9;
                    day = 28;
                    break;
                case 20:
                    month = 9;
                    day = 20;
                    break;
                case 21:
                    month = 10;
                    day = 10;
                    break;
                case 22:
                    month = 9;
                    day = 24;
                    break;
                case 23:
                    month = 9;
                    day = 16;
                    break;
                case 24:
                    month = 10;
                    day = 16;
                    break;
                default:
                    break;
            }

            calendarEndDST = (GregorianCalendar) calendarCurrentLocalAFTime.clone();
            calendarEndDST.set(Calendar.DAY_OF_MONTH, day);//Calendar.SUNDAY
            calendarEndDST.set(Calendar.MONTH, month - 1);//Calendar.MARCH
            calendarEndDST.set(Calendar.HOUR_OF_DAY, 2);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on the last Friday before April 2nd  (%s)  at 02:00 Local\n\nDST ends on the Saturday 'night' before the fast of Yom Kippur (Sunday %s at 02:00 Local)", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("JO")) {
            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.FRIDAY, 1, Calendar.OCTOBER, lastWeek, Calendar.FRIDAY, 1);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);

            RuleDST = String.format("DST starts on the last Friday of March  (%s)  at 00:00 Local\n\nDST ends on the last Friday of October  (%s)  at 00:00 Local", startDate, endDate);

        } else if (CodeISO.equalsIgnoreCase("LB")) {
            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.SUNDAY, 1, Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 1);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on the last Sunday of March  (%s)  at 00:00 Local\n\nDST ends on the last Sunday of October  (%s)  at 00:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("IR")) {
            initCalendars(true, Calendar.MARCH, -1, 22, 0, Calendar.SEPTEMBER, -1, 22, 0);

            RuleDST = "DST starts on the 22nd of March  at 00:00 Local\n\nDST ends on the 22nd of September at 00:00 Local \n\n DST changeover is always on the 22nd, except for leap years, where the changeover is on the 21st.";
        } else if (CodeISO.equalsIgnoreCase("PK")) {
            noDST();
        } else if (CodeISO.equalsIgnoreCase("PS")) {
            //Palestine     //Last Friday before April 2nd (not included !)
            //Oct Fri>=15  0:00
            initCalendars(true, Calendar.APRIL, firstWeek, Calendar.FRIDAY, 1, Calendar.OCTOBER, 3, Calendar.FRIDAY, 1);
            if (calendarStartDST.get(Calendar.DAY_OF_MONTH) > 1)
                initCalendars(true, Calendar.MARCH, lastWeek, Calendar.FRIDAY, 1, Calendar.OCTOBER, 3, Calendar.FRIDAY, 1);
            String startDate = getFormattedShortDate(calendarStartDST);
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts last Friday before April 2nd  (%s)  at 00:00 Local\n\nDST ends on the third Friday of October  (%s)  at 00:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("WS")) {
            //Samoa         //Sep lastSun 0:00
            //Apr Sun>=1  1:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.SEPTEMBER, lastWeek, Calendar.SUNDAY, 0);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            nowBeforeOctober = nowCalendar.before(calendarStartDST);
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 1);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the first Sunday of April  (%s)  at 01:00 Local\n\nDST starts on the last Sunday of September  (%s)  at 00:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the last Sunday of September  (%s)  at 00:00 Local\n\nDST ends on the first Sunday of April  (%s)  at 01:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("SY")) {
            //  Syria         //Mar lastFri 0:00
            //Nov Sun>=1  0:00
            initCalendars(true, Calendar.MARCH, lastWeek, Calendar.FRIDAY, 1, Calendar.NOVEMBER, 3, Calendar.FRIDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);
            // String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on the last Friday of March  (%s)  at 00:00 Local\n\nDST ends on the third Friday of November 1st at 00:00 Local", startDate);
        } else if (CodeISO.equalsIgnoreCase("AU") ||
                CodeISO.equalsIgnoreCase("HM")) {
            if (((Math.abs(OffsetUTC - 9.5) <= kEpsilon) && (LatA > 26)) || ((Math.abs(OffsetUTC - 10.0) <= kEpsilon) && (LatA > 29)) ||
                    ((Math.abs(OffsetUTC - 10.0) <= kEpsilon) && (LatA > 28.6) && (LongA > 149.3))
                    || ((Math.abs(OffsetUTC - 10.0) <= kEpsilon) && (LatA > 28.3) && (LongA > 152))) {
                RegionDST = "The southern and south-eastern districts in ";

                initCalendars(true);
                calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, firstWeek, Calendar.SUNDAY, 3);
                String startDate = getFormattedShortDate(calendarStartDST);

                boolean nowBeforeOctober;
                GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
                nowBeforeOctober = nowCalendar.before(calendarStartDST);
                eqDST = true;
                int year = nowCalendar.get(Calendar.YEAR);
                calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 3);
                if (nowBeforeOctober) {
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST ends on the first Sunday of April  (%s)  at 03:00 Local\n\nDST starts on the first Sunday of October  (%s)  at 02:00 Local", endDate, startDate);
                } else {
                    calendarEndDST.add(Calendar.YEAR, year + 1);
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST starts on the first Sunday of October  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of April  (%s)  at 03:00 Local", startDate, endDate);
                }
            } else if (Math.abs(OffsetUTC - 10.5) <= kEpsilon) {
                RegionDST = "Lord Howe Island - ";
                //LH Island          //Oct lastSun 2:00  (30 minutes DST only)
                //Apr Sun>=1  2:00  (next year)
                initCalendars(false);
                OffsetDST = 0.5;
                calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, 2);
                String startDate = getFormattedShortDate(calendarStartDST);

                boolean nowBeforeOctober;
                GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
                nowBeforeOctober = nowCalendar.before(calendarStartDST);
                eqDST = true;
                int year = nowCalendar.get(Calendar.YEAR);
                calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 2);
                if (nowBeforeOctober) {
                    String endDate = getFormattedShortDate(calendarEndDST);

                    RuleDST = String.format("DST (30 minutes) ends on the first Sunday of April  (%s)  at 02:00 Local\n\nDST starts on the last Sunday of October  (%s)  at 02:00 Local", endDate, startDate);
                } else {
                    calendarEndDST.add(Calendar.YEAR, year + 1);
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST (30 minutes) starts on the last Sunday of October  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of April  (%s)  at 02:00 Local", startDate, endDate);
                }
            } else {
                RegionDST = "Queensland, Northern Territory and Western ";
                noDST();
            }
        } else if (CodeISO.equalsIgnoreCase("NZ")) {
            //New Zealand
            if (Math.abs(OffsetUTC - 12.0) <= kEpsilon) {
                RegionDST = "The continent of ";
                //New Zealand        //Sep lastSun 2:00s
                //Apr Sun>=1  2:00s  (next year)
                initCalendars(true);
                calendarStartDST = getStartEndCalendar(Calendar.SEPTEMBER, lastWeek, Calendar.SUNDAY, 2);
                String startDate = getFormattedShortDate(calendarStartDST);

                boolean nowBeforeOctober;
                GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
                nowBeforeOctober = nowCalendar.before(calendarStartDST);
                eqDST = true;
                int year = nowCalendar.get(Calendar.YEAR);
                calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 3);
                if (nowBeforeOctober) {
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST ends on the first Sunday of April  (%s)  at 03:00 Local\n\nDST starts on the last Sunday of September  (%s)  at 02:00 Local", endDate, startDate);
                } else {
                    calendarEndDST.add(Calendar.YEAR, year + 1);
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST starts on the last Sunday of September  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of April  (%s)  at 03:00 Local", startDate, endDate);
                }
            } else {
                RegionDST = "Chatham Islands - ";
                //Chatham Islands    //Sep lastSun 2:45s
                //Apr Sun>=1  2:45s  (next year)
                initCalendars(true);
                calendarStartDST = getStartEndCalendar(Calendar.SEPTEMBER, lastWeek, Calendar.SUNDAY, 3);
                String startDate = getFormattedShortDate(calendarStartDST);

                boolean nowBeforeOctober;
                GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
                nowBeforeOctober = nowCalendar.before(calendarStartDST);
                eqDST = true;
                int year = nowCalendar.get(Calendar.YEAR);
                calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 4);
                if (nowBeforeOctober) {
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST ends on the first Sunday of April  (%s)  at 03:45 Local\n\nDST starts on the last Sunday of September  (%s)  at 02:45 Local", endDate, startDate);
                } else {
                    calendarEndDST.add(Calendar.YEAR, year + 1);
                    String endDate = getFormattedShortDate(calendarEndDST);
                    RuleDST = String.format("DST starts on the last Sunday of September  (%s)  at 02:45 Local\n\nDST ends on the first Sunday of April  (%s)  at 03:45 Local", startDate, endDate);
                }
            }
        } else if (CodeISO.equalsIgnoreCase("EG")) {
            //Egypt         //Apr lastFri      0:00s
            noDST();
            //Before Ramadam  22:59s
/*			initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.APRIL, lastWeek, Calendar.FRIDAY,0);
			String startDate = getFormattedShortDate(calendarStartDST);
	  
			calendarEndDST = GetRamadan(0);//vbSaturday
			String endDate = getFormattedShortDate(calendarEndDST);
	        
			RuleDST = String.format("DST starts on the last Friday of April  (%s)  at 00:00 Local\n\nDST ends with start of Ramadan  (%s)  at 00:00 Local", startDate, endDate );
	*/
        } else if (CodeISO.equalsIgnoreCase("NA")) {
            //Namibia       //Sep Sun>=1 2:00
            //Apr Sun>=1 2:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.SEPTEMBER, firstWeek, Calendar.SUNDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            if (nowCalendar.before(calendarStartDST))
                nowBeforeOctober = true;
            else
                nowBeforeOctober = false;
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 2);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the first Sunday of April  (%s)  at 02:00 Local\n\nDST starts on the first Sunday of September  (%s)  at 02:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the first Sunday of September  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of April  (%s)  at 02:00 Local", startDate, endDate);
            }
        } else if (CodeISO.equalsIgnoreCase("BD")) {
            //Bangladesh    //Jun Fri>=15 22:59s
            //Oct lastSun 2:00s
            noDST();
        } else if (CodeISO.equalsIgnoreCase("MA") ||
                CodeISO.equalsIgnoreCase("EH")) {
            //Morocco            //April  1
            //Western Sahara     //Before Ramadam  22:59s
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.APRIL, 1, Calendar.SUNDAY, 0);
            String startDate = getFormattedShortDate(calendarStartDST);

            calendarEndDST = GetRamadan(Calendar.SATURDAY);//vbSaturday
            String endDate = getFormattedShortDate(calendarEndDST);
            RuleDST = String.format("DST starts on first Sunday of April  (%s)  at 00:00 Local\n\nDST ends on the Saturday  (%s)  before the start of Ramadan  at 24:00 Local", startDate, endDate);
        } else if (CodeISO.equalsIgnoreCase("FJ")) {
            //Fiji          'Oct Sun>=21 2:00
            //Mar Sun>=1  3:00
            initCalendars(true);
            calendarStartDST = getStartEndCalendar(Calendar.OCTOBER, 4, Calendar.SUNDAY, 2);
            String startDate = getFormattedShortDate(calendarStartDST);

            boolean nowBeforeOctober;
            GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            if (nowCalendar.before(calendarStartDST))
                nowBeforeOctober = true;
            else
                nowBeforeOctober = false;
            eqDST = true;
            int year = nowCalendar.get(Calendar.YEAR);
            calendarEndDST = getStartEndCalendar(Calendar.MARCH, 1, Calendar.SUNDAY, 3);
            if (nowBeforeOctober) {
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST ends on the first Sunday of March  (%s)  at 03:00 Local\n\nDST starts on the fourth Sunday of October  (%s)  at 02:00 Local", endDate, startDate);
            } else {
                calendarEndDST.add(Calendar.YEAR, year + 1);
                String endDate = getFormattedShortDate(calendarEndDST);
                RuleDST = String.format("DST starts on the fourth Sunday of October  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of March  (%s)  at 03:00 Local", startDate, endDate);
            }
        }else if(CodeISO.equalsIgnoreCase("TO")){
            Date dateChoose = new Date();
            if (calendarChoose != null) {
                dateChoose = calendarChoose.getTime();
            }
            //Date 4/10/2015
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, Calendar.NOVEMBER, 11);
            Date dateSpecial = calendar.getTime();
            if (dateChoose.before(dateSpecial)) {
                noDST();
            } else {
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateChoose.getTime());
                OffsetDST = 1;
                initCalendars(true);
                if(calendar.get(Calendar.MONTH) < 1){
                    calendarEndDST = getStartEndCalendar3(calendar.get(Calendar.YEAR), Calendar.JANUARY, 3, Calendar.SUNDAY, 3);
                    calendarStartDST = getStartEndCalendar3(calendar.get(Calendar.YEAR) - 1, Calendar.NOVEMBER, 1, Calendar.SUNDAY, 2);
                }else {
                    calendarEndDST = getStartEndCalendar3(calendar.get(Calendar.YEAR) + 1, Calendar.JANUARY, 3, Calendar.SUNDAY, 3);
                    calendarStartDST = getStartEndCalendar3(calendar.get(Calendar.YEAR), Calendar.NOVEMBER, 1, Calendar.SUNDAY, 2);
                }

                String startDate = getFormattedShortDate(calendarStartDST);
                String endDate = getFormattedShortDate(calendarEndDST);

                RuleDST = String.format("As from 2016 DST starts on the first Sunday of November  ( %s )  at 02:00 Local\n\nDST ends on the third Sunday of January next year  ( %s )  at 03:00 Local", startDate, endDate);
                eqDST = true;
            }
        }
        else {
            //"TM", "TJ", "UZ", "AI", "AG", "AW", "BB", "BZ", "KY", "CR", "DM", "DO", "SV", "GD"
            //"GP", , "JM", "MQ", "MS", "AN", "PA", "PR", "KN", "LC", "VC", "VI", "VG", "BO"
            //"CO", "EC", "GF", "GY", "PE", "GE", "SR", "TT", "VE", "BH", "KW", "OM", "QA", "SA"
            //"AE", "YE", "AF", "BT", "IO", "BN", "KH", "CN", "TL", "HK", "IN", "ID", "JP"
            //"KP", "KR", "LA", "MO", "MY", "MV", "MM", "NP", "PH", "SG", "TW", "TH", "VN", "MU"
            //"CX", "CK", "CC",, "PF", "GU", "KI", "MP", "MH", "FM", "NR", "NC", "NU", "NF"
            //"PW", "PG", "PN", "SB", "TK", "TO", "TV", "UM", "VU", "WF", "DZ", "AO", "BJ"
            //"BW", "BF", "BI", "CM", "CV", "CF", "TD", "KM", "CG", "CI", "CD", "DJ", "GQ", "ER"
            //"ET", "GA", "GM", "GH", "GN", "GW", "KE", "LS", "LR",  "MG", "MW", "ML", "MR"
            //"YT", "MZ", "NE", "NG", "RE", "RW", "SH", "ST", "SN", "SC", "SL", "SO"
            //"ZA", "SD", "SZ", "TZ", "TG", "UG", "ZM", "ZW", "AS", "TF", "KZ", "KG"
            //"GT", "HN", "NI", "IQ", "CN", "MN", "LK", "BV", "TN", "IS"

            noDST();

        } //main if


        //DST
        //RuleDST = RegionDST & AirfieldName & " :" & vbCrLf & vbCrLf & RuleDST
        //RuleDST = [[RegionDST stringByAppendingString:AirfieldName] stringByAppendingFormat:":\n\n%", RuleDST];
        if (RegionDST.equalsIgnoreCase(""))
            RuleDST = "\n" + RuleDST;    //\n
        else
            RuleDST = RegionDST + AirfieldName + "\n" + RuleDST;    //	\n
    }

    public void EuropeRule() {
        //Mar lastSun 1:00u
        //Oct lastSun 1:00u

        initCalendars(true, Calendar.MARCH, lastWeek, Calendar.SUNDAY, (int) (1 + OffsetUTC), Calendar.OCTOBER, lastWeek, Calendar.SUNDAY, (int) (1 + OffsetUTC + 1));//OffsetDST=1
        String startDate = getFormattedShortDate(calendarStartDST);
        String endDate = getFormattedShortDate(calendarEndDST);
        String startTime = String.format("0%d:00", (int) (1 + OffsetUTC));//getFormattedShortTime(calendarStartDST);
        String endTime = String.format("0%d:00", (int) (1 + OffsetUTC + 1));//getFormattedShortTime(calendarEndDST);
        RuleDST = String.format("DST starts on the last Sunday of March at 01:00 UTC\n(%s at %s Local)\n\nDST ends on the last Sunday of October at 01:00 UTC\n(%s at %s Local)", startDate, startTime, endDate, endTime);
    }

    //PL-949
    private void TurkeyRule() {
        initCalendars(true);
        RuleDST = "As from September 2016 it was decided to stay on DST.";
        //OffsetDST = 1;
    }
    //End PL-949

    public void CanadaUsRule() {
        //Mar Sun>=8 2:00
        //Nov Sun>=1 2:00
        initCalendars(true, Calendar.MARCH, 2, Calendar.SUNDAY, 2, Calendar.NOVEMBER, firstWeek, Calendar.SUNDAY, 1);
        String startDate = getFormattedShortDate(calendarStartDST);
        String endDate = getFormattedShortDate(calendarEndDST);
        RuleDST = String.format("DST starts on the second Sunday of March  (%s)  at 02:00 Local\n\nDST ends on the first Sunday of November  (%s)  at 02:00 Local", startDate, endDate);
    }


    public void noDST() {
        initCalendars(false);
        String countryOrRegion;
        if (RegionDST.equals("")) {
            countryOrRegion = "country";
        } else {
            countryOrRegion = "region";
        }
        RuleDST = String.format("This %s does not observe DST.", countryOrRegion);
        OffsetDST = 0;
    }

    //JIRA PL-705 step 1/4
    GregorianCalendar GetChile() {
        GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int yearNow = nowCalendar.get(Calendar.YEAR);
        switch (yearNow) {
            case 2010:
                calendarStartDST = getStartEndCalendar2(yearNow, 3, 1, Calendar.SUNDAY, 3);
                calendarEndDST = getStartEndCalendar2(yearNow, 9, 8, Calendar.SATURDAY, 4);
                break;
            case 2011:
                calendarStartDST = getStartEndCalendar2(yearNow, 4, 2, Calendar.SUNDAY, 3);
                calendarEndDST = getStartEndCalendar2(yearNow, 7, 18, Calendar.SATURDAY, 4);
                break;
            case 2012:
            case 2013:
            case 2014:
                calendarStartDST = getStartEndCalendar2(yearNow, 3, 23, Calendar.SUNDAY, 3);
                calendarEndDST = getStartEndCalendar2(yearNow, 8, 1, Calendar.SUNDAY, 4);
                break;
            case 2015:
                break;
            case 2016:
                calendarStartDST = getStartEndCalendar2(yearNow, 4, 8, Calendar.SUNDAY, 0);
                calendarEndDST = getStartEndCalendar2(yearNow, 7, 8, Calendar.SUNDAY, 0);
                break;
        }
        if (yearNow < 2010) {
            calendarStartDST = getStartEndCalendar2(yearNow, 3, 1, Calendar.SUNDAY, 3);
            calendarEndDST = getStartEndCalendar2(yearNow, 9, 8, Calendar.SATURDAY, 4);
        } else if (yearNow > 2016) {
            calendarStartDST = getStartEndCalendar2(yearNow, 4, 8, Calendar.SUNDAY, 0);
            calendarEndDST = getStartEndCalendar2(yearNow, 7, 8, Calendar.SUNDAY, 0);
        }
        String startDate = getFormattedShortDate(calendarStartDST);
        String endDate = getFormattedShortDate(calendarEndDST);
        RuleDST = String.format("As from 2016 DST starts on the second Sunday of May  ( %s )  at 00:00 Local\n\nDST ends on the second Sunday of August  ( %s )  at 00:00 Local", startDate, endDate);
        eqDST = true;
        return null;
    }

    //End JIRA PL-705
    GregorianCalendar GetRamadan(int WhatDayBefore) {
        GregorianCalendar ramadan;

        GregorianCalendar nowCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int LocYear;
        LocYear = nowCalendar.get(Calendar.YEAR);
        if (WhatDayBefore > 0) {
            WhatDayBefore = (WhatDayBefore + 6) % 7;
        }

        int year = 0, month = 0, day = 0;
        switch (LocYear) {
            case 2010:
                year = 2010;
                month = 8;
                day = 11;
                break;
            case 2011:
                year = 2011;
                month = 8;
                day = 1;
                break;
            case 2012:
                year = 2012;
                month = 7;
                day = 20;
                break;
            case 2013:
                year = 2013;
                month = 7;
                day = 9;
                break;
            default:
                break;
        }

        ramadan = (GregorianCalendar) calendarCurrentLocalAFTime.clone();
        ramadan.set(Calendar.DAY_OF_MONTH, day);//Calendar.SUNDAY
        ramadan.set(Calendar.MONTH, month - 1);//Calendar.MARCH


        if (WhatDayBefore > 0) {
            int weekday = ramadan.get(Calendar.DAY_OF_WEEK);
            //adjust: Sunday is 1, but it should be Monday
            int adjustedWeekDay = 0;
            switch (weekday) {
                case 2://monday
                    adjustedWeekDay = 1;
                    break;
                case 3:
                    adjustedWeekDay = 2;
                    break;
                case 4:
                    adjustedWeekDay = 3;
                    break;
                case 5:
                    adjustedWeekDay = 4;
                    break;
                case 6:
                    adjustedWeekDay = 5;
                    break;
                case 7://saturday
                    adjustedWeekDay = 6;
                    break;
                case 1://sunday
                    adjustedWeekDay = 7;
                    break;
                default:
                    break;
            }

            if (adjustedWeekDay > WhatDayBefore) {
                ramadan.add(Calendar.DAY_OF_MONTH, (WhatDayBefore - adjustedWeekDay));
            } else {
                ramadan.add(Calendar.DAY_OF_MONTH, (WhatDayBefore - adjustedWeekDay - 7));
            }
        }
        return ramadan;
    }

    String getFormattedShortDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        return sdf.format(cal.getTime());
    }

    void initCalendars(boolean bOffsetDST, int startMonthtDST, int startWeek, int startDay, int startHour,
                       int endMonthtDST, int endWeek, int endDay, int endHour) {// init UTC and local airfield nowCalendar
        if (bOffsetDST) {
            OffsetDST = 1.0;
        }
        calendarCurrentUTC = new GregorianCalendar();
        if (calendarChoose != null)
            calendarCurrentUTC.set(Calendar.YEAR, calendarChoose.get(Calendar.YEAR));
        int zoneOffset = calendarCurrentUTC.get(Calendar.ZONE_OFFSET) / (60 * 1000);//in minutes
        int dstOffset = calendarCurrentUTC.get(Calendar.DST_OFFSET) / (60 * 1000);
        int minutes = calendarCurrentUTC.get(GregorianCalendar.MINUTE);
        calendarCurrentUTC.set(GregorianCalendar.MINUTE, minutes - (zoneOffset + dstOffset));

        calendarCurrentLocalAFTime = (GregorianCalendar) calendarCurrentUTC.clone();
        int minutesOffset = (int) (OffsetUTC * 60);//
        calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, minutesOffset);

        calendarStartDST = getStartEndCalendar(startMonthtDST, startWeek, startDay, startHour);
        calendarEndDST = getStartEndCalendar(endMonthtDST, endWeek, endDay, endHour);
        //	AdjustDST();
    }

    void initCalendars(boolean bOffsetDST) {
        if (bOffsetDST) {
            OffsetDST = 1.0;
        }
        calendarCurrentUTC = new GregorianCalendar();
        Log.d("calendarCurrentUTC", calendarCurrentUTC.getTime().toString());
        if (calendarChoose != null) {
            Log.d("calendarChoose", calendarChoose.getTime().toString());
            calendarCurrentUTC.set(Calendar.YEAR, calendarChoose.get(Calendar.YEAR));
            calendarCurrentUTC.set(Calendar.MONTH, calendarChoose.get(Calendar.MONTH));
            calendarCurrentUTC.set(Calendar.DAY_OF_MONTH, calendarChoose.get(Calendar.DAY_OF_MONTH));
        }

        Log.d("calendarCurrentUTC 2", calendarCurrentUTC.getTime().toString());
        int zoneOffset = calendarCurrentUTC.get(Calendar.ZONE_OFFSET) / (60 * 1000);//in minutes
        int dstOffset = calendarCurrentUTC.get(Calendar.DST_OFFSET) / (60 * 1000);
        int minutes = calendarCurrentUTC.get(GregorianCalendar.MINUTE);
        calendarCurrentUTC.set(GregorianCalendar.MINUTE, minutes - (zoneOffset + dstOffset));

        calendarCurrentLocalAFTime = (GregorianCalendar) calendarCurrentUTC.clone();
        int minutesOffset = (int) (OffsetUTC * 60);//
        calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, minutesOffset);
        Log.d("Current1", calendarCurrentLocalAFTime.getTime().toString());
    }

    GregorianCalendar getStartEndCalendar(int startEndMonthtDST, int startEndWeek, int startEndDay, int startEndHour) {
        //	GregorianCalendar cal = (GregorianCalendar) calendarCurrentLocalAFTime.clone();
        GregorianCalendar cal = new GregorianCalendar();
        if (startEndDay >= 0 && startEndDay < 8)
            cal.set(Calendar.DAY_OF_WEEK, startEndDay);//Calendar.SUNDAY
        else
            cal.set(Calendar.DAY_OF_MONTH, startEndDay);//Calendar.SUNDAY
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, startEndWeek);//-1
        cal.set(Calendar.MONTH, startEndMonthtDST);//Calendar.MARCH
        cal.set(Calendar.HOUR_OF_DAY, startEndHour);
        cal.set(Calendar.MINUTE, 0);//default

        return cal;
    }

    GregorianCalendar getStartEndCalendar2(int year, int startEndMonthtDST, int startEndDayOfMonth, int startEndDayOfWeek, int startEndHour) {
        //	GregorianCalendar cal = (GregorianCalendar) calendarCurrentLocalAFTime.clone();
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, startEndMonthtDST);//Calendar.MARCH
        cal.set(Calendar.DAY_OF_WEEK, startEndDayOfWeek);//Calendar.SUNDAY
        cal.set(Calendar.DAY_OF_MONTH, startEndDayOfMonth);

        //cal.set(Calendar.HOUR_OF_DAY, startEndHour);
        //cal.set(Calendar.MINUTE, 0);//default
        cal.set(Calendar.HOUR, startEndHour);
        return cal;
    }
    GregorianCalendar getStartEndCalendar3(int year, int startEndMonthDST, int startEndWeekOfMonth, int startEndDayOfWeek, int startEndHour) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, startEndMonthDST);//Calendar.MARCH
        cal.set(Calendar.WEEK_OF_MONTH,startEndWeekOfMonth);
        cal.set(Calendar.DAY_OF_WEEK, startEndDayOfWeek);//Calendar.SUNDAY
        cal.set(Calendar.HOUR, startEndHour);
        return cal;
    }
    void AdjustDST() {

        if (eqDST)// South of equator , here start and end in same year
        {
            //JIRA PL-705 step 2/4
            if (CodeISO.equalsIgnoreCase("CL")) {
                if (calendarStartDST != null && calendarEndDST != null && calendarCurrentLocalAFTime.after(calendarStartDST) && calendarCurrentLocalAFTime.before(calendarEndDST)) {
                    bDST_Time = false;
                    //calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, (int) (OffsetDST * 60));
                } else {
                    calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, (int) (OffsetDST * 60));
                    bDST_Time = true;
                }
            } else if (calendarStartDST != null && calendarEndDST != null && calendarCurrentLocalAFTime.after(calendarStartDST) && calendarCurrentLocalAFTime.before(calendarEndDST)) {
                calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, (int) (OffsetDST * 60));
                bDST_Time = true;
            }
            //End JIRA PL-705
        } else {
            if (calendarStartDST != null && calendarEndDST != null && calendarCurrentLocalAFTime.after(calendarStartDST) && calendarCurrentLocalAFTime.before(calendarEndDST)) {
                calendarCurrentLocalAFTime.add(GregorianCalendar.MINUTE, (int) (OffsetDST * 60));
                bDST_Time = true;
            }
            //PL-949
            if (CodeISO.equalsIgnoreCase("TR")) {
                Date dateChoose = new Date();
                if (calendarChoose != null) {
                    dateChoose = calendarChoose.getTime();
                }
                //Date 8/9/2016
                Calendar calendar = Calendar.getInstance();
                calendar.set(2016, Calendar.MAY, 8);
                Date dateSpecial = calendar.getTime();
                if (!dateChoose.before(dateSpecial)) {
                    bDST_Time = true;
                }
            }
        }
    }

    public void CalcTimeLocal(boolean DisplayDate)//Date TimeUTC,
    {
        GetTimeZone();
        AdjustDST();
    }

    public void CalcTimeLocal(Calendar calendar)//Date TimeUTC,
    {
        calendarChoose = calendar;
        GetTimeZone();
        AdjustDST();
    }

    Calendar AdjustUTC_DST(Calendar calend, boolean local) {
        if (eqDST)// South of equator , here start and end in same year
        {
            if (calend.after(calendarStartDST) || calend.before(calendarEndDST)) {
                if (local)
                    calend.add(GregorianCalendar.MINUTE, (int) ((OffsetDST + OffsetUTC) * 60));
                else
                    calend.add(GregorianCalendar.MINUTE, (int) (-(OffsetDST + OffsetUTC) * 60));
                bDST_Time = true;
            } else {
                if (local)
                    calend.add(GregorianCalendar.MINUTE, (int) ((OffsetUTC) * 60));
                else
                    calend.add(GregorianCalendar.MINUTE, (int) (-OffsetUTC * 60));
            }
        } else {
            if (calend.after(calendarStartDST) && calend.before(calendarEndDST)) {
                if (local)
                    calend.add(GregorianCalendar.MINUTE, (int) ((OffsetDST + OffsetUTC) * 60));
                else
                    calend.add(GregorianCalendar.MINUTE, (int) (-(OffsetDST + OffsetUTC) * 60));
                bDST_Time = true;
            } else {
                if (local)
                    calend.add(GregorianCalendar.MINUTE, (int) ((OffsetUTC) * 60));
                else
                    calend.add(GregorianCalendar.MINUTE, (int) (-OffsetUTC * 60));

            }

        }
        return calend;
    }

}
