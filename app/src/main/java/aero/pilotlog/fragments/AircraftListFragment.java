package aero.pilotlog.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.AircraftListAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISwipeLayoutAircraftCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by ngoc.dh on 8/17/2015.
 * Aircraft list
 */
public class AircraftListFragment extends BaseMCCFragment implements IAsyncTaskCallback, ISwipeLayoutAircraftCallback {
    @Bind(R.id.indexableListView)
    IndexableListView mLvAircraft;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Nullable
    @Bind(R.id.rlSearchBar)
    RelativeLayout mHeaderRlSearch;
    @Bind(R.id.tvTitle)
    TextView mHeaderTvTitle;
    @Bind(R.id.tvNumber)
    TextView mHeaderTvNumber;
    @Nullable
    @Bind(R.id.btn_bottom1)
    Button mFooterBtnAdd;
    @Nullable
    @Bind(R.id.btn_bottom2)
    Button mFooterBtnImportContact;
    @Nullable
    @Bind(R.id.ln_bottom2)
    RelativeLayout mrlBottom2;
    @Nullable
    @Bind(R.id.btn_bottom3)
    Button mFooterBtnSort;
    @Nullable
    @Bind(R.id.btn_bottom4)
    Button mFooterBtnDelete;
    @Nullable
    @Bind(R.id.btn_bottom5)
    Button mFooterBtnSelect;
    @Nullable
    @Bind(R.id.ln_bottom5)
    RelativeLayout mrlBottom5;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Nullable
    @Bind(R.id.rlBottomBar)
    LinearLayout mLnBottomBar;

    private List<Aircraft> mAircraftList;
    private List<Aircraft> mAircraftCopy;
    private int mViewType = MCCPilotLogConst.LIST_MODE;
    //For side index scroll
    private AircraftListAdapter mAircraftListAdapter;
    //private DatabaseManager mDatabaseManager;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManager;
    //For search
    private boolean mIsSearch, mIsFirstSort;
    private String mRefSearch;
    private String mCurrentSelectAircraft;
    private String SELECT_LIST_TYPE = MCCPilotLogConst.SELECT_LIST_TYPE_AIRCRAFT_INFO;

    /**
     * Constructor
     */
    public AircraftListFragment() {
    }

    @Override
    protected int getContentResId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mViewType = bundle.getInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE);
            mRefSearch = bundle.getString(MCCPilotLogConst.AIRCRAFT_REF_SEARCH, STRING_EMPTY);
        }

        return R.layout.fragment_delay_tails;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return mViewType == MCCPilotLogConst.SELECT_MODE ? FLAG_NO_RESOURCE : R.layout.layout_bottom_bar;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity);
        mIsSearch = true;
        mAircraftCopy = new ArrayList<>();
        if (mViewType == MCCPilotLogConst.SELECT_MODE && !TextUtils.isEmpty(mRefSearch)) {
            mAircraftList = mDatabaseManager.getAircraftByRefSearch(mRefSearch);
        } else {
            mAircraftList = populateAircraft();
        }

        /*For search*/
        if (mAircraftList != null && mAircraftList.isEmpty()) {
            mAircraftCopy.addAll(mAircraftList);
        }
        initView();

        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    /**
     * Initialize views
     */
    private void initView() {
        /*Set header view*/
        if (mHeaderTvTitle != null) {
            mHeaderTvTitle.setText(mViewType == MCCPilotLogConst.SELECT_MODE ? "Select Aircraft" : getString(R.string.aircraft_list_title));
        }
        /*Set footer view*/
        if (mFooterBtnAdd != null && mFooterBtnSort != null && mFooterBtnDelete != null && mFooterBtnSelect != null && mFooterBtnImportContact != null) {
            mFooterBtnAdd.setVisibility(View.VISIBLE);
            mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_type));
            mFooterBtnSort.setVisibility(View.VISIBLE);
            mFooterBtnDelete.setVisibility(View.VISIBLE);
            mFooterBtnSelect.setVisibility(View.GONE);
            mrlBottom2.setVisibility(View.GONE);
            mFooterBtnImportContact.setVisibility(View.GONE);
            mrlBottom5.setVisibility(View.GONE);
        }
        /*Display search bar for phone only*/

        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            mIbMenu.setImageResource(R.drawable.ic_back);
        }
        mLnBottomBar.setWeightSum(3f);
        KeyboardUtils.hideKeyboardWhenClickLoupe(mEdtSearch, getActivity());
        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);

    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        if (mAircraftList == null) {
            return;
        }
        mAircraftListAdapter = new AircraftListAdapter(mActivity, mAircraftList) {

        };
        mAircraftListAdapter.setISwipeLayoutCallback(this);
        mAircraftListAdapter.setSelectMode(mViewType == MCCPilotLogConst.SELECT_MODE);
        /*Handle side index scroll*/
        mLvAircraft.setAdapter(mAircraftListAdapter);
        mLvAircraft.setDrawRightBar(true); /*Draw right bar containing characters of list view*/
        mLvAircraft.setFastScrollEnabled(true);

        mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAircraftListAdapter.getCount());
        int sortType = Integer.parseInt(mDatabaseManager.getSettingLocal("SortAircraft").getData());
        mIsFirstSort = true;
        if (sortType == MCCPilotLogConst.AIRCRAFT_SORT_TYPE) {
            if (mFooterBtnSort != null) {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_type));
            }
            sortAircraftList();
        } else if (sortType == MCCPilotLogConst.AIRCRAFT_SORT_TAIL) {
            if (mFooterBtnSort != null) {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_tail));
            }
            sortAircraftList();
        }
        if (!mAircraftList.isEmpty()) {
            // Change 02/25/2016
//            refreshAircraftInfo();
        }
    }

    private int getCurrentIndexAfterSort() {
        if (mAircraftList != null) {
            for (int size = mAircraftList.size(), i = 0; i < size; i++) {
                if (mCurrentSelectAircraft != null && mAircraftList.get(i).getAircraftCode() != null && mCurrentSelectAircraft.equals(mAircraftList.get(i).getAircraftCode())) {
                    return i;
                }
            }
        }

        return 0;
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterViewParent, View pView, final int position, long id) {
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAircraftListAdapter.isEnableClick()) {
                return;
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null)
            SELECT_LIST_TYPE = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRCRAFT_INFO);
        /*if (isTablet()) {

        } else {*/
        switch (SELECT_LIST_TYPE) {
            case MCCPilotLogConst.SELECT_LIST_TYPE_AIRCRAFT_INFO:
                bundle = new Bundle();
                bundle.putByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY, mAircraftList.get(position).getAircraftCode());
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AircraftInfoFragment.class, bundle, isTablet() ? FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
                break;
            case MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_ADD:
                final FlightAddsFragment flightAddsFragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                if (flightAddsFragment != null) {
                    KeyboardUtils.hideKeyboard(mActivity);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            flightAddsFragment.setAircraft(mAircraftList.get(position));
                        }
                    });

                    finishFragment();
                }
                break;
            case MCCPilotLogConst.SELECT_LIST_TYPE_LOGBOOK_SEARCH:
                final LogbooksListFragment logbooksListFragment = (LogbooksListFragment) getFragment(LogbooksListFragment.class);
                if (logbooksListFragment != null) {
                    KeyboardUtils.hideKeyboard(mActivity);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            logbooksListFragment.setAircraftFilter(mAircraftList.get(position).getReference());
                        }
                    });

                    finishFragment();
                }
                break;
        }

        //}
    }

    /**
     * process call fragment aircraft info, with tablet
     *
     * @param pBundle bundle
     */
    public void callAircraftInfoTablet(Bundle pBundle) {
        Fragment fragment = getRightFragment();
        if (fragment != null && fragment instanceof AircraftInfoFragment) {
            ((AircraftInfoFragment) fragment).initDataAircraft(pBundle);
        } else {
            replaceFragmentTablet(R.id.rightContainerFragment, AircraftInfoFragment.class, pBundle, FLAG_NOT_ADD_STACK);
        }
    }

    /**
     * Get all flight pilot from database
     *
     * @return flight pilot list
     */
 /*   private List<FlightPilot> populateFlightPilot() {
        List<FlightPilot> flightPilots;
        flightPilots = mDatabaseManager.getAllFlightPilots();
        return flightPilots;
    }*/

    /**
     * Get all aircraft from database
     *
     * @return aircraft list
     */
    private List<Aircraft> populateAircraft() {
        List<Aircraft> aircraftList;
        aircraftList = mDatabaseManager.getAllAircraft();
        return aircraftList;
    }

    /**
     * Delete all aircraft
     */
    private void deleteAllAircraft() {
        if (mDatabaseManager.getSizeFlight() == 0) {
            if (mAircraftList.size() > 0) {
                MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.delete_aircraft_dialog_title
                        , R.string.delete_all_aircraft, new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    mDatabaseManager.deleteAllAircraft();
                                    mAircraftList.clear();
                                    mAircraftList = populateAircraft();
                                    mAircraftListAdapter.refreshAircraftListAdapter(mAircraftList);
                                    mAircraftCopy.clear();
                                    mAircraftCopy.addAll(mAircraftList);
                                    mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAircraftList.size());
//                        refreshAircraftInfo();
                                    if (isTablet()) {
                                        //if aircraft is empty then view info is watermark
                                        replaceFragmentTablet(R.id.rightContainerFragment, AircraftInfoEmptyFragment.class, FLAG_NOT_ADD_STACK);
                                    }
                                }
                            }
                        }).show();
            }
        } else {
            MccDialog.getOkAlertDialog(mActivity, R.string.delete_aircraft_dialog_title
                    , R.string.delete_each_aircraft).show();
        }
    }

    /**
     * Search bar
     *
     * @param pText: inserted text for search
     */
    @Nullable
    @OnTextChanged({R.id.edtSearch})
    public void onTextChanged(CharSequence pText) {
        try {
            if (mIsSearch && pText.length() != 0) {
                String strSearch = pText.toString().trim();
                mAircraftList.clear();
                if (strSearch.length() > 0) {
                    mAircraftList = mDatabaseManager.searchAircraft(strSearch);
                } else {
                    mAircraftList.addAll(mAircraftCopy);
                }
                mAircraftListAdapter.refreshAircraftListAdapter(mAircraftList);
            } else if (pText.length() == 0) {

                mAircraftList.clear();
                mAircraftList.addAll(mAircraftCopy);
                mAircraftListAdapter.refreshAircraftListAdapter(mAircraftList);

            }
            mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAircraftList.size());
            mIsSearch = true;
        } catch (Exception ex) {

        }
        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
    }

    @Nullable
    @OnClick({R.id.btn_bottom1, R.id.btn_bottom3, R.id.btn_bottom4, R.id.btnCancel, R.id.ibMenu})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btn_bottom1: //Add aircraft
                //MccDialog.getOkAlertDialog(mActivity, R.string.aircraft_add_title, R.string.aircraft_add_message).show();
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AircraftAddFragment.class, getArguments(), FLAG_ADD_STACK);
                break;
            case R.id.btn_bottom3: // Sort aircraft
                if (mFooterBtnSort == null) {
                    break;
                }
                if (mFooterBtnSort.getText().toString().equals(getString(R.string.sort_aircraft_list_type))) {
                    /*StorageUtils.writeIntToSharedPref(mActivity
                            , MCCPilotLogConst.AIRCRAFT_SORT_KEY, MCCPilotLogConst.AIRCRAFT_SORT_TAIL);*/
                    mDatabaseManager.updateSettingLocal("SortAircraft", String.valueOf(MCCPilotLogConst.AIRCRAFT_SORT_TAIL));
                } else {
                   /* StorageUtils.writeIntToSharedPref(mActivity
                            , MCCPilotLogConst.AIRCRAFT_SORT_KEY, MCCPilotLogConst.AIRCRAFT_SORT_TYPE);*/
                    mDatabaseManager.updateSettingLocal("SortAircraft", String.valueOf(MCCPilotLogConst.AIRCRAFT_SORT_TYPE));
                }
                sortAircraftList();
                if (isTablet()) {
                    Bundle bundle = new Bundle();
                    if (mAircraftList.isEmpty()) {
                        bundle.putString(MCCPilotLogConst.AIRCRAFT_CODE_KEY, MCCPilotLogConst.STRING_EMPTY);
                    } else {
                        //database v5
                        //bundle.putString(MCCPilotLogConst.AIRCRAFT_CODE_KEY, mAircraftListAdapter.getItem(getCurrentIndexAfterSort()).getAircraftCode());
                    }
                    if (mViewType == MCCPilotLogConst.LIST_MODE) {
                        AircraftInfoFragment mAircraftInfoFragment = new AircraftInfoFragment();
                        Fragment fragment = getRightFragment();
                        if (fragment != null && !(fragment instanceof AircraftInfoEmptyFragment)) {
                            ((BaseMCCFragment) fragment)
                                    .replaceFragmentTablet(R.id.rightContainerFragment, mAircraftInfoFragment, bundle, FLAG_NOT_ADD_STACK);
                        }
                    }
                }
                break;
            case R.id.btn_bottom4: // Delete all aircraft
                deleteAllAircraft();
                break;
            case R.id.btnCancel: //Cancel search
                mIsSearch = false;
                if (mEdtSearch != null) {
                    mEdtSearch.setText(MCCPilotLogConst.STRING_EMPTY);
                    mEdtSearch.clearFocus();
                }
                mAircraftList.clear();
                mAircraftList.addAll(mAircraftCopy);
                mAircraftListAdapter.refreshAircraftListAdapter(mAircraftList);
                mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAircraftList.size());
                break;
            case R.id.ibMenu: // Open sliding menu
                if (mViewType == MCCPilotLogConst.SELECT_MODE) {
                    finishFragment();
                } else {
                    toggleMenu();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Method to sort list when bottom bar gets clicked
     */
    public void sortAircraftList() {
        if (mAircraftList == null) {
            return;
        }
        /*if (StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.AIRCRAFT_SORT_KEY
                , MCCPilotLogConst.AIRCRAFT_SORT_TYPE) == MCCPilotLogConst.AIRCRAFT_SORT_TYPE) {*/
        if (Integer.parseInt(mDatabaseManager.getSettingLocal("SortAircraft").getData()) == MCCPilotLogConst.AIRCRAFT_SORT_TYPE) {
            Collections.sort(mAircraftList, new Comparator<Aircraft>() {
                @Override
                public int compare(Aircraft aircraft1, Aircraft aircraft2) {
                    if (TextUtils.isEmpty(aircraft1.getModel())) {
                        aircraft1.setModel(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (TextUtils.isEmpty(aircraft1.getSubModel())) {
                        aircraft1.setSubModel(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (aircraft1.getDeviceCode() == null) {
                        aircraft1.setDeviceCode(0);
                    }
                    if (TextUtils.isEmpty(aircraft1.getReference())) {
                        aircraft1.setReference(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (TextUtils.isEmpty(aircraft2.getModel())) {
                        aircraft2.setModel(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (TextUtils.isEmpty(aircraft2.getSubModel())) {
                        aircraft2.setSubModel(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (aircraft2.getDeviceCode() == null) {
                        aircraft2.setDeviceCode(0);
                    }
                    if (TextUtils.isEmpty(aircraft2.getReference())) {
                        aircraft2.setReference(MCCPilotLogConst.STRING_EMPTY);
                    }

                    /*Sort: model, submodel, device, reference*/
                    int modelComparison = aircraft1.getModel().compareToIgnoreCase(aircraft2.getModel());
                    int variantComparison = aircraft1.getSubModel().compareToIgnoreCase(aircraft2.getSubModel());
                    int deviceComparison = aircraft1.getDeviceCode().compareTo(aircraft2.getDeviceCode());
                    int refComparison = aircraft1.getReference().compareToIgnoreCase(aircraft2.getReference());

                    return modelComparison == 0 ? variantComparison == 0 ? deviceComparison == 0 ? refComparison : deviceComparison : variantComparison : modelComparison;
                }
            });
            if (mFooterBtnSort != null) {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_type));
            }
        } else {
            Collections.sort(mAircraftList, new Comparator<Aircraft>() {
                @Override
                public int compare(Aircraft aircraft1, Aircraft aircraft2) {
                    if (aircraft1.getDeviceCode() == null) {
                        aircraft1.setDeviceCode(0);
                    }
                    if (TextUtils.isEmpty(aircraft1.getReference())) {
                        aircraft1.setReference(MCCPilotLogConst.STRING_EMPTY);
                    }
                    if (aircraft2.getDeviceCode() == null) {
                        aircraft2.setDeviceCode(0);
                    }
                    if (TextUtils.isEmpty(aircraft2.getReference())) {
                        aircraft2.setReference(MCCPilotLogConst.STRING_EMPTY);
                    }

                    /*Sort: device, reference*/
                    int deviceComparison = aircraft1.getDeviceCode().compareTo(aircraft2.getDeviceCode());
                    int refComparison = aircraft1.getReference().compareToIgnoreCase(aircraft2.getReference());

                    return deviceComparison == 0 ? refComparison : deviceComparison;
                }
            });
            if (mFooterBtnSort != null) {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_tail));
            }

        }
        mAircraftListAdapter.notifyDataSetChanged();

        if (isTablet() && !mIsFirstSort) {
            Fragment fragment = getRightFragment();
            if (mCurrentSelectAircraft != null && fragment != null && !(fragment instanceof AircraftInfoEmptyFragment)) {
                int newPosition = getCurrentIndexAfterSort();
                mAircraftListAdapter.removeAllItemSelected();
                mAircraftListAdapter.setSelectItem(newPosition);
                mLvAircraft.setSelection(newPosition);
            }
        }
        mIsFirstSort = false;

        mAircraftCopy.clear();
        mAircraftCopy.addAll(mAircraftList);
    }

    /**
     * Check if aircraft is logged on any flight
     *
     * @param flightPilots list flight
     * @param position     position to be deleted
     * @return true if aircraft is not logged on any flight, otherwise return false
     */
 /*   private boolean isAbleToDeleteAircraft(List<FlightPilot> flightPilots, int position) {
        for (int i = 0; i < flightPilots.size(); i++) {
            if (mAircraftList.get(position).getAircraftCode().equals(flightPilots.get(i).getAircraftCode())) {
                return false;
            }
        }
        return true;
    }*/

    /**
     * Restore view state of button sort
     * onResume()
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mAircraftList = populateAircraft();
        end();
        if (mFooterBtnSort == null) {
            return;
        }
        //if (StorageUtils.getIntFromSharedPref(mActivity, MCCPilotLogConst.AIRCRAFT_SORT_KEY, MCCPilotLogConst.AIRCRAFT_SORT_TYPE) == MCCPilotLogConst.AIRCRAFT_SORT_TYPE) {
        SettingLocal settingLocalSortAircraft = mDatabaseManager.getSettingLocal("SortAircraft");
        if (settingLocalSortAircraft != null) {
            String data = settingLocalSortAircraft.getData();
            if (!TextUtils.isEmpty(data) && Integer.parseInt(data) == MCCPilotLogConst.AIRCRAFT_SORT_TYPE) {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_type));
            } else {
                mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_tail));
            }
        }
    }

    /**
     * close swipe layout in case list view
     *
     * @param pSmooth smooth
     */
    private void closeSwipeView(boolean pSmooth) {
        if (mAircraftListAdapter != null && mAircraftListAdapter.getSwipeLayoutShown()) {
            mAircraftListAdapter.closeSwipe(pSmooth);
        }
    }

    /**
     * Refresh the aircraft info fragment for tablet
     */
    private void refreshAircraftInfo() {
        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            return;
        }
        if (isTablet()) {
            Bundle bundle = new Bundle();
            if (mAircraftList.isEmpty()) {
                bundle.putString(MCCPilotLogConst.AIRCRAFT_CODE_KEY, MCCPilotLogConst.STRING_EMPTY);
            } else {
                mAircraftListAdapter.setSelectItem(0);
                //database v5
                // mCurrentSelectAircraft = mAircraftListAdapter.getItem(0).getAircraftCode();
                bundle.putString(MCCPilotLogConst.AIRCRAFT_CODE_KEY, mCurrentSelectAircraft);
            }
            callAircraftInfoTablet(bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDelete(View pView, final Aircraft pModel, final int pIndex) {
        closeSwipeView(true);
        if (!mDatabaseManager.isAircraftUsedInFlight(pModel.getAircraftCode())) {
            String aircraftName = mAircraftList.get(pIndex).getReference();
            MccDialog.getOkDeleteAlertDialog(mActivity, R.string.delete_aircraft_dialog_title
                    , String.format(getString(R.string.delete_single_aircraft_confirm), aircraftName), new MccDialog.MCCDialogCallBack() {

                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                Aircraft aircraft = mAircraftList.get(pIndex);

                                byte[] aircraftCode = aircraft.getAircraftCode();
                                mDatabaseManager.deleteAircraft(aircraftCode);
                                mAircraftList.remove(pIndex);
                                refreshAdapter();
                                //Refresh copy
                                mAircraftCopy.clear();
                                mAircraftCopy.addAll(mAircraftList);
                                mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAircraftList.size());
                                MccDialog.getOkAlertDialog(mActivity, R.string.delete_aircraft_dialog_title, R.string.delete_aircraft_done).show();
                            }
                        }

                    }).show();

        } /*else {

            String aircraftName = mAircraftList.get(pIndex).getReference();
            MccDialog.getOkAlertDialog(mActivity, R.string.delete_aircraft_dialog_title, String.format(getString(R.string.delete_aircraft_denied), aircraftName)).show();
        }*/ else {
            String aircraftName = mAircraftList.get(pIndex).getReference();
            MccDialog.getDeActiveAlertDialogMagenta(mActivity, R.string.delete_aircraft_dialog_title,
                    String.format(getString(R.string.delete_aircraft_denied), aircraftName), new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                mAircraftList.get(pIndex).setActive(false);
                                mDatabaseManager.insertAircraft(mAircraftList.get(pIndex));
                                mAircraftList.remove(pIndex);
                                refreshAdapter();
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onDeActivate(View pView, Aircraft pModel,final int index) {
        closeSwipeView(true);
            MccDialog.getDeActiveAlertDialog(mActivity, R.string.de_active_aircraft_dialog_title
                    , String.format(getString(R.string.de_active_single_aircraft_confirm),  mAircraftList.get(index).getReference()),
                    new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                mAircraftList.get(index).setActive(false);
                                mDatabaseManager.insertAircraft(mAircraftList.get(index));
                                mAircraftList.remove(index);
                                refreshAdapter();
                                //refresh copy
                                mAircraftCopy.clear();
                                mAircraftCopy.addAll(mAircraftList);
                            }
                        }
                    }).show();


    }

    public void refreshAdapter() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAircraftListAdapter != null) {
                            mAircraftListAdapter.setSelectMode(mViewType == MCCPilotLogConst.SELECT_MODE);

                            mAircraftListAdapter.refreshAircraftListAdapter(mAircraftList);
                            setSizeAircraft();
                        }
                    }
                });
            }
        });

    }

    public void setSizeAircraft() {
        int size = 0;
        if (mAircraftList != null && !mAircraftList.isEmpty()) {
            size = mAircraftList.size();
        }
        mHeaderTvNumber.setText("" + size);
    }
}
