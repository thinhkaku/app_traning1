package aero.pilotlog.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.ZExpenseAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZExpense;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.IndexableListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZExpenseFragment extends BaseMCCFragment implements IAsyncTaskCallback {
    @Bind(R.id.indexableListView)
    IndexableListView mListView;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Nullable
    @Bind(R.id.rlSearchBar)
    RelativeLayout mHeaderRlSearch;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mHeaderTvNumber;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;

    private DatabaseManager mDatabaseManager;
    private List<ZExpense> mList = new ArrayList<>();
    private List<ZExpense> mListCopy = new ArrayList<>();
    private ZExpenseAdapter mAdapter;
    private boolean mIsSearch = true;
    private int groupCode;


    public ZExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        Bundle bundle = getArguments();
        if (bundle == null) return;
        groupCode = bundle.getInt(MCCPilotLogConst.EXPENSE_GROUP_ID);
        mList = mDatabaseManager.getAllZExpenseByGroup(groupCode);
        if (mList != null && mList.size() > 0)
            mListCopy.addAll(mList);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        mAdapter = new ZExpenseAdapter(mActivity, mList);
        mListView.setDrawRightBar(true);//Draw right bar contain characters of list view
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);
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
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        //int data
        initView();
        LoadDataTask mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    public void initView() {

        mTvTitle.setText("Select Expense Type");
        mIbMenu.setImageResource(R.drawable.ic_back);
        //mHeaderRlSearch.setVisibility(View.GONE);
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnCancel})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                finishFragment();
                break;
            case R.id.btnCancel:
                cancelSearch(mEdtSearch);
                break;
        }
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView, final int pPosition, long pLong) {

        final ExpenseAddFragment expenseAddFragment = (ExpenseAddFragment) getFragment(ExpenseAddFragment.class);
        if (expenseAddFragment != null) {
            KeyboardUtils.hideKeyboard(mActivity);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    ZExpense zExpense = mList.get(pPosition);
                    expenseAddFragment.setTypeExpense(zExpense);
                }
            });
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
        }
    }

    @Nullable
    @OnTextChanged(R.id.edtSearch)
    public void onTextChanged(CharSequence pText) {
        if (mIsSearch) {
            doSearch(pText.toString());
        }
        mIsSearch = true;
    }

    public void doSearch(String pText) {
        String strSearch = pText.trim();
        mList.clear();
        if (strSearch.length() > 0) {
            mList = mDatabaseManager.searchZExpenseByGroupCode(groupCode, strSearch);
        } else {
            mList.addAll(mListCopy);
        }
        refreshAdapter();
    }

    /**
     * Process cancel search
     *
     * @param pEditTextSearch
     */
    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mList.clear();
        mList.addAll(mListCopy);

        refreshAdapter();
    }

    /**
     * Refresh delay list or tails list
     */
    public void refreshAdapter() {
        if (mAdapter != null) {
            mAdapter.refreshAdapter(mList);
        }
    }

}
