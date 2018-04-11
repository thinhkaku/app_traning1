package aero.pilotlog.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;

/**
 * Created by phuc.dd on 9/12/2017.
 */

public class MccSwitch extends RelativeLayout {
    private String mTitle;
    private String mDescriptionLeft, mDescriptionRight, mDescriptionCenter;
    private String mFootNoteLeft;
    private String mFootNoteRight, mFootNoteCenter;
    private String mTextLeft, mTextCenter, mTextRight;
    private AppCompatRadioButton radioButtonLeft;
    private AppCompatRadioButton radioButtonCenter;
    private AppCompatRadioButton radioButtonRight;
    private TextView mTvTitle;
    private TextView mTvDescription;
    private TextView mTvFootNote;
    private int mSettingCode;
    private boolean isChecked, isVisibleRadioButtonCenter;
    private TypedArray mTypedAttrs;   // list attribute of item
    private Context mContext;

    public MccSwitch(Context context) {
        super(context);
    }

    @SuppressWarnings("ResourceType")
    public MccSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.MccItemSwitch);
        mTitle = mTypedAttrs.getString(0);
        mDescriptionLeft = mTypedAttrs.getString(1);
        mDescriptionCenter = mTypedAttrs.getString(2);
        mDescriptionRight = mTypedAttrs.getString(3);
        mFootNoteLeft = mTypedAttrs.getString(4);
        mFootNoteCenter = mTypedAttrs.getString(5);
        mFootNoteRight = mTypedAttrs.getString(6);
        mTextLeft = mTypedAttrs.getString(7);
        mTextCenter = mTypedAttrs.getString(8);
        mTextRight = mTypedAttrs.getString(9);
        mSettingCode = mTypedAttrs.getInt(10, 0);
        onCreateLayout();
        System.gc();
    }

    public void onCreateLayout() {
        inflate(getContext(), R.layout.mcc_item_switch, this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvDescription = (TextView) findViewById(R.id.tvDescription);
        mTvFootNote = (TextView) findViewById(R.id.tvFootNote);
        mTvTitle.setText(mTitle);
        radioButtonLeft = (AppCompatRadioButton) findViewById(R.id.rbLeft);
        radioButtonCenter = (AppCompatRadioButton) findViewById(R.id.rbCenter);
        radioButtonRight = (AppCompatRadioButton) findViewById(R.id.rbRight);
        radioButtonLeft.setText(mTextLeft);
        if (!TextUtils.isEmpty(mTextCenter)) {
            radioButtonCenter.setText(mTextCenter);
            radioButtonCenter.setVisibility(View.VISIBLE);
            isVisibleRadioButtonCenter = true;
        }
        radioButtonRight.setText(mTextRight);
        setSameWidth();
        setOnClick();
        if (mSettingCode != 0 &&
                mSettingCode != MCCPilotLogConst.SETTING_CODE_DATE_MODE &&
                mSettingCode != MCCPilotLogConst.SETTING_CODE_TIME_MODE) {
            String settingData = DatabaseManager.getInstance(mContext).getSetting(mSettingCode).getData();
            if (!TextUtils.isEmpty(settingData)) {
                if (settingData.equals("0")) {
                    radioButtonLeft.setChecked(true);
                    radioButtonLeft.setTextColor(getResources().getColor(R.color.background_white));
                    setTextDescription(mDescriptionLeft);
                    setTextFootNote(mFootNoteLeft);
                } else {
                    isChecked = true;
                    radioButtonRight.setChecked(true);
                    radioButtonRight.setTextColor(getResources().getColor(R.color.background_white));
                    setTextDescription(mDescriptionRight);
                    setTextFootNote(mFootNoteRight);
                }
            }
        } else if (mSettingCode != MCCPilotLogConst.SETTING_CODE_DATE_MODE ||
                mSettingCode != MCCPilotLogConst.SETTING_CODE_TIME_MODE) {
            String settingData = DatabaseManager.getInstance(mContext).getSetting(mSettingCode).getData();
            if (settingData.equals("0")) {//center: local
                radioButtonCenter.setChecked(true);
                radioButtonCenter.setTextColor(getResources().getColor(R.color.background_white));
                setTextDescription(mDescriptionCenter);
                setTextFootNote(mFootNoteCenter);
            } else if (settingData.equals("1")) {//left: utc
                radioButtonLeft.setChecked(true);
                radioButtonLeft.setTextColor(getResources().getColor(R.color.background_white));
                setTextDescription(mDescriptionLeft);
                setTextFootNote(mFootNoteLeft);
            } else {
                radioButtonRight.setChecked(true);
                radioButtonRight.setTextColor(getResources().getColor(R.color.background_white));
                setTextDescription(mDescriptionRight);
                setTextFootNote(mFootNoteRight);
            }
        }
    }

    public void setTextDescription(String text) {
        //if (!TextUtils.isEmpty(text)) {
        mTvDescription.setText(text);
        mTvDescription.setVisibility(View.VISIBLE);
        //}
    }

    public void setTextFootNote(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvFootNote.setText(text);
            mTvFootNote.setVisibility(View.VISIBLE);
        }
    }

    private void updateSetting(String data) {
        if (mSettingCode != 0) {
            DatabaseManager.getInstance(mContext).updateSetting(mSettingCode, data);
        }
    }

    private void setOnClick() {
        radioButtonLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioButtonLeft.setTextColor(getResources().getColor(R.color.background_white));
                    if (mSettingCode == MCCPilotLogConst.SETTING_CODE_DATE_MODE ||
                            mSettingCode == MCCPilotLogConst.SETTING_CODE_TIME_MODE){
                        updateSetting("1");
                    }else {
                        updateSetting("0");
                    }
                    setTextDescription(mDescriptionLeft);
                    setTextFootNote(mFootNoteLeft);
                    isChecked = false;
                } else {
                    isChecked = true;
                    radioButtonLeft.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
        radioButtonCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radioButtonCenter.setTextColor(getResources().getColor(R.color.background_white));
                    if (mSettingCode == MCCPilotLogConst.SETTING_CODE_DATE_MODE ||
                            mSettingCode == MCCPilotLogConst.SETTING_CODE_TIME_MODE){
                        updateSetting("0");
                    }
                    setTextDescription(mDescriptionCenter);
                    setTextFootNote(mFootNoteCenter);
                } else {
                    radioButtonCenter.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
        radioButtonRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (mSettingCode == MCCPilotLogConst.SETTING_CODE_DATE_MODE ||
                            mSettingCode == MCCPilotLogConst.SETTING_CODE_TIME_MODE){
                        updateSetting("-1");
                    }else if(mSettingCode==MCCPilotLogConst.SETTING_CODE_ACCURACY){
                        String logDecimal = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_IS_LOG_DECIMAL).getData();
                        if(logDecimal.equals("0")){
                            updateSetting("1");
                        }else {
                            updateSetting("2");
                        }
                    }else {
                        updateSetting("1");
                    }
                    radioButtonRight.setTextColor(getResources().getColor(R.color.background_white));
                    setTextDescription(mDescriptionRight);
                    setTextFootNote(mFootNoteRight);
                } else {
                    radioButtonRight.setTextColor(getResources().getColor(R.color.mcc_cyan));
                }
            }
        });
    }

    private void setSameWidth() {
        int maxWidth;
        radioButtonLeft.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        radioButtonCenter.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        radioButtonRight.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        maxWidth = radioButtonLeft.getMeasuredWidth();
        maxWidth = maxWidth > radioButtonCenter.getMeasuredWidth() ? maxWidth : radioButtonCenter.getMeasuredWidth();
        maxWidth = maxWidth > radioButtonRight.getMeasuredWidth() ? maxWidth : radioButtonRight.getMeasuredWidth();
        Log.d("max width", String.valueOf(maxWidth));
        radioButtonLeft.setWidth(maxWidth);
        radioButtonCenter.setWidth(maxWidth);
        radioButtonRight.setWidth(maxWidth);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void setFootNoteLeft(String mFootNoteLeft) {
        this.mFootNoteLeft = mFootNoteLeft;
    }

    public void setFootNoteRight(String mFootNoteRight) {
        this.mFootNoteRight = mFootNoteRight;
    }

    public AppCompatRadioButton getRadioButtonLeft() {
        return radioButtonLeft;
    }

    public AppCompatRadioButton getRadioButtonRight() {
        return radioButtonRight;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
