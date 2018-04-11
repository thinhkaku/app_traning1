package aero.pilotlog.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.SignatureActivity;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.common.RouteInfoCalculator;
import aero.pilotlog.common.StateKey;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Flight;
import aero.pilotlog.databases.entities.ImagePic;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZDelay;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.interfaces.IItemsFlight;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.PhotoUtils;
import aero.pilotlog.utilities.StorageUtils;
import aero.pilotlog.utilities.TimeUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.utilities.ValidationUtils;
import aero.pilotlog.widgets.ItemsFlightView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightAddsFragment extends BaseMCCFragment implements IItemsFlight, IAsyncTaskCallback {
    @Nullable
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Nullable
    @Bind(R.id.tvTitle)
    TextView mtvTitle2;
    @Nullable
    @Bind(R.id.rlBackIcon)
    RelativeLayout mHeaderRlBack;
    /* @Bind(R.id.ibMenu)
     ImageButton mHeaderIbMenu;*/
    @Nullable
    @Bind(R.id.lnEdit)
    LinearLayout lnEdit;
    @Nullable
    @Bind(R.id.btnRight)
    Button btnRight;

    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    @Bind(R.id.ll_flight_configure)
    LinearLayout mBottomBar;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Nullable
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Nullable
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Nullable
    @Bind(R.id.tv_action_bar_left)
    TextView mTvCancel;
    @Nullable
    @Bind(R.id.tv_action_bar_right)
    TextView mTvSave;
    @Nullable
    @Bind(R.id.sv_flight_add)
    ScrollView mSvFlightAdd;

    //Flight Route
    @Bind(R.id.item_flight_route_date)
    ItemsFlightView mItemFlightRouteDate;
    @Bind(R.id.item_flight_route_aircraft)
    ItemsFlightView mItemFlightRouteAircraft;
    @Bind(R.id.item_flight_route_departure)
    ItemsFlightView mItemFlightRouteDeparture;
    @Bind(R.id.item_flight_route_arrival)
    ItemsFlightView mItemFlightRouteArrival;
    @Bind(R.id.item_flight_runway_departure)
    ItemsFlightView mItemFlightRouteRunwayDeparture;
    @Bind(R.id.item_flight_runway_arrival)
    ItemsFlightView mItemFlightRouteRunwayArrival;
    @Bind(R.id.item_flight_pairing)
    ItemsFlightView mItemFlightRoutePairing;
    @Bind(R.id.item_flight_flightno)
    ItemsFlightView mItemFlightRouteFlightNo;

    //Flight Hours
    @Bind(R.id.item_flight_hobbs_out)
    ItemsFlightView mItemFlightHobbsOut;
    @Bind(R.id.item_flight_hobbs_in)
    ItemsFlightView mItemFlightHobbsIn;
    @Bind(R.id.item_flight_off_block)
    ItemsFlightView mItemFlightOffBlock;
    @Bind(R.id.item_flight_scheduled_out)
    ItemsFlightView mItemFlightScheduledOut;
    @Bind(R.id.item_flight_scheduled_in)
    ItemsFlightView mItemFlightScheduledIn;
    @Bind(R.id.item_flight_on_block)
    ItemsFlightView mItemFlightOnBlock;
    @Bind(R.id.item_flight_total)
    ItemsFlightView mItemFlightTotal;
    @Bind(R.id.item_flight_take_off)
    ItemsFlightView mItemFlightTakeOff;
    @Bind(R.id.item_flight_landing)
    ItemsFlightView mItemFlightLanding;
    @Bind(R.id.item_flight_pic)
    ItemsFlightView mItemFlightPic;
    @Bind(R.id.item_flight_picus)
    ItemsFlightView mItemFlightPicUS;
    @Bind(R.id.item_flight_copilot)
    ItemsFlightView mItemFlightCopilot;
    @Bind(R.id.item_flight_dual)
    ItemsFlightView mItemFlightDual;
    @Bind(R.id.item_flight_instructor)
    ItemsFlightView mItemFlightInstructor;
    @Bind(R.id.item_flight_examiner)
    ItemsFlightView mItemFlightExaminer;
    @Bind(R.id.item_flight_relief)
    ItemsFlightView mItemFlightRelief;
    @Bind(R.id.item_flight_night)
    ItemsFlightView mItemFlightNight;
    @Bind(R.id.item_flight_ifr)
    ItemsFlightView mItemFlightIFR;
    @Bind(R.id.item_flight_actual_instrument)
    ItemsFlightView mItemFlightActualInstrument;
    @Bind(R.id.item_flight_simulated_instrument)
    ItemsFlightView mItemFlightSimulatedInstrument;
    @Bind(R.id.item_flight_xc)
    ItemsFlightView mItemFlightXC;
    @Bind(R.id.item_flight_user_active1)
    ItemsFlightView mItemFlightUserActive1;
    @Bind(R.id.item_flight_user_active2)
    ItemsFlightView mItemFlightUserActive2;
    @Bind(R.id.item_flight_user_active3)
    ItemsFlightView mItemFlightUserActive3;
    @Bind(R.id.item_flight_user_active4)
    ItemsFlightView mItemFlightUserActive4;

    //Flight Currencies
    @Bind(R.id.item_flight_currencies_task)
    ItemsFlightView mItemFlightCurrenciesTask;
    @Bind(R.id.item_flight_currencies_today)
    ItemsFlightView mItemFlightCurrenciesTOday;
    @Bind(R.id.item_flight_currencies_tonight)
    ItemsFlightView mItemFlightCurrenciesTOnight;
    @Bind(R.id.item_flight_currencies_ldgday)
    ItemsFlightView mItemFlightCurrenciesLDGDay;
    @Bind(R.id.item_flight_currencies_ldgnight)
    ItemsFlightView mItemFlightCurrenciesLDGNight;
    @Bind(R.id.item_flight_currencies_holding)
    ItemsFlightView mItemFlightCurrenciesHolding;
    @Bind(R.id.item_flight_currencies_fuel)
    ItemsFlightView mItemFlightCurrenciesFuel;
    @Bind(R.id.item_flight_currencies_fuel_planned)
    ItemsFlightView mItemFlightCurrenciesFuelPlanned;
    @Bind(R.id.item_flight_currencies_fuel_used)
    ItemsFlightView mItemFlightCurrenciesFuelUsed;
    @Bind(R.id.item_flight_currencies_pax)
    ItemsFlightView mItemFlightCurrenciesPax;
    @Bind(R.id.item_flight_currencies_user_n1)
    ItemsFlightView mItemFlightCurrenciesUserN1;
    @Bind(R.id.item_flight_currencies_user_n2)
    ItemsFlightView mItemFlightCurrenciesUserN2;
    @Bind(R.id.item_flight_currencies_user_n3)
    ItemsFlightView mItemFlightCurrenciesUserN3;
    @Bind(R.id.item_flight_currencies_lifts)
    ItemsFlightView mItemFlightCurrenciesLifts;
    @Bind(R.id.item_flight_currencies_deicing)
    ItemsFlightView mItemFlightCurrenciesDeicing;
    @Bind(R.id.item_flight_currencies_delay)
    ItemsFlightView mItemFlightCurrenciesDelay;
    @Bind(R.id.item_flight_currencies_approach1)
    ItemsFlightView mItemFlightCurrenciesApproach;
    @Bind(R.id.item_flight_currencies_operation)
    ItemsFlightView mItemFlightCurrenciesOperation;
    @Bind(R.id.item_flight_currencies_glider)
    ItemsFlightView mItemFlightCurrenciesGlider;
    @Bind(R.id.item_flight_currencies_signbox)
    ItemsFlightView mItemFlightCurrenciesSignBox;
    @Bind(R.id.item_flight_currencies_signpen)
    ItemsFlightView mItemFlightCurrenciesSignPen;

    //Flight Crew
    @Bind(R.id.item_flight_crew_pic)
    ItemsFlightView mItemFlightCrewPic;
    @Bind(R.id.item_flight_crew_2nd)
    ItemsFlightView mItemFlightCrew2nd;
    @Bind(R.id.item_flight_crew_3rd)
    ItemsFlightView mItemFlightCrew3rd;
    @Bind(R.id.item_flight_crew_4th)
    ItemsFlightView mItemFlightCrew4th;
    @Bind(R.id.item_flight_crew_list)
    ItemsFlightView mItemFlightCrewList;

    //Flight Log
    @Bind(R.id.item_flight_log_remarks)
    ItemsFlightView mItemFlightLogRemarks;
    @Bind(R.id.item_flight_log_instruction)
    ItemsFlightView mItemFlightLogInstruction;
    @Bind(R.id.item_flight_report)
    ItemsFlightView mItemFlightLogFlightReport;
    @Bind(R.id.route)
    LinearLayout mLlRoute;
    @Bind(R.id.hours)
    LinearLayout mLlHours;
    @Bind(R.id.currencies)
    LinearLayout mLlCurrencies;
    @Bind(R.id.crew)
    LinearLayout mLlCrew;
    @Bind(R.id.log)
    LinearLayout mLlLogReport;
    public static final int RED_COLOR = android.R.color.holo_red_light;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.US);
    private int mMode = 1; /*Default mode is Add */
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;
    public static final int MODE_RETURN = 3;
    public static final int MODE_NEXT = 7;
    public static final int MODE_PASTE_DATA = 4;
    public static final int MODE_FLIGHT_VIEW = 5;
    public static final int MODE_EDIT_FLIGHT_VIEW = 6;
    public static final int CREW_PIC = 1;
    public static final int CREW_2ND = 2;
    public static final int CREW_3RD = 3;
    public static final int CREW_4TH = 4;
    private String mZLaunchCodes, mZApproachCodes, mZDelayCodes, mZOperationCodes;
    private boolean isSettingIata = false;
    private Airfield mAirfieldArrival, mAirfieldDeparture;
    private Aircraft mAircraft;
    private Pilot mPilot1, mPilot2, mPilot3, mPilot4;
    private boolean isLogTimeDecimal;
    private Calendar mFlightDate = Calendar.getInstance();
    private int mArrTimeSCH, mDepTimeSCH, mToOff, mLdgOn;
    private String mTotalSCH;
    private List<ItemsFlightView> listItemFlightViews;
    private Flight mFlight;
    private FlightUtils flightUtils;
    private LoadDataTask mLoadDataTask;
    private boolean isAutoLoadXC;
    private int actualInstrumentPercent, reliefPercent;
    private boolean isShowTopKey = false;
    private String keyboardMemoryTemp1 = "";
    private FlightUtils.TimeMode timeMode = FlightUtils.TimeMode.LOCAL;
    private FlightUtils.TimeMode dateMode = FlightUtils.TimeMode.LOCAL;
    private TimeZone timeZoneDeparture, timeZoneArrival;
    private int timeZoneCodeDeparture = 0, timeZoneCodeArrival = 0;
    private boolean isShowSchedule = false;
    private final int AIRCRAFT_TYPE_AIRCRAFT = 1;
    private final int AIRCRAFT_TYPE_SIMULATOR = 2;
    private final int AIRCRAFT_TYPE_DRONE = 3;
    //private boolean isNotPreserveAccuracy = false;
    private String settingNightMode;
    private boolean isChange;
    private Bundle mBundle;

    private enum PageGoTo {
        AIRFIELD,
        AIRCRAFT,
        DELAY,
        APPROACH,
        LAUNCH,
        OPERATION,
        PILOT1,
        PILOT2,
        PILOT3,
        PILOT4,
        SIGN_PEN,
        NONE
    }

    PageGoTo pageGoTo = PageGoTo.NONE;

    public FlightAddsFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        mBundle = getArguments();
        mMode = mBundle.getInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE);
        if (mMode == MODE_FLIGHT_VIEW)
            return R.layout.layout_action_bar;
        else
            return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_add_flight;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_flight_footer_tablet;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mIbMenu.setVisibility(View.GONE);
        mBundle = getArguments();
        mMode = mBundle.getInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE);
        if (mMode == MODE_FLIGHT_VIEW) {
            if (!isTablet()) {
                mtvTitle2.setText("Back");
                mIbLeft.setVisibility(View.VISIBLE);
            } else {
                mtvTitle2.setText("");
                //Disable header back icon for tablet
                mHeaderRlBack.setClickable(false);
            }
            //btnRight.setVisibility(View.VISIBLE);
            lnEdit.setVisibility(View.VISIBLE);
        }

        mDatabaseManagerV5 = aero.pilotlog.databases.manager.DatabaseManager.getInstance(mActivity);
        //createListItems();
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
        //createListItems();
        //initView();
    }

    @Override
    public void onClickIconTop(ItemsFlightView pView) {
        if (pView == mItemFlightCrewPic)
            mPilot1 = flightUtils.onClickButtonTopPilot(pView, mItemFlightCrewPic, mItemFlightCrew2nd, mItemFlightCrew3rd, mItemFlightCrew4th);
        else if (pView == mItemFlightCrew2nd)
            mPilot2 = flightUtils.onClickButtonTopPilot(pView, mItemFlightCrewPic, mItemFlightCrew2nd, mItemFlightCrew3rd, mItemFlightCrew4th);
        else if (pView == mItemFlightCrew3rd)
            mPilot3 = flightUtils.onClickButtonTopPilot(pView, mItemFlightCrewPic, mItemFlightCrew2nd, mItemFlightCrew3rd, mItemFlightCrew4th);
        else if (pView == mItemFlightCrew4th)
            mPilot4 = flightUtils.onClickButtonTopPilot(pView, mItemFlightCrewPic, mItemFlightCrew2nd, mItemFlightCrew3rd, mItemFlightCrew4th);
    }

    @Override
    public void onClickIconBottom(ItemsFlightView itemsFlightView) {
        Bundle bundle = new Bundle();
        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
        bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_ADD);
        switch (itemsFlightView.getId()) {
            case R.id.item_flight_route_date:
                flightUtils.displayDateDialog(mItemFlightRouteDate.getDescription(), mItemFlightRouteDate, mFlightDate);
                break;
            case R.id.item_flight_route_aircraft:
                pageGoTo = PageGoTo.AIRCRAFT;
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AircraftListFragment.class, bundle, true);
                break;
            case R.id.item_flight_route_departure:
                pageGoTo = PageGoTo.AIRFIELD;
                bundle.putBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL, true);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldsListFragment.class, bundle, true);
                break;
            case R.id.item_flight_route_arrival:
                pageGoTo = PageGoTo.AIRFIELD;
                bundle.putBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL, false);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AirfieldsListFragment.class, bundle, true);
                break;
            case R.id.item_flight_crew_pic:
                pageGoTo = PageGoTo.PILOT1;
                bundle.putBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, true);
                bundle.putInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, CREW_PIC);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, PilotListFragment.class, bundle, true);
                break;
            case R.id.item_flight_crew_2nd:
                pageGoTo = PageGoTo.PILOT2;
                bundle.putBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, true);
                bundle.putInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, CREW_2ND);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, PilotListFragment.class, bundle, true);
                break;
            case R.id.item_flight_crew_3rd:
                pageGoTo = PageGoTo.PILOT3;
                bundle.putBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, true);
                bundle.putInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, CREW_3RD);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, PilotListFragment.class, bundle, true);
                break;
            case R.id.item_flight_crew_4th:
                pageGoTo = PageGoTo.PILOT4;
                bundle.putBoolean(MCCPilotLogConst.IS_ADD_NEW_PILOT_FOR_FLIGHT, true);
                bundle.putInt(MCCPilotLogConst.SELECT_LIST_PILOT_INDICATOR, CREW_4TH);
                this.replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, PilotListFragment.class, bundle, true);
                break;
            case R.id.item_flight_currencies_signpen:
                pageGoTo = PageGoTo.SIGN_PEN;
                showSignatureScreen();
                break;
            case R.id.item_flight_currencies_delay:
                pageGoTo = PageGoTo.DELAY;
                bundle.putInt(MCCPilotLogConst.DELAY_OR_TAILS, MCCPilotLogConst.DELAY_ADAPTER);
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, DelayListFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_flight_currencies_approach1:
                pageGoTo = PageGoTo.APPROACH;
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, ApproachFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_flight_currencies_operation:
                pageGoTo = PageGoTo.OPERATION;
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, OperationFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_flight_currencies_glider:
                pageGoTo = PageGoTo.LAUNCH;
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, GliderLaunchFragment.class, bundle, FLAG_ADD_STACK);
                break;
            case R.id.item_flight_pic:
            case R.id.item_flight_picus:
            case R.id.item_flight_copilot:
            case R.id.item_flight_dual:
                checkedPicPicUsCoDual(itemsFlightView);
                break;
            case R.id.item_flight_instructor:
            case R.id.item_flight_examiner:
            case R.id.item_flight_ifr:
            case R.id.item_flight_simulated_instrument:
            case R.id.item_flight_user_active1:
            case R.id.item_flight_user_active2:
            case R.id.item_flight_user_active3:
            case R.id.item_flight_user_active4:
            case R.id.item_flight_xc:
                flightUtils.checkedItems(itemsFlightView, mItemFlightTotal);
                break;
            case R.id.item_flight_currencies_task:
                flightUtils.checkedItemTask(itemsFlightView, mItemFlightCurrenciesTOday, mItemFlightCurrenciesTOnight,
                        mItemFlightCurrenciesLDGDay, mItemFlightCurrenciesLDGNight, routeInfoCalculator, mAircraft,
                        mItemFlightCopilot, mItemFlightPicUS, mItemFlightTotal, mItemFlightActualInstrument, actualInstrumentPercent);
                break;
            case R.id.item_flight_actual_instrument:
                if (!TextUtils.isEmpty(mItemFlightTotal.getDescription()) && TextUtils.isEmpty(itemsFlightView.getDescription())) {
                    if (actualInstrumentPercent != 0) {
                        flightUtils.autoLoadActualInstrument(itemsFlightView, mItemFlightTotal, mItemFlightCurrenciesTask, actualInstrumentPercent, isLogTimeDecimal);
                    } else {
                        itemsFlightView.setDescription(mItemFlightTotal.getDescription());
                        itemsFlightView.setMinutesData(mItemFlightTotal.getMinutesData());
                    }
                } else
                    itemsFlightView.clearValue();
                break;
            case R.id.item_flight_relief:
                if (!TextUtils.isEmpty(mItemFlightTotal.getDescription()) && TextUtils.isEmpty(itemsFlightView.getDescription())) {
                    if (reliefPercent != 0) {
                        flightUtils.autoLoadRelief(itemsFlightView, mItemFlightTotal, reliefPercent, isLogTimeDecimal);
                    } else {
                        itemsFlightView.setDescription(mItemFlightTotal.getDescription());
                        itemsFlightView.setMinutesData(mItemFlightTotal.getMinutesData());
                    }
                } else
                    itemsFlightView.clearValue();
                break;

        }
    }

    private void finishOutInHours(String value, ItemsFlightView itemsFlightView, TimeZone timeZone, int timeZoneCode) {
        String timeDisplay = flightUtils.getTimeDisplay(value, false, false, isLogTimeDecimal);
        itemsFlightView.setDescription(timeDisplay);
        flightUtils.calcAndDisplayTimeMode(itemsFlightView, timeMode, timeZone, timeZoneCode, mFlightDate);
        //if (mItemFlightHobbsOut.getVisibility() == View.GONE || TextUtils.isEmpty(mItemFlightHobbsOut.getDescription())
        //        || TextUtils.isEmpty(mItemFlightHobbsIn.getDescription())){
        flightUtils.calcTotalTime(mItemFlightOffBlock, mItemFlightOnBlock, mItemFlightTotal, isLogTimeDecimal, timeZoneDeparture, timeZoneArrival, timeZoneCodeDeparture, timeZoneCodeArrival, mFlightDate);
        //}
        if (isNullOrEmptyItemsFlightView(mItemFlightOffBlock) && isNullOrEmptyItemsFlightView(mItemFlightOnBlock))
            flightUtils.calcTotalTime(mItemFlightHobbsOut, mItemFlightHobbsIn, mItemFlightTotal);
    }

    private void finishHobbs(String value, ItemsFlightView itemsFlightView) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(7);
        itemsFlightView.getEdtDescription().setFilters(fArray);
        value = value.replace(".", "").replace(",", "");
       /* if (value.length() > 6) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_hobbs_title, R.string.invalid_time_content).show();
            Double doubleValue = 99999.9;
            DecimalFormat hobbsFormat = new DecimalFormat("#.#");
            value = hobbsFormat.format(doubleValue);
        } else {*/
        if (!TextUtils.isEmpty(value)) {
            value = value.substring(0, value.length() - 1) + "." + value.substring(value.length() - 1, value.length());
            Double doubleValue = Utils.parseDouble(value);
            DecimalFormat hobbsFormat = new DecimalFormat("#.#");
            hobbsFormat.setMinimumFractionDigits(1);
            value = hobbsFormat.format(doubleValue);
        } else {
            value = "";
            //flightUtils.calcTotalTime(mItemFlightOffBlock, mItemFlightOnBlock, mItemFlightTotal, isLogTimeDecimal, timeZoneDeparture, timeZoneArrival, timeZoneCodeDeparture, timeZoneCodeArrival, mFlightDate);
        }
        //}
        itemsFlightView.setDescription(value);
        if (!checkEnableField(mItemFlightOffBlock) || isNullOrEmptyItemsFlightView(mItemFlightOffBlock) || isNullOrEmptyItemsFlightView(mItemFlightOnBlock))
            //        || TextUtils.isEmpty(mItemFlightHobbsIn.getDescription())){
            flightUtils.calcTotalTime(mItemFlightHobbsOut, mItemFlightHobbsIn, mItemFlightTotal);
    }

    private void finishTakeOffLanding(String value, ItemsFlightView itemsFlightView, TimeZone timeZone, int timeZoneCode) {
        String timeDisplay = flightUtils.getTimeDisplay(value, false, false, isLogTimeDecimal);
        itemsFlightView.setDescription(timeDisplay);
        flightUtils.calcAndDisplayTimeMode(itemsFlightView, timeMode, timeZone, timeZoneCode, mFlightDate);
        String takeOffHours = flightUtils.convertTimeToUTC(mItemFlightTakeOff.getDescription(), timeMode, timeZoneDeparture, timeZoneCode, mFlightDate);
        String landingHours = flightUtils.convertTimeToUTC(mItemFlightLanding.getDescription(), timeMode, timeZoneArrival, timeZoneCode, mFlightDate);
        int totalAir = TimeUtils.calcTotalMinute(takeOffHours, landingHours, flightUtils.getAccuracy());
        if (totalAir < 0) totalAir = 0;
        mItemFlightTotal.setMinutesDataOnlyForMinArr(totalAir);
        mItemFlightTotal.setTotalArr(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(totalAir), isLogTimeDecimal), isLogTimeDecimal));
    }

    private void finishSchedule(String value, ItemsFlightView itemsFlightView, TimeZone timeZone, int timeZoneCode) {
        String timeDisplay = flightUtils.getTimeDisplay(value, false, false, isLogTimeDecimal);
        itemsFlightView.setDescription(timeDisplay);
       /* if(mAircraft!=null && mAircraft.getDeviceCode()>1){
            timeZone = timeZoneDeparture;
        }*/
        if (itemsFlightView.getId() == mItemFlightScheduledOut.getId()) {
            mItemFlightOffBlock.setScheduleHour(flightUtils.getScheduledHour(timeDisplay));
        } else {
            mItemFlightOnBlock.setScheduleHour(flightUtils.getScheduledHour(timeDisplay));
        }
        flightUtils.calcAndDisplayTimeMode(itemsFlightView, timeMode, timeZone, timeZoneCode, mFlightDate);
        String scheduleOutHours = flightUtils.convertTimeToUTC(mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
        String scheduleInHours = flightUtils.convertTimeToUTC(mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
        int totalSched = TimeUtils.calcTotalMinute(scheduleOutHours, scheduleInHours, flightUtils.getAccuracy());
        if (totalSched < 0) totalSched = 0;
        mItemFlightTotal.setTotalSchedule(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(totalSched), isLogTimeDecimal), isLogTimeDecimal));
    }

    private void finishLogAirfield(String value, ItemsFlightView itemsFlightView, boolean finishDeparture) {
        if (!flightUtils.validateAirfieldIdentifier(value) && !TextUtils.isEmpty(value)) {
            if (finishDeparture)
                itemsFlightView.clearValue();
            MccDialog.getOkAlertDialog(mActivity, String.format(getString(R.string.airfield_unknown_title), value), getString(R.string.airfield_unknown_message)).show();
            return;
        }
        Airfield airfield = flightUtils.setTextAirfield(value, itemsFlightView, isSettingIata);
        if (finishDeparture)
            mAirfieldDeparture = airfield;
        else mAirfieldArrival = airfield;
        setAirfield(airfield, finishDeparture, false);


    }

    @Override
    public void onFinishInput(ItemsFlightView itemsFlightView, boolean isChangeMinuteTotal) {
        itemsFlightView.setSaveState(itemsFlightView.getDescription().trim());
        String value = itemsFlightView.getDescription().trim();
        if (!isChange) isChange = true;
        if (itemsFlightView.getFlightType() == ItemsFlightView.ITEM_FLIGHT_HOURS) {
            switch (itemsFlightView.getId()) {
                case R.id.item_flight_total:
                    finishInputTotalTime(itemsFlightView, value, isChangeMinuteTotal);
                    break;
                case R.id.item_flight_hobbs_out:
                    finishHobbs(value, itemsFlightView);
                    break;
                case R.id.item_flight_hobbs_in:
                    finishHobbs(value, itemsFlightView);
                    break;
                case R.id.item_flight_off_block:
                    finishOutInHours(value, itemsFlightView, timeZoneDeparture, timeZoneCodeDeparture);
                    break;
                case R.id.item_flight_on_block:
                    finishOutInHours(value, itemsFlightView, timeZoneArrival, timeZoneCodeArrival);
                    break;
                case R.id.item_flight_take_off:
                    finishTakeOffLanding(value, itemsFlightView, timeZoneDeparture, timeZoneCodeDeparture);
                    break;
                case R.id.item_flight_landing:
                    finishTakeOffLanding(value, itemsFlightView, timeZoneArrival, timeZoneCodeArrival);
                    break;
                case R.id.item_flight_scheduled_out:
                    finishSchedule(value, itemsFlightView, timeZoneDeparture, timeZoneCodeDeparture);
                    break;
                case R.id.item_flight_scheduled_in:
                    finishSchedule(value, itemsFlightView, timeZoneArrival, timeZoneCodeArrival);
                    break;
                case R.id.item_flight_pic:
                case R.id.item_flight_picus:
                    String timeDisplay1 = flightUtils.getTimeDisplay(value, isLogTimeDecimal, true, isLogTimeDecimal);
                    itemsFlightView.setDescription(timeDisplay1);
                    if (!isNullOrEmptyItemsFlightView(itemsFlightView))
                        flightUtils.autoLoadTask(mItemFlightCurrenciesTask, true, mAircraft);
                    itemsFlightView.setMinutesData(TimeUtils.convertHourToMin(timeDisplay1));
                    break;
               /* case R.id.item_flight_pic:
                case R.id.item_flight_picus:
                    flightUtils.autoLoadTask(mItemFlightCurrenciesTask, true, false);
                    break;*/
                default:
                    String timeDisplay = flightUtils.getTimeDisplay(value, isLogTimeDecimal, true, isLogTimeDecimal);
                    itemsFlightView.setDescription(timeDisplay);
                    itemsFlightView.setMinutesData(TimeUtils.convertHourToMin(timeDisplay));
                    break;
            }
        } else {
            switch (itemsFlightView.getId()) {
                case R.id.item_flight_route_aircraft:
                    if (value.length() == 0) {
                        break;
                    }
                    if (value.contains("-")) {
                        value = value.replace("-", "");
                    }
                    final String valueFinal = value;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<Aircraft> listAircraft = mDatabaseManagerV5.getAircraftByRefSearch(valueFinal);
                                    int aircraftListSize = listAircraft.size();
                                    if (aircraftListSize == 0) {
                                        mItemFlightRouteAircraft.setFootNote(getString(R.string.not_found_aircraft), RED_COLOR);
                                        mAircraft = null;
                                    } else if (aircraftListSize == 1) {
                                        mItemFlightRouteAircraft.setDescription(listAircraft.get(0).getReference());
                                        if (TextUtils.isEmpty(listAircraft.get(0).getModel())) {
                                            mItemFlightRouteAircraft.setFootNote(listAircraft.get(0).getSubModel());
                                        } else if (TextUtils.isEmpty(listAircraft.get(0).getSubModel())) {
                                            mItemFlightRouteAircraft.setFootNote(listAircraft.get(0).getModel());
                                        } else {
                                            mItemFlightRouteAircraft.setFootNote(listAircraft.get(0).getModel() + "-" + listAircraft.get(0).getSubModel());
                                        }
                                        setAircraft(listAircraft.get(0));
                                    } else {
                                        Bundle b = new Bundle();
                                        b.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
                                        b.putString(MCCPilotLogConst.AIRCRAFT_REF_SEARCH, valueFinal);
                                        replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, AircraftListFragment.class, b, true);
                                    }
                                }
                            });
                        }
                    });

                    break;
                case R.id.item_flight_route_departure:
                    finishLogAirfield(value, itemsFlightView, true);
                    break;
                case R.id.item_flight_route_arrival:
                    finishLogAirfield(value, itemsFlightView, false);
                    break;
                case R.id.item_flight_currencies_fuel_used:
                    if (TextUtils.isEmpty(mItemFlightCurrenciesFuel.getDescription())) return;
                    if (TextUtils.isEmpty(mItemFlightCurrenciesFuelUsed.getDescription())) return;
                    int fuel = Integer.parseInt(mItemFlightCurrenciesFuel.getDescription());
                    int used = Integer.parseInt(mItemFlightCurrenciesFuelUsed.getDescription());
                    if (used < 0) {
                        used = fuel + used;
                        if (used < 0) used = 0;
                        mItemFlightCurrenciesFuelUsed.setDescription(String.valueOf(used));
                    }
                    break;
                case R.id.item_flight_currencies_fuel:
                    if (TextUtils.isEmpty(mItemFlightCurrenciesFuel.getDescription())) return;
                    if (TextUtils.isEmpty(mItemFlightCurrenciesFuelUsed.getDescription())) return;
                    int fuel2 = Integer.parseInt(mItemFlightCurrenciesFuel.getDescription());
                    int used2 = Integer.parseInt(mItemFlightCurrenciesFuelUsed.getDescription());
                    if (used2 < 0) {
                        used2 = fuel2 + used2;
                        if (used2 < 0) {
                            used2 = 0;
                        }
                        mItemFlightCurrenciesFuelUsed.setDescription(String.valueOf(used2));

                    }
                    break;
                case R.id.item_flight_currencies_fuel_planned:
                    break;
                case R.id.item_flight_crew_pic:
                    mPilot1 = flightUtils.searchAndSetPilot(value, mItemFlightCrewPic);
                    break;
                case R.id.item_flight_crew_2nd:
                    mPilot2 = flightUtils.searchAndSetPilot(value, mItemFlightCrew2nd);
                    break;
                case R.id.item_flight_crew_3rd:
                    mPilot3 = flightUtils.searchAndSetPilot(value, mItemFlightCrew3rd);
                    break;
                case R.id.item_flight_crew_4th:
                    mPilot4 = flightUtils.searchAndSetPilot(value, mItemFlightCrew4th);
                    break;
                case R.id.item_flight_crew_list:
                    break;
            }
        }
    }

    private void finishInputTotalTime(final ItemsFlightView itemsFlightView, String value, final boolean isChangeMinuteTotal) {
        final String timeDisplay = flightUtils.getTimeDisplay(value, isLogTimeDecimal, true, isLogTimeDecimal);
        final int minTotal = TimeUtils.convertHourToMin(timeDisplay);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemsFlightView.setDescription(timeDisplay);
                if (isChangeMinuteTotal)
                    itemsFlightView.setMinutesData(minTotal);
            }
        });

        if (isAutoLoadXC)
            flightUtils.autoLoadXC(mAirfieldDeparture, mAirfieldArrival, mAircraft, mItemFlightXC, mItemFlightTotal);
        flightUtils.autoLoadActualInstrument(mItemFlightActualInstrument, mItemFlightTotal, mItemFlightCurrenciesTask,
                actualInstrumentPercent, isLogTimeDecimal);
        flightUtils.autoLoadRelief(mItemFlightRelief, mItemFlightTotal, reliefPercent, isLogTimeDecimal);
        flightUtils.autoLoadConditionTime(mItemFlightUserActive1, mItemFlightUserActive2, mItemFlightUserActive3,
                mItemFlightUserActive4, mItemFlightIFR, mItemFlightSimulatedInstrument, mItemFlightTotal);
        flightUtils.autoLoadFunctionTime(mAircraft, mItemFlightPic, mItemFlightCopilot, mItemFlightDual, mItemFlightInstructor,
                mItemFlightPicUS, mItemFlightExaminer, mItemFlightCurrenciesTask, mItemFlightTotal, mItemFlightRelief, mItemFlightRelief.getMinutesData() /*100 - reliefPercent*/, isLogTimeDecimal);
        if (!TextUtils.isEmpty(itemsFlightView.getDescription()) && !TextUtils.isEmpty(mItemFlightOffBlock.getDescription()) && !TextUtils.isEmpty(mItemFlightOnBlock.getDescription()))
            setDataForNight();
    }

    @Override
    public void onShowInfoPage(int flightItemId) {
        switch (flightItemId) {
            case R.id.item_flight_off_block:
                setInOutScheduled(true);
                break;
            case R.id.item_flight_on_block:
                setInOutScheduled(false);
                break;
        }
    }

    private void setupItemForDroneOrSimulator(final ItemsFlightView item, int aircraftType) {
        if (item.getId() != R.id.item_flight_crew_3rd && item.getId() != R.id.item_flight_crew_4th && item.getId() != R.id.item_flight_route_departure) {
            if (listItemFlightViews.indexOf(item) > -1) {
                listItemFlightViews.remove(item);
               /* mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
                item.clearValue();
                item.setVisibility(View.GONE);
                  /*  }
                });*/
            }
        }
        switch (aircraftType) {
            case AIRCRAFT_TYPE_DRONE:
                switch (item.getId()) {
                    case R.id.item_flight_crew_3rd:
                       /* mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setTextTitle("3rd PILOT");
                           /* }
                        });*/
                        break;
                    case R.id.item_flight_crew_4th:
                       /* mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setTextTitle("4th PILOT");
                        /*    }
                        });*/
                        break;
                    case R.id.item_flight_route_departure:
                        /*mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setTextTitle("LAUNCH SITE");
                        item.setViewLineBorder(View.GONE);
                          /*  }
                        });*/
                        break;
                }
                break;
            case AIRCRAFT_TYPE_SIMULATOR:
                switch (item.getId()) {
                    case R.id.item_flight_crew_3rd:
                    case R.id.item_flight_crew_4th:
                       /* mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setTextTitle("INSTRUCTOR");
                           /* }
                        });*/
                        break;
                    case R.id.item_flight_route_departure:
                       /* mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setTextTitle("SIMULATOR LOCATION");
                        item.setViewLineBorder(View.GONE);
                          /*  }
                        });*/
                        break;
                }
                break;
        }
    }

    private void setupItem(final ItemsFlightView item, int settingCode) {
        SettingConfig settingConfig = mDatabaseManagerV5.getSetting(settingCode);
        if (settingConfig == null) return;
        if (settingConfig.getData().equals("1")) {
            listItemFlightViews.add(item);
            /*mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {*/
            item.setVisibility(View.VISIBLE);
            if (item.getId() == R.id.item_flight_runway_departure)
                item.clearFocus();
            /*    }
            });*/
            switch (item.getId()) {
                case R.id.item_flight_flightno:
                    setupItem(mItemFlightRoutePairing, 418);
                    final SettingConfig settingFlightNo = mDatabaseManagerV5.getSetting(7);
                    if (settingFlightNo != null && !TextUtils.isEmpty(settingFlightNo.getData())) {
                       /* mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                        item.setDescription(settingFlightNo.getData());
                       /*     }
                        });*/
                    }
                    break;
                case R.id.item_flight_currencies_task:
                   /* mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flightUtils.autoLoadTask(mItemFlightCurrenciesTask, false, mAircraft);
                        }
                    });*/
                    break;
                case R.id.item_flight_xc:
                    String autoLoadXC = mDatabaseManagerV5.getSetting(492).getData();
                    if (!TextUtils.isEmpty(autoLoadXC) && autoLoadXC.equals("1"))
                        isAutoLoadXC = true;
                    break;
                case R.id.item_flight_actual_instrument:
                    String autoLoadActualInstrument = mDatabaseManagerV5.getSetting(493).getData();
                    if (!TextUtils.isEmpty(autoLoadActualInstrument)) {
                        actualInstrumentPercent = Integer.parseInt(autoLoadActualInstrument);
                    }
                    break;
                case R.id.item_flight_relief:
                    String autoLoadRelief = mDatabaseManagerV5.getSetting(494).getData();
                    if (!TextUtils.isEmpty(autoLoadRelief)) {
                        reliefPercent = Integer.parseInt(autoLoadRelief);
                    }
                    break;
                case R.id.item_flight_scheduled_out:
                    isShowSchedule = true;
                    setInOutScheduled(true);
                    //mItemFlightTotal.setVisibleScheduleHour();
                    mItemFlightOffBlock.setVisibleIvInfo(View.VISIBLE);
                    break;
                case R.id.item_flight_scheduled_in:
                    setInOutScheduled(false);
                    mItemFlightOnBlock.setVisibleIvInfo(View.VISIBLE);
                    break;
                case R.id.item_flight_crew_3rd:
                    /*mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {*/
                    item.setTextTitle("3rd PILOT");
                  /*      }
                    });*/
                    break;
                case R.id.item_flight_crew_4th:
                   /* mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {*/
                    item.setTextTitle("4th PILOT");
                     /*   }
                    });*/
                    break;
            }
        } else {
            switch (item.getId()) {
                case R.id.item_flight_runway_departure:
                    mItemFlightRouteDeparture.setViewLineBorder(View.VISIBLE);
                    break;
                case R.id.item_flight_runway_arrival:
                    mItemFlightRouteArrival.setViewLineBorder(View.VISIBLE);
                    break;
                case R.id.item_flight_flightno:
                    mItemFlightRouteRunwayArrival.setViewLineBorder(View.GONE);
                    if (!checkEnableField(mItemFlightRouteRunwayArrival)) {
                        mItemFlightRouteArrival.setViewLineBorder(View.GONE);
                    }
                    break;
            }
        }


    }

    private void setupItemUserFields(final ItemsFlightView item, int settingCode, final int settingCaption) {
        if (mDatabaseManagerV5.getSetting(settingCode).getData().equals("1")) {
            listItemFlightViews.add(item);
            /*mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {*/
            item.setVisibility(View.VISIBLE);
            item.setTextTitle(mDatabaseManagerV5.getSetting(settingCaption).getData());
               /* }
            });*/

        }
    }

    private void alterItemForDoneOrSimulator(int aircraftType) {
        //finishOutInHours(mItemFlightOnBlock.getDescription(),mItemFlightOnBlock,timeZoneDeparture);
        //finishSchedule(mItemFlightScheduledIn.getDescription(), mItemFlightScheduledIn, timeZoneDeparture);
        setAirfield(null, false, true);
        setupItemForDroneOrSimulator(mItemFlightRouteFlightNo, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightRoutePairing, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightRouteRunwayDeparture, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightRouteRunwayArrival, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightTakeOff, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightLanding, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightCrew3rd, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightCrew4th, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightRouteArrival, aircraftType);
        setupItemForDroneOrSimulator(mItemFlightRouteDeparture, aircraftType);
    }

    private void createListItems() {
        listItemFlightViews = new ArrayList<>();
        listItemFlightViews.add(mItemFlightRouteDate);
        mItemFlightRouteDate.setVisibility(View.VISIBLE);
        listItemFlightViews.add(mItemFlightRouteAircraft);
        mItemFlightRouteAircraft.setVisibility(View.VISIBLE);
        listItemFlightViews.add(mItemFlightRouteDeparture);
        mItemFlightRouteDeparture.setTextTitle("DEPARTURE");
        mItemFlightRouteDeparture.setVisibility(View.VISIBLE);
        setupItem(mItemFlightRouteRunwayDeparture, 496);
        listItemFlightViews.add(mItemFlightRouteArrival);
        mItemFlightRouteArrival.setVisibility(View.VISIBLE);
        setupItem(mItemFlightRouteRunwayArrival, 497);
        setupItem(mItemFlightRouteFlightNo, 417);
        setupItem(mItemFlightHobbsOut, 423);
        setupItem(mItemFlightHobbsIn, 423);
        setupItem(mItemFlightOffBlock, 419);
        setupItem(mItemFlightScheduledOut, 498);
        setupItem(mItemFlightTakeOff, 422);
        setupItem(mItemFlightLanding, 422);
        setupItem(mItemFlightOnBlock, 419);
        setupItem(mItemFlightScheduledIn, 498);
        setupItem(mItemFlightTotal, 421);
        setupItem(mItemFlightPic, 401);
        setupItem(mItemFlightPicUS, 402);
        setupItem(mItemFlightCopilot, 403);
        setupItem(mItemFlightDual, 404);
        setupItem(mItemFlightInstructor, 405);
        setupItem(mItemFlightExaminer, 406);
        setupItem(mItemFlightRelief, 407);
        setupItem(mItemFlightNight, 416);
        setupItem(mItemFlightIFR, 410);
        setupItem(mItemFlightActualInstrument, 408);
        setupItem(mItemFlightSimulatedInstrument, 409);
        setupItem(mItemFlightXC, 411);
        setupItemUserFields(mItemFlightUserActive1, 412, 480);
        setupItemUserFields(mItemFlightUserActive2, 413, 481);
        setupItemUserFields(mItemFlightUserActive3, 414, 482);
        setupItemUserFields(mItemFlightUserActive4, 415, 483);
        setupItem(mItemFlightCurrenciesTask, 424);
        setupItem(mItemFlightCurrenciesTOday, 425);
        setupItem(mItemFlightCurrenciesTOnight, 426);
        setupItem(mItemFlightCurrenciesLDGDay, 427);
        setupItem(mItemFlightCurrenciesLDGNight, 428);
        setupItem(mItemFlightCurrenciesLifts, 431);
        setupItem(mItemFlightCurrenciesHolding, 430);
        setupItem(mItemFlightCurrenciesFuel, 432);
        setupItem(mItemFlightCurrenciesFuelPlanned, 434);
        setupItem(mItemFlightCurrenciesFuelUsed, 433);
        setupItem(mItemFlightCurrenciesPax, 435);
        setupItemUserFields(mItemFlightCurrenciesUserN1, 436, 484);
        setupItemUserFields(mItemFlightCurrenciesUserN2, 437, 485);
        setupItemUserFields(mItemFlightCurrenciesUserN3, 438, 486);
        setupItem(mItemFlightCurrenciesDeicing, 439);
        setupItem(mItemFlightCurrenciesDelay, 440);
        setupItem(mItemFlightCurrenciesApproach, 442);
        setupItem(mItemFlightCurrenciesGlider, 448);
        setupItem(mItemFlightCurrenciesOperation, 449);
        setupItem(mItemFlightCurrenciesSignBox, 452);
        setupItem(mItemFlightCurrenciesSignPen, 453);
        setupItem(mItemFlightCrewPic, 454);
        setupItem(mItemFlightCrew2nd, 455);
        setupItem(mItemFlightCrew3rd, 456);
        setupItem(mItemFlightCrew4th, 457);
        setupItem(mItemFlightCrewList, 458);
        setupItem(mItemFlightLogRemarks, 459);
        setupItem(mItemFlightLogInstruction, 460);
        setupItem(mItemFlightLogFlightReport, 461);
    }

    private void initView() {
        for (int i = 0; i < listItemFlightViews.size(); i++) {
            listItemFlightViews.get(i).setIItemFlight(this);
        }
        mItemFlightTotal.setHint(isLogTimeDecimal ? R.string.decimal_hint : R.string.hours_hint);
        initValue();
    }

    private void initSetting() {
        String logDecimal = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
        isLogTimeDecimal = flightUtils.isLogTimeDecimal = logDecimal.equals("1");
        int accuracy = Integer.parseInt(mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_ACCURACY).getData());
        flightUtils.setAccuracy(accuracy);
        //isNotPreserveAccuracy = logDecimal.equals("2");
        isSettingIata = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_IATA).getData().equals("1");
        String tMode = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_TIME_MODE).getData();

        switch (tMode) {
            case "1":
                timeMode = FlightUtils.TimeMode.UTC;
                break;
            case "0":
                timeMode = FlightUtils.TimeMode.LOCAL;
                break;
            case "-1":
                timeMode = FlightUtils.TimeMode.BASE;
                break;
        }
        String dMode = mDatabaseManagerV5.getSetting(309).getData();
        switch (dMode) {
            case "1":
                dateMode = FlightUtils.TimeMode.UTC;
                break;
            case "0":
                dateMode = FlightUtils.TimeMode.LOCAL;
                break;
            case "-1":
                dateMode = FlightUtils.TimeMode.BASE;
                break;
        }
        settingNightMode = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_MODE).getData();
    }

    private void initValue() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        if (checkEnableField(mItemFlightCurrenciesTask))
            mItemFlightCurrenciesTask.setDescription("PF");
        mItemFlightRouteAircraft.setParentFragment(this);
        mItemFlightRouteDeparture.setParentFragment(this);
        mItemFlightRouteArrival.setParentFragment(this);
        mItemFlightCrewPic.setParentFragment(this);
        mItemFlightCrew2nd.setParentFragment(this);
        mItemFlightCrew3rd.setParentFragment(this);
        mItemFlightCrew4th.setParentFragment(this);
        mMode = bundle.getInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE);
        byte[] flightCode = bundle.getByteArray(MCCPilotLogConst.FLIGHT_CODE);
        switch (mMode) {
            case MODE_ADD:
                mTvTitle.setText(R.string.text_flight_add);
                break;
            case MODE_NEXT:
            case MODE_RETURN:
              /*  if (mDbManager.getNumberOfFlights() >= MCCPilotLogConst.FLIGHT_WARNING_QUANTITY) {
                    MccDialog.getOkAlertDialog(mActivity, R.string.warning_number_flight_record).show();
                }*/
                mTvTitle.setText(R.string.text_flight_add);
                if (flightCode != null) {
                    Flight currentFlight = mDatabaseManagerV5.getFlightByCode(flightCode);
                    mFlight = new Flight();
                    if (currentFlight.getArrTimeUTC() + 30 >= 1440 && timeMode == FlightUtils.TimeMode.UTC) {
                        try {
                            Date currentDate = DB_DATE_FORMAT.parse(currentFlight.getDateUTC());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            mFlight.setDateUTC(flightUtils.DB_DATE_FORMAT.format(calendar.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mFlight.setDateUTC(currentFlight.getDateUTC());
                    }
                    if (timeMode == FlightUtils.TimeMode.BASE && (currentFlight.getArrTimeUTC() + currentFlight.getBaseOffset() + 30 >= 1440 ||
                            (currentFlight.getArrTimeUTC() + currentFlight.getBaseOffset() < 0 && currentFlight.getArrTimeUTC() + currentFlight.getBaseOffset() + 30 >= 0))) {
                        try {
                            Date currentDate = DB_DATE_FORMAT.parse(currentFlight.getDateBASE());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            mFlight.setDateBASE(flightUtils.DB_DATE_FORMAT.format(calendar.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mFlight.setDateBASE(currentFlight.getDateBASE());
                    }

                    if (timeMode == FlightUtils.TimeMode.LOCAL && (currentFlight.getArrTimeUTC() + currentFlight.getArrOffset() + 30 >= 1440 ||
                            (currentFlight.getArrTimeUTC() + currentFlight.getBaseOffset() < 0 && currentFlight.getArrTimeUTC() + currentFlight.getArrOffset() + 30 >= 0))) {
                        try {
                            Date currentDate = DB_DATE_FORMAT.parse(currentFlight.getDateLOCAL());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            mFlight.setDateLOCAL(flightUtils.DB_DATE_FORMAT.format(calendar.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mFlight.setDateLOCAL(currentFlight.getDateLOCAL());
                    }
                    mFlight.setAircraftCode(currentFlight.getAircraftCode());
                    mFlight.setDepCode(currentFlight.getArrCode());
                    if (mMode != MODE_NEXT) {
                        mFlight.setArrCode(currentFlight.getDepCode());
                    }
                    mFlight.setDepRwy(currentFlight.getArrRwy());
                    mFlight.setArrRwy(currentFlight.getDepRwy());
                    mFlight.setP1Code(currentFlight.getP1Code());
                    mFlight.setP2Code(currentFlight.getP2Code());
                    mFlight.setP3Code(currentFlight.getP3Code());
                    mFlight.setP4Code(currentFlight.getP4Code());
                    mFlight.setCrewList(currentFlight.getCrewList());
                    mFlight.setTagOps(currentFlight.getTagOps());
                    mFlight.setTraining(currentFlight.getTraining());
                    mFlight.setSignBox(currentFlight.getSignBox());
                    if (!TextUtils.isEmpty(currentFlight.getFlightNumber())) {
                        mFlight.setFlightNumber(ValidationUtils.incrementedAlpha(currentFlight.getFlightNumber().trim()));
                    }
                    if (currentFlight.getPF())
                        mFlight.setPF(false);
                    else
                        mFlight.setPF(true);
                }
                break;
            case MODE_EDIT_FLIGHT_VIEW:
            case MODE_EDIT:
                mTvTitle.setText(R.string.text_flight_edit);
                if (flightCode != null) {
                    mFlight = mDatabaseManagerV5.getFlightByCode(flightCode);
                    ImagePic imagePic = mDatabaseManagerV5.getSignatureByFlightCode(mFlight.getFlightCode());
                    if(imagePic!=null){
                        setImageForSignature(imagePic.getFileName());
                    }
                    mItemFlightCurrenciesSignPen.showImageSign();
                }
                break;
            case MODE_PASTE_DATA:
                mTvTitle.setText(R.string.text_flight_edit);
                if (flightCode != null) {
                    mFlight = mDatabaseManagerV5.getFlightByCode(flightCode);
                    if (mFlight != null) {
                        if (!TextUtils.isEmpty(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT1))) {
                            mFlight.setP1Code(Utils.getByteArrayFromGUID(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT1)));
                        }
                        if (!TextUtils.isEmpty(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT2))) {
                            mFlight.setP2Code(Utils.getByteArrayFromGUID(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT2)));
                        }
                        if (!TextUtils.isEmpty(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT3))) {
                            mFlight.setP3Code(Utils.getByteArrayFromGUID(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT3)));
                        }
                        if (!TextUtils.isEmpty(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT4))) {
                            mFlight.setP4Code(Utils.getByteArrayFromGUID(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_PILOT4)));
                        }
                        if (!TextUtils.isEmpty(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_AIRCRAFT))) {
                            mFlight.setAircraftCode(Utils.getByteArrayFromGUID(StorageUtils.getStringFromSharedPref(mActivity, StateKey.SAVED_AIRCRAFT)));
                        }
                        mFlight.setPF(StorageUtils.getBooleanFromSharedPref(mActivity, StateKey.SAVED_TASK, true));
                        clearState();
                    }
                }
                break;
            case MODE_FLIGHT_VIEW:
                mTvTitle.setText(R.string.text_flight_view);
                if (flightCode != null) {
                    mFlight = mDatabaseManagerV5.getFlightByCode(flightCode);
                    for (int i = 0; i < listItemFlightViews.size(); i++) {
                        listItemFlightViews.get(i).disableForFlightView();
                    }
                }
                break;
            default:
                break;
        }
        if (mFlight != null) { //mFlight = new Flight();
            try {
                switch (dateMode) {
                    case UTC:
                        if (mFlight.getDateUTC() != null) {
                            mFlightDate.setTime(DB_DATE_FORMAT.parse(mFlight.getDateUTC()));
                        }
                        break;
                    case LOCAL:
                        if (mFlight.getDateLOCAL() != null) {
                            mFlightDate.setTime(DB_DATE_FORMAT.parse(mFlight.getDateLOCAL()));
                        }
                        break;
                    case BASE:
                        if (mFlight.getDateBASE() != null) {
                            mFlightDate.setTime(DB_DATE_FORMAT.parse(mFlight.getDateBASE()));
                        }
                        break;
                }
            } catch (ParseException pex) {

            }
            mItemFlightRouteDate.setDescription(DISPLAY_DATE_FORMAT.format(mFlightDate.getTime()));
            Calendar calToday = Calendar.getInstance();
            if (calToday.get(Calendar.YEAR) == mFlightDate.get(Calendar.YEAR) &&
                    calToday.get(Calendar.MONTH) == mFlightDate.get(Calendar.MONTH) &&
                    calToday.get(Calendar.DAY_OF_MONTH) == mFlightDate.get(Calendar.DAY_OF_MONTH)) {
                mItemFlightRouteDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mFlightDate.getTime()), R.color.grey_footer_text);
            } else {
                mItemFlightRouteDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mFlightDate.getTime()), RED_COLOR);
            }
            setDataEdit();
        } else {
            mFlight = new Flight();
            mFlight.setFlightCode(Utils.getByteArrayFromGUID(Utils.generateStringGUID()));
            mItemFlightRouteDate.setDescription(DISPLAY_DATE_FORMAT.format(mFlightDate.getTime()));
            mItemFlightRouteDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mFlightDate.getTime()));
        }

    }

    private void setDataEdit() {
        mAircraft = mDatabaseManagerV5.getAircraftByCode(mFlight.getAircraftCode());
        flightUtils.loadAircraftCondition(mAircraft);
        mItemFlightRouteAircraft.setDescription(mAircraft.getReference());
        if (!TextUtils.isEmpty(mAircraft.getSubModel())) {
            mItemFlightRouteAircraft.setFootNote(mAircraft.getModel() + "-" + mAircraft.getSubModel());
        } else {
            mItemFlightRouteAircraft.setFootNote(mAircraft.getModel());
        }
        if (mAircraft.getDeviceCode() > 1) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alterItemForDoneOrSimulator(mAircraft.getDeviceCode());
                }
            });
        }
        mAirfieldDeparture = mDatabaseManagerV5.getAirfieldByCode(mFlight.getDepCode());
        timeZoneDeparture = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(mAirfieldDeparture.getTZCode()).getTimeZone());
        timeZoneCodeDeparture = mAirfieldDeparture.getTZCode();
        flightUtils.setTextAirfield(mAirfieldDeparture, mItemFlightRouteDeparture, isSettingIata);
        if (mMode != MODE_NEXT) {
            mAirfieldArrival = mDatabaseManagerV5.getAirfieldByCode(mFlight.getArrCode());
            timeZoneArrival = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(mAirfieldArrival.getTZCode()).getTimeZone());
            timeZoneCodeArrival = mAirfieldArrival.getTZCode();
            if (checkEnableField(mItemFlightRouteArrival)) {
                flightUtils.setTextAirfield(mAirfieldArrival, mItemFlightRouteArrival, isSettingIata);
            }
        }

        String pilotCodeEmpty = MCCPilotLogConst.PILOT_CODE_EMPTY;
        if (checkEnableField(mItemFlightCrewPic)) {
            mPilot1 = mDatabaseManagerV5.getPilotByCode(mFlight.getP1Code());
            if (Utils.getGUIDFromByteArray(mPilot1.getPilotCode()).equals(pilotCodeEmpty))
                mPilot1 = null;
            if (mPilot1 != null) {
                mItemFlightCrewPic.setDescription(mPilot1.getPilotName());
                mItemFlightCrewPic.setFootNote(!TextUtils.isEmpty(mPilot1.getPilotRef()) ? mPilot1.getPilotRef() : "*");
            }
        }
        if (checkEnableField(mItemFlightCrew2nd)) {
            mPilot2 = mDatabaseManagerV5.getPilotByCode(mFlight.getP2Code());
            if (Utils.getGUIDFromByteArray(mPilot2.getPilotCode()).equals(pilotCodeEmpty))
                mPilot2 = null;
            if (mPilot2 != null) {
                mItemFlightCrew2nd.setDescription(mPilot2.getPilotName());
                mItemFlightCrew2nd.setFootNote(!TextUtils.isEmpty(mPilot2.getPilotRef()) ? mPilot2.getPilotRef() : "*");
            }
        }
        if (checkEnableField(mItemFlightCrew3rd)) {
            mPilot3 = mDatabaseManagerV5.getPilotByCode(mFlight.getP3Code());
            if (Utils.getGUIDFromByteArray(mPilot3.getPilotCode()).equals(pilotCodeEmpty))
                mPilot3 = null;
            if (mPilot3 != null) {
                mItemFlightCrew3rd.setDescription(mPilot3.getPilotName());
                mItemFlightCrew3rd.setFootNote(!TextUtils.isEmpty(mPilot3.getPilotRef()) ? mPilot3.getPilotRef() : "*");
            }
        }
        if (checkEnableField(mItemFlightCrew4th)) {
            mPilot4 = mDatabaseManagerV5.getPilotByCode(mFlight.getP4Code());
            if (Utils.getGUIDFromByteArray(mPilot4.getPilotCode()).equals(pilotCodeEmpty))
                mPilot4 = null;
            if (mPilot4 != null) {
                mItemFlightCrew4th.setDescription(mPilot4.getPilotName());
                mItemFlightCrew4th.setFootNote(!TextUtils.isEmpty(mPilot4.getPilotRef()) ? mPilot4.getPilotRef() : "*");
            }
        }
        if (checkEnableField(mItemFlightRouteRunwayDeparture)) {
            mItemFlightRouteRunwayDeparture.setDescription(mFlight.getDepRwy());
            mItemFlightRouteRunwayArrival.setDescription(mFlight.getArrRwy());
        }
        if (checkEnableField(mItemFlightRouteFlightNo)) {
            mItemFlightRouteFlightNo.setDescription(mFlight.getFlightNumber());
        }
        if (checkEnableField(mItemFlightRoutePairing)) {
            mItemFlightRoutePairing.setDescription(mFlight.getPairing());
        }
        if (checkEnableField(mItemFlightHobbsOut)) {
            DecimalFormat hobbsFormat = new DecimalFormat("#.#");
            if (mFlight.getHobbsOut() != null && mFlight.getHobbsOut() > 0) {
                mItemFlightHobbsOut.setDescription(hobbsFormat.format(mFlight.getHobbsOut() * 0.1));
            }
            if (mFlight.getHobbsIn() != null && mFlight.getHobbsOut() > 0)
                mItemFlightHobbsIn.setDescription(hobbsFormat.format(mFlight.getHobbsIn() * 0.1));
        }


        if (checkEnableField(mItemFlightOffBlock)) {
            mItemFlightOffBlock.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getDepTimeUTC(mFlight.getDepTimeUTC())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightOffBlock, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
        }
        if (checkEnableField(mItemFlightOnBlock)) {
            mItemFlightOnBlock.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getArrTimeUTC(mFlight.getArrTimeUTC())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightOnBlock, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
        }

        if (checkEnableField(mItemFlightTakeOff)) {
            mItemFlightTakeOff.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getDepTimeUTC(mFlight.getToTimeUTC())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightTakeOff, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
        }
        if (checkEnableField(mItemFlightLanding)) {
            mItemFlightLanding.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getArrTimeUTC(mFlight.getLdgTimeUTC())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightLanding, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
        }
        String scheduleConfig = mDatabaseManagerV5.getSetting(498).getData();
        if (scheduleConfig.equals("1")) {
            mItemFlightScheduledOut.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getDepTimeUTC(mFlight.getDepTimeSCHED())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightScheduledOut, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
        }
        if (scheduleConfig.equals("1")) {
            mItemFlightScheduledIn.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getArrTimeUTC(mFlight.getArrTimeSCHED())), false));
            flightUtils.calcAndDisplayTimeMode(mItemFlightScheduledIn, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
        }


        if (checkEnableField(mItemFlightTotal)) {
            mItemFlightTotal.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinTOTAL()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            /*accuracy*/
            mItemFlightTotal.setMinutesData(mFlight.getMinTOTAL());
            if (mFlight.getMinAIR() > 0) {
                mItemFlightTotal.setTotalArr(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinAIR()), isLogTimeDecimal), isLogTimeDecimal));
                mItemFlightTotal.setMinutesDataOnlyForMinArr(mFlight.getMinAIR());
            }

            if (scheduleConfig.equals("1")) {
                String scheduleOutHours = flightUtils.convertTimeToUTC(mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                String scheduleInHours = flightUtils.convertTimeToUTC(mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                int totalSched = TimeUtils.calcTotalMinute(scheduleOutHours, scheduleInHours, flightUtils.getAccuracy());
                if (totalSched < 0) totalSched = 0;
                mItemFlightTotal.setTotalSchedule(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(totalSched), isLogTimeDecimal), isLogTimeDecimal));
            }
        }

        if (checkEnableField(mItemFlightPic)) {
            mItemFlightPic.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinPIC()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightPic.setMinutesData(mFlight.getMinPIC());
        }
        if (checkEnableField(mItemFlightPicUS)) {
            mItemFlightPicUS.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinPICUS()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightPicUS.setMinutesData(mFlight.getMinPICUS());
        }
        if (checkEnableField(mItemFlightCopilot)) {
            mItemFlightCopilot.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinCOP()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightCopilot.setMinutesData(mFlight.getMinCOP());
        }
        if (checkEnableField(mItemFlightDual)) {
            mItemFlightDual.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinDUAL()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightDual.setMinutesData(mFlight.getMinDUAL());
        }
        if (checkEnableField(mItemFlightInstructor)) {
            mItemFlightInstructor.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinINSTR()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightInstructor.setMinutesData(mFlight.getMinINSTR());
        }
        if (checkEnableField(mItemFlightExaminer)) {
            mItemFlightExaminer.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinEXAM()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightExaminer.setMinutesData(mFlight.getMinEXAM());
        }
        if (checkEnableField(mItemFlightRelief)) {
            mItemFlightRelief.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinREL()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightRelief.setMinutesData(mFlight.getMinREL());
        }
        if (checkEnableField(mItemFlightNight)) {
            mItemFlightNight.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinNIGHT()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightNight.setMinutesData(mFlight.getMinNIGHT());
        }
        if (checkEnableField(mItemFlightIFR)) {
            mItemFlightIFR.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinIFR()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightIFR.setMinutesData(mFlight.getMinIFR());
        }
        if (checkEnableField(mItemFlightActualInstrument)) {
            mItemFlightActualInstrument.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinIMT()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightActualInstrument.setMinutesData(mFlight.getMinIMT());
        }
        if (checkEnableField(mItemFlightSimulatedInstrument)) {
            mItemFlightSimulatedInstrument.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinSFR()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightSimulatedInstrument.setMinutesData(mFlight.getMinSFR());
        }
        if (checkEnableField(mItemFlightXC)) {
            mItemFlightXC.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinXC()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightXC.setMinutesData(mFlight.getMinXC());
        }
        if (checkEnableField(mItemFlightUserActive1)) {
            mItemFlightUserActive1.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinU1()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightUserActive1.setMinutesData(mFlight.getMinU1());
        }
        if (checkEnableField(mItemFlightUserActive2)) {
            mItemFlightUserActive2.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinU2()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightUserActive2.setMinutesData(mFlight.getMinU2());
        }
        if (checkEnableField(mItemFlightUserActive3)) {
            mItemFlightUserActive3.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinU3()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightUserActive3.setMinutesData(mFlight.getMinU3());
        }
        if (checkEnableField(mItemFlightUserActive4)) {
            mItemFlightUserActive4.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(mFlight.getMinU4()), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
            mItemFlightUserActive3.setMinutesData(mFlight.getMinU4());
        }

        if (checkEnableField(mItemFlightCurrenciesTask))
            mItemFlightCurrenciesTask.setDescription(mFlight.getPF() ? "PF" : "PM");
        if (checkEnableField(mItemFlightCurrenciesTOday))
            mItemFlightCurrenciesTOday.setDescription(mFlight.getToDay());
        if (checkEnableField(mItemFlightCurrenciesTOnight))
            mItemFlightCurrenciesTOnight.setDescription(mFlight.getToNight());
        if (checkEnableField(mItemFlightCurrenciesLDGDay))
            mItemFlightCurrenciesLDGDay.setDescription(mFlight.getLdgDay());
        if (checkEnableField(mItemFlightCurrenciesLDGNight))
            mItemFlightCurrenciesLDGNight.setDescription(mFlight.getLdgNight());


        if (checkEnableField(mItemFlightCurrenciesLifts))
            mItemFlightCurrenciesLifts.setDescription(mFlight.getLiftSW());
        if (checkEnableField(mItemFlightCurrenciesHolding))
            mItemFlightCurrenciesHolding.setDescription(mFlight.getHolding());
        if (checkEnableField(mItemFlightCurrenciesFuel))
            mItemFlightCurrenciesFuel.setDescription(mFlight.getFuel());
        if (checkEnableField(mItemFlightCurrenciesFuelUsed))
            mItemFlightCurrenciesFuelUsed.setDescription(mFlight.getFuelUsed());
        if (checkEnableField(mItemFlightCurrenciesFuelPlanned))
            mItemFlightCurrenciesFuelPlanned.setDescription(mFlight.getFuelPlanned());
        if (checkEnableField(mItemFlightCurrenciesPax))
            mItemFlightCurrenciesPax.setDescription(mFlight.getPax());
        if (checkEnableField(mItemFlightCurrenciesDeicing))
            mItemFlightCurrenciesDeicing.setDescription(mFlight.getDeIce() ? 1 : 0);

        if (checkEnableField(mItemFlightCurrenciesDelay)) {
            mZDelayCodes = mFlight.getTagDelay();
            if (!TextUtils.isEmpty(mZDelayCodes)) {
                String[] zDelayCodeArray;
                StringBuilder flightZDelayString = new StringBuilder();
                zDelayCodeArray = mZDelayCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zDelayCodeArray.length; i++) {
                    ZDelay zDelay = mDatabaseManagerV5.getZDelay(zDelayCodeArray[i]);
                    if (zDelay != null) {
                        flightZDelayString.append(zDelay.getDelayCode() + " (" + zDelay.getDelayDD() + ")");
                        flightZDelayString.append(", ");
                    }
                }
                mItemFlightCurrenciesDelay.setDescription(flightZDelayString.toString().substring(0, flightZDelayString.length() - 2));
            }
        }
        if (checkEnableField(mItemFlightCurrenciesApproach)) {
            mZApproachCodes = mFlight.getTagApproach();
            if (!TextUtils.isEmpty(mZApproachCodes)) {
                String[] zLaunchCodeArray;
                StringBuilder flightZApproachString = new StringBuilder();
                zLaunchCodeArray = mZApproachCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zLaunchCodeArray.length; i++) {
                    ZApproach zApproach = mDatabaseManagerV5.getZApproach(zLaunchCodeArray[i]);
                    if (zApproach != null) {
                        flightZApproachString.append(zApproach.getAPShort());
                        flightZApproachString.append(", ");
                    }
                }
                mItemFlightCurrenciesApproach.setDescription(flightZApproachString.toString().substring(0, flightZApproachString.length() - 2), R.color.black);
            }
        }
        if (checkEnableField(mItemFlightCurrenciesOperation)) {
            mZOperationCodes = mFlight.getTagOps();
            if (!TextUtils.isEmpty(mZOperationCodes)) {
                String[] zOperationCodeArray;
                StringBuilder flightZOperationString = new StringBuilder();
                zOperationCodeArray = mZOperationCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zOperationCodeArray.length; i++) {
                    ZOperation zOperation = mDatabaseManagerV5.getZOperation(zOperationCodeArray[i]);

                    if (zOperation != null) {
                        flightZOperationString.append(zOperation.getOpsShort());
                        flightZOperationString.append(", ");
                    }
                }
                mItemFlightCurrenciesOperation.setDescription(flightZOperationString.toString().substring(0, flightZOperationString.length() - 2));
            }
        }
        if (checkEnableField(mItemFlightCurrenciesGlider)) {
            mZLaunchCodes = mFlight.getTagLaunch();
            if (!TextUtils.isEmpty(mZLaunchCodes)) {
                String[] zLaunchCodeArray;
                StringBuilder flightZLaunchString = new StringBuilder();
                zLaunchCodeArray = mZLaunchCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zLaunchCodeArray.length; i++) {
                    ZLaunch zLaunch = mDatabaseManagerV5.getGliderLaunch(zLaunchCodeArray[i]);
                    if (zLaunch != null) {
                        flightZLaunchString.append(zLaunch.getLaunchShort());
                        flightZLaunchString.append(", ");
                    }
                }
                mItemFlightCurrenciesGlider.setDescription(flightZLaunchString.toString().substring(0, flightZLaunchString.length() - 2));
            }
        }

        if (checkEnableField(mItemFlightCurrenciesSignBox))
            mItemFlightCurrenciesSignBox.setDescription(mFlight.getSignBox());
       /* if (checkEnableField(mItemFlightCurrenciesSignPen))
            mItemFlightCurrenciesSignPen.setDescription(mFlight);*/

        if (checkEnableField(mItemFlightCrewList))
            mItemFlightCrewList.setDescription(mFlight.getCrewList());
        if (checkEnableField(mItemFlightLogRemarks))
            mItemFlightLogRemarks.setDescription(mFlight.getRemarks());
        if (checkEnableField(mItemFlightLogInstruction))
            mItemFlightLogInstruction.setDescription(mFlight.getTraining());
        if (checkEnableField(mItemFlightLogFlightReport))
            mItemFlightLogFlightReport.setDescription(mFlight.getReport());

    }

    public void setAirfield(final Airfield airfield, final boolean departure, final boolean isDroneOrSimulator) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (!isChange) isChange = true;
                if (departure) {
                    mAirfieldDeparture = airfield;
                    if (mAirfieldDeparture != null) {
                        timeZoneDeparture = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(mAirfieldDeparture.getTZCode()).getTimeZone());
                        timeZoneCodeDeparture = mAirfieldDeparture.getTZCode();
                        flightUtils.calcAndDisplayTimeMode(mItemFlightOffBlock, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                        flightUtils.calcAndDisplayTimeMode(mItemFlightTakeOff, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                        flightUtils.calcAndDisplayTimeMode(mItemFlightScheduledOut, timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                    }
                    if (mAircraft != null && mAircraft.getDeviceCode() > 1) {
                        mAirfieldArrival = airfield;
                        if (mAirfieldArrival != null) {
                            timeZoneArrival = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(mAirfieldArrival.getTZCode()).getTimeZone());
                            timeZoneCodeArrival = mAirfieldArrival.getTZCode();
                            flightUtils.calcAndDisplayTimeMode(mItemFlightOnBlock, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                            flightUtils.calcAndDisplayTimeMode(mItemFlightLanding, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                            flightUtils.calcAndDisplayTimeMode(mItemFlightScheduledIn, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                        }
                    }
                } else {
                    mAirfieldArrival = airfield;
                    if (isDroneOrSimulator) {
                        mAirfieldArrival = mAirfieldDeparture;
                        timeZoneArrival = timeZoneDeparture;
                        if (mAirfieldArrival != null)
                            timeZoneCodeArrival = mAirfieldArrival.getTZCode();
                    }
                    if (mAirfieldArrival != null) {
                        timeZoneArrival = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(mAirfieldArrival.getTZCode()).getTimeZone());
                        timeZoneCodeArrival = mAirfieldArrival.getTZCode();
                        flightUtils.calcAndDisplayTimeMode(mItemFlightOnBlock, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                        flightUtils.calcAndDisplayTimeMode(mItemFlightLanding, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                        flightUtils.calcAndDisplayTimeMode(mItemFlightScheduledIn, timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                    }
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isDroneOrSimulator) {
                            mItemFlightTotal.clearValue();
                        } else {
                            if (checkEnableField(mItemFlightTakeOff)) {
                                String takeOffHours = flightUtils.convertTimeToUTC(mItemFlightTakeOff.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                                String landingHours = flightUtils.convertTimeToUTC(mItemFlightLanding.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                                int totalAir = TimeUtils.calcTotalMinute(takeOffHours, landingHours, flightUtils.getAccuracy());
                                if (totalAir < 0) totalAir = 0;
                                mItemFlightTotal.setTotalArr(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(totalAir), isLogTimeDecimal), isLogTimeDecimal));
                            }
                        }
                        if (checkEnableField(mItemFlightScheduledOut)) {
                            String scheduleOutHours = flightUtils.convertTimeToUTC(mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                            String scheduleInHours = flightUtils.convertTimeToUTC(mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                            int totalSched = TimeUtils.calcTotalMinute(scheduleOutHours, scheduleInHours, flightUtils.getAccuracy());
                            if (totalSched < 0) totalSched = 0;
                            mItemFlightTotal.setTotalSchedule(flightUtils.getTotalScheduleAndArr(TimeUtils.convertMinuteToHour(String.valueOf(totalSched), isLogTimeDecimal), isLogTimeDecimal));
                        }
                        //if (checkEnableField(mItemFlightOffBlock) && (mItemFlightHobbsOut.getVisibility() == View.GONE
                        //        || TextUtils.isEmpty(mItemFlightHobbsOut.getDescription())
                        //       || TextUtils.isEmpty(mItemFlightHobbsIn.getDescription())) )
                        //if(!isNullOrEmptyItemsFlightView(mItemFlightOffBlock) && !isNullOrEmptyItemsFlightView(mItemFlightOnBlock))
                        flightUtils.calcTotalTime(mItemFlightOffBlock, mItemFlightOnBlock, mItemFlightTotal, isLogTimeDecimal, timeZoneDeparture, timeZoneArrival, timeZoneCodeDeparture, timeZoneCodeArrival, mFlightDate);
                    }
                });

                if (isAutoLoadXC)
                    flightUtils.autoLoadXC(mAirfieldDeparture, mAirfieldArrival, mAircraft, mItemFlightXC, mItemFlightTotal);
            }
        });
    }

    public void setAircraft(final Aircraft aircraft) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (!isChange) isChange = true;
                flightUtils.loadAircraftCondition(aircraft);
                if (checkEnableField(mItemFlightPic) && !isNullOrEmptyItemsFlightView(mItemFlightPic) ||
                        checkEnableField(mItemFlightPicUS) && !isNullOrEmptyItemsFlightView(mItemFlightPicUS))
                    flightUtils.autoLoadTask(mItemFlightCurrenciesTask, true, aircraft);
                else
                    flightUtils.autoLoadTask(mItemFlightCurrenciesTask, false, aircraft);
                mZLaunchCodes = flightUtils.autoLoadDefaultLaunch(aircraft, mItemFlightCurrenciesGlider);
                mZOperationCodes = flightUtils.autoLoadDefaultOperation(aircraft, mItemFlightCurrenciesOperation);
                mZApproachCodes = flightUtils.autoLoadDefaultApproach(aircraft, mItemFlightCurrenciesApproach);
                if (checkEnableField(mItemFlightCrewPic) && isNullOrEmptyItemsFlightView(mItemFlightCrewPic)
                        && checkEnableField(mItemFlightCrew2nd) && isNullOrEmptyItemsFlightView(mItemFlightCrew2nd)) {
                    mPilot2 = flightUtils.autoLoadPilot(aircraft, mItemFlightCrew2nd);
                }
                final Aircraft aircraftTemp = mAircraft;
                mAircraft = aircraft;
                mItemFlightTotal.setFinishInput();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (aircraftTemp != null && (aircraftTemp.getDeviceCode() == 3 || aircraftTemp.getDeviceCode() == 2) && aircraft != null && aircraft.getDeviceCode() == 1) {
                                    createListItems();
                                } else if (aircraft != null && (aircraft.getDeviceCode() == 2 || aircraft.getDeviceCode() == 3)) {
                                    alterItemForDoneOrSimulator(aircraft.getDeviceCode());
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    public void setPilot(Pilot pilot, int pilotIndicator) {
        if (!isChange) isChange = true;
        switch (pilotIndicator) {
            case CREW_PIC:
                mPilot1 = pilot;
                break;
            case CREW_2ND:
                mPilot2 = pilot;
                break;
            case CREW_3RD:
                mPilot3 = pilot;
                break;
            case CREW_4TH:
                mPilot4 = pilot;
                break;
        }
    }


    private void setInOutScheduled(final boolean out) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (out) {
                    if (mItemFlightScheduledOut.getVisibility() != View.GONE) {
                        if (mActivity.getCurrentFocus() != null && mActivity.getCurrentFocus() == mItemFlightScheduledOut.getEdtDescription())
                            KeyboardUtils.hideKeyboard(mActivity);
                        mItemFlightScheduledOut.setVisibility(View.GONE);
                        mItemFlightOffBlock.setViewLineBorder(View.VISIBLE);
                        mItemFlightOffBlock.setIconArrowInfo(R.drawable.ic_info_arrow);
                        mItemFlightOffBlock.setVisibleScheduleHour(View.VISIBLE);
                    } else {
                        mItemFlightScheduledOut.setVisibility(View.VISIBLE);
                        mItemFlightOffBlock.setViewLineBorder(View.GONE);
                        mItemFlightOffBlock.setIconArrowInfo(R.drawable.ic_info_arrow_up);
                        mItemFlightOffBlock.setVisibleScheduleHour(View.GONE);
                    }
                } else {
                    if (mItemFlightScheduledIn.getVisibility() != View.GONE) {
                        if (mActivity.getCurrentFocus() != null && mActivity.getCurrentFocus() == mItemFlightScheduledIn.getEdtDescription())
                            KeyboardUtils.hideKeyboard(mActivity);
                        mItemFlightScheduledIn.setVisibility(View.GONE);
                        mItemFlightOnBlock.setViewLineBorder(View.VISIBLE);
                        mItemFlightOnBlock.setIconArrowInfo(R.drawable.ic_info_arrow);
                        mItemFlightOnBlock.setVisibleScheduleHour(View.VISIBLE);
                    } else {
                        mItemFlightScheduledIn.setVisibility(View.VISIBLE);
                        mItemFlightOnBlock.setViewLineBorder(View.GONE);
                        mItemFlightOnBlock.setIconArrowInfo(R.drawable.ic_info_arrow_up);
                        mItemFlightOnBlock.setVisibleScheduleHour(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (listItemFlightViews != null) {
            for (int i = 0; i < listItemFlightViews.size(); i++) {
                listItemFlightViews.get(i).restoreState();
            }
            if (pageGoTo == PageGoTo.AIRFIELD && mAirfieldArrival != null) {
                /*mItemFlightRouteArrival.setDescription(isSettingIata ? (TextUtils.isEmpty(mAirfieldArrival.getAFIATA()) ? mAirfieldArrival.getAFICAO() : mAirfieldArrival.getAFIATA())
                        : (TextUtils.isEmpty(mAirfieldArrival.getAFICAO()) || mAirfieldDeparture.getAFICAO().equalsIgnoreCase("zzzz") ? mAirfieldArrival.getAFIATA() : mAirfieldArrival.getAFICAO()));
                mItemFlightRouteArrival.setFootNote(mAirfieldArrival.getAFName());*/
                flightUtils.setTextAirfield(mAirfieldArrival, mItemFlightRouteArrival, isSettingIata);
            }
            if (pageGoTo == PageGoTo.AIRFIELD && mAirfieldDeparture != null) {
              /*  mItemFlightRouteDeparture.setDescription(isSettingIata ? (TextUtils.isEmpty(mAirfieldDeparture.getAFIATA()) ? mAirfieldDeparture.getAFICAO() : mAirfieldDeparture.getAFIATA())
                        : (TextUtils.isEmpty(mAirfieldDeparture.getAFICAO()) || mAirfieldDeparture.getAFICAO().equalsIgnoreCase("zzzz") ? mAirfieldDeparture.getAFIATA() : mAirfieldDeparture.getAFICAO()));
                mItemFlightRouteDeparture.setFootNote(mAirfieldDeparture.getAFName());*/
                flightUtils.setTextAirfield(mAirfieldDeparture, mItemFlightRouteDeparture, isSettingIata);
            }
            if (pageGoTo == PageGoTo.AIRCRAFT && mAircraft != null) {
                mItemFlightRouteAircraft.setDescription(mAircraft.getReference());
                if (!TextUtils.isEmpty(mAircraft.getSubModel())) {
                    mItemFlightRouteAircraft.setFootNote(mAircraft.getModel() + "-" + mAircraft.getSubModel());
                } else {
                    mItemFlightRouteAircraft.setFootNote(mAircraft.getModel());
                }
            }
            if (pageGoTo == PageGoTo.PILOT1 && mPilot1 != null) {
                mItemFlightCrewPic.setDescription(mPilot1.getPilotName());
                mItemFlightCrewPic.setFootNote(!TextUtils.isEmpty(mPilot1.getPilotRef()) ? mPilot1.getPilotRef() : "*");
            }
            if (pageGoTo == PageGoTo.PILOT2 && mPilot2 != null) {
                mItemFlightCrew2nd.setDescription(mPilot2.getPilotName());
                mItemFlightCrew2nd.setFootNote(!TextUtils.isEmpty(mPilot2.getPilotRef()) ? mPilot2.getPilotRef() : "*");
            }
            if (pageGoTo == PageGoTo.PILOT3 && mPilot3 != null) {
                mItemFlightCrew3rd.setDescription(mPilot3.getPilotName());
                mItemFlightCrew3rd.setFootNote(!TextUtils.isEmpty(mPilot3.getPilotRef()) ? mPilot3.getPilotRef() : "*");
            }
            if (pageGoTo == PageGoTo.PILOT4 && mPilot4 != null) {
                mItemFlightCrew4th.setDescription(mPilot4.getPilotName());
                mItemFlightCrew4th.setFootNote(!TextUtils.isEmpty(mPilot4.getPilotRef()) ? mPilot4.getPilotRef() : "*");
            }

            if (pageGoTo == PageGoTo.APPROACH && !TextUtils.isEmpty(mZApproachCodes)) {
                String[] zLaunchCodeArray;
                StringBuilder flightZApproachString = new StringBuilder();
                zLaunchCodeArray = mZApproachCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zLaunchCodeArray.length; i++) {
                    ZApproach zApproach = mDatabaseManagerV5.getZApproach(zLaunchCodeArray[i]);
                    if (zApproach != null) {
                        flightZApproachString.append(zApproach.getAPShort());
                        flightZApproachString.append(", ");
                    }
                }
                mItemFlightCurrenciesApproach.setDescription(flightZApproachString.toString().substring(0, flightZApproachString.length() - 2), R.color.black);
            }
            if (pageGoTo == PageGoTo.OPERATION && !TextUtils.isEmpty(mZOperationCodes)) {
                String[] zOperationCodeArray;
                StringBuilder flightZOperationString = new StringBuilder();
                zOperationCodeArray = mZOperationCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zOperationCodeArray.length; i++) {
                    ZOperation zOperation = mDatabaseManagerV5.getZOperation(zOperationCodeArray[i]);

                    if (zOperation != null) {
                        flightZOperationString.append(zOperation.getOpsShort());
                        flightZOperationString.append(", ");
                    }
                }
                mItemFlightCurrenciesOperation.setDescription(flightZOperationString.toString().substring(0, flightZOperationString.length() - 2));
            }
            if (pageGoTo == PageGoTo.DELAY && !TextUtils.isEmpty(mZDelayCodes)) {
                String[] zDelayCodeArray;
                StringBuilder flightZDelayString = new StringBuilder();
                zDelayCodeArray = mZDelayCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zDelayCodeArray.length; i++) {
                    ZDelay zDelay = mDatabaseManagerV5.getZDelay(zDelayCodeArray[i]);
                    if (zDelay != null) {
                        flightZDelayString.append(zDelay.getDelayCode() + " (" + zDelay.getDelayDD() + ")");
                        flightZDelayString.append(", ");
                    }
                }
                mItemFlightCurrenciesDelay.setDescription(flightZDelayString.toString().substring(0, flightZDelayString.length() - 2));
            }
            if (pageGoTo == PageGoTo.LAUNCH && !TextUtils.isEmpty(mZLaunchCodes)) {
                String[] zLaunchCodeArray;
                StringBuilder flightZLaunchString = new StringBuilder();
                zLaunchCodeArray = mZLaunchCodes.split(MCCPilotLogConst.SPLIT_KEY);
                for (int i = 0; i < zLaunchCodeArray.length; i++) {
                    ZLaunch zLaunch = mDatabaseManagerV5.getGliderLaunch(zLaunchCodeArray[i]);
                    if (zLaunch != null) {
                        flightZLaunchString.append(zLaunch.getLaunchShort());
                        flightZLaunchString.append(", ");
                    }
                }
                mItemFlightCurrenciesGlider.setDescription(flightZLaunchString.toString().substring(0, flightZLaunchString.length() - 2));
            }
        }
        pageGoTo = PageGoTo.NONE;
    }

    private void checkedPicPicUsCoDual(ItemsFlightView itemsFlightView) {
        String currentValue = itemsFlightView.getDescription();
        mItemFlightPic.clearValue();
        mItemFlightPicUS.clearValue();
        mItemFlightCopilot.clearValue();
        mItemFlightDual.clearValue();
        if (!TextUtils.isEmpty(mItemFlightTotal.getDescription()) && TextUtils.isEmpty(currentValue)) {
            if (mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_DEDUCT_RELIEF).getData().equals("0")) {
                itemsFlightView.setMinutesData(mItemFlightTotal.getMinutesData());
                itemsFlightView.setDescription(mItemFlightTotal.getDescription());
            } else {
                reliefPercent = reliefPercent >= 0 ? reliefPercent : reliefPercent * -1;
                itemsFlightView.setMinutesData(mItemFlightTotal.getMinutesData() * (100 - reliefPercent) / 100);
                itemsFlightView.setDescription(TimeUtils.convertMinuteToHour(String.valueOf((int) (mItemFlightTotal.getMinutesData() * (100 - reliefPercent) / 100)), isLogTimeDecimal));
            }

            if (itemsFlightView.getId() == R.id.item_flight_pic || itemsFlightView.getId() == R.id.item_flight_picus)
                flightUtils.autoLoadTask(mItemFlightCurrenciesTask, true, mAircraft);
        }

    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {

        flightUtils = FlightUtils.getInstance(mActivity, mDatabaseManagerV5);
        initSetting();
        flightUtils.timeMode = this.timeMode;
        flightUtils.conditionLoad = null;
        //createListItems();

    /*    mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createListItems();
            }
        });*/
    }

    @Override
    public void updateUI() {
        createListItems();
        mSvFlightAdd.setVisibility(View.VISIBLE);
        initView();

    }

    @Override
    public void end() {
        isChange = false;
    }

    private void showTopKey() {
        if (!isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (!isTablet()) {
                mTopKey.setVisibility(View.VISIBLE);
                mBottomBar.setVisibility(View.GONE);

            } else {
                mTopKey.setVisibility(View.VISIBLE);
                mBottomBar.setVisibility(View.GONE);
              /*  Fragment fragment = getLeftFragment();
                if (fragment instanceof FlightListFragment) {
                    ((FlightListFragment) fragment).hideBottomBar();
                }*/
            }
            mSvFlightAdd.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    int scrollY = mSvFlightAdd.getScrollY();
                    if (scrollY > 0) {
                        View v = mActivity.getCurrentFocus();
                        if (v instanceof MccEditText) {
                            int edtHeight = v.getHeight();
                            mSvFlightAdd.setScrollY(scrollY + edtHeight);
                        }
                    }
                    mSvFlightAdd.getViewTreeObserver().removeOnScrollChangedListener(this);

                }
            });
            isShowTopKey = true;
        }
    }

    private void hideTopKey() {
        if (isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if (!isTablet()) {
                mTopKey.setVisibility(View.GONE);
                mBottomBar.setVisibility(View.VISIBLE);
            } else {
                mTopKey.setVisibility(View.GONE);
                mBottomBar.setVisibility(View.VISIBLE);
               /* Fragment fragment = getLeftFragment();
                if (fragment instanceof LogbookListFragment) {
                    ((LogbookListFragment) fragment).showBottomBar();
                }*/
            }
            isShowTopKey = false;
        }
    }

    int currentFocus;

    private void nextPrevFocus(MccEnum.topKeyboardCustomInput input) {
        View v = mActivity.getCurrentFocus();
        final MccEditText edt;
        if (v instanceof MccEditText) {
            edt = (MccEditText) v;
        } else {
            return;
        }
        switch (input) {
            case CANCEL:
                final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                edt.setText(keyboardMemoryTemp1);
                edt.clearFocus();
                hideTopKey();
                break;
            case DONE:
                final InputMethodManager imm1 = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                edt.clearFocus();
                hideTopKey();
                break;
            case NEXT:
                currentFocus++;
                if (listItemFlightViews.size() > currentFocus) {
                    MccEditText edtNext = listItemFlightViews.get(currentFocus).getEdtDescription();
                    if ((listItemFlightViews.get(currentFocus).getId() == R.id.item_flight_scheduled_out ||
                            listItemFlightViews.get(currentFocus).getId() == R.id.item_flight_scheduled_in) && isShowSchedule) {
                        if (listItemFlightViews.get(currentFocus).getVisibility() == View.GONE) {
                            currentFocus++;
                            edtNext = listItemFlightViews.get(currentFocus).getEdtDescription();
                        }
                    }
                    if (edtNext.getVisibility() == View.VISIBLE && edtNext.isEnabled()) {
                        edtNext.requestFocus();
                        mSvFlightAdd.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                            @Override
                            public void onScrollChanged() {
                                int scrollY = mSvFlightAdd.getScrollY();
                                if (scrollY > 0) {
                                    int edtHeight = edt.getHeight();
                                    mSvFlightAdd.setScrollY(scrollY + edtHeight);
                                }
                                mSvFlightAdd.getViewTreeObserver().removeOnScrollChangedListener(this);

                            }
                        });
                    } else {
                        nextPrevFocus(MccEnum.topKeyboardCustomInput.NEXT);
                    }
                } else {
                    edt.clearFocus();
                    hideTopKey();
                    KeyboardUtils.hideSoftKeyboard(mActivity);
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View view, ItemsFlightView itemsFlightView) {
        keyboardMemoryTemp1 = ((MccEditText) view).getText().toString();
        currentFocus = listItemFlightViews.indexOf(itemsFlightView);
        if (itemsFlightView.getId() == R.id.item_flight_flightno) {
            SettingConfig settingConfig = mDatabaseManagerV5.getSetting(7);
            if (settingConfig != null && !TextUtils.isEmpty(settingConfig.getData()) && !TextUtils.isEmpty(itemsFlightView.getDescription())) {
                if (settingConfig.getData().equals(itemsFlightView.getDescription())) {//flight number prefix
                    itemsFlightView.getEdtDescription().setSelection(itemsFlightView.getDescription().length());
                }
            }
        }
        showTopKey();
        View v2 = mActivity.getCurrentFocus();
        if (v2 == null || !(v2 instanceof MccEditText)) {
            hideTopKey();
        }
    }

    @Nullable
    @OnClick({R.id.tvFlightTitle, R.id.tv_action_bar_left, R.id.tv_action_bar_center, R.id.tv_action_bar_right, R.id.btn_cancel, R.id.btn_next, R.id.btn_done,
            R.id.btn_hour_shortcut, R.id.btn_route_shortcut, R.id.btn_currencies_shortcut, R.id.btn_crew_shortcut, R.id.btn_log_shortcut, R.id.rlBackIcon})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            case R.id.tvFlightTitle:
                break;
            case R.id.tv_action_bar_left:
                onKeyBackPress();
                break;
            case R.id.tv_action_bar_right:
                if (mMode == MODE_FLIGHT_VIEW) {
                    Bundle b = new Bundle();
                    b.putInt(MCCPilotLogConst.FLIGHT_SCREEN_MODE, FlightAddsFragment.MODE_EDIT_FLIGHT_VIEW);
                    byte[] flightCode = mBundle.getByteArray(MCCPilotLogConst.FLIGHT_CODE);
                    b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, flightCode);
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, FlightAddsFragment.class, b, FLAG_ADD_STACK);
                } else {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (validateAllFields(false)) {
                                boolean isSave = saveFlight(false);
                            }
                        }
                    });
                }
                break;
            case R.id.tv_action_bar_center:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (validateAllFields(true)) {
                            boolean isSave = saveFlight(true);
                        }
                    }
                });
                break;
            case R.id.btn_cancel:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.CANCEL);
                break;
            case R.id.btn_next:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.NEXT);
                break;
            case R.id.btn_done:
                nextPrevFocus(MccEnum.topKeyboardCustomInput.DONE);
                break;
            case R.id.btn_route_shortcut:
                mSvFlightAdd.smoothScrollTo(0, mLlRoute.getTop() + 10);
                break;
            case R.id.btn_hour_shortcut:
                mSvFlightAdd.smoothScrollTo(0, mLlHours.getTop() + 10);
                break;
            case R.id.btn_currencies_shortcut:
                mSvFlightAdd.smoothScrollTo(0, mLlCurrencies.getTop() + 10);
                break;
            case R.id.btn_crew_shortcut:
                mSvFlightAdd.smoothScrollTo(0, mLlCrew.getTop() + 10);
                break;
            case R.id.btn_log_shortcut:
                mSvFlightAdd.smoothScrollTo(0, mLlLogReport.getTop() + 10);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        clearFocus();
        hideTopKey();
    }

    private void clearFocus() {
        View v = mActivity.getCurrentFocus();
        if (v != null && v instanceof MccEditText) {
            v.clearFocus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == flightUtils.REQUEST_CODE_SIGNATURE /*&& resultCode == Activity.RESULT_OK*/) {
            //mItemFlightCurrenciesSignPen.setDescription(MCCPilotLogConst.X_MARK);
            ImagePic imagePic = mDatabaseManagerV5.getSignatureByFlightCode(mFlight.getFlightCode());
            if(imagePic!=null){
                setImageForSignature(imagePic.getFileName());
                mItemFlightCurrenciesSignPen.showImageSign();
            }
        }
    }

    private void setImageForSignature(String imageName) {
        createSignFolder();
        String fileNamePic = mSignDir.getPath() + File.separator + imageName;
        File signImgFile = new File(fileNamePic);
        /*Show bitmap from file*/
        if (signImgFile.exists()) {
            Bitmap bm = null;
            try {
                bm = PhotoUtils.decodeBitmapFromUri(mActivity, Uri.fromFile(signImgFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mItemFlightCurrenciesSignPen.setImageSign(bm);
        }
    }

    private File mSignDir;

    private String createSignFolder() {
        String pathToFiles = StorageUtils.getStorageRootFolder(mActivity);
        File folderFiles = new File(pathToFiles);
        String path = folderFiles + File.separator + getString(R.string.sign_folder);
        mSignDir = new File(path);
        if (!mSignDir.exists()) {
            mSignDir.mkdir();
        }
        return path;
    }

    private void showSignatureScreen() {
        Bundle b = new Bundle();
        b.putByteArray(MCCPilotLogConst.FLIGHT_CODE, mFlight.getFlightCode());
        Intent i = new Intent(mActivity, SignatureActivity.class);
        i.putExtra(MCCPilotLogConst.SIGNATURE_BUNDLE, b);
        startActivityForResult(i, flightUtils.REQUEST_CODE_SIGNATURE);
    }

    public void setMZLaunchCodes(String zLaunchCodes) {
        if (!isChange) isChange = true;
        mZLaunchCodes = zLaunchCodes;
    }

    public String getmZLaunchCodes() {
        return mZLaunchCodes;
    }

    public void setmZApproachCodes(String zApproachCodes) {
        if (!isChange) isChange = true;
        mZApproachCodes = zApproachCodes;
    }

    public String getmZApproachCodes() {
        return mZApproachCodes;
    }

    public void setmZDelayCodes(String zDelayCodes) {
        if (!isChange) isChange = true;
        mZDelayCodes = zDelayCodes;
    }

    public String getmZDelayCodes() {
        return mZDelayCodes;
    }

    public void setmZOperationCodes(String zOperationCodes) {
        if (!isChange) isChange = true;
        mZOperationCodes = zOperationCodes;
    }

    public String getmZOperationCodes() {
        return mZOperationCodes;
    }

    private boolean isNullOrEmptyItemsFlightView(ItemsFlightView itemsFlightView) {
        return TextUtils.isEmpty(itemsFlightView.getDescription());
    }

    private boolean checkEnableField(ItemsFlightView itemsFlightView) {
        return itemsFlightView.getVisibility() == View.VISIBLE;
    }

    private boolean showOkAlertDialog(final int title, final int message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkAlertDialog(mActivity, title, message).show();
            }
        });
        return false;
    }

    private boolean showOkAlertDialog(final int title, final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkAlertDialog(mActivity, title, message).show();
            }
        });

        return false;
    }

    boolean resultSaveDialog = false;

    private boolean showSaveCancelDialog(final int title, final int message) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getSaveCancelAlertDialog(mActivity, title, message, new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        countDownLatch.countDown();
                        if (pDialogType == DialogInterface.BUTTON_NEGATIVE) {
                            resultSaveDialog = false;
                        } else {
                            resultSaveDialog = true;
                        }
                    }
                }).show();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
        return resultSaveDialog;
    }

    private boolean showSaveCancelDialog(final String title, final String message) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getSaveCancelAlertDialog(mActivity, title, message, new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        countDownLatch.countDown();
                        if (pDialogType == DialogInterface.BUTTON_NEGATIVE) {
                            resultSaveDialog = false;
                        } else {
                            resultSaveDialog = true;
                        }
                    }
                }).show();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
        return resultSaveDialog;
    }

    private boolean showOkCancelDialog(final int title, final int message) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MccDialog.getOkCancelAlertDialog(mActivity, title, message, new MccDialog.MCCDialogCallBack() {
                    @Override
                    public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                        countDownLatch.countDown();
                        if (pDialogType == DialogInterface.BUTTON_NEGATIVE) {
                            resultSaveDialog = false;
                        } else {
                            resultSaveDialog = true;
                        }
                    }
                }).show();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
        return resultSaveDialog;
    }

    private boolean validate12TimeFieldsExceedTotalTime(ItemsFlightView itemsFlightView, int minTotalActual) {
        if (itemsFlightView.getVisibility() == View.VISIBLE) {
            int minute = TimeUtils.convertHourToMin(itemsFlightView);
            if (minute > minTotalActual) {
                return showOkAlertDialog(R.string.invalid_time_title,
                        String.format(getString(R.string.invalid_time_message), itemsFlightView.getTextTitle()));
            }
        }
        return true;
    }

    private boolean validate4UserFieldsExceedTotalTime(ItemsFlightView itemsFlightView, int minTotalActual) {
        if (itemsFlightView.getVisibility() == View.VISIBLE) {
            int minute = TimeUtils.convertHourToMin(itemsFlightView);
            if (minute > minTotalActual) {
                if (!showSaveCancelDialog(getString(R.string.confirm_time_title),
                        String.format(getString(R.string.invalid_time_message), itemsFlightView.getTextTitle())))
                    return false;
            }
        }
        return true;
    }

    private int calculate5TimeFields() {
        int total = 0;
        if (checkEnableField(mItemFlightPic) && !isNullOrEmptyItemsFlightView(mItemFlightPic)) {
            total += TimeUtils.convertHourToMin(mItemFlightPic);//.getMinutesData();
        }
        if (checkEnableField(mItemFlightPicUS) && !isNullOrEmptyItemsFlightView(mItemFlightPicUS)) {
            total += TimeUtils.convertHourToMin(mItemFlightPicUS);//.getMinutesData();
        }
        if (checkEnableField(mItemFlightCopilot) && !isNullOrEmptyItemsFlightView(mItemFlightCopilot)) {
            total += TimeUtils.convertHourToMin(mItemFlightCopilot);//.getMinutesData();
        }
        if (checkEnableField(mItemFlightDual) && !isNullOrEmptyItemsFlightView(mItemFlightDual)) {
            total += TimeUtils.convertHourToMin(mItemFlightDual);//.getMinutesData();
        }
        if (checkEnableField(mItemFlightRelief) && !isNullOrEmptyItemsFlightView(mItemFlightRelief)) {
            total += TimeUtils.convertHourToMin(mItemFlightRelief);//.getMinutesData();
        }
        return total;
    }

    private boolean isHavePilotSelfOn4Fields() {
        if (checkEnableField(mItemFlightCrewPic) && mPilot1 != null && Utils.getGUIDFromByteArray(mPilot1.getPilotCode()).equals(MCCPilotLogConst.PILOT_CODE_SELF))
            return true;
        if (checkEnableField(mItemFlightCrew2nd) && mPilot2 != null && Utils.getGUIDFromByteArray(mPilot2.getPilotCode()).equals(MCCPilotLogConst.PILOT_CODE_SELF))
            return true;
        if (checkEnableField(mItemFlightCrew3rd) && mPilot3 != null && Utils.getGUIDFromByteArray(mPilot3.getPilotCode()).equals(MCCPilotLogConst.PILOT_CODE_SELF))
            return true;
        if (checkEnableField(mItemFlightCrew4th) && mPilot4 != null && Utils.getGUIDFromByteArray(mPilot4.getPilotCode()).equals(MCCPilotLogConst.PILOT_CODE_SELF))
            return true;
        return false;
    }

   /* private void showConfirmAlertDialog(int title, int message){
        MccDialog.getYesNoAlertDialog(mActivity, title, message);
        return;
    }*/

    private int minActualInstrument = 0;
    int minTotalActual = 0, minTotalSchedule, minTotalAirTime;
    String timeOutTimeUTC = "", timeInTimeUTC = "", timeScheduleOutUTC = "", timeScheduleInUTC = "", timeOffUTC = "", timeOnUTC = "";

    private boolean validateAllFields(boolean isDraftSave) {
        if (isNullOrEmptyItemsFlightView(mItemFlightRouteDate)) {
            return showOkAlertDialog(R.string.invalid_date, R.string.missing_flight_date);
        }
        if (isNullOrEmptyItemsFlightView(mItemFlightRouteAircraft) || mAircraft == null) {
            return showOkAlertDialog(R.string.invalid_aircraft, R.string.invalid_aircraft_message);
        }
        if (isNullOrEmptyItemsFlightView(mItemFlightRouteDeparture) || mAirfieldDeparture == null) {
            return showOkAlertDialog(R.string.invalid_airfield, R.string.invalid_departure_airfield_message);
        }
        if (mAircraft.getDeviceCode() == 2 || mAircraft.getDeviceCode() == 3) {
            mAirfieldArrival = mAirfieldDeparture;
        } else {
            if (isNullOrEmptyItemsFlightView(mItemFlightRouteArrival) || mAirfieldArrival == null) {
                return showOkAlertDialog(R.string.invalid_airfield, R.string.invalid_arrival_airfield_message);
            }
        }

        if (isDraftSave) {
            return true;
        }
        double hobbsTotalTime = 0;
        if (checkEnableField(mItemFlightHobbsOut)) {
            //yes
            if (!isNullOrEmptyItemsFlightView(mItemFlightHobbsOut) && !isNullOrEmptyItemsFlightView(mItemFlightHobbsIn)) {
                //both hobbs logged
                Double hobbsOut = Utils.parseDouble(mItemFlightHobbsOut.getDescription());
                Double hobbsIn = Utils.parseDouble(mItemFlightHobbsIn.getDescription());
                if (hobbsIn < hobbsOut) {
                    return showOkAlertDialog(R.string.invalid_hobbs, R.string.invalid_hobbs_message);
                } else {
                    hobbsTotalTime = hobbsIn - hobbsOut;
                }
            } else {
                if (isNullOrEmptyItemsFlightView(mItemFlightHobbsOut) && isNullOrEmptyItemsFlightView(mItemFlightHobbsIn)) {
                    //bot hobbs empty
                    if (!checkEnableField(mItemFlightOffBlock)) {
                        //out/in hours not enable
                        return showOkAlertDialog(R.string.invalid_hobbs, R.string.invalid_hobbs_missing_message);
                    }
                } else {
                    return showOkAlertDialog(R.string.invalid_hobbs, R.string.invalid_hobbs_missing_message);
                }
            }
        }
        int minTotalCalculate = 0;
        if (mItemFlightOffBlock.getVisibility() == View.VISIBLE) {
            if (isNullOrEmptyItemsFlightView(mItemFlightOffBlock) || isNullOrEmptyItemsFlightView(mItemFlightOnBlock)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightOffBlock) && isNullOrEmptyItemsFlightView(mItemFlightOnBlock)) {
                    //out in hours both empty
                    if (checkEnableField(mItemFlightHobbsOut)) {
                        if (hobbsTotalTime <= 0) {
                            return showOkAlertDialog(R.string.invalid_out_in, R.string.invalid_out_in_message);
                        } else {
                            minTotalCalculate = TimeUtils.convertHourToMin(String.valueOf(hobbsTotalTime));
                        }
                    } else {
                        return showOkAlertDialog(R.string.invalid_out_in, R.string.invalid_out_in_message);
                    }
                } else {
                    if (!showSaveCancelDialog(R.string.confirm_block_hours, isNullOrEmptyItemsFlightView(mItemFlightOffBlock) ? R.string.confirm_out_hours_message :
                            R.string.confirm_in_hours_message))
                        return false;
                    timeOutTimeUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightOffBlock) ? "00:00" : mItemFlightOffBlock.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                    timeInTimeUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightOnBlock) ? "00:00" : mItemFlightOnBlock.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                    minTotalCalculate = TimeUtils.calcTotalMinute(timeOutTimeUTC, timeInTimeUTC, flightUtils.getAccuracy());
                }
            } else {
                timeOutTimeUTC = flightUtils.convertTimeToUTC(mItemFlightOffBlock.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                timeInTimeUTC = flightUtils.convertTimeToUTC(mItemFlightOnBlock.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                minTotalCalculate = TimeUtils.calcTotalMinute(timeOutTimeUTC, timeInTimeUTC, flightUtils.getAccuracy());
            }

            if (mItemFlightTotal.getVisibility() == View.VISIBLE) {
                minTotalActual = TimeUtils.convertHourToMin(mItemFlightTotal);
                if ((minTotalActual > minTotalCalculate + 3) || (!isLogTimeDecimal && minTotalActual > minTotalCalculate)) {
                    return showOkAlertDialog(R.string.invalid_total_time, R.string.total_time_difference_from_on_off_block);
                } else if ((minTotalActual < minTotalCalculate - 3) || (!isLogTimeDecimal && minTotalActual < minTotalCalculate)) {
                    if (!showSaveCancelDialog(R.string.confirm_total_time, R.string.total_time_not_match_with_block_hours))
                        return false;
                }
            } else {
                minTotalActual = minTotalCalculate;
            }
        }
        if (minTotalActual > 0) {
            if (minTotalActual > 1320) {
                return showOkAlertDialog(R.string.invalid_total_time, R.string.exceed_max_total_time);
            }
            if (minTotalActual > 840) {
                if (!showSaveCancelDialog(R.string.confirm_total_time, R.string.total_time_exceed_one_four_hours))
                    return false;
            }

            if (!validate12TimeFieldsExceedTotalTime(mItemFlightPic, minTotalActual)) return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightPicUS, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightCopilot, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightDual, minTotalActual)) return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightInstructor, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightExaminer, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightRelief, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightNight, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightIFR, minTotalActual)) return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightActualInstrument, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightSimulatedInstrument, minTotalActual))
                return false;
            if (!validate12TimeFieldsExceedTotalTime(mItemFlightXC, minTotalActual)) return false;
            if (!validate4UserFieldsExceedTotalTime(mItemFlightUserActive1, minTotalActual))
                return false;
            if (!validate4UserFieldsExceedTotalTime(mItemFlightUserActive2, minTotalActual))
                return false;
            if (!validate4UserFieldsExceedTotalTime(mItemFlightUserActive3, minTotalActual))
                return false;
            if (!validate4UserFieldsExceedTotalTime(mItemFlightUserActive4, minTotalActual))
                return false;
            if (checkEnableField(mItemFlightPic) || checkEnableField(mItemFlightPicUS) || checkEnableField(mItemFlightCopilot)
                    || checkEnableField(mItemFlightDual) || checkEnableField(mItemFlightInstructor) || checkEnableField(mItemFlightExaminer)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightPic) && isNullOrEmptyItemsFlightView(mItemFlightPicUS)
                        && isNullOrEmptyItemsFlightView(mItemFlightCopilot) && isNullOrEmptyItemsFlightView(mItemFlightDual)
                        && isNullOrEmptyItemsFlightView(mItemFlightInstructor) && isNullOrEmptyItemsFlightView(mItemFlightExaminer)) {
                    if (!showSaveCancelDialog(R.string.confirm_function_time, R.string.missing_function_time))
                        return false;
                }
                int minTotal5TimeFields = calculate5TimeFields();
                if (minTotal5TimeFields > minTotalActual) {
                    if (!showSaveCancelDialog(R.string.confirm_function_time, R.string.five_time_fields_exceed_total_time))
                        return false;
                }
            }
        }

        //

        if (checkEnableField(mItemFlightScheduledOut)) {
            if (!isNullOrEmptyItemsFlightView(mItemFlightScheduledOut) && !isNullOrEmptyItemsFlightView(mItemFlightScheduledIn)) {
                timeScheduleOutUTC = flightUtils.convertTimeToUTC(mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                timeScheduleInUTC = flightUtils.convertTimeToUTC(mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                minTotalSchedule = TimeUtils.calcTotalMinute(timeScheduleOutUTC, timeScheduleInUTC, flightUtils.getAccuracy());
                if (minTotalActual > 0) {
                    if ((minTotalSchedule > minTotalActual + minTotalActual * 0.1) || minTotalSchedule < minTotalActual - minTotalActual * 0.1) {
                        int resId = 0;
                        if (mAircraft.getDeviceCode() == 1)
                            resId = R.string.confirm_schedule_was_two_seven_hours_flight;
                        if (mAircraft.getDeviceCode() == 2)
                            resId = R.string.confirm_schedule_was_two_seven_hours_simulator;
                        if (mAircraft.getDeviceCode() == 3)
                            resId = R.string.confirm_schedule_was_two_seven_hours_drone;
                        String scheduleHour = TimeUtils.convertMinuteToHour(String.valueOf(minTotalSchedule), isLogTimeDecimal);
                        if (!showSaveCancelDialog(getString(R.string.confirm_total_time), String.format(getString(resId), scheduleHour)))
                            return false;
                    }
                }
            } else {
                if (!isNullOrEmptyItemsFlightView(mItemFlightScheduledOut) || !isNullOrEmptyItemsFlightView(mItemFlightScheduledIn)) {
                    if (!showSaveCancelDialog(getString(R.string.confirm_schedule_hours),
                            String.format(getString(R.string.confirm_schedule_as_midnight), !isNullOrEmptyItemsFlightView(mItemFlightScheduledOut) ? "Out" : "In")))
                        return false;
                    timeScheduleOutUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightScheduledOut) ? "00:00" : mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                    timeScheduleInUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightScheduledIn) ? "00:00" : mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                    minTotalSchedule = TimeUtils.calcTotalMinute(timeScheduleOutUTC, timeScheduleInUTC, flightUtils.getAccuracy());
                    if (minTotalActual > 0) {
                        if ((minTotalSchedule > minTotalActual + minTotalActual * 0.2) || minTotalSchedule < minTotalActual - minTotalActual * 0.2) {
                            int resId = 0;
                            if (mAircraft.getDeviceCode() == 1)
                                resId = R.string.confirm_schedule_was_two_seven_hours_flight;
                            if (mAircraft.getDeviceCode() == 2)
                                resId = R.string.confirm_schedule_was_two_seven_hours_simulator;
                            if (mAircraft.getDeviceCode() == 3)
                                resId = R.string.confirm_schedule_was_two_seven_hours_drone;
                            if (!showSaveCancelDialog(getString(R.string.confirm_total_time), String.format(getString(resId), TimeUtils.convertMinuteToHour(String.valueOf(minTotalSchedule), isLogTimeDecimal))))//isLogTimeDecimal ? "2.7" : "2:40")))
                                return false;
                        }
                    }
                } else {
                    if (!showSaveCancelDialog(R.string.confirm_schedule_hours, R.string.confirm_schedule_hours_log_without_block_hours))
                        return false;
                    minTotalSchedule = 0;
                }
            }
        }


        if (checkEnableField(mItemFlightTakeOff)) {
            if (mAircraft.getDeviceCode() == 1) {
                if (!isNullOrEmptyItemsFlightView(mItemFlightTakeOff) && !isNullOrEmptyItemsFlightView(mItemFlightLanding)) {
                    timeOffUTC = flightUtils.convertTimeToUTC(mItemFlightTakeOff.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                    timeOnUTC = flightUtils.convertTimeToUTC(mItemFlightLanding.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                    minTotalAirTime = TimeUtils.calcTotalMinute(timeOffUTC, timeOnUTC, flightUtils.getAccuracy());
                } else if (isNullOrEmptyItemsFlightView(mItemFlightTakeOff) && isNullOrEmptyItemsFlightView(mItemFlightLanding)) {
                    if (!showSaveCancelDialog(R.string.confirm_to_ldg_hours, R.string.confirm_to_ldg_hours_without_off_on_message))
                        return false;
                    minTotalAirTime = 0;
                } else {
                    if (!showSaveCancelDialog(getString(R.string.confirm_to_ldg_hours),
                            String.format(getString(R.string.confirm_log_to_ldg_hours_as_midnight),
                                    !isNullOrEmptyItemsFlightView(mItemFlightTakeOff) ? "Takeoff" : "Landing")))
                        return false;
                    timeOffUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightTakeOff) ? "00:00" : mItemFlightTakeOff.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
                    timeOnUTC = flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightLanding) ? "00:00" : mItemFlightLanding.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate);
                    minTotalAirTime = TimeUtils.calcTotalMinute(timeOffUTC, timeOnUTC, flightUtils.getAccuracy());
                }
                if (minTotalAirTime > minTotalActual) {
                    return showOkAlertDialog(R.string.invalid_air_time, R.string.verify_take_off_and_landing);
                }
                if (checkEnableField(mItemFlightOffBlock)) {
                    int minOutTime = TimeUtils.convertHourToMin(mItemFlightOffBlock.getDescription());
                    int minTakeOff = TimeUtils.convertHourToMin(mItemFlightTakeOff.getDescription());
                    if (minTakeOff != 0) {
                        if (minTakeOff - minOutTime > 30) {
                            if (!showSaveCancelDialog(R.string.confirm_air_time, R.string.verify_take_off_time))
                                return false;
                        } else if (minTakeOff < minOutTime) {
                            return showOkAlertDialog(R.string.invalid_air_time, R.string.invalid_take_off_time);
                        }
                    }
                    int minOnTime = TimeUtils.convertHourToMin(mItemFlightOnBlock.getDescription());
                    int minLanding = TimeUtils.convertHourToMin(mItemFlightLanding.getDescription());
                    if (minLanding != 0) {
                        if (minOnTime - minLanding > 30) {
                            if (!showSaveCancelDialog(R.string.confirm_air_time, R.string.verify_landing_time))
                                return false;
                        } else if (minLanding > minOnTime) {
                            return showOkAlertDialog(R.string.invalid_air_time, R.string.invalid_landing_time);
                        }
                    }

                }
            }
        }
        if (checkEnableField(mItemFlightCrewPic) || checkEnableField(mItemFlightCrew2nd) ||
                checkEnableField(mItemFlightCrew3rd) || checkEnableField(mItemFlightCrew4th)) {
            if (checkEnableField(mItemFlightCrewPic) && mPilot1 == null && !isNullOrEmptyItemsFlightView(mItemFlightCrewPic))
                return showOkAlertDialog(R.string.invalid_pilot, R.string.invalid_pilot_message);
            if (checkEnableField(mItemFlightCrew2nd) && mPilot2 == null && !isNullOrEmptyItemsFlightView(mItemFlightCrew2nd))
                return showOkAlertDialog(R.string.invalid_pilot, R.string.invalid_pilot_message);
            if (checkEnableField(mItemFlightCrew3rd) && mPilot3 == null && !isNullOrEmptyItemsFlightView(mItemFlightCrew3rd))
                return showOkAlertDialog(R.string.invalid_pilot, R.string.invalid_pilot_message);
            if (checkEnableField(mItemFlightCrew4th) && mPilot4 == null && !isNullOrEmptyItemsFlightView(mItemFlightCrew4th))
                return showOkAlertDialog(R.string.invalid_pilot, R.string.invalid_pilot_message);
            if (!isHavePilotSelfOn4Fields()) {
                if (!showSaveCancelDialog(R.string.confirm_pilot, R.string.confirm_pilot_log_without_self))
                    return false;
            }
            if (checkEnableField(mItemFlightCrewPic)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightCrewPic)) {
                    if (!showSaveCancelDialog(R.string.confirm_pilot, R.string.confirm_pilot_in_command_message))
                        return false;
                }
                if (checkEnableField(mItemFlightCrew2nd)) {
                    if (isNullOrEmptyItemsFlightView(mItemFlightCrew2nd) && mAircraft.getCategory() == 2) {
                        if (!showSaveCancelDialog(R.string.confirm_pilot, R.string.confirm_co_pilot_message))
                            return false;
                    }
                }
            }
        }

        if (mAircraft.getDeviceCode() == 2 || mAircraft.getDeviceCode() == 3)
            return true;

        if (checkEnableField(mItemFlightCurrenciesOperation)) {
            if (!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesOperation) && mItemFlightCurrenciesOperation.getDescription().contains("SOLO")) {
                if (mAircraft.getCategory() != 1) {
                    if (!showSaveCancelDialog(R.string.confirm_solo_time, R.string.confirm_solo_time_message))
                        return false;
                }
                int minPic = 0;
                if (!isNullOrEmptyItemsFlightView(mItemFlightPic)) { //if (checkEnableField(mItemFlightPic) &&
                    minPic = TimeUtils.convertHourToMin(mItemFlightPic);
                }
                if (minPic == 0) {
                    if (!showSaveCancelDialog(R.string.confirm_solo_time, R.string.confirm_log_solo_time_message))
                        return false;
                }
            }
        }
        if (checkEnableField(mItemFlightCurrenciesApproach)) {
            if (!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesApproach)
                    && mItemFlightCurrenciesApproach.getDescription().contains("Visual")
                    && minTotalActual > 0) {
                if (!isNullOrEmptyItemsFlightView(mItemFlightActualInstrument)) {
                    minActualInstrument = TimeUtils.convertHourToMin(mItemFlightActualInstrument);
                    if (minActualInstrument == minTotalActual) {
                        final CountDownLatch countDownLatch = new CountDownLatch(1);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MccDialog.getYesNoAlertDialog(mActivity, R.string.confirm_act_instr,
                                        isLogTimeDecimal ? R.string.confirm_act_instr_message_decimal : R.string.confirm_act_instr_message_non_decimal,
                                        new MccDialog.MCCDialogCallBack() {
                                            @Override
                                            public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                                                countDownLatch.countDown();
                                                if (pDialogType != DialogInterface.BUTTON_NEGATIVE) {
                                                    minActualInstrument = minActualInstrument - 4;
                                                    mItemFlightActualInstrument.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(minActualInstrument), isLogTimeDecimal));
                                                    mItemFlightActualInstrument.setMinutesData(minActualInstrument);
                                                }
                                            }
                                        }).show();
                            }
                        });
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
        if (checkEnableField(mItemFlightCurrenciesGlider)) {
            if (!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesGlider) && mAircraft.getClassZ() != 2 && mAircraft.getClassZ() != 6) {
                if (!showSaveCancelDialog(R.string.confirm_launch_type, R.string.confirm_launch_type_message))
                    return false;
            }
        }
        if (checkEnableField(mItemFlightCurrenciesTOday) || checkEnableField(mItemFlightCurrenciesTOnight)
                || checkEnableField(mItemFlightCurrenciesLDGDay) || checkEnableField(mItemFlightCurrenciesLDGNight)) {
            if (checkEnableField(mItemFlightNight)) {
                int minToNight = 0, minLdgNight = 0, minToDay = 0, minLdgDay = 0, minNightTime = 0;
                if (checkEnableField(mItemFlightCurrenciesTOnight) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesTOnight)) {
                    minToNight = Integer.parseInt(mItemFlightCurrenciesTOnight.getDescription());
                }
                if (checkEnableField(mItemFlightCurrenciesLDGNight) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesLDGNight)) {
                    minLdgNight = Integer.parseInt(mItemFlightCurrenciesLDGNight.getDescription());
                }
                if (!isNullOrEmptyItemsFlightView(mItemFlightNight)) {
                    minNightTime = TimeUtils.convertHourToMin(mItemFlightNight);
                }
                if ((minToNight > 0 || minLdgNight > 0) && minNightTime == 0) {
                    if (!showSaveCancelDialog(R.string.confirm_night_time, R.string.confirm_night_time_message))
                        return false;
                }
                if (checkEnableField(mItemFlightCurrenciesTOday) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesTOday)) {
                    minToDay = Integer.parseInt(mItemFlightCurrenciesTOday.getDescription());
                }
                if (checkEnableField(mItemFlightCurrenciesLDGDay) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesLDGDay)) {
                    minLdgDay = Integer.parseInt(mItemFlightCurrenciesLDGDay.getDescription());
                }
                if ((minToDay > 0 || minLdgDay > 0) && (minToNight == 0 && minLdgNight == 0) && minNightTime == minTotalActual) {
                    if (!showSaveCancelDialog(R.string.confirm_night_time, R.string.confirm_log_to_ldg_during_day))
                        return false;
                }
            }
        }
        if (checkEnableField(mItemFlightCurrenciesPax)) {
            int minPax = 0;
            if (!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesPax)) {
                minPax = Integer.parseInt(mItemFlightCurrenciesPax.getDescription());
            }
            if (minPax == 0) {
                if (!showSaveCancelDialog(R.string.confirm_pax, R.string.pax_message)) return false;
            } else {
                if (mAircraft.getSeats() > 0 && minPax > (mAircraft.getSeats() + mAircraft.getSeats() * 0.04)) {
                    if (!showSaveCancelDialog(getString(R.string.confirm_pax),
                            String.format(getString(R.string.confirm_pax_message_sub_one), mAircraft.getSeats())
                                    + String.format(getString(R.string.confirm_pax_message_sub_two), minPax)))
                        return false;
                }
            }
        }
        if (flightUtils.getSettingDataByCode(MCCPilotLogConst.SETTING_CODE_FUELS_MONITORING).equals("1")) {
          /*  if (checkEnableField(mItemFlightCurrenciesFuel)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightCurrenciesFuel) || Integer.parseInt(mItemFlightCurrenciesFuel.getDescription()) <= 0) {
                    return showOkAlertDialog(R.string.invalid_fuel, R.string.fuel_less_than_zero);
                }
            }
            if (checkEnableField(mItemFlightCurrenciesFuelUsed)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightCurrenciesFuelUsed) || Integer.parseInt(mItemFlightCurrenciesFuelUsed.getDescription()) <= 0) {
                    return showOkAlertDialog(R.string.invalid_fuel, R.string.fuel_used_less_than_zero);
                }
            }
            if (checkEnableField(mItemFlightCurrenciesFuelPlanned)) {
                if (isNullOrEmptyItemsFlightView(mItemFlightCurrenciesFuelPlanned) || Integer.parseInt(mItemFlightCurrenciesFuelPlanned.getDescription()) <= 0) {
                    return showOkAlertDialog(R.string.invalid_fuel, R.string.fuel_planned_less_than_zero);
                }
            }*/
            if (checkEnableField(mItemFlightCurrenciesFuel) && checkEnableField(mItemFlightCurrenciesFuelUsed)) {
                if (!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesFuel) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesFuelUsed)) {
                    int fuel;
                    int fuelUsed;
                    fuel = Integer.parseInt(mItemFlightCurrenciesFuel.getDescription());
                    fuelUsed = Integer.parseInt(mItemFlightCurrenciesFuelUsed.getDescription());
                    if (fuelUsed > fuel) {
                        return showOkAlertDialog(R.string.invalid_fuel, R.string.fuel_exceed_block_fuel);
                    }
                    boolean isShowConfirmFuel = false;
                    if (fuel < 1000) {
                        if (fuelUsed < 0.35 * fuel) {
                            isShowConfirmFuel = true;
                        }
                    } else if (fuel < 5000) {
                        if (fuelUsed < 0.5 * (fuel - 1000)) {
                            isShowConfirmFuel = true;


                        }
                    } else {
                        if (fuelUsed < 0.5 * (fuel - 2500)) {
                            isShowConfirmFuel = true;
                        }
                    }
                    if (isShowConfirmFuel) {
                        final double percent = (double) fuelUsed / fuel;
                        if (!showSaveCancelDialog(getString(R.string.confirm_fuel), String.format(getString(R.string.confirm_fuel_message), (int) (percent * 100), "%")))
                            return false;
                    }
                } else {
                    if (!showSaveCancelDialog(R.string.confirm_fuel, R.string.confirm_without_fuel_message))
                        return false;
                }
            }
        }
        return true;
    }

    private boolean saveFlight(boolean isDraft) {
        Flight flight = new Flight();

       /* if (mFlight.getFlightCode() != null) {
            flightCode = mFlight.getFlightCode();
        } else {
            flightCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        }*/
        flight.setFlightCode(mFlight.getFlightCode());
        Date selectedDate = new Date(mItemFlightRouteDate.getDescription());
        if (selectedDate != null) {
            if (TextUtils.isEmpty(timeOutTimeUTC))
                timeOutTimeUTC = flightUtils.convertTimeToUTC(!TextUtils.isEmpty(mItemFlightOffBlock.getDescription()) ?
                        mItemFlightOffBlock.getDescription() : "00:00", timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate);
            if (!TextUtils.isEmpty(timeOutTimeUTC)) {
                Calendar calendarUTC = getDate(selectedDate, TimeUtils.convertHourToMin(timeOutTimeUTC), FlightUtils.TimeMode.UTC);
                flight.setDateUTC(flightUtils.DB_DATE_FORMAT.format(calendarUTC.getTime()));
                Calendar calendarLocal = getDate(selectedDate, TimeUtils.convertHourToMin(timeOutTimeUTC), FlightUtils.TimeMode.LOCAL);
                flight.setDateLOCAL(flightUtils.DB_DATE_FORMAT.format(calendarLocal.getTime()));
                Calendar calendarBase = getDate(selectedDate, TimeUtils.convertHourToMin(timeOutTimeUTC), FlightUtils.TimeMode.BASE);
                flight.setDateBASE(flightUtils.DB_DATE_FORMAT.format(calendarBase.getTime()));
            } else {
                flight.setDateUTC(flightUtils.DB_DATE_FORMAT.format(selectedDate));
                flight.setDateLOCAL(flightUtils.DB_DATE_FORMAT.format(selectedDate));
                flight.setDateBASE(flightUtils.DB_DATE_FORMAT.format(selectedDate));
            }
        }

        flight.setFlightNumber(mItemFlightRouteFlightNo.getDescription());
        flight.setPairing(mItemFlightRoutePairing.getDescription());
        flight.setDepRwy(mItemFlightRouteRunwayDeparture.getDescription());
        flight.setArrRwy(mItemFlightRouteRunwayArrival.getDescription());
        flight.setAircraftCode(mAircraft.getAircraftCode());
        flight.setDepCode(mAirfieldDeparture.getAFCode());
        if (!mAirfieldDeparture.getShowList()) {
            mAirfieldDeparture.setShowList(true);
            mDatabaseManagerV5.insertOrUpdateAirfield(mAirfieldDeparture);
        }
        flight.setArrCode(mAirfieldArrival.getAFCode());
        if (!mAirfieldArrival.getShowList() && mAirfieldArrival != mAirfieldDeparture) {
            mAirfieldArrival.setShowList(true);
            mDatabaseManagerV5.insertOrUpdateAirfield(mAirfieldArrival);
        }
        if (!isNullOrEmptyItemsFlightView(mItemFlightHobbsOut) && !isNullOrEmptyItemsFlightView(mItemFlightHobbsIn)) {
            flight.setHobbsOut(Long.parseLong(mItemFlightHobbsOut.getDescription().replace(",", "").replace(".", "")));
            flight.setHobbsIn(Long.parseLong(mItemFlightHobbsIn.getDescription().replace(",", "").replace(".", "")));
        }
        if (!isDraft) {

            flight.setDepTimeUTC(TimeUtils.convertHourToMin(timeOutTimeUTC));
            flight.setArrTimeUTC(TimeUtils.convertHourToMin(timeInTimeUTC));
            flight.setDepTimeSCHED(TimeUtils.convertHourToMin(timeScheduleOutUTC));
            flight.setArrTimeSCHED(TimeUtils.convertHourToMin(timeScheduleInUTC));
            flight.setToTimeUTC(TimeUtils.convertHourToMin(timeOffUTC));
            flight.setLdgTimeUTC(TimeUtils.convertHourToMin(timeOnUTC));
            flight.setMinTOTAL(minTotalActual);
            flight.setMinAIR(minTotalAirTime);
            flight.setMinIMT(minActualInstrument == 0 ? TimeUtils.convertHourToMin(mItemFlightActualInstrument) : minActualInstrument);
        } else {
            flight.setDepTimeUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightOffBlock.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate)));
            flight.setArrTimeUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightOnBlock.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate)));
            flight.setDepTimeSCHED(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightScheduledOut.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate)));
            flight.setArrTimeSCHED(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightScheduledIn.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate)));
            flight.setToTimeUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightTakeOff.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, mFlightDate)));
            flight.setLdgTimeUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(mItemFlightLanding.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, mFlightDate)));
            flight.setMinTOTAL(mItemFlightTotal.getMinutesData());
            flight.setMinAIR(mItemFlightTotal.getMinutesDataOnlyForMinArr());
            flight.setMinIMT(mItemFlightActualInstrument.getMinutesData());
        }
        flight.setBaseOffset(flightUtils.getBaseOffset(mFlightDate));
        flight.setDepOffset((int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate));
        flight.setArrOffset((int) flightUtils.getDepArrOffset(timeZoneArrival != null ? timeZoneArrival : timeZoneDeparture, timeZoneArrival != null ? timeZoneCodeArrival : timeZoneCodeDeparture, mFlightDate));
        flight.setMinPIC(mItemFlightPic.getMinutesData());
        flight.setMinPICUS(mItemFlightPicUS.getMinutesData());
        flight.setMinCOP(mItemFlightPicUS.getMinutesData());
        flight.setMinDUAL(mItemFlightDual.getMinutesData());
        flight.setMinINSTR(mItemFlightInstructor.getMinutesData());
        flight.setMinEXAM(mItemFlightExaminer.getMinutesData());
        flight.setMinREL(mItemFlightRelief.getMinutesData());
        flight.setMinNIGHT(mItemFlightNight.getMinutesData());
        flight.setMinXC(mItemFlightXC.getMinutesData());
        flight.setMinIFR(mItemFlightIFR.getMinutesData());
        flight.setMinSFR(mItemFlightSimulatedInstrument.getMinutesData());
        flight.setMinU1(mItemFlightUserActive1.getMinutesData());
        flight.setMinU2(mItemFlightUserActive2.getMinutesData());
        flight.setMinU3(mItemFlightUserActive3.getMinutesData());
        flight.setMinU4(mItemFlightUserActive4.getMinutesData());
        flight.setToDay(mItemFlightCurrenciesTOday.getDescriptionInt());
        flight.setToNight(mItemFlightCurrenciesTOnight.getDescriptionInt());
        flight.setLdgDay(mItemFlightCurrenciesLDGDay.getDescriptionInt());
        flight.setLdgNight(mItemFlightCurrenciesLDGNight.getDescriptionInt());
        flight.setHolding(mItemFlightCurrenciesHolding.getDescriptionInt());
        flight.setLiftSW(mItemFlightCurrenciesLifts.getDescriptionInt());
        flight.setFuel(mItemFlightCurrenciesFuel.getDescriptionInt());
        flight.setFuelUsed(mItemFlightCurrenciesFuelUsed.getDescriptionInt());
        flight.setFuelPlanned(mItemFlightCurrenciesFuelPlanned.getDescriptionInt());
        flight.setPax(mItemFlightCurrenciesPax.getDescriptionInt());
        flight.setDeIce(!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesPax) ? true : false);
        flight.setUserNum(mItemFlightCurrenciesUserN1.getDescriptionInt());
        flight.setUserText(mItemFlightCurrenciesUserN2.getDescription());
        flight.setUserBool(!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesUserN3) ? true : false);
        flight.setTagApproach(mZApproachCodes);
        flight.setTagLaunch(mZLaunchCodes);
        flight.setTagOps(mZOperationCodes);
        flight.setTagDelay(mZDelayCodes);
        flight.setPF(!isNullOrEmptyItemsFlightView(mItemFlightCurrenciesTask) ?
                (mItemFlightCurrenciesTask.getDescription().equals("PF") ? true : false) : true);//default = 1
        flight.setP1Code(mPilot1 != null ? mPilot1.getPilotCode() : Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY));
        flight.setP2Code(mPilot2 != null ? mPilot2.getPilotCode() : Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY));
        flight.setP3Code(mPilot3 != null ? mPilot3.getPilotCode() : Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY));
        flight.setP4Code(mPilot4 != null ? mPilot4.getPilotCode() : Utils.getByteArrayFromGUID(MCCPilotLogConst.PILOT_CODE_EMPTY));
        flight.setCrewList(mItemFlightCrewList.getDescription());
        flight.setTraining(mItemFlightLogInstruction.getDescription());
        flight.setReport(mItemFlightLogFlightReport.getDescription());
        flight.setRemarks(mItemFlightLogRemarks.getDescription());
        flight.setSignBox(mItemFlightCurrenciesSignBox.getDescriptionInt());
        flight.setNextPage(false);
        flight.setNextSummary(false);
        flight.setToEdit(isDraft);
        flight.setRecord_Upload(!isDraft);
        flight.setRecord_Modified(isDraft ? 0 : DateTimeUtils.getCurrentUTCUnixTimeStamp());
        boolean isSave = mDatabaseManagerV5.insertFlight(flight);
        if (isSave) {
            StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT1, Utils.getGUIDFromByteArray(flight.getP1Code()));
            StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT2, Utils.getGUIDFromByteArray(flight.getP2Code()));
            StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT3, Utils.getGUIDFromByteArray(flight.getP3Code()));
            StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT4, Utils.getGUIDFromByteArray(flight.getP4Code()));
            StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_AIRCRAFT, Utils.getGUIDFromByteArray(flight.getAircraftCode()));
            StorageUtils.writeBooleanToSharedPref(mActivity, StateKey.SAVED_TASK, !flight.getPF());

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMode != MODE_EDIT_FLIGHT_VIEW) {
                        final FlightListFragment flightListFragment = (FlightListFragment) getFragment(FlightListFragment.class);
                        if (flightListFragment != null) {
                            flightListFragment.refreshListFlight();
                        }
                        final DutyFragment dutyFragment = (DutyFragment) getFragment(DutyFragment.class);
                        if (dutyFragment != null) {
                            dutyFragment.refreshListDuty();
                        }
                        finishFragment();
                    } else {
                        finishFragment();
                    }

                }
            });
        }
        return isSave;
    }

    private int getDepTimeUTC(Integer minutes) {
        if (minutes == null || minutes == 0) return 0;
        Integer depOffset = mFlight.getDepOffset();
        Integer baseOffset = mFlight.getBaseOffset();
        int minutesReturn = minutes;
        if (timeMode == FlightUtils.TimeMode.BASE) {
            minutesReturn = (minutes + baseOffset + 1440) % 1440;
        } else if (timeMode == FlightUtils.TimeMode.LOCAL) {
            minutesReturn = (minutes + depOffset + 1440) % 1440;
        }
        return minutesReturn;
    }

    private int getArrTimeUTC(Integer minutes) {
        if (minutes == null || minutes == 0) return 0;
        Integer arrOffset = mFlight.getArrOffset();
        Integer baseOffset = mFlight.getBaseOffset();
        int minutesReturn = minutes;
        if (timeMode == FlightUtils.TimeMode.BASE) {
            minutesReturn = (minutes + baseOffset + 1440) % 1440;
        } else if (timeMode == FlightUtils.TimeMode.LOCAL) {
            minutesReturn = (minutes + arrOffset + 1440) % 1440;
        }
        return minutesReturn;
    }

    private Calendar getDate(Date currentDate, int minDepTimeUTC, FlightUtils.TimeMode dateModeResult) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        switch (dateMode) {
            case UTC:
                switch (dateModeResult) {
                    case UTC:
                        return calendar;
                    case LOCAL:
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                        break;
                    case BASE:
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                        break;
                }
            case LOCAL:
                switch (dateModeResult) {
                    case UTC:
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        }
                        break;
                    case LOCAL:
                        return calendar;
                    case BASE:
                        calendar = getDate(currentDate, minDepTimeUTC, FlightUtils.TimeMode.UTC);
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                }
                break;
            case BASE:
                switch (dateModeResult) {
                    case UTC:
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        }
                        break;
                    case LOCAL:
                        calendar = getDate(currentDate, minDepTimeUTC, FlightUtils.TimeMode.UTC);
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneDeparture, timeZoneCodeDeparture, mFlightDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                        break;
                    case BASE:
                        return calendar;
                }
                break;
        }
        return calendar;
    }

    RouteInfoCalculator routeInfoCalculator;

    private void setDataForNight() {
        Calendar flightDate = Calendar.getInstance();
        flightDate.setTime(mFlightDate.getTime());

        if (TextUtils.isEmpty(settingNightMode) || settingNightMode.equalsIgnoreCase("0")) return;
        routeInfoCalculator = new RouteInfoCalculator(mActivity, settingNightMode);
        int depHourUTC = TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightOffBlock)
                ? "00:00" : mItemFlightOffBlock.getDescription(), timeMode, timeZoneDeparture, timeZoneCodeDeparture, flightDate));
        int arrHourUTC = TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(isNullOrEmptyItemsFlightView(mItemFlightOnBlock)
                ? "00:00" : mItemFlightOnBlock.getDescription(), timeMode, timeZoneArrival, timeZoneCodeArrival, flightDate));
        int minTotal = mItemFlightTotal.getMinutesData();
        routeInfoCalculator.initRoute(mAirfieldDeparture, mAirfieldArrival, depHourUTC, arrHourUTC, minTotal, flightDate.getTime());
        routeInfoCalculator.calculateRoute();
        if (checkEnableField(mItemFlightCurrenciesTask) && !isNullOrEmptyItemsFlightView(mItemFlightCurrenciesTask) && mItemFlightCurrenciesTask.getDescription().equalsIgnoreCase("pf")) {
            if (checkEnableField(mItemFlightCurrenciesTOday) && routeInfoCalculator.route.TODay != 0)
                mItemFlightCurrenciesTOday.setDescription(String.valueOf(routeInfoCalculator.route.TODay));
            if (checkEnableField(mItemFlightCurrenciesTOnight) && routeInfoCalculator.route.TONight != 0)
                mItemFlightCurrenciesTOnight.setDescription(String.valueOf(routeInfoCalculator.route.TONight));
            if (checkEnableField(mItemFlightCurrenciesLDGDay) && routeInfoCalculator.route.LdgDay != 0)
                mItemFlightCurrenciesLDGDay.setDescription(String.valueOf(routeInfoCalculator.route.LdgDay));
            if (checkEnableField(mItemFlightCurrenciesLDGNight) && routeInfoCalculator.route.LdgNight != 0)
                mItemFlightCurrenciesLDGNight.setDescription(String.valueOf(routeInfoCalculator.route.LdgNight));
        }
        SettingConfig settingNightCalc = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_CALC);
        if (settingNightCalc == null || TextUtils.isEmpty(settingNightCalc.getData())) return;
        switch (settingNightCalc.getData()) {
            case MCCPilotLogConst.NIGHT_CALC_SS_SR:
                if (checkEnableField(mItemFlightNight) && routeInfoCalculator.route.gNightTime != 0) {
                    mItemFlightNight.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(routeInfoCalculator.route.gNightTime), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
                    mItemFlightNight.setMinutesData(routeInfoCalculator.route.gNightTime);
                }
                break;
            case MCCPilotLogConst.NIGHT_CALC_FIX_HOURS:
                if (checkEnableField(mItemFlightNight) && routeInfoCalculator.route.fNightTime != 0) {
                    mItemFlightNight.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(routeInfoCalculator.route.fNightTime), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
                    mItemFlightNight.setMinutesData(routeInfoCalculator.route.fNightTime);
                }
                break;
            case MCCPilotLogConst.NIGHT_CALC_BOTH:
                if (checkEnableField(mItemFlightNight) && routeInfoCalculator.route.gNightTime != 0) {
                    mItemFlightNight.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(routeInfoCalculator.route.gNightTime), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
                    mItemFlightNight.setMinutesData(routeInfoCalculator.route.gNightTime);
                }
                if (checkEnableField(mItemFlightUserActive4) && routeInfoCalculator.route.fNightTime != 0) {
                    mItemFlightUserActive4.setDescription(flightUtils.getTimeDisplay(TimeUtils.convertMinuteToHour(String.valueOf(routeInfoCalculator.route.fNightTime), isLogTimeDecimal), isLogTimeDecimal, true, isLogTimeDecimal));
                    mItemFlightUserActive4.setMinutesData(routeInfoCalculator.route.fNightTime);
                }
                break;
        }
    }

    public void onKeyBackPress() {
        if (isChange) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    boolean confirm = showOkCancelDialog(mMode == MODE_ADD || mMode == MODE_RETURN || mMode == MODE_NEXT ? R.string.text_flight_add : R.string.text_flight_edit, R.string.cancel_message_content);
                    if (confirm) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                KeyboardUtils.hideSoftKeyboard(mActivity);
                                finishFragment();
                            }
                        });
                    }
                }
            });
        } else {
            KeyboardUtils.hideSoftKeyboard(mActivity);
            finishFragment();
        }
    }

    private void clearState() {
        StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT1, STRING_EMPTY);
        StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT2, STRING_EMPTY);
        StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT3, STRING_EMPTY);
        StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_PILOT4, STRING_EMPTY);
        StorageUtils.writeStringToSharedPref(mActivity, StateKey.SAVED_AIRCRAFT, STRING_EMPTY);
        StorageUtils.writeBooleanToSharedPref(mActivity, StateKey.SAVED_TASK, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMode == MODE_FLIGHT_VIEW && mFlight != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initValue();
                }
            });
        }
    }


}
