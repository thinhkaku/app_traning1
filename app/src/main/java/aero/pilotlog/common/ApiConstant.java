package aero.pilotlog.common;

import android.Manifest;

/**
 * Created by phuc.dd on 3/14/2017.
 */
public class ApiConstant {
    public static final boolean DEBUG = true;

    public static final String CURRENT_LAT = "latitude";
    public static final String CURRENT_LONG = "longtitude";
    public static final String TIME_TO_SAVE_PARKING_POSITION = "time to save parking position";

    public static final String WIFI_LOGIN_USER = "wifi_login";
    public static final String WIFI_PASSWORD = "wifi_pass";

    public static final String PARKING_POSITION = "parking_position";
    public static final String HOTEL_ROOM = "hotel_room";

    public static final String DOWNLOAD_FROM_FIRST_DAY_OF_MONTH = "1";
    public static final String LENGT_HTML1 = "length_html1";
    public static final String LENGT_HTML2 = "length_html2";
    public static final String LENGT_HTML3 = "length_html3";

    public static final String PERIOD_HTML1 = "period_html1";
    public static final String PERIOD_HTML2 = "period_html2";
    public static final String PERIOD_HTML3 = "period_html3";

    public static final int HTML1_INDEX = 0;
    public static final int HTML2_INDEX = 1;
    public static final int HTML3_INDEX = 2;
    public static final int HTML1 = 1;
    public static final int HTML2 = 2;
    public static final int HTML3 = 3;

    public static final int REQUEST_CODE_CHOOSE_COUNTRY = 1;
    public static final String UTC = "UTC";
    public static final String DEFAULT_TIMEZONE = "UTC";
    public static final String TIMEZONE = "timezone";
    public static final String SPACE_STR = " ";
    public static final String SECURITY_SEED = "crewCONNECTs";
    public static final String SLASH = "/";
    public static final String HTML_CONTENT = "html_content";

    public static final String BASE_CURRENCY = "base_currency";
    public static final String OTHER_CURRENCY = "other_currency";

    private static final String MCCPILOTLOG_URL = "http://www.crewlounge.aero";
    public static final String SKY_VECTOR_URL = "http://skyvector.com/airport/";
    public static final String TRIP_ADVISOR_URL = "http://www.tripadvisor.com/Search?q=";
    public static final String WEATHER_CHANNEL_URL = "http://m.weather.com/weather/tomorrow/";
    public static final String METAR_URL = "http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=";
    public static final String TAF_URL = "http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&stationString=";
    public static final String GET_STARTED_URL = "http://support.crewlounge.aero/solution/articles/6000096159/";
    public static final String SURROUNDING_COUNTRIES_URL = "https://www.bing.com/maps?q=";
    public static final String MORE_TIME_ZONE_INFO_URL = "http://www.timeanddate.com/time/zone/";


    private static final String DIR_DOCPDF = "/docpdf/";
    public static final String DIR_PDF = "/pdfRoster/";
    public static final String DIR_HTML = "/html";
    public static final String DIR_CSS = "/html/css";
    public static final String DIR_IMG = "/html/img";
    public static final String DIR_CUSTOM = "/html/custom";
    public static final String DIR_WEATHER = "/weather";
    public static final String DIR_USER = "/user";






    public static final String IMAGE_SIZE_SMALL_LABEL = "small";
    public static final String IMAGE_SIZE_LARGE_LABEL = "large";
    public static final String IMAGE_SIZE_THUMB_LABEL = "thumb";
    public static final int IMAGE_SIZE_THUMB = 250;
    public static final int IMAGE_SIZE_SMALL = 500;
    public static final int IMAGE_SIZE_LARGE = 2000;



    public static final String DIR_USER_PROFILE_PICTURE = "/user/images";
    //User image file name format: UserID_large.jpg or UserID_small.jpg or UserID_thumb.jpg
    private static final String USER_IMAGE_FILE_NAME = "%s_%s.jpg";
    public static final String USER_IMAGE_FILE_PATH = DIR_USER_PROFILE_PICTURE + "/" + USER_IMAGE_FILE_NAME;
    public static final String DIR_SHARED_PREFS = "/../shared_prefs";
    public static final String NOTIFICATION_FILENAME = "crewCONNECTNotification.txt";
    private static final String USER_GUIDE_CHECK_FILENAME = "crewconnect_userguide.txt";
    public static final String USER_GUIDE_FILENAME = "crewconnect_userguide.pdf";
    private static final String TERMS_AND_CONDITIONS_FILENAME = "disclaimer.html";
    public static final String SETTINGS_FILENAME = "settings.xml";
    public static final String NOTIFICATION_URL = MCCPILOTLOG_URL + DIR_DOCPDF + NOTIFICATION_FILENAME;
    public static final String USER_GUIDE_CHECK_URL = MCCPILOTLOG_URL + DIR_DOCPDF + USER_GUIDE_CHECK_FILENAME;
    public static final String USER_GUIDE_URL = MCCPILOTLOG_URL + DIR_DOCPDF + USER_GUIDE_FILENAME;
    public static final String TERMS_AND_CONDITIONS_URL = MCCPILOTLOG_URL + SLASH + TERMS_AND_CONDITIONS_FILENAME;
    public static final String EMAIL_REPORT_TO = "support@crewlounge.aero";
    public static final String EMAIL_HELPDESK_SUBJECT_TERM_OF_USE = "crewCONNECT - Terms Of Use";
    public static final String EMAIL_SYNC_LOG_SUBJECT = "crewCONNECT – Sync Log";
    public static final String EMAIL_HELPDESK_BODY_AIRLINE = "Airline: ";

    public static final String KEY_PREF_USER_GUIDE_VERSION = "pref_user_guide_version";
    public static final String KEY_PREF_AIMS_ON = "pref_aims_on";
    public static final String KEY_PREF_FIRST_LAUNCH_APP = "pref_first_lauch";
    public static final String KEY_NEW_REGISTER_USER = "pref_new_user";
    public static final String KEY_IS_DIRECT_LOGIN_EXTRA = "pref_direct_extra";

    //inflight rest pref
    public static final String KEY_PREF_START_TIME_INFLIGHT_REST = "pref_start_time_inflight_rest";
    public static final String KEY_PREF_END_TIME_INFLIGHT_REST = "pref_end_time_inflight_rest";
    public static final String KEY_PREF_NUMBER_REST_SHIFTS_INFLIGHT_REST = "pref_number_rest_shifts_inflight_rest";
    public static final String KEY_PREF_MINUTE_CREW_CHANGE_INFLIGHT_REST = "pref_minute_crew_change_inflight_rest";
    public static final String ALARM_NOTIFICATION_TITLE = "In-flight Rest Alarm";
    public static final String ALARM_NOTIFICATION_MESSAGE = "Wake up!";

    //
    public static final String KEY_PREF_SHOW_PDF_WARNING = "key_pref_show_pdf_warning";
    //pickup pref
    public static final String KEY_PREF_FLIGHT_STD_PICKUP = "pref_flight_std_pickup";
    public static final String KEY_PREF_ARRIVING_AT_THE_AIRPORT_PICKUP = "pref_arriving_at_the_airport_pickup";
    public static final String KEY_PREF_TAXI_DRIVING_TIME_PICKUP = "pref_taxi_driving_time_pickup";
    public static final String KEY_PREF_WAKE_UP_PICKUP = "pref_wake_up_pickup";

    public static final String EMAIL_ROSTER_SUBJECT = "my ROSTER - ";
    public static final String EMAIL_ROSTER_BODY = "Generated by crewCONNECT.aero";

    public static final String CREW_DOCK_ACC_ELEMENT_NAME = "LoginWidgetH_userName";
    public static final String CREW_DOCK_ACC_ELEMENT_PASSWORD = "LoginWidgetH_password";
    public static final String CREW_DOCK_ACC_HANDLER = "handler";
    public static final String CREW_DOCK_ACC_TOOL = "tool";
    public static final String CREW_DOCK_ACC_RETURN_URL = "returnURL";
    public static final String CREW_DOCK_ACC_LOGIN_REDIRECT = "LoginWidgetH_redirect";
    public static final String CREW_DOCK_ACC_ID = "LoginWidgetH_id";
    public static final String CREW_DOCK_ACC_ADMINCONSOLE = "bNoAdminConsole";


    public static final String CREW_DOCK_SUBMIT_FORM = "LoginWidgetH_MainForm";
    public static final String CREW_DOCK_URL_LOGIN = "https://www.crewdock.com/pport/web/Login";
    public static final String OPSMAN_URL_LOGIN = "http://intranet.hlf.de";
    public static final String OPSMAN_URL_AUTH = "https://auth2.tui.de/ldap.login/index.asp?org=http%3A%2F%2Fintranet%2Ehlf%2Ede%2Fauthsrv%2Elogin%2Flogin%2Easp%3Forg%3D%252Fawca%252Ffg%252Easp%253Fid%253Dbqpu";
    public static final String OPSMAN_URL_THOMSON_LOGIN = "https://crewportal.thomson.co.uk/tom/auth/vb-auth.htm";
    public static final String CREW_DOCK_URL_PORTAL = "https://crewdock.com/pport/web/Portal/Cabin%20Crew";
    public static final String OPSMAN_URL_PORTAL = "https://intranet.hlf.de/ca/c/hz/";

    public static final String CREW_DOCK_URL_ROSTER = "https://www.crewdock.com/pport/web/Portal/Pilot/Personal/Rosters";
    public static final String CREW_DOCK_URL_ROSTER_CABIN_CREW = "https://crewdock.com/pport/web/Portal/Cabin%20Crew/Operational/Roster";
    public static final String CREW_DOCK_URL_WEB = "https://crewdock.com/pport/web";
    public static final String SABRE_URL_WEB_LOGIN = "CWPLogin.aspx";
    public static final String CITYJET_LOGIN_PAGE = "PortalLogin.aspx";
    public static final String SABRE_URL_WEB_LOGIN_ERROR = "CWPLogin.aspx?errorMsg=";
    public static final String SABRE_URL_WEB_PORTAL = "CWP_Default.aspx";
    public static final String SABRE_URL_WEB_ROSTER = "CWP_RosterTW.aspx";
    public static final String SABRE_URL_WEB_PERFORMED_ACTIVITIES = "CWP_Perma.aspx";
    public static final String SABRE_PARA_VIEWSTATE = "__VIEWSTATE";
    public static final String SABRE_PARA_EVENTTARGET = "__EVENTTARGET";
    public static final String SABRE_PARA_EVENTARGUMENT = "__EVENTARGUMENT";
    public static final String SABRE_PARA_CTRLUSERNAME = "ctrlUserName";
    public static final String SABRE_PARA_CTRLPASSWORD = "ctrlPassword";
    public static final String SABRE_PARA_BTN = "btnLogin";

    public static final String COMPANY_LIST_FILE = "CompanyList";
    public static final String CURRENCY_FILE = "currency";
    public static final String TERM_OF_USE_FILE = "TermsofUse.htm";
    public static final String PAGE_FORMAT_LAYOUT_FRIDGE = "fridge";
    public static final String PAGE_FORMAT_LAYOUT_LIST = "list";
    public static final String PRINT_CREATE_PREVIEW = "CREATE_PREVIEW";
    public static final String PRINT_CREATE_SEND = "CREATE_SEND";
    public static final String CALENDAR_SIMULATOR = "Simulator";
    public static final String STR_EMPTY = "";
    public static final String STATUS = "status";
    public static final String MESSAGE_USER_UNKNOWN = "User unknown";
    public static final String PROPE_SET_PICTURE = "setProfilePicture";
    public static final String LONG_TIME = "01/01/2099";
    //If airline support offline
    public static final String OFFLINE_SUPPORT = "1";
    public static final String UNITED_AIRLINES = "UAL";

    public static final String NEW_ROSTER_CODE = "new roster code";
    public static final String NEW_ROSTER_DEFINITION = "roster_code_definition";
    /*FOIP*/
    public static final String CODE_AIRLINE_EMIRATE = "UAE";
    /**/
    public static final String PREF_COMPANY = "pref_company";
    public static final String CURRENCY_TYPE = "CurrencyType";
    public static final String CURRENCY_CODE = "CurrencyCode";
    public static final String VERIFY_USER = "verifyedUser";
    public static final String REGISTERED_USER = "registeredUser";
    public static final String CONNECT_TO_MY_ACCOUNT = "connecToMyAccount";
    public static final String AIR_ATLANTA = "ABD";
    public static final String MOZAMBIQUE_AIRLINE = "LAM";
    /*AIMS*/
    public static final String AIMS_URL_WEB_VERIFY = "/wtouch/wtouch.exe/verify";
    public static final String AIMS_PARA_PASSOWRD = "Crew_Psw";
    public static final String AIMS_PARA_AD = "AD";
    public static final String AIMS_PARA_AD_EXIST = "ADExists";
    public static final String AIMS_PARA_ID = "Crew_Id";
    public static final String AIMS_PARA_CHK = "CHK";
    public static final String AIMS_PARA_CR = "CR";
    public static final String AIMS_PARA_VER = "VER";
    public static final String AIMS_PARA_UNO = "UNO";
    public static final String AIMS_PARA_EXITBTN = "EXITBTN";
    public static final String AIMS_PARA_NDAY = "nDays";
    public static final String AIMS_PARA_LANG_CODE = "LANG_CODE";
    public static final String AIMS_PARA_LANG_ISO = "LANG_ISO";
    public static final String AIMS_PARA_USER_AGENT = "UserAgent";
    public static final String AIMS_PARA_CRM = "Crm";
    public static final String AIMS_PARA_IDS = "Ids";
    public static final String AIMS_PARA_PASST = "Passt";
    public static final String AIMS_PARA_PUBLISH = "Published";
    public static final String AIMS_PARA_CALENDAR = "cal1";
    public static final String AIMS_PARA_TIME_FORMAT = "times_format";
    public static final String AIMS_OPER = "/wtouch/perinfo.exe/oper";
    public static final String AIMS_CRWSCHE = "/wtouch/perinfo.exe/crwsche";
    /*AIMS*/
    public static final String AIMS_CREW_PASSWORD = "Pass";
    public static final String AIMS_CREW_ID = "Id";
    public static final String AIR_ASIA = "AXM";
    public static final String WEST_JET_ENCORE = "WEN";
    public static final String SCOOT = "SCO";
    /*OPSMAN*/
    public static final String OPSMAN_ORG = "org";
    public static final String OPSMAN_US = "us";
    public static final String OPSMAN_PW = "pw";
    public static final String OPSMAN_OU = "ou";
    /*Merlot*/
    public static final String OPSMAN_SUBMIT = "submit";
    //THOMSON
    public static final String THOMSON_USER_NAME = "j_username";
    public static final String THOMSON_PASSWORD = "j_password";
    public static final String THOMSON_CRSTOKEN = "CSRFToken";
    public static final String THOMSON_MEMOWORD_CHAR_INDEX = "memWordCharsIndexes";
    public static final String THOMSON_MEMOWORD_CHAR = "memWordChars";
    public static final String THOMSON_USERROLES = "userRoles";
    public static final String THOMSON_REDIRECT = "redirect";
    /*OPS MAN*/
    public static final String ARKEFLY = "TFL";
    public static final String JETAIRFLY = "JAF";
    public static final String HAMBURG = "HH-Int";
    public static final String HAPAG_LLOYD = "HLX";
    public static final String THOMSON = "TOM";
    /*CREWLINK*/
    public static final String CODE_BRITISH_EMAESTRO = "BAW";
    public static final String CODE_BRITISH_CREWLINK = "BAWC";
    public static final String BRITISH_BASELINK_FLIGHT_CREW = "https://crewlink.baplc.com/statements/";
    public static final String BRITISH_BASELINK_CABBIN_CREW = "https://crewlink.baplc.com/ccroster/displayroster.do";
    /*New parse*/
    public static final String CODE_AIRLINE_TNT = "TNT";
    public static final String CODE_AIRLINE_HIFLY_PORTUGAL = "HFY";
    public static final String AZUL_BRAZILIAN = "AZU";
    public static final String JETSTAR_AUSTRALIA = "JST";
    public static final String CITY_JET_COMPANY_ID = "BCY";
    /*CREWCONNEX*/
    public static final String DANIS_AIR_TRANSPORT = "DTR";
    public static final String WEST_ATLANTIC = "SWN";
    public static final String SMALL_PLANET = "LLC";
    public static final String ARKIRA_ISRAELI = "AIZ";
    public static final String DANCOPTER = "DOP";
    public static final String NEXTJET = "NTJ";
    public static final String JET_TIME = "JTG";
    // OPSMAN
    public static final String ATLANTIC_AIRWAY = "FLI";
    public static final String TAROM = "ROT";
    public static final String STOBART_AIR = "STK";
    public static final String JE_JU = "JJA";
    public static final String SKY_AIRLINE = "SKU";
    //AIMS
    public static final String AIR_BERLIN = "BER";
    public static final String AER_LINGUS = "EIN";
    public static final String AIR_TRANSAT = "TSC";
    public static final String CODE_AIRLINE_WIDEROE = "WIF";
    public static final String EASY_JET = "EZY";
    public static final String EASY_JET_ANA = "EZYA";
    public static final String PRIVATE_AIR = "PTI";
    public static final String FLY_BE_FINLAND = "FCM";
    public static final String BMI_REGIONAL = "BMR";
    public static final String COPA_AIRLINE = "CMP";
    public static final String EURO_ATLANTIC = "MMZ";
    public static final String FLY_BE = "BEE";
    public static final String QATAR = "QTR";
    public static final String TRAVEL_SERVICE = "TVS";
    public static final String BEL_AIR = "BHP";
    public static final String GULF_AIR = "GFA";
    public static final String VIRGIN_ATLANTIC = "VIR";
    //
    public static final String HORIZON_AIR = "QXE";
    public static final String ATLAS_AIR = "GTI";
    public static final String AIR_ASTANA = "KZR";

    //netline
    public static final String LOT_POLISH = "LOT";

    /*AEGEAN Airline*/
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String REPOSITORY = "repository";
    public static final String SITE_NAME = "site_name";
    public static final String SECURE = "secure";
    public static final String RESOURCE_ID = "resource_id";
    public static final String LOGIN_TYPE = "login_type";
    /*Olympic Air*/
    public static final String X_PARAM = "x";
    public static final String Y_PARAM = "Y";
    /*Aer Lingus*/
    public static final String LOC = "loc";
    public static final String LOCATE_DATE = "localdate";
    public static final String LOGIN = "login";
    public static final String AIR_LINGUS_PASS = "passwd";
    /*Air Astana*/
    public static final String CURL = "curl";
    public static final String FLAG = "flags";
    public static final String FORCEDOWNLEVEL = "forcedownlevel";
    public static final String FORMDIR = "formdir";
    public static final String TRUSTED = "trusted";
    public static final String USERNAME = "username";
    public static final String SUBMIT_CREDS = "SubmitCreds";
    /*EASYJET*/
    public static final String LANGUAGE = "language";
    public static final String RSA_PORT = "rsa_port";
    public static final String V_HOST = "vhost";
    public static final String STATE = "state";
    public static final String LOG_ON_FORM = "mrhlogonform";
    public static final String TRANSL_FORM = "translform";
    public static final String MINI_UI = "miniui";
    public static final String SET_MIN = "tzoffsetmin";
    public static final String CONTENT_TYPE = "sessContentType";
    public static final String OVER_PASS = "overpass";
    public static final String UI_TRANSLATION = "ui_translation";
    public static final String LANG_CHAR = "langchar";
    public static final String LANG_SWITCHER = "langswitcher";
    public static final String ROAM_TEST_COOKIES = "uRoamTestCookie";

    public static final String ID = "Id";
    public static final String DEFAULT_EMAIL = "test.user@crewCONNECT.aero";
    public static final String DEFAULT_PASSWORD = "2015#AirCrew";
    public static final String DEFAULT_USER_ID = "1000007";
    public static final String USE_TEST_ACCOUNT = "use_test_account";
    public static final String SPACE = " ";
    /*crew_detail*/
    public static final String TAG_INTEREST = "like";
    public static final String TAG_ID = "id";
    public static final String SHARE_PICTURE = "share_picture";
    public static final String SIGN_IN = "Sign-In    ";
    public static final String START = "Start          ";
    public static final String END = "End            ";
    public static final String SIGN_OUT = "Sign-Out   ";
    public static final String STR_NONE = "NONE";
    public static final String STR_FOOTNOTE = "#crewCONNECT";
    /*parking coodinates*/
    public static final String LOCAL_UNITED_KINGDOM = "GB";
    public static final String LOCAL_INDIA = "IN";
    public static final String LOCAL_HONG_KONG = "HK";
    public static final String LOCAL_CANADA = "CA";
    public static final String LOCAL_AUSTRALIA = "AU";
    public static final String LOCAL_NEW_ZEALAND = "NZ";
    public static final String LOCAL_IRELAND = "IE";
    public static final double LocationAccuracyHundredMeters = 100;
    public static final double LocationAccuracyNearestFiftyMeters = 50;
    public static final double LocationAccuracyBest = 0.1;
    public static final String UPCOMMING_LAYOVERS = "1";
    /*Find & visit const*/
    public static final String HOMEBASE = "0";
    public static final String OTHER_AIRPORTS = "2";
    /*Error code*/
    public static final int ERROR_UNKNOWN = -1000;
    public static final int ERROR_WRONG_PASSWORD = 1;
    public static final int ERROR_USER_DOESNOT_EXIST = 99;
    public static final int SUCCESS = 100;
    public static final int ERROR_LOGIN_DENIED = 401;
    public static final int ERROR_LICENSE_REQUIRED = 402;
    public static final int ERROR_NOT_AUTHORIZED = 403;
    public static final int ERROR_NOT_FOUND = 404;
    public static final int ERROR_ROUTE_UNKNOWN = 405;
    public static final int ERROR_CONFLICT = 419;
    public static final int ERROR_MISSING_DATA = 422;
    public static final int ERROR_TOKEN_INVALID = 901;
    public static final int ERROR_TOKEN_EXPIRED = 902;
    public static final int ERROR_INVALID_SIGNATURE = 903;
    /*Hotel status chain event*/
    public static final String FIRST_CHAIN_EVENT = "0";
    public static final String MID_CHAIN_EVENT = "1";
    public static final String LAST_CHAIN_EVENT = "2";
    public static final String SINGLE_CHAIN_EVENT = "4";
    /*Log folder*/
    public static final String LOG_DIR = "/log/";
    public static final int EXP_LOG_DAYS = 1;
    public static final String DEACTIVATE = "1";
    /*Help*/
    public static final String HELP_URL = "http://support.crewlounge.aero/support/solutions/6000008531";
    public static final String USER_GUIDE_VERSION = "UserGuideVersion";
    public static final int DEFAULT_GUIDE_VERSION = 26;
    public static final String PDF_FILE_PATH = "/sdcard/crewconnect_userguide.pdf";
    public static final String PDF_USER_GUIDE = "http://www.crewlounge.aero/docpdf/crewconnect_userguide.pdf";
    public static final String ANDROID_PACKAGE_LINK = "com.android.vending";
    public static final String INTENT_TYPE_PDF = "application/pdf";
    public static final String GOOGLE_MARKET_PDF = "market://details?id=com.adobe.reader";
    public static final String PDF_CHECK_NEW_VER = "http://www.crewlounge.aero/docpdf/crewconnect_userguide.txt";
    /*New taxi icon: duty type is flight but the icon is different*/
    public static final String TAXI = "TX";
    /**
     * Calendar Permission for Android M and above
     */
    public static final int PERS_REQUEST_CODE = 123;
    public static final String[] permsCalendar = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
    public static final String[] permsStorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] permsCall = {Manifest.permission.CALL_PHONE};
    public static final String[] permsGPS = {Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] permsCamara = {Manifest.permission.CAMERA};
    /* NOTAMS URL used at Airfield info page */
    public static final String NOTAMS_URL = "https://pilotweb.nas.faa.gov/PilotWeb/notamRetrievalByICAOAction.do?method=displayByICAOs&reportType=RAW&formatType=DOMESTIC&retrieveLocId=%s&actionType=notamRetrievalByICAOs";
    public static final int EVENT_DESCRIPTON_LEN = 40;
    /* Roster share feature constants============*/
    public static final String ROSTER_SHARE_FILE = "shareFile.txt";
    public static final int TABLET_VISIBLE_USER = 6;
    public static final int PHONE_VISIBLE_USER = 2;
    public static final int USER_FETCH_RANGE = 3;
    public static final int MIN_EVENT_HEIGHT = 80;
    public static final int MIN_EVENT_WIDTH = 200;
    //Webview jobname
    public static final String IAP_RECEIPT_FILE = "iap_receipt.log";
    public static final String RECEIPT_DIR = "/receipt/";
    /*Main Menu*/
    public static final int BUTTON_HTML = 1;
    public static final String SCREEN_TYPE = "screen_type";
    public static final String HTML_POSITION = "html_position";
    public static final String TAG_EXTENDED_PROFILE = "extendedProfile";
    public static final String TAG_DRAWABLE = "drawable";
    public static final String AIR_MALTA = "AMC";
    public static final String AIR_MALTA_CABIN_CREW = "AMC2";
    public static final String KEY_PREF_FLOAT_X = "FLOAT_X_POS";
    public static final String KEY_PREF_FLOAT_Y = "FLOAT_Y_POS";
    public static final int NAUTICAL_MILE = 1852;
    public static final int MILE = 1609;
    public static final int KILOMET = 1000;
    /**
     * Enumeration used at History and History sub page
     */
    public static final String HISTORY_TYPE = "history_sub_type";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String PDF_PATH = "/pdf_tmp";
    public static int TIME_OUT = 30000;
    /*Download Sabre*/
    public static String HREF = "href";
    public static String LINK = "link";
    public static String PLATFORM = "ANDROID";
    public static String STR_DEFAULT_VALUE = "0";

    /*
    LATAM
     */
    public static String LATAM_LOGIN_BASE_URL = "http://portal.lan.com/symphony/aircrew/lg34?p1=";

    /*Preference*/
    public static String EXPIRE = "1";
    public static String VERIFIED = "1";
    public enum printEmailMode {
        PRINT_FRIDGE, PRINT_LIST, EMAIL_FRIDGE, EMAIL_LIST
    }

    /*Find & meet */
    public class LayoverStatus {
        public static final int hotelStatusIndecisive = 1;
        public static final int hotelStatusSporting = 2;
        public static final int hotelStatusActivity = 3;
        public static final int hotelStatusShopping = 4;
        public static final int hotelStatusDrinking = 5;
        public static final int hotelStatusDatingMen = 90;
        public static final int hotelStatusDatingMenHidden = 91;
        public static final int hotelStatusDatingWomen = 92;
        public static final int hotelStatusDatingWomenHidden = 93;
        public static final int hotelStatusOther = 99;
    }

    public class EXPAND_MODE {
        public static final String AUTO = "0";
        public static final String FULL_COLLAPSE = "1";
        public static final String FULL_EXPAND = "2";
    }

    public class HistoryType {
        public static final int DESTINATION = 100;
        public static final int COUNTRIES = 101;
        public static final int CONTINENTS = 102;
        public static final int ANOTHER_TIME_ZONE = 103;
    }
}
