package aero.pilotlog.common;

import aero.pilotlog.utilities.MCCApplication;


/**
 * Created by tuan.pv on 2015/07/06.
 */
public interface MCCPilotLogConst {

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }
    public static final String WATCH_VIDEO_LINK= "http://support.crewlounge.aero/support/solutions/articles/6000096159";
    public static final boolean DEBUG = true;
    String TYPE_OF_APP_TIME_SELECTED = "typeOfAppCounterSelected";
    boolean sIsTablet = MCCApplication.isTablet;
    int sScreenWidth = MCCApplication.SCREEN_WIDTH;
    int sScreenHight = MCCApplication.SCREEN_HEIGHT;
    String SYNC_INCOMPLETE_FLIGHT = "Sync Incomplete Flight";
    String SCREEN_TYPE = "screen_type";
    String FLIGHT_CODES = "flight_codes";

    String STRING_EMPTY = "";
    String SLASH = "/";

    String PC_PHONE = "Phone";
    String PC_PC = "PC";

    String IATA = "IATA";
    String ICAO = "ICAO";

    String NULL = "null";
    String SYNC_HISTORY_FOLDER = "Sync History Folder";

    String LOG_AIRFIELD_FLIGHT = "LogAirfieldFlight";
    String LOG_FUEL_FLIGHT = "LogFuelFlight";
    String LOG_FUEL_USED_FLIGHT = "LogFueUsedlFlight";
    String LOG_FUEL_PLANED_FLIGHT = "LogFuePlanedlFlight";
    String LOG_TODAY_FLIGHT = "LogToDayFlight";
    String LOG_LDG_DAY_FLIGHT = "LogLdgDayFlight";
    String LOG_LANDING_FLIGHT = "LogLandingFlight";
    String LOG_SIGN_FLIGHT = "LogSignFlight";

    String LOG_DATE_FLIGHT = "LogDateFlight";
    String LOG_AIRCRAFT_FLIGHT = "LogAirCraftFlight";
    String LOG_NUMBER_FLIGHT = "LogNumberFlight";
    String LOG_TOTAL_TIME_FLIGHT = "LogTotalTimeFlight";
    String LOG_PIC_FLIGHT = "LogPICFlight";
    String LOG_PICUS_FLIGHT = "LogPicusFlight";
    String LOG_COPILOT_FLIGHT = "LogCoPilotFlight";
    String LOG_DUAL_FLIGHT = "LogDualFlight";
    String LOG_INSTRUCTOR_FLIGHT = "LogInstructorFlight";
    String LOG_EXAMINER_FLIGHT = "LogExaminerFlight";
    String LOG_RELIEF_FLIGHT = "LogReliefFlight";
    String LOG_NIGHT_FLIGHT = "LogNightFlight";
    String LOG_IFR_FLIGHT = "LogIFRFlight";
    String LOG_ACTUAL_FLIGHT = "LogActualFlight";
    String LOG_SIMULATED_FLIGHT = "LogSimulatedFlight";
    String LOG_CROSS_FLIGHT = "LogCrossFlight";
    String LOG_USER1_FLIGHT = "LogUser1Flight";
    String LOG_USER2_FLIGHT = "LogUser2Flight";
    String LOG_USER3_FLIGHT = "LogUser3Flight";
    String LOG_USER4_FLIGHT = "LogUser4Flight";
    String LOG_TASK_FLIGHT = "LogTaskFlight";
    String LOG_AUTOLAND_FLIGHT = "LogAutoLandFlight";
    String LOG_HOLDING_FLIGHT = "LogHoldingFlight";
    String LOG_PAX_FLIGHT = "LogPaxFlight";
    String LOG_UNUMERIC_FLIGHT = "LogUNumericFlight";
    String LOG_UTEXT_FLIGHT = "LogUTextFlight";
    String LOG_UBOOL_FLIGHT = "LogUBOOLFlight";
    String LOG_DEICING_FLIGHT = "LogDeicingFlight";
    String LOG_DELAY_FLIGHT = "LogDelayFlight";
    String LOG_APP1_FLIGHT = "LogApp1Flight";
    String LOG_APP2_FLIGHT = "LogApp2Flight";
    String LOG_APP3_FLIGHT = "LogApp3Flight";
    String LOG_GLIDER_FLIGHT = "LogGliderFlight";
    String LOG_LIFT_FLIGHT = "LogLiftFlight";
    String LOG_REMARKS_FLIGHT = "LogRemarksFlight";
    String LOG_INSTRUCTION_FLIGHT = "LogInstructionFlight";
    String LOG_REPORT_FLIGHT = "LogReportFlight";
    String LOG_PICTURE_FLIGHT = "LogPictureFlight";
    String LOG_PILOT2_FLIGHT = "LogPilot2Flight";
    String LOG_PILOT3_FLIGHT = "LogPilot3Flight";
    String LOG_PILOT4_FLIGHT = "LogPilot4Flight";
    String LOG_PAIRING_FLIGHT = "LogPairingFlight";
    String LOG_DELAY2_FLIGHT = "LogDelay2Flight";
    String LOG_TYPE_OF_OPS1_FLIGHT = "LogTypeOfOps1Flight";
    String LOG_TYPE_OF_OPS2_FLIGHT = "LogTypeOfOps2Flight";
    String LOG_TYPE_OF_OPS3_FLIGHT = "LogTypeOfOps3Flight";
    String LOG_APP_1_NUMBER_FLIGHT = "LogApp1NumberFlight";
    String LOG_APP_2_NUMBER_FLIGHT = "LogApp2NumberFlight";
    String LOG_APP_3_NUMBER_FLIGHT = "LogApp3NumberFlight";
    String LOG_CREWLIST_FLIGHT = "LogCrewListFlight";

    String FLAG_IS_FROM_OTHER_SCREEN = "FlagIsFromOtherScreen";
    String XML_FOLDER = "XML";
    String XMLPC_FOLDER = "XMLPC";
    String DB_FOLDER = "DB_FOLDER";
    String SYNCED_SIGN_FOLDER = "Synced Sign";
    String PICTURE_FOLDER = "Picture";
    String SIGN_FOLDER = "Sign";
    String BACK_UP_FILE_NAME = "mobile.%s.backup.sqlite";
    String BACK_UP_FILE_NAME_V4 = "backup.%s.zip";

    /*About*/
    String GOOGLE_MARKET1 = "market://details?id=";
    String GOOGLE_MARKET2 = "https://play.google.com/store/apps/details?id=";
    String PACKAGE_GMAIL = "com.google.android.gm";
    String ACTIVITY_GMAIL = "com.google.android.gm.ComposeActivityGmail";
    String EMAIL_HELPDESK = "support@crewlounge.aero";
    String PREFIX_SUBJECT = "mobile.";
    String SUFFIX_SUBJECT = ".log";
    String INTENT_TYPE = "message/rfc822";
    String DOT_GM = ".gm";
    String GMAIL = "gmail";
    String INBOX = "inbox";
    String TEXT_SYNC_ID = "SyncID: ";
    String TEXT_APP_VERSION = "Version: ";
    String WRITE_MESSAGE_HERE = "Write your message here: ";
    String LINE_EMAIL_HEADER = "______________________________________________";
    String TEXT_DEVICE = "Platform: ";
    String TEXT_OS = "Android ";
    String MCC_APP_NAME = "App: CrewLounge #PILOTLOG";
    String TEXT_LINE_SEPARATOR = "line.separator";
    String MCC_SITE = "http://www.crewlounge.aero/";
    String TERM_AND_COND_SITE ="https://www.crewlounge.aero/disclaimer/";// "http://www.mccpilotlog.net/disclaimer/";

    /*Help*/
    String PDF_USER_GUIDE = "http://www.crewlounge.aero/docpdf/pilotlog_userguide.pdf";
    String PDF_FILE_PATH = "/sdcard/pilotlog_userguide.pdf";
    String ANDROID_PACKAGE_LINK = "com.android.vending";
    String INTENT_TYPE_PDF = "application/pdf";
    String GOOGLE_MARKET_PDF = "market://details?id=com.adobe.reader";
    String USER_GUIDE_VERSION = "UserGuideVersion";
    int DEFAULT_GUIDE_VERSION = 26;

    /*Delay*/
    String SCREEN_VIEW_AS_TYPE = "screen_view_as_type";
    int LIST_MODE = 1;
    int SELECT_MODE = 2;

    /*Tails*/
    int MAX_LENGTH_TAGS = 10;
    String DELAY_OR_TAILS = "delay_or_tails";
    int DELAY_ADAPTER = 1;
    int TAILS_ADAPTER = 2;
    String CONTINENT_ASIA = "AS";
    String CONTINENT_SOUTH_AMERICA = "SA";
    String CONTINENT_MORTH_AMERICA = "NA";
    String CONTINENT_AFRICA = "AF";
    String CONTINENT_EUROPE = "EU";
    String CONTINENT_ANTARCTICA = "AN";
    String CONTINENT_OCEANIA = "OC";

    /*Expense*/
    String EXPENSE_GROUP_ID = "expense_group_id";

    /*Main Menu*/
    int BUTTON_AIRFIELDS = 1;
    int BUTTON_AIRCRAFT = 2;
    int BUTTON_FLIGHT = 3;
    int BUTTON_WEATHER = 4;
    int BUTTON_SIGN = 5;
    int BUTTON_PILOTS = 6;
    int BUTTON_DUTY = 7;
    int BUTTON_TOTALS = 8;
    int BUTTON_LOGBOOK = 9;
    int BUTTON_QUALIFICATION = 10;
    int BUTTON_DELAY = 11;
    int BUTTON_SYNC = 12;
    int BUTTON_HELP = 13;
    int BUTTON_TAILS = 14;
    int BUTTON_SETTINGS = 15;
    int BUTTON_LIMITS = 16;
    int BUTTON_EXPENSES = 17;

    /*With Database*/
    int NON_SEARCH = 1;
    int SEARCH_ON_REGAC_TAILS = 2;

    //pilot info
    String PILOT_CODE_KEY = "PilotCode";

    String SPLASH = "/";
    String PILOT_IMAGE_FILE_NAME_V3 = "img.%s.jpg";
    String PILOT_IMAGE_FILE_NAME_V4 = "img.%s";
    String EXPENSE_IMAGE_FILE_NAME = "img.expense.%s";


    //Pilot add edit
    String PILOT_ADD_EDIT_VIEW_TYPE = "PilotViewType";
    int PILOT_ADD_VIEW = 0;
    int PILOT_EDIT_VIEW = 1;
    int PILOT_IMPORT_VIEW = 3;
    String IS_ADD_NEW_PILOT_FOR_FLIGHT = "IsAddNewPilotForFlight";
    String IS_ADD_NEW_FOR_FLIGHT = "IsAddNewForFlight";

    String PILOT_CODE_FORMAT = "p%05d";

    /*Fragment Airfield*/
    String PREF_SORT_TYPE = "sort_type";
    int SORT_BY_AIRFIELD_NAME = 0;
    int SORT_BY_AIRFIELD_ICAO = 1;
    int SORT_BY_AIRFIELD_IATA = 2;
    String URL_NOTAMS_PREFIX = "https://pilotweb.nas.faa.gov/PilotWeb/notamRetrievalByICAOAction.do?method=displayByICAOs&reportType=RAW&formatType=DOMESTIC&retrieveLocId=";
    String URL_NOTAMS_SUFFIX = "&actionType=notamRetrievalByICAOs";

    /*Airfield + Weather */
    String PREF_SORT_TYPE_WEATHER = "sort_type_weather";
    String AIRFIELD_CODE_KEY = "AFCode";
    String AIRFIELD_INFO_MODE = "Airfield_info_mode";
    String PILOT_INFO_MODE = "Pilot_info_mode";
    String AIRCRAFT_INFO_MODE = "Aircraft_info_mode";
    String AIRFIELD_NOTES_KEY = "AirfieldNotes";
    String AIRFIELD_IDENTIFIER_KEY = "AirfieldIdentifier";
    String AIRFIELD_WEATHER_CODE = "AirfieldWeatherCode";
    String WEATHER_VIEW_TYPE = "WeatherViewType";
    String AIRFIELD_OR_WEATHER = "airfield_or_weather";
    String AIRFIELD_BOOLEAN_IS_DEP = "AirfieldBooleanIsDep";
    String GOOGLE_EARTH_BUNDLE = "Google Earth Bundle";
    int AIRFIELD_ADAPTER = 1;
    int WEATHER_ADAPTER = 2;
    int MENU_WEATHER_REFRESH_TODAY = 0;
    int MENU_WEATHER_REFRESH_ALL = 1;
    int AIRFIELD_SELECT_MODE_FROM_NAVIGATION = 1;
    int AIRFIELD_SELECT_MODE_FROM_OTHER = 2;
    String IS_FROM_AIRFIELD_INFO = "IsFromAirfieldInfo";
    /*Pilot list */
    String PILOT_SORT_KEY = "PilotSortKey";
    int PILOT_SORT_TYPE_NAME = 0;
    int PILOT_SORT_TYPE_EMPID = 1;
    String PILOT_NAME_KEY = "PilotName";
    String PILOT_PHONE_KEY = "PilotPhone";
    String PILOT_EMAIL_KEY = "PilotEmail";

    String FROM_WEATHER_LIST = "from_weather_list";

    /*Signature*/
    String SIGNATURE_VIEW_TYPE = "SignatureViewType";
    int SIGNATURE_FROM_MENU = 0;
    int SIGNATURE_FROM_FLIGHT_ADD_EDIT = 1;
    String SIGN_NAME_FORMAT = "sign.%s";

    /*GoogleEarth*/
    String GOOGLE_EARTH_FULLSCREEN = "GooleEarthFullScreen";
    int GOOGLE_EARTH_FORMAT = 0;

    /*Aircraft List*/

    String AIRCRAFT_CODE_KEY = "Aircraft Code";
    String AIRCRAFT_SORT_KEY = "AircraftSortKey";
    int AIRCRAFT_SORT_TYPE = 0;
    int AIRCRAFT_SORT_TAIL = 1;
    String AIRCRAFT_ACTUAL_CODE = "1";
    String AIRCRAFT_SIMULATOR_CODE = "2";
    String AIRCRAFT_SIMULATOR = " (simulator)";
    String FIRST_AIRCRAFT_CODE_KEY = "FirstAircraft";
    String AIRCRAFT_REF_SEARCH = "AircraftRefSearch";
    String ZNPT_TYPE = "znpt_type";

    /*Aircraft Info*/
    String AIRCRAFT_MIN_U1 = "U1";
    String AIRCRAFT_MIN_U2 = "U2";
    String AIRCRAFT_MIN_U3 = "U3";
    String AIRCRAFT_MIN_U4 = "U4";

    /*Sync */
    String FTP_BASE_IP = "CL-Cloud.net";
    String FTP_USER_NAME = "mccUser_MOB";
    String FTP_PASSWORD = "login%1967#MCC";
    String SEARCH_LK = "http://" + FTP_BASE_IP + "/getsynclist.asp?file=pc.";
    String COLOR_DARK_GREEN = "#006400";
    int SYNC_HISTORY_DELETE_ALL = 0;
    int SYNC_HISTORY_DELETE_OLDER = 1;
    String MAIL_TO = "support@crewlounge.aero";
    String SYNC_LAST_PILOT_CODE_KEY = "sync_last_pilot_code_key";
    String SYNC_LAST_Flight_CODE_KEY = "sync_last_flight_code_key";

    /**
     * Tab character \t
     */
    String TAB_CHARACTER = "\t";
    /**
     * Carriage character \r
     */
    String CARRIAGE_CHARACTER = "\r";
    /**
     * New line character \n
     */
    String NEW_LINE_CHARACTER = "\n";

    String COMMA = ", ";
    String SPACE = " ";
    /*Flight View*/
    String SIMULATOR = "2";
    /*Logbook*/
    String FLIGHT_LOGBOOK_CODE = "flight_code";
    int LOGBOOK_DELETE_ONE = 1;
    int LOGBOOK_DELETE_ALL = 2;
    int LOGBOOK_DELETE_PRIOR = 3;
    int LOGBOOK_DELETE_IN = 4;
    // PL-433
    int MENU_LOGBOOK_DELETE_ALL = 0;
    int MENU_LOGBOOK_DELETE_PRIOR = 2;
    int MENU_LOGBOOK_DELETE_IN = 4;
    //End PL-433
    int APPROACH_TYPE_1 = 1;
    int APPROACH_TYPE_2 = 2;
    int APPROACH_TYPE_3 = 3;
    int CREW_PIC = 1;
    int CREW_2ND = 2;
    int CREW_3RD = 3;
    int CREW_4TH = 4;
    int NUMER_ITEM_LOGBOOK_ONE_PAGE = 20;
    String PREF_SORT_TYPE_LOGBOOK = "sort_type_logbook";

    /*Flight Add */
    String DEFAULT_MAX_HOUR = "23:59";
    String DEFAULT_MIN_MINUTE = "00:00";
    String DEFAULT_MAX_MINUTE = "00:59";
    int TASK_PF = -1;
    int TASK_PM = 0;
    String TASK_PF_TEXT = "PF";
    String TASK_PM_TEXT = "PM";
    String FLIGHT_SCREEN_MODE = "Flight Screen Mode";
    String FLIGHT_CODE = "Flight code";
    String EXPENSE_CODE = "Expense code";
    String DUTY_CODE = "Duty code";
    String ONE_STRING = "1";
    String ZERO_STRING = "0";
    String X_MARK = "x";
    int TRUE_IN_DB = -1;
    int FALSE_IN_DB = 0;
    String TRUE_IN_DB_STR = "-1";
    int FLIGHT_WARNING_QUANTITY = 95;
    int FLIGHT_MAX_QUANTITY = 100;
    String SIGN_FILE_NAME_FORMAT = "img.%s.jpg";
    String FLIGHT_DATE = "Flight Date";
    String SIGNATURE_BUNDLE = "Signature Bundle";
    String REC_STATUS_1 = "1";
    String REC_STATUS_2 = "2";
    String REC_STATUS_3 = "3";
    String APPROACH_VISUAL = "301";
    String PREF_SORT_TYPE_DUTY = "sort_type_duty";
    /*Flight List*/
    String PREF_SORT_TYPE_FLIGHT = "sort_type_flight";
    String PREF_DATE_BACKUP = "date_backup";
    int SORT_BY_1_GREAT_THAN_31 = 0;
    int SORT_BY_31_GREAT_THAN_1 = 1;
    int SORT_BY_31_LESS_THAN_1 = 2;
    // PL-433
    //index menu
    int MENU_FLIGHT_EDIT = 0;
    int MENU_FLIGHT_EDIT_PASTE = 2;
    int MENU_FLIGHT_NEXT = 4;
    int MENU_FLIGHT_RETURN = 5;
    int MENU_FLIGHT_DELETE = 6;

    //end PL-433
    /*Currencies*/
    String CURRENCY_COLOR_CODE_RED = "2";
    String CURRENCY_COLOR_CODE_ORANGE = "1";
    String CURRENCY_COLOR_CODE_CYAN = "0";
    String PREF_TIME_LAST_UPDATE = "time_last_update";

    /*Sync History*/
    int HISTORY_FLIGHT_RECORD = 1;
    int HISTORY_PILOT_PICTURE = 2;
    int HISTORY_SIGN_PICTURE = 3;

    /*Help*/
    String URL_SUPPORT = "http://crewlounge.support/support/solutions/folders/6000012791";//support.mccpilotlog.net/support
    String URL_START = "http://crewlounge.support/solution/articles/9863";

    /*v5 GUID pilot code empty and pilot code self*/
    String PILOT_CODE_EMPTY = "00000000-0000-0000-0000-000000000000";
    String PILOT_CODE_SELF = "00000000-0000-0000-0000-000000000001";

    String LANDING_SCREEN = "landing_screen";
    String PRESERVE_ACCURACY = "preserve_accuracy";
    String SPLIT_KEY_APPEND = "|:";
    String SPLIT_KEY = "\\|:";
    String SELECT_LIST_TYPE = "select_list_type";
    String SELECT_LIST_TYPE_WEATHER = "select_list_type_weather";
    String SELECT_LIST_TYPE_AIRFIELD_NAVIGATION = "select_list_type_airfield_navigation";
    String SELECT_LIST_TYPE_AIRFIELD_INFO = "select_list_type_airfield_info";
    String SELECT_LIST_TYPE_FLIGHT_LOGGING_HOME_BASE = "select_list_type_flight_logging_home_base";
    String SELECT_LIST_TYPE_FLIGHT_ADD = "select_list_type_flight_add";
    String SELECT_LIST_TYPE_DUTY_ADD = "select_list_type_duty_add";
    String SELECT_LIST_TYPE_AIRFIELD_ADD = "select_list_type_airfield_add";
    String SELECT_LIST_TYPE_LOGBOOK_SEARCH = "select_list_type_logbook_search";
    String SELECT_LIST_DEPARTURE_OR_ARRIVAL = "select_list_departure_or_arrival";

    String SELECT_LIST_TYPE_AIRCRAFT_INFO = "select_list_type_aircraft_info";
    String SELECT_LIST_TYPE_PILOT_INFO = "select_list_type_pilot_info";
    String SELECT_LIST_PILOT_INDICATOR = "select_list_pilot_indicator";
    String ADD_EDIT_MODE = "add_edit_mode";
    String ADD_MODE = "add_mode";
    String EDIT_MODE = "edit_mode";

    int SETTING_CODE_FUELS_MONITORING = 161;
    int SETTING_CODE_DEDUCT_RELIEF = 491;
    int SETTING_CODE_IS_LOG_DECIMAL = 50;
    int SETTING_CODE_ACCURACY = 500;
    int SETTING_CODE_HOME_BASE = 13;
    int SETTING_CODE_IATA = 8;
    int SETTING_CODE_TIME_MODE = 308;
    int SETTING_CODE_DATE_MODE = 309;
    int SETTING_CODE_NIGHT_TIME_SRSS = 314;
    int SETTING_CODE_NIGHT_TIME_FROM = 315;
    int SETTING_CODE_NIGHT_TIME_UNTIL = 316;
    int SETTING_CODE_DEFAULT_CURRENCY = 300;
    int SETTING_CODE_NIGHT_CALC = 22;
    int SETTING_CODE_NIGHT_MODE = 159;
    int SETTING_CODE_USER_TIME_4 = 415;
    int SETTING_CODE_USER_TIME_4_CAPTION = 483;
    int SETTING_CODE_SORT_FLIGHT = 80;
    int SETTING_CODE_SORT_LOGBOOK = 310;

    int SETTING_CODE_USER_DEFINE_CAPTION1 = 480;
    int SETTING_CODE_USER_DEFINE_CAPTION2 = 481;
    int SETTING_CODE_USER_DEFINE_CAPTION3 = 482;
    int SETTING_CODE_USER_DEFINE_CAPTION4 = 483;
    int SETTING_CODE_USER_NUM = 484;
    int SETTING_CODE_USER_TEXT = 485;
    int SETTING_CODE_USER_BOOL = 486;
    int SETTING_CODE_LAST_QUALIFICATION_CHECK = 495;

    String NIGHT_CALC_SS_SR = "0";
    String NIGHT_CALC_FIX_HOURS = "1";
    String NIGHT_CALC_BOTH = "2";
    String NIGHT_MODE_AUTO_LOAD_WITHOUT_TO_LDG = "2";

    /*String LOGBOOK_AIRFIELD_CODE_FILTER = "logbook_airfield_code_filter";
    String LOGBOOK_AIRFIELD_CODE_FILTER = "logbook_airfield_code_filter";
    String LOGBOOK_AIRFIELD_CODE_FILTER = "logbook_airfield_code_filter";*/
}