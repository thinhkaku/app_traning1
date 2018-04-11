package aero.pilotlog.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Duty;
import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IItemsFlight;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.TimeUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemsFlightView;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyAddFragment extends BaseMCCFragment implements IItemsFlight {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ibMenu)
    ImageView ivMenu;
    @Bind(R.id.tv_action_bar_center)
    TextView tvActionbarCenter;
    @Bind(R.id.lineDraft)
    View lineDraft;
    @Bind(R.id.item_duty_date)
    ItemsFlightView itemDutyDate;
    @Bind(R.id.item_duty_check_in)
    ItemsFlightView itemCheckIn;
    @Bind(R.id.item_time_zone_in)
    ItemsFlightView itemTimeZoneIn;
    @Bind(R.id.item_duty_check_out)
    ItemsFlightView itemCheckOut;
    @Bind(R.id.item_time_zone_out)
    ItemsFlightView itemTimeZoneOut;
    @Bind(R.id.item_duty_duration)
    ItemsFlightView itemDuration;
    @Bind(R.id.item_type_of_event)
    ItemsFlightView itemTypeOfEvent;
    @Bind(R.id.item_description)
    ItemsFlightView itemDescription;
    @Bind(R.id.item_notes)
    ItemsFlightView itemNotes;
    @Bind(R.id.sv_duty_add)
    ScrollView scrollView;
    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    /*@Bind(R.id.ll_flight_configure)
    LinearLayout mBottomBar;*/

    private Calendar mDate = Calendar.getInstance();
    private Duty mDuty;
    private Airfield airfieldBase;
    private FlightUtils.TimeMode timeMode = FlightUtils.TimeMode.LOCAL;
    private FlightUtils.TimeMode dateMode = FlightUtils.TimeMode.LOCAL;
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManagerV5;
    private Bundle mBundle;
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE", Locale.US);
    public static final int RED_COLOR = android.R.color.holo_red_light;
    private List<ItemsFlightView> listItemFlightViews;
    private boolean isLogTimeDecimal;
    private FlightUtils flightUtils;
    private boolean isShowTopKey = false;
    private String keyboardMemoryTemp1 = "";
    private TimeZone timeZoneIn, timeZoneOut;
    private int timeZoneInCode = 0, timeZoneOutCode = 0;
    private boolean isChange;



    public DutyAddFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_duty_add;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_pilot_footer;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mDatabaseManagerV5 = DatabaseManager.getInstance(mActivity);
        mBundle = getArguments();
        initView();
    }

    private void initView() {
        ivMenu.setVisibility(View.GONE);
        tvActionbarCenter.setVisibility(View.GONE);
        lineDraft.setVisibility(View.GONE);
        initSetting();
        createListItems();
        for (int i = 0; i < listItemFlightViews.size(); i++) {
            listItemFlightViews.get(i).setIItemFlight(this);
            listItemFlightViews.get(i).setVisibility(View.VISIBLE);
        }
        itemDuration.setHint(isLogTimeDecimal ? R.string.decimal_hint : R.string.hours_hint);
        itemDuration.setViewOnlyMode();
        itemDuration.setInvisibleIconBottom();

        itemTypeOfEvent.setDescription("FDP - Flight Duty Period");
        itemTypeOfEvent.getEdtDescription().setTextColor(mActivity.getResources().getColor(android.R.color.darker_gray));
        itemTypeOfEvent.getEdtDescription().setEnabled(false);
        itemTypeOfEvent.setViewOnlyMode();
        itemTypeOfEvent.setInvisibleIconBottom();

        if (timeMode == timeMode.LOCAL || dateMode == timeMode.LOCAL) {
            itemTimeZoneIn.setVisibility(View.VISIBLE);
            itemTimeZoneIn.setIItemFlight(this);
            itemTimeZoneOut.setVisibility(View.VISIBLE);
            itemTimeZoneOut.setIItemFlight(this);
            itemCheckIn.setViewLineBorder(View.GONE);
            itemCheckOut.setViewLineBorder(View.GONE);
            itemTimeZoneIn.getEdtDescription().setFocusable(false);
            itemTimeZoneOut.getEdtDescription().setFocusable(false);
        } else {
            itemTimeZoneIn.setVisibility(View.GONE);
            itemTimeZoneOut.setVisibility(View.GONE);
            itemCheckIn.setViewLineBorder(View.VISIBLE);
            itemCheckOut.setViewLineBorder(View.VISIBLE);
        }
        initValue();
    }


    private void initValue() {
        byte[] dutyCode = null;
        if (mBundle != null) {
            dutyCode = mBundle.getByteArray(MCCPilotLogConst.DUTY_CODE);
        }
        if (dutyCode != null) {
            mDuty = mDatabaseManagerV5.getDutyByCode(dutyCode);
        }
        if (mDuty != null) { //mFlight = new Flight();
            mTvTitle.setText("Edit Duty");
            try {
                switch (timeMode) {
                    case UTC:
                        if (mDuty.getEventDateUTC() != null) {
                            mDate.setTime(DB_DATE_FORMAT.parse(mDuty.getEventDateUTC()));
                        }
                        break;
                    case LOCAL:
                        if (mDuty.getEventDateLOCAL() != null) {
                            mDate.setTime(DB_DATE_FORMAT.parse(mDuty.getEventDateLOCAL()));
                        }
                        break;
                    case BASE:
                        if (mDuty.getEventDateBASE() != null) {
                            mDate.setTime(DB_DATE_FORMAT.parse(mDuty.getEventDateBASE()));
                        }
                        break;
                }
            } catch (ParseException pex) {

            }
            itemDutyDate.setDescription(DISPLAY_DATE_FORMAT.format(mDate.getTime()));
            Calendar calToday = Calendar.getInstance();
            if (calToday.get(Calendar.YEAR) == mDate.get(Calendar.YEAR) &&
                    calToday.get(Calendar.MONTH) == mDate.get(Calendar.MONTH) &&
                    calToday.get(Calendar.DAY_OF_MONTH) == mDate.get(Calendar.DAY_OF_MONTH)) {
                itemDutyDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()), R.color.grey_footer_text);
            } else {
                itemDutyDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()), RED_COLOR);
            }
            setDataEdit();
        } else {
            mTvTitle.setText("Add Duty");
            mDuty = new Duty();
            itemDutyDate.setDescription(DISPLAY_DATE_FORMAT.format(mDate.getTime()));
            itemDutyDate.setFootNote(DAY_OF_WEEK_FORMAT.format(mDate.getTime()));
            setTimeZone(mDatabaseManagerV5.getTimeZoneByCode(airfieldBase.getTZCode()), true);
            setTimeZone(mDatabaseManagerV5.getTimeZoneByCode(airfieldBase.getTZCode()), false);
            setTextTimeZone();
        }
    }

    private void setDataEdit() {
        timeZoneInCode = mDuty.getStartTZCode();
        timeZoneOutCode = mDuty.getEndTZCode();
        timeZoneIn = getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(timeZoneInCode));
        timeZoneOut = getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(timeZoneOutCode));
        itemDescription.setDescription(mDuty.getEventDescription());
        itemNotes.setDescription(mDuty.getDutyNotes());
        itemCheckIn.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getCheckInUTC(mDuty.getEventStartUTC())), false));
        flightUtils.calcAndDisplayTimeMode(itemCheckIn, timeMode, timeZoneIn, timeZoneInCode, mDate);
        itemCheckOut.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(getCheckOutUTC(mDuty.getEventEndUTC())), false));
        flightUtils.calcAndDisplayTimeMode(itemCheckOut, timeMode, timeZoneOut, timeZoneOutCode, mDate);
        itemDuration.setDescription(TimeUtils.convertMinuteToHour(String.valueOf(mDuty.getDuration()), isLogTimeDecimal));
        itemDuration.getEdtDescription().setTextColor(mActivity.getResources().getColor(android.R.color.darker_gray));
    }

    private void setTextTimeZone() {
        if (timeZoneIn != null) {
            ZTimeZone zTimeZone = mDatabaseManagerV5.getTimeZoneByCode(timeZoneInCode);
            if (zTimeZone == null) return;
            itemTimeZoneIn.setDescription(zTimeZone.getTimeZone());
            Calendar c = Calendar.getInstance(timeZoneIn);
            String offset;
            double offsetValue = (double) Utils.getTimeOffset(timeZoneIn.getOffset(c.getTimeInMillis()) / 60000, timeZoneInCode) / 60;
            if (offsetValue < 0) {
                offset = "" + offsetValue;
            } else {
                offset = "+" + offsetValue;
            }
            if (offset.endsWith(".0")) offset = offset.replace(".0", "");
            itemTimeZoneIn.setFootNote("UTC " + offset);
        }
        if (timeZoneOut != null) {
            ZTimeZone zTimeZone = mDatabaseManagerV5.getTimeZoneByCode(timeZoneOutCode);
            if (zTimeZone == null) return;
            itemTimeZoneOut.setDescription(zTimeZone.getTimeZone());
            Calendar c = Calendar.getInstance(timeZoneOut);
            String offset;
            double offsetValue = (double) Utils.getTimeOffset(timeZoneOut.getOffset(c.getTimeInMillis()) / 60000, timeZoneOutCode) / 60;
            if (offsetValue < 0) {
                offset = "" + offsetValue;
            } else {
                offset = "+" + offsetValue;
            }
            if (offset.endsWith(".0")) offset = offset.replace(".0", "");
            itemTimeZoneOut.setFootNote("UTC " + offset);
        }
    }

    private void initSetting() {
        flightUtils = FlightUtils.getInstance(mActivity, mDatabaseManagerV5);
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
        flightUtils.timeMode = this.timeMode;
        String dMode = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_DATE_MODE).getData();
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
        flightUtils.conditionLoad = null;
        String logDecimal = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
        isLogTimeDecimal = flightUtils.isLogTimeDecimal = logDecimal.equals("1") || logDecimal.equals("2");
        String iCaoCodeBase = mDatabaseManagerV5.getSetting(MCCPilotLogConst.SETTING_CODE_HOME_BASE).getData();
        airfieldBase = mDatabaseManagerV5.getAirfieldByICAOIATA(iCaoCodeBase);
        //timeZoneIn = timeZoneOut = TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(airfieldBase.getTZCode()).getTimeZone());
        //timeZoneInCode = timeZoneOutCode = airfieldBase.getTZCode();
    }

    private void createListItems() {
        listItemFlightViews = new ArrayList<>();
        listItemFlightViews.add(itemDutyDate);
        listItemFlightViews.add(itemCheckIn);
        listItemFlightViews.add(itemCheckOut);
        listItemFlightViews.add(itemDuration);
        listItemFlightViews.add(itemTypeOfEvent);
        listItemFlightViews.add(itemDescription);
        listItemFlightViews.add(itemNotes);


    }

    private void showTopKey() {
        if (!isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (!isTablet()) {
                mTopKey.setVisibility(View.VISIBLE);
                //mBottomBar.setVisibility(View.GONE);

            } else {
                mTopKey.setVisibility(View.VISIBLE);
                //mBottomBar.setVisibility(View.GONE);
              /*  Fragment fragment = getLeftFragment();
                if (fragment instanceof LogbooksListFragment) {
                    ((LogbooksListFragment) fragment).hideBottomBar();
                }*/
            }
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    int scrollY = scrollView.getScrollY();
                    if (scrollY > 0) {
                        View v = mActivity.getCurrentFocus();
                        if (v instanceof MccEditText) {
                            int edtHeight = v.getHeight();
                            scrollView.setScrollY(scrollY + edtHeight);
                        }
                    }
                    scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);

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
                // mBottomBar.setVisibility(View.VISIBLE);
            } else {
                mTopKey.setVisibility(View.GONE);
                //  mBottomBar.setVisibility(View.VISIBLE);
              /*  Fragment fragment = getLeftFragment();
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
                  /*  if ((listItemFlightViews.get(currentFocus).getId() == R.id.item_flight_scheduled_out ||
                            listItemFlightViews.get(currentFocus).getId() == R.id.item_flight_scheduled_in) && isShowSchedule) {
                        if (listItemFlightViews.get(currentFocus).getVisibility() == View.GONE) {
                            currentFocus++;
                            edtNext = listItemFlightViews.get(currentFocus).getEdtDescription();
                        }
                    }*/
                    if (edtNext.getVisibility() == View.VISIBLE && edtNext.isEnabled()) {
                        edtNext.requestFocus();
                        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                            @Override
                            public void onScrollChanged() {
                                int scrollY = scrollView.getScrollY();
                                if (scrollY > 0) {
                                    int edtHeight = edt.getHeight();
                                    scrollView.setScrollY(scrollY + edtHeight);
                                }
                                scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);

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
        if(itemsFlightView.getId()!=R.id.item_time_zone_in && itemsFlightView.getId()!=R.id.item_time_zone_out){
            keyboardMemoryTemp1 = ((MccEditText) view).getText().toString();
            currentFocus = listItemFlightViews.indexOf(itemsFlightView);
       /* if (itemsFlightView.getId() == R.id.item_flight_flightno) {
            SettingConfig settingConfig = mDatabaseManagerV5.getSetting(7);
            if (settingConfig != null && !TextUtils.isEmpty(settingConfig.getData()) && !TextUtils.isEmpty(itemsFlightView.getDescription())) {
                if (settingConfig.getData().equals(itemsFlightView.getDescription())) {//flight number prefix
                    itemsFlightView.getEdtDescription().setSelection(itemsFlightView.getDescription().length());
                }
            }
        }*/
            showTopKey();
            View v2 = mActivity.getCurrentFocus();
            if (v2 == null || !(v2 instanceof MccEditText)) {
                hideTopKey();
            }
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
    public void onClickIconTop(ItemsFlightView itemsFlightView) {

    }

    @Override
    public void onClickIconBottom(ItemsFlightView itemsFlightView) {
        Bundle bundle = new Bundle();
        bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
        bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_DUTY_ADD);
        switch (itemsFlightView.getId()) {
            case R.id.item_duty_date:
                flightUtils.displayDateDialog(itemDutyDate.getDescription(), itemDutyDate, mDate);
                isChange = true;
                break;
            case R.id.item_time_zone_in:
                isChange = true;
                bundle.putBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL, true);
                this.replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, TimezoneFragment.class, bundle, true);
                break;
            case R.id.item_time_zone_out:
                isChange = true;
                bundle.putBoolean(MCCPilotLogConst.SELECT_LIST_DEPARTURE_OR_ARRIVAL, false);
                this.replaceFragment(isTablet()? R.id.rightContainerFragment:R.id.fragmentMain, TimezoneFragment.class, bundle, true);
                break;
        }
    }

    @Override
    public void onFinishInput(ItemsFlightView itemsFlightView, boolean isChangeMinuteTotal) {
        itemsFlightView.setSaveState(itemsFlightView.getDescription().trim());
        String value = itemsFlightView.getDescription().trim();
        if (!isChange) isChange = true;
        if (itemsFlightView.getFlightType() == ItemsFlightView.ITEM_FLIGHT_HOURS) {
            switch (itemsFlightView.getId()) {
                case R.id.item_duty_check_in:
                    finishCheckInOut(value, itemsFlightView, timeZoneIn, timeZoneInCode);
                    break;
                case R.id.item_duty_check_out:
                    finishCheckInOut(value, itemsFlightView, timeZoneOut, timeZoneOutCode);
                    break;
            }
        }
    }

    private void finishCheckInOut(String value, ItemsFlightView itemsFlightView, TimeZone timeZone, int timeZoneCode) {
        String timeDisplay = flightUtils.getTimeDisplay(value, false, false, isLogTimeDecimal);
        itemsFlightView.setDescription(timeDisplay);
        flightUtils.calcAndDisplayTimeModeForDuty(itemsFlightView, timeMode, timeZone, timeZoneCode, mDate);
        flightUtils.calcDuration(itemCheckIn, itemCheckOut, itemDuration, isLogTimeDecimal, timeZoneIn, timeZoneOut, timeZoneInCode, timeZoneOutCode, mDate);
    }

    public void setTimeZone(final ZTimeZone zTimeZone, boolean checkIn) {
        if (checkIn) {
            timeZoneIn = getTimeZone(zTimeZone);
            timeZoneInCode = zTimeZone.getTZCode();
            if (!TextUtils.isEmpty(itemCheckIn.getDescription())) {
                finishCheckInOut(itemCheckIn.getDescription(), itemCheckIn, timeZoneIn, timeZoneInCode);
            }

        } else {
            timeZoneOut = getTimeZone(zTimeZone);
            timeZoneOutCode = zTimeZone.getTZCode();
            if (!TextUtils.isEmpty(itemCheckOut.getDescription())) {
                finishCheckInOut(itemCheckOut.getDescription(), itemCheckOut, timeZoneOut, timeZoneOutCode);
            }
        }
    }

    @Override
    public void onShowInfoPage(int flightItemId) {

    }

    @Nullable
    @OnClick({R.id.tv_action_bar_left, R.id.tv_action_bar_right, R.id.btn_cancel, R.id.btn_next, R.id.btn_done})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tv_action_bar_left:
                onKeyBackPress();
                break;
            case R.id.tv_action_bar_right:
                saveDuty();
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
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (listItemFlightViews != null) {
            for (int i = 0; i < listItemFlightViews.size(); i++) {
                listItemFlightViews.get(i).restoreState();
            }
            setTextTimeZone();
        }
    }

    private TimeZone getTimeZone(ZTimeZone zTimeZone) {
        return TimeZone.getTimeZone(mDatabaseManagerV5.getTimeZoneByCode(zTimeZone.getTZCode()).getTimeZone());
    }

    private void saveDuty() {
        if (TextUtils.isEmpty(itemCheckIn.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_duty_check_in, R.string.invalid_duty_check_in_content).show();
            return;
        }
        if (TextUtils.isEmpty(itemCheckOut.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_duty_check_out, R.string.invalid_duty_check_out_content).show();
            return;
        }
        if (TextUtils.isEmpty(itemDescription.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_duty_description, R.string.invalid_duty_description_content).show();
            return;
        }
     /*   if (TextUtils.isEmpty(itemNotes.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.invalid_duty_notes, R.string.invalid_duty_notes_content).show();
            return;
        }*/

        Duty duty = new Duty();
        byte[] dutyCode;
        if (mDuty.getDutyCode() != null) {
            dutyCode = mDuty.getDutyCode();
        } else {
            dutyCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        }
        duty.setDutyCode(dutyCode);
        duty.setDutyType(7);//Flight Duty Period
        duty.setDeadhead(false);
        duty.setEventDescription(itemDescription.getDescription());
        Date selectedDate = new Date(itemDutyDate.getDescription());
        if (selectedDate != null) {
            String timeCheckInUtc = flightUtils.convertTimeToUTC(itemCheckIn.getDescription(), timeMode, timeZoneIn, timeZoneInCode, mDate);
            if (!TextUtils.isEmpty(timeCheckInUtc)) {
                Calendar calendarUTC = getDate(selectedDate, TimeUtils.convertHourToMin(timeCheckInUtc), FlightUtils.TimeMode.UTC);
                duty.setEventDateUTC(flightUtils.DB_DATE_FORMAT.format(calendarUTC.getTime()));
                Calendar calendarLocal = getDate(selectedDate, TimeUtils.convertHourToMin(timeCheckInUtc), FlightUtils.TimeMode.LOCAL);
                duty.setEventDateLOCAL(flightUtils.DB_DATE_FORMAT.format(calendarLocal.getTime()));
                Calendar calendarBase = getDate(selectedDate, TimeUtils.convertHourToMin(timeCheckInUtc), FlightUtils.TimeMode.BASE);
                duty.setEventDateBASE(flightUtils.DB_DATE_FORMAT.format(calendarBase.getTime()));
            } else {
                duty.setEventDateUTC(flightUtils.DB_DATE_FORMAT.format(selectedDate));
                duty.setEventDateLOCAL(flightUtils.DB_DATE_FORMAT.format(selectedDate));
                duty.setEventDateBASE(flightUtils.DB_DATE_FORMAT.format(selectedDate));
            }
        }
        duty.setEventStartUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(itemCheckIn.getDescription(), timeMode, timeZoneIn, timeZoneInCode, mDate)));
        duty.setEventEndUTC(TimeUtils.convertHourToMin(flightUtils.convertTimeToUTC(itemCheckOut.getDescription(), timeMode, timeZoneOut, timeZoneOutCode, mDate)));
        duty.setBaseOffset(flightUtils.getBaseOffset(mDate));
        duty.setStartOffset((int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate));
        duty.setEndOffset((int) flightUtils.getDepArrOffset(timeZoneOut, timeZoneOutCode, mDate));
        duty.setStartTZCode(timeZoneInCode);
        duty.setEndTZCode(timeZoneOutCode);
        duty.setDuration(TimeUtils.convertHourToMin(itemDuration.getDescription()));
        duty.setDutyNotes(itemNotes.getDescription());
        duty.setRecord_Upload(true);
        duty.setRecord_Modified(DateTimeUtils.getCurrentUTCUnixTimeStamp());
        mDatabaseManagerV5.insertDuty(duty);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final DutyFragment dutyFragment = (DutyFragment) getFragment(DutyFragment.class);
                if (dutyFragment != null) {
                    dutyFragment.refreshListDuty();
                }
                finishFragment();

            }
        });
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
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                        break;
                    case BASE:
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                        break;
                }
            case LOCAL:
                switch (dateModeResult) {
                    case UTC:
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        }
                        break;
                    case LOCAL:
                        return calendar;
                    case BASE:
                        calendar = getDate(currentDate, minDepTimeUTC, FlightUtils.TimeMode.UTC);
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, +1);
                        }
                }
                break;
            case BASE:
                switch (dateModeResult) {
                    case UTC:
                        if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        } else if (minDepTimeUTC /*+ (int) flightUtils.getDepArrOffset(timeZoneDeparture)*/ + flightUtils.getBaseOffset(mDate) > 1440) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        }
                        break;
                    case LOCAL:
                        calendar = getDate(currentDate, minDepTimeUTC, FlightUtils.TimeMode.UTC);
                        if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) < 0) {
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                        } else if (minDepTimeUTC + (int) flightUtils.getDepArrOffset(timeZoneIn, timeZoneInCode, mDate) > 1440) {
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

    public void onKeyBackPress() {
        if (isChange&&isVisible()) {
            MccDialog.getOkCancelAlertDialog(mActivity, mDuty.getDutyCode() == null ? R.string.duty_add_title : R.string.duty_edit_title
                    , R.string.cancel_message_content
                    , new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                KeyboardUtils.hideSoftKeyboard(mActivity);
                                finishFragment();
                            }
                        }
                    }).show();
        } else {
            KeyboardUtils.hideSoftKeyboard(mActivity);
            finishFragment();
        }
    }

    private int getCheckInUTC(Integer minutes) {
        if (minutes == null || minutes == 0) return 0;
        Integer depOffset = mDuty.getStartOffset();
        Integer baseOffset = mDuty.getBaseOffset();
        int minutesReturn = minutes;
        if (timeMode == FlightUtils.TimeMode.BASE) {
            minutesReturn = (minutes + baseOffset + 1440) % 1440;
        } else if (timeMode == FlightUtils.TimeMode.LOCAL) {
            minutesReturn = (minutes + depOffset + 1440) % 1440;
        }
        return minutesReturn;
    }

    private int getCheckOutUTC(Integer minutes) {
        if (minutes == null || minutes == 0) return 0;
        Integer arrOffset = mDuty.getEndOffset();
        Integer baseOffset = mDuty.getBaseOffset();
        int minutesReturn = minutes;
        if (timeMode == FlightUtils.TimeMode.BASE) {
            minutesReturn = (minutes + baseOffset + 1440) % 1440;
        } else if (timeMode == FlightUtils.TimeMode.LOCAL) {
            minutesReturn = (minutes + arrOffset + 1440) % 1440;
        }
        return minutesReturn;
    }
}
