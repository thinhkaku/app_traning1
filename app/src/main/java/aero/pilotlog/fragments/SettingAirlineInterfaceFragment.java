package aero.pilotlog.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.adapters.SettingGeneralAdapter;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.models.SettingGeneralModel;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingAirlineInterfaceFragment extends BaseMCCFragment {
    private SettingGeneralAdapter mAdapter;
    @Bind(R.id.lvSetting)
    ListView mLvSetting;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    private int mViewType = MCCPilotLogConst.SELECT_MODE;
    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    private void initView(){
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_airline_interface));
        mIbLeft.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE || isTablet() ? View.GONE : View.VISIBLE);
        mIbMenu.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE ? View.VISIBLE : View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
        mAdapter = new SettingGeneralAdapter(mActivity);
        mAdapter.addSectionHeaderItem(new SettingGeneralModel("Airline", ""));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "AirlineID"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "AirlineImportDefault"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "AirlineCapsName"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "AirlineSwapName"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "CheckIn"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "CheckOut"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_airline), "CheckOutFrom"));
        mAdapter.addItem(new SettingGeneralModel(getString(R.string.settings_group_config_profile), "FunctionRank"));
        mLvSetting.setAdapter(mAdapter);
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_airline_interface;
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
                Log.d("rlBackIcon", "touch");
                finishFragment();
                break;
            default:
                break;
        }
    }

}
