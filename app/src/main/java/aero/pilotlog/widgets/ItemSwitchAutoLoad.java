package aero.pilotlog.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.InputFilterMinMax;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ItemSettingInterface;
import aero.pilotlog.utilities.StorageUtils;

/**
 * Created by phuc.dd on 3/29/2017.
 */
public class ItemSwitchAutoLoad extends ItemBase {
    public Switch mSwitch;
    private LinearLayout lnFootnote;
    //TextView tvFootNote;
    private AppCompatCheckBox checkBoxOnlyPF;
    DatabaseManager databaseManager;
    boolean isFirst;
    private TextView mTvFootNote;
    public void setTextFootNote(String text){
        if(!TextUtils.isEmpty(text) && mTvFootNote!=null){
            mTvFootNote.setText(text);
            mTvFootNote.setVisibility(View.VISIBLE);
        }
    }

    public ItemSwitchAutoLoad(Context context) {
        super(context);
    }

    public ItemSwitchAutoLoad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onCreateLayout() {
        inflate(getContext(), R.layout.item_switch_auto_load, this);
        databaseManager = DatabaseManager.getInstance(mContext);
        mSwitch = (Switch) findViewById(R.id.switch_setting_description);
        lnFootnote = (LinearLayout) findViewById(R.id.ln_footnote);
        mTvFootNote = (TextView)findViewById(R.id.tvFootNote);
        //tvFootNote = (TextView) findViewById(R.id.txt_setting_footNote);
        checkBoxOnlyPF = (AppCompatCheckBox) findViewById(R.id.cb_only_PF);
        String mSwitchTextOn = mTypedAttrs.getString(R.styleable.ItemSetting_switchTextOn);
        String mSwitchTextOff = mTypedAttrs.getString(R.styleable.ItemSetting_switchTextOff);
        if (mSwitchTextOn != null && mSwitchTextOff != null) {
            mSwitch.setTextOn(mSwitchTextOn);
            mSwitch.setTextOff(mSwitchTextOff);
        }

        String textFootNote = mTypedAttrs.getString(22);
        if (!TextUtils.isEmpty(textFootNote) && checkBoxOnlyPF != null) {
            checkBoxOnlyPF.setText(textFootNote);
        }
        // set listener when user change state switch button
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (isFirst) {
                        switch (getId()) {
                            case R.id.item_fuel_co:
                                displayTextBox(mTvTitle.getText().toString(), 1, 999999999, 9);
                                lnFootnote.setVisibility(View.GONE);
                                break;
                            default:
                                lnFootnote.setVisibility(View.VISIBLE);
                                displayTextBox("% FROM TOTAL TIME", 1, 100, 3);
                                break;
                        }
                    }

                } else {
                    lnFootnote.setVisibility(View.GONE);
                    mValue = "0";
                    onCustomUpdateUi(mValue, ItemSwitchAutoLoad.this);
                }
                isFirst = true;
            }
        });
        checkBoxOnlyPF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (getId()) {
                    case R.id.item_auto_load_relief:
                        if (!TextUtils.isEmpty(TAG2)) {
                            if (b) {
                                databaseManager.updateSetting(TAG2, "1");
                            } else {
                                databaseManager.updateSetting(TAG2, "0");
                            }
                        }
                        break;
                    default:
                        if (b) {
                            StorageUtils.writeIntToSharedPref(mContext, String.valueOf(ItemSwitchAutoLoad.this.getId()), 0);
                            databaseManager.updateSetting(TAG, "-" + mValue.toString());
                        } else {
                            StorageUtils.writeIntToSharedPref(mContext, String.valueOf(ItemSwitchAutoLoad.this.getId()), 1);
                            databaseManager.updateSetting(TAG, mValue.toString());
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onCustomUpdateUi(String value, View view) {
        if (!TextUtils.isEmpty(value)) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue != 0) {
                    mValue = value.replace("-", "");
                    mSwitch.setText(mValue + "% from Total Time");
                    mSwitch.setChecked(true);
                    lnFootnote.setVisibility(View.VISIBLE);
                    switch (getId()) {
                        case R.id.item_auto_load_relief:
                            databaseManager.updateSetting(TAG, mValue.toString());
                            if (!TextUtils.isEmpty(TAG2)) {
                                SettingConfig settingConfig = databaseManager.getSetting(TAG2);
                                if (settingConfig != null) {
                                    checkBoxOnlyPF.setChecked(settingConfig.getData().equals("0") ? false : true);
                                }
                            }
                            break;
                        case R.id.item_fuel_co:
                            lnFootnote.setVisibility(View.GONE);
                            mSwitch.setText(mValue);
                            mSwitch.setChecked(true);
                            databaseManager.updateSetting(TAG, mValue);
                        default:
                            if (intValue > 0) {
                                checkBoxOnlyPF.setChecked(false);
                                StorageUtils.writeIntToSharedPref(mContext, String.valueOf(ItemSwitchAutoLoad.this.getId()), 1);
                                databaseManager.updateSetting(TAG, mValue.toString());
                            } else if (intValue < 0) {
                                checkBoxOnlyPF.setChecked(true);
                                StorageUtils.writeIntToSharedPref(mContext, String.valueOf(ItemSwitchAutoLoad.this.getId()), 0);
                                databaseManager.updateSetting(TAG, "-" + mValue.toString());
                            }
                            break;
                    }

                } else if (getId() == R.id.item_fuel_co) {
                    mSwitch.setText("Auto");
                    mSwitch.setChecked(false);
                    databaseManager.updateSetting(TAG, "0");
                } else {
                    mSwitch.setText("Off");
                    mSwitch.setChecked(false);
                    databaseManager.updateSetting(TAG, "0");
                }
                isFirst = true;

            } catch (Exception ex) {
            }
            //break;
            //}
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

    private void displayTextBox(String title, int minLength, int maxLength, int lengthFilter) {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(lengthFilter);
        fArray[1] = new InputFilterMinMax(minLength, maxLength);
        inputText.setFilters(fArray);
        if (!TextUtils.isEmpty(mSwitch.getText())) {
            SettingConfig settingConfig = databaseManager.getSetting(TAG);
            if (settingConfig != null) {
                String percent = settingConfig.getData();
                inputText.setText(percent);
            }
        }
        inputText.selectAll();
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(title);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext,R.style.MessageDialogTheme);
        builder
                //.setTitle(titleDialog)
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
                        mSwitch.setText(inputText.getText().toString() + "% from Total Time");
                        if (!TextUtils.isEmpty(TAG)) {
                            int onlyPF = StorageUtils.getIntFromSharedPref(mContext, String.valueOf(ItemSwitchAutoLoad.this.getId()), 0);
                            if (onlyPF == 0) {
                                onCustomUpdateUi("-" + inputText.getText().toString(), ItemSwitchAutoLoad.this);
                            } else {
                                onCustomUpdateUi(inputText.getText().toString(), ItemSwitchAutoLoad.this);
                            }

                        }
                        dialog.dismiss();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (TextUtils.isEmpty(inputText.getText().toString())) {
                    onCustomUpdateUi("0", ItemSwitchAutoLoad.this);
                }
            }
        });
        alertDialog.show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
