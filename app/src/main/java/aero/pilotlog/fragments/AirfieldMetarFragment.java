package aero.pilotlog.fragments;
//AirfieldMetarFragment

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.WeatherLocal;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.tasks.DownloadAsync;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class AirfieldMetarFragment extends BaseMCCFragment implements MccEditText.EditTextImeBackListener {

    /*@Bind(R.id.tv_title)
    TextView mTvTitleSecond;*/
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.tv_refresh)
    TextView mTvRefresh;
    @Bind(R.id.edt_airfield_weather)
    MccEditText mEdtAirfieldWeather;
    @Bind(R.id.tv_weather_info)
    TextView mTvWeatherInfo;
    @Bind(R.id.iv_weather_icon)
    ImageView mIvWeatherIcon;

    @Bind(R.id.tv_weather_airfield_name)
    TextView mTvAirfieldName;
    @Bind(R.id.tv_weather_airfield_country)
    TextView mTvAirfieldCountry;
    @Bind(R.id.llLoading)
    LinearLayout mLloading;
    @Nullable
    @Bind(R.id.tv_back)
    TextView mTvBack;
    public static final String METAR_URL = "http://tgftp.nws.noaa.gov/data/observations/metar/stations/%s.TXT";//correct
    public static final String TAF_URL = "http://tgftp.nws.noaa.gov/data/forecasts/taf/stations/%s.TXT";//correct
    public static final String INFOR_FORMAT = "%s" + "<br><br>" + "%s";
    public static final String METAR_FORMAT = "<font color=\"#558EBF\">METAR: <br>" + "%s" + "</font>" + "<br>%s<br>";
    public static final String TAF_FORMAT = "<font color=\"#558EBF\">TAF: <br>" + "%s" + "</font>" + "<br>%s<br>";
    public static final String ICAO_ZZZZ = "ZZZZ";
    private DatabaseManager mDatabaseManager;
    private String metar, taf;

    public static final int ADD_NEW_REPORT = 1;
    public static final int VIEW_EXISTED_REPORT = 2;
    private int mViewType;
    private Airfield mAirfieldCallback = null;
    private WeatherLocal mWeather;
    private Bundle mBundle;
    //private boolean mIsFromAirfieldInfo = false;

    /**
     * return current screen is add new or view info.
     *
     * @return
     */
    public int getWeatherViewType() {
        return mViewType;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        KeyboardUtils.hideKeyboard(mActivity, pContentView);
        initView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_weather;
    }

    @Override
    protected int getHeaderResId() {
        return isTablet() ? R.layout.layout_weather_action_bar : R.layout.layout_weather_action_bar_phone;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    /**
     * Initialize view
     */
    private void initView() {
        mEdtAirfieldWeather.setOnEditTextImeBackListener(this);
        mDatabaseManager = new DatabaseManager(mActivity);

        mBundle = getArguments();
        if (mBundle != null) {
            mViewType = mBundle.getInt(MCCPilotLogConst.WEATHER_VIEW_TYPE, VIEW_EXISTED_REPORT);
            byte[] airfieldCode = mBundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
            if(airfieldCode!=null){
                Airfield mAirfield = mDatabaseManager.getAirfieldByCode(airfieldCode);
                if(mAirfield!=null){
                    calculateWeather(mAirfield);
                }
            }

        }
        if (!isTablet()) {
            mTvSave.setVisibility(View.VISIBLE);
            mTvRefresh.setVisibility(View.GONE);
            if (mViewType == VIEW_EXISTED_REPORT) {
                mTvSave.setVisibility(View.GONE);
                mTvRefresh.setVisibility(View.VISIBLE);
            } else {
                mTvSave.setVisibility(View.VISIBLE);
                mTvRefresh.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onHideKeyboard(MccEditText pEditText) {
        String iCaoIata = mEdtAirfieldWeather.getText().toString().toUpperCase();
        if (!TextUtils.isEmpty(iCaoIata) && !iCaoIata.equalsIgnoreCase(ICAO_ZZZZ)) {
            Airfield airfield = mDatabaseManager.getAirfieldByICAOIATA(iCaoIata);
            if (airfield != null) {
                ZCountry country = mDatabaseManager.getCountryByCode(airfield.getAFCountry());
                if (country!=null) {
                    mEdtAirfieldWeather.setText(airfield.getAFICAO());
                    mTvAirfieldName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                    mTvAirfieldName.setText(airfield.getAFName());
                    mTvAirfieldCountry.setText(country.getCountryName());
                    getWeatherInfo(airfield);
                }
            } else {
                clearValues();
                mEdtAirfieldWeather.setText(iCaoIata);
                mTvAirfieldName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                mTvAirfieldName.setText(R.string.airfield_not_found);
            }
        }
    }

    @OnTextChanged(R.id.edt_airfield_weather)
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        mTvWeatherInfo.setText(STRING_EMPTY);
        mIvWeatherIcon.setImageDrawable(null);
        mTvAirfieldCountry.setText(STRING_EMPTY);
        mTvAirfieldName.setText(STRING_EMPTY);
       // mWeather = null;
    }

    @Nullable
    @OnClick({R.id.iv_search_airfield_weather, R.id.iv_clear, R.id.tv_cancel, R.id.tv_refresh, R.id.tv_save, R.id.tv_back/*, R.id.rlBackIcon*/})
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.tv_cancel:
                KeyboardUtils.hideKeyboard(mActivity);
                finishFragment();
                break;
            case R.id.iv_search_airfield_weather:
                mViewType = ADD_NEW_REPORT;
              /*  if (mIsFromAirfieldInfo && isTablet()) {
                    Fragment leftFragment = getLeftFragment();
                    if (leftFragment != null && leftFragment instanceof AirfieldsListFragment) {
                        ((AirfieldsListFragment) leftFragment).setViewType(MCCPilotLogConst.SELECT_MODE);
                        ((AirfieldsListFragment) leftFragment).SELECT_LIST_TYPE = MCCPilotLogConst.SELECT_LIST_TYPE_WEATHER;
                        Utils.showToast(mActivity, "Pick an airfield on the left to calculate distance!");
                    }
                } else {
                    replaceAirfieldListFragment();
                }*/
                replaceAirfieldListFragment();
                mEdtAirfieldWeather.getText().clear();
                break;
            case R.id.tv_refresh:
                if (NetworkUtils.isHavingNetwork(mActivity)) {
                    String iCaoIata = mEdtAirfieldWeather.getText().toString();
                    if (!TextUtils.isEmpty(iCaoIata) && !iCaoIata.equalsIgnoreCase(ICAO_ZZZZ)) {
                        Airfield airfield = mDatabaseManager.getAirfieldByICAOIATA(iCaoIata);
                        if (airfield != null) {
                            getWeatherInfo(airfield);
                        }
                    }
                } else {
                    Utils.showToast(mActivity, R.string.no_internet_weather);
                }
                break;
            case R.id.tv_save:
                KeyboardUtils.hideKeyboard(mActivity);
                saveWeather();
                break;
            case R.id.iv_clear:
                mEdtAirfieldWeather.setText(STRING_EMPTY);
                break;
            case R.id.tv_back:
                finishFragment();
                break;
            default:
                break;
        }
    }

    private void replaceAirfieldListFragment() {
        AirfieldsListFragment airfieldListFragment = new AirfieldsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
        bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE,MCCPilotLogConst.SELECT_LIST_TYPE_WEATHER);
        if (isTablet()) {
            bundle.putBoolean(MCCPilotLogConst.FROM_WEATHER_LIST, true);
            airfieldListFragment.setArguments(bundle);
            replaceFragment(R.id.rightContainerFragment, airfieldListFragment, FLAG_ADD_STACK);
        } else {
            airfieldListFragment.setArguments(bundle);
            replaceFragment(R.id.fragmentMain, airfieldListFragment, FLAG_ADD_STACK);
        }
    }

    public void setSelectedAirfield(Airfield pAirfield) {
        mAirfieldCallback = pAirfield;
    }


  /*  public void setSelectedAirfield(String pAirfieldCode) {
        AirfieldPilot airfieldPilot = DatabaseManager.getInstance(mActivity).getAirfieldPilotFromCode(pAirfieldCode);
        mAirfieldCallback = new AirfieldPilotCountryModel(airfieldPilot, DatabaseManager.getInstance(mActivity).getCountryFromCountryCode(airfieldPilot.getAFCountry()));
    }*/

    /**
     * Due to some unknown bugs, I'm unable to set text to edit text after callback. So I had to set text in this method.
     * Same for calling onResume()
     *
     * @param savedInstanceState saveInstance
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        calculateWeather(mAirfieldCallback);
//        if (mWeather != null && mAirfieldCallback == null) {
//            mEdtAirfieldWeather.setText(mWeather.getAFIcao());
//        }
        mAirfieldCallback = null;
    }


    private void calculateWeather(Airfield airfield) {
        if (airfield != null) {
            ZCountry country = mDatabaseManager.getCountryByCode(airfield.getAFCountry());
            if (country!=null) {
                mEdtAirfieldWeather.setText(airfield.getAFICAO());
                mTvAirfieldName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                mTvAirfieldName.setText(airfield.getAFName());
                mTvAirfieldCountry.setText(country.getCountryName());
                if (mViewType == VIEW_EXISTED_REPORT) {
                    WeatherLocal mWeather = mDatabaseManager.getWeather(airfield.getAFICAO());
                    if (mWeather != null) {
                        mTvWeatherInfo.setText(Html.fromHtml(mWeather.getReport()));
                        mIvWeatherIcon.setImageResource(getResources().getIdentifier(mWeather.getIcon(), "drawable", mActivity.getPackageName()));
                    }
                } else {
                    getWeatherInfo(airfield);
                }

            }
        }
    }

    @OnFocusChange(R.id.edt_airfield_weather)
    public void onFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            clearValues();
        }
    }

    /**
     * Clear weather info and weather image onTextChangedEvent
     */
    public void clearValues() {
        mEdtAirfieldWeather.setText(STRING_EMPTY);
        mTvWeatherInfo.setText(STRING_EMPTY);
        mIvWeatherIcon.setImageDrawable(null);
        mTvAirfieldCountry.setText(STRING_EMPTY);
        mTvAirfieldName.setText(STRING_EMPTY);
        //mWeather = null;
    }

    /**
     * Get the icon name from METAR string
     *
     * @param pMetar metar
     * @return icon name, starts with wx
     */
    public String getWeatherIcon(String pMetar) {
        String iconString;
        if (!TextUtils.isEmpty(pMetar) && pMetar.length() > 23) {
            iconString = pMetar.substring(22);
        } else {
            return MCCPilotLogConst.STRING_EMPTY;
        }
        String flowCTL;
        //Precipitation
        if (iconString.contains("SN") && !iconString.contains("TSNO") && !iconString.contains("DSNT") && !iconString.contains("VISNO")
                || iconString.contains("SG") ||
                iconString.contains("PE") || iconString.contains("IC")) {
            flowCTL = "SN";
        } else if (iconString.contains("GS") ||
                iconString.contains("TS") && !iconString.contains("TSNO") && !iconString.contains("DSNT") && !iconString.contains("VISNO") ||
                iconString.contains("GR")) {
            flowCTL = "TS";
        } else if (iconString.contains("TCU") || iconString.contains("CB")) {
            flowCTL = "CB";
            //PL-963
        } else if (iconString.contains("DZ") || (iconString.replace("RAILS","").contains("RA"))) {
            //end PL-963
            flowCTL = "RA";
        } else if (iconString.contains("BR") || iconString.contains("FG") ||
                iconString.contains("HZ") || iconString.contains("VV")) {
            flowCTL = "FG";
        } else {
            flowCTL = MCCPilotLogConst.STRING_EMPTY;
        }

        //Clouds
        if (iconString.contains("OVC")) {
            flowCTL = "OVC" + flowCTL;
        } else if (iconString.contains("BKN")) {
            flowCTL = "BKN" + flowCTL;
        } else if (iconString.contains("SCT") || !flowCTL.equalsIgnoreCase("")) {
            flowCTL = "SCT" + flowCTL;
        }

        //Wind
        if ((iconString.contains("WS") || iconString.contains("SQ")
                || iconString.contains(" FC") || iconString.contains("G3")
                || iconString.contains("G4")) && !flowCTL.contains("TS")) {    //TS has priority over wind
            flowCTL = "WIND";
        }

        //Dust
        if (iconString.contains("DU") || iconString.contains("FU") ||
                iconString.contains("SA") || iconString.contains("SS") ||
                iconString.contains("DA") || iconString.contains("VA ")) /*VA with 1 space to avoid CAVOK */ {
            flowCTL = "DUST";
        }

        if (flowCTL.equalsIgnoreCase(MCCPilotLogConst.STRING_EMPTY)) {
            if (pMetar.length() > 48) {
                iconString = pMetar.substring(46);//19
            } else {
                return MCCPilotLogConst.STRING_EMPTY;
            }
            int idx = iconString.indexOf("/") - 3;
            String cutStr = "";
            try {
                cutStr = iconString.substring(idx, idx + 3);
            } catch (StringIndexOutOfBoundsException error) {
                error.printStackTrace();
            }
//            if (cutStr.contains("M"))
//                cutStr.replace("M", "-");
            cutStr = cutStr.trim();
            //copy only digits from cutStr
            int temperature = -1;
            try {
                temperature = Integer.parseInt(cutStr);
            } catch (NumberFormatException ignored) {

            }

            if (temperature != -1) {
                if (temperature < 0) {
                    flowCTL = "cold";
                } else if (temperature > 33) {
                    flowCTL = "hot";
                } else {
                    flowCTL = "cavok";
                }
            }
        }
        return "wx_" + flowCTL.toLowerCase();
    }

    /**
     * Execute task to get weather information
     */
    private void getWeatherInfo(final Airfield airfield) {
        KeyboardUtils.hideKeyboard(mActivity);
        if (NetworkUtils.isHavingNetwork(mActivity)) {
            mLloading.setVisibility(View.VISIBLE);
            mTvWeatherInfo.setText(STRING_EMPTY);
            new DownloadAsync() {
                @Override
                public void onPostDownload(List<String> listString) {
                    String tempMetar = "", tempTaf = "";
                    if (listString.size() >= 1) {
                        metar = listString.get(0);
                        if (!TextUtils.isEmpty(metar)) {
                            int indexBreak = metar.indexOf("\n");
                            if (indexBreak > 0 && metar.length() > indexBreak) {
                                tempMetar = String.format(METAR_FORMAT, metar.substring(0, indexBreak), metar.substring(indexBreak + 1));
                            }
                        }
                    }
                    if (listString.size() >= 2) {
                        taf = listString.get(1);
                        if (!TextUtils.isEmpty(taf)) {
                            int indexBreak = taf.indexOf("\n");
                            if (indexBreak > 0 && taf.length() > indexBreak) {
                                tempTaf = String.format(TAF_FORMAT, taf.substring(0, indexBreak), taf.substring(indexBreak + 1));
                            }
                        }
                    }
                    try {
                        if (!Utils.checkStringNotEmptyNull(metar) && !Utils.checkStringNotEmptyNull(taf)) {
                            //PL-978
                            Utils.showToast(mActivity, getString(R.string.no_wx_report_available));
                            //End PL-978
                            mLloading.setVisibility(View.GONE);
                            return;
                        }
                        String info = String.format(INFOR_FORMAT, Utils.checkStringNotEmptyNull(metar) ? tempMetar : "", Utils.checkStringNotEmptyNull(taf) ? tempTaf : "");
                        mTvWeatherInfo.setText(Html.fromHtml(info));
                        String weatherIcon = getWeatherIcon(metar);
                        mIvWeatherIcon.setImageResource(getResources().getIdentifier(weatherIcon, "drawable", mActivity.getPackageName()));
                        ZCountry country = DatabaseManager.getInstance(mActivity).getCountryByCode(String.valueOf(airfield.getAFCountry()));
                        mWeather = new WeatherLocal(airfield.getAFICAO(), airfield.getAFIATA(), info, weatherIcon, String.valueOf(Calendar.getInstance().getTimeInMillis()),airfield.getAFName(), country.getCountryName());
                        mLloading.setVisibility(View.GONE);
                        checkOutdated(metar, taf);
                        //on phone, after refresh successful then auto save to database
                        if (!isTablet() && mViewType == VIEW_EXISTED_REPORT) {
                            saveWeather();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }.execute(String.format(METAR_URL, airfield.getAFICAO()), String.format(TAF_URL, airfield.getAFICAO()));
            System.gc();
        } else {
            Utils.showToast(mActivity, R.string.no_internet_weather);
        }
    }

    /**
     * Save weather in database and update list
     */
    private boolean saveWeather() {
        boolean b = false;
        if (mWeather != null) {
            b = mDatabaseManager.saveWeather(mWeather);
            if (b) {
                Fragment fragment = isTablet() ? getLeftFragment() : getFragment(WeatherListFragment.class);
                if (fragment instanceof WeatherListFragment) {
                    ((WeatherListFragment) fragment).addNewWeather(mWeather);
                }
                if (!isTablet()) {
                    if (mViewType != VIEW_EXISTED_REPORT) {
                        finishFragment();
                    }
                } else {
                    clearValues();
                }
            } else {
                Utils.showToast(mActivity, "Failed to save weather");
            }
        }

        return b;
    }

    /**
     * Check outdate of metar, taf
     *
     * @param metar METAR
     * @param taf   TAF
     */
    private void checkOutdated(String metar, String taf) {
        if (metar == null || taf == null) {
            return;
        }
        String mDate = MCCPilotLogConst.STRING_EMPTY, tDate = MCCPilotLogConst.STRING_EMPTY;
        if (metar.length() > 10)
            mDate = metar.substring(0, 10);
        if (taf.length() > 10)
            tDate = taf.substring(0, 10);
        Calendar nowCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String nowDate = sdf.format(nowCalendar.getTime());
        if (!nowDate.equalsIgnoreCase(mDate) && mDate.contains("/") || !nowDate.equalsIgnoreCase(tDate) && tDate.contains("/")) {
            final String text = "WX report appears to be outdated";
            MccDialog.getOkAlertDialog(mActivity, R.string.caution, text);
        }
    }

    /**
     * Method to set data for tablet layout
     *
     * @param pIcao icao
     */
/*    public void setWeatherInfoForTablet(String pIcao) {
        AirfieldPilotCountryModel mAirfield = mDatabaseManager.getAirfieldCountryFromIcaoIata(pIcao);
        if (mAirfield != null) {
            mTvAirfieldName.setText(mAirfield.getAirfieldPilot().getAFName());
            mTvAirfieldCountry.setText(mAirfield.getCountryGeo().getCountryName());
            mEdtAirfieldWeather.setText(mAirfield.getAirfieldPilot().getAFIcao());

            *//*Load weather info *//*
            Weather mWeather = mDatabaseManager.getWeather(pIcao);
            if (mWeather != null) {
                mTvWeatherInfo.setText(mWeather.getReport());
                mIvWeatherIcon.setImageResource(getResources().getIdentifier(mWeather.getIcon(), "drawable", mActivity.getPackageName()));
            }
        }
    }*/
}