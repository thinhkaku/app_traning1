package aero.pilotlog.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ItemSettingInterface;
import aero.pilotlog.utilities.StorageUtils;

/**
 * Created by tuan.na on 9/23/2015.
 * Item Flight Config
 */
public class ItemFlightConfigure extends LinearLayout implements View.OnClickListener {

    private static final String STRING_EMPTY = "";
    private ItemSettingInterface.OnCustomText obOCustomText = null;
    protected CheckBox mCheckBox;
    protected TextView mTvFlightConfigureTitle;
    protected TextView mTvFlightConfigureDescription;
    private OnCheckedCB mOnCheckedCB;
    private ImageButton ivImageSettingEdit;
    private Context mContext;
    private String userCaptionDatabaseName = null;

    public String getSaveCaptionKey() {
        return saveCaptionKey;
    }

    public void setSaveCaptionKey(String saveCaptionKey) {
        this.saveCaptionKey = saveCaptionKey;
        String titleSave = StorageUtils.getStringFromSharedPref(mContext, getSaveCaptionKey());
        if (!TextUtils.isEmpty(titleSave)) {
            mTvFlightConfigureTitle.setText(titleSave);
        }
    }

    private String saveCaptionKey;

    public ItemFlightConfigure(Context context) {
        super(context);
        mContext = context;
    }
    @SuppressWarnings("ResourceType")
    public ItemFlightConfigure(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemFlightConfigure);
        inflate(getContext(), R.layout.item_flight_configure, this);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mTvFlightConfigureTitle = (TextView) findViewById(R.id.tv_configure_title);
        mTvFlightConfigureDescription = (TextView) findViewById(R.id.tv_configure_description);
        String title = mTypedAttrs.getString(1);
        String description = mTypedAttrs.getString(2);
        boolean isDisplayCheckbox = mTypedAttrs.getBoolean(3, false);
        String configName = null;
        String mCaption = null;
        if (this.getTag() != null) {
            try {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setBackgroundForCheckboxUnMandatory();
                                setChecked(DatabaseManager.getInstance(mContext).getSetting(ItemFlightConfigure.this.getTag().toString()).getData().equals("1") ? true : false);
                            }
                        });
                    }
                });

            } catch (Exception e) {
            }

        }
        if (!isDisplayCheckbox) {
            mCheckBox.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(configName)) {
            mCaption = getCaption(configName);
        }
        if (!TextUtils.isEmpty(mCaption)) {
            mTvFlightConfigureTitle.setText(mCaption);
        } else {
            mTvFlightConfigureTitle.setText(title);
        }
        mTvFlightConfigureDescription.setText(description);
        ivImageSettingEdit = (ImageButton) findViewById(R.id.image_setting_edit);
        ivImageSettingEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                String titleDialog;
                titleDialog = "Edit Caption";
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
                final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
                InputFilter[] fArray = new InputFilter[2];
                fArray[0] = new InputFilter.LengthFilter(50);
                fArray[1] = new InputFilter.AllCaps();
                inputText.setFilters(fArray);
                if(!TextUtils.isEmpty(mTvFlightConfigureTitle.getText())) {
                    inputText.setText(mTvFlightConfigureTitle.getText());
                    inputText.setSelection(mTvFlightConfigureTitle.getText().length() - 1);
                }
                inputText.selectAll();
                new AlertDialog.Builder(mContext)
                        .setTitle(titleDialog)
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
                                mTvFlightConfigureTitle.setText(inputText.getText().toString());
                                if (!TextUtils.isEmpty(userCaptionDatabaseName)) {
                                    DatabaseManager.getInstance(mContext).updateSetting(userCaptionDatabaseName, inputText.getText().toString());
                                }
                                mCheckBox.setChecked(true);
                                DatabaseManager.getInstance(mContext).updateSetting(ItemFlightConfigure.this.getTag().toString(), "1");
                                dialog.dismiss();
                            }
                        }).show();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        mTypedAttrs.recycle();
    }

    private String getCaption(String configName) {
        String caption = "";
        switch (this.getId()) {
            case R.id.item_configure_user_1:
            case R.id.item_configure_user_2:
            case R.id.item_configure_user_3:
            case R.id.item_configure_user_4:
            case R.id.item_configure_user_text:
            case R.id.item_configure_user_boolean:
            case R.id.item_configure_user_numeric:
                switch (configName) {
                    case "Time_User1":
                        userCaptionDatabaseName = "user_Caption1";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION1).getData();
                        break;
                    case "Time_User2":
                        userCaptionDatabaseName = "user_Caption2";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION2).getData();
                        break;
                    case "Time_User3":
                        userCaptionDatabaseName = "user_Caption3";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION3).getData();
                        break;
                    case "Time_User4":
                        userCaptionDatabaseName = "user_Caption4";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_DEFINE_CAPTION4).getData();
                        break;
                    case "item_UserNum":
                        userCaptionDatabaseName = "user_CaptionNum";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_NUM).getData();
                        break;
                    case "item_UserText":
                        userCaptionDatabaseName = "user_CaptionText";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_TEXT).getData();
                        break;
                    case "item_UserBool":
                        userCaptionDatabaseName = "user_CaptionBool";
                        caption = DatabaseManager.getInstance(mContext).getSetting(MCCPilotLogConst.SETTING_CODE_USER_BOOL).getData();
                        break;
                }
                break;
        }
        return caption;
    }

    public ItemFlightConfigure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDualCheck(OnCheckedCB pOnCheckedCB) {
        this.mOnCheckedCB = pOnCheckedCB;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItemFlightConfigure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void onClick(View v) {
        /*if (mCheckBox == null) {
            return;
        }
        mCheckBox.setChecked(!mCheckBox.isChecked());*/
    }

    public boolean isCheck() {
        return mCheckBox != null && mCheckBox.isChecked();
    }

    public void setChecked(boolean pCheck) {
        if (mCheckBox != null) {
            mCheckBox.setChecked(pCheck);
        }
    }

    public interface OnCheckedCB {
        void dualCheck(ItemFlightConfigure pItem, boolean pIsCheck);
    }

    public void setBackgroundForCheckboxMandatory() {
        if (mCheckBox != null) {
            mCheckBox.setButtonDrawable(R.drawable.btn_checkbox_flight_setup_mandatory);
            mCheckBox.setEnabled(false);
        }
    }

    public void setBackgroundForCheckboxMandatoryUnCheck() {
        if (mCheckBox != null) {
            mCheckBox.setButtonDrawable(R.drawable.ic_uncheck);
            mCheckBox.setEnabled(false);
        }
    }

    public void setBackgroundForCheckboxUnMandatory() {
        if (mCheckBox != null) {
            mCheckBox.setButtonDrawable(R.drawable.btn_checkbox_flight_setup);
            mCheckBox.setEnabled(true);
        }
    }

    public void setVisibilityImageSettingEdit(int visible) {
        ivImageSettingEdit.setVisibility(visible);
    }


}
