package aero.pilotlog.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.DutyExpandAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.IOnClickDutyItemSearch;
import aero.pilotlog.interfaces.ISwipeLayoutDutyCallback;
import aero.pilotlog.models.DutyModels;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.widgets.MccDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DutyFragment extends BaseMCCFragment implements IAsyncTaskCallback, IOnClickDutyItemSearch, ISwipeLayoutDutyCallback {
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.btnSort)
    Button mBtnSort;


    private DatabaseManager mDatabaseManager;
    private LoadDataTask mLoadDataTask;
    private int mTypeScreen, mSortType;
    private Bundle mBundle;
    //private LogbookAdapter mLogbookAdapter;
    private DutyExpandAdapter mAdapter;

    private List<DutyModels> mList = new ArrayList<>();
    private List<DutyModels> mListCopy = new ArrayList<>();
    private HashMap<DutyModels, List<FlightModel>> mListChild = new HashMap<>();
    @Bind(R.id.expandable_list)
    ExpandableListView mListView;


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        mBundle = getArguments();
        return R.layout.layout_expandable_listview;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar_flight;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final Bundle b = new Bundle();
                byte[] flightCode = mListChild.get(mList.get(groupPosition)).get(childPosition).getFlightCode();
                b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_EDIT);
                b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, flightCode);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, FlightAddsFragment.class, b, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
                return false;
            }
        });
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                final Bundle b = new Bundle();
                byte[] dutyCode = mList.get(i).getDutyCode();
                b.putByteArray(MCCPilotLogConst.DUTY_CODE, dutyCode);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, DutyAddFragment.class, b, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
                return true;
            }
        });
    }

    public void refreshListDuty() {
        if (mAdapter != null) {
            mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
            mLoadDataTask.execute();
            mAdapter.notifyDataSetChanged();
        }
    }

    public void initView() {
        mTvTitle.setText(R.string.duty_title);

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
    public void start() {

    }

    @Override
    public void doWork() {
        //mFlightPilotList.clear();
        mList = mDatabaseManager.getDutyList(mSortType);
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                List<FlightModel> flightModels = mDatabaseManager.getDutyFlightList(0,
                        mList.get(i).getEventDateUTC(), mList.get(i).getEventStartUTC(), mList.get(i).getEventEndUTC());
                mListChild.put(mList.get(i), flightModels);
            }
        }

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        mListCopy.addAll(mList);
        mAdapter = new DutyExpandAdapter(mActivity, mList, mListChild);
        mAdapter.setOnClickDutyItemSearch(this);
        mAdapter.setSwipeLayoutCallBack(this);
        //mAdapter.setBaseMCCFragment(this);
        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    boolean result;
    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnAdd, R.id.btnSort, R.id.btnDeleteAll})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu: // Open sliding menu
                toggleMenu();
                break;
            case R.id.btnSort:
                processSortFlight();
                break;
            case R.id.btnAdd: //Add duty
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, DutyAddFragment.class, getArguments(), FLAG_ADD_STACK);
                break;
            case R.id.btnDeleteAll:
                MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.confirm_delete_all_duty_title,
                        getString(R.string.confirm_delete_all_duty_content), new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    result = mDatabaseManager.deleteAllDuties();
                                    if (result) {
                                        mList.clear();
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


    public void refreshAdapter() {
        if (mAdapter != null) {
            if (mList != null) {
                mAdapter.refreshAdapter(mList,mListChild);
            }
        }
    }

    @Override
    public void onKeyBackPress() {
        super.onKeyBackPress();
    }

    @Override
    public void onClickDutyItemSearch(int index) {
        if (mListView.isGroupExpanded(index)) {
            ((DutyModels) mAdapter.getGroup(index)).setSelected(false);
            mListView.collapseGroup(index);
        } else {
            ((DutyModels) mAdapter.getGroup(index)).setSelected(true);
            mListView.expandGroup(index);
        }
    }

    public void closeSwipeView(boolean pSmooth) {
        if (mAdapter != null && mAdapter.getSwipeLayoutShown()) {
            mAdapter.closeSwipe(pSmooth);
        }
    }

    @Override
    public void onDeleteRecord(View pView, final DutyModels duty, int pPosition) {
        closeSwipeView(true);
        MccDialog.getOkDeleteAlertDialog(mActivity, R.string.confirm_delete_duty_title
                , getString(R.string.confirm_delete_single_duty), new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                            mDatabaseManager.deleteDuty(duty.getDutyCode());
                            mList.remove(duty);
                            mListChild.remove(duty);
                            refreshAdapter();
                        }
                    }
                }).show();
    }

    @Override
    public void onDeleteChildRecord(View pView,final DutyModels duty, final FlightModel flightModel, int pPosition) {
        String deleteRecord;
        String flightNumber = flightModel.getFlightNumber();
        String flightAirfield = flightModel.getFlightAirfield();
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
        Calendar calendar = DateTimeUtils.getCalendar(flightModel.getFlightDateUTC());
        String flightDate = DateTimeUtils.formatDateToString(calendar.getTime());
        deleteRecord = deleteRecord + flightAirfield + " on " + flightDate;

        MccDialog.getOkDeleteAlertDialog(mActivity, R.string.confirm_delete_flight_title
                , String.format(getString(R.string.confirm_delete_single_flight), deleteRecord), new MccDialog.MCCDialogCallBack() {

                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                            boolean result = mDatabaseManager.deleteFlight(MCCPilotLogConst.LOGBOOK_DELETE_ONE, flightModel, FlightUtils.TimeMode.UTC);
                            if (result) {
                                //mList.remove(duty);
                                mListChild.get(duty).remove(flightModel);
                                refreshAdapter();
                            }
                        }
                    }
                }).show();
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
        mList = mDatabaseManager.getDutyList(mSortType);
        refreshAdapter();
        Fragment fragment = getRightFragment();
       /* if (isTablet() && mListView != null && mList != null && !mList.isEmpty() && fragment != null && !(fragment instanceof ExpenseAddFragment)) {
            mAdapter.removeAllItemSelected();
            int newPosition = getCurrentIndexAfterSort();
            mAdapter.setSelectItem(newPosition);
            mIndexableListView.setSelection(newPosition);
        }*/
    }
}
