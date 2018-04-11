package aero.pilotlog.fragments;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.AllowanceRules;
import aero.pilotlog.databases.entities.BackupDB;
import aero.pilotlog.databases.entities.Duty;
import aero.pilotlog.databases.entities.Expense;
import aero.pilotlog.databases.entities.Flight;
import aero.pilotlog.databases.entities.ImagePic;
import aero.pilotlog.databases.entities.LimitRules;
import aero.pilotlog.databases.entities.MyQuery;
import aero.pilotlog.databases.entities.MyQueryBuild;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.RecordDelete;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ValidationRules;
import aero.pilotlog.databases.entities.Weather;
import aero.pilotlog.databases.entities.WeatherAF;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.models.TableUpload;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.Utils;
import retrofit.RetrofitError;

import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_AIRCRAFT;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_AIRFIELD;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_ALLOWANCE_RULES;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_BACKUP_DB;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_DUTY;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_EXPENSE;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_FLIGHT;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_IMAGE_PIC;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_LIMIT_RULES;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_MY_QUERY;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_MY_QUERY_BUILD;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_PILOT;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_RECORD_DELETE;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_SETTING_CONFIG;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_VALIDATION_RULES;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_WEATHER;
import static aero.pilotlog.common.CloudApiConstants.JSON_ELEMENT_WEATHER_AF;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncFragment extends Fragment implements View.OnClickListener {
    private ProfileUtils profileUtils;
    private MccNetWorkingService mccNetWorkingService;
    private DatabaseManager databaseManager;
    private TextView tvProgress;
    private TextView tvPercentProgress;
    private TextView tvSyncLog;
    private ProgressBar progressBar;
    private Button buttonSync;
    private ScrollView scrollViewLog;
    private LinearLayout llSync;
    private ExpandableLayout expandableLayout;
    private boolean isContinueToSync = true;
    private List<Airfield> airfields = new ArrayList<>();
    private List<Airfield> places = new ArrayList<>();
    private List<Aircraft> aircrafts = new ArrayList<>();
    private List<SettingConfig> settingConfigs = new ArrayList<>();
    private List<Pilot> pilots = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();
    private List<AllowanceRules> allowanceRulesList = new ArrayList<>();
    private List<ValidationRules> validationRulesList = new ArrayList<>();
    private List<LimitRules> limitRulesList = new ArrayList<>();
    private List<MyQueryBuild> myQueryBuilds = new ArrayList<>();
    private List<MyQuery> myQueries = new ArrayList<>();
    private List<WeatherAF> weatherAFS = new ArrayList<>();
    private List<Weather> weathers = new ArrayList<>();
    private List<BackupDB> backupDBS = new ArrayList<>();
    private List<Flight> flights = new ArrayList<>();
    private List<Duty> duties = new ArrayList<>();
    private List<ImagePic> imagePics = new ArrayList<>();
    private List<RecordDelete> recordDeletes = new ArrayList<>();

    public SyncFragment() {
        // Required empty public constructor
    }

    private SyncV5 syncV5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sync, container, false);
        init();
        buttonSync = (Button) rootView.findViewById(R.id.bntSync);
        buttonSync.setOnClickListener(this);
        tvProgress = (TextView) rootView.findViewById(R.id.tvProgress);
        tvPercentProgress = (TextView) rootView.findViewById(R.id.tvPercentProgress);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        scrollViewLog = (ScrollView) rootView.findViewById(R.id.scrollView_log);
        tvSyncLog = (TextView) rootView.findViewById(R.id.tvSyncLog);
        llSync = (LinearLayout) rootView.findViewById(R.id.ll_sync);
        expandableLayout = (ExpandableLayout) rootView.findViewById(R.id.expandable_layout);


       /* scrollViewLog.post(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollViewLog.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });*/
        return rootView;
    }

    private void init() {
        databaseManager = DatabaseManager.getInstance(getActivity());
        profileUtils = ProfileUtils.newInstance();
        mccNetWorkingService = APIManager.getNetworkingServiceInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bntSync:
                expandableLayout.expand(true);
                syncV5 = new SyncV5();
                syncV5.execute();
                break;
        }
    }

    private void sync() {
        syncDownload(DateTimeUtils.getCurrentUTCUnixTimeStamp() - 20415310, "_modified", pageDownload);
        //syncUpload();
    }

    private String log = "";

    private void sendLogResult(final boolean isSuccess) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess) {
                            log += " <font color='#00A000'>" + "\t\t\t\t\t SUCCESS</font> ";
                        } else {
                            log += " <font color='#DC0000'>" + "\t\t\t\t\t FAILED</font> ";
                        }

                        tvSyncLog.setText(Html.fromHtml(log));
                    }
                });
            }
        });
    }

    private void sendLog(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log += "<br>" + message;
                tvSyncLog.setText(Html.fromHtml(log));
                scrollViewLog.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private int totalRecordsDownload;

    private void sendMessageStatus(final String message, final int percent, final int numberDownloaded) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message2 = message.replace("<br>", "");
                if (message.equalsIgnoreCase("starting")) {
                    buttonSync.setText("cancel");
                }
                if (message2.equalsIgnoreCase("sync completed.")) {
                    tvProgress.setText(message2);
                    buttonSync.setText("sync now");
                } else {
                    tvProgress.setText(message2 + " " + numberDownloaded + "/" + totalRecordsDownload);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(percent, true);
                } else {
                    progressBar.setProgress(percent);
                }
                tvPercentProgress.setText(String.valueOf(percent) + "%");
                //log += "<br>" + message;
                //tvSyncLog.setText(Html.fromHtml(log));
                //scrollViewLog.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


   /* private void sendMessageStatus(final String message, final int percent) {
       *//* AsyncTask.execute(new Runnable() {
            @Override
            public void run() {*//*
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message2 = message.replace("<br>","");
                        if(message.equalsIgnoreCase("starting")){
                            buttonSync.setText("cancel");
                        }
                        if(message2.equalsIgnoreCase("sync completed.")){
                            tvProgress.setText(message2);
                            buttonSync.setText("sync now");
                        }else {
                            tvProgress.setText(message2 + "...");
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress(percent, true);
                        } else {
                            progressBar.setProgress(percent);
                        }
                        tvPercentProgress.setText(String.valueOf(percent) + "%");
                        log += "<br>" + message;
                        tvSyncLog.setText(Html.fromHtml(log));
                        scrollViewLog.fullScroll(View.FOCUS_DOWN);
                    }
                });
          *//*  }
        });*//*
    }
*/

  /*  private void downloadPlaces() {
        //call download
        mccNetWorkingService.syncDownloadPlaces(DateTimeUtils.getCurrentUTCUnixTimeStamp() - 20415310, 100,
                profileUtils.getUserProfileRespond().getJwt(),
                new MccCallback<>(
                        new MccCallback.RequestListener<JsonObject>() {
                            @Override
                            public void onSuccess(JsonObject response) {
                                sendMessageResult(true);
                                try {
                                    sendMessageStatus("<br>Update downloaded data", 10);
                                    Gson gson = new Gson();
                                    JSONObject jsonObject = new JSONObject(gson.toJson(response));
                                    workingDownloadPlaces(200, jsonObject);
                                } catch (JSONException e) {
                                    isContinueToSync = false;
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                isContinueToSync = false;
                                sendMessageResult(false);
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                isContinueToSync = false;
                                sendMessageResult(false);
                                workingDownloadPlaces(errorCode, null);
                            }
                        }));
        //default sync logic

        //updaterAF logic

        //discard logic

        //numeric id logic
    }
*/

  private void updateRecordsDownloadedToDB(){
      while (flights.size() + airfields.size()< 2407){
          Log.d("size", String.valueOf(flights.size()));
          try {
              Thread.sleep(100);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      boolean updateData = databaseManager.insertOrUpdateDataForSync(airfields, aircrafts, pilots,
              expenses, allowanceRulesList, validationRulesList, limitRulesList, myQueryBuilds,
              myQueries, weatherAFS, weathers, backupDBS, flights, duties, imagePics);
      Log.d("Update data", String.valueOf(updateData));
     /* AsyncTask.execute(new Runnable() {
          @Override
          public void run() {
              Log.d("size0", String.valueOf(flights.size()));
              while (flights.size() + airfields.size()< totalRecordsDownload){
                  Log.d("size", String.valueOf(flights.size()));
                  try {
                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
              boolean updateData = databaseManager.insertOrUpdateDataForSync(airfields, aircrafts, pilots,
                      expenses, allowanceRulesList, validationRulesList, limitRulesList, myQueryBuilds,
                      myQueries, weatherAFS, weathers, backupDBS, flights, duties, imagePics);
              Log.d("update data", String.valueOf(updateData));
          }
      });*/

  }

    private void syncDownload(final long datetimeStamp, String order, int page) {
        //sendMessageStatus("<br>Download user data", 10);
        //download places

        //download airfield
        mccNetWorkingService.syncDownloadTables(datetimeStamp, 100, order, page,
                profileUtils.getUserProfileRespond().getJwt(),
                new MccCallback<>(
                        new MccCallback.RequestListener<JsonObject>() {
                            @Override
                            public void onSuccess(JsonObject response) {
                                //sendLogResult(true);
                                try {
                                    // sendMessageStatus("<br>Update downloaded data", 10);
                                    Gson gson = new Gson();
                                    final JSONObject jsonObject = new JSONObject(gson.toJson(response));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                          /*  AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("indexing", String.valueOf(pageDownload));
                                                    Log.d("size", String.valueOf(flights.size()));
                                                    workingDownloadTables(200, jsonObject, datetimeStamp);
                                                }
                                            });*/
                                           // Log.d("indexing", String.valueOf(pageDownload));
                                           // Log.d("size", String.valueOf(flights.size()));
                                            workingDownloadTables(200, jsonObject, datetimeStamp);
                                        }
                                    }).start();

                                    totalRecordsDownload = jsonObject.getInt("total");
                                    if ((pageDownload * 100) < totalRecordsDownload)
                                        sendMessageStatus("Download records", 20, pageDownload  * 100);
                                    else
                                        sendMessageStatus("Download records", 20, (pageDownload * 100) - ((pageDownload * 100) - totalRecordsDownload));
                                    try {
                                        String next = jsonObject.getString(CloudApiConstants.JSON_ELEMENT_NEXT);
                                        pageDownload++;
                                        if (isContinueToSync) {
                                          /*  AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    syncDownload(datetimeStamp, "_modified", pageDownload);
                                                }
                                            });*/
                                            syncDownload(datetimeStamp, "_modified", pageDownload);

                                        } else {
                                            //break sync
                                        }
                                    } catch (JSONException e) {
                                        sendLog("index data");
                                        updateRecordsDownloadedToDB();
                                    /*    if (isContinueToSync) {
                                            boolean updateData = databaseManager.insertOrUpdateDataForSync(airfields, aircrafts, pilots,
                                                    expenses, allowanceRulesList, validationRulesList, limitRulesList, myQueryBuilds,
                                                    myQueries, weatherAFS, weathers, backupDBS, flights, duties, imagePics);
                                            Log.d("update data", String.valueOf(updateData));

                                        } else {
                                            //break sync;
                                        }
                                        if (isContinueToSync) {
                                            sendLog("<br>Upload user data");
                                            syncUpload();
                                        }
                                        //syncDelete();

                                        else {
                                            sendLogResult(false);
                                        }*/
                                        //don't have next value -> continue to sync

                                    }



                                } catch (JSONException e) {
                                    isContinueToSync = false;
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                isContinueToSync = false;
                                sendLogResult(false);
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                isContinueToSync = false;
                                sendLogResult(false);
                                workingDownloadTables(errorCode, null, datetimeStamp);
                            }
                        }));

    }

    int pageDownload = 1;

    private void workingDownloadTables(int errorCode, JSONObject response, long datetimeStamp) {
        if (response != null) {
            try {
                //totalRecordsDownload = response.getInt("total");
                JSONArray dataJsonArray = response.getJSONArray(CloudApiConstants.JSON_ELEMENT_DATA);
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                    String table = dataJsonObject.getString(CloudApiConstants.JSON_ELEMENT_TABLE);
                    Gson gson = new Gson();
                    JSONObject metaJsonObject = dataJsonObject.getJSONObject(CloudApiConstants.JSON_ELEMENT_META);
                    metaJsonObject = fixGUIDProblem(metaJsonObject);
                    switch (table.toLowerCase()) {
                        case JSON_ELEMENT_AIRFIELD:
                            sendLog("Download Airfield " + metaJsonObject.getString("AFIATA"));
                            Airfield airfield = gson.fromJson(metaJsonObject.toString(), Airfield.class);
                            airfields.add(airfield);
                            sendLogResult(true);
                            break;
                        case JSON_ELEMENT_AIRCRAFT:
                            //sendLog("Download Aircraft" + metaJsonObject.getString("Reference"));
                            //Aircraft aircraft = gson.fromJson(metaJsonObject.toString(), Aircraft.class);
                            //aircrafts.add(aircraft);
                            break;
                        case JSON_ELEMENT_SETTING_CONFIG:
                            /*sendMessageStatus("Download Setting Config");
                            metaJsonObject.put("AircraftCode", Utils.getJsonStringFromByteArray(guidByteArray));
                            metaJsonObject = fixGUIDProblem(guidByteArray, metaJsonObject);
                            Aircraft aircraft = gson.fromJson(metaJsonObject.toString(), Aircraft.class);
                            aircrafts.add(aircraft);*/
                            break;
                        case JSON_ELEMENT_PILOT:
                            sendLog("Download Pilot" + metaJsonObject.getString("pilotname"));
                            Pilot pilot = gson.fromJson(metaJsonObject.toString(), Pilot.class);
                            pilots.add(pilot);
                            break;
                        case JSON_ELEMENT_EXPENSE:
                            sendLog("Download Expense");
                            Expense expense = gson.fromJson(metaJsonObject.toString(), Expense.class);
                            expenses.add(expense);
                            break;
                        case JSON_ELEMENT_ALLOWANCE_RULES:
                            sendLog("Download Allowance Rule");
                            AllowanceRules allowanceRule = gson.fromJson(metaJsonObject.toString(), AllowanceRules.class);
                            allowanceRulesList.add(allowanceRule);
                            break;
                        case JSON_ELEMENT_VALIDATION_RULES:
                            sendLog("Download Pilot");
                            ValidationRules validationRule = gson.fromJson(metaJsonObject.toString(), ValidationRules.class);
                            validationRulesList.add(validationRule);
                            break;
                        case JSON_ELEMENT_LIMIT_RULES:
                            sendLog("Download Limit Rule");
                            LimitRules limitRule = gson.fromJson(metaJsonObject.toString(), LimitRules.class);
                            limitRulesList.add(limitRule);
                            break;
                        case JSON_ELEMENT_MY_QUERY_BUILD:
                            sendLog("Download My Query Build");
                            MyQueryBuild myQueryBuild = gson.fromJson(metaJsonObject.toString(), MyQueryBuild.class);
                            myQueryBuilds.add(myQueryBuild);
                            break;
                        case JSON_ELEMENT_MY_QUERY:
                            sendLog("Download My Query");
                            MyQuery myQuery = gson.fromJson(metaJsonObject.toString(), MyQuery.class);
                            myQueries.add(myQuery);
                            break;
                        case JSON_ELEMENT_WEATHER_AF:
                            sendLog("Download Weather AF");
                            WeatherAF weatherAF = gson.fromJson(metaJsonObject.toString(), WeatherAF.class);
                            weatherAFS.add(weatherAF);
                            break;
                        case JSON_ELEMENT_WEATHER:
                            sendLog("Download Weather");
                            Weather weather = gson.fromJson(metaJsonObject.toString(), Weather.class);
                            weathers.add(weather);
                            break;
                        case JSON_ELEMENT_BACKUP_DB:
                            sendLog("Download Backup DB");
                            BackupDB backupDB = gson.fromJson(metaJsonObject.toString(), BackupDB.class);
                            backupDBS.add(backupDB);
                            break;
                        case JSON_ELEMENT_FLIGHT:
                            //sendLog("Download Flight" + metaJsonObject.getString("FlightNumber"));
                            //sendMessageStatus("Download flight", 20, (pageDownload-1)* 100 + i);
                            Flight flight = gson.fromJson(metaJsonObject.toString(), Flight.class);
                            flights.add(flight);
                            //sendLogResult(true);
                            break;
                        case JSON_ELEMENT_DUTY:
                            sendLog("Download Duty");
                            Duty duty = gson.fromJson(metaJsonObject.toString(), Duty.class);
                            duties.add(duty);
                            break;
                        case JSON_ELEMENT_IMAGE_PIC:
                            sendLog("Download Image Pic");
                            ImagePic imagePic = gson.fromJson(metaJsonObject.toString(), ImagePic.class);
                            imagePics.add(imagePic);
                            break;
                        case JSON_ELEMENT_RECORD_DELETE:
                            sendLog("Download Record Delete");
                            RecordDelete recordDelete = gson.fromJson(metaJsonObject.toString(), RecordDelete.class);
                            recordDeletes.add(recordDelete);
                            break;
                    }
                }
            } catch (JSONException e) {
                isContinueToSync = false;
                sendLogResult(false);
                e.printStackTrace();
            }
            //continue download next page if any
            //as discussion on Hipchat if there are more page then we have "next" value
            /*try {
                String next = response.getString(CloudApiConstants.JSON_ELEMENT_NEXT);
                pageDownload++;
                if (isContinueToSync) {
                    syncDownload(datetimeStamp, "_modified", pageDownload);

                *//*    SyncV5 sync = new SyncV5();
                    sync.execute();*//*
                } else {
                    //break sync
                }
            } catch (JSONException e) {
                if (isContinueToSync) {
                    boolean updateData = databaseManager.insertOrUpdateDataForSync(airfields, aircrafts, pilots,
                            expenses, allowanceRulesList, validationRulesList, limitRulesList, myQueryBuilds,
                            myQueries, weatherAFS, weathers, backupDBS, flights, duties, imagePics);
                    Log.d("update data", String.valueOf(updateData));

                } else {
                    //break sync;
                }
                if (isContinueToSync) {
                    sendLog("<br>Upload user data");
                    syncUpload();
                }
                //syncDelete();

                else {
                    sendLogResult(false);
                }
                //don't have next value -> continue to sync

            }*/


        } else {
            switch (errorCode) {
                case 403:
                    break;
            }
            //break sync
        }
    }

    private JSONObject fixGUIDProblem(JSONObject metaJsonObject) throws JSONException {
        String guidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

        Gson gson = new Gson();
        String stringMeta = gson.toJson(metaJsonObject);

        Iterator<String> iter = metaJsonObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = metaJsonObject.get(key);
                String stringValue = String.valueOf(value);
                if (stringValue.matches(guidRegex)) {
                    metaJsonObject.put(key, Utils.getJsonStringFromByteArray(Utils.getByteArrayFromGUID(stringValue)));
                    stringMeta = gson.toJson(metaJsonObject);
                    //String target = "\"" + Utils.getJsonStringFromByteArray(Utils.getByteArrayFromGUID(stringValue)) + "\"";
                    stringMeta = stringMeta.replace("\"" + Utils.getJsonStringFromByteArray(Utils.getByteArrayFromGUID(stringValue)) + "\"",
                            Utils.getJsonStringFromByteArray(Utils.getByteArrayFromGUID(stringValue)));
                    metaJsonObject = gson.fromJson(stringMeta, JSONObject.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Something went wrong!
            }
        }

        /*stringMeta = stringMeta.replace("\"" + guid + "\"", guid);*/
        stringMeta = stringMeta.replace("\"PF\":1".trim(), "\"PF\":true").replace("\"PF\":0".trim(), "\"PF\":false");
        stringMeta = stringMeta.replace("\"DeIce\":1".trim(), "\"DeIce\":true").replace("\"DeIce\":0".trim(), "\"DeIce\":false");
        stringMeta = stringMeta.replace("\"ToEdit\":1".trim(), "\"ToEdit\":true").replace("\"ToEdit\":0".trim(), "\"ToEdit\":false");
        stringMeta = stringMeta.replace("\"NextPage\":1".trim(), "\"NextPage\":true").replace("\"NextPage\":0".trim(), "\"NextPage\":false");
        stringMeta = stringMeta.replace("\"Record_Upload\":1".trim(), "\"Record_Upload\":true").replace("\"Record_Upload\":0".trim(), "\"Record_Upload\":false");
        stringMeta = stringMeta.replace("\"UserBool\":1".trim(), "\"UserBool\":true").replace("\"UserBool\":0".trim(), "\"UserBool\":false");
        stringMeta = stringMeta.replace("\"NextSummary\":1".trim(), "\"NextSummary\":true").replace("\"NextSummary\":0".trim(), "\"NextSummary\":false");
        stringMeta = stringMeta.replace("\"Sea\":1".trim(), "\"Sea\":true").replace("\"Sea\":0".trim(), "\"Sea\":false");
        JSONObject jsonObject = new JSONObject(stringMeta);
        return jsonObject.getJSONObject("nameValuePairs");

    }


    private void syncDelete() {
        // sendMessageStatus("<br>Upload user data", 50);
        final List<RecordDelete> recordDeleteList = databaseManager.getRecordDelete();
        if (recordDeleteList.size() > 0) {
            //  sendMessageStatus("<br>Upload record deletes", 50);
            for (int i = 0; i < recordDeleteList.size(); i++) {
                final int index = i;
                mccNetWorkingService.syncDeleteRecords(recordDeleteList.get(i).getTableName(),
                        Utils.getGUIDFromByteArray(recordDeleteList.get(i).getRecordCode()),
                        profileUtils.getUserProfileRespond().getJwt(),
                        new MccCallback<>(
                                new MccCallback.RequestListener<JsonElement>() {
                                    @Override
                                    public void onSuccess(JsonElement response) {
                                        Log.d("delete Response", response.toString());

                                        //if success
                                        databaseManager.removeDeleteRecord(recordDeleteList.get(index).getRecordCode());
                                    }

                                    @Override
                                    public void onRetrofitFailure(RetrofitError error) {
                                    }

                                    @Override
                                    public void onFailure(int errorCode) {
                                    }
                                }));
            }
        }


    }

    int indexUpload = 0;

    private void syncUpload() {
        //upload airfields

        final List<Airfield> airfields = databaseManager.getAllAirfieldToSync();
        if (airfields != null && airfields.size() > 0) {
            //for (int i = 0; i < airfields.size(); i++) {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(new TableUpload<>(airfields.get(indexUpload)));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.get("meta").getAsJsonObject()
                    .addProperty("AFCode", Utils.getGUIDFromByteArray(airfields.get(indexUpload).getAFCode()));
            //sendMessageStatus("Upload Airfield " + airfields.get(indexUpload).getAFICAO(), 30);
            final int count = indexUpload;
            mccNetWorkingService.syncUploadTables("airfield", Utils.getGUIDFromByteArray(airfields.get(indexUpload).getAFCode()),
                    jsonObject,
                    profileUtils.getUserProfileRespond().getJwt(),
                    new MccCallback<>(
                            new MccCallback.RequestListener<JsonElement>() {
                                @Override
                                public void onSuccess(JsonElement response) {
                                    Log.d("airfieldsUp Response", response.toString());
                                    sendLogResult(true);
                                    if (count == airfields.size() - 1) {
                                        //   sendMessageStatus("<br>Sync completed.", 100);

                                    } else {
                                        indexUpload++;
                                        syncUpload();
                                    }
                                }

                                @Override
                                public void onRetrofitFailure(RetrofitError error) {
                                }

                                @Override
                                public void onFailure(int errorCode) {
                                }
                            }));
            //}


        }


        //upload ...
    }

    class SyncV5 extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(1, true);
            } else {
                progressBar.setProgress(1);
            }
            //scrollViewLog.setVisibility(View.VISIBLE);
            //expand(llSync);

            //expandableLayout.expand(true);

            sendLog("Starting");

            //this method will be running on UI thread
           /* pdLoading.setMessage("\tLoading...");
            pdLoading.show();*/
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sync();
            return isContinueToSync;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            /*if(result){
                sendMessageStatus("Success!",100);
            }else {
                sendMessageStatus("Failed!",100);
            }*/

            //this method will be running on UI thread

            //pdLoading.dismiss();
        }
    }
}
