package aero.pilotlog.fragments;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.DateFormat;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;

/**
 * A simple {@link Fragment} subclass.
 */
public class AirfieldForecastFragment extends BaseMCCFragment {
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;
    @Bind(R.id.txtAirfield)
    TextView txtAirfield ;
    @Bind(R.id.txtICAO)
    TextView txtICAO ;
    @Bind(R.id.txtCity)
    TextView txtCity ;
    @Bind(R.id.txtDay1)
    TextView txtDay1;
    @Bind(R.id.txtDay2)
    TextView txtDay2;
    @Bind(R.id.txtDay3)
    TextView txtDay3;
    @Bind(R.id.txtDay4)
    TextView txtDay4;
    @Bind(R.id.txtDay5)
    TextView txtDay5;
    @Bind(R.id.txtForecast1)
    TextView txtForecast1;
    @Bind(R.id.txtForecast2)
    TextView txtForecast2;
    @Bind(R.id.txtForecast3)
    TextView txtForecast3;
    @Bind(R.id.txtForecast4)
    TextView txtForecast4;
    @Bind(R.id.txtForecast5)
    TextView txtForecast5;
    @Bind(R.id.txtWindInfo1)
    TextView txtWindInfo1;
    @Bind(R.id.txtWindInfo2)
    TextView txtWindInfo2;
    @Bind(R.id.txtWindInfo3)
    TextView txtWindInfo3;
    @Bind(R.id.txtWindInfo4)
    TextView txtWindInfo4;
    @Bind(R.id.txtWindInfo5)
    TextView txtWindInfo5;
    @Bind(R.id.txtUnableLoad)
    TextView txtUnableLoad;
    @Bind(R.id.imgFlag)
    ImageView imgFlag;
    @Bind(R.id.imgDay1)
    ImageView imgDay1;
    @Bind(R.id.imgDay2)
    ImageView imgDay2;
    @Bind(R.id.imgDay3)
    ImageView imgDay3;
    @Bind(R.id.imgDay4)
    ImageView imgDay4;
    @Bind(R.id.imgDay5)
    ImageView imgDay5;
    @Bind(R.id.llDay1)
    LinearLayout llDay1;
    @Bind(R.id.llDay2)
    LinearLayout llDay2;
    @Bind(R.id.llDay3)
    LinearLayout llDay3;
    @Bind(R.id.llDay4)
    LinearLayout llDay4;
    @Bind(R.id.llDay5)
    LinearLayout llDay5;
    @Bind(R.id.llForecast)
    LinearLayout llForecast;

    DatabaseManager databaseManager;
    private JsonArray data;
    private String currentDate;
    private boolean isShowProgress;
    private JsonObject jsonForecast;

    public AirfieldForecastFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_forecast;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        Bundle bundle = getArguments();
        databaseManager = DatabaseManager.getInstance(mActivity);
        initView(bundle);
    }
    private void initView(Bundle bundle) {
        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
        //if (!isTablet()) {
            mIbLeft.setVisibility(View.VISIBLE);
        //}
        //Disable header back icon for tablet
       /* if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }*/
        mTvTitle.setText("Forecast");
        byte[] airfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        setupForecast(airfieldCode);
    }

    @Nullable
    @OnClick({R.id.rlBackIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
        }
    }

    private void setupForecast(byte[] airfieldCode) {

        Airfield airfield = databaseManager.getAirfieldByCode(airfieldCode);
        String icao = airfield.getAFICAO();
        String iata = airfield.getAFIATA();
        String city = airfield.getCity();
        String airfieldName = airfield.getAFName();
        MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();

        txtAirfield.setText(airfieldName);
        txtCity.setText(city);
        if (TextUtils.isEmpty(icao)) {
            txtICAO.setText(iata);
        } else if (TextUtils.isEmpty(iata)) {
            txtICAO.setText(icao);
        } else {
            String result = icao + " - " + iata;
            txtICAO.setText(result);
        }

        int resId = getResources().getIdentifier(String.format(Locale.getDefault(), "flag_%03d", airfield.getAFCountry()), "drawable", mActivity.getPackageName());
        if (resId == 0) {
            resId = R.drawable.flag_238;
        }
        imgFlag.setImageDrawable(ContextCompat.getDrawable(mActivity, resId));

        if (!isShowProgress) {
            MccDialog.showProgressDialog(mActivity);
            isShowProgress = true;
        }

        llForecast.setVisibility(View.INVISIBLE);

        mccNetWorkingService.getForecast(ProfileUtils.newInstance().getUserProfileRespond().getJwt(), icao, new MccCallback<JsonElement>(
                        new MccCallback.RequestListener<JsonElement>() {
                            @Override
                            public void onSuccess(JsonElement jsonElement) {

                                jsonForecast = jsonElement.getAsJsonObject();
                                if (jsonForecast != null && jsonForecast.getAsJsonArray("data") != null) {
                                    data = jsonForecast.getAsJsonArray("data");
                                    llForecast.setVisibility(View.VISIBLE);
                                    txtUnableLoad.setVisibility(View.GONE);
                                    Calendar calendar = Calendar.getInstance();
                                    currentDate = DateFormat.DATE_MONTH_FORMAT.format(calendar.getTime());
                                    for (int i = 0; i < 5; i++) {
                                        switch (i) {
                                            case 0:
                                                setData(i, llDay1, txtDay1, txtForecast1, txtWindInfo1, imgDay1);
                                                break;
                                            case 1:
                                                setData(i, llDay2, txtDay2, txtForecast2, txtWindInfo2, imgDay2);
                                                break;
                                            case 2:
                                                setData(i, llDay3, txtDay3, txtForecast3, txtWindInfo3, imgDay3);
                                                break;
                                            case 3:
                                                setData(i, llDay4, txtDay4, txtForecast4, txtWindInfo4, imgDay4);
                                                break;
                                            case 4:
                                                setData(i, llDay5, txtDay5, txtForecast5, txtWindInfo5, imgDay5);
                                                break;
                                        }
                                    }
                                } else {
                                    setUnableLoadForecast();
                                }

                                if (isShowProgress) {
                                    MccDialog.hideProgressDialog();
                                    isShowProgress = false;
                                }
                            }

                            @Override
                            public void onRetrofitFailure(RetrofitError error) {
                                MccDialog.hideProgressDialog();
                                isShowProgress = false;
                                MccDialog.showGeneralError(mActivity);
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                MccDialog.hideProgressDialog();
                                isShowProgress = false;
                                MccDialog.showErrorByErrorCode(mActivity, errorCode);
                            }
                        }) {
                });
    }

    private void setUnableLoadForecast() {
        llForecast.setVisibility(View.INVISIBLE);
        txtUnableLoad.setVisibility(View.VISIBLE);
    }

    private void setData(int i, LinearLayout llDay, TextView txtDay, TextView txtForecast, TextView txtWind, ImageView imgDay) {
        try {

            JsonObject date = data.get(i).getAsJsonObject();
            Log.d("json", data.toString());
            String dayOfWeekString;
            String dateFromJSON = Utils.getStringJSON(date, "date");
            Date dateCompare = DateFormat.DATE_MONTH_FORMAT.parse(dateFromJSON);
            if (DateFormat.DATE_MONTH_FORMAT.parse(currentDate).before(dateCompare)) {
                SimpleDateFormat dateFormat = DateFormat.DAY_OF_WEEK_FORMAT;
                dateFormat.setTimeZone(TimeZone.getDefault());
                dayOfWeekString = DateFormat.DAY_OF_WEEK_FORMAT.format(dateCompare);
                txtDay.setText(dayOfWeekString);
            } else if (!currentDate.equals(dateFromJSON)) {
                llDay.setVisibility(View.GONE);
                return;
            }

            String description = Utils.getStringJSON(date, "description");
            txtForecast.setText(description);

            int imgId = Integer.parseInt(Utils.getStringJSON(date, "image").replace(".png", ""));
            int resIdWeather = getResources().getIdentifier(String.format(Locale.getDefault(), "forecast_%02d", imgId), "drawable", mActivity.getPackageName());
            imgDay.setImageDrawable(ContextCompat.getDrawable(mActivity, resIdWeather));

            String maxTempC = Utils.getStringJSON(date, "maxTemp");
            String minTempC = Utils.getStringJSON(date, "minTemp");
            String maxTempF = String.valueOf(9 * Integer.parseInt(maxTempC) / 5 + 32);
            String minTempF = String.valueOf(9 * Integer.parseInt(minTempC) / 5 + 32);
            String temp = maxTempC + "/" + minTempC + " \u2103" + " - " + maxTempF + "/" + minTempF + " \u2109";
            String windDirection = Utils.getStringJSON(date, "windDirection");
            String windSpeed = Utils.getStringJSON(date, "windSpeed");
            String result = temp + " | " + windSpeed + "km/h from " + windDirection;
            txtWind.setText(result);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
