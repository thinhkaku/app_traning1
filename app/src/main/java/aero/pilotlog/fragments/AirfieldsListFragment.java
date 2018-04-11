package aero.pilotlog.fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.AirfieldsAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.SettingsConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISwipeLayoutAirfieldCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.text.ParseException;
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
 * A simple {@link Fragment} subclass.
 */
public class AirfieldsListFragment extends BaseMCCFragment implements ISwipeLayoutAirfieldCallback, IAsyncTaskCallback {

    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.indexableListView)
    IndexableListView mIndexableListView;
    @Nullable
    @Bind(R.id.btn_bottom2)
    Button mButtonBottom2;
    @Nullable
    @Bind(R.id.btn_bottom3)
    Button mButtonBottom3;
    @Nullable
    @Bind(R.id.btn_bottom4)
    Button mButtonBottom4;
    @Nullable
    @Bind(R.id.btn_bottom5)
    Button mButtonBottom5;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;

    private int mSortType = MCCPilotLogConst.SORT_BY_AIRFIELD_NAME, mSortTypeCurrent;
    private String mDisplayType = SettingsConst.SETTING_IATA;
    private LoadDataTask mLoadDataTask;
    private int mViewType = MCCPilotLogConst.LIST_MODE;
    private DatabaseManager mDatabaseManager;
    private List<Airfield> mModelList = new ArrayList<>();
    private List<Airfield> mModelListCopy = new ArrayList<>();
    private AirfieldsAdapter mAirfieldAdapter;
    //private boolean mIsSearch = true;
    private byte[] mCurrentAirfield = null;

    private String SELECT_LIST_TYPE = "";

    public void setViewType(int pViewType) {
        this.mViewType = pViewType;
        //refreshAdapter();
    }

    public AirfieldsListFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;

    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_delay_tails;
    }

    @Override
    protected int getFooterResId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mViewType = bundle.getInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE);
            SELECT_LIST_TYPE = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO);
        }

        return R.layout.layout_bottom_bar;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(AirfieldsListFragment.this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, AirfieldsListFragment.this, mLlLoading);
        mLoadDataTask.execute();

    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mModelList = mDatabaseManager.getAllAirfieldFavorite();
        mModelListCopy.clear();
        if (mModelList != null && !mModelList.isEmpty()) {
            sortAirfield(mSortType, mModelList);
            mModelListCopy.addAll(mModelList);
        }


    }

    @Override
    public void updateUI() {
    }

    @Override
    public void end() {
        setTextSizeAirfield();
        mAirfieldAdapter = new AirfieldsAdapter(mActivity, mModelList);
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
        mAirfieldAdapter.setISwipeLayoutCallback(this);
        mAirfieldAdapter.setSortType(mSortType);
        mAirfieldAdapter.setDisplayType(mDisplayType);
        mAirfieldAdapter.setTypeAdapter(MCCPilotLogConst.AIRFIELD_ADAPTER);
        mAirfieldAdapter.setSelectMode(mViewType == MCCPilotLogConst.SELECT_MODE);
        mIndexableListView.setAdapter(mAirfieldAdapter);
        mIndexableListView.setFastScrollEnabled(true);
    }

    public void initView() {
        //Top bar
        mTvTitle.setText(SELECT_LIST_TYPE != MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO ? "Select Airfield" : getString(R.string.text_airfields_camelcase));
        mIbLeft.setVisibility(SELECT_LIST_TYPE == MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO /*|| isTablet()*/ ? View.GONE : View.VISIBLE);
        mIbMenu.setVisibility(SELECT_LIST_TYPE == MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO ? View.VISIBLE : View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }

        //Bottom bar
        if (mButtonBottom2 != null && mButtonBottom5 != null) {
            mButtonBottom2.setBackgroundResource(R.drawable.ic_nav);
            mButtonBottom5.setVisibility(View.GONE);
        }
        SettingConfig setting = mDatabaseManager.getSetting("IATA");
        if (setting != null) {
            mDisplayType = setting.getData();
        }
        mSortType = mSortTypeCurrent = Integer.parseInt(mDatabaseManager.getSettingLocal("SortAirfield").getData());
        if (mButtonBottom3 != null) {
            switch (mSortType) {
                case MCCPilotLogConst.SORT_BY_AIRFIELD_NAME:
                    mButtonBottom3.setText(getString(R.string.text_name));
                    break;
                case MCCPilotLogConst.SORT_BY_AIRFIELD_IATA:
                    mButtonBottom3.setText(getString(R.string.text_iata));
                    break;
                case MCCPilotLogConst.SORT_BY_AIRFIELD_ICAO:
                    mButtonBottom3.setText(getString(R.string.text_icao));
                    break;
                default:
                    break;
            }
        }
        KeyboardUtils.hideKeyboardWhenClickLoupe(mEdtSearch, getActivity());
        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);
        if (mViewType == MCCPilotLogConst.SELECT_MODE) {
            mButtonBottom2.setVisibility(View.GONE);
            mButtonBottom3.setVisibility(View.GONE);
            if (mButtonBottom4 != null) {
                mButtonBottom4.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onDelete(View pView,final Airfield pModel,final int pIndex) {
        closeSwipeView(true);
        if (mDatabaseManager.isAirfieldUsedInFlight(pModel.getAFCode())) {
            //PL-685
            String airfieldName = pModel.getAFName();
            MccDialog.getOkAlertDialog(mActivity, R.string.msg_title_delete_airfield, String.format(getString(R.string.msg_cannot_delete_airfield), airfieldName),
                     new MccDialog.MCCDialogCallBack() {

                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            pModel.setShowList(false);
                            mDatabaseManager.insertOrUpdateAirfield(pModel);
                            mModelList.remove(pModel);
                            mAirfieldAdapter.refreshAirfieldAdapter(mModelList);
                        }
                    }).show();

        } else {
            String airfieldName = pModel.getAFName();
            MccDialog.getOkDeleteAlertDialog(mActivity, R.string.msg_title_delete_airfield
                    , String.format(getString(R.string.delete_single_airfield_confirm), airfieldName), new MccDialog.MCCDialogCallBack() {

                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                mDatabaseManager.deleteAirfield(pModel.getAFCode());
                                mModelList.remove(pModel);
                                //iAirfieldDeletedCallback.callBack();
                                refreshAdapter();
                                MccDialog.getOkAlertDialog(mActivity, R.string.msg_title_delete_airfield, R.string.delete_airfield_done).show();
                            }
                        }
                    }).show();
        }
    }


    public void sortAirfield(final int pType, List<Airfield> list) {
        Collections.sort(list, new Comparator<Airfield>() {
            @Override
            public int compare(Airfield leftItem, Airfield rightItem) {
                switch (pType) {
                    case MCCPilotLogConst.SORT_BY_AIRFIELD_NAME:
                        String strNameLeft = "", strNameRight = "";
                        if (leftItem != null) {
                            strNameLeft = leftItem.getAFName();
                        }
                        if (rightItem != null) {
                            strNameRight = rightItem.getAFName();
                        }

                        return strNameLeft.compareToIgnoreCase(strNameRight);
                    case MCCPilotLogConst.SORT_BY_AIRFIELD_IATA:
                        String strIataLeft = null, strIataRight = null;
                        if (leftItem != null) {
                            strIataLeft = leftItem.getAFIATA();
                        }
                        if (rightItem != null) {
                            strIataRight = rightItem.getAFIATA();
                        }

                        if (TextUtils.isEmpty(strIataLeft) && strIataRight != null) {
                            return STRING_EMPTY.compareToIgnoreCase(strIataRight);
                        } else if (TextUtils.isEmpty(strIataRight) && strIataLeft != null) {
                            return leftItem.getAFIATA().compareToIgnoreCase(STRING_EMPTY);
                        } else if (strIataLeft != null) {
                            return strIataLeft.compareToIgnoreCase(strIataRight);
                        }
                        return 0;
                    case MCCPilotLogConst.SORT_BY_AIRFIELD_ICAO:
                        int resultICAO = 0;
                        if (leftItem.getAFICAO() != null && rightItem.getAFICAO() != null) {
                            resultICAO = leftItem.getAFICAO().compareToIgnoreCase(rightItem.getAFICAO());
                        }
                        if (resultICAO != 0) {
                            return resultICAO;
                        }
                        if (!TextUtils.isEmpty(leftItem.getAFIATA()) && !TextUtils.isEmpty(rightItem.getAFIATA())) {
                            return leftItem.getAFIATA().compareToIgnoreCase(rightItem.getAFIATA());
                        }
                        return 0;
                    default:
                        return 0;
                }
            }
        });
    }

    public void setTextSizeAirfield() {
        int size = 0;
        if (mModelList != null && !mModelList.isEmpty()) {
            size = mModelList.size();
        }
        if (isSearch & size > 1) size = size - 2;
        mTvNumber.setText("" + size);
    }

    public void closeSwipeView(boolean pSmooth) {
        if (mAirfieldAdapter != null && mAirfieldAdapter.getSwipeLayoutShown()) {
            mAirfieldAdapter.closeSwipe(pSmooth);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if (!isTablet()) {
            mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
            mLoadDataTask.execute();
        }*/
    }

    @Nullable
    @OnClick({R.id.rlBackIcon, R.id.ibMenu, R.id.btnCancel, R.id.btn_bottom1, R.id.btn_bottom2, R.id.btn_bottom3, R.id.btn_bottom4})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                if (mViewType == MCCPilotLogConst.SELECT_MODE /*&& !isTablet()*/) {
                    finishFragment();
                }
                break;
            case R.id.ibMenu:
                toggleMenu();
                break;
            case R.id.btnCancel:
                //cancelSearch();
                mEdtSearch.setText("");
                break;
            case R.id.btn_bottom1:
                //callAddAirfield();
                MccDialog.getAlertDialog(mActivity,  R.string.add_airfield
                        ,R.string.airfield_confirm_add
                        , R.string.alert_search_button
                        , R.string.alert_create_new_button,-1
                        , new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    mEdtSearch.requestFocus();
                                    KeyboardUtils.showKeyboard(mActivity);
                                }else {
                                    replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldAddsFragment.class, FLAG_ADD_STACK);
                                }
                            }
                        }).show();

                break;
            case R.id.btn_bottom2:
                callNavigationFragment();
                break;
            case R.id.btn_bottom3:
                if (!isSearch)
                    processSort(mModelList);
                else {
                    createFavoriteListAndNotFavoriteList(mEdtSearch.getText().toString());
                    processSort(listFavorite);
                    processSort(listNotFavorite);
                    createSearchList();
                }
                updateHighLightPos();
                break;
            case R.id.btn_bottom4:
                deleteAll();
                break;
            default:
                break;
        }
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, final int pPosition, long pLong) {
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAirfieldAdapter.isEnableClick()) {
                return;
            }
        }
        if (mModelList.get(pPosition).getAFCode() == null) return;
        Bundle bundle = getArguments();
        if (bundle == null) return;
        if (TextUtils.isEmpty(SELECT_LIST_TYPE))
            SELECT_LIST_TYPE = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO);
       /* if (isTablet()) {
            Fragment fragment = getRightFragment();
            switch (SELECT_LIST_TYPE) {
                case MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO:
                    bundle = new Bundle();
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mModelList.get(pPosition).getAFCode());
                    //((AirfieldsInfoFragment) fragment).setAirfieldInfo(bundle);
                    mCurrentAirfield = mModelList.get(pPosition).getAFCode();
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_WEATHER:
                    ((AirfieldMetarFragment) fragment).setSelectedAirfield(mModelList.get(pPosition));
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_NAVIGATION:
                    ((AirfieldNavigationFragment) fragment).setSelectedAirfield(mModelList.get(pPosition));
                    break;

            }
        } else {*/
            switch (SELECT_LIST_TYPE) {
                case MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_INFO:
                    bundle = new Bundle();
                    bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, mModelList.get(pPosition).getAFCode());
                    replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldsInfoFragment.class, bundle, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_WEATHER:
                    AirfieldMetarFragment fragmentMetar = (AirfieldMetarFragment) getFragment(AirfieldMetarFragment.class);
                    if (fragmentMetar != null) {
                        fragmentMetar.setSelectedAirfield(mModelList.get(pPosition));
                        KeyboardUtils.hideKeyboard(mActivity);
                        finishFragment();
                    }
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_NAVIGATION:
                    AirfieldNavigationFragment fragmentNavigation = (AirfieldNavigationFragment) getFragment(AirfieldNavigationFragment.class);
                    if (fragmentNavigation != null) {
                        fragmentNavigation.setSelectedAirfield(mModelList.get(pPosition));
                        KeyboardUtils.hideKeyboard(mActivity);
                        finishFragment();
                    }
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_LOGGING_HOME_BASE:
                    SettingFlightLoggingFragment settingFlightLoggingFragment = (SettingFlightLoggingFragment) getFragment(SettingFlightLoggingFragment.class);
                    if (settingFlightLoggingFragment != null) {
                        settingFlightLoggingFragment.setSelectedAirfield(mModelList.get(pPosition));
                        KeyboardUtils.hideKeyboard(mActivity);
                        finishFragment();
                    }
                    break;
                case MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_ADD:
                    final FlightAddsFragment flightAddsFragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);

                    if (flightAddsFragment != null) {
                        final boolean departure = bundle.getBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL);
                        KeyboardUtils.hideKeyboard(mActivity);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                flightAddsFragment.setAirfield(mModelList.get(pPosition), departure, false);
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
                                logbooksListFragment.setAirfieldFilter(mModelList.get(pPosition).getAFName());
                            }
                        });

                        finishFragment();
                    }
                    break;
            }
        //}
    }


/*    public void cancelSearch() {
        mIsSearch = false;
        mEdtSearch.setText("");
        mEdtSearch.clearFocus();
        mModelList.clear();
        showCompleteListAgain();
        refreshAdapter();
        updateHighLightPos();
    }*/

    public void callNavigationFragment() {
        if (isTablet()) {
            Fragment fragment = getRightFragment();
            if (fragment == null || !(fragment instanceof AirfieldNavigationFragment)) {
                if (checkIsAirfieldInfoFragment(fragment)) {
                    finishFragment();
                }
                replaceFragmentTablet(R.id.rightContainerFragment, AirfieldNavigationFragment.class, FLAG_ADD_STACK);
            }
        } else {
            replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldNavigationFragment.class, FLAG_ADD_STACK);
        }
    }

    private boolean checkIsAirfieldInfoFragment(Fragment pFragment) {
        return pFragment != null && !(pFragment instanceof AirfieldsInfoFragment);
    }

    public void processSort(List<Airfield> list) {
        if (mButtonBottom3 != null) {
            switch (mSortType) {
                case MCCPilotLogConst.SORT_BY_AIRFIELD_NAME:
                    //If sort current as by airfield name, then when click button sort, switch to sort by airfield aficao
                    mSortType = MCCPilotLogConst.SORT_BY_AIRFIELD_ICAO;
                    mButtonBottom3.setText(getString(R.string.text_icao));
                    break;
                case MCCPilotLogConst.SORT_BY_AIRFIELD_IATA:
                    //If sort current as by airfield afiata, then when click button sort, switch to sort by airfield name
                    mSortType = MCCPilotLogConst.SORT_BY_AIRFIELD_NAME;
                    mButtonBottom3.setText(getString(R.string.text_name));
                    break;
                case MCCPilotLogConst.SORT_BY_AIRFIELD_ICAO:
                    //If sort current as by airfield aficao, then when click button sort, switch to sort by airfield afiata
                    mSortType = MCCPilotLogConst.SORT_BY_AIRFIELD_IATA;
                    mButtonBottom3.setText(getString(R.string.text_iata));
                    break;
                default:
                    break;
            }
            mDatabaseManager.updateSettingLocal("SortAirfield", String.valueOf(mSortType));
            sortAirfield(mSortType, list);
            refreshAdapter();
        }
    }

    private void updateHighLightPos() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isTablet()) {
                            Fragment fragment = getRightFragment();
                            if (mAirfieldAdapter != null && mModelList != null && !mModelList.isEmpty() && fragment != null && !(fragment instanceof AirfieldEmptyFragment)) {
                                mAirfieldAdapter.removeAllItemSelected();
                                int newPosition = getCurrentIndexAfterSort();
                                mAirfieldAdapter.setSelectItem(newPosition);
                                mIndexableListView.setSelection(newPosition);
                            }
                        }
                    }
                });
            }
        });
    }

    public void refreshAdapter() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAirfieldAdapter != null) {
                            mAirfieldAdapter.setSortType(mSortType);
                            mAirfieldAdapter.setDisplayType(mDisplayType);

                            mAirfieldAdapter.setSelectMode(mViewType == MCCPilotLogConst.SELECT_MODE);

                            mAirfieldAdapter.refreshAirfieldAdapter(mModelList);
                            setTextSizeAirfield();
                        }
                    }
                });
            }
        });

    }

    private int getCurrentIndexAfterSort() {
        if (mModelList != null) {
            for (int size = mModelList.size(), i = 0; i < size; i++) {
                Airfield airfieldPilot = mModelList.get(i);
                if (mCurrentAirfield != null && airfieldPilot != null && airfieldPilot.getAFCode() != null && mCurrentAirfield.equals(airfieldPilot.getAFCode())) {
                    return i;
                }
            }
        }

        return 0;
    }

    List<Airfield> listFavorite;
    List<Airfield> listNotFavorite;
    boolean isSearch = false;

    @OnTextChanged(R.id.edtSearch)
    public void onTextChanged(CharSequence pText) {
        //if (pText.length() > 0) {
        if (!TextUtils.isEmpty(pText)) {
            final String strSearch = pText.toString().trim();
            if (strSearch.length() > 2) {
                isSearch = true;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createFavoriteListAndNotFavoriteList(strSearch);
                                createSearchList();
                            }
                        });
                    }
                });
            }
          /*  else {
                isSearch = false;
                mModelList.clear();
                showCompleteListAgain();
                refreshAdapter();
            }*/
        } else {
            isSearch = false;
            mModelList.clear();
            showCompleteListAgain();
            refreshAdapter();
        }
        updateHighLightPos();
    }

    private void createFavoriteListAndNotFavoriteList(String strSearch) {
        listFavorite = mDatabaseManager.getAirfieldsByICAOIATAOrAFName(strSearch, true);
        listNotFavorite = mDatabaseManager.getAirfieldsByICAOIATAOrAFName(strSearch, false);
    }

    private void createSearchList() {
        mModelList.clear();
        Airfield airfieldFavoriteHeader = new Airfield();
        airfieldFavoriteHeader.setAFName("My Favorite Airfields");
        mModelList.add(airfieldFavoriteHeader);
        mModelList.addAll(listFavorite);
        Airfield otherAirfieldHeader = new Airfield();
        otherAirfieldHeader.setAFName("Other Airfields");
        mModelList.add(otherAirfieldHeader);
        mModelList.addAll(listNotFavorite);
        refreshAdapter();
    }

    public void showCompleteListAgain() {
        mModelList.addAll(mModelListCopy);
        if (mSortType != mSortTypeCurrent) {
            sortAirfield(mSortType, mModelList);
        }
    }

    public void deleteAll() {
        if (mModelList != null && !mModelList.isEmpty()) {
            if (mDatabaseManager.getSizeFlight() > 0) {
                MccDialog.getOkAlertDialog(mActivity, R.string.msg_title_delete_airfield, R.string.msg_not_allow_delete_all_airfield).show();
            } else {
                MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.msg_title_delete_airfield, R.string.msg_allow_delete_all_airfield
                        , new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    mDatabaseManager.deleteAllAirfields();
                                    mModelList.clear();
                                    mModelListCopy.clear();
                                    refreshAdapter();
                                }
                            }
                        }).show();
            }
        }
    }

}
