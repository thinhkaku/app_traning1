package aero.pilotlog.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.AircraftInfoConst;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZFNPT;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.CircleImageView;
import aero.pilotlog.widgets.ItemInputText;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.ItemWithDot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 2/23/2017.
 * Aircraft info
 */
public class AircraftInfoFragment extends BaseMCCFragment {

    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    @Bind(R.id.ibMenu)
    ImageButton mHeaderIbMenu;
    @Bind(R.id.lnEdit)
    LinearLayout lnEdit;
    @Bind(R.id.tvModel)
    TextView tvModel;
    @Bind(R.id.tvManufacture)
    TextView tvManufacture;
    @Bind(R.id.tvCompany)
    TextView tvCompany;
    @Bind(R.id.tvRegistration)
    TextView tvRegistration;
    @Bind(R.id.item_aircraft_type)
    ItemInputText itemAircraftType;
    @Bind(R.id.item_aircraft_class)
    ItemInputText itemAircraftClass;
    @Bind(R.id.item_aircraft_category)
    ItemInputText itemAircraftCategory;
    @Bind(R.id.item_aircraft_power)
    ItemInputText itemAircraftPower;
    @Bind(R.id.edt_item_aircraft_type_rating)
    ItemInputText itemAircraftTypeRating;
    @Bind(R.id.tvSpectification)
    TextView tvSpectification;
    @Bind(R.id.item_aircraft_seats)
    ItemWithDot itemAircraftSeats;
    @Bind(R.id.item_aircraft_aerobatic)
    ItemWithDot itemAircraftAerobatic;
    @Bind(R.id.item_aircraft_complex)
    ItemWithDot itemAircraftComplex;
    @Bind(R.id.item_aircraft_high_performance)
    ItemWithDot itemAircraftHighPerformance;
    @Bind(R.id.item_aircraft_tm_glider)
    ItemWithDot itemAircraftGlider;
    @Bind(R.id.item_aircraft_tailwheel)
    ItemWithDot itemAircraftTailWheel;
    @Bind(R.id.item_aircraft_more_than_57kg)
    ItemWithDot itemAircraftMoreThan;
    @Bind(R.id.edt_item_aircraft_function_time)
    ItemInputText itemAircraftFunctionTime;
    @Bind(R.id.edt_item_aircraft_condition_time)
    ItemInputText itemAircraftConditionTime;
    @Bind(R.id.edt_item_aircraft_operation)
    ItemInputTextWithIcon itemAircraftOperation;
    @Bind(R.id.edt_item_aircraft_approach)
    ItemInputTextWithIcon itemAircraftApproach;
    @Bind(R.id.edt_item_aircraft_launch)
    ItemInputTextWithIcon itemAircraftLaunch;
    @Bind(R.id.edt_item_aircraft_history)
    ItemInputTextWithIcon itemAircraftHistory;
    @Bind(R.id.lnHeader)
    LinearLayout lnHeader;
    @Bind(R.id.iv_image_menu)
    CircleImageView ivProfile;
    @Bind(R.id.sv_aircraft_info)
    ScrollView svAircraftInfo;
    Bundle mBundle;
    @Bind(R.id.ln_history)
    LinearLayout lnHistory;

    private byte[] mAircraftCode;
    DatabaseManager mDatabaseManager;

    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_aircraft_info;
    }

    @Override
    protected int getFooterResId() {
        return FLAG_NO_RESOURCE;
    }

    @Override
    protected void onCreateFragment(View pContentView) {
        ButterKnife.bind(this, mViewContainer);
        mBundle = getArguments();
        initView();
        initDataAircraft(mBundle);
    }

    private void initView() {
        //set header views
        mHeaderIbMenu.setVisibility(View.GONE);
        if (!isTablet()) {
            mTvTitle.setText("Back");
            mIbLeft.setVisibility(View.VISIBLE);
        }else {
            mTvTitle.setText("");
            //Disable header back icon for tablet
            mHeaderRlBack.setClickable(false);
        }

        lnEdit.setVisibility(View.VISIBLE);


        itemAircraftTypeRating.setVisibleLine(View.GONE);
        itemAircraftLaunch.setVisibleLine(View.GONE);

        lnHeader.post(new Runnable() {
            public void run() {
                int headerHeight = lnHeader.getHeight();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                int imageHeight = ivProfile.getHeight();
                params.setMargins(0, headerHeight - imageHeight / 2 - (imageHeight % 2) / 2,
                        (int) getResources().getDimension(R.dimen.margin_right_profile_image), 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ivProfile.setLayoutParams(params);

                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, headerHeight, 0, 0);
                svAircraftInfo.setLayoutParams(params);
            }
        });
    }

    /**
     * Initialize data
     *
     * @param pBundle bundle
     */
    public void initDataAircraft(Bundle pBundle) {
        Aircraft aircraft;
        /*Get aircraft from aircraft code*/
        mDatabaseManager = DatabaseManager.getInstance(mActivity);
        if (pBundle != null) {
            mAircraftCode = pBundle.getByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY);
        }
        if (mAircraftCode != null) {
            aircraft = mDatabaseManager.getAircraftByCode(mAircraftCode);
        } else {
            aircraft = new Aircraft();
        }
        tvModel.setText(!TextUtils.isEmpty(aircraft.getSubModel()) ? aircraft.getModel() + "-" + aircraft.getSubModel()
                : aircraft.getModel());
        if (!TextUtils.isEmpty(aircraft.getMake())) {
            tvManufacture.setText(aircraft.getMake());
        } else {
            tvManufacture.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(aircraft.getCompany())) {
            tvCompany.setText(aircraft.getCompany());
        } else {
            tvCompany.setVisibility(View.GONE);
        }
        tvRegistration.setText(!TextUtils.isEmpty(aircraft.getFin()) ? (!aircraft.getReference().equals(aircraft.getFin()) ?
                aircraft.getReference() + " (" + aircraft.getFin() + ")" : aircraft.getReference())
                : aircraft.getReference());
        itemAircraftType.setDescription(getAircraftDeviceCodeString(aircraft));
        itemAircraftClass.setDescription(getAircraftClassString(aircraft));
        if (aircraft.getDeviceCode() != 3) {
            itemAircraftCategory.setDescription(aircraft.getCategory() == 1 ? "Single Pilot" : "Multi Pilot");
        } else {
            itemAircraftCategory.setVisibility(View.GONE);
        }
        boolean isShowSpec = false;
        //if(aircraft.getDeviceCode() != 3){
        itemAircraftPower.setDescription(getAircraftPowerString(aircraft));

        if (aircraft.getSeats() > 0) {
            itemAircraftSeats.setText(String.format("%s Seats", aircraft.getSeats()));
            itemAircraftSeats.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftSeats.setVisibility(View.GONE);
        }
        if (aircraft.getAerobatic()) {
            itemAircraftAerobatic.setText("Aerobatic");
            itemAircraftAerobatic.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftAerobatic.setVisibility(View.GONE);
        }
        if (aircraft.getComplex()) {
            itemAircraftComplex.setText("Complex");
            itemAircraftComplex.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftComplex.setVisibility(View.GONE);
        }
        if (aircraft.getHighPerf()) {
            itemAircraftHighPerformance.setText("High Performance");
            itemAircraftHighPerformance.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftHighPerformance.setVisibility(View.GONE);
        }
        if (aircraft.getTMG()) {
            itemAircraftGlider.setText("TM Glider");
            itemAircraftGlider.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftGlider.setVisibility(View.GONE);
        }
        if (aircraft.getTailwheel()) {
            itemAircraftTailWheel.setText("Tail Wheel");
            itemAircraftTailWheel.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftTailWheel.setVisibility(View.GONE);
        }
        if (aircraft.getKg5700()) {
            itemAircraftMoreThan.setText("More than 5700 kg - 12500 lbs");
            itemAircraftMoreThan.setVisibility(View.VISIBLE);
            isShowSpec = true;
        } else {
            itemAircraftMoreThan.setVisibility(View.GONE);
        }
        ZApproach zApproach = getZApproach(aircraft);
        if (zApproach != null) {
            itemAircraftApproach.setDescription(zApproach.getAPShort());
            itemAircraftApproach.setFootNote(zApproach.getAPLong());
        }
        ZLaunch zLaunch = getZLaunch(aircraft);
        if (zLaunch != null) {
            itemAircraftLaunch.setDescription(zLaunch.getLaunchShort());
            itemAircraftLaunch.setFootNote(zLaunch.getLaunchLong());
        }
        itemAircraftConditionTime.setDescription(getAircraftLogConditionString(aircraft));
        //}

        itemAircraftTypeRating.setDescription(aircraft.getRating());

        if (isShowSpec) {
            tvSpectification.setVisibility(View.VISIBLE);
        }
        itemAircraftFunctionTime.setDescription(getAircraftDefaultLog(aircraft));
        ZOperation zOperation = getZOperation(aircraft);
        if (zOperation != null) {
            itemAircraftOperation.setDescription(zOperation.getOpsShort());
            itemAircraftOperation.setFootNote(zOperation.getOpsLong());
        }

        List<FlightModel> flightModels = mDatabaseManager.getLogbookListByAircraftPilotAirfield(MccEnum.dateFilter.LAST_90_DAYS, mAircraftCode, null, null);
        itemAircraftHistory.setVisibilityLineBottom(View.GONE);
        if (flightModels != null && flightModels.size() > 0) {
            String strCurrentDate = flightModels.get(0).getFlightDateUTC();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String flightDate = DateTimeUtils.formatDateToString(newDate);
            itemAircraftHistory.setDescription(String.format("%s : %s", flightDate, flightModels.get(0).getFlightAirfield()));
            Pilot pilot1 = null, pilot2 = null, pilot3 = null, pilot4 = null;
            if (flightModels.get(0).getmP1() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP1()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot1 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP1());
            if (flightModels.get(0).getmP2() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP2()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot2 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP2());
            if (flightModels.get(0).getmP3() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP3()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot3 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP3());
            if (flightModels.get(0).getmP4() != null && !Utils.getGUIDFromByteArray(flightModels.get(0).getmP4()).equals(MCCPilotLogConst.PILOT_CODE_EMPTY))
                pilot4 = mDatabaseManager.getPilotByCode(flightModels.get(0).getmP4());

            if (pilot1 != null || pilot2 != null || pilot3 != null || pilot4 != null) {
                String withPilot = String.format("with %s%s%s%s",
                        pilot1 != null ? pilot1.getPilotName() + ", " : "",
                        pilot2 != null ? pilot2.getPilotName() + ", " : "",
                        pilot3 != null ? pilot3.getPilotName() + ", " : "",
                        pilot4 != null ? pilot4.getPilotName() + ", " : "");
                withPilot = withPilot.substring(0, withPilot.length() - 2);
                itemAircraftHistory.setFootNote(withPilot);
            }

        } else {
            itemAircraftHistory.setDescription("none");
            lnHistory.setVisibility(View.GONE);
        }

    }

    private ZOperation getZOperation(Aircraft aircraft) {
        ZOperation zOperation = mDatabaseManager.getZOperation(aircraft.getDefaultOps());
        return zOperation;
    }

    private ZApproach getZApproach(Aircraft aircraft) {
        ZApproach zApproach = mDatabaseManager.getZApproach(aircraft.getDefaultApp());
        return zApproach;
    }

    private ZLaunch getZLaunch(Aircraft aircraft) {
        ZLaunch zLaunch = mDatabaseManager.getGliderLaunch(aircraft.getDefaultLaunch());
        return zLaunch;
    }

    private String getAircraftDefaultLog(Aircraft aircraft) {
        switch (aircraft.getDefaultLog()) {
            case 0:
                return "";
            case 1:
                return "PIC";
            case 2:
                return "SIC";
            case 3:
                return "Dual";
            case 4:
                return "Instructor";
            case 5:
                return "PICus";
            case 6:
                return "Examiner";
            case 7:
                return "PICus when PF";
            default:
                return "";
        }
    }

    private String getAircraftPowerString(Aircraft aircraft) {
        if (aircraft.getDeviceCode() == 3) itemAircraftPower.setVisibility(View.GONE);
        switch (aircraft.getPower()) {
            case 0:
                return "Unpowered";
            case 1:
                return "SE - Piston";
            case 2:
                return "SE - Turboprop-shaft";
            case 3:
                return "SE - Turbojet-fan";
            case 4:
                return "ME - Piston";
            case 5:
                return "ME - Turboprop-shaft";
            case 6:
                return "ME - Turbojet-fan";
            default:
                return "Unpowered";
        }
    }


    private String getAircraftClassString(Aircraft aircraft) {
        boolean subClass = aircraft.getSea();
        String returnString = "";
        switch (aircraft.getClassZ()) {
            case 1:
                returnString = "Microlight";
                break;
            case 2:
                returnString = "Glider";
                break;
            case 3:
                returnString = "Lighter-than-Air";
                break;
            case 4:
                returnString = "Rotorcraft";
                break;
            case 5:
                returnString = "Aeroplane";
                break;
            case 6:
                returnString = "Unmanned Aircraft";
                break;
        }
        if (subClass)
            return returnString + " (Sea)";
        else return returnString;
    }

    private String getAircraftDeviceCodeString(Aircraft aircraft) {
        switch (aircraft.getDeviceCode()) {
            case 1:
                return "Aircraft";
            case 2:
                return !TextUtils.isEmpty(getFNPT(aircraft)) ? "Simulator (" + getFNPT(aircraft) + ")" : "Simulator";
            case 3:
                return !TextUtils.isEmpty(getFNPT(aircraft)) ? "Drone (" + getFNPT(aircraft) + ")" : "Drone";
            default:
                return "";
        }
    }

    private String getFNPT(Aircraft aircraft) {
        ZFNPT zfnpt = mDatabaseManager.getFNPTByCode(aircraft.getFNPT());
        return zfnpt.getFnptShort();
    }

    private String getAircraftLogConditionString(Aircraft aircraft) {
        int condLogNum, rest;
        String condLogString = MCCPilotLogConst.STRING_EMPTY;
        String condLogAdded = MCCPilotLogConst.STRING_EMPTY;
        condLogNum = aircraft.getCondLog();

        while (condLogNum > 0) {
            rest = 0;
            if (condLogNum > AircraftInfoConst.MIN_U4) {
                rest = condLogNum - AircraftInfoConst.MIN_U4;
                condLogNum = AircraftInfoConst.MIN_U4;
            } else if (condLogNum > AircraftInfoConst.MIN_U3 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U3;
                condLogNum = AircraftInfoConst.MIN_U3;
            } else if (condLogNum > AircraftInfoConst.MIN_U2 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U2;
                condLogNum = AircraftInfoConst.MIN_U2;
            } else if (condLogNum > AircraftInfoConst.MIN_U1 && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.MIN_U1;
                condLogNum = AircraftInfoConst.MIN_U1;
            } else if (condLogNum > AircraftInfoConst.SIM_INSTR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.SIM_INSTR;
                condLogNum = AircraftInfoConst.SIM_INSTR;
            } else if (condLogNum > AircraftInfoConst.IFR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.IFR;
                condLogNum = AircraftInfoConst.IFR;
            } else if (condLogNum > AircraftInfoConst.ACT_INSTR && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.ACT_INSTR;
                condLogNum = AircraftInfoConst.ACT_INSTR;
            } else if (condLogNum > AircraftInfoConst.RELIEF && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.RELIEF;
                condLogNum = AircraftInfoConst.RELIEF;
            } else if (condLogNum > AircraftInfoConst.XC && !equalToCondLog(condLogNum)) {
                rest = condLogNum - AircraftInfoConst.XC;
                condLogNum = AircraftInfoConst.XC;
            }

            switch (condLogNum) {
                case AircraftInfoConst.MIN_U4:
                    condLogAdded = mDatabaseManager.getSetting(480).getData();
                    break;
                case AircraftInfoConst.MIN_U3:
                    condLogAdded = mDatabaseManager.getSetting(481).getData();
                    break;
                case AircraftInfoConst.MIN_U2:
                    condLogAdded = mDatabaseManager.getSetting(482).getData();
                    break;
                case AircraftInfoConst.MIN_U1:
                    condLogAdded = mDatabaseManager.getSetting(483).getData();
                    break;
                case AircraftInfoConst.SIM_INSTR:
                    condLogAdded = AircraftInfoConst.SIM_INSTR_STRING;
                    break;
                case AircraftInfoConst.IFR:
                    condLogAdded = AircraftInfoConst.IFR_STRING;
                    break;
                case AircraftInfoConst.ACT_INSTR:
                    condLogAdded = AircraftInfoConst.ACT_INSTR_STRING;
                    break;
                case AircraftInfoConst.RELIEF:
                    condLogAdded = AircraftInfoConst.RELIEF_STRING;
                    break;
                case AircraftInfoConst.XC:
                    condLogAdded = AircraftInfoConst.XC_STRING;
                    break;
                default:
                    break;
            }

            condLogString += MCCPilotLogConst.STRING_EMPTY.equals(condLogString) ? condLogAdded : MCCPilotLogConst.COMMA + condLogAdded;
            condLogNum = rest;
        }
        return condLogString;
    }

    /**
     * Handle back icon and button Menu
     *
     * @param pView buttons' view
     */
    @Nullable
    @OnClick({R.id.rlBackIcon, R.id.tv_action_bar_right, R.id.ln_history})
    public void onClick(View pView) {
        Bundle bundle = new Bundle();
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            case R.id.tv_action_bar_right:
                bundle.putByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY, mAircraftCode);
                bundle.putString(MCCPilotLogConst.ADD_EDIT_MODE, MCCPilotLogConst.EDIT_MODE);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, AircraftAddFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.ln_history:
                bundle.putByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY, mAircraftCode);
                replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, LogbooksListFragment.class, bundle, FLAG_ADD_STACK);
                break;
            default:
                break;
        }
    }

    private boolean equalToCondLog(int testNumber) {
        boolean result = false;
        switch (testNumber) {
            case AircraftInfoConst.XC:
                result = true;
                break;
            case AircraftInfoConst.RELIEF:
                result = true;
                break;
            case AircraftInfoConst.ACT_INSTR:
                result = true;
                break;
            case AircraftInfoConst.IFR:
                result = true;
                break;
            case AircraftInfoConst.SIM_INSTR:
                result = true;
                break;
            case AircraftInfoConst.MIN_U1:
                result = true;
                break;
            case AircraftInfoConst.MIN_U2:
                result = true;
                break;
            case AircraftInfoConst.MIN_U3:
                result = true;
                break;
            case AircraftInfoConst.MIN_U4:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
     /*   if(mBundle!=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initView();
                    initDataAircraft(mBundle);
                }
            });

        }*/

    }

    @Override
    public void onResume() {
        if (mBundle != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ButterKnife.bind(this, mViewContainer);
                    initView();
                    initDataAircraft(mBundle);
                }
            });

        }
        super.onResume();
    }
}
