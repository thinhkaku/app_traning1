package aero.pilotlog.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ItemSettingInterface;

/**
 * Created by tuan.na on 8/11/2015.
 * Item Switch
 */
public class ItemSwitch extends ItemBase {

    private final static String ON = "1";
    private final static String OFF = "0";
    private final static String IATA = "IATA";
    private final static String ICAO = "ICAO";

    public boolean isChecked() {
        return check;
    }

    private boolean check = false;
    IItemSwitch iItemSwitch;

    public void setIItemSwitch(IItemSwitch iItemSwitch) {
        this.iItemSwitch = iItemSwitch;
    }

    public Switch getSwitch() {
        return mSwitch;
    }

    private Switch mSwitch;

    private TextView tvFootnote;

    public ItemSwitch(Context context) {
        super(context);
    }

    public ItemSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreateLayout() {
        inflate(getContext(), R.layout.item_setting_switch, this);
        mSwitch = (Switch) findViewById(R.id.switch_setting_description);
        tvFootnote = (TextView) findViewById(R.id.txt_setting_footNote);

        String mSwitchTextOn = mTypedAttrs.getString(R.styleable.ItemSetting_switchTextOn);
        String mSwitchTextOff = mTypedAttrs.getString(R.styleable.ItemSetting_switchTextOff);
        if (mSwitchTextOn != null && mSwitchTextOff != null) {
            mSwitch.setTextOn(mSwitchTextOn);
            mSwitch.setTextOff(mSwitchTextOff);
        }

        // set listener when user change state switch button
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (iItemSwitch != null) {
                    iItemSwitch.switchChange(isChecked, ItemSwitch.this);
                }
                check = isChecked;
                if (isChecked) {
                    itemId = 1;
                } else {
                    itemId = 0;
                }
                //onUpdateDescription(isChecked ? "1" : "0", ItemSwitch.this);
                DatabaseManager db = new DatabaseManager(mContext);
                switch (ItemSwitch.this.getId()) {
                    case R.id.item_aircraft_logbook:
                        SettingConfig settingConfigRun2Name1 = db.getSetting("Name1");
                        SettingConfig settingConfigRun2Name2 = db.getSetting("Name2");
                        if (settingConfigRun2Name1 != null && settingConfigRun2Name2 != null) {
                            String name1 = settingConfigRun2Name1.getData();
                            String name2 = settingConfigRun2Name2.getData();
                            if (!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(name2)) {
                                if (isChecked) {
                                    mSwitch.setChecked(true);
                                    onUpdateDescription(name2, ItemSwitch.this);
                                } else {
                                    mSwitch.setChecked(false);
                                    onUpdateDescription(name1, ItemSwitch.this);
                                }
                            }
                        }
                        break;
                  /*  case R.id.item_airfield_identifier:
                        onUpdateDescription(isChecked ? "1" : "0", ItemSwitch.this);
                        db.updateSetting(TAG, isChecked ? "1" : "0");
                        break;*/


                    /*case R.id.item_do_not_preserve_accuracy:
                        StorageUtils.writeIntToSharedPref(mContext, MCCPilotLogConst.PRESERVE_ACCURACY, isChecked ? 1 : 0);
                        onUpdateDescription(isChecked ? "2" : "1", ItemSwitch.this);
                        db.updateSetting(TAG, isChecked ? "2" : "1");
                        break;
                    case R.id.item_log_hours_flight_decimal:
                        int preserveAccuracy = StorageUtils.getIntFromSharedPref(mContext, MCCPilotLogConst.PRESERVE_ACCURACY, 0);
                        if (preserveAccuracy == 0) {
                            onUpdateDescription(isChecked ? "1" : "0", ItemSwitch.this);
                            db.updateSetting(TAG, isChecked ? "1" : "0");
                        } else {
                            onUpdateDescription(isChecked ? "2" : "0", ItemSwitch.this);
                            db.updateSetting(TAG, isChecked ? "2" : "0");
                        }
                        break;*/
                    case R.id.item_night_mode_do_not_suggest_ldg:
                        onUpdateDescription(isChecked ? "2" : "1", ItemSwitch.this);
                        if (!TextUtils.isEmpty(TAG)) {
                            db.updateSetting(TAG, isChecked ? "2" : "1");
                        }
                        break;
                    default:
                        onUpdateDescription(isChecked ? "1" : "0", ItemSwitch.this);
                        if (!TextUtils.isEmpty(TAG)) {
                            db.updateSetting(TAG, isChecked ? "1" : "0");
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onCustomUpdateUi(String value, View view) {
        switch (view.getId()) {
            case R.id.item_aircraft_logbook:
                mSwitch.setText(value);
                tvFootnote.setVisibility(View.VISIBLE);
                if (mSwitch.isChecked()) {
                    tvFootnote.setText("Show this Aircraft in Logbook 2");
                } else {
                    tvFootnote.setText("Show this Aircraft in Logbook 1");
                }
                break;
            /*case R.id.item_airfield_identifier:
                if ("1".equals(value)) {
                    mSwitch.setText(IATA);
                    mSwitch.setChecked(true);
                } else {
                    mSwitch.setText(ICAO);
                    mSwitch.setChecked(false);
                }
                break;*/


           /* case R.id.item_do_not_preserve_accuracy:
                if ("2".equals(value)) {
                    mSwitch.setText("On");
                    mSwitch.setChecked(true);
                } else {
                    int preserveAccuracy = StorageUtils.getIntFromSharedPref(mContext, MCCPilotLogConst.PRESERVE_ACCURACY, 0);
                    if (preserveAccuracy == 0) {
                        mSwitch.setText("Off");
                        mSwitch.setChecked(false);
                    } else {
                        mSwitch.setText("On");
                        mSwitch.setChecked(true);
                    }
                }
                break;*/
            case R.id.item_fuel_unit:
                if ("1".equals(value)) {
                    mSwitch.setText("kg");
                    mSwitch.setChecked(true);
                } else {
                    mSwitch.setText("lbs");
                    mSwitch.setChecked(false);
                }
                break;
            case R.id.item_night_mode_do_not_suggest_ldg:
                if ("2".equals(value)) {
                    mSwitch.setText("On");
                    mSwitch.setChecked(true);
                } else {
                    mSwitch.setText("Off");
                    mSwitch.setChecked(false);
                }
                break;
            default:
                if ("1".equals(value) || "2".equals(value)) {
                    mSwitch.setText("On");
                    mSwitch.setChecked(true);
                } else {
                    mSwitch.setText("Off");
                    mSwitch.setChecked(false);
                }
                break;
        }
    }

    @Override
    public String getValueFromSharePref(String value) {
        return null;
    }

    @Override
    public void onItemClick(ItemSettingInterface.ItemSettingListener obItemSettingListener, View view) {

    }

    public void setChecked(boolean value) {
        mSwitch.setChecked(value);
    }

    public boolean getChecked() {
        return mSwitch.isChecked();
    }

    public void setFootNote(String text) {
        if(!TextUtils.isEmpty(text) && tvFootnote!=null){
            tvFootnote.setText(text);
            tvFootnote.setVisibility(View.VISIBLE);
        }

    }

    public void setVisibleFootNote(int visibility) {
        tvFootnote.setVisibility(visibility);
    }

    public interface IItemSwitch {
        void switchChange(boolean change, ItemSwitch itemSwitch);
    }

}
