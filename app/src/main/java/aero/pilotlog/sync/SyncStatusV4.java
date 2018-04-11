package aero.pilotlog.sync;

/**
 * Created by tuan.na on 8/27/2015.
 * Status v4
 */
public class SyncStatusV4 {

        public static final int CONNECT = 1;
        public static final int CONNECT_SUCCESS = 2;
        public static final int DOWNLOAD_FILE = 3;
        public static final int PROCESS_FILE = 4;
        public static final int DOWNLOAD_FILE_SUCCESS = 5;
        public static final int DOWNLOAD_PICTURE = 6;
        public static final int DOWNLOAD_PICTURE_SUCCESS = 7;
        public static final int CREATE_TABLE_ID = 8;
        public static final int CREATE_TABLE_ID_SUCCESS = 9;
        public static final int CREATE_XML_FILE = 10;
        public static final int LOGIN_TO_CLOUD = 11;
        public static final int LOGIN_TO_CLOUD_SUCCESS = 12;
        public static final int UPLOAD_XML = 13;
        public static final int UPLOAD_XML_SUCCESS = 14;
        public static final int UPLOAD_PILOT_PICTURE = 15;
        public static final int UPLOAD_PICTURE_SUCCESS = 16;
        public static final int UPLOADING_PILOT_PICTURE = 17;
        public static final int SYNC_COMPLETE = 20;
        public static final int UPDATE_TABLE_ID = 18;
        public static final int SET_TABLE_ID_SUCCESS = 19;
        public static final int CREATEING_XML_FILE_DONE = 31;

        //processing xml
        public static final int CLEANING_RECORD = 21;
        public static final int PROCESSING_AIRCRAFTS = 22;
        public static final int PROCESSING_AIRFIELDS = 23;
        public static final int PROCESSING_PILOT = 24;
        public static final int PROCESSING_FLIGHTS = 25;
        public static final int UPDATING_TOTALS = 26;
        public static final int UPDATING_GRAND_TOTALS = 27;
        public static final int PROCESSING_LOGBOOK_FLIGHTS = 28;
        public static final int PROCESSING_CURRENCIES = 29;
        public static final int PROCESSING_ICAL = 30;


        public static final int NO_CONNECTION = -2;
        public static final int SLOW_CONNECTION = -3;
        public static final int FAILED_TO_MOVE_FILE = -4;
        public static final int FAILED_TO_DOWNLOAD_FILE = -5;
        public static final int FAILED_TO_DELETE_FILE = -6;
        public static final int NEED_UPDATE = -8;
        public static final int XML_TOO_OLD = -9;
        public static final int ERROR_CREATING_XML_FILE = -10;
        public static final int ERROR_SYNC_PC = -11;
        public static final int ERROR_PARSING_XML = -12;
        public static final int LOST_CONNECTION = -13;
        public static final int FAILED_TO_GET_TABLE_ID = -14;
        public static final int ERROR_SET_TABLE_ID = -15;
        public static final int ERROR = -16;
        public static final int FAILED_TO_LOG_IN_TO_CLOUD = -17;
        public static final int FAILED_TO_UPLOAD_XML = -18;
        public static final int FAILED_TO_STORE_FILE = -19;
        public static final int FAILED_TO_UPLOAD_PICTURE = -20;
        public static final int NO_RECORD_XML = -21;
        public static final int ERROR_CALENDAR_NAME = -22;
        public static final int ERROR_CALENDAR_NO_NAME = -23;
        public static final int ERROR_IN_TABLE_ID = -24;
        public static final int ERROR_FILE_DAMAGED = -25;
}
