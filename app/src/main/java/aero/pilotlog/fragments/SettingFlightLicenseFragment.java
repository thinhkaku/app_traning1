package aero.pilotlog.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.widgets.ItemInputText;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFlightLicenseFragment extends BaseMCCFragment {

    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    private int mViewType = MCCPilotLogConst.SELECT_MODE;

    @Bind(R.id.item_license_class)
    ItemInputText itemLicenseClass;
    @Bind(R.id.item_license_number)
    ItemInputText itemLicenseNbr;
    @Bind(R.id.item_license_issued)
    ItemInputText itemLicenseIssued;
    @Bind(R.id.item_license_valid)
    ItemInputText itemLicenseValid;

    @Bind(R.id.item_license_2_class)
    ItemInputText itemLicense2Class;
    @Bind(R.id.item_license_2_number)
    ItemInputText itemLicense2Nbr;
    @Bind(R.id.item_license_2_issued)
    ItemInputText itemLicense2Issued;
    @Bind(R.id.item_license_2_valid)
    ItemInputText itemLicense2Valid;

    @Bind(R.id.item_license_date_begin_career)
    ItemInputText itemLicenseBeginCareer;
    @Bind(R.id.item_license_xc150)
    ItemInputText itemLicenseXC150;
    @Bind(R.id.item_license_xc300)
    ItemInputText itemLicenseXC300;
    @Bind(R.id.item_license_xc100)
    ItemInputText itemLicenseXC100;
    @Bind(R.id.item_license_night_dual)
    ItemInputText itemLicenseNightDual;


    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    private void initView(){
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_flight_license));
        mIbLeft.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE || isTablet() ? View.GONE : View.VISIBLE);
        mIbMenu.setVisibility(mViewType == MCCPilotLogConst.LIST_MODE ? View.VISIBLE : View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
        initData();
    }

    private void initData(){
        itemLicenseClass.setDescription("Airline Transport Pilot License");
        itemLicense2Class.setDescription("Airline Transport Pilot License");
        itemLicenseNbr.setDescription("AA398287");
        itemLicense2Nbr.setDescription("AA398287");
        itemLicenseIssued.setText("2017-02-08");
        itemLicenseValid.setText("2017-02-08");
        itemLicense2Issued.setText("2017-02-08");
        itemLicense2Valid.setText("2017-02-08");
        itemLicense2Valid.setVisibleLine(View.GONE);
        itemLicenseBeginCareer.setText("2017-02-08");
        itemLicenseXC150.setText("2017-02-08");
        itemLicenseXC300.setText("2017-02-08");
        itemLicenseXC100.setText("2017-02-08");
        itemLicenseNightDual.setText("2017-02-08");
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_flight_license;
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
