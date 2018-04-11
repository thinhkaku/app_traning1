package aero.pilotlog.fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.MainActivity;
import aero.pilotlog.activities.MainTabletActivity;
import aero.pilotlog.adapters.AircraftSearchAdapter;
import aero.pilotlog.adapters.AirfieldSearchAdapter;
import aero.pilotlog.adapters.FlightAdapter;
import aero.pilotlog.adapters.PilotSearchAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISequenceAction;
import aero.pilotlog.interfaces.ISwipeLayoutFlightCallback;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.ValidationUtils;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.PopupSelectionMenu;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogbooksListFragment extends BaseMCCFragment implements IAsyncTaskCallback , ISwipeLayoutFlightCallback {


    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Nullable
    @Bind(R.id.listView)
    ListView mListView;
    @Nullable
    @Bind(R.id.rlBottomBar)
    LinearLayout mRlBottomBar;
    @Nullable
    @Bind(R.id.llBottomBar)
    LinearLayout mLlBottomBar;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.btnSort)
    Button mBtnSort;
    @Nullable
    @Bind(R.id.ll_search_logbook)
    LinearLayout mLlSearchLogbook;
    @Bind(R.id.edt_filter_date)
    EditText edtFilterDate;
    @Bind(R.id.edt_search_pilot)
    AutoCompleteTextView edtFilterPilot;
    @Bind(R.id.edt_search_aircraft)
    AutoCompleteTextView edtFilterAircraft;
    @Bind(R.id.edt_search_airfield)
    AutoCompleteTextView edtFilterAirfield;

    private DatabaseManager mDatabaseManager;
    private List<FlightModel> mFlightList = new ArrayList<>();
    private List<FlightModel> mFlightListCopy = new ArrayList<>();
    private FlightAdapter mAdapter;
    private LoadDataTask mLoadDataTask;
    private Bundle mBundle;
    private int mSortType;
    private int backStackCount = 0;
    private byte[] airfieldCodeFilter;
    private List<Airfield> airfieldList;
    private AirfieldSearchAdapter airfieldSearchAdapter;
    private List<Pilot> pilotList;
    private PilotSearchAdapter pilotSearchAdapter;
    private List<Aircraft> aircraftList;
    private AircraftSearchAdapter aircraftSearchAdapter;
    private byte[] aircraftCodeFilter;
    private byte[] pilotCodeFilter;
    private MccEnum.dateFilter dateFilter = MccEnum.dateFilter.LAST_90_DAYS;
    //private LoadMoreLogbookTask mLoadMoreLogbookTask;
    private int mLogbookPage = 1;
    private LinearLayout mCustomLayout;
    private PopupSelectionMenu mPopupSelectionMenu;


    public LogbooksListFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mBundle = getArguments();
        initFilter();
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
        airfieldList = mDatabaseManager.getAllAirfieldFavorite();
        edtFilterAirfield.setThreshold(1);
        airfieldSearchAdapter = new AirfieldSearchAdapter(mActivity, R.layout.layout_index_listview, R.id.lbl_af_name, airfieldList);
        edtFilterAirfield.setAdapter(airfieldSearchAdapter);

        pilotList = mDatabaseManager.getAllPilot();
        edtFilterPilot.setThreshold(1);
        pilotSearchAdapter = new PilotSearchAdapter(mActivity, R.layout.layout_index_listview, R.id.lbl_pilot_name, pilotList);
        edtFilterPilot.setAdapter(pilotSearchAdapter);

        aircraftList = mDatabaseManager.getAllAircraft();
        edtFilterAircraft.setThreshold(1);
        aircraftSearchAdapter = new AircraftSearchAdapter(mActivity, R.layout.layout_index_listview, R.id.lbl_reference, aircraftList);
        edtFilterAircraft.setAdapter(aircraftSearchAdapter);
    }

    private void initFilter() {
        if (mBundle == null) return;
        aircraftCodeFilter = mBundle.getByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY);
        airfieldCodeFilter = mBundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        pilotCodeFilter = mBundle.getByteArray(MCCPilotLogConst.PILOT_CODE_KEY);
        Aircraft aircraft = mDatabaseManager.getAircraftByCode(aircraftCodeFilter);
        Airfield airfield = mDatabaseManager.getAirfieldByCode(airfieldCodeFilter);
        Pilot pilot = mDatabaseManager.getPilotByCode(pilotCodeFilter);
        if (aircraft != null || airfield != null || pilot != null) {
            if (aircraft != null) {
                edtFilterAircraft.setText(aircraft.getRefSearch());
            }
            if (airfield != null) {
                edtFilterAirfield.setText(airfield.getAFName());
            }
            if (pilot != null) {
                edtFilterPilot.setText(pilot.getPilotName());
            }
        }
    }

    private void initView() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        backStackCount = fm.getBackStackEntryCount();
       // if (backStackCount > 0) {
            Fragment leftFragment = getLeftFragment();
            if(leftFragment!=null && !leftFragment.getClass().getName().contains("LogbooksListFragment")){
                mIbMenu.setImageResource(R.drawable.ic_back);
            }

        //}
        mTvTitle.setText(R.string.text_logbook_camelcase);
        SettingLocal settingSort = mDatabaseManager.getSettingLocal(MCCPilotLogConst.SETTING_CODE_SORT_LOGBOOK);
        if (settingSort != null) {
            mSortType = Integer.parseInt(settingSort.getData());
            switch (mSortType) {
                case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                    mBtnSort.setText(getString(R.string.text_sort_1greatthan31));
                    break;
                case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                    mBtnSort.setText(getString(R.string.text_sort_31greatthan1));
                    break;
                case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                    mBtnSort.setText(getString(R.string.text_sort_31lessthan1));
                    break;
                default:
                    break;
            }
        }
        mActivity.setFlightLogbookSortType(mSortType);
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_standar_listview;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar_logbook;
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mFlightList.clear();
        mFlightList = mDatabaseManager.getLogbookList(mSortType, dateFilter, aircraftCodeFilter, airfieldCodeFilter, pilotCodeFilter);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        mFlightListCopy.addAll(mFlightList);
        mAdapter = new FlightAdapter(mActivity, mFlightList);
        mAdapter.setSwipeLayoutCallBack(this);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Nullable
    @OnItemClick({R.id.listView})
    public void onItemClick(AdapterView<?> pAdapterView, View pView, final int pPosition, long pLong) {
        if (mPopupSelectionMenu != null && mPopupSelectionMenu.isShowing()) {
            return;
        }
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAdapter.isEnableClick()) {
                return;
            }
        }
        FlightModel flight = mFlightList.get(pPosition);
        Bundle b = new Bundle();
        b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, flight.getFlightCode());
        b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_FLIGHT_VIEW);
        replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);

    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnDeleteAll, R.id.btnSort, R.id.btnAdd, R.id.btnSearch, R.id.tv_cancel, R.id.tv_search,
            R.id.iv_filter_date, R.id.iv_filter_pilot, R.id.iv_filter_airfield, R.id.iv_filter_aircraft,
            R.id.btn_configure_shortcut, R.id.ll_search_logbook})
    public void onClick(View pView) {
        Bundle bundle = new Bundle();
        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
        bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_LOGBOOK_SEARCH);
        switch (pView.getId()) {
            case R.id.ibMenu:
              /*  if (backStackCount == 0) {
                    KeyboardUtils.hideKeyboard(mActivity);
                    toggleMenu();
                } else {
                    finishFragment();
                }*/
                Fragment leftFragment = getLeftFragment();
                if(leftFragment!=null && !leftFragment.getClass().getName().contains("LogbooksListFragment")){
                    finishFragment();
                } else {
                    toggleMenu();
                }
                break;
            case R.id.btnSearch:
                if (mLlSearchLogbook == null) {
                    break;
                }
                if (mLlSearchLogbook.getVisibility() == View.GONE) {
                    mLlSearchLogbook.setVisibility(View.VISIBLE);
                    //Insight 67740
                    if (mLlBottomBar != null) {
                        mLlBottomBar.setVisibility(View.GONE);
                    }
                    mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //End 67740
                } else {
                    mLlSearchLogbook.setVisibility(View.GONE);
                    //Insight 67740
                    if (mLlBottomBar != null) {
                        mLlBottomBar.setVisibility(View.VISIBLE);
                    }
                    mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    //End 67740
                }
                break;
            case R.id.iv_filter_date:
                displayDialogSelect();
                break;
            case R.id.iv_filter_pilot:
                this.replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, PilotListFragment.class, bundle, true);
                break;
            case R.id.iv_filter_aircraft:
                this.replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AircraftListFragment.class, bundle, true);
                break;
            case R.id.iv_filter_airfield:
                this.replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldsListFragment.class, bundle, true);
                break;
            case R.id.tv_search:
                if (mLlSearchLogbook != null) {
                    mLlSearchLogbook.setVisibility(View.GONE);
                }
                if (mLlBottomBar != null) {
                    mLlBottomBar.setVisibility(View.VISIBLE);
                }
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                searchLogbook();
                break;
            case R.id.tv_cancel:
                if (mLlSearchLogbook != null) {
                    mLlSearchLogbook.setVisibility(View.GONE);
                }
                //Insight 67740
                if (mLlBottomBar != null) {
                    mLlBottomBar.setVisibility(View.VISIBLE);
                }
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                //End 67740
                KeyboardUtils.hideKeyboard(mActivity);//TuanPV add new
                edtFilterAircraft.getText().clear();
                edtFilterAirfield.getText().clear();
                edtFilterPilot.getText().clear();
                edtFilterDate.setText("Last 90 days");
                searchLogbook();
                break;
            case R.id.btnSort:
                processSortLogbook();
                break;
            case R.id.btnDeleteAll:
                showOptionDeleteAll();
                break;
        }
    }

    public void refreshListFlight() {
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isTablet()) {
            mActivity = (MainTabletActivity) getActivity();
        } else {
            mActivity = (MainActivity) getActivity();
        }
        refreshListFlight();
    }

    private void displayDialogSelect() {
        ArrayList<CharSequence> listStringDialog = new ArrayList<>();
        listStringDialog.add("Last 90 days");
        listStringDialog.add("Last 6 months");
        listStringDialog.add("Year 2017");
        listStringDialog.add("Year 2016");
        listStringDialog.add("All Records");
        android.support.v7.app.AlertDialog alertDialog;
        int index = listStringDialog.indexOf(edtFilterDate.getText().toString());
        alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mActivity, "Select Date range",
                listStringDialog.toArray(new CharSequence[listStringDialog.size()]), index, new ISequenceAction() {
                    @Override
                    public void sequenceAction(String pName, int pIndex) {
                        edtFilterDate.setText(pName);
                    }
                });
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
    }

    public void processSortLogbook() {
        switch (mSortType) {
            case MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31:
                mSortType = MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1;
                mBtnSort.setText(getString(R.string.text_sort_31greatthan1));
                break;
            case MCCPilotLogConst.SORT_BY_31_GREAT_THAN_1:
                mSortType = MCCPilotLogConst.SORT_BY_31_LESS_THAN_1;
                mBtnSort.setText(getString(R.string.text_sort_31lessthan1));
                break;
            case MCCPilotLogConst.SORT_BY_31_LESS_THAN_1:
                mSortType = MCCPilotLogConst.SORT_BY_1_GREAT_THAN_31;
                mBtnSort.setText(getString(R.string.text_sort_1greatthan31));
                break;
            default:
                break;
        }
        mActivity.setFlightLogbookSortType(mSortType);
        mDatabaseManager.updateSettingLocal("SortLogbook", String.valueOf(mSortType));
        mFlightList.clear();
        mFlightListCopy.clear();
        refreshListFlight();
        refreshAdapter();
        mLlLoading.setVisibility(View.VISIBLE);

    }

    public void refreshAdapter() {
        if (mAdapter != null) {
            mAdapter.refreshLogbookAdapter(mFlightList);
            setTextSizeLogbook(mFlightList);
        }
    }

    public void setTextSizeLogbook(List<FlightModel> pFlightList) {
        int size = 0;
        if (pFlightList != null && !pFlightList.isEmpty()) {
            size = pFlightList.size();
        }
        mTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + size);
    }

    private void searchLogbook() {
        KeyboardUtils.hideKeyboard(mActivity);
        mLlLoading.setVisibility(View.VISIBLE);
        if (mLlSearchLogbook != null) {
            mLlSearchLogbook.setVisibility(View.GONE);
        }
        aircraftCodeFilter = null;
        airfieldCodeFilter = null;
        pilotCodeFilter = null;
        String filterPilot = edtFilterPilot.getText().toString().trim();
        String filterAirfield = edtFilterAirfield.getText().toString().trim();
        String filterAircraft = edtFilterAircraft.getText().toString().trim();
        if (filterPilot.length() < 3 || ValidationUtils.isNumeric(filterPilot)) {
            edtFilterPilot.getText().clear();
            filterPilot = STRING_EMPTY;
        }
        if (filterAircraft.length() < 3) {
            edtFilterAircraft.getText().clear();
            filterAircraft = STRING_EMPTY;
        }
        if (filterAirfield.length() < 3) {
            edtFilterAirfield.getText().clear();
            filterAirfield = STRING_EMPTY;
        }
        Aircraft aircraftSearch = mDatabaseManager.getAircraftByReference(filterAircraft);
        if (aircraftSearch != null) aircraftCodeFilter = aircraftSearch.getAircraftCode();
        Airfield airfieldSearch = mDatabaseManager.getAirfieldsByName(filterAirfield, true);
        if (airfieldSearch != null) airfieldCodeFilter = airfieldSearch.getAFCode();
        Pilot pilotSearch = mDatabaseManager.getPilotByName(filterPilot);
        if (pilotSearch != null) pilotCodeFilter = pilotSearch.getPilotCode();
        switch (edtFilterDate.getText().toString()) {
            case "Last 90 days":
                dateFilter = MccEnum.dateFilter.LAST_90_DAYS;
                break;
            case "Last 6 months":
                dateFilter = MccEnum.dateFilter.LAST_6_MONTHS;
                break;
            case "Year 2017":
                dateFilter = MccEnum.dateFilter.YEAR_2017;
                break;
            case "Year 2016":
                dateFilter = MccEnum.dateFilter.YEAR_2016;
                break;
            case "All Records":
                dateFilter = MccEnum.dateFilter.ALL_REPORTS;
                break;
        }

        mFlightList.clear();
        mSearchLogbookTask = new SearchLogbookTask();
        mSearchLogbookTask.execute();
    }

    private SearchLogbookTask mSearchLogbookTask;

    private class SearchLogbookTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLlLoading.setVisibility(View.VISIBLE);
            if (mLlSearchLogbook != null) {
                mLlSearchLogbook.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLlLoading.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<FlightModel> flightList = mDatabaseManager.getLogbookList(mSortType, dateFilter, aircraftCodeFilter, airfieldCodeFilter, pilotCodeFilter);
            mFlightList.addAll(flightList);

            return null;
        }
    }

    public void setAircraftFilter(final String aircraftReference) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edtFilterAircraft.setText(aircraftReference);
            }
        });
    }

    public void setAirfieldFilter(final String airfieldFilter) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edtFilterAirfield.setText(airfieldFilter);
            }
        });
    }

    public void setPilotFilter(final String pilotFilter) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edtFilterPilot.setText(pilotFilter);
            }
        });
    }

    boolean result = false;

    private void showOptionDeleteAll() {
        mCustomLayout = (LinearLayout) safeInflater(getLayoutInflater(), null, R.layout.menu_option_delete);
        TextView tvDeleteAllFlightPrior = ButterKnife.findById(mCustomLayout, R.id.tvDeleteAllFlightPrior);
        tvDeleteAllFlightPrior.setText(getString(R.string.text_menu_delete_all_flight_older_than_6_months));

        PopupSelectionMenu.OnClickMenuListener onClickMenuListener = new PopupSelectionMenu.OnClickMenuListener() {
            @Override
            public void onClickMenu(int type) {
                switch (type) {
                    case MCCPilotLogConst.MENU_LOGBOOK_DELETE_ALL:
                        MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.confirm_delete_all_flight_title,
                                getString(R.string.confirm_delete_all_flight_content), new MccDialog.MCCDialogCallBack() {
                                    @Override
                                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                            result = mDatabaseManager.deleteFlight(MCCPilotLogConst.LOGBOOK_DELETE_ALL, null, FlightUtils.TimeMode.UTC);
                                            if (result) {
                                                mFlightList.clear();
                                                refreshAdapter();
                                            }
                                        }
                                    }
                                }).show();
                        break;
                    case MCCPilotLogConst.MENU_LOGBOOK_DELETE_PRIOR:
                        String confirm_content;
                        confirm_content = getString(R.string.confirm_delete_all_flight_older_than_6months);
                        MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.confirm_delete_all_flight_title,
                                confirm_content, new MccDialog.MCCDialogCallBack() {
                                    @Override
                                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                            FlightUtils.TimeMode dateMode;
                                            String dMode = mDatabaseManager.getSetting(309).getData();
                                            switch (dMode) {
                                                case "1":
                                                    dateMode = FlightUtils.TimeMode.UTC;
                                                    break;
                                                case "0":
                                                    dateMode = FlightUtils.TimeMode.LOCAL;
                                                    break;
                                                case "-1":
                                                    dateMode = FlightUtils.TimeMode.BASE;
                                                    break;
                                                default:
                                                    dateMode = FlightUtils.TimeMode.LOCAL;
                                                    break;
                                            }
                                            result = mDatabaseManager.deleteFlight(MCCPilotLogConst.LOGBOOK_DELETE_PRIOR, null, dateMode);
                                            if (result) {
                                                List<FlightModel> flightPilotList = mDatabaseManager.getFlightList(mSortType);
                                                mFlightList.clear();
                                                mFlightList.addAll(flightPilotList);
                                                refreshAdapter();
                                            }
                                        }
                                    }
                                }).show();
                        break;
                    case MCCPilotLogConst.MENU_LOGBOOK_DELETE_IN:
                        MccDialog.getOkCancelAlertDialog(mActivity, R.string.confirm_delete_all_flight_title,
                                getString(R.string.confirm_delete_all_flight_in_content), new MccDialog.MCCDialogCallBack() {
                                    @Override
                                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                            result = mDatabaseManager.deleteFlight(MCCPilotLogConst.LOGBOOK_DELETE_IN, null, FlightUtils.TimeMode.UTC);
                                            if (result) {
                                                List<FlightModel> flightPilotList = mDatabaseManager.getFlightList(mSortType);
                                                mFlightList.clear();
                                                mFlightList.addAll(flightPilotList);
                                                refreshAdapter();
                                            }
                                        }
                                    }
                                }).show();
                        break;
                    default:
                        break;
                }

            }
        };
        mPopupSelectionMenu = new PopupSelectionMenu(mActivity);
        mPopupSelectionMenu.setOnClickMenuListenerOb(onClickMenuListener);
        mPopupSelectionMenu.show(mCustomLayout, mLlBottomBar);
    }

    public void closeSwipeView(boolean pSmooth) {
        if (mAdapter != null && mAdapter.getSwipeLayoutShown()) {
            mAdapter.closeSwipe(pSmooth);
        }
    }

    @Override
    public void onDeleteRecord(View pView, final FlightModel pFlight, int pPosition) {
        closeSwipeView(true);
        //PL-685
        String deleteRecord;
        String flightNumber = pFlight.getFlightNumber();
        String flightAirfield = pFlight.getFlightAirfield();
        if (!TextUtils.isEmpty(flightNumber)) {
            if (flightNumber.equalsIgnoreCase("simulator"))
                deleteRecord = "Delete SIM ";
            else
                deleteRecord = "delete " + flightNumber;
        } else {
            deleteRecord = "Delete Record ";
            flightAirfield = "";
        }
        if (!TextUtils.isEmpty(flightAirfield)) {
            flightAirfield = " (" + flightAirfield + ")";
        }
        Calendar calendar = DateTimeUtils.getCalendar(pFlight.getFlightDateUTC());
        String flightDate = DateTimeUtils.formatDateToString(calendar.getTime());
        deleteRecord = deleteRecord + flightAirfield + " on " + flightDate;

        MccDialog.getOkDeleteAlertDialog(mActivity, R.string.confirm_delete_flight_title
                , String.format(getString(R.string.confirm_delete_single_flight), deleteRecord), new MccDialog.MCCDialogCallBack() {

                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                            boolean result = mDatabaseManager.deleteFlight(MCCPilotLogConst.LOGBOOK_DELETE_ONE, pFlight, FlightUtils.TimeMode.UTC);
                            if (result) {
                                mFlightList.remove(pFlight);
                                refreshAdapter();
                            }
                        }
                    }
                }).show();
    }
}
