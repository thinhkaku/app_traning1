package aero.pilotlog.utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.view.View;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.common.AircraftInfoConst;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.Navigation;
import aero.pilotlog.common.RouteInfoCalculator;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Flight;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.widgets.ItemsFlightView;
import aero.pilotlog.widgets.MccDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by phuc.dd on 3/28/2017.
 */
public class FlightUtils {
    private final Context mContext;
    private static FlightUtils mInstance;
    DatabaseManager databaseManager;
    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.US);
    public FlightUtils.TimeMode timeMode = FlightUtils.TimeMode.LOCAL;
    public boolean isLogTimeDecimal;
    public static final int REQUEST_CODE_SIGNATURE = 2110;
    public HashMap<String, Boolean> conditionLoad;
    private Pilot pilotSelf = null;

    public static void setmInstance(FlightUtils mInstance) {
        FlightUtils.mInstance = mInstance;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    private int accuracy;

    public static FlightUtils getInstance(Context pContext, DatabaseManager databaseManager) {

        if (mInstance == null) {
            mInstance = new FlightUtils(pContext, databaseManager);
        }
        return mInstance;
    }

    public FlightUtils(final Context pContext, DatabaseManager databaseManager) {
        this.mContext = pContext;
        this.databaseManager = databaseManager;
    }

    public final int RED_COLOR = android.R.color.holo_red_light;

    public boolean isOlderDate(String todayDate, String savedDate) {
        SimpleDateFormat simpleDateFormatS = new SimpleDateFormat("yyyyMMdd", Locale.US);
        try {
            Date toDay = simpleDateFormatS.parse(todayDate);
            Date savedDay = simpleDateFormatS.parse(savedDate);
            long diff = toDay.getTime() - savedDay.getTime();
            return (diff / 86400000 > 30);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getSettingDataByCode(int settingCode) {
        SettingConfig setting = databaseManager.getSetting(settingCode);
        if (setting != null) {
            String value = setting.getData();
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        return "";
    }

    public String getScheduledHour(String hour) {
        if (TextUtils.isEmpty(hour)) hour = "00:00";
        return hour;
    }

    public String getTotalScheduleAndArr(String hour, boolean isLogTimeDecimal) {
        hour = getTimeDisplay(hour, isLogTimeDecimal, true, isLogTimeDecimal);
        if (TextUtils.isEmpty(hour) || hour.equals("0.0") || hour.equals("0:00")) {
            hour = "‒‒";
        }
        return hour;
    }

  /*  public String getTotalAir(String hour, boolean isLogTimeDecimal) {
        if (TextUtils.isEmpty(hour) || hour.equals("0.0") || hour.equals("00:00")) hour = "-:-";
        return hour;
    }*/

    public boolean validateAirfieldIdentifier(String pValue) {
        return pValue.length() == 3 || pValue.length() == 4;
    }

    public Airfield setTextAirfield(String pIcaoIata, ItemsFlightView itemsAirfield, boolean isSettingIata) {
        if (TextUtils.isEmpty(pIcaoIata)) {
            itemsAirfield.setFootNote("");
            return null;
        }
        Airfield airfield = databaseManager.getAirfieldByICAOIATA(pIcaoIata);
        if (airfield != null) {
            itemsAirfield.setDescription(isSettingIata ? (TextUtils.isEmpty(airfield.getAFIATA()) ? airfield.getAFICAO() : airfield.getAFIATA())
                    : (TextUtils.isEmpty(airfield.getAFICAO()) || airfield.getAFICAO().equalsIgnoreCase("zzzz") ? airfield.getAFIATA() : airfield.getAFICAO()));
            //itemsAirfield.setDescription(isSettingIata ? airfield.getAFIATA() : airfield.getAFICAO());
            if (airfield.getShowList())
                itemsAirfield.setFootNote(airfield.getAFName(), R.color.grey_footer_text);
            else
                itemsAirfield.setFootNote(airfield.getAFName(), R.color.color_amber);
        } else {
            itemsAirfield.clearValue();
            itemsAirfield.setFootNote(mContext.getString(R.string.invalid_airfield), RED_COLOR);
        }
        return airfield;
    }

    public void setTextAirfield(Airfield airfield, ItemsFlightView itemsAirfield, boolean isSettingIata) {
        if (airfield == null) return;
        itemsAirfield.setDescription(isSettingIata ? (TextUtils.isEmpty(airfield.getAFIATA()) ? airfield.getAFICAO() : airfield.getAFIATA())
                : (TextUtils.isEmpty(airfield.getAFICAO()) || airfield.getAFICAO().equalsIgnoreCase("zzzz") ? airfield.getAFIATA() : airfield.getAFICAO()));
        //itemsAirfield.setDescription(isSettingIata ? airfield.getAFIATA() : airfield.getAFICAO());
        if (airfield.getShowList())
            itemsAirfield.setFootNote(airfield.getAFName(), R.color.grey_footer_text);
        else
            itemsAirfield.setFootNote(airfield.getAFName(), R.color.color_amber);
    }

    public Pilot searchAndSetPilot(final String value, final ItemsFlightView itemsFlightView) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        final Pilot pilot = databaseManager.getPilotByNameOrRef(value);
        ((BaseActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pilot != null) {
                    itemsFlightView.setDescription(pilot.getPilotName());
                    itemsFlightView.setFootNote(pilot.getPilotRef(), R.color.grey_footer_text);
                } else {
                    itemsFlightView.setDescription(value, RED_COLOR);
                    itemsFlightView.setFootNote("");
                }
            }
        });
        return pilot;
    }

    public String getTimeDisplay(String value, boolean canConvertToDecimal, boolean isCheckHour, boolean isLogTimeDecimal) {
        if (!TextUtils.isEmpty(value)) {
            value = value.replace(":", "");
            String timeDisplay;
            double time;
            try {
                value = value.replace(",", ".");
                if (value.contains(".")) {
                    value = value.replace(".", "");
                }
                time = Double.parseDouble(value);
            } catch (Exception e) {
                e.printStackTrace();
                MccDialog.getOkAlertDialog(mContext, R.string.invalid_time_title, R.string.invalid_time_content).show();
                return "";
            }

            DecimalFormat fractionFormat = new DecimalFormat("###0.0");
            if (isLogTimeDecimal && canConvertToDecimal) {
                if (time > 240) {
                    MccDialog.getOkAlertDialog(mContext, R.string.invalid_time_title, R.string.invalid_time_content).show();
                    time = 240;
                }
                timeDisplay = fractionFormat.format(time / 10);

            } else {
                if (value.length() > 4) {
                    MccDialog.getOkAlertDialog(mContext, R.string.invalid_time_title, R.string.invalid_time_content).show();
                    timeDisplay = MCCPilotLogConst.DEFAULT_MAX_HOUR;
                } else {
                    DecimalFormat fourZeroFormat = new DecimalFormat("0000");
                    int hour = 0;
                    try {
                        hour = Integer.parseInt(fourZeroFormat.format(time).substring(0, 2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean isAutoCorrected = false;
                    if (hour > 23) {
                        hour = 23;
                        isAutoCorrected = true;
                    }
                    int minute = 0;
                    try {
                        minute = Integer.parseInt(fourZeroFormat.format(time).substring(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (minute > 59) {
                        minute = 59;
                        isAutoCorrected = true;
                    }
                    if (isAutoCorrected) {
                        MccDialog.getOkAlertDialog(mContext, R.string.invalid_time_title, R.string.invalid_time_content).show();
                    }
                    String hourToDisplay = "";
                    hourToDisplay += hour;
                    if (!isCheckHour) {
                        if (hour < 10) {
                            hourToDisplay = "0" + hour;
                        }
                    }
                    if (minute < 10) {
                        timeDisplay = hourToDisplay + ":" + "0" + minute;
                    } else {
                        timeDisplay = hourToDisplay + ":" + minute;
                    }
                }

            }
            return timeDisplay;
        }
        return "";
    }

    public String replaceInvalidCharacter(String value) {
        if (!TextUtils.isEmpty(value)) {
            value = value.replaceAll("<", ":");
            value = value.replaceAll(">", ":");
        }
        return value;
    }

    public void displayDateDialog(String currentDateString, final ItemsFlightView itemsFlightViewDate, final Calendar flightDate) {
        Date currentDate = null;
        try {
            currentDate = DISPLAY_DATE_FORMAT.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar cal = dateToCalendar(currentDate);
        cal.set(Calendar.HOUR, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(mContext, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                //Calendar mFlightDate = Calendar.getInstance();
                flightDate.set(Calendar.MONTH, monthOfYear);
                flightDate.set(Calendar.YEAR, year);
                flightDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                itemsFlightViewDate.setDescription(DISPLAY_DATE_FORMAT.format(flightDate.getTime()));
                Calendar calToday = Calendar.getInstance();
                if (calToday.get(Calendar.YEAR) == flightDate.get(Calendar.YEAR) &&
                        calToday.get(Calendar.MONTH) == flightDate.get(Calendar.MONTH) &&
                        calToday.get(Calendar.DAY_OF_MONTH) == flightDate.get(Calendar.DAY_OF_MONTH)) {
                    itemsFlightViewDate.setFootNote(DAY_OF_WEEK_FORMAT.format(flightDate.getTime()), R.color.grey_footer_text);
                } else {
                    itemsFlightViewDate.setFootNote(DAY_OF_WEEK_FORMAT.format(flightDate.getTime()), RED_COLOR);
                }
            }
        }, year, month, day);
        datePicker.setCancelable(false);
        datePicker.show();
    }

    private Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void checkedItems(ItemsFlightView itemsFlightView, ItemsFlightView itemsTotal) {
        String currentValue = itemsFlightView.getDescription();
        if (!TextUtils.isEmpty(itemsTotal.getDescription()) && TextUtils.isEmpty(currentValue)) {
            itemsFlightView.setDescription(itemsTotal.getDescription());
            itemsFlightView.setMinutesData(itemsTotal.getMinutesData());
        } else {
            itemsFlightView.clearValue();
            itemsFlightView.setMinutesData(0);
        }

    }

    public void autoSwitchPicCusAndCoPilot(ItemsFlightView itemCoPilot,
                                           ItemsFlightView itemPicUs, ItemsFlightView itemTask,
                                           ItemsFlightView itemTotal, ItemsFlightView itemRelief, int percent, boolean isLogDecimal) {
        if (TextUtils.isEmpty(itemTotal.getDescription())) return;
        int minTotal = itemTotal.getMinutesData();
        if (conditionLoad == null || !conditionLoad.get(AircraftInfoConst.RELIEF_STRING)
                || itemRelief.getVisibility() == View.GONE || getSettingDataByCode(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).equals("0")) {
            percent = 100;
        }
        double minValue = /*Math.round*/(minTotal * (double) percent / 100);
        if (minValue < 0) minValue *= -1;
        String valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue), isLogDecimal);

        if (!TextUtils.isEmpty(itemTask.getDescription()) && itemTask.getDescription().equals("PF")) {
            itemPicUs.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
            itemPicUs.setMinutesData((int) minValue);
        } else {
            itemCoPilot.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
            itemCoPilot.setMinutesData((int) minValue);
        }
    }

    public void checkedItemTask(ItemsFlightView itemsTask, ItemsFlightView itemToday,
                                ItemsFlightView itemToNight, ItemsFlightView itemLdgDay,
                                ItemsFlightView itemLdgNight, RouteInfoCalculator routeInfoCalculator,
                                Aircraft aircraft, ItemsFlightView itemCoPilot, ItemsFlightView itemPicUs,
                                ItemsFlightView itemTotal, ItemsFlightView itemActualInst, int percentActualInst) {

        if (!TextUtils.isEmpty(itemsTask.getDescription()) && itemsTask.getDescription().equals("PF")) {
            itemToday.clearValue();
            itemToNight.clearValue();
            itemLdgDay.clearValue();
            itemLdgNight.clearValue();
            itemsTask.setDescription("PM");
            if (aircraft != null && aircraft.getDefaultLog() == 7) { //CoPilot when PM
                if (checkEnableField(itemPicUs) && itemPicUs.getMinutesData() > 0) {
                    itemCoPilot.setMinutesData(itemPicUs.getMinutesData());
                    itemCoPilot.setDescription(itemPicUs.getDescription());
                    itemPicUs.clearValue();
                }
            }
        } else {
            if (routeInfoCalculator != null) {
                if (checkEnableField(itemToday) && routeInfoCalculator.route.TODay != 0)
                    itemToday.setDescription(String.valueOf(routeInfoCalculator.route.TODay));
                if (checkEnableField(itemToNight) && routeInfoCalculator.route.TONight != 0)
                    itemToNight.setDescription(String.valueOf(routeInfoCalculator.route.TONight));
                if (checkEnableField(itemLdgDay) && routeInfoCalculator.route.LdgDay != 0)
                    itemLdgDay.setDescription(String.valueOf(routeInfoCalculator.route.LdgDay));
                if (checkEnableField(itemLdgNight) && routeInfoCalculator.route.LdgNight != 0)
                    itemLdgNight.setDescription(String.valueOf(routeInfoCalculator.route.LdgNight));
            }
            itemsTask.setDescription("PF");
            if (aircraft != null && aircraft.getDefaultLog() == 7) {// PicUs when PF
                if (checkEnableField(itemCoPilot) && itemCoPilot.getMinutesData() > 0) {
                    itemPicUs.setMinutesData(itemCoPilot.getMinutesData());
                    itemPicUs.setDescription(itemCoPilot.getDescription());
                    itemCoPilot.clearValue();
                }
            }
        }
        if (checkEnableField(itemActualInst)) {
            autoLoadActualInstrument(itemActualInst, itemTotal, itemsTask, percentActualInst, isLogTimeDecimal);
        }
    }

    public void autoLoadActualInstrument(final ItemsFlightView itemActual, final ItemsFlightView itemTotal,
                                         final ItemsFlightView itemTask, final int percent, final boolean isLogDecimal) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (itemActual.getVisibility() == View.GONE)
                            return;
                        if (conditionLoad == null || !conditionLoad.get(AircraftInfoConst.ACT_INSTR_STRING))
                            return;
                        if (TextUtils.isEmpty(itemTotal.getDescription()))
                            return;

                        if (percent > 0 || (percent < 0 && !TextUtils.isEmpty(itemTask.getDescription()) && itemTask.getDescription().equals("PF"))) {
                            int minTotal = itemTotal.getMinutesData();
                            double minActual = Math.round(minTotal * (double) percent / 100);
                            if (minActual < 0) minActual *= -1;
                            String valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minActual), isLogDecimal);
                            itemActual.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                            itemActual.setMinutesData((int) minActual);
                        } else {
                            itemActual.clearValue();
                        }
                    }
                });
            }
        });
    }


    public void autoLoadTask(final ItemsFlightView itemTask, final boolean isLogPicOrPicus, final Aircraft aircraft) {
        if (itemTask.getVisibility() == View.GONE) {
            return;
        }
        if (aircraft == null) {
            return;
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String configLoadTask = databaseManager.getSetting(490).getData();
                        switch (configLoadTask) {
                            case "0":
                                if (aircraft.getCategory() == 1)
                                    itemTask.setDescription("PF");
                                else
                                    itemTask.setDescription("PM");
                                break;
                            case "1":
                                if (aircraft.getCategory() == 1)
                                    itemTask.setDescription("PF");
                                else
                                    itemTask.setDescription(getAlternatePFPM());
                                break;
                            case "2":
                                if (isLogPicOrPicus)
                                    itemTask.setDescription("PF");
                                break;
                            case "3":
                                itemTask.setDescription("PF");
                                break;
                        }

                    }
                });
            }
        });
    }

    private String getAlternatePFPM() {
        Flight flight = databaseManager.getPreviousFlight();
        if (flight != null) {
            if (flight.getPF()) {
                return "PM";
            } else {
                return "PF";
            }
        } else {
            return "PF";
        }
    }

    public void autoLoadXC(final Airfield airfieldDep, final Airfield airfieldArr, final Aircraft aircraft, final ItemsFlightView itemXC, final ItemsFlightView itemsTotal) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (itemXC.getVisibility() == View.GONE)
                            return;
                        if (conditionLoad == null || !conditionLoad.get(AircraftInfoConst.XC_STRING))
                            return;
                        if (TextUtils.isEmpty(itemsTotal.getDescription()))
                            return;
                        if (airfieldDep == null || airfieldArr == null)
                            return;
                        if (airfieldDep.getAFCode() != airfieldArr.getAFCode()) {
                            if (aircraft != null && aircraft.getClassZ() == 4) {
                                if (orthodromicDist(airfieldDep, airfieldArr) > 25) {
                                    itemXC.setDescription(itemsTotal.getDescription());
                                    itemXC.setMinutesData(itemsTotal.getMinutesData());
                                } else
                                    itemXC.clearValue();
                            } else {
                                if (orthodromicDist(airfieldDep, airfieldArr) > 50) {
                                    itemXC.setDescription(itemsTotal.getDescription());
                                    itemXC.setMinutesData(itemsTotal.getMinutesData());
                                } else
                                    itemXC.clearValue();
                            }
                        }
                    }
                });
            }
        });

    }


    private int orthodromicDist(Airfield airfieldFrom, Airfield airfieldTo) {
        Navigation nav = new Navigation();
        nav.latitudeA = airfieldFrom.getLatitude();
        nav.longitudeA = airfieldFrom.getLongitude();

        nav.latitudeB = airfieldTo.getLatitude();
        nav.longitudeB = airfieldTo.getLongitude();

        nav.calculate();
        return nav.orthoDist;
    }


    public void autoLoadRelief(final ItemsFlightView itemRelief, final ItemsFlightView itemTotal, final int percent, final boolean isLogDecimal) {
       /* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
        if (itemRelief.getVisibility() == View.GONE)
            return;
        if (conditionLoad == null || !conditionLoad.get(AircraftInfoConst.RELIEF_STRING))
            return;
        if (TextUtils.isEmpty(itemTotal.getDescription()))
            return;

        if (percent > 0) {
            int minTotal = itemTotal.getMinutesData();
            double minRelief = /*Math.round*/(minTotal * (double) percent / 100);
            if (minRelief < 0) minRelief *= -1;
            String valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minRelief), isLogDecimal);
            itemRelief.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
            itemRelief.setMinutesData((int) minRelief);
        }
                    /*}
                });
            }
        });*/
    }

    public String convertTimeToLocalOrUTC(String time, TimeMode timeMode, TimeZone timeZone, int timeZoneCode, Calendar pFlightDate) {
        Calendar flightDate = Calendar.getInstance();
        flightDate.setTime(pFlightDate.getTime()); //do not change pFlightDate;
        if (!time.contains(":")) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        if (timeZone == null) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        try {
            //Calendar pFlightDate = Calendar.getInstance();
            String[] times = time.split(":");
            flightDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            flightDate.set(Calendar.MINUTE, Integer.parseInt(times[1]));

            //java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
            Calendar c = Calendar.getInstance(timeZone);
            long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, timeZoneCode, flightDate.getTime());
            switch (timeMode) {
                case UTC:
                    flightDate.add(Calendar.MINUTE, (int) offsetMinute);
                    return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) + " local";
                case BASE:
                    int offsetHomeBase = getBaseOffset(flightDate);
                    if (offsetHomeBase != TIME_ZONE_OFFSET_ERR) {
                        int offsetHomeBaseAndLocal = offsetHomeBase - (int) offsetMinute;
                        flightDate.add(Calendar.MINUTE, -offsetHomeBaseAndLocal);
                        return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) + " local";
                    }
                case LOCAL:
                    flightDate.add(Calendar.MINUTE, -(int) offsetMinute);
                    return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) + " UTC";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MCCPilotLogConst.STRING_EMPTY;
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public String convertTimeToUTC(String time, TimeMode inputTimeMode, TimeZone timeZone, int timeZoneCode, Calendar pFlightDate) {
        Calendar flightDate = Calendar.getInstance();
        flightDate.setTime(pFlightDate.getTime());
        if (inputTimeMode == TimeMode.UTC) return time;
        if (!time.contains(":")) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        try {
            //Calendar pFlightDate = Calendar.getInstance();
            String[] times = time.split(":");
            flightDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            flightDate.set(Calendar.MINUTE, Integer.parseInt(times[1]));
            switch (inputTimeMode) {
                case BASE:
                    int offsetHomeBase = getBaseOffset(flightDate);
                    if (offsetHomeBase != TIME_ZONE_OFFSET_ERR) {
                        flightDate.add(Calendar.MINUTE, -offsetHomeBase);
                        return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime());
                    }
                case LOCAL:
                    if (timeZone == null) {
                        return MCCPilotLogConst.STRING_EMPTY;
                    }
                    Calendar c = Calendar.getInstance(timeZone);
                    long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, timeZoneCode, flightDate.getTime());
                    flightDate.add(Calendar.MINUTE, -(int) offsetMinute);
                    return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MCCPilotLogConst.STRING_EMPTY;
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public long getDepArrOffset(TimeZone timeZone, int timeZoneCode, Calendar pFlightDate) {
        Calendar c = Calendar.getInstance(timeZone);
        long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, timeZoneCode, pFlightDate.getTime());
        return offsetMinute;
    }

    public int getBaseOffset(Calendar pFlightDate) {
        SettingConfig settingHomeBase = databaseManager.getSetting(13);
        if (settingHomeBase != null) {
            Airfield airfield = databaseManager.getAirfieldByICAOIATA(settingHomeBase.getData());
            if (airfield != null) {
                ZTimeZone zTimeZone = databaseManager.getTimeZoneByCode(airfield.getTZCode());
                if (zTimeZone != null) {
                    java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
                    Calendar c = Calendar.getInstance(timeZone);
                    long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, airfield.getTZCode(), pFlightDate.getTime());
                    return (int) offsetMinute;
                }
            }
        }
        return TIME_ZONE_OFFSET_ERR;
    }

    int TIME_ZONE_OFFSET_ERR = 0;

    public enum TimeMode {
        UTC,
        LOCAL,
        BASE,
    }

    public void calcAndDisplayTimeMode(final ItemsFlightView itemsFlightView, final TimeMode timeMode, final TimeZone zTimeZone, final int timeZoneCode, final Calendar flightDate) {
        if (zTimeZone == null) return;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsFlightView.setFootNote(convertTimeToLocalOrUTC(itemsFlightView.getDescription(), timeMode, zTimeZone, timeZoneCode, flightDate));
                    }
                });
            }
        });
    }

    public void calcTotalTime(final ItemsFlightView itemOffBlock, final ItemsFlightView itemOnBlock,
                              final ItemsFlightView itemTotal, final boolean isLogTimeDecimal,
                              final TimeZone timeZoneOut, final TimeZone timeZoneIn,
                              final int timeZoneCodeOut, final int timeZoneCodeIn, final Calendar flightDate) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(itemOffBlock.getDescription()) || TextUtils.isEmpty(itemOnBlock.getDescription())) {
                            return;
                        }
                        String outHours = convertTimeToUTC(itemOffBlock.getDescription(), timeMode, timeZoneOut, timeZoneCodeOut, flightDate);
                        String InHours = convertTimeToUTC(itemOnBlock.getDescription(), timeMode, timeZoneIn, timeZoneCodeIn, flightDate);

                        int minTotal = TimeUtils.calcTotalMinute(outHours, InHours, accuracy);
                        if (minTotal == -1) {
                            return;
                        }
                        itemTotal.setMinutesData(minTotal);
                        itemTotal.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(minTotal), isLogTimeDecimal), true);
                    }
                });
            }
        });
    }

    public void calcTotalTime(final ItemsFlightView hobbsOut, final ItemsFlightView hobbsIn, final ItemsFlightView itemTotal){
        if (TextUtils.isEmpty(hobbsOut.getDescription()) || TextUtils.isEmpty(hobbsIn.getDescription())) {
            return;
        }
        double hobbsOffset = Utils.parseDouble(hobbsIn.getDescription()) - Utils.parseDouble(hobbsOut.getDescription());
        if(hobbsOffset<0)return;
        int minTotal = TimeUtils.convertHourToMin(String.valueOf(hobbsOffset));
        itemTotal.setMinutesData(minTotal);
        itemTotal.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(minTotal), isLogTimeDecimal), true);

    }

    public void autoLoadFunctionTime(Aircraft aircraft, ItemsFlightView itemPic, ItemsFlightView itemCoPilot, ItemsFlightView itemDual,
                                     ItemsFlightView itemInstructor, ItemsFlightView itemPicUs, ItemsFlightView itemExam, ItemsFlightView itemTask,
                                     ItemsFlightView itemTotal, ItemsFlightView itemRelief, int minRelief, boolean isLogDecimal) {
        if (aircraft == null || aircraft.getDefaultLog() == null)
            return;
        if (TextUtils.isEmpty(itemTotal.getDescription())) return;
        int minTotal = itemTotal.getMinutesData();
        if (conditionLoad == null || !conditionLoad.get(AircraftInfoConst.RELIEF_STRING)
                || itemRelief.getVisibility() == View.GONE || getSettingDataByCode(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).equals("0")) {
            //percent = 100;
            minRelief = 0;
        }
        //double minValue = /*Math.round*/(minTotal * (double) percent / 100);
        int minValue = minTotal - minRelief;
        if (minValue < 0) minValue *= -1;
        String valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue), isLogDecimal);
        if (isLogDecimal && !TextUtils.isEmpty(itemRelief.getDescription()) && !TextUtils.isEmpty(valueDisplay)) {
            int minValuePercentSixty = Integer.valueOf(valueDisplay.replace(".", "").replace(",", ""));
            int minReliefPercentSixty = Integer.valueOf(itemRelief.getDescription().replace(".", "").replace(",", ""));
            int minTotalPercentSixty = Integer.valueOf(itemTotal.getDescription().replace(".", "").replace(",", ""));
            Log.d("value", String.valueOf(minValuePercentSixty + minReliefPercentSixty - minTotalPercentSixty));
            if (minValuePercentSixty + minReliefPercentSixty - minTotalPercentSixty > 0 && isLogDecimal) {
                valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue - 6), isLogDecimal);
            } else if (minValuePercentSixty + minReliefPercentSixty - minTotalPercentSixty < 0 && isLogDecimal) {
                valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue + 6), isLogDecimal);
            } else {
                valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue), isLogDecimal);
            }
        }
        switch (aircraft.getDefaultLog()) {
            case 1:
                if (checkEnableField(itemPic)) {
                    itemPic.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemPic.setMinutesData((int) minValue);
                }
                break;
            case 2:
                if (checkEnableField(itemCoPilot)) {
                    itemCoPilot.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemCoPilot.setMinutesData((int) minValue);
                }
                break;
            case 3:
                if (checkEnableField(itemDual)) {
                    itemDual.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemDual.setMinutesData((int) minValue);
                }
                break;
            case 4:
                if (checkEnableField(itemInstructor)) {
                    valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minTotal), isLogDecimal);
                    itemInstructor.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemInstructor.setMinutesData(minTotal);
                    //also auto load time pic
                    if (checkEnableField(itemPic) && aircraft.getDeviceCode() == 1) {
                        valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minValue), isLogDecimal);
                        itemPic.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                        itemPic.setMinutesData((int) minValue);
                    }
                }
                break;
            case 5:
                if (checkEnableField(itemPicUs)) {
                    itemPicUs.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemPicUs.setMinutesData((int) minValue);
                }
                break;
            case 6:
                if (checkEnableField(itemExam)) {
                    valueDisplay = TimeUtils.convertMinuteToHour(String.valueOf(minTotal), isLogDecimal);
                    itemExam.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemExam.setMinutesData(minTotal);
                }
                break;
            case 7:
                if (checkEnableField(itemPicUs) && checkEnableField(itemCoPilot)) {
                    if (!TextUtils.isEmpty(itemTask.getDescription()) && itemTask.getDescription().equals("PF")) {
                        itemPicUs.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                        itemPicUs.setMinutesData((int) minValue);
                    } else {
                        itemCoPilot.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                        itemCoPilot.setMinutesData((int) minValue);
                    }
                } else if (checkEnableField(itemPicUs)) {
                    itemPicUs.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemPicUs.setMinutesData((int) minValue);
                } else if (checkEnableField(itemCoPilot)) {
                    itemCoPilot.setDescription(getTimeDisplay(valueDisplay, isLogDecimal, true, isLogDecimal));
                    itemCoPilot.setMinutesData((int) minValue);
                }
                break;
        }
    }

    public void autoLoadConditionTime(final ItemsFlightView itemU1, final ItemsFlightView itemU2, final ItemsFlightView itemU3,
                                      final ItemsFlightView itemU4, final ItemsFlightView itemIFR, final ItemsFlightView itemSimInstrument, final ItemsFlightView itemTotal) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (conditionLoad == null) return;
                        String totalValue = itemTotal.getDescription();
                        if (TextUtils.isEmpty(totalValue)) return;
                        if (conditionLoad.get(AircraftInfoConst.U1) && itemU1.getVisibility() == View.VISIBLE) {
                            itemU1.setDescription(totalValue);
                            itemU1.setMinutesData(itemTotal.getMinutesData());
                        }
                        if (conditionLoad.get(AircraftInfoConst.U2) && itemU2.getVisibility() == View.VISIBLE) {
                            itemU2.setDescription(totalValue);
                            itemU2.setMinutesData(itemTotal.getMinutesData());
                        }
                        if (conditionLoad.get(AircraftInfoConst.U3) && itemU3.getVisibility() == View.VISIBLE) {
                            itemU3.setDescription(totalValue);
                            itemU3.setMinutesData(itemTotal.getMinutesData());
                        }
                        if (conditionLoad.get(AircraftInfoConst.U4) && itemU4.getVisibility() == View.VISIBLE && !itemU4.getTextTitle().trim().equalsIgnoreCase("fh-night")) {
                            Log.d("title min u4", itemU4.getTextTitle());
                            itemU4.setDescription(totalValue);
                            itemU4.setMinutesData(itemTotal.getMinutesData());
                        } else {
                            Log.d("title min u4", itemU4.getTextTitle());
                        }
                        if (conditionLoad.get(AircraftInfoConst.IFR_STRING) && itemIFR.getVisibility() == View.VISIBLE) {
                            itemIFR.setDescription(totalValue);
                            itemIFR.setMinutesData(itemTotal.getMinutesData());
                        }
                        if (conditionLoad.get(AircraftInfoConst.SIM_INSTR_STRING) && itemSimInstrument.getVisibility() == View.VISIBLE) {
                            itemSimInstrument.setDescription(totalValue);
                            itemSimInstrument.setMinutesData(itemTotal.getMinutesData());
                        }
                    }
                });
            }
        });

    }

    public void loadAircraftCondition(Aircraft aircraft) {
        HashMap<String, Boolean> hashMap = new HashMap<>();
        hashMap.put(AircraftInfoConst.U4, false);
        hashMap.put(AircraftInfoConst.U3, false);
        hashMap.put(AircraftInfoConst.U2, false);
        hashMap.put(AircraftInfoConst.U1, false);
        hashMap.put(AircraftInfoConst.SIM_INSTR_STRING, false);
        hashMap.put(AircraftInfoConst.IFR_STRING, false);
        hashMap.put(AircraftInfoConst.ACT_INSTR_STRING, false);
        hashMap.put(AircraftInfoConst.RELIEF_STRING, false);
        hashMap.put(AircraftInfoConst.XC_STRING, false);
        int condLogNum, rest;
        condLogNum = aircraft.getCondLog();

        while (condLogNum > 0) {
            rest = 0;
            if (condLogNum > AircraftInfoConst.MIN_U4) {
                rest = condLogNum - AircraftInfoConst.MIN_U4;
                condLogNum = AircraftInfoConst.MIN_U4;
            } else if (condLogNum > AircraftInfoConst.MIN_U3 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U3;
                condLogNum = AircraftInfoConst.MIN_U3;
            } else if (condLogNum > AircraftInfoConst.MIN_U2 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U2;
                condLogNum = AircraftInfoConst.MIN_U2;
            } else if (condLogNum > AircraftInfoConst.MIN_U1 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U1;
                condLogNum = AircraftInfoConst.MIN_U1;
            } else if (condLogNum > AircraftInfoConst.SIM_INSTR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.SIM_INSTR;
                condLogNum = AircraftInfoConst.SIM_INSTR;
            } else if (condLogNum > AircraftInfoConst.IFR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.IFR;
                condLogNum = AircraftInfoConst.IFR;
            } else if (condLogNum > AircraftInfoConst.ACT_INSTR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.ACT_INSTR;
                condLogNum = AircraftInfoConst.ACT_INSTR;
            } else if (condLogNum > AircraftInfoConst.RELIEF && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.RELIEF;
                condLogNum = AircraftInfoConst.RELIEF;
            } else if (condLogNum > AircraftInfoConst.XC && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.XC;
                condLogNum = AircraftInfoConst.XC;
            }

            switch (condLogNum) {
                case AircraftInfoConst.MIN_U4:
                    hashMap.put(AircraftInfoConst.U4, true);
                    break;
                case AircraftInfoConst.MIN_U3:
                    hashMap.put(AircraftInfoConst.U3, true);
                    break;
                case AircraftInfoConst.MIN_U2:
                    hashMap.put(AircraftInfoConst.U2, true);
                    break;
                case AircraftInfoConst.MIN_U1:
                    hashMap.put(AircraftInfoConst.U1, true);
                    break;
                case AircraftInfoConst.SIM_INSTR:
                    hashMap.put(AircraftInfoConst.SIM_INSTR_STRING, true);
                    break;
                case AircraftInfoConst.IFR:
                    hashMap.put(AircraftInfoConst.IFR_STRING, true);
                    break;
                case AircraftInfoConst.ACT_INSTR:
                    //condLogAdded = AircraftInfoConst.ACT_INSTR_STRING;
                    hashMap.put(AircraftInfoConst.ACT_INSTR_STRING, true);
                    break;
                case AircraftInfoConst.RELIEF:
                    hashMap.put(AircraftInfoConst.RELIEF_STRING, true);
                    break;
                case AircraftInfoConst.XC:
                    hashMap.put(AircraftInfoConst.XC_STRING, true);
                    break;
                default:
                    break;
            }
            condLogNum = rest;
        }
        conditionLoad = hashMap;
    }

    private boolean equalToCondLog(int testNumber) {
        boolean result = false;
        switch (testNumber) {
            case AircraftInfoConst.XC:
                result = true;
                break;
            case AircraftInfoConst.RELIEF:
                result = true;
                break;
            case AircraftInfoConst.ACT_INSTR:
                result = true;
                break;
            case AircraftInfoConst.IFR:
                result = true;
                break;
            case AircraftInfoConst.SIM_INSTR:
                result = true;
                break;
            case AircraftInfoConst.MIN_U1:
                result = true;
                break;
            case AircraftInfoConst.MIN_U2:
                result = true;
                break;
            case AircraftInfoConst.MIN_U3:
                result = true;
                break;
            case AircraftInfoConst.MIN_U4:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    public String autoLoadDefaultOperation(Aircraft aircraft, final ItemsFlightView itemOps) {
        if (aircraft == null) {
            return "";
        }
        if (aircraft.getDefaultOps() != null && aircraft.getDefaultOps() > 0 && itemOps.getVisibility() == View.VISIBLE) {
            final ZOperation zOperation = databaseManager.getZOperation(aircraft.getDefaultOps());
            if (zOperation != null) {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemOps.setDescription(zOperation.getOpsShort());
                    }
                });
                return String.valueOf(aircraft.getDefaultOps());
            }
        }
        return "";
    }

    public Pilot autoLoadPilot(Aircraft aircraft, final ItemsFlightView itemsFlightView) {
        if (aircraft.getDefaultLog() != null && aircraft.getDefaultLog() == 1 && itemsFlightView.getVisibility() == View.VISIBLE) {
            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemsFlightView.setDescription("SELF");
                    itemsFlightView.setFootNote("*");
                }
            });
        }
        if (pilotSelf == null)
            pilotSelf = databaseManager.getPilotByCode(Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_SELF));
        return pilotSelf;
    }

    public String autoLoadDefaultLaunch(Aircraft aircraft, final ItemsFlightView itemLaunch) {
        if (aircraft == null) {
            return "";
        }
        if (aircraft.getDefaultLaunch() != null && aircraft.getDefaultLaunch() > 0 && itemLaunch.getVisibility() == View.VISIBLE) {
            final ZLaunch zLaunch = databaseManager.getGliderLaunch(aircraft.getDefaultLaunch());
            if (zLaunch != null) {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemLaunch.setDescription(zLaunch.getLaunchShort());
                    }
                });
                return String.valueOf(aircraft.getDefaultLaunch());
            }
        }
        return "";
    }

    public String autoLoadDefaultApproach(Aircraft aircraft, final ItemsFlightView itemApproach) {
        if (aircraft == null) {
            return "";
        }
        if (aircraft.getDefaultApp() != null && aircraft.getDefaultApp() > 0 && itemApproach.getVisibility() == View.VISIBLE) {
            final ZApproach zApproach = databaseManager.getZApproach(aircraft.getDefaultApp());
            if (zApproach != null) {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemApproach.setDescription(zApproach.getAPShort());
                    }
                });
                return String.valueOf(aircraft.getDefaultApp());
            }
        }
        return "";
    }


    public Pilot onClickButtonTopPilot(final ItemsFlightView itemsFlightView, final ItemsFlightView itemsFlightViewCrew1,
                                       final ItemsFlightView itemsFlightViewCrew2, final ItemsFlightView itemsFlightViewCrew3,
                                       final ItemsFlightView itemsFlightViewCrew4) {
        /*((BaseActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        if (pilotSelf == null)
            pilotSelf = databaseManager.getPilotByCode(Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_SELF));
        if (TextUtils.isEmpty(itemsFlightView.getDescription())) {
            if (itemsFlightViewCrew1.getDescription().equals(pilotSelf.getPilotName())) {
                itemsFlightViewCrew1.clearValue();
            }
            if (itemsFlightViewCrew2.getDescription().equals(pilotSelf.getPilotName())) {
                itemsFlightViewCrew2.clearValue();
            }
            if (itemsFlightViewCrew3.getDescription().equals(pilotSelf.getPilotName())) {
                itemsFlightViewCrew3.clearValue();
            }
            if (itemsFlightViewCrew4.getDescription().equals(pilotSelf.getPilotName())) {
                itemsFlightViewCrew4.clearValue();
            }
            itemsFlightView.setDescription(pilotSelf.getPilotName());
            itemsFlightView.setFootNote("*");
            return pilotSelf;
        } else {
            itemsFlightView.clearValue();
            return null;
        }

    }

    private boolean checkEnableField(ItemsFlightView itemsFlightView) {
        return itemsFlightView.getVisibility() == View.VISIBLE;
    }


    /*Duty*/
    public void calcDuration(final ItemsFlightView itemOffBlock, final ItemsFlightView itemOnBlock,
                              final ItemsFlightView itemTotal, final boolean isLogTimeDecimal,
                              final TimeZone timeZoneOut, final TimeZone timeZoneIn,
                              final int timeZoneCodeOut, final int timeZoneCodeIn, final Calendar flightDate) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(itemOffBlock.getDescription()) || TextUtils.isEmpty(itemOnBlock.getDescription())) {
                            return;
                        }
                        String outHours = convertTimeToUTC(itemOffBlock.getDescription(), timeMode, timeZoneOut, timeZoneCodeOut, flightDate);
                        String InHours = convertTimeToUTC(itemOnBlock.getDescription(), timeMode, timeZoneIn, timeZoneCodeIn, flightDate);

                        int minTotal = TimeUtils.calcTotalMinute(outHours, InHours,accuracy);
                        if (minTotal == -1) {
                            return;
                        }
                        itemTotal.setMinutesData(minTotal);
                        itemTotal.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(minTotal), isLogTimeDecimal));
                        itemTotal.getEdtDescription().setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                    }
                });
            }
        });
    }

    public void calcAndDisplayTimeModeForDuty(final ItemsFlightView itemsFlightView, final TimeMode timeMode, final TimeZone zTimeZone, final int timeZoneCode, final Calendar flightDate) {
        if (zTimeZone == null) return;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemsFlightView.setFootNote(convertTimeToLocalOrUTCForDuty(itemsFlightView.getDescription(), timeMode, zTimeZone, timeZoneCode, flightDate));
                    }
                });
            }
        });
    }

    public String convertTimeToLocalOrUTCForDuty(String time, TimeMode timeMode, TimeZone timeZone, int timeZoneCode, Calendar pFlightDate) {
        Calendar flightDate = Calendar.getInstance();
        flightDate.setTime(pFlightDate.getTime()); //do not change pFlightDate;
        if (!time.contains(":")) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        if (timeZone == null) {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        try {
            //Calendar pFlightDate = Calendar.getInstance();
            String[] times = time.split(":");
            flightDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            flightDate.set(Calendar.MINUTE, Integer.parseInt(times[1]));

            //java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
            Calendar c = Calendar.getInstance(timeZone);
            long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, timeZoneCode, flightDate.getTime());
            switch (timeMode) {
                case UTC:
                    flightDate.add(Calendar.MINUTE, (int) offsetMinute);
                    return /*new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) +*/ " UTC";
                case BASE:
                    int offsetHomeBase = getBaseOffset(flightDate);
                    if (offsetHomeBase != TIME_ZONE_OFFSET_ERR) {
                        //int offsetHomeBaseAndLocal = offsetHomeBase - (int) offsetMinute;
                        flightDate.add(Calendar.MINUTE, -(int)offsetMinute);
                        return /*new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) +*/ " BASE";
                    }
                case LOCAL:
                    flightDate.add(Calendar.MINUTE, -(int) offsetMinute);
                    return new SimpleDateFormat("HH:mm", Locale.US).format(flightDate.getTime()) + " UTC";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MCCPilotLogConst.STRING_EMPTY;
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }
}
