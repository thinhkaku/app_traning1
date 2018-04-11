package aero.pilotlog.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ItemSettingInterface;
import aero.pilotlog.utilities.LogUtils;
import aero.pilotlog.utilities.StorageUtils;

/**
 * Created by binh.pd on 1/27/2015.
 * asd
 */
public abstract class ItemBase extends RelativeLayout implements ItemSettingInterface.ItemSettingListener {

    protected String TAG;
    protected String TAG2;
    protected String mValue;        // store old value of this setting save in share pref
    protected TextView mTvTitle;    // title of item

    protected TextView mTvDescription;  // description of item
    protected String[] arrTitle;
    protected String mDefault;          // default value of item
    protected TypedArray mTypedAttrs;   // list attribute of item
    protected Context mContext;
    protected ItemSettingInterface.OnInitDialog onInitDialog;  // Object listener custom create dialog
    protected boolean mIsSelectDate;
    protected boolean mIsShowFootnote;
    protected boolean mIsEnableClick;
    protected int inputType;
    protected int itemId = -1;


    public int getItemId() {
        if (this.getVisibility() == View.VISIBLE)
            return itemId;
        else return -1;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public ItemBase(Context context) {
        super(context);
    }

    @SuppressWarnings("ResourceType")
    public ItemBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        String mTextTitle;
        //String mTextDescription;
        String mTextFootnote;
        mContext = context;
        mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemSetting);

        TAG = mTypedAttrs.getString(0);
        TAG2 = mTypedAttrs.getString(23);
        mDefault = mTypedAttrs.getString(R.styleable.ItemSetting_default_setting);
        mTextTitle = mTypedAttrs.getString(1);
        final String mTextDescription = mTypedAttrs.getString(7);
        mTextFootnote = mTypedAttrs.getString(22);

        mIsSelectDate = mTypedAttrs.getBoolean(18, false);
        mIsShowFootnote = mTypedAttrs.getBoolean(19, true);
        inputType = mTypedAttrs.getInt(17, InputType.TYPE_CLASS_TEXT);
        mIsEnableClick = mTypedAttrs.getBoolean(20, true);
        boolean isVisibleLineEnd = mTypedAttrs.getBoolean(21, true);
        //JIRA PL-741
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!TextUtils.isEmpty(TAG)) {
                                if ("LandingPage".equalsIgnoreCase(TAG)) {
                                    mValue = DatabaseManager.getInstance(mContext).getSettingLocal(TAG).getData();
                                } else {
                                    mValue = DatabaseManager.getInstance(mContext).getSetting(TAG).getData();
                                    if ("TimeMode".equals(TAG) || "DateMode".equalsIgnoreCase(TAG)) {
                                        switch (mValue) {
                                            case "1":
                                                mValue = "UTC";
                                                break;
                                            case "0":
                                                mValue = "Local";
                                                break;
                                            case "-1":
                                                mValue = "Base";
                                                break;
                                            default:
                                                mValue = "UTC";
                                                break;
                                        }
                                    }
                   /* else if("mode_TaskPF".equalsIgnoreCase(TAG)){
                        switch ()
                    }*/
                                }
                            } else {

                            }

                        } catch (Exception e) {
                        }
                        if (!TextUtils.isEmpty(mTextDescription)) {
                            onUpdateDescription(mTextDescription, getRootView());
                        } else if (!TextUtils.isEmpty(mValue))
                            onUpdateDescription(mValue, getRootView());
                        if (ItemBase.this.getId() != R.id.item_aircraft_device
                         && ItemBase.this.getId() != R.id.item_airfield_wiki
                                && ItemBase.this.getId() != R.id.item_airfield_sky_vector
                                && ItemBase.this.getId() != R.id.item_airfield_trip_advisor
                                && ItemBase.this.getId() != R.id.item_airfield_more_weather
                                && ItemBase.this.getId() != R.id.item_airfield_metar_taf
                                && ItemBase.this.getId() != R.id.item_airfield_forecast
                                && ItemBase.this.getId() != R.id.item_airfield_notams
                                && ItemBase.this.getId() != R.id.item_airfield_sunrise
                                && ItemBase.this.getId() != R.id.item_airfield_sunset
                                && ItemBase.this.getId() != R.id.item_airfield_local_time
                                && ItemBase.this.getId() != R.id.item_airfield_timezone
                                && ItemBase.this.getId() != R.id.item_airfield_latitude
                                && ItemBase.this.getId() != R.id.item_airfield_longitude
                                && ItemBase.this.getId() != R.id.item_more_timezone)
                            ItemBase.this.setOnClickListener(onClickListener);
                    }
                });
            }
        });

        //End JIRA PL-741

        //set layout
        onCreateLayout();

        mTvTitle = (TextView) findViewById(R.id.txt_setting_title);
        mTvDescription = (TextView) findViewById(R.id.txt_setting_description);
        // init layout item
        mTvTitle.setText(mTextTitle);

        TextView textViewFootnote = (TextView) findViewById(R.id.txt_setting_footNote);
        if (!mIsShowFootnote && textViewFootnote != null) {
            textViewFootnote.setVisibility(View.GONE);

        }
      /*  if (!TextUtils.isEmpty(mTextFootnote))
            textViewFootnote.setText(mTextFootnote);*/
        if (!isVisibleLineEnd) {
            setVisibleLine(View.GONE);
        }


        System.gc();
    }

    /**
     * set custom layout for item
     */
    public abstract void onCreateLayout();

    /**
     * Get tag of item
     *
     * @return string
     */
    public String getTagItem() {
        return TAG;
    }

    /**
     * set disable click for item
     *
     * @param isDisable isDisable
     */
    public void setEnabledItem(Boolean isDisable) {
        this.setEnabled(isDisable);
//        if (isDisable) {
//            mTvDescription.setTextColor(getResources().getColor(R.color.description_setting_privacy));
//        } else {
//            mTvDescription.setTextColor(getResources().getColor(R.color.description_setting_navigation));
//        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onItemClick(ItemBase.this, v);
        }
    };

    /**
     * update title when initial or user select choice dialog
     *
     * @param value value is user selected
     */
    @Override
    public void onUpdateDescription(String value, View view) {
        LogUtils.e("equal", this.mValue + "|" + value + "|" + view);
        onCustomUpdateUi(value, view);
    }

    /**
     * child item can't do custom action when item base is selected
     */
    abstract public void onCustomUpdateUi(String value, View view);

    /**
     * find index of value in array value
     *
     * @param value    value
     * @param arrValue list value
     * @return index of value
     */
    public int indexOfValue(String value, CharSequence[] arrValue) {
        int size = arrValue.length;
        for (int i = 0; i < size; i++) {
            if (value.equals(arrValue[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Update new title for item
     *
     * @param newTitle new title
     */
    public void setTitle(String newTitle) {
        mTvTitle.setText(newTitle);
    }

    public void setTitle(SpannableStringBuilder newTitle) {
        mTvTitle.setText(newTitle);
    }

    /**
     * Update new description
     * Method should be called when bind descripton from share pref on onCreateView(...)
     *
     * @param newDescription new description
     */
    public void setDescription(String newDescription) {
        mTvDescription.setText(newDescription);
        onUpdateDescription(newDescription, this);
    }

    /**
     * Method should be called when use custom change value in preference and want to update description
     *
     * @param newDescription new description
     */
    public void updateDescription(String newDescription) {
        mTvDescription.setText(newDescription);
    }

    // PL-365

    /**
     * Check value of description is null or empty
     */
    public boolean checkDescriptionIsNullOrEmpty() {
        return mTvDescription.getText() == null || "".equals(mTvDescription.getText());
    }
//end PL-365

    /**
     * reload value from share preference to description
     */
    public void reloadDescription() {
        String newValue = StorageUtils.getStringFromSharedPref(mContext, TAG, MCCPilotLogConst.STRING_EMPTY);
        setDescription(getValueFromSharePref(newValue));
    }

    /**
     * Set listener for use custom create show dialog
     *
     * @param onInitDialog onInitDialog
     */
    public void setOnInitDialogListener(ItemSettingInterface.OnInitDialog onInitDialog) {
        this.onInitDialog = onInitDialog;
    }

    public ItemSettingInterface.OnInitDialog getOnInitDialogListener() {
        return this.onInitDialog;
    }

    /**
     * Get default title which was describe in xml
     *
     * @return cu
     */
    @SuppressWarnings("ResourceType")
    public String getDefaultTitle() {
        String titleDialog = mTypedAttrs.getString(R.styleable.ItemSetting_title_bar);
        titleDialog = !TextUtils.isEmpty(titleDialog) ? titleDialog : mTypedAttrs.getString(1);
        return titleDialog;
    }

    public void setVisibleLine(int visibleLine) {
        View view = findViewById(R.id.profile_line);
        view.setVisibility(visibleLine);
    }

    public String getDescription() {
        if (mTvDescription != null) {
            return mTvDescription.getText().toString();
        } else return "";
    }


    public TextView getTvDescription() {
        return mTvDescription;
    }

    /**
     *
     */
    abstract public String getValueFromSharePref(String value);
}
