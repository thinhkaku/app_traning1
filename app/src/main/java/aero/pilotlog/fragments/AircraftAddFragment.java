package aero.pilotlog.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.AircraftInfoConst;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZFNPT;
import aero.pilotlog.databases.entities.ZLaunch;
import aero.pilotlog.databases.entities.ZOperation;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IEditTagClear;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.utilities.Utils;
import aero.pilotlog.widgets.ItemInputTagsWithIcon;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.ItemSwitch;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


/**
 * Created by phuc.dd on 02/14/2017.
 */
public class AircraftAddFragment extends BaseMCCFragment implements IEditTagClear, ItemInputTextWithIcon.IItemInputText, ItemSwitch.IItemSwitch {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ibMenu)
    ImageView ivMenu;
    @Bind(R.id.tv_action_bar_center)
    TextView tvActionbarCenter;
    @Bind(R.id.lineDraft)
    View lineDraft;
    @Bind(R.id.sv_aircraft_add)
    ScrollView svAircraft;

    @Bind(R.id.item_aircraft_aerobatic)
    ItemSwitch itemSwitchAerobatic;
    @Bind(R.id.item_aircraft_complex)
    ItemSwitch itemSwitchComplex;
    @Bind(R.id.item_aircraft_high_performance)
    ItemSwitch itemSwitchHighPerformance;
    @Bind(R.id.item_aircraft_tm_glider)
    ItemSwitch itemSwitchTmGlider;
    @Bind(R.id.item_aircraft_tailwheel)
    ItemSwitch itemSwitchTailWheel;
    @Bind(R.id.item_aircraft_more_than_57kg)
    ItemSwitch itemSwitchMoreThan57kg;

    @Bind(R.id.item_aircraft_type)
    ItemInputTextWithIcon itemAircraftType;
    @Bind(R.id.item_aircraft_class)
    ItemInputTextWithIcon itemAircraftClass;
    @Bind(R.id.item_aircraft_subclass)
    ItemInputTextWithIcon itemAircraftSubClass;
    @Bind(R.id.item_aircraft_category)
    ItemInputTextWithIcon itemAircraftCategory;
    @Bind(R.id.item_aircraft_power)
    ItemInputTextWithIcon itemAircraftPower;
    @Bind(R.id.item_aircraft_device)
    ItemInputTextWithIcon itemAircraftDevice;

    @Bind(R.id.item_aircraft_auto_load_function_time)
    ItemInputTagsWithIcon itemAircraftFunctionTime;
    @Bind(R.id.item_aircraft_auto_load_condition_time)
    ItemInputTagsWithIcon itemAircraftConditionTime;
    @Bind(R.id.item_aircraft_default_operation)
    ItemInputTagsWithIcon itemAircraftDefaultOperation;
    @Bind(R.id.item_aircraft_default_approach)
    ItemInputTagsWithIcon itemAircraftDefaultApproach;
    @Bind(R.id.item_aircraft_default_launch)
    ItemInputTagsWithIcon itemAircraftDefaultLaunch;
    @Bind(R.id.item_aircraft_logbook)
    ItemSwitch itemAircraftLogbook;

    @Bind(R.id.edt_item_aircraft_company)
    MccEditText edtItemAircraftCompany;
    @Bind(R.id.edt_item_aircraft_registration)
    MccEditText edtItemAircraftRegistration;
    @Bind(R.id.edt_item_aircraft_fin)
    MccEditText edtItemAircraftFin;
    @Bind(R.id.edt_item_aircraft_model)
    MccEditText edtItemAircraftModel;
    @Bind(R.id.edt_item_aircraft_variant)
    MccEditText edtItemAircraftVariant;
    @Bind(R.id.edt_item_aircraft_manufacture)
    MccEditText edtItemAircraftManufacture;
    @Bind(R.id.edt_item_aircraft_type_rating)
    MccEditText edtItemAircraftRating;
    @Bind(R.id.edt_item_aircraft_pass_seats)
    MccEditText edtItemAircraftPassSeats;
    @Bind(R.id.ln_pass_seats)
    LinearLayout lnPassSeats;
    private DatabaseManager databaseManager;
    private static ZApproach mApproach;
    private static ZLaunch mLaunch;
    private static ZOperation mOperation;
    String currentAircraftClass = "";
    private ZFNPT mZfnpt;
    private Aircraft aircraft = null;
    @Bind(R.id.top_key)
    LinearLayout mTopKey;
    boolean isShowTopKey = false;
    private HashMap<MccEditText, Integer> editTextTagMap;
    private boolean isChanged;

    public AircraftAddFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_add_edit_flight_new;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_aircraft_add;
    }

    @Override
    protected int getFooterResId() {
        return R.layout.layout_add_pilot_footer;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        databaseManager = DatabaseManager.getInstance(mActivity);
        itemAircraftDefaultOperation.setIEditTagClear(this);
        itemAircraftDefaultLaunch.setIEditTagClear(this);
        itemAircraftDefaultApproach.setIEditTagClear(this);
        itemAircraftFunctionTime.setIEditTagClear(this);
        itemAircraftConditionTime.setIEditTagClear(this);
        itemAircraftType.setIItemInputText(this);
        itemAircraftClass.setIItemInputText(this);
        initView();
        setOnClickNavigationItem(itemAircraftDefaultOperation);
        setOnClickNavigationItem(itemAircraftDefaultLaunch);
        setOnClickNavigationItem(itemAircraftDefaultApproach);

        //itemAircraftDevice.setOnClickListener(null);
        itemAircraftDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentSwitchValue();
                Bundle bundle = new Bundle();
                bundle.putBoolean(MCCPilotLogConst.ZNPT_TYPE, itemAircraftDevice.isDrone());
                replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, FNPTFragment.class, bundle, true);
            }
        });

        edtItemAircraftCompany.addTextChangedListener(textWatcher);
        edtItemAircraftRegistration.addTextChangedListener(textWatcher);
        edtItemAircraftFin.addTextChangedListener(textWatcher);
        edtItemAircraftModel.addTextChangedListener(textWatcher);
        edtItemAircraftVariant.addTextChangedListener(textWatcher);
        edtItemAircraftManufacture.addTextChangedListener(textWatcher);
        edtItemAircraftRating.addTextChangedListener(textWatcher);
        edtItemAircraftPassSeats.addTextChangedListener(textWatcher);
    }

    private void setOnClickNavigationItem(final ItemInputTagsWithIcon item) {
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentSwitchValue();
                if (item == itemAircraftDefaultOperation)
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, OperationFragment.class, true);
                else if (item == itemAircraftDefaultLaunch)
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, GliderLaunchFragment.class, true);
                else if (item == itemAircraftDefaultApproach)
                    replaceFragment(isTablet() ? R.id.rightContainerFragment : R.id.fragmentMain, ApproachFragment.class, true);
            }
        });
    }


    private void initView() {
        editTextTagMap = initEditTextTag();
        itemAircraftDevice.setVisibleFootNote(View.GONE);

        Bundle bundle = getArguments();
        byte[] mAircraftCode = null;
        if (bundle != null) {
            mAircraftCode = bundle.getByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY);
        }
        if (mAircraftCode != null) {
            aircraft = databaseManager.getAircraftByCode(mAircraftCode);
        }
        if (aircraft == null)
            mTvTitle.setText("Add Aircraft");
        else
            mTvTitle.setText("Edit Aircraft");
        ivMenu.setVisibility(View.GONE);
        tvActionbarCenter.setVisibility(View.GONE);
        lineDraft.setVisibility(View.GONE);
        itemSwitchAerobatic.setVisibleLine(View.GONE);
        itemSwitchComplex.setVisibleLine(View.GONE);
        itemSwitchHighPerformance.setVisibleLine(View.GONE);
        itemSwitchTmGlider.setVisibleLine(View.GONE);
        itemSwitchTailWheel.setVisibleLine(View.GONE);
        setEditTextFilterAirCraft(edtItemAircraftRegistration);
        createType(aircraft);
        createClass(aircraft);
        createSubClass(aircraft);
        createCategory(aircraft);
        createPower(aircraft);
        createFNPT(aircraft);
        createTextFields(aircraft);
        createSwitchFields(aircraft);
        createCompany(aircraft);
        createAutoLoadFunctionTime(aircraft);
        createAutoLoadConditionTime(aircraft);
        createOperation(aircraft);
        createApproach(aircraft);
        createLaunch(aircraft);
        createRun2Logbook(aircraft);
        if (aircraft != null) {
            if (aircraft.getClassZ() == 2) {
                itemAircraftDefaultLaunch.setVisibility(View.VISIBLE);
            } else if (aircraft.getClassZ() == 4 || aircraft.getClassZ() == 5) {
                itemAircraftDefaultApproach.setVisibility(View.VISIBLE);
            }
            if (aircraft.getDeviceCode() == 2) {
                setLayoutWhenAircraftTypeChange(AircraftType.SIMULATOR);
            } else if (aircraft.getDeviceCode() == 3) {
                setLayoutWhenAircraftTypeChange(AircraftType.DRONE);
            }
        }
    }

    private void createType(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Aircraft");
        charSequences.add("Simulator");
        charSequences.add("Drone");
        itemAircraftType.setIcon(R.drawable.ic_arrow);
        if (aircraft != null)
            itemAircraftType.setListStringDialog(charSequences, aircraft.getDeviceCode() - 1);
        else
            itemAircraftType.setListStringDialog(charSequences, -1);
    }

    private void createClass(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Microlight");
        charSequences.add("Glider");
        charSequences.add("Lighter-than-Air");
        charSequences.add("Rotorcraft");
        charSequences.add("Aeroplane");
        itemAircraftClass.setIcon(R.drawable.ic_arrow);
        if (aircraft != null)
            itemAircraftClass.setListStringDialog(charSequences, aircraft.getClassZ() - 1);
        else
            itemAircraftClass.setListStringDialog(charSequences, -1);
    }

    private void createSubClass(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Land");
        charSequences.add("Sea");
        itemAircraftSubClass.setIcon(R.drawable.ic_arrow);
        if (aircraft != null)
            itemAircraftSubClass.setListStringDialog(charSequences, aircraft.getSea() ? 1 : 0);
        else
            itemAircraftSubClass.setListStringDialog(charSequences, -1);
    }

    private void createCategory(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Single Pilot");
        charSequences.add("Multi Pilot");
        itemAircraftCategory.setIcon(R.drawable.ic_arrow);
        if (aircraft != null)
            itemAircraftCategory.setListStringDialog(charSequences, aircraft.getCategory() - 1);
        else
            itemAircraftCategory.setListStringDialog(charSequences, -1);
    }

    private void createPower(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Unpowered");
        charSequences.add("SE - Piston");
        charSequences.add("SE - Turboprop-shaft");
        charSequences.add("SE - Turbojet-fan");
        charSequences.add("ME - Piston");
        charSequences.add("ME - Turboprop-shaft");
        charSequences.add("ME - Turbojet-fan");
        itemAircraftPower.setIcon(R.drawable.ic_arrow);
        if (aircraft != null)
            itemAircraftPower.setListStringDialog(charSequences, aircraft.getPower());
        else
            itemAircraftPower.setListStringDialog(charSequences, -1);
    }

    private void createCompany(Aircraft aircraft) {
        if (aircraft != null && !TextUtils.isEmpty(aircraft.getCompany())) {
            edtItemAircraftCompany.setText(aircraft.getCompany());
        } else {
            SettingConfig settingConfig = databaseManager.getSetting("AirlineOperator");
            if (settingConfig != null) {
                String company = settingConfig.getData();
                if (company != null)
                    edtItemAircraftCompany.setText(company);
            }
        }

    }

    private void createAutoLoadFunctionTime(Aircraft aircraft) {
        ArrayList<CharSequence> charSequences = getCharSequencesFunctionTime();
        itemAircraftFunctionTime.setIcon(R.drawable.ic_arrow);
        if (aircraft != null) {
            if (aircraft.getDefaultLog() > 0)
                itemAircraftFunctionTime.getTagsEditText().setTags(charSequences.get(aircraft.getDefaultLog() - 1));
        }
        itemAircraftFunctionTime.setListStringDialog(charSequences, false);
    }

    SettingConfig settingConfigUserTime1;
    SettingConfig settingConfigUserTime2;
    SettingConfig settingConfigUserTime3;
    SettingConfig settingConfigUserTime4;

    private void createAutoLoadConditionTime(Aircraft aircraft) {
        if (aircraft != null) {
            String conditionText = getAircraftLogConditionString(aircraft);
            if (!TextUtils.isEmpty(conditionText)) {
                String[] conditionArr = conditionText.split(MCCPilotLogConst.SPLIT_KEY);
                itemAircraftConditionTime.getTagsEditText().setTags(conditionArr);
            }
        }
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add(AircraftInfoConst.XC_STRING);
        charSequences.add(AircraftInfoConst.RELIEF_STRING);
        charSequences.add(AircraftInfoConst.IFR_STRING);
        charSequences.add(AircraftInfoConst.ACT_INSTR_STRING);
        charSequences.add(AircraftInfoConst.SIM_INSTR_STRING);

        settingConfigUserTime1 = databaseManager.getSetting("Time_User1");
        settingConfigUserTime2 = databaseManager.getSetting("Time_User2");
        settingConfigUserTime3 = databaseManager.getSetting("Time_User3");
        settingConfigUserTime4 = databaseManager.getSetting("Time_User4");

        if (settingConfigUserTime1 != null && Integer.parseInt(settingConfigUserTime1.getData()) == 1) {
            SettingConfig settingConfigCaptionUserTime1 = databaseManager.getSetting("user_Caption1");
            if (settingConfigCaptionUserTime1 != null && !TextUtils.isEmpty(settingConfigCaptionUserTime1.getData())) {
                charSequences.add(settingConfigCaptionUserTime1.getData());
            }
        }
        if (settingConfigUserTime2 != null && Integer.parseInt(settingConfigUserTime2.getData()) == 1) {
            SettingConfig settingConfigCaptionUserTime2 = databaseManager.getSetting("user_Caption2");
            if (settingConfigCaptionUserTime2 != null && !TextUtils.isEmpty(settingConfigCaptionUserTime2.getData())) {
                charSequences.add(settingConfigCaptionUserTime2.getData());
            }
        }
        if (settingConfigUserTime3 != null && Integer.parseInt(settingConfigUserTime3.getData()) == 1) {
            SettingConfig settingConfigCaptionUserTime3 = databaseManager.getSetting("user_Caption3");
            if (settingConfigCaptionUserTime3 != null && !TextUtils.isEmpty(settingConfigCaptionUserTime3.getData())) {
                charSequences.add(settingConfigCaptionUserTime3.getData());
            }
        }
        if (settingConfigUserTime4 != null && Integer.parseInt(settingConfigUserTime4.getData()) == 1) {
            SettingConfig settingConfigCaptionUserTime4 = databaseManager.getSetting("user_Caption4");
            if (settingConfigCaptionUserTime4 != null && !TextUtils.isEmpty(settingConfigCaptionUserTime4.getData())) {
                charSequences.add(settingConfigCaptionUserTime4.getData());
            }
        }
        itemAircraftConditionTime.setIcon(R.drawable.ic_arrow);
        itemAircraftConditionTime.setListStringDialog(charSequences, true);
    }

    private void createApproach(Aircraft aircraft) {
        if (aircraft != null) {
            ZApproach zApproach = databaseManager.getZApproach(aircraft.getDefaultApp());
            if (zApproach != null) {
                mApproach = zApproach;
                itemAircraftDefaultApproach.setTagsEditText(zApproach.getAPShort());
                itemAircraftDefaultApproach.setFootNote(zApproach.getAPLong());
            }
        }
        itemAircraftDefaultApproach.setIcon(R.drawable.ic_arrow);
        itemAircraftDefaultApproach.setLayoutResId(R.id.fragmentMain);
        itemAircraftDefaultApproach.setParentFragment(this);
        itemAircraftDefaultApproach.setNavigationToFragment(ApproachFragment.class);
    }

    public void setSelectedApproach(ZApproach pApproach) {
        this.mApproach = pApproach;
        scrollToBottom(itemAircraftDefaultApproach);
    }

    private void createFNPT(Aircraft aircraft) {
        mZfnpt = null;
        if (aircraft != null) {
            ZFNPT zfnpt = databaseManager.getFNPTByCode(aircraft.getFNPT());
            if (zfnpt != null) {
                mZfnpt = zfnpt;
                itemAircraftDevice.setDescription(zfnpt.getFnptShort());
                itemAircraftDevice.setFootNote(zfnpt.getFnptLong());
            }
        }
        itemAircraftDevice.setIcon(R.drawable.ic_arrow);
    }

    public void setSelectedFNPT(ZFNPT pFnpt) {
        this.mZfnpt = pFnpt;
        if (pFnpt != null)
            itemAircraftDevice.setVisibleFootNote(View.VISIBLE);

    }

    private void createLaunch(Aircraft aircraft) {
        if (aircraft != null) {
            ZLaunch zLaunch = databaseManager.getGliderLaunch(aircraft.getDefaultLaunch());
            if (zLaunch != null) {
                mLaunch = zLaunch;
                itemAircraftDefaultLaunch.setTagsEditText(zLaunch.getLaunchShort());
                itemAircraftDefaultLaunch.setFootNote(zLaunch.getLaunchLong());
            }
        }
        itemAircraftDefaultLaunch.setIcon(R.drawable.ic_arrow);
        itemAircraftDefaultLaunch.setLayoutResId(R.id.fragmentMain);
        itemAircraftDefaultLaunch.setParentFragment(this);
        itemAircraftDefaultLaunch.setNavigationToFragment(GliderLaunchFragment.class);
    }

    public void setSelectedLaunch(ZLaunch pLaunch) {
        this.mLaunch = pLaunch;
        scrollToBottom(itemAircraftDefaultLaunch);
    }

    private void createOperation(Aircraft aircraft) {
        if (aircraft != null) {
            ZOperation zOperation = databaseManager.getZOperation(aircraft.getDefaultOps());
            if (zOperation != null) {
                mOperation = zOperation;
                itemAircraftDefaultOperation.setTagsEditText(zOperation.getOpsShort());
                itemAircraftDefaultOperation.setFootNote(zOperation.getOpsLong());
            }
        }
        itemAircraftDefaultOperation.setIcon(R.drawable.ic_arrow);
        itemAircraftDefaultOperation.setLayoutResId(R.id.fragmentMain);
        itemAircraftDefaultOperation.setParentFragment(this);
        itemAircraftDefaultOperation.setNavigationToFragment(OperationFragment.class);
    }

    public void setSelectedOperation(ZOperation pOperation) {
        this.mOperation = pOperation;
        if (mApproach == null && mLaunch == null) {
            scrollToBottom(itemAircraftDefaultOperation);
        }
    }

    private void createRun2Logbook(Aircraft aircraft) {
        SettingConfig settingConfigRun2 = databaseManager.getSetting("Running2");
        if (settingConfigRun2 != null) {
            String data = settingConfigRun2.getData();
            if (!TextUtils.isEmpty(data) && Integer.parseInt(data) == 1) {
                if (aircraft != null) {
                    itemAircraftLogbook.setChecked(aircraft.getRun2());
                } else {
                    itemAircraftLogbook.setChecked(isRun2);
                }
                itemAircraftLogbook.setVisibility(View.VISIBLE);
                itemAircraftLogbook.setVisibleLine(View.GONE);
            } else {
                itemAircraftLogbook.setVisibility(View.GONE);//test
            }
        }
    }

    private void createTextFields(Aircraft aircraft) {
        if (aircraft != null) {
            if (!TextUtils.isEmpty(aircraft.getReference())) {
                edtItemAircraftRegistration.setText(aircraft.getReference());
            }
            if (!TextUtils.isEmpty(aircraft.getFin())) {
                edtItemAircraftFin.setText(aircraft.getFin());
            }
            if (!TextUtils.isEmpty(aircraft.getModel())) {
                edtItemAircraftModel.setText(aircraft.getModel());
            }
            if (!TextUtils.isEmpty(aircraft.getSubModel())) {
                edtItemAircraftVariant.setText(aircraft.getSubModel());
            }
            if (!TextUtils.isEmpty(aircraft.getMake())) {
                edtItemAircraftManufacture.setText(aircraft.getMake());
            }
            if (!TextUtils.isEmpty(aircraft.getRating())) {
                edtItemAircraftRating.setText(aircraft.getRating());
            }
            edtItemAircraftPassSeats.setText(aircraft.getSeats() == 0 ? "" : aircraft.getSeats().toString());
        }
    }

    boolean isAerobatic;
    boolean isComplex;
    boolean isHighPerformance;
    boolean isTMG;
    boolean isTailsWheel;
    boolean isMore;
    boolean isRun2;

    private void setCurrentSwitchValue() {
        isAerobatic = itemSwitchAerobatic.getChecked();
        isComplex = itemSwitchComplex.getChecked();
        isHighPerformance = itemSwitchHighPerformance.getChecked();
        isTMG = itemSwitchTmGlider.getChecked();
        isTailsWheel = itemSwitchTailWheel.getChecked();
        isMore = itemSwitchMoreThan57kg.getChecked();
        isRun2 = itemAircraftLogbook.getChecked();
    }

   /* @Override
    public void onPause() {
        setCurrentSwitchValue();
        super.onPause();
    }*/

    private void createSwitchFields(Aircraft aircraft) {
        if (aircraft != null) {
            itemSwitchAerobatic.setChecked(aircraft.getAerobatic());
            isAerobatic = aircraft.getAerobatic();
            itemSwitchComplex.setChecked(aircraft.getComplex());
            isComplex = aircraft.getComplex();
            itemSwitchHighPerformance.setChecked(aircraft.getHighPerf());
            isHighPerformance = aircraft.getHighPerf();
            itemSwitchTmGlider.setChecked(aircraft.getTMG());
            isTMG = aircraft.getTMG();
            itemSwitchTailWheel.setChecked(aircraft.getTailwheel());
            isTailsWheel = aircraft.getTailwheel();
            itemSwitchMoreThan57kg.setChecked(aircraft.getKg5700());
            isMore = aircraft.getKg5700();
        } else {
            itemSwitchAerobatic.setChecked(isAerobatic);
            itemSwitchComplex.setChecked(isComplex);
            itemSwitchHighPerformance.setChecked(isHighPerformance);
            itemSwitchTmGlider.setChecked(isTMG);
            itemSwitchTailWheel.setChecked(isTailsWheel);
            itemSwitchMoreThan57kg.setChecked(isMore);
        }
    }

    @Nullable
    @OnClick({R.id.tvFlightTitle, R.id.tv_action_bar_left, R.id.tv_action_bar_right, R.id.btn_cancel, R.id.btn_next, R.id.btn_done,
    })
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.tvFlightTitle:
                break;
            case R.id.tv_action_bar_left:
                onKeyBackPress();
                break;
            case R.id.tv_action_bar_right:
                boolean isSaved = saveAircraft();
                if (isSaved) {
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        String selectListType = bundle.getString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_AIRCRAFT_INFO);
                        if (!TextUtils.isEmpty(selectListType) && selectListType.equalsIgnoreCase(MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_ADD)) {
                            final FlightAddsFragment flightAddsFragment = (FlightAddsFragment) getFragment(FlightAddsFragment.class);
                            if (flightAddsFragment != null) {
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        flightAddsFragment.setAircraft(aircraft);
                                    }
                                });
                                KeyboardUtils.hideSoftKeyboard(mActivity);
                                finishFragment();
                                finishFragment();
                                return;
                            }
                        }
                    }
                    KeyboardUtils.hideSoftKeyboard(mActivity);
                    finishFragment();
                }
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
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mLaunch != null) {
            itemAircraftDefaultLaunch.setTagsEditText(mLaunch.getLaunchShort());
            itemAircraftDefaultLaunch.setFootNote(mLaunch.getLaunchLong());

        }
        if (mApproach != null) {
            itemAircraftDefaultApproach.setTagsEditText(mApproach.getAPShort());
            itemAircraftDefaultApproach.setFootNote(mApproach.getAPLong());

        }

        if (mOperation != null) {
            itemAircraftDefaultOperation.setTagsEditText(mOperation.getOpsShort());
            itemAircraftDefaultOperation.setFootNote(mOperation.getOpsLong());

        }

        if (mZfnpt != null) {
            itemAircraftDevice.setDescription(mZfnpt.getFnptShort());
            itemAircraftDevice.setFootNote(mZfnpt.getFnptLong());
        }
        if (itemAircraftFunctionTime.getTagsEditText() != null && itemAircraftFunctionTime.getTagsEditText().getTags() != null && itemAircraftFunctionTime.getTagsEditText().getTags().size() > 0) {
            itemAircraftFunctionTime.setTagsEditText(itemAircraftFunctionTime.getTagsEditText().getTags());
        }

        if (itemAircraftConditionTime.getTagsEditText() != null && itemAircraftConditionTime.getTagsEditText().getTags() != null && itemAircraftFunctionTime.getTagsEditText().getTags().size() > 0) {
            itemAircraftConditionTime.setTagsEditText(itemAircraftConditionTime.getTagsEditText().getTags());
        }

        createSwitchFields(null);
    }

    private ArrayList<CharSequence> getCharSequencesFunctionTime() {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("PIC");
        charSequences.add("SIC");
        charSequences.add("Dual");
        charSequences.add("Instructor");
        charSequences.add("PICus");
        charSequences.add("Examiner");
        charSequences.add("PICus when PF");
        return charSequences;
    }

    private String createRefSearch(String reference) {
        return reference.replace(".", "").replace("+", "").replace("-", "").replace("(", "").replace(")", "").replace("/", "");
    }

    private void setEditTextFilterAirCraft(MccEditText edt) {
        InputFilter filterChar = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '(', ')', '/', '.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        //InputFilter filterCap = new InputFilter.AllCaps();

        edt.setFilters(new InputFilter[]{filterChar});
    }

    private boolean checkEnable(ItemSwitch item) {
        return item.getVisibility() == View.VISIBLE;
    }

    private boolean saveAircraft() {
        byte[] aircraftCode;
        if (aircraft != null) {
            aircraftCode = aircraft.getAircraftCode();
        } else {
            aircraftCode = Utils.getByteArrayFromGUID(Utils.generateStringGUID());
        }
        int active = 1;
        int deviceCode = itemAircraftType.getItemId() + 1;
        String company = edtItemAircraftCompany.getText().toString();
        String reference = edtItemAircraftRegistration.getText().toString();
        String fin = edtItemAircraftFin.getText().toString();
        String make = edtItemAircraftManufacture.getText().toString();
        String model = edtItemAircraftModel.getText().toString();
        String subModel = edtItemAircraftVariant.getText().toString();
        String rating = edtItemAircraftRating.getText().toString();
        int category = 0;
        if (itemAircraftCategory.getVisibility() == View.VISIBLE)
            category = itemAircraftCategory.getItemId() + 1;
        int aircraftClass = 1;
        if (!TextUtils.isEmpty(itemAircraftClass.getDescription()) &&
                itemAircraftClass.getDescription().equalsIgnoreCase("Unmanned Aircraft")) {
            aircraftClass = 6;
        } else {
            aircraftClass = itemAircraftClass.getItemId() + 1;
        }
        int sea = itemAircraftSubClass.getVisibility() == View.VISIBLE ? itemAircraftSubClass.getItemId() : 0;
        int power = itemAircraftPower.getVisibility() == View.VISIBLE ? itemAircraftPower.getItemId() : 0;
        if (power == -1) power = 0;
        int moreThan57kg = checkEnable(itemSwitchMoreThan57kg) ? itemSwitchMoreThan57kg.getItemId() : 0;
        int tmg = checkEnable(itemSwitchTmGlider) ? itemSwitchTmGlider.getItemId() : 0;
        int tailWheel = checkEnable(itemSwitchTailWheel) ? itemSwitchTailWheel.getItemId() : 0;
        int complex = checkEnable(itemSwitchComplex) ? itemSwitchComplex.getItemId() : 0;
        int highPerformance = checkEnable(itemSwitchHighPerformance) ? itemSwitchHighPerformance.getItemId() : 0;
        int aerobatic = checkEnable(itemSwitchAerobatic) ? itemSwitchAerobatic.getItemId() : 0;
        int passSeats = 0;
        if (edtItemAircraftPassSeats.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(edtItemAircraftPassSeats.getText().toString())) {
            passSeats = Integer.parseInt(edtItemAircraftPassSeats.getText().toString());
        }
        int fNPT = 0;
        if (mZfnpt != null && itemAircraftDevice.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(itemAircraftDevice.getDescription())) {
            fNPT = mZfnpt.getFnptCode();
        }
        int defaultApp = 0;
        if (mApproach != null && itemAircraftDefaultApproach.getVisibility() == View.VISIBLE) {
            defaultApp = mApproach.getAPCode();
        }
        int defaultLaunch = 0;
        if (mLaunch != null && itemAircraftDefaultLaunch.getVisibility() == View.VISIBLE) {
            defaultLaunch = mLaunch.getLaunchCode();
        }
        int defaultOps = 0;
        if (mOperation != null && itemAircraftDefaultOperation.getVisibility() == View.VISIBLE) {

            defaultOps = mOperation.getOpsCode();
        }
        int defaultLog = 0;
        if (!TextUtils.isEmpty(itemAircraftFunctionTime.getTagsEditText().getText().toString()))
            defaultLog = getCharSequencesFunctionTime().indexOf(itemAircraftFunctionTime.getTagsEditText().getText().toString().trim()) + 1;
        int conditionLog = 0;
        if (itemAircraftConditionTime.getVisibility() == View.VISIBLE) {
            if (itemAircraftConditionTime.getTagsEditText().getTags() != null && itemAircraftConditionTime.getTagsEditText().getTags().size() > 0)
                for (int i = 0; i < itemAircraftConditionTime.getTagsEditText().getTags().size(); i++) {
                    switch (itemAircraftConditionTime.getTagsEditText().getTags().get(i)) {
                        case AircraftInfoConst.XC_STRING:
                            conditionLog += AircraftInfoConst.XC;
                            break;
                        case AircraftInfoConst.RELIEF_STRING:
                            conditionLog += AircraftInfoConst.RELIEF;
                            break;
                        case AircraftInfoConst.IFR_STRING:
                            conditionLog += AircraftInfoConst.IFR;
                            break;
                        case AircraftInfoConst.ACT_INSTR_STRING:
                            conditionLog += AircraftInfoConst.ACT_INSTR;
                            break;
                        case AircraftInfoConst.SIM_INSTR_STRING:
                            conditionLog += AircraftInfoConst.SIM_INSTR;
                            break;
                    }
                    if (settingConfigUserTime1 != null && Integer.parseInt(settingConfigUserTime1.getData()) == 1) {
                        SettingConfig settingConfigCaptionUserTime = databaseManager.getSetting("user_Caption1");
                        if (settingConfigCaptionUserTime != null && !TextUtils.isEmpty(settingConfigCaptionUserTime.getData())) {
                            if (itemAircraftConditionTime.getTagsEditText().getTags().get(i).equalsIgnoreCase(settingConfigCaptionUserTime.getData())) {
                                int configCode = settingConfigCaptionUserTime.getConfigCode();
                                conditionLog = switchConfigCodeUserTime(conditionLog, configCode);
                            }
                        }
                    }
                    if (settingConfigUserTime2 != null && Integer.parseInt(settingConfigUserTime2.getData()) == 1) {
                        SettingConfig settingConfigCaptionUserTime = databaseManager.getSetting("user_Caption2");
                        if (settingConfigCaptionUserTime != null && !TextUtils.isEmpty(settingConfigCaptionUserTime.getData())) {
                            if (itemAircraftConditionTime.getTagsEditText().getTags().get(i).equalsIgnoreCase(settingConfigCaptionUserTime.getData())) {
                                int configCode = settingConfigCaptionUserTime.getConfigCode();
                                conditionLog = switchConfigCodeUserTime(conditionLog, configCode);
                            }
                        }
                    }
                    if (settingConfigUserTime3 != null && Integer.parseInt(settingConfigUserTime3.getData()) == 1) {
                        SettingConfig settingConfigCaptionUserTime = databaseManager.getSetting("user_Caption3");
                        if (settingConfigCaptionUserTime != null && !TextUtils.isEmpty(settingConfigCaptionUserTime.getData())) {
                            if (itemAircraftConditionTime.getTagsEditText().getTags().get(i).equalsIgnoreCase(settingConfigCaptionUserTime.getData())) {
                                int configCode = settingConfigCaptionUserTime.getConfigCode();
                                conditionLog = switchConfigCodeUserTime(conditionLog, configCode);
                            }
                        }
                    }
                    if (settingConfigUserTime4 != null && Integer.parseInt(settingConfigUserTime4.getData()) == 1) {
                        SettingConfig settingConfigCaptionUserTime = databaseManager.getSetting("user_Caption4");
                        if (settingConfigCaptionUserTime != null && !TextUtils.isEmpty(settingConfigCaptionUserTime.getData())) {
                            if (itemAircraftConditionTime.getTagsEditText().getTags().get(i).equalsIgnoreCase(settingConfigCaptionUserTime.getData())) {
                                int configCode = settingConfigCaptionUserTime.getConfigCode();
                                conditionLog = switchConfigCodeUserTime(conditionLog, configCode);
                            }
                        }
                    }
                }
        }
        int run2logbook = itemAircraftLogbook.getItemId();
        String refSearch = "";
        if (!TextUtils.isEmpty(reference)) {
            refSearch = createRefSearch(reference);
        }
        long dateModify = DateTimeUtils.getCurrentUTCUnixTimeStamp();
        if (validateEmpty()) {
            Aircraft pAircraft = new Aircraft();
            pAircraft.setAircraftCode(aircraftCode);
            pAircraft.setActive(active == 1 ? true : false);
            pAircraft.setDeviceCode(deviceCode);
            pAircraft.setCompany(company);
            pAircraft.setReference(reference.toUpperCase());
            pAircraft.setRefSearch(refSearch.toUpperCase());
            pAircraft.setFin(fin.toUpperCase());
            pAircraft.setMake(make);
            pAircraft.setModel(model.toUpperCase());
            pAircraft.setSubModel(subModel.toUpperCase());
            pAircraft.setRating(rating);
            pAircraft.setCategory(category);
            pAircraft.setClass(aircraftClass);
            pAircraft.setSea(sea == 1 ? true : false);
            pAircraft.setPower(power);
            pAircraft.setKg5700(moreThan57kg == 1 ? true : false);
            pAircraft.setTMG(tmg == 1 ? true : false);
            pAircraft.setTailwheel(tailWheel == 1 ? true : false);
            pAircraft.setComplex(complex == 1 ? true : false);
            pAircraft.setHighPerf(highPerformance == 1 ? true : false);
            pAircraft.setAerobatic(aerobatic == 1 ? true : false);
            pAircraft.setSeats(passSeats);
            pAircraft.setFNPT(fNPT);
            pAircraft.setDefaultApp(defaultApp);
            pAircraft.setDefaultLaunch(defaultLaunch);
            pAircraft.setDefaultOps(defaultOps);
            pAircraft.setDefaultLog(defaultLog);
            pAircraft.setCondLog(conditionLog);
            pAircraft.setRun2(run2logbook == 1 ? true : false);
            pAircraft.setRecord_Modified(dateModify);
            pAircraft.setRecord_Upload(true);
            if (databaseManager.isAircraftExist(pAircraft.getRefSearch()) && aircraft == null) {
                MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.aircraft_already_used).show();
                return false;
            }
            aircraft = pAircraft;

            boolean result = databaseManager.insertAircraft(pAircraft);
            clearAutoLoad();
            return result;
        }
        return false;
    }

    private int switchConfigCodeUserTime(int conditionLog, int configCode) {
        switch (configCode) {
            case 480:
                conditionLog += AircraftInfoConst.MIN_U1;
                break;
            case 481:
                conditionLog += AircraftInfoConst.MIN_U2;
                break;
            case 482:
                conditionLog += AircraftInfoConst.MIN_U3;
                break;
            case 483:
                conditionLog += AircraftInfoConst.MIN_U4;
                break;
        }
        return conditionLog;
    }

    private boolean validateEmpty() {
        if (TextUtils.isEmpty(itemAircraftType.getDescription().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_type).show();
            return false;
        } else if (TextUtils.isEmpty(itemAircraftClass.getDescription().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_class).show();
            return false;
        } else if (itemAircraftSubClass.getVisibility() == View.VISIBLE && TextUtils.isEmpty(itemAircraftSubClass.getDescription().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_subclass).show();
            return false;
        } else if (itemAircraftCategory.getVisibility() == View.VISIBLE && TextUtils.isEmpty(itemAircraftCategory.getDescription().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_category).show();
            return false;
        } else if (itemAircraftPower.getVisibility() == View.VISIBLE && TextUtils.isEmpty(itemAircraftPower.getDescription().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_power).show();
            return false;
        } else if (itemAircraftDevice.getVisibility() == View.VISIBLE && TextUtils.isEmpty(itemAircraftDevice.getDescription())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_device_power).show();
            return false;
        } else if (TextUtils.isEmpty(edtItemAircraftCompany.getText().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_company).show();
            return false;
        } else if (TextUtils.isEmpty(edtItemAircraftRegistration.getText().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_registration).show();
            return false;
        } else if (TextUtils.isEmpty(edtItemAircraftModel.getText().toString())) {
            MccDialog.getOkAlertDialog(mActivity, R.string.add_aircraft_message, R.string.missing_aircraft_model).show();
            return false;
        }
        return true;
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
                    condLogAdded = databaseManager.getSetting(480).getData();
                    break;
                case AircraftInfoConst.MIN_U3:
                    condLogAdded = databaseManager.getSetting(481).getData();
                    break;
                case AircraftInfoConst.MIN_U2:
                    condLogAdded = databaseManager.getSetting(482).getData();
                    break;
                case AircraftInfoConst.MIN_U1:
                    condLogAdded = databaseManager.getSetting(483).getData();
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

            condLogString += MCCPilotLogConst.STRING_EMPTY.equals(condLogString) ? condLogAdded : MCCPilotLogConst.SPLIT_KEY_APPEND + condLogAdded;
            condLogNum = rest;
        }
        return condLogString;
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

    private HashMap<MccEditText, Integer> initEditTextTag() {
        HashMap<MccEditText, Integer> mapEditText = new HashMap<>();
        mapEditText.put(edtItemAircraftCompany, 1);
        mapEditText.put(edtItemAircraftRegistration, 2);
        mapEditText.put(edtItemAircraftFin, 3);
        mapEditText.put(edtItemAircraftModel, 4);
        mapEditText.put(edtItemAircraftVariant, 5);
        mapEditText.put(edtItemAircraftManufacture, 6);
        mapEditText.put(edtItemAircraftRating, 7);
        mapEditText.put(edtItemAircraftPassSeats, 8);
        setActionDone(edtItemAircraftCompany);
        setActionDone(edtItemAircraftRegistration);
        setActionDone(edtItemAircraftFin);
        setActionDone(edtItemAircraftModel);
        setActionDone(edtItemAircraftVariant);
        setActionDone(edtItemAircraftManufacture);
        setActionDone(edtItemAircraftRating);
        setActionDone(edtItemAircraftPassSeats);
        edtItemAircraftCompany.setSelectAllOnFocus(true);
        edtItemAircraftRegistration.setSelectAllOnFocus(true);
        edtItemAircraftFin.setSelectAllOnFocus(true);
        edtItemAircraftModel.setSelectAllOnFocus(true);
        edtItemAircraftVariant.setSelectAllOnFocus(true);
        edtItemAircraftManufacture.setSelectAllOnFocus(true);
        edtItemAircraftRating.setSelectAllOnFocus(true);
        edtItemAircraftPassSeats.setSelectAllOnFocus(true);
        return mapEditText;
    }

    private void setActionDone(final MccEditText edt) {
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    nextPrevFocus(MccEnum.topKeyboardCustomInput.DONE);
                }
                return false;
            }
        });

    }


    private void showTopKey() {
        if (!isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (!isTablet()) {
                mTopKey.setVisibility(View.VISIBLE);
            } else {
                mTopKey.setVisibility(View.VISIBLE);
                Fragment fragment = getLeftFragment();
                if (fragment instanceof PilotListFragment) {
                    ((PilotListFragment) fragment).mFooterContainer.setVisibility(View.GONE);
                }
            }
            isShowTopKey = true;
        }

    }

    private void hideTopKey() {
        if (isShowTopKey) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if (!isTablet()) {
                mTopKey.setVisibility(View.GONE);
            } else {
                mTopKey.setVisibility(View.GONE);
                Fragment fragment = getLeftFragment();
                if (fragment instanceof PilotListFragment) {
                    ((PilotListFragment) fragment).mFooterContainer.setVisibility(View.VISIBLE);
                }
            }
            isShowTopKey = false;
        }
    }

    String keyboardMemoryTemp1 = "";

    private void nextPrevFocus(MccEnum.topKeyboardCustomInput input) {
        View v = mActivity.getCurrentFocus();
        MccEditText edt;
        int entryPoint;
        if (v instanceof MccEditText) {
            entryPoint = editTextTagMap.get(v);
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
                entryPoint++;
                MccEditText edtNext = getKeyByValue(editTextTagMap, entryPoint);
                if (edtNext != null && edtNext.getVisibility() != View.GONE) {
                    edtNext.requestFocus();
                } else {
                    edt.clearFocus();
                    hideTopKey();
                    KeyboardUtils.hideSoftKeyboard(mActivity);
                }
                break;
        }
    }


    private MccEditText getKeyByValue(HashMap<MccEditText, Integer> map, int value) {
        for (Map.Entry<MccEditText, Integer> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }


    @OnFocusChange({
            R.id.edt_item_aircraft_company, R.id.edt_item_aircraft_registration, R.id.edt_item_aircraft_fin,
            R.id.edt_item_aircraft_model, R.id.edt_item_aircraft_manufacture, R.id.edt_item_aircraft_variant,
            R.id.edt_item_aircraft_type_rating, R.id.edt_item_aircraft_pass_seats
    })
    void onFocusChange(View v) {
        switch (v.getId()) {
            case R.id.edt_item_aircraft_company:
            case R.id.edt_item_aircraft_registration:
            case R.id.edt_item_aircraft_fin:
            case R.id.edt_item_aircraft_model:
            case R.id.edt_item_aircraft_manufacture:
            case R.id.edt_item_aircraft_variant:
            case R.id.edt_item_aircraft_type_rating:
            case R.id.edt_item_aircraft_pass_seats:
                keyboardMemoryTemp1 = ((MccEditText) v).getText().toString();
                showTopKey();
                break;
        }
        View v2 = mActivity.getCurrentFocus();
        if (v2 == null || !(v2 instanceof MccEditText)) {
            hideTopKey();
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
    public void onTagsClear(ItemInputTagsWithIcon itemInputTagsWithIcon) {
        switch (itemInputTagsWithIcon.getId()) {
            case R.id.item_aircraft_default_operation:
                itemInputTagsWithIcon.getTagsEditText().setTags("");
                mOperation = null;
                break;
            case R.id.item_aircraft_default_approach:
                itemInputTagsWithIcon.getTagsEditText().setTags("");
                mApproach = null;
                break;
            case R.id.item_aircraft_default_launch:
                itemInputTagsWithIcon.getTagsEditText().setTags("");
                mLaunch = null;
                break;
        }
    }

    @Override
    public void onTagsChanged(ItemInputTagsWithIcon itemInputTagsWithIcon) {
        if (!isChanged) isChanged = true;
    }

    private void clearAutoLoad() {
        mOperation = null;
        mApproach = null;
        mLaunch = null;
    }

    @Override
    public void selectValueChange(String value, ItemInputTextWithIcon itemInputTextWithIcon) {
        switch (itemInputTextWithIcon.getId()) {
            case R.id.item_aircraft_type:
                if (itemInputTextWithIcon.getItemId() == 2) {
                    setLayoutWhenAircraftTypeChange(AircraftType.DRONE);
                } else if (itemInputTextWithIcon.getItemId() == 1) {
                    setLayoutWhenAircraftTypeChange(AircraftType.SIMULATOR);
                } else {
                    setLayoutWhenAircraftTypeChange(AircraftType.AIRCRAFT);
                }
                break;
            case R.id.item_aircraft_class:
                if (itemInputTextWithIcon.getItemId() == 1) {
                    itemAircraftDefaultLaunch.setVisibility(View.VISIBLE);
                } else {
                    itemAircraftDefaultLaunch.setVisibility(View.GONE);
                }
                if (itemInputTextWithIcon.getItemId() == 3 || itemInputTextWithIcon.getItemId() == 4)
                    itemAircraftDefaultApproach.setVisibility(View.VISIBLE);
                else
                    itemAircraftDefaultApproach.setVisibility(View.GONE);
                break;
        }
    }

    private void setLayoutWhenAircraftTypeChange(AircraftType aircraftType) {
        if (aircraftType == AircraftType.DRONE) {
            lnPassSeats.setVisibility(View.GONE);
            edtItemAircraftPassSeats.setVisibility(View.GONE);
            itemAircraftClass.setEnabled(false);
            currentAircraftClass = itemAircraftClass.getDescription();
            itemAircraftClass.setDescription("Unmanned Aircraft");
            itemAircraftClass.setVisibleIcon(View.GONE);
            itemAircraftClass.getTvDescription().setTextColor(getResources().getColor(R.color.grey_description_text));
            itemAircraftSubClass.setVisibility(View.GONE);
            itemAircraftCategory.setVisibility(View.GONE);
            itemAircraftPower.setVisibility(View.GONE);
            if (!itemAircraftDevice.isDrone()) {
                itemAircraftDevice.setIsDrone(true);
                itemAircraftDevice.setDescription("");
                itemAircraftDevice.setFootNote("");
            }
            itemAircraftDevice.setVisibility(View.VISIBLE);
            itemAircraftConditionTime.setVisibility(View.GONE);
            itemAircraftDefaultApproach.setVisibility(View.GONE);
            itemSwitchAerobatic.setVisibility(View.GONE);
            itemSwitchComplex.setVisibility(View.GONE);
            itemSwitchHighPerformance.setVisibility(View.GONE);
            itemSwitchTmGlider.setVisibility(View.GONE);
            itemSwitchTailWheel.setVisibility(View.GONE);
            itemSwitchMoreThan57kg.setVisibility(View.GONE);
        } else {
            lnPassSeats.setVisibility(View.VISIBLE);
            edtItemAircraftPassSeats.setVisibility(View.VISIBLE);
            itemAircraftClass.setEnabled(true);
            itemAircraftClass.setVisibleIcon(View.VISIBLE);
            itemAircraftClass.getTvDescription().setTextColor(getResources().getColor(R.color.blackOlive));
            if (!TextUtils.isEmpty(currentAircraftClass) && !currentAircraftClass.equalsIgnoreCase("Unmanned Aircraft")) {
                itemAircraftClass.setDescription(currentAircraftClass);
            } else if (!TextUtils.isEmpty(itemAircraftClass.getDescription()) && itemAircraftClass.getDescription().equalsIgnoreCase("Unmanned Aircraft")) {
                itemAircraftClass.setDescription("");
            }
            itemAircraftSubClass.setVisibility(View.VISIBLE);
            itemAircraftCategory.setVisibility(View.VISIBLE);
            itemAircraftPower.setVisibility(View.VISIBLE);
            itemAircraftDevice.setVisibility(View.GONE);
            itemAircraftConditionTime.setVisibility(View.VISIBLE);
            itemSwitchAerobatic.setVisibility(View.VISIBLE);
            itemSwitchComplex.setVisibility(View.VISIBLE);
            itemSwitchHighPerformance.setVisibility(View.VISIBLE);
            itemSwitchTmGlider.setVisibility(View.VISIBLE);
            itemSwitchTailWheel.setVisibility(View.VISIBLE);
            itemSwitchMoreThan57kg.setVisibility(View.VISIBLE);
            if (aircraftType == aircraftType.SIMULATOR) {
                itemAircraftDevice.setVisibility(View.VISIBLE);
                if (itemAircraftDevice.isDrone()) {
                    itemAircraftDevice.setIsDrone(false);
                    itemAircraftDevice.setDescription("");
                    itemAircraftDevice.setFootNote("");
                }
            }
        }
    }

    @Override
    public void switchChange(boolean change, ItemSwitch itemSwitch) {
        if (!isChanged) isChanged = true;
    }

    public enum AircraftType {
        AIRCRAFT,
        SIMULATOR,
        DRONE
    }

    private void scrollToBottom(final ItemInputTagsWithIcon item) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        svAircraft.smoothScrollTo(0, item.getTop() + 100);
                    }
                });
            }
        });

    }


    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            if (!isChanged)
                isChanged = true;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    public void onKeyBackPress() {
        if ((isChanged || isSelectValueChanged()) && isVisible()) {
            MccDialog.getOkCancelAlertDialog(mActivity, aircraft == null ? R.string.aircraft_add_title : R.string.aircraft_edit_title
                    , R.string.cancel_message_content
                    , new MccDialog.MCCDialogCallBack() {
                        @Override
                        public void onClickDialog(DialogInterface pAlertDialog, int pDialogType) {
                            if (pDialogType == DialogInterface.BUTTON_POSITIVE) {
                                KeyboardUtils.hideSoftKeyboard(mActivity);
                                clearAutoLoad();
                                finishFragment();
                            }
                        }
                    }).show();
        } else {
            KeyboardUtils.hideSoftKeyboard(mActivity);
            finishFragment();
        }
    }


    private boolean isSelectValueChanged() {
        return itemAircraftType.isValueChanged ||
                itemAircraftClass.isValueChanged ||
                itemAircraftSubClass.isValueChanged ||
                itemAircraftCategory.isValueChanged ||
                itemAircraftPower.isValueChanged ||
                itemAircraftDevice.isValueChanged ||
                itemAircraftFunctionTime.isValueChanged ||
                itemAircraftConditionTime.isValueChanged ||
                itemAircraftDefaultOperation.isValueChanged ||
                itemAircraftDefaultApproach.isValueChanged ||
                itemAircraftDefaultLaunch.isValueChanged;
    }

    @Override
    public void onResume() {
        itemSwitchAerobatic.setIItemSwitch(this);
        itemSwitchComplex.setIItemSwitch(this);
        itemSwitchHighPerformance.setIItemSwitch(this);
        itemSwitchTmGlider.setIItemSwitch(this);
        itemSwitchTailWheel.setIItemSwitch(this);
        super.onResume();
    }
}
