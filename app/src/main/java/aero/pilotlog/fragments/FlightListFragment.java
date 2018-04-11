package aero.pilotlog.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.FlightAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISwipeLayoutFlightCallback;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.KeyboardUtils;
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
import butterknife.OnItemLongClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightListFragment extends BaseMCCFragment implements IAsyncTaskCallback, ISwipeLayoutFlightCallback {
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

    private DatabaseManager mDatabaseManager;
    private List<FlightModel> mFlightList = new ArrayList<>();
    private List<FlightModel> mFlightListCopy = new ArrayList<>();
    private FlightAdapter mAdapter;
    private LoadDataTask mLoadDataTask;
    private Bundle mBundle;
    private int mSortType;
    private LinearLayout mCustomLayout;
    private PopupSelectionMenu mPopupSelectionMenu;

    public FlightListFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        //mDatabaseManager.updateAFCode();
        //mDatabaseManager.updatePilotCode();
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    public void initView() {
        mTvTitle.setText(R.string.text_flights);
        SettingLocal settingSort = mDatabaseManager.getSettingLocal(MCCPilotLogConst.SETTING_CODE_SORT_FLIGHT);
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
        return isTablet()?  R.layout.layout_bottom_bar_flight:
         R.layout.layout_bottom_bar_flight_phone;
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mFlightList.clear();
        mFlightList = mDatabaseManager.getFlightList(mSortType);
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
        final Bundle b = new Bundle();
        byte[] flightCode = mFlightList.get(pPosition).getFlightCode();
        b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_EDIT);
        b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, flightCode);
        replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
    }

    @Nullable
    @OnItemLongClick(R.id.listView)
    public boolean onItemLongClick(AdapterView<?> pAdapterView, View pView, final int pPosition, long pLong) {
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAdapter.isEnableClick()) {
                return true;
            }
        }
        showOptionMenuEdit(mFlightList.get(pPosition));

        return true;
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnDeleteAll, R.id.btnSort, R.id.btnAdd, R.id.btnSearch, R.id.tv_cancel, R.id.tv_search, R.id.btn_configure_shortcut, R.id.ll_search_logbook})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                KeyboardUtils.hideKeyboard(mActivity);
                toggleMenu();
                break;
            case R.id.btnAdd:
                if (mFlightList.size() == MCCPilotLogConst.FLIGHT_MAX_QUANTITY) {
                    MccDialog.getOkAlertDialog(mActivity, R.string.warning, R.string.flight_quantity_warning_message).show();
                    return;
                }
                final Bundle b = new Bundle();
                b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_ADD);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                break;

            //End PL-419
            case R.id.btn_configure_shortcut:
                KeyboardUtils.hideKeyboard(mActivity);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightConfigsFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.btnDeleteAll:
                showOptionDeleteAll();
                break;
            case R.id.btnSort:
                processSortFlight();
                break;
            default:
                break;
        }
    }

    public void processSortFlight() {
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
        mDatabaseManager.updateSettingLocal(MCCPilotLogConst.SETTING_CODE_SORT_FLIGHT, String.valueOf(mSortType));
        mFlightList = mDatabaseManager.getFlightList(mSortType);
        refreshAdapter();
        Fragment fragment = getRightFragment();
        if (isTablet() && mListView != null && mFlightList != null && !mFlightList.isEmpty() && fragment != null && !(fragment instanceof FlightAddEmptyFragment)) {
            mAdapter.removeAllItemSelected();
            int newPosition = getCurrentIndexAfterSort();
            mAdapter.setSelectItem(newPosition);
            mListView.setSelection(newPosition);
        }
    }
    private String mCurrentSelectFlight;
    private int getCurrentIndexAfterSort() {

        for (int size = mFlightList.size(), i = 0; i < size; i++) {
            if (mCurrentSelectFlight != null && mFlightList.get(i) != null && mFlightList.get(i).getFlightCode() != null && mCurrentSelectFlight.equals(mFlightList.get(i).getFlightCode())) {
                return i;
            }
        }

        return 0;
    }

    public void refreshListFlight() {
        if (mAdapter != null) {
            mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
            mLoadDataTask.execute();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    boolean result = false;

    private void showOptionDeleteAll() {
        mCustomLayout = (LinearLayout) safeInflater(getLayoutInflater(), null, R.layout.menu_option_delete);
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
                        confirm_content = getString(R.string.confirm_delete_all_flight_prior_content);
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
        if (isTablet()) {
            mPopupSelectionMenu.show(mCustomLayout, mLlBottomBar);
        } else {
            mPopupSelectionMenu.show(mCustomLayout, mRlBottomBar);
        }
    }

    public void refreshAdapter() {
        if (mAdapter != null) {
            mAdapter.refreshLogbookAdapter(mFlightList);
            setTextSizeLogbook();
        }
    }

    public void setTextSizeLogbook() {
        int size = 0;

        if (mFlightList != null && !mFlightList.isEmpty()) {
            size = mFlightList.size();
        }
        mTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + size);
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
        Calendar calendar = DateTimeUtils.getCalendar(pFlight.getFlightDateUTC().replace("-",""));
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

    private void showOptionMenuEdit(final FlightModel currentFlightModel) {
        mCustomLayout = (LinearLayout) safeInflater(getLayoutInflater(), null, R.layout.menu_option_edit_flight);
        LinearLayout lnReturn = (LinearLayout) mCustomLayout.findViewById(R.id.lnReturnFlight);
        LinearLayout lnNext = (LinearLayout) mCustomLayout.findViewById(R.id.lnNextFlight);
        if (lnReturn != null && currentFlightModel.getAircraftDeviceCode() == 1) {
            lnReturn.setVisibility(View.VISIBLE);
        }
        if (lnNext != null && currentFlightModel.getAircraftDeviceCode() == 1) {
            lnNext.setVisibility(View.VISIBLE);
        }
        PopupSelectionMenu.OnClickMenuListener onClickMenuListener = new PopupSelectionMenu.OnClickMenuListener() {
            @Override
            public void onClickMenu(int type) {
                if (currentFlightModel != null) {
                    final Bundle b;
                    switch (type) {
                        case MCCPilotLogConst.MENU_FLIGHT_EDIT:
                            b = new Bundle();
                            b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_EDIT);
                            b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, currentFlightModel.getFlightCode());
                            replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                            break;
                        case MCCPilotLogConst.MENU_FLIGHT_EDIT_PASTE:
                            b = new Bundle();
                            b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_PASTE_DATA);
                            b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, currentFlightModel.getFlightCode());
                            replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                            break;
                        case MCCPilotLogConst.MENU_FLIGHT_RETURN:
                            if (mDatabaseManager.getNumberOfFlights() == MCCPilotLogConst.FLIGHT_MAX_QUANTITY) {
                                MccDialog.getOkAlertDialog(mActivity, R.string.warning, R.string.flight_quantity_warning_message).show();
                                return;
                            }
                            b = new Bundle();
                            b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_RETURN);
                            b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, currentFlightModel.getFlightCode());
                            if (!isTablet()) {
                                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                            }
                            break;
                        case MCCPilotLogConst.MENU_FLIGHT_NEXT:
                            if (mDatabaseManager.getNumberOfFlights() == MCCPilotLogConst.FLIGHT_MAX_QUANTITY) {
                                MccDialog.getOkAlertDialog(mActivity, R.string.warning, R.string.flight_quantity_warning_message).show();
                                return;
                            }
                            b = new Bundle();
                            b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_NEXT);
                            b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, currentFlightModel.getFlightCode());
                            if (!isTablet()) {
                                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                            }
                            break;
                        case MCCPilotLogConst.MENU_FLIGHT_DELETE:

                            //PL-685
                            String deleteRecord;
                            String flightNumber = currentFlightModel.getFlightNumber();
                            String flightAirfield = currentFlightModel.getFlightAirfield();
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
                            Calendar calendar = DateTimeUtils.getCalendar(currentFlightModel.getFlightDateUTC());
                            String flightDate = DateTimeUtils.formatDateToString(calendar.getTime());
                            deleteRecord = deleteRecord + flightAirfield + " on " + flightDate;

                            MccDialog.getOkDeleteAlertDialog(mActivity, R.string.confirm_delete_flight_title
                                    , String.format(getString(R.string.confirm_delete_single_flight), deleteRecord), new MccDialog.MCCDialogCallBack() {

                                        @Override
                                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                                boolean result = mDatabaseManager.deleteFlight(currentFlightModel.getFlightCode());
                                                if (result) {
                                                    mFlightList.remove(currentFlightModel);
                                                    refreshAdapter();
                                                }
                                            }
                                        }
                                    }).show();
                            //End PL-685
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        mPopupSelectionMenu = new PopupSelectionMenu(mActivity);
        mPopupSelectionMenu.setOnClickMenuListenerOb(onClickMenuListener);
        if (isTablet()) {
            mPopupSelectionMenu.show(mCustomLayout, mLlBottomBar);
        } else {
            mPopupSelectionMenu.show(mCustomLayout, mRlBottomBar);
        }

    }


}
