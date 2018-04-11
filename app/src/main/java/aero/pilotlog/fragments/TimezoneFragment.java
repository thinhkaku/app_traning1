package aero.pilotlog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;


import aero.pilotlog.adapters.TimezoneAdapter;
import aero.pilotlog.common.MCCPilotLogConst;

import aero.pilotlog.databases.entities.ZTimeZone;
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

public class TimezoneFragment extends BaseMCCFragment implements IAsyncTaskCallback {


    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.tvNumber)
    TextView mTvNumber;
    @Nullable
    @Bind(R.id.edtSearch)
    EditText mEdtSearch;
    @Bind(R.id.indexableListView)
    IndexableListView mIndexableListView;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.ln_tag_bar)
    LinearLayout lnTagBar;

    private List<ZTimeZone> mTimezoneList = new ArrayList<>();
    private List<ZTimeZone> mTimezoneListCopy = new ArrayList<>();
    private TimezoneAdapter mTimezoneAdapter;
    private boolean mIsSearch = true;
    private LoadDataTask mLoadDataTask;

    private String SELECT_LIST_TYPE = "";

    public TimezoneFragment() {
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
        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void closeFragment() {
        finishFragment();
    }

    private void initView() {
        mTvTitle.setText("Select Time Zone");
        mIbMenu.setImageResource(R.drawable.ic_back);

        KeyboardUtils.hideKeyboard(mActivity, mViewContainer);

    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.btnCancel})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.btnCancel:
                mIsSearch = false;
                cancelSearch(mEdtSearch);
                break;
            case R.id.ibMenu:
                closeFragment();
                break;
            default:
                break;
        }
    }

    @OnItemClick(R.id.indexableListView)
    public void onItemClick(AdapterView<?> pAdapterView, View pView,final int pPosition, long pLong) {
        Bundle bundle = getArguments();
        if (TextUtils.isEmpty(SELECT_LIST_TYPE) && bundle!=null)
            SELECT_LIST_TYPE = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_ADD);
        switch (SELECT_LIST_TYPE) {
            case MCCPilotLogConst.SELECT_LIST_TYPE_DUTY_ADD:
                final DutyAddFragment dutyAddFragment = (DutyAddFragment) getFragment(DutyAddFragment.class);
                if (dutyAddFragment != null) {
                    final boolean departure = bundle.getBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL);
                    dutyAddFragment.setTimeZone(mTimezoneList.get(pPosition), departure);
                    KeyboardUtils.hideKeyboard(mActivity);
                    finishFragment();
                }
                break;
            default:
                AirfieldAddsFragment airfieldAddsFragment = (AirfieldAddsFragment) getFragment(AirfieldAddsFragment.class);
                if (airfieldAddsFragment != null) {
                    airfieldAddsFragment.setTimeZone(mTimezoneList.get(pPosition));
                    KeyboardUtils.hideKeyboard(mActivity);
                    finishFragment();
                }
                break;
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

    public void doSearch (String pText) {
        String strSearch = pText.trim();
        mTimezoneList.clear();
        if(strSearch.length()>0){
            mTimezoneList = DatabaseManager.getInstance(mActivity).searchZTimezone(strSearch);
        } else {
            mTimezoneList.addAll(mTimezoneListCopy);
        }

        refreshAdapter();
    }
    public void cancelSearch(EditText pEditTextSearch) {
        pEditTextSearch.setText("");
        pEditTextSearch.clearFocus();
        mTimezoneList.clear();
        mTimezoneList.addAll(mTimezoneListCopy);
        refreshAdapter();
    }
    public  void refreshAdapter(){
        if(mTimezoneAdapter != null){
            mTimezoneAdapter.refreshAdapter(mTimezoneList);
        }
    }
    @Override
    protected void onKeyBackPress() {
        closeFragment();
        super.onKeyBackPress();
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mTimezoneList = DatabaseManager.getInstance(mActivity).getAllZTimezone();
    }

    @Override
    public void updateUI() {
        if (mTimezoneList != null && mTimezoneList.size() > 0 && mTimezoneList.get(0).getTZCode() == 0) {
            mTimezoneList.remove(0);
        }

        mTimezoneListCopy.clear();
        if (mTimezoneList != null && !mTimezoneList.isEmpty()) {
            mTimezoneListCopy.addAll(mTimezoneList);
        } else {
            mTimezoneList = new ArrayList<>();
        }

        mTimezoneAdapter = new TimezoneAdapter(mActivity,mTimezoneList);
        mIndexableListView.setDrawRightBar(true);
        mIndexableListView.setAdapter(mTimezoneAdapter);
        mIndexableListView.setFastScrollEnabled(true);
    }

    @Override
    public void end() {

    }
}
