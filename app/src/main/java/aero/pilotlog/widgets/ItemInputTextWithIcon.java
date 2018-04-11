package aero.pilotlog.widgets;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.entities.ZCurrency;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.fragments.BaseFragment;
import aero.pilotlog.fragments.SettingFlightLoggingFragment;
import aero.pilotlog.interfaces.ISequenceAction;
import aero.pilotlog.interfaces.ItemSettingInterface;

import java.util.ArrayList;
import java.util.List;

public class ItemInputTextWithIcon extends ItemBase {

    private TextView tvDescription;
    private TextView tvFootNote;
    private TextView tvFootNote2;
    private View viewLineBorderBottom;
    private ArrayList<CharSequence> listLandingPage = null;
    private ArrayList<CharSequence> listDateTimeMode = null;
    private ArrayList<CharSequence> listCurrency = null;
    private ArrayList<CharSequence> listStringDialog = null;
    private int defaultIndex;
    private BaseFragment parentFragment;
    private int layoutResId = 0;
    private Class<? extends BaseFragment> navigationToFragment;
    private boolean isDrone = false;//only for select FNPT
    IItemInputText iItemInputText;
    public boolean isValueChanged = false;

    public void setIItemInputText(IItemInputText iItemInputText) {
        this.iItemInputText = iItemInputText;
    }

    public boolean isDrone() {
        return isDrone;
    }

    public void setIsDrone(boolean isDrone) {
        this.isDrone = isDrone;
    }

    public void setNavigationToFragment(Class<? extends BaseFragment> navigationToFragment) {
        this.navigationToFragment = navigationToFragment;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public void setParentFragment(BaseFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    public void setMarginLeftDescriptionFootNote(int margin) {
        LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (tvDescription != null) {
            lastTxtParams.setMargins(margin, 0, 0, 0);
            tvDescription.setLayoutParams(lastTxtParams);
            tvDescription.invalidate();
        }
        if (tvFootNote != null) {
            lastTxtParams.setMargins(margin, 0, 0, 0);
            tvFootNote.setLayoutParams(lastTxtParams);
            tvFootNote.invalidate();
        }
    }


    public ItemInputTextWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreateLayout() {
        inflate(getContext(), R.layout.item_setting_base_with_icon, this);
        tvDescription = (TextView) findViewById(R.id.txt_setting_description);
        tvFootNote = (TextView) findViewById(R.id.txt_setting_footNote);
        tvFootNote2 = (TextView) findViewById(R.id.txt_setting_footNote_2);
        viewLineBorderBottom = findViewById(R.id.profile_line);
    }

    public void setVisibilityLineBottom(int visibility) {
        if (viewLineBorderBottom != null) {
            viewLineBorderBottom.setVisibility(visibility);
        }
    }

    public void setTvFootNote2(String text, int color) {
        if (tvFootNote2 != null) {
            tvFootNote2.setVisibility(View.VISIBLE);
            tvFootNote2.setText(text);
            tvFootNote2.setBackgroundResource(color);
        }
    }

    public void clearText() {
        if (tvDescription != null)
            tvDescription.setText("");
        if (tvFootNote != null)
            tvFootNote.setText("");
        if (tvFootNote2 != null) {
            tvFootNote2.setText("");
            tvFootNote2.setVisibility(View.GONE);
        }

    }


    @Override
    public String getValueFromSharePref(String value) {
        return value;
    }

    /**
     * set icon button
     */
    public void setIcon(int id) {
        ImageView imageView = (ImageView) findViewById(R.id.ib_setting_icon);
        imageView.setBackgroundResource(id);
    }

    public void setVisibleIcon(int visibleIcon) {
        ImageView imageView = (ImageView) findViewById(R.id.ib_setting_icon);
        imageView.setVisibility(visibleIcon);
    }

    @Override
    public void onItemClick(ItemSettingInterface.ItemSettingListener obItemSettingListener, View view) {
        if (this.getId() == R.id.item_default_currency) {
            if (!TextUtils.isEmpty(mValue)) {
                displayShareDialog(Integer.parseInt(mValue));//except index = 0
            } else {
                displayShareDialog(0);
            }
        }
        /*else if (this.getId() == R.id.item_log_date || this.getId() == R.id.item_log_time) {
            displayShareDialog(0);
        }*/
        else if (this.getId() == R.id.item_landing_page) {
            displayShareDialog(0);
        } else if (listStringDialog != null) {
            displayDialogSelect();
        } else {
            if (!isValueChanged) isValueChanged = true;
            navigationToListSelect();
        }
    }

    private void displayDialogSelect() {
        android.support.v7.app.AlertDialog alertDialog;
        int index = listStringDialog.indexOf(mValue);
        alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mContext, mTvTitle.getText().toString(),
                listStringDialog.toArray(new CharSequence[listStringDialog.size()]), index, new ISequenceAction() {
                    @Override
                    public void sequenceAction(String pName, int pIndex) {
                        mValue = pName;
                        itemId = pIndex;
                        if (iItemInputText != null)
                            iItemInputText.selectValueChange(mValue, ItemInputTextWithIcon.this);
                        if (ItemInputTextWithIcon.this.getId() == R.id.item_night_mode ||
                                ItemInputTextWithIcon.this.getId() == R.id.item_auto_load_task)
                            onCustomUpdateUi(String.valueOf(pIndex), ItemInputTextWithIcon.this);
                        else
                            onCustomUpdateUi(pName, ItemInputTextWithIcon.this);
                        if (!isValueChanged) isValueChanged = true;
                    }
                });
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
    }

    private void displayShareDialog(int index) {
        android.support.v7.app.AlertDialog alertDialog = null;
        if (this.getId() == R.id.item_default_currency) {

            List<ZCurrency> zCurrencyList = DatabaseManager.getInstance(mContext).getAllZCurrency();
            if (listCurrency == null) {
                listCurrency = new ArrayList<>();
                for (int i = 0; i < zCurrencyList.size(); i++) { //except index = 0
                    listCurrency.add(!TextUtils.isEmpty(zCurrencyList.get(i).getCurrShort()) ?
                            zCurrencyList.get(i).getCurrShort() + " - " + zCurrencyList.get(i).getCurrLong() : "");
                }
            }
            alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mContext,
                    mTvTitle.getText().toString(),
                    listCurrency.toArray(new CharSequence[listCurrency.size()]), index, new ISequenceAction() {
                        @Override
                        public void sequenceAction(String pName, int pIndex) {
                            mValue = String.valueOf(pIndex);
                            onCustomUpdateUi(String.valueOf(pIndex), ItemInputTextWithIcon.this);
                        }
                    });
        }
        else if (this.getId() == R.id.item_landing_page) {
            if (listLandingPage == null) {
                listLandingPage = createLandingValue();
            }
            index = listLandingPage.indexOf(mValue);
            if (index < 0) index = 0;
            alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mContext,
                    mTvTitle.getText().toString(),
                    listLandingPage.toArray(new CharSequence[listLandingPage.size()]), index, new ISequenceAction() {
                        @Override
                        public void sequenceAction(String pLandingScreenName, int pIndex) {
                            mValue = pLandingScreenName;
                            onCustomUpdateUi(pLandingScreenName, ItemInputTextWithIcon.this);
                        }
                    });
        }
        if (alertDialog != null) {
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }
    }

    private ArrayList<CharSequence> createLandingValue() {
        ArrayList<CharSequence> entries = new ArrayList<>();
        entries.add("Sync");
        entries.add("Flights");
        entries.add("Pilots");
        entries.add("Airfields");
        entries.add("Totals");
        entries.add("Logbook");
        entries.add("Duty");
        entries.add("Currency");
        entries.add("Weather");
        entries.add("Delay");
        entries.add("Tails");
        return entries;
    }

    private ArrayList<CharSequence> createLogDatesHoursValue() {
        ArrayList<CharSequence> entries = new ArrayList<>();
        entries.add("UTC");
        entries.add("Local");
        entries.add("Base");
        return entries;
    }

    @Override
    public void onCustomUpdateUi(String value, View view) {
        aero.pilotlog.databases.manager.DatabaseManager db = new aero.pilotlog.databases.manager.DatabaseManager(mContext);
        switch (this.getId()) {
            case R.id.item_default_currency:
                ZCurrency zCurrency = db.getZCurrencyByCode(value);
                String zCurShort = zCurrency.getCurrShort();
                //String zCurLong = zCurrency.getCurrLong();
                tvDescription.setText(zCurShort);
                //tvFootNote.setText(zCurLong);
                if (!TextUtils.isEmpty(value)) {
                    db.updateSetting(TAG, value);
                }
                break;
            case R.id.item_auto_load_task:
                tvDescription.setText(value);
                tvFootNote.setVisibility(View.VISIBLE);
                switch (value) {
                    case "0":
                        tvDescription.setText("Off");
                        tvFootNote.setText("Do not auto load Task");
                        db.updateSetting(TAG, "0");
                        break;
                    case "1":
                        tvDescription.setText("Alternate");
                        tvFootNote.setText("Alternate PF / PM (PNF)");
                        db.updateSetting(TAG, "1");
                        break;
                    case "2":
                        tvDescription.setText("PICUS");
                        tvFootNote.setText("PF when PICus time is logged");
                        db.updateSetting(TAG, "2");
                        break;
                    case "3":
                        tvDescription.setText("Always");
                        tvFootNote.setText("Always PF");
                        db.updateSetting(TAG, "3");
                        break;
                    case "Off":
                        tvFootNote.setText("Do not auto load Task");
                        db.updateSetting(TAG, "0");
                        break;
                    case "Alternate":
                        tvFootNote.setText("Alternate PF / PM (PNF)");
                        db.updateSetting(TAG, "1");
                        break;
                    case "PICUS":
                        tvFootNote.setText("PF when PICus time is logged");
                        db.updateSetting(TAG, "2");
                        break;
                    case "Always":
                        tvFootNote.setText("Always PF");
                        db.updateSetting(TAG, "3");
                        break;
                }

                break;
            case R.id.item_night_mode:
                switch (value) {
                    case "0":
                        tvDescription.setText("Off");
                        db.updateSetting(TAG, value);
                        if (iItemInputText != null) {
                            iItemInputText.selectValueChange(value, ItemInputTextWithIcon.this);
                        }
                        break;
                    case "1":
                    case "2":
                        if (iItemInputText == null) {
                            SettingConfig settingConfig = db.getSetting("NightCalc");
                            if (settingConfig != null) {
                                String nightCalcData = settingConfig.getData();
                                if (!TextUtils.isEmpty(nightCalcData)) {

                                    switch (nightCalcData) {
                                        case "0":
                                            SettingConfig settingConfigSRSS = db.getSetting("NightTimeSR");
                                            if (settingConfigSRSS != null) {
                                                String sRSSData = settingConfigSRSS.getData();
                                                if (!TextUtils.isEmpty(sRSSData))
                                                    tvDescription.setText(String.format("Night: %s minutes after SS / before SR", sRSSData));
                                            }
                                            break;
                                        case "1":
                                            SettingConfig settingConfigFrom = db.getSetting("NightTimeFrom");
                                            SettingConfig settingConfigUntil = db.getSetting("NightTimeUntil");
                                            if (settingConfigFrom != null && settingConfigUntil != null) {
                                                String nightFromData = settingConfigFrom.getData();
                                                String nightUtilData = settingConfigUntil.getData();
                                                if (!TextUtils.isEmpty(nightFromData) && !TextUtils.isEmpty(nightUtilData)) {
                                                    tvDescription.setText(String.format("Fixed Hours from %s to %s  incl.", nightFromData, nightUtilData));
                                                }
                                            }
                                            break;
                                        case "2":
                                            tvDescription.setText("Both (SS/SR + fixed Hours)");
                                    }

                                }
                            }
                        } else {
                            iItemInputText.selectValueChange(value, ItemInputTextWithIcon.this);
                        }
                        break;
                    default:
                        //tvDescription.setText("Both (SS/SR + fixed Hours)");
                        if (iItemInputText != null) {
                            iItemInputText.selectValueChange(value, ItemInputTextWithIcon.this);
                        }
                        break;
                }
                break;
            case R.id.item_landing_page:
                if (!TextUtils.isEmpty(value)) {
                    if (listLandingPage == null) {
                        listLandingPage = createLandingValue();
                    }
                    int index = listLandingPage.indexOf(value);
                    if (index < 0)
                        value = listLandingPage.get(1).toString();
                    db.updateSettingLocal(TAG, value);
                    tvDescription.setText(value);
                }
                break;
            case R.id.item_home_base:
                Airfield airfield = db.getAirfieldByICAOIATA(value);
                if (airfield != null) {
                    String iAta = airfield.getAFIATA();
                    if (!TextUtils.isEmpty(iAta))
                        iAta = iAta + "-";
                    tvDescription.setText(String.format("%s%s\n%s", iAta, airfield.getAFICAO(), airfield.getAFName()));
                    ZCountry zCountry = db.getCountryByCode(airfield.getAFCountry());
                    if (zCountry != null)
                        tvFootNote.setText(zCountry.getCountryName());
                }
                break;
            default:
                if (value != null) {
                    tvDescription.setText(value);
                }
                break;
        }
    }

    public void setListStringDialog(ArrayList<CharSequence> listStringDialog, int defaultIndex) {
        this.listStringDialog = listStringDialog;
        if (defaultIndex < 0 || defaultIndex >= listStringDialog.size())
            defaultIndex = -1;
        this.defaultIndex = defaultIndex;
        if (defaultIndex > -1) {
            mValue = listStringDialog.get(defaultIndex).toString();
        } else {
            mValue = "";
        }
        itemId = defaultIndex;
        onCustomUpdateUi(mValue, ItemInputTextWithIcon.this);
    }

    public void navigationToListSelect() {
        if (parentFragment != null && layoutResId != 0 && navigationToFragment != null) {
            if (parentFragment.getTag() == SettingFlightLoggingFragment.class.getName()) {
                Bundle bundle = new Bundle();
                bundle.putInt(MCCPilotLogConst.SCREEN_VIEW_AS_TYPE, MCCPilotLogConst.SELECT_MODE);
                bundle.putString(MCCPilotLogConst.SELECT_LIST_TYPE, MCCPilotLogConst.SELECT_LIST_TYPE_FLIGHT_LOGGING_HOME_BASE);
                parentFragment.replaceFragment(MCCPilotLogConst.sIsTablet?R.id.llRootMainTablet : R.id.fragmentMain, navigationToFragment, bundle, true);
            } else if (navigationToFragment != null && navigationToFragment.getName().equals("aero.pilotlog.fragments.ZExpenseFragment")) {
                Bundle bundle = new Bundle();
                bundle.putInt(MCCPilotLogConst.EXPENSE_GROUP_ID, getItemId());
                parentFragment.replaceFragment(MCCPilotLogConst.sIsTablet? R.id.rightContainerFragment:R.id.fragmentMain, navigationToFragment, bundle, true);
            } else {
                parentFragment.replaceFragment(MCCPilotLogConst.sIsTablet? R.id.rightContainerFragment:R.id.fragmentMain, navigationToFragment, true);
            }
        }else {
            return;
        }
    }


    public void setFootNote(String footNote) {
        tvFootNote.setText(footNote);
        if (!TextUtils.isEmpty(footNote)) {
            tvFootNote.setVisibility(View.VISIBLE);
        } else {
            tvFootNote.setVisibility(View.GONE);
        }

    }
    public void setFootNote(String footNote, int color) {
        tvFootNote.setText(footNote);
        if (!TextUtils.isEmpty(footNote)) {
            tvFootNote.setVisibility(View.VISIBLE);
            tvFootNote.setTextColor(getResources().getColor(color));
        } else {
            tvFootNote.setVisibility(View.GONE);
        }

    }

    public void setVisibleFootNote(int visible) {
        tvFootNote.setVisibility(visible);
    }

    public interface IItemInputText {
        void selectValueChange(String value, ItemInputTextWithIcon itemSwitch);
    }
}
