package aero.pilotlog.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingAndToolFragment extends BaseMCCFragment {
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;

    private int mViewType = MCCPilotLogConst.LIST_MODE;


    public SettingAndToolFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_and_tool;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.settings_and_tools));
        mIbLeft.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE || isTablet() ? View.GONE : View.VISIBLE);
        mIbMenu.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE ? View.VISIBLE : View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
    }

    @Nullable
    @OnClick({R.id.ibMenu, R.id.llMyProfile, R.id.llGeneral, R.id.llFlightLicense, R.id.llAirlineInterface, R.id.llFlightLogging,
            R.id.llUserAccount, R.id.llDatabaseBackup, R.id.llStartOver})
    public void onClick(View pView) {
        Bundle bundle = new Bundle();
        switch (pView.getId()) {

            case R.id.ibMenu:
                toggleMenu();
                break;
            case R.id.llGeneral:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingGeneralFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llMyProfile:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingProfileFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llFlightLicense:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingFlightLicenseFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llAirlineInterface:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingAirlineInterfaceFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llFlightLogging:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, SettingFlightLoggingFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llUserAccount:
                replaceFragment(isTablet() ? R.id.llRootMainTablet : R.id.fragmentMain, UserAccountFragment.class, FLAG_ADD_STACK);
                break;
            case R.id.llDatabaseBackup:
                break;
            case R.id.llStartOver:
                break;
            default:
                break;
        }
    }
}
