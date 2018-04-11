package aero.pilotlog.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.ExpenseAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Expense;
import aero.pilotlog.databases.entities.SettingLocal;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.ISwipeLayoutExpenseCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.widgets.IndexableListView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseListFragment extends BaseMCCFragment implements IAsyncTaskCallback, ISwipeLayoutExpenseCallback {
    @Bind(R.id.indexableListView)
    IndexableListView mListView;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mHeaderTvTitle;
    @Bind(R.id.tvNumber)
    TextView mHeaderTvNumber;
    @Nullable
    @Bind(R.id.btnAdd)
    Button mFooterBtnAdd;
    @Bind(R.id.btnSort)
    Button mBtnSort;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;

    private List<Expense> mList;
    private List<Expense> mListCopy;
    //private int mViewType = MCCPilotLogConst.LIST_MODE;
    //For side index scroll
    private ExpenseAdapter mAdapter;
    private int mSortType;
    //private DatabaseManager mDatabaseManager;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private boolean mIsFirstSort;
    private LoadDataTask mLoadDataTask;

    /**
     * Constructor
     */
    public ExpenseListFragment() {
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_index_listview;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_bottom_bar_flight;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManagerV5 = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity);
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    /**
     * Initialize views
     */
    private void initView() {
        /*Set header view*/
        if (mHeaderTvTitle != null) {
            mHeaderTvTitle.setText("Expenses");
        }
        SettingLocal settingSort = mDatabaseManagerV5.getSettingLocal(MCCPilotLogConst.SETTING_CODE_SORT_FLIGHT);
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
        /*Set footer view*/
       /* if (mFooterBtnAdd != null && mFooterBtnSort != null && mFooterBtnDelete != null && mFooterBtnSelect != null && mFooterBtnImportContact != null) {
            mFooterBtnAdd.setVisibility(View.VISIBLE);
            mFooterBtnSort.setText(getString(R.string.sort_aircraft_list_type));
            mFooterBtnSort.setVisibility(View.VISIBLE);
            mFooterBtnDelete.setVisibility(View.VISIBLE);
            mFooterBtnSelect.setVisibility(View.GONE);
            mrlBottom2.setVisibility(View.GONE);
            mFooterBtnImportContact.setVisibility(View.GONE);
            mrlBottom5.setVisibility(View.GONE);
        }
        mLnBottomBar.setWeightSum(3f);*/
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mListCopy = new ArrayList<>();
        mList = mDatabaseManagerV5.getAllExpense(mSortType);
    }

    @Override
    public void updateUI() {
        if (mList != null && mList.isEmpty()) {
            mListCopy.addAll(mList);
        }

    }

    @Override
    public void end() {
        if (mList == null) {
            return;
        }
        mAdapter = new ExpenseAdapter(mActivity, mList);
        mAdapter.setSwipeLayoutCallBack(this);
        /*Handle side index scroll*/
        mListView.setAdapter(mAdapter);
        mListView.setDrawRightBar(false); /*Draw right bar containing characters of list view*/
        mListView.setFastScrollEnabled(true);

        mHeaderTvNumber.setText(MCCPilotLogConst.STRING_EMPTY + mAdapter.getCount());
    }


    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterViewParent, View pView, final int position, long id) {
        if (pView instanceof SwipeLayout) {
            final SwipeLayout swipeLayout = (SwipeLayout) pView;
            if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open || !mAdapter.isEnableClick()) {
                return;
            }
        }
        final Bundle b = new Bundle();
        byte[] expCode = mList.get(position).getExpCode();
        b.putByteArray(MCCPilotLogConst.EXPENSE_CODE, expCode);
        replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, ExpenseAddFragment.class, b, isTablet()?FLAG_NOT_ADD_STACK : FLAG_ADD_STACK);
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

    boolean result = false;
    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnAdd, R.id.btnSort, R.id.btnDeleteAll})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btnAdd:
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, ExpenseAddFragment.class, null, FLAG_ADD_STACK);
                break;
            case R.id.btnDeleteAll:
                MccDialog.getOkDeleteAllAlertDialog(mActivity, R.string.confirm_delete_all_expense_title,
                        getString(R.string.confirm_delete_all_expense_content), new MccDialog.MCCDialogCallBack() {
                            @Override
                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                    result = mDatabaseManagerV5.deleteAllExpenses();
                                    if (result) {
                                        mList.clear();
                                        refreshAdapter();
                                    }
                                }
                            }
                        }).show();
                break;
            case R.id.ibMenu: // Open sliding menu
                toggleMenu();
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
        mDatabaseManagerV5.updateSettingLocal(MCCPilotLogConst.SETTING_CODE_SORT_FLIGHT, String.valueOf(mSortType));
        mList = mDatabaseManagerV5.getAllExpense(mSortType);
        refreshAdapter();
        Fragment fragment = getRightFragment();
       /* if (isTablet() && mListView != null && mList != null && !mList.isEmpty() && fragment != null && !(fragment instanceof ExpenseAddFragment)) {
            mAdapter.removeAllItemSelected();
            int newPosition = getCurrentIndexAfterSort();
            mAdapter.setSelectItem(newPosition);
            mIndexableListView.setSelection(newPosition);
        }*/
    }

    private String mCurrentSelectFlight;

    private int getCurrentIndexAfterSort() {

        for (int size = mList.size(), i = 0; i < size; i++) {
            if (mCurrentSelectFlight != null && mList.get(i) != null && mList.get(i).getExpCode() != null && mCurrentSelectFlight.equals(mList.get(i).getExpCode())) {
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

    public void refreshAdapter() {
        if (mAdapter != null) {
            mAdapter.refreshAdapter(mList);
        }
    }


    private void closeSwipeView(boolean pSmooth) {
        if (mAdapter != null && mAdapter.getSwipeLayoutShown()) {
            mAdapter.closeSwipe(pSmooth);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refreshList() {
        if (mAdapter != null) {
            mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
            mLoadDataTask.execute();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteRecord(View pView, final Expense expense, int pPosition) {
        closeSwipeView(true);
        MccDialog.getOkDeleteAlertDialog(mActivity, R.string.confirm_delete_expense_title,
                R.string.confirm_delete_expense_content, new MccDialog.MCCDialogCallBack() {

                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                            mDatabaseManagerV5.deleteExpense(expense.getExpCode());
                            mList.remove(expense);
                            refreshAdapter();
                        }
                    }
                }).show();
    }

    @Override
    protected void onKeyBackPress() {
        if(mAdapter.isOpenSwipe()){
            mAdapter.closeSwipe(true);
        }else {
            super.onKeyBackPress();
        }

    }
}
