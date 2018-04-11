package aero.pilotlog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.entities.ZTimeZoneDST;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemInputTextFootnote;
import aero.pilotlog.widgets.ItemInputTextWithIcon;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeZoneInfoFragment extends BaseMCCFragment {
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;

    @Bind(R.id.iv_flag)
    ImageView ivFlag;
    @Bind(R.id.tv_country)
    TextView tvCountry;
    @Bind(R.id.tv_city)
    TextView tvCity;

    @Bind(R.id.item_timezone)
    ItemInputTextWithIcon itemTimeZone;
    @Bind(R.id.item_local_time)
    ItemInputTextWithIcon itemLocalTime;
    @Bind(R.id.item_dst_rule)
    ItemInputTextFootnote itemDstRule;
    @Bind(R.id.item_more_timezone)
    ItemInputTextFootnote itemMoreTimeZone;

    DatabaseManager mDatabaseManager;
    Airfield airfield = null;
    ZCountry country = null;
    public static final String LOCAL_TIME_FORMAT = " %s";
    java.util.TimeZone timeZone;

    public TimeZoneInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_time_zone_info;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        Bundle bundle = getArguments();
        initView(bundle);
    }

    private void initView(Bundle bundle) {
        mHeaderIbMenu.setVisibility(View.GONE);
       // if (!isTablet()) {
            mIbLeft.setVisibility(View.VISIBLE);
        //}
        //Disable header back icon for tablet
      /*  if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }*/
        mTvTitle.setText("Time Zone");

        byte[] mAirfieldCode = null;
        if (bundle != null) {
            mAirfieldCode = bundle.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
        }
        if (mAirfieldCode != null) {
            airfield = mDatabaseManager.getAirfieldByCode(mAirfieldCode);
        }
        if (airfield != null) {
            country = mDatabaseManager.getCountryByCode(airfield.getAFCountry());
            if (country != null) {
                int resId = mActivity.getResources().getIdentifier(String.format("flag_%03d", country.getCountryCode()), "drawable", mActivity.getPackageName());
                ivFlag.setBackgroundResource(resId);
                tvCountry.setText(country.getCountryName());

            }
            tvCity.setText(airfield.getCity());
            ZTimeZone zTimeZone = mDatabaseManager.getTimeZoneByCode(airfield.getTZCode());
            setTimeZone(zTimeZone);
            setDSTRule(zTimeZone);
        }

    }

    private void setTimeZone(ZTimeZone zTimeZone) {

        if (zTimeZone != null) {
            timeZone = java.util.TimeZone.getTimeZone(zTimeZone.getTimeZone());
            Calendar c = Calendar.getInstance(timeZone);
            String offset;
            double offsetValue = (double) Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, airfield.getTZCode()) / 60;

            if (offsetValue < 0) {
                offset = "" + offsetValue;
            } else {
                offset = "+" + offsetValue;
            }
            if (offset.endsWith(".0")) offset = offset.replace(".0", "");
            String localTime = String.format(LOCAL_TIME_FORMAT, String.format(Locale.getDefault(), "%02d", c.get(Calendar.HOUR_OF_DAY))
                    + ":" + String.format(Locale.getDefault(), "%02d", c.get(Calendar.MINUTE)));
            itemTimeZone.setDescription("UTC " + offset);
            String timezoneFootNote = String.format("%s - %s\n%s", zTimeZone.getZoneShort(), zTimeZone.getZoneLong(), zTimeZone.getTimeZone());
            itemTimeZone.setFootNote(timezoneFootNote);
            itemTimeZone.setMarginLeftDescriptionFootNote(40);
            itemLocalTime.setMarginLeftDescriptionFootNote(40);
            itemDstRule.setMarginLeftDescriptionFootNote(70);
            itemMoreTimeZone.setMarginLeftDescriptionFootNote(70);
            itemLocalTime.setDescription(localTime.trim());
            double compareTwoTimeZone = compareDeviceTimeZoneWithAirfieldTimeZone(c);
            if (compareTwoTimeZone > 0) {
                itemLocalTime.setFootNote(String.format("This region is %s %s behind on my current time zone", compareTwoTimeZone,
                        compareTwoTimeZone != 1 ? "hours" : "hour"));
            } else if (compareTwoTimeZone < 0) {
                itemLocalTime.setFootNote(String.format("This region is %s %s ahead on my current time zone", compareTwoTimeZone * -1,
                        compareTwoTimeZone != -1 ? "hours" : "hour"));
            } else {
                itemLocalTime.setFootNote("This region is in the same time zone as you");
            }

        }
    }

    private double compareDeviceTimeZoneWithAirfieldTimeZone(Calendar airfieldCal) {
        Calendar deviceCal = Calendar.getInstance();
        java.util.TimeZone deviceTimeZone = deviceCal.getTimeZone();
        double deviceOffsetValue = (double) deviceTimeZone.getOffset(deviceCal.getTimeInMillis()) / 3600000;
        double airfieldOffsetValue = (double) Utils.getTimeOffset(timeZone.getOffset(airfieldCal.getTimeInMillis()) / 60000, airfield.getTZCode()) / 60;
        double result = deviceOffsetValue - airfieldOffsetValue;
        //return deviceOffsetValue >= 0 ? result : result * -1;
        return result;
    }

    private void setDSTRule(ZTimeZone zTimeZone) {
        ZTimeZoneDST zTimeZoneDST = mDatabaseManager.getTimeZoneDSTByCode(zTimeZone.getDSTCode());
        if (zTimeZoneDST != null) {
            String dstRuleString = zTimeZoneDST.getRule().replace("|:", "\n\n");
            itemDstRule.setDescription(dstRuleString);
        }
    }

    @Nullable
    @OnClick({R.id.rlBackIcon, R.id.ln_more_timezone, R.id.item_more_timezone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            case R.id.ln_more_timezone:
            case R.id.item_more_timezone:
                if (airfield != null && country != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(ApiConstant.MORE_TIME_ZONE_INFO_URL + country.getCountryName()));
                    startActivity(browserIntent);
                }
                break;
            default:
                break;
        }
    }
}
