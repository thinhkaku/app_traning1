package aero.pilotlog.utilities;

import android.text.TextUtils;

import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.widgets.ItemsFlightView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by tuan.na on 9/9/2015.
 */
public class TimeUtils {


    public static int convertHourToMin(String pHour) {
        int returnTime = 0;
        if (TextUtils.isEmpty(pHour)) {
            return returnTime;
        }
        try {
            if (pHour.contains(":")) {
                String[] time = pHour.split(":");
                return Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
            }
            pHour = pHour.replace(",", ".");
            return (int) (Double.parseDouble(pHour) * 60);
        } catch (Exception e) {
            e.printStackTrace();
            return returnTime;
        }
    }

    public static int convertHourToMin(ItemsFlightView itemsFlightView) {//, boolean isNotPreserveAccuracy) {
       /* if(!isNotPreserveAccuracy && itemsFlightView.getMinutesData()!=0 ) //add condition 2 because when edit dont have minute data
            return itemsFlightView.getMinutesData();*/
        int returnTime = 0;
        String pHour = itemsFlightView.getDescription();
        if (TextUtils.isEmpty(pHour)) {
            return returnTime;
        }
        try {
            if (pHour.contains(":")) {
                String[] time = pHour.split(":");
                return Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
            }
            pHour = pHour.replace(",", ".");
            return (int) (Double.parseDouble(pHour) * 60);
        } catch (Exception e) {
            e.printStackTrace();
            return returnTime;
        }
    }



    public static String convertMinuteToHour(String pMinutes, boolean pLogInDecimal) {
        String time = MCCPilotLogConst.STRING_EMPTY;
        if (TextUtils.isEmpty(pMinutes) /*|| pMinutes.equals("0")*/ || pMinutes.equals("null")) {
            return time;
        }
        double minuteTotal = 0;
        if (pLogInDecimal) {
            try {
                pMinutes = pMinutes.replace(",", ".");
                minuteTotal = Double.parseDouble(pMinutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            double doubleHour = minuteTotal / 60;
            DecimalFormat dcf = new DecimalFormat("0.0");
            dcf.setRoundingMode(RoundingMode.HALF_UP);
            time = dcf.format(doubleHour);
        } else {
            try {
                pMinutes = pMinutes.replace(",", ".");
                minuteTotal = Double.parseDouble(pMinutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int hour = (int) (minuteTotal / 60);
            int minute = (int) (minuteTotal % 60);
            DecimalFormat decimalFormat = new DecimalFormat("00");
            time = String.format("%s:%s", decimalFormat.format(hour), decimalFormat.format(minute));
        }
        System.gc();
        return time;
    }


    public static double getTimeOffset(String pAirfieldTimeZone) {
        try {
            TimeZone tz = TimeZone.getTimeZone(pAirfieldTimeZone);
            Calendar c = Calendar.getInstance(tz);
            long offset = tz.getOffset(c.getTimeInMillis());
            return (offset / 1000 / 60 / 60.0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static int calcTotalMinute(String pOffTime, String pOnTime, int accuracy) {
        int returnValue = -1;
        if (pOffTime != null && pOnTime != null) {
            if (!pOffTime.contains(":") || !pOnTime.contains(":")) {
                return -1;
            }
            try {
                String[] offTimeArr = pOffTime.split(":");
                String[] onTimeArr = pOnTime.split(":");
                int offHour = Integer.parseInt(offTimeArr[0]);
                int offMin = Integer.parseInt(offTimeArr[1]);
                int onHour = Integer.parseInt(onTimeArr[0]);
                int onMin = Integer.parseInt(onTimeArr[1]);
                int offTime = offHour * 60 + offMin;
                int onTime = onHour * 60 + onMin;
                switch (accuracy){
                    case 0:
                        returnValue = (onTime - offTime + 1440)%1440;
                        break;
                    case 1:
                        returnValue = (onTime - offTime + 1440)%1440;
                        returnValue = (int)((returnValue + 2.5)/5)*5;
                        break;
                    case 2:
                        returnValue = (onTime - offTime + 1440)%1440;
                        returnValue = (int)((returnValue + 3)/6)*6;
                        break;
                }
                if (returnValue == 1440) {
                    returnValue = 0;
                }
            } catch (Exception ignored) {
                return -1;
            }
        }

        return returnValue;
    }

}
