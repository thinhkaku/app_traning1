package aero.pilotlog.utilities;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by tuan.pv on 2015/08/18.
 */
public class DateTimeUtils {
    public static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static SimpleDateFormat mSimpleDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
    public static SimpleDateFormat mSimpleWeekDayFormat = new SimpleDateFormat("EEEE", Locale.US);
    public static SimpleDateFormat mSimpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    /**
     * parse string to Date object
     *
     * @param pDate string input
     * @return Date
     */
    private static Date convertStringToDate(String pDate) {
        try {
            return mSimpleDateFormat.parse(pDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * format Date to string
     *
     * @param pDate Date input
     * @return string
     */
    public static String formatDateToString(Date pDate) {
        return mSimpleDateFormat.format(pDate);
    }

    /**
     * get date current.
     *
     * @return date
     */
    public static Date getCurrentDate() {
        try {
            return mSimpleDateFormat.parse(mSimpleDateFormat.format(new Date()));
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * format Date object to weekday as string
     *
     * @param pDate date input
     * @return a weekday
     */
    public static String getWeekDay(Date pDate) {
        return mSimpleWeekDayFormat.format(pDate);
    }

    /**
     * format Date object to hour as string
     *
     * @param pDate date input
     * @return hour
     */
    public static String convertDateTimeToHour(Date pDate) {
        return mSimpleTimeFormat.format(pDate);
    }

    /**
     * get date time of last sync
     *
     * @param pTime
     * @return
     */
    public static String getDateLastSync(long pTime) {
        String strTime = "";
        if (pTime > 0) {
            Date date = new Date(pTime);
            strTime = formatDateToString(date) + " at " + convertDateTimeToHour(date);
        }

        return strTime;
    }

    public static Date getDateFromTimestamp(String pTime, boolean pIsAddSixMonths) {
        Date date = null;
        long time = System.currentTimeMillis();
        try {
            time = Long.parseLong(pTime);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        if (pIsAddSixMonths) {
            calendar.add(Calendar.MONTH, 6);
        }

        try {
            if (pIsAddSixMonths) {
                date = mSimpleDateFormat.parse(formatDateToString(calendar.getTime()));
            } else {
                date = mSimpleDateTimeFormat.parse(mSimpleDateTimeFormat.format(calendar.getTime()));
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return date;
    }

    public static Date getDateFromCalendar(String pText) {
        Date result = null;
        try {
            Calendar calendar = getCalendar(pText);
            String str = formatDateToString(calendar.getTime());
            result = convertStringToDate(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * from string datetime convert to calendar
     *
     * @param pDateTime datetime string
     * @return calendar
     */
    public static Calendar getCalendar(String pDateTime) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        if (!TextUtils.isEmpty(pDateTime) && pDateTime.length() == 8) {
            String year, month, day;
            year = pDateTime.substring(0, 4);
            month = pDateTime.substring(4, 6);
            day = pDateTime.substring(6);
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day), 0, 0);
        }
        return calendar;
    }

    public static Calendar getCurrentCalendar(String pDateTime) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if (!TextUtils.isEmpty(pDateTime) && pDateTime.length() == 8) {
            String year, month, day;
            year = pDateTime.substring(0, 4);
            month = pDateTime.substring(4, 6);
            day = pDateTime.substring(6);
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        }
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static String getDateStr(int pMinusDay) {
        String month, day;
        int iMonth, iDay;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -pMinusDay);

        iMonth = calendar.get(Calendar.MONTH) + 1;
        iDay = calendar.get(Calendar.DAY_OF_MONTH);
        month = iMonth < 10 ? "0" + iMonth : "" + iMonth;
        day = iDay < 10 ? "0" + iDay : "" + iDay;

        return calendar.get(Calendar.YEAR) + month + day;
    }

    public static Calendar getDateFromString(String pStrDate, String pMinutes) {
        if (!TextUtils.isEmpty(pStrDate)) {
            Calendar calendar = getCalendar(pStrDate);
            int min;
            try {
                min = Integer.parseInt(pMinutes);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                min = 0;
            }
            int hour = min / 60;
            min = min % 60;

            if (hour > 0) {
                int day = hour / 24;
                if (day > 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, day);
                }
                calendar.add(Calendar.HOUR_OF_DAY, hour % 24);
            }
            calendar.add(Calendar.MINUTE, min);

            return calendar;
        } else {
            return null;
        }

    }

    public static String getTimeString(String pMinutes, boolean pIsTotal, boolean pIsDecimal, String pTimeDecimal) {
        String sTime = "";
        int minutes = 0;
        if (pMinutes != null && !pMinutes.equals("")) {
            minutes = Integer.parseInt(pMinutes);
        }
        if (minutes > 0) {
            if (pTimeDecimal.equals("1") && pIsDecimal) {
                double min = Math.round(minutes * 10 / 60.0f) / 10.0f; //+ 0.4f
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                sTime = decimalFormat.format(min);
            } else {
                int h = minutes / 60;
                int m = minutes % 60;
                String hr = "" + h;
                if (h < 10 && !pIsTotal)
                    hr = "0" + hr;
                if (m < 10)
                    sTime = hr + ":0" + m;
                else
                    sTime = hr + ":" + m;
            }
        }
        return sTime;
    }

    public static int getHoursFromString(String pMinutes) {
        int minutes = 0;
        if (pMinutes != null && !pMinutes.equals("")) {
            minutes = Integer.parseInt(pMinutes);
        }

        return minutes / 60;
    }

    public static int getMinutesFromString(String pMinutes) {
        int minutes = 0;
        if (pMinutes != null && !pMinutes.equals("")) {
            minutes = Integer.parseInt(pMinutes);
        }

        return minutes % 60;
    }

    public static int getMinutesFromWidget(String pMinutes) {
        int minutes = 0;
        try {
            String sHour, sMinute;
            int hour, minute;

            pMinutes = pMinutes.replace(",", ":");
            pMinutes = pMinutes.replace(".", ":");

            String[] pMinutesArr = pMinutes.split(":");
            if (pMinutesArr.length > 1) {
                sHour = pMinutesArr[0];
                sMinute = pMinutesArr[1];
                hour = Integer.parseInt(sHour);
                minute = Integer.parseInt(sMinute);
                minutes = hour * 60 + minute;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return minutes;
    }

//    private static Date addHourOfDay(Date dt, int hour) {
//        if (hour > 0) {
//            int day = hour / 24;
//            if (day > 0) {
//                dt = dt.AddDays(day);
//            }
//            dt = dt.AddHours(hour % 24);
//        }
//        return dt;
//    }

    //v5 PL-1037
    public static long getCurrentUTCUnixTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("utc"));
        return calendar.getTimeInMillis() / 1000;
    }
    //End PL-1037
}
