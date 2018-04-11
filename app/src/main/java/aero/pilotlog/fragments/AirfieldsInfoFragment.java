package aero.pilotlog.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.GoogleMapActivity;
import aero.pilotlog.api.APIManager;
import aero.pilotlog.api.MccNetWorkingService;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.common.DateFormat;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.common.SunCalc;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.MccCallback;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.LocationUtils;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.utilities.ProfileUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.CircleImageView;
import aero.pilotlog.widgets.ItemInputText;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.MccDialog;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;

/**
 * A simple {@link Fragment} subclass.
 */
public class AirfieldsInfoFragment extends BaseMCCFragment {
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;
    @Bind(R.id.lnEdit)
    LinearLayout lnEdit;
    @Bind(R.id.btnRight)
    Button btnRight;
    @Bind(R.id.tv_af_name)
    TextView tvAFName;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_elevation)
    TextView tvElevation;
    @Bind(R.id.tv_af_icao)
    TextView tvIcao;

    @Bind(R.id.iv_flag)
    ImageView ivFlag;
    @Bind(R.id.tv_country)
    TextView tvCountry;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.llWeather)
    LinearLayout llWeatherShow;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.tvUnableWeather)
    TextView tvUnableLoadWeather;

    @Bind(R.id.item_airfield_latitude)
    ItemInputText itemAirfieldLatitude;
    @Bind(R.id.item_airfield_longitude)
    ItemInputText itemAirfieldLongitude;
    @Bind(R.id.item_airfield_local_time)
    ItemInputText itemAirfieldLocalTime;
    @Bind(R.id.item_airfield_timezone)
    ItemInputText itemAirfieldTimezone;
    @Bind(R.id.item_airfield_sunrise)
    ItemInputText itemAirfieldSunrise;
    @Bind(R.id.item_airfield_sunset)
    ItemInputText itemAirfieldSunset;

    @Bind(R.id.notes_mcc)
    TextView tvNotesByMCC;
    @Bind(R.id.notes_user)
    TextView tvNotesByUser;
    @Bind(R.id.temp)
    TextView tvTemp;
    @Bind(R.id.imgWeather)
    TextView tvImageWeather;
    @Bind(R.id.txtWeather)
    TextView tvWeather;

    @Bind(R.id.lnHeader)
    LinearLayout lnHeader;
    @Bind(R.id.iv_image_menu)
    CircleImageView ivImageMaps;
    @Bind(R.id.sv_airfield_info)
    ScrollView svAirfieldInfo;
    @Bind(R.id.edt_item_airfield_history)
    ItemInputTextWithIcon itemAirfieldHistory;
    @Bind(R.id.ln_history)
    LinearLayout lnHistory;
   /* @Bind(R.id.map)
    View fragmentMap;*/

    DatabaseManager mDatabaseManager;
    private byte[] mAirfieldCode;
    List<String> categories;
    java.util.TimeZone timeZone;
    Airfield airfield = null;
    private static final int ANTARTICA_CODE = 8;
    private String MAPS_URL = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=10&size=%sx%s&maptype=roadmap&markers=color:red|label:A|%s,%s";

    public static final String LOCAL_TIME_FORMAT = " %s";
    private Bundle mBundle;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfields_info;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mBundle = getArguments();
        initView();
        initDataAirfield();
    }

    private void initView() {
        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
        if (!isTablet()) {
            mTvTitle.setText("Back");
            mIbLeft.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setText("");
            //Disable header back icon for tablet
            mHeaderRlBack.setClickable(false);
        }
        btnRight.setVisibility(View.VISIBLE);
        lnEdit.setVisibility(View.VISIBLE);
    }

    private void setIconFavorite(boolean isShowList) {
        btnRight.setBackgroundResource(0);
        if (isShowList) {
            btnRight.setBackgroundResource(R.drawable.fav_icon_border);
        } else {
            btnRight.setBackgroundResource(R.drawable.fav_0_icon_white);
        }
    }

    public void initDataAirfield() {

        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        if (mBundle != null) {
            mAirfieldCode = mBundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        }
        if (mAirfieldCode != null) {
            airfield = mDatabaseManager.getAirfieldByCode(mAirfieldCode);
        }
        if (airfield != null) {
            setIconFavorite(airfield.getShowList());
            tvAFName.setText(airfield.getAFName().toUpperCase());
            categories = initCategory();
            tvCategory.setText(categories.get(airfield.getAFCat()));
            if (airfield.getElevationFT() != -99 && airfield.getElevationFT() != 0) {
                tvElevation.setText(String.format("Elevation %d feet", airfield.getElevationFT()));
            }
            if (!TextUtils.isEmpty(airfield.getAFIATA())) {
                tvIcao.setText(String.format("%s / %s", airfield.getAFICAO(), airfield.getAFIATA()));
            } else {
                tvIcao.setText(airfield.getAFICAO());
            }

            ZCountry country = mDatabaseManager.getCountryByCode(airfield.getAFCountry());
            if (country != null) {
                int resId = mActivity.getResources().getIdentifier(String.format("flag_%03d", country.getCountryCode()), "drawable", mActivity.getPackageName());
                ivFlag.setBackgroundResource(resId);
                tvCountry.setText(country.getCountryName());
            }

            tvCity.setText(airfield.getCity());
            ZTimeZone zTimeZone = mDatabaseManager.getTimeZoneByCode(airfield.getTZCode());
            if (zTimeZone != null) {
                timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
                Calendar c = Calendar.getInstance(timeZone);
                String offset;
                double offsetValue = (double) Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, airfield.getTZCode()) / 60;

                if (offsetValue < 0) {
                    offset = "" + offsetValue;
                } else {
                    offset = "+" + offsetValue;
                }
                if (offset.endsWith(".0")) offset = offset.replace(".0", "");
                String localTime = String.format(LOCAL_TIME_FORMAT, String.format(Locale.getDefault(), "%02d", c.get(Calendar.HOUR_OF_DAY))
                        + ":" + String.format(Locale.getDefault(), "%02d", c.get(Calendar.MINUTE)));
                itemAirfieldTimezone.setDescription("UTC " + offset);
                itemAirfieldLocalTime.setText(localTime.trim());
            }

            itemAirfieldLatitude.setDescription(airfield.getLatitude() != -200000 ? LocationUtils.getLatitudeString(airfield.getLatitude().toString()) : "");
            itemAirfieldLongitude.setDescription(airfield.getLongitude() != -200000 ? LocationUtils.getLongitudeString(airfield.getLongitude().toString()) : "");
            if (!TextUtils.isEmpty(airfield.getNotes()))
                tvNotesByMCC.setText(airfield.getNotes());
            if (!TextUtils.isEmpty(airfield.getNotesUser()))
                tvNotesByUser.setText(airfield.getNotesUser());
            displaySunriseSunset(airfield);
            setUpWeather(airfield);
            if (NetworkUtils.isHavingNetwork(mActivity)) {
                new DownloadImageTask(ivImageMaps)
                        .execute(String.format(MAPS_URL, LocationUtils.getLatitudeFloat(airfield.getLatitude()),
                                LocationUtils.getLongitudeFloat(airfield.getLongitude()),
                                (int) getResources().getDimension(R.dimen.profile_image_size),
                                (int) getResources().getDimension(R.dimen.profile_image_size),
                                LocationUtils.getLatitudeFloat(airfield.getLatitude()),
                                LocationUtils.getLongitudeFloat(airfield.getLongitude())));
                ivImageMaps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        Float lat = LocationUtils.getLatitudeFloat(airfield.getLatitude());
                        Float lng = LocationUtils.getLongitudeFloat(airfield.getLongitude());
                        intent = new Intent(mActivity, GoogleMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(GoogleMapActivity.KEY_LAT, String.valueOf(lat));
                        bundle.putString(GoogleMapActivity.KEY_LNG, String.valueOf(lng));
                        bundle.putString(GoogleMapActivity.KEY_ADDRESS, airfield.getAFName());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            } else {
                ivImageMaps.setVisibility(View.VISIBLE);
            }
            lnHeader.post(new Runnable() {
                public void run() {
                    int headerHeight = lnHeader.getHeight();
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    int imageHeight = ivImageMaps.getHeight();
                    params.setMargins(0, headerHeight - imageHeight / 2 - (imageHeight % 2) / 2,
                            (int) getResources().getDimension(R.dimen.margin_right_profile_image), 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    ivImageMaps.setLayoutParams(params);
                    params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, headerHeight, 0, 0);
                    svAirfieldInfo.setLayoutParams(params);
                }
            });
        }

        List<FlightModel> flightModels = mDatabaseManager.getLogbookListByAircraftPilotAirfield(MccEnum.dateFilter.LAST_90_DAYS, null, mAirfieldCode, null);
        itemAirfieldHistory.setVisibilityLineBottom(View.GONE);
        if (flightModels != null && flightModels.size() > 0) {
            String strCurrentDate = flightModels.get(0).getFlightDateUTC();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String flightDate = DateTimeUtils.formatDateToString(newDate);
            itemAirfieldHistory.setDescription(String.format("%s", flightDate));
            Pilot pilot1 = null, pilot2 = null, pilot3 = null, pilot4 = null;
            if (flightModels.get(0).getmP1() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP1()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot1 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP1());
            if (flightModels.get(0).getmP2() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP2()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot2 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP2());
            if (flightModels.get(0).getmP3() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP3()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot3 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP3());
            if (flightModels.get(0).getmP4() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP4()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot4 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP4());

            if (pilot1 != null || pilot2 != null || pilot3 != null || pilot4 != null) {
                String withPilot = String.format("with %s%s%s%s",
                        pilot1 != null ? pilot1.getPilotName() + ", " : "",
                        pilot2 != null ? pilot2.getPilotName() + ", " : "",
                        pilot3 != null ? pilot3.getPilotName() + ", " : "",
                        pilot4 != null ? pilot4.getPilotName() + ", " : "");
                withPilot = withPilot.substring(0, withPilot.length() - 2);
                String onAircraft = String.format("\non %s", flightModels.get(0).getAircraftName());
                itemAirfieldHistory.setFootNote(withPilot + onAircraft);
            }

        } else {
            lnHistory.setVisibility(View.GONE);
            itemAirfieldHistory.setDescription("none");
        }
    }

    private List<String> initCategory() {
        List<String> categories = new ArrayList<>();
        categories.add("");
        categories.add("Drone Site");
        categories.add("Air Force Base, Navy Coast Guard");
        categories.add("Ballooning site, Glider - STOL - ULM strip");
        categories.add("Water Landing Strip , Hidroport");
        categories.add("Heliport, Medical Center");
        categories.add("Small Landing Strip");
        categories.add("Small Airfield");
        categories.add("Medium Aerodrome");
        categories.add("International Airport");
        categories.add("Train Station");
        categories.add("Metropolitan Area");
        return categories;
    }

    private void setUpWeather(Airfield airfield) {
        if (!NetworkUtils.isHavingNetwork(mActivity)) {
            //llWeatherShow.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            tvUnableLoadWeather.setVisibility(View.VISIBLE);
        } else {
            //llWeatherShow.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);
            //tvUnableLoadWeather.setVisibility(View.VISIBLE);
            MccNetWorkingService mccNetWorkingService = APIManager.getNetworkingServiceInstance();

            mccNetWorkingService.getForecast(ProfileUtils.newInstance().getUserProfileRespond().getJwt(),
                    airfield.getAFICAO(),new MccCallback<JsonElement>(
                    new MccCallback.RequestListener<JsonElement>() {
                        @Override
                        public void onSuccess(JsonElement response) {
                            setData(response);
                            llWeatherShow.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            tvUnableLoadWeather.setVisibility(View.GONE);
                        }

                        @Override
                        public void onRetrofitFailure(RetrofitError error) {
                            progressBar.setVisibility(View.GONE);
                            tvUnableLoadWeather.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(int errorCode) {
                            progressBar.setVisibility(View.GONE);
                            tvUnableLoadWeather.setVisibility(View.VISIBLE);
                        }
                    }) {
            });

        }
    }

    private void setData(JsonElement jsonElement) {
        if (timeZone != null) {
            JsonObject jsonForecast = jsonElement.getAsJsonObject();
            Calendar calendar = Calendar.getInstance(timeZone);
            String currentDate = DateFormat.DATE_MONTH_FORMAT.format(calendar.getTime());
            if (jsonForecast != null && jsonForecast.getAsJsonArray("data") != null) {
                JsonArray data = jsonForecast.getAsJsonArray("data");
                for (int i = 0; i < 5; i++) {
                    JsonObject date = data.get(i).getAsJsonObject();
                    if (currentDate.equals(Utils.getStringJSON(date, "date"))) {
                        String maxTempC = Utils.getStringJSON(date, "maxTemp");
                        String minTempC = Utils.getStringJSON(date, "minTemp");
                        String maxTempF = String.valueOf(9 * Integer.parseInt(maxTempC) / 5 + 32);
                        String minTempF = String.valueOf(9 * Integer.parseInt(minTempC) / 5 + 32);
                        String temp = maxTempC + " / " + minTempC + " \u2103" + "\n" + maxTempF + " / " + minTempF + " \u2109";
                        String description = Utils.getStringJSON(date, "description");
                        int imgId = Integer.parseInt(Utils.getStringJSON(date, "image").replace(".png", ""));
                        int resId = getResources().getIdentifier(String.format(Locale.getDefault(), "forecast_%02d", imgId), "drawable", mActivity.getPackageName());
                        tvTemp.setText(temp);
                        tvWeather.setText(description);
                        tvImageWeather.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, resId), null, null);
                        break;
                    } else {
                        setUnableLoadWeather();
                    }
                }
            } else {
                setUnableLoadWeather();
            }
        } else {
            setUnableLoadWeather();
        }

    }

    private void setUnableLoadWeather() {
        tvTemp.setText(R.string.unable_to_load_weather_label);
        tvWeather.setVisibility(View.GONE);
        tvImageWeather.setVisibility(View.INVISIBLE);
    }

    @Nullable
    @OnClick({R.id.rlBackIcon, R.id.btnRight, R.id.ll_metar_taf, R.id.item_airfield_metar_taf, R.id.ln_country,
            R.id.ll_airfield_more_weather, R.id.item_airfield_more_weather,
            R.id.ln_wiki, R.id.item_airfield_wiki, R.id.ln_sky_vector, R.id.item_airfield_sky_vector,
            R.id.item_airfield_trip_advisor, R.id.ln_trip_advisor,
            R.id.item_airfield_notams, R.id.ln_notams,
            R.id.item_airfield_forecast, R.id.ln_forecast,
            R.id.ln_lat_long, R.id.item_airfield_latitude, R.id.item_airfield_longitude,
            R.id.ln_globe, R.id.item_airfield_sunset, R.id.item_airfield_sunrise,
            R.id.ln_time_zone, R.id.item_airfield_local_time, R.id.item_airfield_timezone,
            R.id.ln_notes_user, R.id.notes_user, R.id.tv_action_bar_right, R.id.ln_history})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ln_history:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, LogbooksListFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.tv_action_bar_right:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldAddsFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.rlBackIcon:
                finishFragment();
                break;
            case R.id.btnRight:
                //finishFragment();
                airfield.setShowList(!airfield.getShowList());
                setIconFavorite(airfield.getShowList());
                mDatabaseManager.insertOrUpdateAirfield(airfield);
                break;
            case R.id.ln_country:
                if (airfield != null) {
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, CountryInfoFragment.class, bundle, FLAG_ADD_STACK);
                }
                break;
            case R.id.ln_notes_user:
            case R.id.notes_user:
                displayTextBox(tvNotesByUser);
                break;
            case R.id.ln_time_zone:
            case R.id.item_airfield_local_time:
            case R.id.item_airfield_timezone:
                if (airfield != null) {
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, TimeZoneInfoFragment.class, bundle, FLAG_ADD_STACK);
                }
                break;
            case R.id.ln_globe:
            case R.id.item_airfield_sunset:
            case R.id.item_airfield_sunrise:
                if (airfield != null && airfield.getAFCountry() != ANTARTICA_CODE && (airfield.getLongitude() != 0 || airfield.getLatitude() != 0)) {
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldGlobeFragment.class, bundle, FLAG_ADD_STACK);
                } else {
                    MccDialog.getOkAlertDialog(mActivity, getString(R.string.text_airfield_globe), getString(R.string.airfield_globe_msg_not)).show();
                }
                break;
            case R.id.ln_lat_long:
            case R.id.item_airfield_latitude:
            case R.id.item_airfield_longitude:
                if (airfield != null) {
                    if (airfield.getLongitude() == -200000 && airfield.getLongitude() == -200000) {
                        MccDialog.getOkAlertDialog(mActivity, getString(R.string.text_airfield_google), getString(R.string.airfield_google_coordinates_miss)).show();
                        return;
                    }
                    if (!NetworkUtils.isHavingNetwork(mActivity)) {
                        MccDialog.getOkAlertDialog(mActivity, getString(R.string.text_airfield_google), getString(R.string.no_internet_connection)).show();
                        return;
                    }
                   /* if (isTablet()) {
                        b.putInt(MCCPilotLogConst.GOOGLE_EARTH_FULLSCREEN, MCCPilotLogConst.GOOGLE_EARTH_FORMAT);
                        b.putString(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfield.getAFCode());
                        Intent intent = new Intent(mActivity, GoogleEarthActivity.class);
                        intent.putExtra(MCCPilotLogConst.GOOGLE_EARTH_BUNDLE, b);
                        startActivity(intent);
                        break;
                    } else {*/
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldGoogleEarthFragment.class, bundle, FLAG_ADD_STACK);
                    //}
                }
                break;
            case R.id.ll_metar_taf:
            case R.id.item_airfield_metar_taf:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                bundle.putInt(MCCPilotLogConst.WEATHER_VIEW_TYPE, AirfieldMetarFragment.ADD_NEW_REPORT);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldMetarFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_airfield_forecast:
            case R.id.ln_forecast:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldForecastFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_airfield_notams:
            case R.id.ln_notams:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldNotamsFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_airfield_trip_advisor:
            case R.id.ln_trip_advisor:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldTripAdvisorFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.ln_sky_vector:
            case R.id.item_airfield_sky_vector:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldSkyVectorFragment.class, bundle, FLAG_ADD_STACK);
                break;

            case R.id.ln_wiki:
            case R.id.item_airfield_wiki:
                bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mAirfieldCode);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldWikiFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.ll_airfield_more_weather:
            case R.id.item_airfield_more_weather:
                if (!NetworkUtils.isHavingNetwork(mActivity)) {
                    Toast.makeText(mActivity,
                            getString(R.string.dialog_no_internet_content),
                            android.widget.Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (airfield != null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(ApiConstant.WEATHER_CHANNEL_URL +
                                        LocationUtils.getLatitudeFloat(airfield.getLatitude()) + "," +
                                        LocationUtils.getLongitudeFloat(airfield.getLongitude())));
                        startActivity(browserIntent);
                    }
                }
                break;

            default:
                break;
        }
    }

    private void displaySunriseSunset(Airfield pAirfield) {
        SimpleDateFormat sdfD0 = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        SunCalc sc = new SunCalc();
        sc.PosDate = Calendar.getInstance();
        sc.PosLatitude = pAirfield.getLatitude();
        sc.PosLongitude = pAirfield.getLongitude();
        sc.CalculateSun(mActivity);

        String sunTxt = "";
        Calendar cl = Calendar.getInstance();

        if (sc.SR >= 0 && sc.SR <= 1440)
            sunTxt = SunCalc.ShowHHMM(sc.SR) + " UTC";
        else {
            if (sc.SR < 0) {
                cl.roll(Calendar.DAY_OF_MONTH, false);
                cl.roll(Calendar.DAY_OF_MONTH, false);


                sunTxt = sdfD0.format(cl.getTime()) + " " + SunCalc.ShowHHMM(1440 + sc.SR);

            } else if (sc.SR > 1440 && sc.SR <= 2880) {
                cl.add(Calendar.DAY_OF_MONTH, 1);
                sunTxt = sdfD0.format(cl.getTime()) + " " + SunCalc.ShowHHMM(sc.SR);

            }
        }
        itemAirfieldSunrise.setDescription(sunTxt);
        Calendar cl1 = Calendar.getInstance();

        if (sc.SS >= 0 && sc.SS <= 1440)
            sunTxt = SunCalc.ShowHHMM(sc.SS) + " UTC";
        else {
            if (sc.SS < 0) {
                cl1.roll(Calendar.DAY_OF_MONTH, false);
                cl1.roll(Calendar.DAY_OF_MONTH, false);
                sunTxt = SunCalc.ShowHHMM(1440 + sc.SS) + " UTC" + "\n" + sdfD0.format(cl1.getTime());
            } else if (sc.SS > 1440 && sc.SS <= 2880) {
                cl1.add(Calendar.DAY_OF_MONTH, 1);
                sunTxt = SunCalc.ShowHHMM(sc.SS - 1440) + " UTC" + "\n" + sdfD0.format(cl1.getTime());
            }
        }

        itemAirfieldSunset.setDescription(sunTxt);
        if (sc.PolarDayNight == 1) {
            itemAirfieldSunrise.setDescription("Polar Day");
            itemAirfieldSunset.setDescription("Sun up all day or twilight");
        } else if (sc.PolarDayNight == 2) {
            itemAirfieldSunrise.setDescription("Polar Night");
            itemAirfieldSunset.setDescription("Sun down all day");
        }
    }

    private void displayTextBox(final TextView currentTextView) {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        String titleDialog;
        titleDialog = "EDIT NOTES";
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        inputText.setGravity(Gravity.TOP);
        inputText.setHeight(300);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(254);
        inputText.setFilters(fArray);
        if (!TextUtils.isEmpty(currentTextView.getText().toString())) {
            inputText.setText(currentTextView.getText().toString());
            inputText.setSelection(currentTextView.getText().length() - 1);
        }
        inputText.selectAll();
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(titleDialog);
        new android.support.v7.app.AlertDialog.Builder(mActivity, R.style.MessageDialogTheme)
                //.setTitle(titleDialog)
                .setView(inputTextDialog)
                .setNegativeButton(R.string.sign_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.clearFocus();
                        if(KeyboardUtils.isKeyboardShow(mActivity)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //KeyboardUtils.hideSoftKeyboard(mActivity);
                        inputText.clearFocus();
                        if(KeyboardUtils.isKeyboardShow(mActivity)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        currentTextView.setText(inputText.getText().toString());
                        if (airfield != null) {
                            airfield.setNotesUser(inputText.getText().toString());
                            mDatabaseManager.insertOrUpdateAirfield(airfield);
                        }
                        dialog.dismiss();
                    }
                }).show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            ivImageMaps.setVisibility(View.VISIBLE);
        }
    }
}
