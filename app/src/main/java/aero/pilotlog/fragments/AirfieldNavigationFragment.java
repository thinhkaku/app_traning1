package aero.pilotlog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.Navigation;
//import net.mcc3si.databasesv5.entities.AirfieldPilot;
//import net.mcc3si.databases.entities.CountryGeo;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
//import net.mcc3si.models.AirfieldPilotCountryModel;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by tuan.na on 7/29/2015.
 * Class for airfield navigation
 */
public class AirfieldNavigationFragment extends BaseMCCFragment implements MccEditText.EditTextImeBackListener {

    @Bind(R.id.edt_airfield_from)
    MccEditText mEdtAirfieldFrom;
    @Bind(R.id.edt_airfield_to)
    MccEditText mEdtAirfieldTo;
    /*Text view of airfield info */
    @Bind(R.id.tv_airfield_from_name)
    TextView mTvAirfieldFromName;
    @Bind(R.id.tv_airfield_from_country)
    TextView mTvAirfieldFromCountry;
    @Bind(R.id.tv_airfield_to_name)
    TextView mTvAirfieldToName;
    @Bind(R.id.tv_airfield_to_country)
    TextView mTvAirfieldToCountry;
    /*Great circle and rhumb line */
    @Bind(R.id.tv_great_circle_kilometers)
    TextView mTvGreatCircleKM;
    @Bind(R.id.tv_great_circle_miles)
    TextView mTvGreatCircleMiles;
    @Bind(R.id.tv_great_circle_statute_mile)
    TextView mTvGreatCircleStatuteMile;
    @Bind(R.id.tv_rhumb_line_kilometers)
    TextView mTvRhumbLineKM;
    @Bind(R.id.tv_rhumb_line_miles)
    TextView mTvRhumbLineMiles;
    @Bind(R.id.tv_rhmb_statute_mile)
    TextView mTvRhumbStatuteMile;
    @Bind(R.id.tv_rhumb_line_track)
    TextView mTvRhumbLineTrack;
    @Bind(R.id.tv_action_bar_right)
    TextView mTvCalculate;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.tv_cancel)
    TextView mTvCancel;

    private static final String KILOMETER = " KM";
    private static final String SM = " SM";
    private static final String MILE = " NM";
    private static final int CALL_FROM = 1;
    private static final int CALL_TO = 2;
    private Airfield mAirfieldCallback;
    private boolean isFromAirfield = false, isHideKeyBoard = false;

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        initView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_airfield_navigation;
    }

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_add_edit_action_bar;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    /**
     * Initialize view
     */
    private void initView() {
        mTvTitle.setText(R.string.navigation_title);
        mTvCalculate.setText("CALCULATE");
        mTvCalculate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calculate_white, 0, 0, 0);
        mIbMenu.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.VISIBLE);
        mEdtAirfieldFrom.setOnEditTextImeBackListener(this);
        mEdtAirfieldTo.setOnEditTextImeBackListener(this);
        mTvAirfieldFromName.setSelected(true);
        mTvAirfieldToName.setSelected(true);

        Bundle b = getArguments();
        if (b != null) {
            byte[] mAfCode = b.getByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY);
            if (mAfCode!=null) {
                Airfield airfield = DatabaseManager.getInstance(mActivity).getAirfieldByCode(mAfCode);
                if(airfield!=null){
                    //ZCountry zCountry = DatabaseManager.getInstance(mActivity).getCountryByCode(airfield.getAFCountry());
                    fillAirfieldOnViews(airfield, CALL_FROM);
                }

            }
        }
    }

    private void fillAirfieldOnViews(Airfield airfield, int pType) {

        if (airfield != null) {
            ZCountry zCountry = DatabaseManager.getInstance(mActivity).getCountryByCode(airfield.getAFCountry());
            if(zCountry!=null){
                if (pType == CALL_FROM) {
                    mTvAirfieldFromName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                    mEdtAirfieldFrom.setText(airfield.getAFICAO());
                    mTvAirfieldFromName.setText(airfield.getAFName());
                    mTvAirfieldFromCountry.setText(zCountry.getCountryName());
                } else {
                    mEdtAirfieldTo.setText(airfield.getAFICAO());
                    mTvAirfieldToName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                    mTvAirfieldToName.setText(airfield.getAFName());
                    mTvAirfieldToCountry.setText(zCountry.getCountryName());
                }
            }
        } else {
            if (pType == CALL_FROM) {
                mTvAirfieldFromName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                mTvAirfieldFromName.setText(R.string.airfield_not_found);
                mTvAirfieldFromCountry.setText(STRING_EMPTY);
            } else {
                mTvAirfieldToName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                mTvAirfieldToName.setText(R.string.airfield_not_found);
                mTvAirfieldToCountry.setText(STRING_EMPTY);
            }
        }
        mTvGreatCircleKM.setText(STRING_EMPTY);
        mTvGreatCircleMiles.setText(STRING_EMPTY);
        mTvRhumbLineKM.setText(STRING_EMPTY);
        mTvRhumbLineMiles.setText(STRING_EMPTY);
        mTvRhumbLineTrack.setText(STRING_EMPTY);

//        mEdtAirfieldTo.clearFocus();
//        mEdtAirfieldFrom.clearFocus();
    }

    @Override
    public void onHideKeyboard(MccEditText pEditText) {
        isHideKeyBoard = true;
        try {
            pEditText.clearFocus();
            pEditText.setFocusable(false);
            pEditText.setFocusableInTouchMode(true);
            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(pEditText.getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (pEditText.getText().toString().trim().length() == 0) {
            return;
        }
        Airfield airfield = getAirfield(pEditText.getText().toString().toUpperCase().trim());
        switch (pEditText.getId()) {
            case R.id.edt_airfield_from:
                mEdtAirfieldFrom.clearFocus();
                fillAirfieldOnViews(airfield, CALL_FROM);
                break;
            case R.id.edt_airfield_to:
                mEdtAirfieldTo.clearFocus();
                mEdtAirfieldFrom.clearFocus();
                fillAirfieldOnViews(airfield, CALL_TO);
                break;
            default:
                break;
        }
        isHideKeyBoard = false;
    }

    /**
     * @param pIcaoIata
     * @return
     */
    private Airfield getAirfield(String pIcaoIata) {
        return new DatabaseManager(mActivity).getAirfieldByICAOIATA(pIcaoIata);
    }

    @OnClick({R.id.iv_search_airfield_from, R.id.iv_search_airfield_to, R.id.tv_action_bar_right, R.id.tv_cancel/*, R.id.ibMenu*/})
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.iv_search_airfield_from:
            case R.id.iv_search_airfield_to:
                KeyboardUtils.hideKeyboard(mActivity);
                mTvAirfieldFromName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                mTvAirfieldToName.setTextColor(getResources().getColor(R.color.mcc_cyan));
                Bundle bundle = new Bundle();
                bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRFIELD_NAVIGATION);
                if (!isTablet()) {
                    replaceAirfieldListFragment(bundle);
                } else {
                    Fragment fragmentLeft = getLeftFragment();
                    if (fragmentLeft != null && fragmentLeft instanceof AirfieldsListFragment) {
                        ((AirfieldsListFragment) fragmentLeft).setViewType(MCCPilotLogConst.SELECT_MODE);
                        ((AirfieldsListFragment) fragmentLeft).setArguments(bundle);
                        Utils.showToast(mActivity, "Pick an airfield on the left to calculate distance!");
                    }
                }
                isFromAirfield = pView.getId() == R.id.iv_search_airfield_from;
                /*if (isFromAirfield) {
                    mEdtAirfieldFrom.setText(STRING_EMPTY);
                    mTvAirfieldFromName.setText(STRING_EMPTY);
                    mTvAirfieldFromCountry.setText(STRING_EMPTY);
                } else {
                    mEdtAirfieldTo.setText(STRING_EMPTY);
                    mTvAirfieldToName.setText(STRING_EMPTY);
                    mTvAirfieldToCountry.setText(STRING_EMPTY);
                }*/
                break;
            case R.id.tv_cancel:
                KeyboardUtils.hideKeyboard(mActivity);
                finishFragment();
                break;
            case R.id.tv_action_bar_right:
                KeyboardUtils.hideKeyboard(mActivity);
                calculateNavigation();
                break;
            default:
                break;
        }

    }

    @OnFocusChange({R.id.edt_airfield_from, R.id.edt_airfield_to})
    void onFocusChanged(View pView, boolean focused) {
        if (focused && !isHideKeyBoard) {
            //clearValue(pView);
        }else {
            if(pView instanceof MccEditText){
                MccEditText pEditText = (MccEditText)pView;
                Airfield airfield = getAirfield(pEditText.getText().toString().toUpperCase().trim());
                switch (pEditText.getId()) {
                    case R.id.edt_airfield_from:
                        fillAirfieldOnViews(airfield, CALL_FROM);
                        break;
                    case R.id.edt_airfield_to:
                        fillAirfieldOnViews(airfield, CALL_TO);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Method to calculate when button clicked
     */
    private void calculateNavigation() {
        clearValue(null);
        DatabaseManager mDbManager = new DatabaseManager(mActivity);
        Airfield mAirfieldFrom, mAirfieldTo;
        mAirfieldFrom = mDbManager.getAirfieldByICAOIATA(mEdtAirfieldFrom.getText().toString().toUpperCase());
        mAirfieldTo = mDbManager.getAirfieldByICAOIATA(mEdtAirfieldTo.getText().toString().toUpperCase());
        if (mEdtAirfieldFrom.getText().toString().length() == 0 || mAirfieldFrom == null
                || mEdtAirfieldTo.getText().toString().length() == 0 || mAirfieldTo == null) {
            MccDialog.getOkAlertDialog(mActivity, R.string.error_navigation, R.string.error_missing_airfield).show();
            return;
        }

        if (mAirfieldFrom.getLatitude() == 0 || mAirfieldFrom.getLongitude() ==0) {
            MccDialog.getOkAlertDialog(mActivity, R.string.error_navigation, String.format(getString(R.string.error_missing_airfield), mAirfieldFrom.getAFName())).show();
            return;
        }
        if (mAirfieldTo.getLatitude() == 0 || mAirfieldTo.getLongitude() ==0) {
            MccDialog.getOkAlertDialog(mActivity, R.string.error_navigation, String.format(getString(R.string.error_missing_airfield), mAirfieldTo.getAFName())).show();
            return;
        }

        Navigation nav = new Navigation();
        nav.latitudeA = mAirfieldFrom.getLatitude();
        nav.longitudeA = mAirfieldFrom.getLongitude();

        nav.latitudeB = mAirfieldTo.getLatitude();
        nav.longitudeB = mAirfieldTo.getLongitude();

        nav.calculate();
        LogUtils.e("navigation", nav.orthoDist + "");
        LogUtils.e("navigation", nav.loxoDist + "");
        LogUtils.e("navigation", nav.loxoTrack + "");

        DecimalFormat dcf = new DecimalFormat("#,##0");
        dcf.setRoundingMode(RoundingMode.HALF_UP);
        mTvGreatCircleKM.setText(dcf.format(nav.orthoDist) + MILE);
        mTvGreatCircleMiles.setText(dcf.format(nav.orthoDist * 1.852) + KILOMETER);
        mTvGreatCircleStatuteMile.setText(dcf.format(nav.orthoDist  * 1.852 / 1.609344)+ SM);

        mTvRhumbLineKM.setText(dcf.format(nav.loxoDist) + MILE);
        mTvRhumbLineMiles.setText(dcf.format(nav.loxoDist * 1.852) + KILOMETER);
        mTvRhumbStatuteMile.setText(dcf.format(nav.loxoDist  * 1.852 / 1.609344)+ SM);

        dcf.applyPattern("000");
        mTvRhumbLineTrack.setText(dcf.format(nav.loxoTrack) + " °");
        System.gc();
    }

    public void setSelectedAirfield(byte[] pAirfieldCode) {
        Airfield airfieldPilot = DatabaseManager.getInstance(mActivity).getAirfieldByCode(pAirfieldCode);
        //mAirfieldCallback = new Airfield(airfieldPilot, DatabaseManager.getInstance(mActivity).getCountryFromCountryCode(airfieldPilot.getAFCountry()));
        mAirfieldCallback = airfieldPilot;
    }

    public void setSelectedAirfield(Airfield pAirfield) {
        mAirfieldCallback = pAirfield;
        clearValue(null);
        if (isTablet()) {
            //AirfieldPilot airfieldPilot = null;
            //CountryGeo countryGeo = null;
           /* if (mAirfieldCallback != null) {
                //airfieldPilot = mAirfieldCallback.getAirfieldPilot();
                countryGeo = mAirfieldCallback.getCountryGeo();
                mAirfieldCallback = null;
            }*/
            if (pAirfield != null){
                ZCountry zCountry = DatabaseManager.getInstance(mActivity).getCountryByCode(pAirfield.getAFCountry());
                if(zCountry!=null)

                        if (isFromAirfield) {
                            mEdtAirfieldFrom.setText(pAirfield.getAFICAO());
                            mTvAirfieldFromName.setText(pAirfield.getAFName());
                            mTvAirfieldFromCountry.setText(zCountry.getCountryName());
                        } else {
                            mEdtAirfieldTo.setText(pAirfield.getAFICAO());
                            mTvAirfieldToName.setText(pAirfield.getAFName());
                mTvAirfieldToCountry.setText(zCountry.getCountryName());
            }
            }

        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

       /* Airfield airfieldPilot = null;
        ZCountry countryGeo = null;
        if (mAirfieldCallback != null) {
            airfieldPilot = mAirfieldCallback.getAirfieldPilot();
            countryGeo = mAirfieldCallback.getCountryGeo();
            mAirfieldCallback = null;
        }*/
        if (mAirfieldCallback != null) {
            ZCountry zCountry = DatabaseManager.getInstance(mActivity).getCountryByCode(mAirfieldCallback.getAFCountry());
            if(zCountry!=null){
                if (isFromAirfield) {
                    mEdtAirfieldFrom.setText(mAirfieldCallback.getAFICAO());
                    mTvAirfieldFromName.setText(mAirfieldCallback.getAFName());
                    mTvAirfieldFromCountry.setText(zCountry.getCountryName());
                } else {
                    mEdtAirfieldTo.setText(mAirfieldCallback.getAFICAO());
                    mTvAirfieldToName.setText(mAirfieldCallback.getAFName());
                    mTvAirfieldToCountry.setText(zCountry.getCountryName());
                }
            }
        }
        mAirfieldCallback = null;
    }

    /**
     * Clear text in great circle and rhumb line on input change
     */
    private void clearValue(View pView) {
        mTvGreatCircleKM.setText(STRING_EMPTY);
        mTvGreatCircleMiles.setText(STRING_EMPTY);
        mTvGreatCircleStatuteMile.setText(STRING_EMPTY);
        mTvRhumbLineKM.setText(STRING_EMPTY);
        mTvRhumbLineMiles.setText(STRING_EMPTY);
        mTvRhumbLineTrack.setText(STRING_EMPTY);
        mTvRhumbStatuteMile.setText(STRING_EMPTY);
        if(pView==null)return;
        if (pView.getId() == R.id.edt_airfield_from || pView.getId() == R.id.iv_search_airfield_from) {
            mEdtAirfieldFrom.setText(STRING_EMPTY);
            mTvAirfieldFromName.setText(STRING_EMPTY);
            mTvAirfieldFromCountry.setText(STRING_EMPTY);
        } else {
            mEdtAirfieldTo.setText(STRING_EMPTY);
            mTvAirfieldToName.setText(STRING_EMPTY);
            mTvAirfieldToCountry.setText(STRING_EMPTY);
        }
    }

    private void replaceAirfieldListFragment(Bundle bundle) {
        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
        bundle.putBoolean(MCCPilotLogConst.AIRFIELD_BOOLEAN_IS_DEP, isFromAirfield);
        replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AirfieldsListFragment.class, bundle, FLAG_ADD_STACK);
    }
}