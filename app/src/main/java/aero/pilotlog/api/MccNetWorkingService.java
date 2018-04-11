package aero.pilotlog.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.interfaces.MccCallback;

import aero.pilotlog.models.Meta;
import aero.pilotlog.models.SuccessRespond;
import aero.pilotlog.models.UserProfileRespond;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by hung.tm on 10/24/2014.
 * Mcc Networking Service
 */
public interface MccNetWorkingService {

    /**
     * Get company from cloud, if passing development parameter
     * => both true/false will get development released airlines
     *
     * @param type             String
     * @param update_companies String
     * @param userID           String
     * @param appVersion       String
     * @param platform         String
     * @param password         String
     * @param versionDate      String
     * @param callback         Callback
     */
    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getCompaniesFromCloud(
            @Field("type") String type,
            @Field("update_companies") String update_companies,
            @Field("userID") String userID,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("password") String password,
            @Field("versionDate") String versionDate,
            @Field("development") String booleanVal,
            Callback<JsonElement> callback
    );

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getCompaniesFromCloudSync(
            @Field("type") String type,
            @Field("update_companies") String update_companies,
            @Field("userID") String userID,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("password") String password,
            @Field("versionDate") String versionDate,
            @Field("development") String booleanVal
    );

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void resetPassword(
            @Field("type") String type,
            @Field("email") String email,
            Callback<JsonElement> callback
    );

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void updatePassword(
            @Field("type") String type,
            @Field("password") String password,
            @Field("userID") String userID,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("newPassword") String newPassword,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void checkUserExist(
            @Field("type") String type,
            @Field("email") String email,
            @Field("password") String password,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void updateUser(
            @Field("type") String type,
            @Field("email") String email,
            @Field("deviceID") String deviceId,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void uploadPofilePicture(
            @Field("type") String type,
            @Field("email") String email,
            @Field("deviceID") String deviceId,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("data") String data,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getProfilePicture(
            @Field("type") String type,
            @Field("email") String email,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("size") String size,
            @Field("users") String users,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getProfilePictureSync(
            @Field("type") String type,
            @Field("email") String email,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("size") String size,
            @Field("users") String users);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void update(
            @Field("type") String type,
            @Field("email") String email,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("update_companies") String updateCompanies,
            @Field("update_airports") String updateAirports,
            @Field("update_messages") String updateMessages,
            @Field("update_crew") String updateCrew,
            @Field("update_currency") String updateCurrency,
            @Field("update_events") String updateEvents,
            @Field("updateSubscription") String updateSubscription,
            @Field("updateFor") String updateFor,
            @Field("company") String company,
            @Field("deviceID") String deviceID,
            @Field("events") String events,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_PARSE_HTML)
    void parse(
            @Field("userID") String userID,
            @Field("apiKey") String apiKey,
            @Field("email") String email,
            @Field("roster") String roster,
            @Field("baseURL") String baseURL,
            @Field("companyCode") String companyCode,
            @Field("company") String company,
            @Field("rosterTimezone") String rosterTimezone,
            @Field("replaceInfo") String replaceInfo,
            @Field("appVersion") String appVersion,
            @Field("deviceID") String deviceID,
            @Field("platform") String platform,
            @Field("password") String password,
            @Field("dutyBefore") String dutyBefore,
            @Field("dutyAfter ") String dutyAfter,
            @Field("useMessaging") String useMessaging,
            @Field("homebase") String homebase,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_PARSE_HTML)
    void parsePDF(
            @Field("userID") String userID,
            @Field("apiKey") String apiKey,
            @Field("email") String email,
            @Field("pdfRoster") String roster,
            @Field("baseURL") String baseURL,
            @Field("companyCode") String companyCode,
            @Field("company") String company,
            @Field("rosterTimezone") String rosterTimezone,
            @Field("replaceInfo") String replaceInfo,
            @Field("appVersion") String appVersion,
            @Field("deviceID") String deviceID,
            @Field("platform") String platform,
            @Field("password") String password,
            @Field("dutyBefore") String dutyBefore,
            @Field("dutyAfter ") String dutyAfter,
            @Field("useMessaging") String useMessaging,
            @Field("homebase") String homebase,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void setUserInfo(
            @Field("type") String type,
            @Field("email") String email,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("employeeID") String employeeID,
            @Field("company") String company,
            @Field("employeeRank") String employeeRank,
            @Field("newPassword") String newPassword,
            @Field("profile_email") String profileEmail,
            @Field("profile_phone") String profilePhone,
            @Field("linkedin") String linkedin,
            @Field("facebook") String facebook,
            @Field("city") String city,
            @Field("country") String country,
            @Field("gender") String gender,
            @Field("likes") String likes,
            @Field("about") String about,
            @Field("privacy_picture") String privacyPicture,
            @Field("privacy_email") String privacyEmail,
            @Field("privacy_phone") String privacyPhone,
            @Field("privacy_country") String privacyCountry,
            @Field("privacy_DOB") String privacyDOB,
            @Field("privacy_facebook") String privacyFacebook,
            @Field("privacy_linkedin") String privacyLinkedin,
            @Field("privacy_profile") String privacyProfile,
            @Field("DOBDay") String dOBDay,
            @Field("DOBMonth") String dOBMonth,
            @Field("DOBYear") String dOBYear,
            @Field("shareAge") String shareAge,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void deleteProfilePicture(
            @Field("type") String type,
            @Field("email") String email,
            @Field("userID") String userID,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void updateLicense(
            @Field("type") String type,
            @Field("period") String period,
            @Field("password") String password,
            @Field("checksum") String checkSum,
            @Field("userID") String userId,
            @Field("appVersion") String appversion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void updateLicenseMcc(
            @Field("type") String type,
            @Field("period") String period,
            @Field("password") String password,
            @Field("checksum") String checkSum,
            @Field("userID") String userId,
            @Field("appVersion") String appversion,
            @Field("platform") String platform,
            @Field("mccROSTER") String mcc,
            Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getCrewList(
            @Field("type") String type,
            @Field("email") String email,
            @Field("update_crew") String updateCrew,
            @Field("company") String company,
            @Field("page") String page,
            @Field("verified") String verify,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getCrewListSync(
            @Field("type") String type,
            @Field("email") String email,
            @Field("update_crew") String updateCrew,
            @Field("company") String company,
            @Field("page") String page,
            @Field("verified") String verify,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getExtendedProfile(
            @Field("type") String type,
            @Field("extendedProfileID") String profileId,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getExtendedProfileSync(
            @Field("type") String type,
            @Field("extendedProfileID") String profileId,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getActivity(
            @Field("type") String type,
            @Field("downloadArray") String downloadArray,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getActivitySync(
            @Field("type") String type,
            @Field("downloadArray") String downloadArray,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void getLayoverCrew(
            @Field("type") String type,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("homebase") String homebase,
            @Field("enrollID") String endrollId,
            MccCallback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    JsonElement getLayoverCrewSync(
            @Field("type") String type,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            @Field("homebase") String homebase,
            @Field("enrollID") String endrollId);

    @GET(CloudApiConstants.API_GET_CURRENCY)
    void getCurrency(Callback<JsonElement> callback);

    @GET(CloudApiConstants.API_GET_CURRENCY)
    JsonElement getCurrencySync();

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void downloadReview(
            @Field("type") String type,
            @Field("activityID") String activityID,
            @Field("syncDate") String syncDate,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void uploadReview(
            @Field("type") String type,
            @Field("activityID") String activityID,
            @Field("rating") String rating,
            @Field("review") String review,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void uploadActivity(
            @Field("type") String type,
            @Field("title") String title,
            @Field("airport") String airport,
            @Field("activityType") String activityType,
            @Field("activitySubtype") String activitySubtype,
            @Field("location") String location,
            @Field("city") String city,
            @Field("address") String address,
            @Field("website") String website,
            @Field("phonenumber") String phone,
            @Field("email") String email,
            @Field("review") String review,
            @Field("rating") String rating,
            @Field("addedBy") String addedBy,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void layoverStatus(
            @Field("type") String type,
            @Field("layoverStatus") String activityID,
            @Field("eventID") String rating,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void uploadRosterCode(
            @Field("type") String type,
            @Field("eventCode") String eventCode,
            @Field("eventDescription") String eventDescription,
            @Field("eventType") String eventType,
            @Field("companyID") String companyID,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    /**
     * Send share request to other user
     *
     * @param type       'shareRequest'
     * @param friendID   (optional) userID of the person this request is send to
     * @param friendText (optional) Email or employeeID of the user this request is send to
     * @param userId     userID
     * @param password   password
     * @param appVersion app version
     * @param platform   platform
     * @param callback   callback
     */
    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void shareRequest(
            @Field("type") String type,
            @Field("friendID") String friendID,
            @Field("friendText") String friendText,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    /**
     * Approve a request that has need_approval status. Users will be sharing their roster from now on.
     *
     * @param type       'shareApprove'
     * @param friendID   UserID of the user who has send this request
     * @param userId     userID
     * @param password   password
     * @param appVersion app version
     * @param platform   platform
     * @param callback   callback
     */
    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void shareApprove(
            @Field("type") String type,
            @Field("friendID") String friendID,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    /**
     * Delete a roster share request.
     *
     * @param type       'shareDelete'
     * @param friendID   userID of the share you would like to delete
     * @param userId     userID
     * @param password   password
     * @param appVersion app version
     * @param platform   platform
     * @param callback   callback
     */
    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void shareDelete(
            @Field("type") String type,
            @Field("friendID") String friendID,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);

    /**
     * Download all share requests and the available events for requests that have been approved
     *
     * @param type       'shareSync'
     * @param userId     userID
     * @param password   password
     * @param appVersion app version
     * @param platform   platform
     * @param callback   callback
     */
    @FormUrlEncoded
    @POST(CloudApiConstants.API_GENERAL)
    void shareSync(
            @Field("type") String type,
            @Field("userID") String userId,
            @Field("password") String password,
            @Field("appVersion") String appVersion,
            @Field("platform") String platform,
            MccCallback<JsonElement> callback);


    //register account
    @FormUrlEncoded
    @POST(CloudApiConstants.API_REGISTER_USER)
    void register(
            @Field("accountEmail") String accountEmail,
            @Field("password") String password,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            MccCallback<SuccessRespond> callback);
    //login user
    @FormUrlEncoded
    @POST(CloudApiConstants.API_LOGIN_USER)
    void checkUserLogin(
            @Field("username") String accountEmail,
            @Field("password") String password,
            @Field("platform") int platform,
            @Field("moduleId") String moduleId,
            @Field("deviceId") String deviceId,
            MccCallback<UserProfileRespond> callback);



    @GET(CloudApiConstants.API_FORGOT_PASS)
    void forgotPass(@Path("email") String email, MccCallback<SuccessRespond> callback);

    @GET(CloudApiConstants.API_GET_TABLE_BY_TIMESTAMP)
    void syncDownloadTables(@Query("from") long timeStamp,
                           @Query("limit") int limit,
                           @Query("order") String order,
                           @Query("page") int page,
                           @Query("token") String token, MccCallback<JsonObject> callback);

    @GET(CloudApiConstants.API_GET_PLACES_BY_TIMESTAMP)
    void syncDownloadPlaces(@Query("from") long timeStamp,
                            @Query("limit") int limit,
                            @Query("token") String token, MccCallback<JsonObject> callback);
    @DELETE(CloudApiConstants.API_DELETE)
    void syncDeleteRecords(@Path("table") String table,
                @Path("guid") String guid,
                     @Query("token") String token,
                MccCallback<JsonElement> callback);
    //update or create
    @Headers({"Accept: application/json"})
    @POST(CloudApiConstants.API_UPLOAD_TABLE)
    void syncUploadTables (
            @Path("table") String table,
            @Path("guid") String guid,
            @Body JsonObject body,
            @Query("token") String token,
            MccCallback<JsonElement> callback);




    @GET(CloudApiConstants.API_FORECAST)
    void getForecast(
            @Header("x-api-token") String token,
            @Path("airfieldIcao") String icao,
            MccCallback<JsonElement> callback);

    @GET("/pilotlog/asp/pilotlog.stats.asp")
    void sendData(
            @Query("ED") String eD,
            @Query("OS") String oS,
            @Query("BD") String bD,
            @Query("ID") String iD,
            @Query("CT") String cT,
            @Query("AI") String aI,
            @Query("DT") String dT,
            @Query("CS") String cS,
            Callback<String> callback);
}
