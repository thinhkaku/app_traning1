package aero.pilotlog.sync;

/**
 * Created by tuan.na on 8/18/2015.
 */
public class SyncStatus {
    public static final int SUCCESS = 1;
    public static final int CONNECTING = 2;
    public static final int SEARCHING_FILES = 90;
    public static final int X_FILES_FOUND = 91;

    public static final int DOWNLOAD_FILE = 3;
    public static final int PROCESS_FILE = 4;
    public static final int PROCESSING_ICAL_DATA = 5;
    public static final int PROCESSING_LOGBOOK_FLIGHTS = 6;
    public static final int CREATING_XML_FILE = 7;
    public static final int UPLOADING_XML_FILE = 8;
    public static final int UPLOAD_PICTURE_SUCCESS = 9;
    public static final int UPLOAD_NEXT_PICTURE = 10;
    public static final int SYNC_COMPLETED = 11;
    public static final int UPDATE_PROGRESS = 12;
    public static final int CLEANING_RECORDS = 13;
    public static final int PROCESSING_AIRCRAFT = 14;
    public static final int PROCESSING_AIRFIELDS = 15;
    public static final int PROCESSING_PILOT = 16;
    public static final int PROCESSING_FLIGHT = 17;
    public static final int UPDATING_TOTALS = 18;
    public static final int UPDATING_GRAND_TOTALS = 19;
    public static final int PROCESSING_CURRENCIES = 20;
    public static final int CREATEING_XML_FILE_DONE = 21;

    public static final int NO_INTERNET = -2;
    public static final int ERROR_SYNC_PC = -3;
    public static final int ERROR_DATA_PROCESSING = -4;
    public static final int ERROR_ADD_LOGBOOK_FLIGHTS = -41;
    public static final int ERROR_FILE_DAMAGED = -16;

    public static final int ERROR_CREATING_XML_FILE = -5;
    public static final int ERROR = -6;
    public static final int FAILED_FTP_UPLOAD = -7;
    public static final int FAILED = -8;
    public static final int NEED_UPDATE = -9;
    public static final int XML_TOO_OLD = -10;
    public static final int NO_INTERNET_ACCESS = -11;
    public static final int ERROR_CALENDAR_NAME = -12;
    public static final int ERROR_CALENDAR_NO_NAME = -14;
    public static final int ERROR_EOF = -15;
}
