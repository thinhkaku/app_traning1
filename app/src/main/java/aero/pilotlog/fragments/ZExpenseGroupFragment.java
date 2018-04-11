package aero.pilotlog.fragments;


import android.os.AsyncTask;
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
import aero.pilotlog.adapters.ZExpenseGroupAdapter;
import aero.pilotlog.databases.entities.ZExpenseGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ZExpenseGroupFragment extends BaseMCCFragment implements IAsyncTaskCallback {
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
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private List<ZExpenseGroup> mList = new ArrayList<>();
    private List<ZExpenseGroup> mListCopy = new ArrayList<>();
    private ZExpenseGroupAdapter mAdapter;
    private boolean mIsSearch = true;


    public ZExpenseGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        mList = mDatabaseManager.getAllExpenseGroup();
        if (mList != null && mList.size() > 0)
            mListCopy.addAll(mList);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        mAdapter = new ZExpenseGroupAdapter(mActivity, mList);
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

        mTvTitle.setText("Select Expense Group");
        mIbMenu.setImageResource(R.drawable.ic_back);
        mHeaderRlSearch.setVisibility(View.GONE);
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnCancel})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu:
                finishFragment();
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
                    ZExpenseGroup zExpenseGroup = mList.get(pPosition);
                    expenseAddFragment.setGroup(zExpenseGroup);
                }
            });
            KeyboardUtils.hideKeyboard(mActivity);
            finishFragment();
        }
    }

}
