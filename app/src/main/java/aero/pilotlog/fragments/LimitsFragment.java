package aero.pilotlog.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.FlightLimitAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.LimitRules;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LimitsFragment extends BaseMCCFragment implements IAsyncTaskCallback {
    @Bind(R.id.lvFlightLimit)
    ListView lvFlightLimit;
    @Bind(R.id.lvDutyLimit)
    ListView lvDutyLimit;
    @Bind(R.id.lvFlatRate)
    ListView lvFlatRate;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tvTitle)
    TextView mHeaderTvTitle;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.ln_message_deduct)
    LinearLayout lnMessageDeduct;

    private List<LimitRules> mAllLimitRules;
    private List<LimitRules> mFlightLimitList;
    private List<LimitRules> mDutyLimitList;
    private List<LimitRules> mFlatRateList;
    private FlightLimitAdapter mFlightLimitAdapter;
    private FlightLimitAdapter mDutyLimitAdapter;
    private FlightLimitAdapter mFlatRateAdapter;
    private DatabaseManager mDatabaseManager;
    private LoadDataTask mLoadDataTask;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_limits;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);

        initView();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initView() {
        /*Set header view*/
        if (mHeaderTvTitle != null) {
            mHeaderTvTitle.setText("Limits");
        }
        if(mDatabaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).getData().equals("1"))
            lnMessageDeduct.setVisibility(View.VISIBLE);
    }
    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        mAllLimitRules = mDatabaseManager.getAllLimitRules(getBaseOffset(Calendar.getInstance()));
        if(mAllLimitRules==null||mAllLimitRules.size()==0){
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MccDialog.getOkAlertDialog(mActivity, "Limits", getString(R.string.message_empty_limits)).show();
                    }
                });

        }else {
            mFlightLimitList = new ArrayList<>();
            mDutyLimitList = new ArrayList<>();
            mFlatRateList = new ArrayList<>();
            for (LimitRules limitRule: mAllLimitRules) {
                if(limitRule.getLType()==1)
                    mFlightLimitList.add(limitRule);
                else if(limitRule.getLType()==2){
                    mFlatRateList.add(limitRule);
                }else {
                    mDutyLimitList.add(limitRule);
                }
            }
        }
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void end() {
        if (mFlightLimitList != null) {
            mFlightLimitAdapter = new FlightLimitAdapter(mActivity, mFlightLimitList);
            lvFlightLimit.setAdapter(mFlightLimitAdapter);
            lvFlightLimit.setFastScrollEnabled(true);
        }

        if (mDutyLimitList != null) {
            mDutyLimitAdapter = new FlightLimitAdapter(mActivity, mDutyLimitList);
            lvDutyLimit.setAdapter(mDutyLimitAdapter);
            lvDutyLimit.setFastScrollEnabled(true);
        }

        if (mFlatRateList != null) {
            mFlatRateAdapter = new FlightLimitAdapter(mActivity, mFlatRateList);
            lvFlatRate.setAdapter(mFlatRateAdapter);
            lvFlatRate.setFastScrollEnabled(true);
        }

        setListViewHeightBasedOnChildren(lvFlightLimit);
        setListViewHeightBasedOnChildren(lvDutyLimit);
        setListViewHeightBasedOnChildren(lvFlatRate);

    }

    public int getBaseOffset(Calendar pFlightDate) {
        SettingConfig settingHomeBase = mDatabaseManager.getSetting(13);
        if (settingHomeBase != null) {
            Airfield airfield = mDatabaseManager.getAirfieldByICAOIATA(settingHomeBase.getData());
            if (airfield != null) {
                ZTimeZone zTimeZone = mDatabaseManager.getTimeZoneByCode(airfield.getTZCode());
                if (zTimeZone != null) {
                    java.util.TimeZone timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
                    Calendar c = Calendar.getInstance(timeZone);
                    long offsetMinute = Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, airfield.getTZCode(), pFlightDate.getTime());
                    return (int) offsetMinute;
                }
            }
        }
        return -9999;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



    @Nullable
    @OnClick({R.id.ibMenu,R.id.btn_insertFlightLimit,R.id.btn_insertDutyLimit,R.id.btn_insertFlatRate})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.ibMenu: // Open sliding menu
                toggleMenu();
                break;
            case R.id.btn_insertFlightLimit:
                mDatabaseManager.insertLimitRulesSample(1);
                mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
                mLoadDataTask.execute();
                break;
            case R.id.btn_insertDutyLimit:
                mDatabaseManager.insertLimitRulesSample(3);
                mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
                mLoadDataTask.execute();
                break;
            case R.id.btn_insertFlatRate:
                mDatabaseManager.insertLimitRulesSample(2);
                mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
                mLoadDataTask.execute();
                break;
        }
    }

}

