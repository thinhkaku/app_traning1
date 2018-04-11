package aero.pilotlog.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.widgets.ItemInputText;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.MccSwitch;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingGeneralFragment extends BaseMCCFragment implements IAsyncTaskCallback {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.item_default_currency)
    ItemInputTextWithIcon mItemCurrency;
    @Bind(R.id.item_landing_page)
    ItemInputTextWithIcon mItemLandingPage;
    @Bind(R.id.item_log_decimal)
    MccSwitch mItemLogDecimal;
    @Bind(R.id.item_log_accuracy)
    MccSwitch mItemLogAccuracy;
    @Bind(R.id.item_company_name)
    ItemInputText itemCompany;
    @Bind(R.id.item_flight_prefix)
    ItemInputText itemFlightPrefix;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.scrollView3)
    ScrollView scrollView;

    private LoadDataTask mLoadDataTask;

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_general));
        mIbLeft.setVisibility(View.VISIBLE);
        mIbMenu.setVisibility(View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
    }

    private void initView() {
        scrollView.setVisibility(View.VISIBLE);
        mItemCurrency.setIcon(R.drawable.ic_arrow);
        mItemLandingPage.setIcon(R.drawable.ic_arrow);
        mItemLandingPage.setVisibleLine(View.INVISIBLE);

        String logDecimal = DatabaseManager.getInstance(mActivity).getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
        if (logDecimal.equals("0")) {
            mItemLogAccuracy.setFootNoteRight("Clock Marks (5 minutes)");
            if (!mItemLogAccuracy.isChecked()) {
                mItemLogAccuracy.setTextFootNote("Highest Accuracy (1 minute)");
            } else {
                mItemLogAccuracy.setTextFootNote("Clock Marks (5 minutes)");
            }
        } else {
            mItemLogAccuracy.setFootNoteRight("Decimals (6 minutes)");
            if (!mItemLogAccuracy.isChecked()) {
                mItemLogAccuracy.setTextFootNote("Highest Accuracy (1 minute)");
            } else {
                mItemLogAccuracy.setTextFootNote("Decimals (6 minutes)");
            }
        }

        mItemLogDecimal.getRadioButtonLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemLogAccuracy.setFootNoteRight("Clock Marks (5 minutes)");
                if (!mItemLogAccuracy.isChecked()) {
                    mItemLogAccuracy.setTextFootNote("Highest Accuracy (1 minute)");
                } else {
                    mItemLogAccuracy.setTextFootNote("Clock Marks (5 minutes)");
                    DatabaseManager.getInstance(mActivity).updateSetting(MCCPilotLogConst.SETTING_CODE_ACCURACY, "1");
                }
            }
        });
        mItemLogDecimal.getRadioButtonRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemLogAccuracy.setFootNoteRight("Decimals (6 minutes)");
                if (!mItemLogAccuracy.isChecked()) {
                    mItemLogAccuracy.setTextFootNote("Highest Accuracy (1 minute)");
                } else {
                    mItemLogAccuracy.setTextFootNote("Decimals (6 minutes)");
                    DatabaseManager.getInstance(mActivity).updateSetting(MCCPilotLogConst.SETTING_CODE_ACCURACY, "2");
                }
            }
        });

        itemCompany.setTextFootNote("Name of your company, airline, flying club or training organisation");
        itemFlightPrefix.setTextFootNote("Airline Flight Number prefix (2 or 3 characters)");
        mItemCurrency.setFootNote("Default currency to log expenses and allowances");
        mItemLandingPage.setFootNote("The page you want this app to open on start");
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_general;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Nullable
    @OnClick({R.id.rlBackIcon})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUI() {
        initView();
    }

    @Override
    public void end() {

    }
}
