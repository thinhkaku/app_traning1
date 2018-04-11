package aero.pilotlog.common;

public class CloudApiConstants {
    public static final String API_BASE = "http://api.crewlounge.center";
    public static final String API_BASE1 = "http://www.mcccloud.net";
    public static final String API_PARSE_HTML = "/parser.php";
    public static final String API_GENERAL = "/dispatcher.php";
    public static final String API_GET_CURRENCY = "/classes/currency.txt";
    public static final String API_FORECAST = "/places/forecast/{airfieldIcao}";


    //API call types
    public static final String TYPE_POST_REGISTER = "register";
    public static final String TYPE_POST_GET_EXISTING_USER = "getExistingUser";
    public static final String TYPE_POST_UPDATE_USER = "updateUser";
    public static final String TYPE_RESET_PASSWORD = "resetPassword";
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_UPDATE_LICENSE = "updateLicense";
    public static final String TYPE_LICENSE_HASH = "licenseHash";
    public static final String TYPE_EXPIRE = "expired";

    public static final String TYPE_UPLOAD_PROFILE_PICTURE = "setProfilePicture";
    public static final String TYPE_GET_PROFILE_PICTURE = "getProfilePicture";
    public static final String TYPE_UPDATE_USER_INFO = "update";
    public static final String TYPE_UPLOAD_SETTING_INFO_USER = "updateUser";
    public static final String TYPE_DELETE_PROFILE_PICTURE = "deleteProfilePicture";
    public static final String TYPE_GET_ACTIVITIES = "getActivities";
    public static final String TYPE_DOWNLOAD_REVIEWS = "downloadReviews";
    public static final String TYPE_UPLOAD_REVIEWS = "uploadReview";
    public static final String TYPE_UPLOAD_ACTIVITY = "uploadActivity";
    public static final String TYPE_UPLOAD_ROSTER_CODE = "addEventCode";
    //Use for find and meet
    public static final String TYPE_LAYOVER_CREW = "layoverCrew";
    public static final String TYPE_LAYOVER_STATUS = "layoverStatus";

    //call parameters
    public static final String PARA_FIRSTNAME = "firstName";
    public static final String PARA_LASTNAME = "lastName";
    public static final String PARA_PROFILE_EMAIL = "profile_email";
    public static final String PARA_COMPANY = "company";
    public static final String PARA_DEVICE_ID = "deviceID";
    public static final String PARA_USE_MESSAGING = "useMessaging";
    public static final String PARA_PLATFORM = "platform";
    public static final String PARA_EXPIRE_DATE = "expireDate";
    public static final String PARA_PROFILE_PHONE = "profile_phone";
    public static final String PARA_PROFILE_FACEBOOK = "facebook";
    public static final String PARA_PROFILE_LINKIN = "linkedin";
    public static final String PARA_PROFILE_DOB = "profile_DOB";
    public static final String PARA_PROFILE_GENDER = "profile_gender";
    public static final String PARA_PROFILE_ABOUT = "profile_about";
    public static final String PARA_PROFILE_PICTURE = "PROFILE_PICTURE";
    public static final String PARA_REGISTRATION_TYPE = "registrationType";
    public static final String PARA_USER_ID = "userID";
    public static final String PARA_LIKED_FB = "likedFB";
    public static final String PARA_EMPLOYEE_ID = "employeeID";
    public static final String PARA_API_KEY = "apiKey";
    public static final String PARA_TIME_STAMP = "timestamp";
    public static final String PARA_MESSAGE = "message";
    public static final String PARE_REGISTER = "register";
    public static final String PARA_EMAIL = "email";
    public static final String PARA_APP_VERSION = "appVersion";
    public static final String PARA_PASSWORD = "password";
    public static final String PARA_ROSTER = "roster";
    public static final String PARA_BASE_URL = "baseURL";
    public static final String PARA_COMPANY_CODE = "companyCode";
    public static final String PARA_ROSTER_TIME_ZONE = "rosterTimezone";
    public static final String PARA_REPLACE_INFO = "replaceInfo";
    public static final String PARA_DUTY_BEFORE = "dutyBefore";
    public static final String PARA_DUTY_AFTER = "dutyAfter";

    public static final String PARA_UPDATE_CREW = "update_crew";

    public static final String PARA_VERIFIED = "verified";
    public static final String PARA_EMPLOYEE_RANK = "employeeRank";
    public static final String PARA_ABOUT = "profile_about";


    public static final String PARA_LIKES = "likes";
    public static final String PARA_GENDER = "profile_gender";
    public static final String PARA_DOB = "profile_DOB";
    public static final String PARA_PHONE = "profile_phone";
    public static final String PARA_COUNTRY = "country";
    public static final String PARA_CITY = "city";
    public static final String PARA_PRIVACY_PROFILE = "privacy_profile";
    public static final String PARA_PRIVACY_LINKEDIN = "privacy_linkedin";
    public static final String PARA_PRIVACY_FACEBOOK = "privacy_facebook";
    public static final String PARA_PRIVACY_DOB = "privacy_DOB";
    public static final String PARA_PRIVACY_COUNTRY = "privacy_country";
    public static final String PARA_PRIVACY_PHONE = "privacy_phone";
    public static final String PARA_PRIVACY_EMAIL = "privacy_email";
    public static final String PARA_PRIVACY_PICTURE = "privacy_picture";
    public static final String PARA_PRIVACY_SHARE_AGE = "shareAge";

    public static final String PROPERTY_ERROR = "error";

    /*Roster share API*/
    public static final String TYPE_SHARE_SYNC = "shareSync";
    public static final String TYPE_SHARE_REQUEST = "shareRequest";
    public static final String TYPE_SHARE_APPROVE = "shareApprove";
    public static final String TYPE_SHARE_DELETE = "shareDelete";


    public static final String API_REGISTER_USER = "/public/register";
    public static final String API_LOGIN_USER = "/public/connect";
    public static final String API_FORGOT_PASS = "/public/forgot/{email}";

    public static final String API_GET_TABLE_BY_TIMESTAMP = "/pilotlog/user/*";
    public static final String API_GET_PLACES_BY_TIMESTAMP = "/places";
    public static final String API_DELETE = "/pilotlog/user/{table}/{guid}";
    public static final String API_UPLOAD_TABLE = "/pilotlog/user/{table}/{guid}";

    public static final String MODULE = "TYH654";
    public static final int PLATFORM = 2;

    public static final String JSON_ELEMENT_NEXT = "next";
    public static final String JSON_ELEMENT_META = "meta";
    public static final String JSON_ELEMENT_TABLE = "table";
    public static final String JSON_ELEMENT_DATA = "data";
    public static final String JSON_ELEMENT_GUID = "guid";
    public static final String JSON_ELEMENT_AIRFIELD = "airfield";
    public static final String JSON_ELEMENT_AIRCRAFT = "aircraft";
    public static final String JSON_ELEMENT_SETTING_CONFIG = "settingconfig";
    public static final String JSON_ELEMENT_PILOT = "pilot";
    public static final String JSON_ELEMENT_EXPENSE = "expense";
    public static final String JSON_ELEMENT_ALLOWANCE_RULES = "allowanceRules";
    public static final String JSON_ELEMENT_VALIDATION_RULES = "validationRules";
    public static final String JSON_ELEMENT_LIMIT_RULES = "limitrules";
    public static final String JSON_ELEMENT_MY_QUERY_BUILD = "myquerybuild";
    public static final String JSON_ELEMENT_MY_QUERY = "myquery";
    public static final String JSON_ELEMENT_WEATHER_AF = "weatheraf";
    public static final String JSON_ELEMENT_WEATHER = "weather";
    public static final String JSON_ELEMENT_BACKUP_DB = "backupdb";
    public static final String JSON_ELEMENT_FLIGHT = "flight";
    public static final String JSON_ELEMENT_DUTY = "duty";
    public static final String JSON_ELEMENT_IMAGE_PIC = "imagepic";
    public static final String JSON_ELEMENT_RECORD_DELETE = "recorddelete";




}
