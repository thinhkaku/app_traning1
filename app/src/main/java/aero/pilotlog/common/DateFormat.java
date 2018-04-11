package aero.pilotlog.common;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hung.nn on 12/19/13.
 * Date Format
 */
public class DateFormat {

    public static final SimpleDateFormat DATE_MONTH_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat DATE_MONTH = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    public static final SimpleDateFormat HTML_NAME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static final SimpleDateFormat DB_DATE_FORMAT_LOCAL_ENG = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.getDefault());
    public static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyyMM", Locale.getDefault());
    public static final SimpleDateFormat WEEK_VIEW_WEEKDAY_FORMAT_TABLET = new SimpleDateFormat("EEE dd", Locale.getDefault());
    public static final SimpleDateFormat WEEK_VIEW_WEEKDAY_FORMAT = new SimpleDateFormat("EEE\ndd", Locale.getDefault());
    public static final SimpleDateFormat WEEK_VIEW_TITLE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat CREW_DOCK_DATE_FORMAT = new SimpleDateFormat("d MMM yy, EEE", Locale.US);
    public static final SimpleDateFormat SABRE_DATE_FORMAT_JET_TIME = new SimpleDateFormat("ddMMMyy HH:mm", Locale.ENGLISH);
    public static final SimpleDateFormat SABRE_DATE_FORMAT_MAROC_TIME = new SimpleDateFormat("ddMMyy HH:mm", Locale.ENGLISH);
    public static final SimpleDateFormat SYNC_LOG_FORMAT = new SimpleDateFormat("MMMM dd", Locale.getDefault());
    public static final SimpleDateFormat PARKING_POSITION_FORMAT = new SimpleDateFormat("dd MMM - HH:mm", Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyMMddHHmmssZ", Locale.getDefault());
    public static final SimpleDateFormat LICENSE_PAGE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final SimpleDateFormat SABRE_DATE_END_PARSE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    public static final SimpleDateFormat VIEW_MONTH_FORMAT = new SimpleDateFormat("yyyyMM", Locale.getDefault());
    public static final SimpleDateFormat LIST_VIEW_TITLE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat MONTH_VIEW_DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat AIRFIELD_LOCAL_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    public static final SimpleDateFormat AIRFIELD_WEATHER_METAR_FORMAT = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    public static final SimpleDateFormat VERSION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    public static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEE", Locale.getDefault());
    public static final SimpleDateFormat FULL_MONTH_FORMAT = new SimpleDateFormat("MMMM", Locale.getDefault());
    public static final SimpleDateFormat MONTH_JS = new SimpleDateFormat("MMM", Locale.ENGLISH);
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static final SimpleDateFormat FULL_MONTH_EXCEPT_HOUR = new SimpleDateFormat("dd MMMM", Locale.getDefault());
    public static final SimpleDateFormat DATE_PICKER_FORMAT = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
}
