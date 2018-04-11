package aero.pilotlog.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.ApiConstant;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZCurrency;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.NetworkUtils;
import aero.pilotlog.widgets.ItemInputText;

import java.text.ParseException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountryInfoFragment extends BaseMCCFragment {
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

    @Bind(R.id.item_country_currency)
    ItemInputText itemCountryCurrency;
    @Bind(R.id.item_country_phone_code)
    ItemInputText itemCountryPhoneCode;
    @Bind(R.id.item_country_internet_ltd)
    ItemInputText itemCountryInternetLtd;
    @Bind(R.id.item_country_iso_codes)
    ItemInputText itemCountryIsoCodes;
    @Bind(R.id.item_country_aircraft_codes)
    ItemInputText itemCountryAircraftCodes;
    @Bind(R.id.item_country_continent)
    ItemInputText itemCountryContinent;
    @Bind(R.id.item_country_capital_city)
    ItemInputText itemCountryCapitalCity;
    @Bind(R.id.ln_surrounding_country)
    LinearLayout lnSurroundingCountry;
    @Bind(R.id.item_country_surrounding)
    ItemInputText itemCountrySurrounding;

    DatabaseManager mDatabaseManager;
    Airfield airfield = null;
    ZCountry country = null;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_country_info;
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
        //if (!isTablet()) {
            mIbLeft.setVisibility(View.VISIBLE);
        //}
        //Disable header back icon for tablet
       /* if (isTablet()) {
            mHeaderRlBack.setClickable(false);
        }*/
        mTvTitle.setText("Country Info");

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

                ZCurrency currency = mDatabaseManager.getZCurrencyByCode(country.getCurrCode());
                if (currency != null) {
                    itemCountryCurrency.setDescription(String.format("%s (%s)", currency.getCurrLong(), currency.getCurrShort()));
                }
                itemCountryInternetLtd.setDescription(country.getTld());
                itemCountryPhoneCode.setDescription(country.getPrefixPhone());
                itemCountryIsoCodes.setDescription(String.format("%s, %s, %s", country.getISO_3166(), country.getISO_Long(), country.getISO_Numeric()));
                String acCodes = country.getRegAC();
                if (!TextUtils.isEmpty(acCodes)) {
                    String[] acCodeArray = acCodes.split(MCCPilotLogConst.SPLIT_KEY);
                    if (acCodeArray.length > 0) {
                        acCodes = "";
                        for (int i = 0; i < acCodeArray.length; i++) {
                            if (!TextUtils.isEmpty(acCodes))
                                acCodes += ", ";
                            acCodes += acCodeArray[i];
                        }
                        itemCountryAircraftCodes.setDescription(acCodes);

                    }
                }
                HashMap<String, String> continents = createContinents();
                String continent = continents.get(country.getContinent());
                if (!TextUtils.isEmpty(continent)) {
                    itemCountryContinent.setDescription(continent);
                }
                itemCountryCapitalCity.setDescription(country.getCapital());

                String surroundings = country.getNeighbours();
                if (!TextUtils.isEmpty(surroundings)) {
                    String[] surroundingArray = surroundings.split(MCCPilotLogConst.SPLIT_KEY);
                    if (surroundingArray.length > 0) {
                        surroundings = "";
                        for (int i = 0; i < surroundingArray.length; i++) {
                            if (!TextUtils.isEmpty(surroundings))
                                surroundings += ", ";
                            ZCountry countrySurrounding = mDatabaseManager.getCountryByIso3166(surroundingArray[i]);
                            if (countrySurrounding != null) {
                                if (!TextUtils.isEmpty(countrySurrounding.getCountryName()))
                                    surroundings += countrySurrounding.getCountryName();
                            }
                        }
                        itemCountrySurrounding.setDescription(surroundings);

                    }
                }
            }
            tvCity.setText(airfield.getCity());
        }
    }

    private HashMap<String, String> createContinents() {
        HashMap<String, String> continents = new HashMap<>();
        continents.put("AF", "Africa");
        continents.put("AN", "Antarctica");
        continents.put("AS", "Asia");
        continents.put("EU", "Europe");
        continents.put("OC", "Oceania");
        continents.put("NA", "North-America");
        continents.put("SA", "South-America");
        return continents;
    }

    @Nullable
    @OnClick({R.id.rlBackIcon, R.id.ln_surrounding_country, R.id.item_country_surrounding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            case R.id.ln_surrounding_country:
            case R.id.item_country_surrounding:
                if (!NetworkUtils.isHavingNetwork(mActivity)) {
                    Toast.makeText(mActivity,
                            getString(R.string.dialog_no_internet_content),
                            android.widget.Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (airfield != null && country != null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(ApiConstant.SURROUNDING_COUNTRIES_URL + country.getCountryName()));
                        startActivity(browserIntent);
                    }
                }
                break;
            default:
                break;
        }
    }
}
