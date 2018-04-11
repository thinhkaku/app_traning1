package aero.pilotlog.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.WeatherLocalAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.SettingsConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.WeatherLocal;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISwipeLayoutCallback;
import aero.pilotlog.tasks.DownloadAsync;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.ProgressDialogUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.PopupSelectionMenu;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by tuan.pv on 2015/08/07.
 * Weather list
 */
public class WeatherListFragment extends BaseMCCFragment implements ISwipeLayoutCallback, IAsyncTaskCallback {

    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.indexableListView)
    IndexableListView mIndexableListView;
    @Bind(R.id.rlBottomBar)
    LinearLayout mLlBottomBar;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.btnSort)
    Button mBtnSort;

    private DatabaseManager mDatabaseManager;
    private List<WeatherLocal> mWeathers = new ArrayList<>();
    private WeatherLocalAdapter mAirfieldAdapter;
    private Activity mActivity;
    private String mDisplayType = SettingsConst.SETTING_IATA;
    private PopupSelectionMenu mPopupSelectionMenu;
    private String mCurrentSelectWeather;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_index_listview;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar_weather_list;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);

        mActivity = getActivity();
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    /**
     * Init view
     */
    public void initView() {
        mTvTitle.setText(R.string.text_weather_reports);
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        //get data from database
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
      /*  Setting setting = mDatabaseManager.getSetting(SettingsConst.AIRFIELD_IDENTIFIER);
        if (setting != null) {
            mDisplayType = setting.getData();
        }*/
        mWeathers = mDatabaseManager.getAllWeather();
      /*  if (mWeathers != null && !mWeathers.isEmpty()) {
            sortWeather();
        }*/

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        //fill data on views
        setTextSizeWeather();
        mAirfieldAdapter = new WeatherLocalAdapter(mActivity, mWeathers);
        mIndexableListView.setDrawRightBar(true);//Draw right bar contain characters of list view
        mIndexableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // when scroll listview then close swipe layout menu if have
                closeSwipeView(true);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //mAirfieldAdapter.setISwipeLayoutCallback(this);
        mAirfieldAdapter.setTypeAdapter(MCCPilotLogConst.WEATHER_ADAPTER);
        mAirfieldAdapter.setDisplayType(mDisplayType);
        mIndexableListView.setAdapter(mAirfieldAdapter);
        mIndexableListView.setFastScrollEnabled(true);

        //PL-903
        mSortType = StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.PREF_SORT_TYPE_WEATHER, SORT_BY_IATA);
        sortWeather();
        refreshWeatherAdapter();
    }




    private int mSortType = 0;
    private static final int SORT_BY_IATA = 0;
    private static final int SORT_BY_ICAO = 1;
    private static final int SORT_BY_DATESAVE_DES = 2;
    private static final int SORT_BY_DATESAVE_ASC = 3;

    public void sortWeather() {
        Log.d("Mode sort weather", String.valueOf(mSortType));
        StorageUtils.writeIntToSharedPref(mActivity, MCCPilotLogConst.PREF_SORT_TYPE_WEATHER, mSortType);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(mSortType==SORT_BY_DATESAVE_ASC){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mBtnSort.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_sort_up) );
            } else {
                mBtnSort.setBackground( getResources().getDrawable(R.drawable.ic_sort_up));
            }
        }else {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mBtnSort.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_sort));
            } else {
                mBtnSort.setBackground( getResources().getDrawable(R.drawable.ic_sort));
            }
        }
        switch (mSortType) {
            case SORT_BY_IATA:
                if (mBtnSort != null)
                    mBtnSort.setText("IATA");
                break;
            case SORT_BY_ICAO:
                if (mBtnSort != null)
                    mBtnSort.setText("ICAO");
                break;
            case SORT_BY_DATESAVE_DES:
            case SORT_BY_DATESAVE_ASC:
                if (mBtnSort != null)
                    mBtnSort.setText("DATE");
                break;
        }

        Collections.sort(mWeathers, new Comparator<WeatherLocal>() {
            @Override
            public int compare(WeatherLocal lhs, WeatherLocal rhs) {
                switch (mSortType) {
                    case SORT_BY_IATA:
                        String leftIATA, rightIATA;
                        leftIATA = TextUtils.isEmpty(lhs.getAFIata()) ? STRING_EMPTY : lhs.getAFIata();
                        rightIATA = TextUtils.isEmpty(rhs.getAFIata()) ? STRING_EMPTY : rhs.getAFIata();
                        return leftIATA.compareToIgnoreCase(rightIATA);
                    case SORT_BY_ICAO:
                        String leftICAO, rightICAO;
                        leftICAO = TextUtils.isEmpty(lhs.getAFIcao()) ? STRING_EMPTY : lhs.getAFIcao();
                        rightICAO = TextUtils.isEmpty(rhs.getAFIcao()) ? STRING_EMPTY : rhs.getAFIcao();
                        return leftICAO.compareToIgnoreCase(rightICAO);
                    case SORT_BY_DATESAVE_DES:
                        String leftDateSaved, rightDateSaved;
                        leftDateSaved = TextUtils.isEmpty(lhs.getDateSaved()) ? STRING_EMPTY : lhs.getDateSaved();
                        rightDateSaved = TextUtils.isEmpty(rhs.getDateSaved()) ? STRING_EMPTY : rhs.getDateSaved();
                        return rightDateSaved.compareToIgnoreCase(leftDateSaved);
                    case SORT_BY_DATESAVE_ASC:
                        leftDateSaved = TextUtils.isEmpty(lhs.getDateSaved()) ? STRING_EMPTY : lhs.getDateSaved();
                        rightDateSaved = TextUtils.isEmpty(rhs.getDateSaved()) ? STRING_EMPTY : rhs.getDateSaved();
                        return leftDateSaved.compareToIgnoreCase(rightDateSaved);
                    default:
                        break;
                }
                return 1;
            }
        });
    }

    /**
     * close swipe layout in case list view
     *
     * @param pSmooth smooth
     */
    public void closeSwipeView(boolean pSmooth) {
        if (mAirfieldAdapter != null && mAirfieldAdapter.getSwipeLayoutShown()) {
            mAirfieldAdapter.closeSwipe(pSmooth);
        }
    }

    @Override
    public void onDelete(View pView, WeatherLocal pModel, int PIndex) {
        closeSwipeView(true);
        mDatabaseManager.deleteWeatherByObject(pModel);
        mWeathers.remove(pModel);
        refreshWeatherAdapter();

        if (isTablet() && !TextUtils.isEmpty(mCurrentSelectWeather) && !TextUtils.isEmpty(pModel.getAFIcao()) && mCurrentSelectWeather.equals(pModel.getAFIcao())) {
            int weatherViewType = 0;
            Fragment fragment = getRightFragment();
            if (fragment != null && fragment instanceof AirfieldMetarFragment) {
                weatherViewType = ((AirfieldMetarFragment) fragment).getWeatherViewType();
                if (weatherViewType == AirfieldMetarFragment.VIEW_EXISTED_REPORT) {
                    ((AirfieldMetarFragment) fragment).clearValues();
                    if (!mWeathers.isEmpty()) {
                        int newPosition = (PIndex <= mWeathers.size() - 1) ? PIndex : 0;
                        mAirfieldAdapter.removeAllItemSelected();
                        mAirfieldAdapter.setSelectItem(newPosition);
                        mIndexableListView.setSelection(newPosition);
                        //if pilot was delete, being view info. Then info screen view info's next item in list.
                        mCurrentSelectWeather = mWeathers.get(newPosition).getAFIcao();
                        //((AirfieldMetarFragment) fragment).setWeatherInfoForTablet(mCurrentSelectWeather);
                    }
                }
            }
        }
    }


    @OnClick({R.id.ibMenu, R.id.btnAdd, R.id.btnDeleteAll, R.id.btnRefresh, R.id.btnSort})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                toggleMenu();
                break;
            case R.id.btnAdd:
                if (mAirfieldAdapter != null) {
                    mAirfieldAdapter.removeAllItemSelected();
                    mAirfieldAdapter.notifyDataSetChanged();
                }
                callAddWeatherFragment();
                break;
            case R.id.btnDeleteAll:
                deleteAll();
                break;
            case R.id.btnRefresh:
                showOptionRefresh();
                break;
            //PL-903
            case R.id.btnSort:
                if (mSortType < 3) mSortType++;
                else mSortType = 0;
                sortWeather();
                refreshWeatherAdapter();
                break;
            //End PL-903
            default:
                break;
        }
    }

    /**
     * process call fragment add new weather
     */
    private void callAddWeatherFragment() {
        replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldMetarFragment.class, null, FLAG_ADD_STACK);
    }

    /**
     * Show menu with options refresh weather reports
     */
    public void showOptionRefresh() {
        LinearLayout mCustomLayout = (LinearLayout) safeInflater(getLayoutInflater(), null, R.layout.menu_option_refresh_weather);

        PopupSelectionMenu.OnClickMenuListener onClickMenuListener = new PopupSelectionMenu.OnClickMenuListener() {
            @Override
            public void onClickMenu(int type) {
                if (type == MCCPilotLogConst.MENU_WEATHER_REFRESH_TODAY) {
                    updateTodayAirfield();
                } else { //type == MCCPilotLogConst.MENU_WEATHER_REFRESH_ALL
                    updateAllWeatherReports();
                }
            }
        };
        mPopupSelectionMenu = new PopupSelectionMenu(mActivity);
        mPopupSelectionMenu.setOnClickMenuListenerOb(onClickMenuListener);
        mPopupSelectionMenu.show(mCustomLayout, mLlBottomBar);
    }

    /**
     * process delete all airfield
     */
    public void deleteAll() {
        if (mWeathers != null && !mWeathers.isEmpty()) {
            MccDialog.getAlertDialog(mActivity, R.string.msg_title_delete_weather, R.string.msg_delete_all_weather
                    , R.string.alert_yes_button, R.string.alert_no_button, MccDialog.FLAG_RESOURCE_NULL, new MccDialog.MCCDialogCallBack() {
                @Override
                public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                    if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                        mDatabaseManager.deleteAllWeather();
                        mWeathers.clear();
                        refreshWeatherAdapter();
                        if (isTablet()) {
                            Fragment fragment = getRightFragment();
                            if (fragment != null && fragment instanceof AirfieldMetarFragment) {
                                int weatherType = ((AirfieldMetarFragment) fragment).getWeatherViewType();
                                if (weatherType == AirfieldMetarFragment.VIEW_EXISTED_REPORT) {
                                    ((AirfieldMetarFragment) fragment).clearValues();
                                }
                            }
                        }
                    }
                }
            }).show();
        }
    }

    /**
     * Handle click on item in weatherlist listview
     *
     * @param pAdapterView adapter
     * @param pView        view
     * @param pPosition    position
     * @param pLong        long
     */
    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition, long pLong) {
        /*int state = mIndexableListView.getScroller().getState();
        if (state != 0) return;*/
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAirfieldAdapter.isEnableClick()) {
                return;
            }
        }
        WeatherLocal weather = mWeathers.get(pPosition);
        mCurrentSelectWeather = weather.getAFIcao();
        Airfield airfield = mDatabaseManager.getAirfieldByICAOIATA(mCurrentSelectWeather);
        Bundle bundle = new Bundle();
        bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, airfield.getAFCode());
        bundle.putInt(MCCPilotLogConst.WEATHER_VIEW_TYPE, AirfieldMetarFragment.VIEW_EXISTED_REPORT);
        if (isTablet()) {
            mAirfieldAdapter.removeAllItemSelected();
            mAirfieldAdapter.setSelectItem(pPosition);
            callAirfieldWeatherInfo(bundle);
        } else {
            replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldMetarFragment.class, bundle, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
        }
    }

    /**
     * process call Airfield WeatherInfo of item Weather with tablet
     *
     * @param pBundle bundle
     */
    public void callAirfieldWeatherInfo(final Bundle pBundle) {
        replaceFragmentTablet(R.id.rightContainerFragment, AirfieldMetarFragment.class, pBundle, FLAG_NOT_ADD_STACK);
    }

    /**
     * Refresh Weather list
     */
    public void refreshWeatherAdapter() {
        if (mAirfieldAdapter != null) {
            mAirfieldAdapter.setDisplayType(mDisplayType);
            mAirfieldAdapter.refreshWeatherAdapter(mWeathers);
            setTextSizeWeather();
        }
    }

    public void setTextSizeWeather() {
        int size = 0;
        if (mWeathers != null && !mWeathers.isEmpty()) {
            size = mWeathers.size();
        }
        mTvNumber.setText("" + size);
    }

    /**
     * Method to update all report in list
     * Flow logic: get all weather -> get ICAO of each report -> refresh data
     */
    private void updateAllWeatherReports() {
        if (isHavingNetwork()) {
            if (!mWeathers.isEmpty()) {
                new ProgressDialogUtils(mActivity).showProgressDialog(R.string.connecting);
                for (final WeatherLocal weather : mWeathers) {
                    final int index = mWeathers.indexOf(weather);
                    new DownloadAsync() {
                        @Override
                        public void onPostDownload(List<String> listString) {
                            new ProgressDialogUtils(mActivity).updateMessage(String.format(getString(R.string.updating_weather_data), weather.getAFIcao()));
                            String metar ="", taf = "";
                            String tempMetar = "", tempTaf = "";
                            if (listString.size() >= 1) {
                                metar = listString.get(0);
                                if (!TextUtils.isEmpty(metar)) {
                                    int indexBreak = metar.indexOf("\n");
                                    if (indexBreak > 0 && metar.length() > indexBreak) {
                                        tempMetar = String.format(AirfieldMetarFragment.METAR_FORMAT, metar.substring(0, indexBreak), metar.substring(indexBreak + 1));
                                    }
                                }
                            }
                            if (listString.size() >= 2) {
                                taf = listString.get(1);
                                if (!TextUtils.isEmpty(taf)) {
                                    int indexBreak = taf.indexOf("\n");
                                    if (indexBreak > 0 && taf.length() > indexBreak) {
                                        tempTaf = String.format(AirfieldMetarFragment.TAF_FORMAT, taf.substring(0, indexBreak), taf.substring(indexBreak + 1));
                                    }
                                }
                            }
                            /* index 0 is METAR, 1 is TAF */
                            //metar = !TextUtils.isEmpty(listString.get(0)) ? listString.get(0) : STRING_EMPTY;
                            //taf = !TextUtils.isEmpty(listString.get(1)) ? listString.get(1) : STRING_EMPTY;
                            try {
                                if (!Utils.checkStringNotEmptyNull(metar) && !Utils.checkStringNotEmptyNull(taf)) {
                                    //PL-978
                                    Utils.showToast(mActivity, getString(R.string.no_wx_report_available));
                                    return;
                                }
                                String info = String.format(AirfieldMetarFragment.INFOR_FORMAT, Utils.checkStringNotEmptyNull(metar) ? tempMetar : "", Utils.checkStringNotEmptyNull(taf) ? tempTaf : "");
                                //weather.setReport(String.format(AirfieldMetarFragment.INFOR_FORMAT, metar, taf));
                                weather.setReport(info);
                                weather.setIcon(new AirfieldMetarFragment().getWeatherIcon(metar));
                                weather.setDateSaved(Calendar.getInstance().getTimeInMillis() + MCCPilotLogConst.STRING_EMPTY);
                                new DatabaseManager(mActivity).saveWeather(weather);
                                mWeathers.set(index, weather);

                                refreshWeatherAdapter();
                                if (mWeathers.size() - 1 == index) {
                                    new ProgressDialogUtils(mActivity).hideProgressDialog();
                                }
                            }catch (Exception ex){

                            }
                        }
                    }.execute(String.format(AirfieldMetarFragment.METAR_URL, weather.getAFIcao()), String.format(AirfieldMetarFragment.TAF_URL, weather.getAFIcao()));
                }

                System.gc();
            }
        } else {
            MccDialog.getOkAlertDialog(mActivity, R.string.no_internet_weather).show();
        }
    }

    /**
     * Method to update (add new if needed) wx report for today's airfield in Flight list
     * Flow logic: get All today flight -> get all airfield in those flight (arrival and departure) -> add to database and list
     */
    private void updateTodayAirfield() {
        final HashMap<String, String> mAirfieldList = new DatabaseManager(mActivity).getAllTodayFlightAirfield();
        final int total = mAirfieldList.keySet().size();
        if (total == 0) {
            MccDialog.getOkAlertDialog(mActivity, R.string.no_flight_today).show();
            return;
        }
        int index = 0;
        new ProgressDialogUtils(mActivity).showProgressDialog(R.string.connecting);
        for (final String icao : mAirfieldList.keySet()) {
            index++;
            final int finalIndex = index;
            new DownloadAsync() {
                @Override
                public void onPostDownload(List<String> listString) {
                    new ProgressDialogUtils(mActivity).updateMessage(String.format(getString(R.string.updating_weather_data), icao));
                    String metar ="", taf = "";
                    String tempMetar = "", tempTaf = "";
                    if (listString.size() >= 1) {
                        metar = listString.get(0);
                        if (!TextUtils.isEmpty(metar)) {
                            int indexBreak = metar.indexOf("\n");
                            if (indexBreak > 0 && metar.length() > indexBreak) {
                                tempMetar = String.format(AirfieldMetarFragment.METAR_FORMAT, metar.substring(0, indexBreak), metar.substring(indexBreak + 1));
                            }
                        }
                    }
                    if (listString.size() >= 2) {
                        taf = listString.get(1);
                        if (!TextUtils.isEmpty(taf)) {
                            int indexBreak = taf.indexOf("\n");
                            if (indexBreak > 0 && taf.length() > indexBreak) {
                                tempTaf = String.format(AirfieldMetarFragment.TAF_FORMAT, taf.substring(0, indexBreak), taf.substring(indexBreak + 1));
                            }
                        }
                    }
                            /* index 0 is METAR, 1 is TAF */
                    //metar = !TextUtils.isEmpty(listString.get(0)) ? listString.get(0) : STRING_EMPTY;
                    //taf = !TextUtils.isEmpty(listString.get(1)) ? listString.get(1) : STRING_EMPTY;
                    try {
                        if (!Utils.checkStringNotEmptyNull(metar) && !Utils.checkStringNotEmptyNull(taf)) {
                            //PL-978
                            Utils.showToast(mActivity, getString(R.string.no_wx_report_available));
                            return;
                        }
                        String info = String.format(AirfieldMetarFragment.INFOR_FORMAT, Utils.checkStringNotEmptyNull(metar) ? tempMetar : "", Utils.checkStringNotEmptyNull(taf) ? tempTaf : "");
                        //weather.setReport(String.format(AirfieldMetarFragment.INFOR_FORMAT, metar, taf));

                        WeatherLocal weather = new WeatherLocal();
                        weather.setReport(info);
                        //weather.setReport(String.format(AirfieldMetarFragment.INFOR_FORMAT, metar, taf));
                        weather.setIcon(new AirfieldMetarFragment().getWeatherIcon(metar));
                        weather.setDateSaved(Calendar.getInstance().getTimeInMillis() + MCCPilotLogConst.STRING_EMPTY);
                        weather.setAFIata(mAirfieldList.get(icao));
                        weather.setAFIcao(icao);
                        new DatabaseManager(mActivity).saveWeather(weather);
                        if (isRecordExist(weather.getAFIcao())) {
                            updateList(weather.getAFIcao(), weather);
                        } else {
                            mWeathers.add(weather);
                        }

                        refreshWeatherAdapter();
                        if (total == finalIndex) {
                            new ProgressDialogUtils(mActivity).hideProgressDialog();
                        }
                    }catch (Exception ex){

                    }
                }
            }.execute(String.format(AirfieldMetarFragment.METAR_URL, icao), String.format(AirfieldMetarFragment.TAF_URL, icao));
        }
    }

    private boolean isRecordExist(String pIcao) {
        for (WeatherLocal w : mWeathers) {
            if (w.getAFIcao().equalsIgnoreCase(pIcao)) {
                return true;
            }
        }
        return false;
    }

    private void updateList(String pIcao, WeatherLocal pWeather) {
        int index;
        for (WeatherLocal w : mWeathers) {
            if (w.getAFIcao().equalsIgnoreCase(pIcao)) {
                index = mWeathers.indexOf(w);
                mWeathers.set(index, pWeather);
                return;
            }
        }
    }

    /**
     * Add new weather from AirfieldWeather screen.
     * If the record of that airfield already existed, update data instead of inserting new one
     *
     * @param pWeather new weather to insert
     */
    public void addNewWeather(WeatherLocal pWeather) {
        if (pWeather == null) {
            return;
        }
        boolean isRecordExisted = false;
        for (WeatherLocal w : mWeathers) {
            if (w.getAFIcao().equals(pWeather.getAFIcao())) {
                w.setReport(pWeather.getReport());
                w.setIcon(pWeather.getIcon());
                w.setDateSaved(pWeather.getDateSaved());
                isRecordExisted = true;
            }
        }
        if (!isRecordExisted) {
            mWeathers.add(pWeather);
        }
        //sort after add new weather
        sortWeather();
        refreshWeatherAdapter();
    }

    //TuanPV for portrait tablet
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPopupSelectionMenu != null) {
            mPopupSelectionMenu.dissmissPopup();
        }
    }

    @Override
    protected void onKeyBackPress() {
//        super.onKeyBackPress();
        if (!isTablet()) {
            super.onKeyBackPress();
        }
    }
}
