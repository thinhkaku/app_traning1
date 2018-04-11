package aero.pilotlog.utilities;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import aero.pilotlog.common.StateKey;
import aero.pilotlog.models.AccountCalendarModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by tuan.na on 7/10/2015.
 * Calendar Access
 */
public class CalendarAccess {

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL                  // 3
    };
    public static final String[] CALENDAR_ID_PROJECTION = new String[]{
            CalendarContract.Calendars._ID                                  // 0
    };

    // The indices for the projection array above.
    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    public static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    public static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private Context mContext;
    private ContentResolver mContentResolver;
    private int mCalendarId = -1;

    public CalendarAccess(Context context) {
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Cursor getCursorCalendars() {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        return mContentResolver.query(uri, EVENT_PROJECTION, null, null, null);
    }

    /**
     * Delete Events from Google Calendar (OS Calendar)
     *
     * @param eventIds event IDs to delete
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void deleteEventsFromGoogleCalendar(Context context, ArrayList<Long> eventIds) {
        ContentResolver cr = context.getContentResolver();
        for (long eventId : eventIds) {
            if (eventId != 0) {
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                if (uri == null) {
                    continue;
                }
                cr.delete(uri, null, null);
            }
        }
    }

    public static ArrayList<Long> getEventFromfGoogleCalendarByOrganizer(Context context, String organizer) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        ArrayList<Long> dutiesIdList = new ArrayList<>();
        if (cr != null) {
            cursor = cr.query(Uri.parse("content://com.android.calendar/events"), CALENDAR_ID_PROJECTION, "organizer" + " = '" + organizer + "'", null, null);
        }
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Long eventId = cursor.getLong(0);
                dutiesIdList.add(eventId);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return dutiesIdList;
    }


    /**
     * get id of all events that have #mccPILOTLOG in the end of description from startTime to endTime
     *
     * @return list id
     */
    public ArrayList<Long> getEventCalendar(long startTime, long endTime, int id) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ArrayList<Long> listEventMcc = new ArrayList<>();
        String select;
        select = CalendarContract.Events.CALENDAR_ID + " = " + id + " and " + CalendarContract.Events.DESCRIPTION + " like '%#mccPILOTLOG' and " +
                CalendarContract.Events.DTSTART + ">=" + startTime + " and " + CalendarContract.Events.DTSTART + " <= " + endTime;
        Cursor cursor = mContentResolver.query(uri, new String[]{CalendarContract.Events._ID}, select, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEventMcc.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listEventMcc;
    }

    /**
     * get id of all events that have #mccPILOTLOG in the end of description
     *
     * @return list id
     */
    public ArrayList<Long> getAllEventId(int id) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ArrayList<Long> listEventMcc = new ArrayList<>();
        String select;
        select = CalendarContract.Events.CALENDAR_ID + " = " + id + " and " + CalendarContract.Events.DESCRIPTION + " like '%#mccPILOTLOG'";
        Cursor cursor = mContentResolver.query(uri, new String[]{CalendarContract.Events._ID}, select, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEventMcc.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listEventMcc;
    }

    /**
     * get id of all events that have #mccPILOTLOG in the end of description
     *
     * @param startTime start time, timestamp
     * @return list id
     */
    public ArrayList<Long> getEventCalendarFrom(int id, long startTime) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ArrayList<Long> listEventMcc = new ArrayList<>();
        String select;
        select = CalendarContract.Events.CALENDAR_ID + " = " + id + " and " + CalendarContract.Events.DESCRIPTION + " like '%#mccPILOTLOG' and " +
                CalendarContract.Events.DTSTART + ">=" + startTime;
        Cursor cursor = mContentResolver.query(uri, new String[]{CalendarContract.Events._ID}, select, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEventMcc.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listEventMcc;
    }

    /**
     * get id of all events that have #mccPILOTLOG in the end of description from startTime to endTime
     *
     * @return list id
     */
    public ArrayList<Long> getPastEventCalendar(int id, long endTime) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ArrayList<Long> listEventMcc = new ArrayList<>();
        String select;
        select = CalendarContract.Events.CALENDAR_ID + " = " + id + " and " + CalendarContract.Events.DESCRIPTION + " like '%#mccPILOTLOG' and " +
                CalendarContract.Events.DTSTART + " < " + endTime;
        Cursor cursor = mContentResolver.query(uri, new String[]{CalendarContract.Events._ID}, select, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEventMcc.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listEventMcc;
    }

    /**
     * get id of all events that have #mccPILOTLOG in the end of description from startTime to endTime
     *
     * @return list id
     */
    public ArrayList<Long> getTodayEventCalendar(int id) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ArrayList<Long> listEventMcc = new ArrayList<>();
        Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startCal.set(Calendar.HOUR, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        long startDay = startCal.getTimeInMillis();
        startCal.add(Calendar.DATE, 1);
        long endDay = startCal.getTimeInMillis();
        String select;
        select = CalendarContract.Events.CALENDAR_ID + " = " + id + " and " + CalendarContract.Events.DESCRIPTION + " like '%#mccPILOTLOG' and " +
                CalendarContract.Events.DTSTART + ">=" + startDay + " and " + CalendarContract.Events.DTSTART + " < " + endDay;
        Cursor cursor = mContentResolver.query(uri, new String[]{CalendarContract.Events._ID}, select, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listEventMcc.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listEventMcc;
    }


    public void makeNewCalendarEntry(AccountCalendarModel pCalendar) {
        if (mCalendarId == -1) {
            mCalendarId = getCalendarId();
        }
        if (mCalendarId == -1) {
            return;
        }
        ContentValues event = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try {
            calendar.setTime(sdf.parse(pCalendar.getStartdate()));
            calendar.add(Calendar.MINUTE, Integer.parseInt(pCalendar.getStarttime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long startTime = calendar.getTimeInMillis();
        long endTime, dur;
        dur = Integer.parseInt(pCalendar.getDuration()) * 60 * 1000;
        if (dur > 10 * 60 * 1000)
            endTime = startTime + dur;
        else
            endTime = startTime + 600 * 60 * 1000;
        int hasAlarm = 0;
        if (pCalendar.getRemindersound().equalsIgnoreCase("True")) {
            hasAlarm = 1;
        }
        final String where = "title=" + "'" + pCalendar.getSubject() + "' " + String.format("and dtstart= '%d'", startTime);
        Uri insertedUri;
        int c = 0;
        boolean bInserted = false;

        try {
            event.put(android.provider.CalendarContract.Events.DTSTART, startTime);
            event.put(android.provider.CalendarContract.Events.DTEND, endTime);
            event.put(android.provider.CalendarContract.Events.TITLE, pCalendar.getSubject());
            event.put(android.provider.CalendarContract.Events.EVENT_COLOR, 0x00457d);
            //event.put(android.provider.CalendarContract.Events.EVENT_COLOR, mContext.getResources().getColor(R.color.mcc_red));
            //event.notifyAll();
            event.put(android.provider.CalendarContract.Events.DESCRIPTION, pCalendar.getBody());
            event.put(android.provider.CalendarContract.Events.HAS_ALARM, hasAlarm);
            event.put(android.provider.CalendarContract.Events.CALENDAR_ID, mCalendarId);
            event.put(android.provider.CalendarContract.Events.EVENT_TIMEZONE, "UTC");

            //PL-502
            long startTimeDeleteEvent;
            Calendar startCal = Calendar.getInstance(TimeZone.getDefault());
            startCal.set(Calendar.HOUR, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.MILLISECOND, 0);
            startTimeDeleteEvent = startCal.getTimeInMillis();

            if(startTime>=startTimeDeleteEvent){
                insertedUri = mContentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, event);
                if (insertedUri != null) {
                    long eventID = Long.parseLong(insertedUri.getLastPathSegment());
                    if (!pCalendar.getReminder().equalsIgnoreCase("0")) {
                        ContentValues values = new ContentValues();
                        values.put(android.provider.CalendarContract.Reminders.MINUTES, pCalendar.getReminder());
                        values.put(android.provider.CalendarContract.Reminders.EVENT_ID, eventID);
                        values.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                        mContentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, values);
                    }
                }
            }else {
                try {
                    // update or
                    c = mContentResolver.update(android.provider.CalendarContract.Events.CONTENT_URI, event, where, null);
                } catch (Exception e) {//add new event
                    insertedUri = mContentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, event);
                    if (insertedUri != null) {
                        long eventID = Long.parseLong(insertedUri.getLastPathSegment());
                        if (!pCalendar.getReminder().equalsIgnoreCase("0")) {
                            ContentValues values = new ContentValues();
                            values.put(android.provider.CalendarContract.Reminders.MINUTES, pCalendar.getReminder());
                            values.put(android.provider.CalendarContract.Reminders.EVENT_ID, eventID);
                            values.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                            mContentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, values);
                        }
                        bInserted = true;
                    }
                }
                if (c == 0 && !bInserted) {
                    insertedUri = mContentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, event);
                    if (insertedUri != null) {
                        long eventID = Long.parseLong(insertedUri.getLastPathSegment());
                        if (!pCalendar.getReminder().equalsIgnoreCase("0")) {
                            ContentValues values = new ContentValues();
                            values.put(android.provider.CalendarContract.Reminders.MINUTES, pCalendar.getReminder());
                            values.put(android.provider.CalendarContract.Reminders.EVENT_ID, eventID);
                            values.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                            mContentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, values);
                        }
                        Log.d("CALENDAR","insert 2");
                    }
                }
            }

            //End PL-502


            /*try {
                // update or
                c = mContentResolver.update(android.provider.CalendarContract.Events.CONTENT_URI, event, where, null);
                Log.d("CALENDAR","Update");
            } catch (Exception e) {//add new event
                insertedUri = mContentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, event);
                if (insertedUri != null) {
                    long eventID = Long.parseLong(insertedUri.getLastPathSegment());
                    if (!pCalendar.getReminder().equalsIgnoreCase("0")) {
                        ContentValues values = new ContentValues();
                        values.put(android.provider.CalendarContract.Reminders.MINUTES, pCalendar.getReminder());
                        values.put(android.provider.CalendarContract.Reminders.EVENT_ID, eventID);
                        values.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                        mContentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, values);
                    }
                    bInserted = true;
                    Log.d("CALENDAR","insert 1");
                }
            }
            if (c == 0 && !bInserted) {
                insertedUri = mContentResolver.insert(android.provider.CalendarContract.Events.CONTENT_URI, event);
                if (insertedUri != null) {
                    long eventID = Long.parseLong(insertedUri.getLastPathSegment());
                    if (!pCalendar.getReminder().equalsIgnoreCase("0")) {
                        ContentValues values = new ContentValues();
                        values.put(android.provider.CalendarContract.Reminders.MINUTES, pCalendar.getReminder());
                        values.put(android.provider.CalendarContract.Reminders.EVENT_ID, eventID);
                        values.put(android.provider.CalendarContract.Reminders.METHOD, android.provider.CalendarContract.Reminders.METHOD_ALERT);
                        mContentResolver.insert(android.provider.CalendarContract.Reminders.CONTENT_URI, values);
                    }
                    Log.d("CALENDAR","insert 2");
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTimeInMillis(String date, String time) {
        try {
            if (date.length() != 8)
                return 0;
            final int year = Integer.parseInt(date.substring(0, 4));
            final int month = Integer.parseInt(date.substring(4, 6));
            final int day = Integer.parseInt(date.substring(6));
            Calendar cal = new GregorianCalendar(year, month - 1, day, 0, 0, 0);
            final int min = Integer.parseInt(time);
            cal.add(Calendar.MINUTE, min);
            int offset;//UTC and current time zone offset in millis
            //offset = cal.getTimeZone().getRawOffset() + cal.getTimeZone().getDSTSavings();
            long mlTime = cal.getTimeInMillis();
            offset = cal.getTimeZone().getOffset(mlTime);

            return mlTime + offset;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCalendarId() {
        int result = -1;

        String id = StorageUtils.getStringFromSharedPref(mContext, StateKey.DEFAULT_CALENDAR_ID, "-1");
        try {
            result = Integer.parseInt(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == -1) {
            Cursor managedCursor;
            Uri uri = android.provider.CalendarContract.Calendars.CONTENT_URI;
            String[] projection = new String[]{
                    android.provider.CalendarContract.Calendars._ID,
                    android.provider.CalendarContract.Calendars.ACCOUNT_NAME,
                    android.provider.CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                    android.provider.CalendarContract.Calendars.NAME,
                    android.provider.CalendarContract.Calendars.CALENDAR_COLOR
            };

//            managedCursor = ((Activity) mContext).managedQuery(uri, projection, null, null, null);
            managedCursor = mContext.getContentResolver().query(uri, projection, null, null, null);

            if (managedCursor != null && managedCursor.moveToFirst()) {
                String calName = managedCursor.getString(PROJECTION_ID_INDEX);
//                String calId = managedCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                try {
                    if (calName != null)
                        result = Integer.parseInt(calName);
                } catch (Exception ignored) {
                }
            }
            if (managedCursor != null) {
                managedCursor.close();
            }
        }

        return result;
    }
}

