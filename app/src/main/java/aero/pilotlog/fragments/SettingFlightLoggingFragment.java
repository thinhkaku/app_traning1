package aero.pilotlog.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.InputFilterMinMax;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IAsyncTaskCallback;
import aero.pilotlog.tasks.LoadDataTask;
import aero.pilotlog.utilities.FlightUtils;
import aero.pilotlog.utilities.KeyboardUtils;
import aero.pilotlog.widgets.ItemInputText;
import aero.pilotlog.widgets.ItemInputTextWithIcon;
import aero.pilotlog.widgets.ItemSwitch;
import aero.pilotlog.widgets.ItemSwitchAutoLoad;
import aero.pilotlog.widgets.MccDialog;
import aero.pilotlog.widgets.MccEditText;
import aero.pilotlog.widgets.MccSwitch;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFlightLoggingFragment extends BaseMCCFragment implements ItemSwitch.IItemSwitch, ItemInputText.IItemInputText, ItemInputTextWithIcon.IItemInputText, IAsyncTaskCallback {
    @Bind(R.id.tvTitle)
    TextView mTvTitle;
    @Bind(R.id.ibLeft)
    ImageButton mIbLeft;
    @Bind(R.id.btnRight)
    Button mBtnRight;
    @Bind(R.id.ibMenu)
    ImageButton mIbMenu;
    @Bind(R.id.item_home_base)
    ItemInputTextWithIcon itemHomeBase;
    @Bind(R.id.item_auto_load_task)
    ItemInputTextWithIcon itemAutoLoadTask;
    @Bind(R.id.item_fuel_unit)
    MccSwitch itemFuelUnit;
    @Bind(R.id.item_night_mode)
    ItemInputTextWithIcon itemNightMode;
    @Bind(R.id.item_night_mode_do_not_suggest_ldg)
    ItemSwitch itemDoNotSuggestLDG;
    @Bind(R.id.item_suggest_home_base_airfield)
    ItemSwitch itemSuggestHomeBase;
    @Bind(R.id.item_auto_load_xc)
    ItemSwitch itemAutoLoadXC;
    @Bind(R.id.item_auto_load_remind_pilot)
    ItemSwitch itemAutoLoadRemindPilot;
    @Bind(R.id.item_fuel_entries)
    ItemSwitch itemFuelEntries;
    @Bind(R.id.item_auto_load_ins)
    ItemSwitchAutoLoad itemAutoLoadIns;
    @Bind(R.id.item_auto_load_relief)
    ItemSwitchAutoLoad itemAutoLoadRelief;
    @Bind(R.id.item_fuel_co)
    ItemSwitchAutoLoad itemFuelCo;
    @Bind(R.id.item_run_2_logbooks)
    ItemSwitch itemRun2Logbook;
    @Bind(R.id.item_night_task)
    ItemSwitch itemNightTask;
    @Bind(R.id.item_night_ss_sr)
    ItemInputText itemNightSSSR;
    @Bind(R.id.item_night_from)
    ItemInputText itemNightFrom;
    @Bind(R.id.item_night_to)
    ItemInputText itemNightTo;
    @Bind(R.id.item_night_mode_do_not_suggest_ldg_line_top)
    View itemNightModeDoNotSuggestLDGLineTop;
    Airfield airfield;
    DatabaseManager databaseManager;
    @Bind(R.id.llLoading)
    LinearLayout mLlLoading;
    @Bind(R.id.scrollView3)
    ScrollView scrollView;

    private LoadDataTask mLoadDataTask;

    public SettingFlightLoggingFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderResId() {
        return R.layout.layout_action_bar;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_setting_flight_logging;
    }

    @Override
    protected int getFooterResId() {
        return 0;
    }

    @Override
    protected void onCreateFragment(View pContentView) throws ParseException {
        ButterKnife.bind(this, mViewContainer);
        mTvTitle.setText(getString(R.string.setting_title) + " - " + getString(R.string.setting_flight_logging));
        mIbLeft.setVisibility(View.VISIBLE);
        mIbMenu.setVisibility(View.GONE);
        if (isTablet()) {
            mBtnRight.setVisibility(View.GONE);
        }
        databaseManager = DatabaseManager.getInstance(mActivity);
        mLoadDataTask = new LoadDataTask(mActivity, this, mLlLoading);
        mLoadDataTask.execute();
        //initView();
    }


    private void initView() {
        scrollView.setVisibility(View.VISIBLE);
        itemHomeBase.setIcon(R.drawable.ic_arrow);
        itemHomeBase.setLayoutResId(R.id.fragmentMain);
        itemHomeBase.setParentFragment(this);
        itemHomeBase.setNavigationToFragment(AirfieldsListFragment.class);
        itemSuggestHomeBase.setFootNote("Recommended only when most flights land at the airfield of departure");
        itemAutoLoadXC.setFootNote("Autoload XC time when route distance is more than 50 NM (aeroplanes) or 25 NM (rotorcraft)");
        itemAutoLoadRemindPilot.setFootNote("Remind me of missing contact details when I fly with a fellow pilot");
        itemFuelEntries.setFootNote("Notify me when Fuel numbers appear strange");
        itemAutoLoadIns.setTextFootNote("Portion of flight in actual IMC conditions");
        itemAutoLoadRelief.setTextFootNote("Rest time on longer flights with augmented flight crew");
        itemFuelCo.setTextFootNote("Leave blank or 0 for 'auto'");
        itemDoNotSuggestLDG.setFootNote("Night Time populates the Takeoff Day-Night and the Landing Day-Night fields. Turn on here to disable this feature.");
        itemNightTask.setFootNote("Takeoff and Landing fields are populated, only when you are PF (Pilot Flying). Turn on here to also auto-populate for PM (PNF).");
        itemNightSSSR.setTextFootNote("Minutes after Sunset / prior Sunrise");
        itemNightFrom.setTextFootNote("Night Time starts at this Hour UTC");
        itemNightTo.setTextFootNote("Night Time ends at this Hour UTC");
        itemNightSSSR.setIItemInputText(this);
        itemNightFrom.setIItemInputText(this);
        itemNightTo.setIItemInputText(this);
        createAutoLoadTask();
        createNightMode();
        itemRun2Logbook.setIItemSwitch(this);
        if (itemRun2Logbook.getChecked())
            itemRun2Logbook.setFootNote("Flights are printed in 2 separate Logbooks based on Aircraft Registration");
        else
            itemRun2Logbook.setFootNote("All Flights are printed in a single Logbook");

    }

    private void createAutoLoadTask() {
        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("Off");
        charSequences.add("Alternate");
        charSequences.add("PICUS");
        charSequences.add("Always");
        itemAutoLoadTask.setIcon(R.drawable.ic_arrow);
        itemAutoLoadTask.setVisibleFootNote(View.GONE);

        if (!TextUtils.isEmpty(itemAutoLoadTask.getDescription())) {
            itemAutoLoadTask.setListStringDialog(charSequences, charSequences.indexOf(itemAutoLoadTask.getDescription()));
        }
    }

    private void createNightMode() {
        ArrayList<CharSequence> charSequencesNightMode = new ArrayList<>();
        charSequencesNightMode.add("Off");
        charSequencesNightMode.add("Between SS and SR");
        charSequencesNightMode.add("Between fixed Local Hours");
        charSequencesNightMode.add("Both (SS/SR + fixed Hours)");
        itemNightMode.setIcon(R.drawable.ic_arrow);
        itemNightMode.setVisibleFootNote(View.GONE);
        if (!TextUtils.isEmpty(itemNightMode.getDescription())) {
            if (itemNightMode.getDescription().equals("Off")) {
                itemNightMode.setListStringDialog(charSequencesNightMode, 0);
            } else {
                SettingConfig settingConfig = databaseManager.getSetting("NightCalc");
                if (settingConfig != null) {
                    String settingData = settingConfig.getData();
                    if (!TextUtils.isEmpty(settingData)) {
                        int currentSelect = 0;
                        switch (settingData) {
                            case "0":
                                currentSelect = 1;
                                break;
                            case "1":
                                currentSelect = 2;
                                break;
                            case "2":
                                currentSelect = 3;
                                break;
                        }
                        itemNightMode.setListStringDialog(charSequencesNightMode, currentSelect);//itemNightMode.getDescription()));
                    }
                }
            }
        }
        selectValueChange(String.valueOf(itemNightMode.getItemId()), itemNightMode);
        itemNightMode.setIItemInputText(this);
        itemDoNotSuggestLDG.setVisibility(itemNightMode.getItemId() == 0 ? View.GONE : View.VISIBLE);
    }

    @Nullable
    @OnClick({R.id.rlBackIcon})
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.rlBackIcon:
                finishFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (this.airfield != null) {
            String iAta = airfield.getAFIATA();
            if (!TextUtils.isEmpty(iAta))
                iAta = iAta + "-";
            itemHomeBase.setDescription(String.format("%s%s\n%s", iAta, airfield.getAFICAO(), airfield.getAFName()));
            ZCountry zCountry = databaseManager.getCountryByCode(airfield.getAFCountry());
            if (zCountry != null)
                itemHomeBase.setFootNote(zCountry.getCountryName());
            databaseManager.updateSetting("HomeBase", airfield.getAFICAO());
        }
    }

    public void setSelectedAirfield(Airfield airfield) {
        this.airfield = airfield;
    }

    @Override
    public void switchChange(boolean change, ItemSwitch itemSwitch) {
        if (itemSwitch.getId() == R.id.item_fuel_unit) {
            if (change)
                itemFuelCo.setTitle(String.format("%s level (g/kg)", getString(R.string.co2_text)));
            else
                itemFuelCo.setTitle(String.format("%s level (g/lbs)", getString(R.string.co2_text)));
        } else if (itemSwitch.getId() == R.id.item_run_2_logbooks) {
            if (change)
                itemRun2Logbook.setFootNote("Flights are printed in 2 separate Logbooks based on Aircraft Registration");
            else
                itemRun2Logbook.setFootNote("All Flights are printed in a single Logbook");

        }
    }

    @Override
    public void selectValueChange(String value, ItemInputTextWithIcon itemSwitch) {
        if (!TextUtils.isEmpty(value)) {
            if (value.equals("0")) {
                itemDoNotSuggestLDG.setVisibility(View.GONE);
                itemNightMode.setDescription("Off");
                itemNightSSSR.setVisibility(View.GONE);
                itemNightFrom.setVisibility(View.GONE);
                itemNightTo.setVisibility(View.GONE);
                itemNightMode.setFootNote("");
                itemNightModeDoNotSuggestLDGLineTop.setVisibility(View.GONE);
                databaseManager.updateSetting("NightMode", "0");
            } else {
                SettingConfig settingUserTime4Caption = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4_CAPTION);
                switch (value) {
                    case "1":
                        //displayTextBoxSRSS("SET MINUTES AFTER SS BEFORE SR", 1, 1440, 4);
                        itemNightSSSR.setVisibility(View.VISIBLE);
                        itemNightSSSR.setVisibleLine(View.GONE);
                        itemNightFrom.setVisibility(View.GONE);
                        itemNightTo.setVisibility(View.GONE);
                        itemNightModeDoNotSuggestLDGLineTop.setVisibility(View.VISIBLE);
                        itemNightMode.setFootNote("");
                        itemNightMode.setDescription("Between SS and SR");
                        databaseManager.updateSetting("NightCalc", "0");
                        if (settingUserTime4Caption != null && settingUserTime4Caption != null && settingUserTime4Caption.getData().equalsIgnoreCase("fh-night")) {
                            databaseManager.updateSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4, "0");
                        }
                        break;
                    case "2":
                        //displayTextBoxFixHours("SET FIXED HOURS", 5, "Hours From", "Hours To");
                        itemNightSSSR.setVisibility(View.GONE);
                        itemNightFrom.setVisibility(View.VISIBLE);
                        itemNightTo.setVisibility(View.VISIBLE);
                        itemNightTo.setVisibleLine(View.GONE);
                        itemNightModeDoNotSuggestLDGLineTop.setVisibility(View.VISIBLE);
                        itemNightMode.setFootNote("");
                        itemNightMode.setDescription("Between fixed Local Hours");
                        databaseManager.updateSetting("NightCalc", "1");
                        if (settingUserTime4Caption != null && settingUserTime4Caption != null && settingUserTime4Caption.getData().equalsIgnoreCase("fh-night")) {
                            databaseManager.updateSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4, "0");
                        }
                        break;
                    case "3":

                        SettingConfig settingUserTime4 = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4);

                        if (settingUserTime4 != null && !TextUtils.isEmpty(settingUserTime4.getData()) && settingUserTime4Caption != null && settingUserTime4Caption.getData() != null) {
                            if (settingUserTime4.getData().equals("1") && !settingUserTime4Caption.getData().equalsIgnoreCase("fh-night")) {
                                MccDialog.getOkAlertDialog(mActivity, R.string.invalid_operation,
                                        String.format(mActivity.getResources().getString(R.string.message_both_night_time_select), settingUserTime4Caption.getData())).show();
                                createNightMode();
                                return;
                            } else {
                                databaseManager.updateSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4, "1");
                                databaseManager.updateSetting(MCCPilotLogConst.SETTING_CODE_USER_TIME_4_CAPTION, "FH-Night");
                                itemNightSSSR.setVisibility(View.VISIBLE);
                                itemNightFrom.setVisibility(View.VISIBLE);
                                itemNightTo.setVisibility(View.VISIBLE);
                                itemNightTo.setVisibleLine(View.GONE);
                                itemNightSSSR.setVisibleLine(View.VISIBLE);
                                itemNightModeDoNotSuggestLDGLineTop.setVisibility(View.VISIBLE);
                                itemNightMode.setDescription("SS/SR and Fixed Hours");
                                itemNightMode.setFootNote("Night Time between SS/SR is stored in the Night time field.\n" +
                                        "Night Time between Fixed Hours is stored in the User Time 4 field.");
                                databaseManager.updateSetting("NightCalc", "2");
                            }
                        }
                        break;
                }
                itemDoNotSuggestLDG.setVisibility(View.VISIBLE);
                databaseManager.updateSetting("NightMode", itemDoNotSuggestLDG.getChecked() ? "2" : "1");
            }


        }

    }


    private void displayTextBoxSRSS(String title, int minLength, int maxLength, int lengthFilter) {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(lengthFilter);
        fArray[1] = new InputFilterMinMax(minLength, maxLength);
        inputText.setFilters(fArray);
        SettingConfig settingConfig = databaseManager.getSetting("NightTimeSR");
        if (settingConfig != null) {
            String percent = settingConfig.getData();
            inputText.setText(percent);
        }
        inputText.selectAll();
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(title);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity, R.style.MessageDialogTheme);
        builder
                //.setTitle(title)
                .setView(inputTextDialog)
                .setNegativeButton(R.string.sign_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        if (!TextUtils.isEmpty(inputText.getText().toString())) {
                            //radioSRSS.setText(String.format("Night: %s minutes after SS / before SR", inputText.getText().toString()));
                            itemNightMode.setDescription(String.format("Night: %s minutes after SS / before SR", inputText.getText().toString()));
                            databaseManager.updateSetting("NightTimeSR", inputText.getText().toString());
                        }
                        dialog.dismiss();
                    }
                }).show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void displayTextBoxFixHours(String title, int lengFilter, String message1, String message2) {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_two_text, null);
        final MccEditText inputText = (MccEditText) inputTextDialog.findViewById(R.id.input);
        final MccEditText inputText2 = (MccEditText) inputTextDialog.findViewById(R.id.input2);
        TextView tvMessage1 = (TextView) inputTextDialog.findViewById(R.id.message);
        TextView tvMessage2 = (TextView) inputTextDialog.findViewById(R.id.message2);
        TextView tvMessage3 = (TextView) inputTextDialog.findViewById(R.id.message3);
        tvMessage1.setText(message1);
        tvMessage2.setText(message2);
        tvMessage3.setText(title);
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(lengFilter);
        inputText.setFilters(fArray);
        inputText2.setFilters(fArray);
        inputText.setOnFocusChangeListener(onFocusChangeListener());
        inputText2.setOnFocusChangeListener(onFocusChangeListener());
        inputText2.setSelectAllOnFocus(true);
        setActionDone(inputText2);
        SettingConfig settingConfigFrom = databaseManager.getSetting("NightTimeFrom");
        final String currentHourFrom;
        final String currentHourTo;
        if (settingConfigFrom != null) {
            currentHourFrom = settingConfigFrom.getData();
            inputText.setText(currentHourFrom);
        } else {
            currentHourFrom = "";
        }
        SettingConfig settingConfigTo = databaseManager.getSetting("NightTimeUntil");
        if (settingConfigTo != null) {
            currentHourTo = settingConfigTo.getData();
            inputText2.setText(currentHourTo);
        } else {
            currentHourTo = "";
        }
        inputText.selectAll();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity, R.style.MessageDialogTheme);
        builder
                //.setTitle(title)
                .setView(inputTextDialog)
                .setNegativeButton(R.string.sign_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        inputText.clearFocus();
                        inputText2.clearFocus();
                        String hoursFrom = currentHourFrom;
                        String hoursTo = currentHourTo;
                        if (!TextUtils.isEmpty(inputText.getText().toString())) {
                            hoursFrom = inputText.getText().toString();
                        }
                        if (!TextUtils.isEmpty(inputText2.getText().toString())) {
                            hoursTo = inputText2.getText().toString();
                        }
                        itemNightMode.setDescription(String.format("Fixed Hours from %s to %s  incl.",
                                hoursFrom, hoursTo));
                        databaseManager.updateSetting("NightTimeFrom", inputText.getText().toString());
                        databaseManager.updateSetting("NightTimeUntil", inputText2.getText().toString());
                        dialog.dismiss();
                    }
                }).show();
       /* AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private View.OnFocusChangeListener onFocusChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && view instanceof MccEditText) {
                    MccEditText editText = (MccEditText) view;
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        FlightUtils flightUtils = FlightUtils.getInstance(mActivity, databaseManager);
                        final String timeDisplay = flightUtils.getTimeDisplay(editText.getText().toString(), false, false, false);
                        editText.setText(timeDisplay);
                        //KeyboardUtils.hideKeyboard(mActivity);
                    }
                }
            }
        };
    }

    private void setActionDone(final MccEditText edt) {
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!TextUtils.isEmpty(edt.getText().toString())) {
                        FlightUtils flightUtils = FlightUtils.getInstance(mActivity, databaseManager);
                        final String timeDisplay = flightUtils.getTimeDisplay(edt.getText().toString(), false, false, false);
                        edt.setText(timeDisplay);
                        KeyboardUtils.showKeyboard(mActivity);
                    }
                }
                return false;
            }
        });

    }

    private void setActionDone(final EditText edt) {
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (!TextUtils.isEmpty(edt.getText().toString())) {
                        FlightUtils flightUtils = FlightUtils.getInstance(mActivity, databaseManager);
                        final String timeDisplay = flightUtils.getTimeDisplay(edt.getText().toString(), false, false, false);
                        edt.setText(timeDisplay);
                        KeyboardUtils.hideKeyboard(mActivity);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void showInputText(MccEditText editText, ItemInputText itemInputText) {
        if (itemInputText.getId() == R.id.item_night_ss_sr) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            InputFilter[] fArray = new InputFilter[2];
            fArray[0] = new InputFilter.LengthFilter(4);
            fArray[1] = new InputFilterMinMax(1, 1440);
            editText.setFilters(fArray);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(5);
            editText.setFilters(fArray);
            editText.setOnFocusChangeListener(onFocusChangeListener());
            setActionDone(editText);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void doWork() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUI() {
        initView();
    }

    @Override
    public void end() {

    }
}
