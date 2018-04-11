package aero.pilotlog.widgets;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.interfaces.ISequenceAction;
import aero.pilotlog.interfaces.ItemSettingInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phuc.dd on 12/28/2016.
 */
public class ItemSettingList extends ItemBase {
    private ItemSettingInterface.OnCustomText obOCustomText = null;
    private ImageView mIvIconLeft;
    private ImageView mIvIconRight;
    public static final int SHARE_WITH_ALL_USER = 0;
    public static final int SHARE_ONLY_WITH_SAME_COMPANY = 1;
    public static final int SHARE_WITH_SAME_COMPANY_AND_FUNCTION = 2;
    public static final int SHARE_WITH_NO_BODY = 3;
    public boolean canShare = true;
    private int currentShareMode = SHARE_WITH_NO_BODY;
    @SuppressWarnings("ResourceType")
    public ItemSettingList(Context context, AttributeSet attrs) {
        super(context, attrs);
        String mTextDescription = mTypedAttrs.getString(7);
        mTvDescription.setText(mTextDescription);
        mIvIconLeft = (ImageView) findViewById(R.id.iconLeft);
        mIvIconLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canShare) {
                    displayShareDialog(currentShareMode);
                }
            }
        });
    }

    private void displayShareDialog(int shareMode) {
        ArrayList<CharSequence> entries = new ArrayList<>();
        entries.add(getResources().getString(R.string.share_with_all_user));
        entries.add(getResources().getString(R.string.share_only_with_same_company));
        entries.add(getResources().getString(R.string.share_with_same_company_and_function));
        entries.add(getResources().getString(R.string.share_with_nobody));
        android.support.v7.app.AlertDialog alertDialog = new MccDialog().getSingleSelectLisAlertDialog(mContext,
                String.format(getResources().getString(R.string.share_setting_dialog_title), "\"" + mTvTitle.getText() + "\""),
                entries.toArray(new CharSequence[entries.size()]), shareMode, new ISequenceAction() {
                    @Override
                    public void sequenceAction(String pLandingScreenName, int pIndex) {
                        setIconLeft(pIndex);
                    }
                });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void displayTextBox() {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(50);
        fArray[1] = new InputFilter.AllCaps();
        inputText.setFilters(fArray);
        inputText.setInputType(inputType);
        if(!TextUtils.isEmpty(mTvDescription.getText())){
            inputText.setText(mTvDescription.getText());
            inputText.setSelection(mTvDescription.getText().length() - 1);
        }
        inputText.selectAll();
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(mTvTitle.getText().toString());
        new android.support.v7.app.AlertDialog.Builder(mContext,R.style.MessageDialogTheme)
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
                        mTvDescription.setText(inputText.getText().toString());
                        dialog.dismiss();
                    }
                }).show();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    private void displayDateDialog() {
        String currentDateString;
        Date currentDate = null;
        if (!TextUtils.isEmpty(mTvDescription.getText())) {
            currentDateString = mTvDescription.getText().toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                currentDate = simpleDateFormat.parse(currentDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (currentDate == null) currentDate = new Date();
        final Calendar cal = dateToCalendar(currentDate);
        cal.set(Calendar.HOUR, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(mContext, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                mTvDescription.setText(simpleDateFormat.format(cal.getTime()));
            }
        }, year, month, day);
        datePicker.setCancelable(false);
        datePicker.show();
    }

    private static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onCreateLayout() {
        inflate(getContext(), R.layout.item_setting_with_two_icons, this);
    }

    /**
     * method allways is call when item need update
     *
     * @param value value get from share preference
     * @param view  view of item
     */
    @Override
    public void onCustomUpdateUi(String value, View view) {
        int minLines;
        if (this.obOCustomText != null) {
            value = obOCustomText.onCustomText(value, view);
        }
        mTvDescription.setText(value);
        minLines = mTypedAttrs.getInt(R.styleable.ItemSetting_minLines, 1);
        mTvDescription.setMinLines(minLines);
    }

    /**
     * get title of value
     *
     * @param value
     * @return
     */
    @Override
    public String getValueFromSharePref(String value) {
        return value;
    }


    @Override
    public void onItemClick(ItemSettingInterface.ItemSettingListener obItemSettingListener, View view) {
        if(!mIsSelectDate){
            displayTextBox();
        }else {
            displayDateDialog();
        }
    }

    public void setIconLeft(int shareMode) {
        int idResource = R.drawable.ic_icon_locked;
        switch (shareMode) {
            case SHARE_WITH_ALL_USER:
                idResource = R.drawable.ic_icon_unlocked;
                break;
            case SHARE_ONLY_WITH_SAME_COMPANY:
                idResource = R.drawable.ic_icon_locked_company;
                break;
            case SHARE_WITH_SAME_COMPANY_AND_FUNCTION:
                idResource = R.drawable.ic_icon_locked_company_function;
                break;
            case SHARE_WITH_NO_BODY:
                idResource = R.drawable.ic_icon_locked;
                break;
            default:
                break;
        }
        currentShareMode = shareMode;
        ImageView imageView = (ImageView) findViewById(R.id.iconLeft);
        imageView.setImageResource(idResource);
        imageView.requestLayout();

    }

    public void setIconRight(int id) {
        ImageView imageView = (ImageView) findViewById(R.id.iconRight);
        imageView.setImageResource(id);
        imageView.requestLayout();
    }

    public void setVisibleIconRight(int visible) {
        ImageView imageView = (ImageView) findViewById(R.id.iconRight);
        imageView.setVisibility(visible);
    }

    public void setVisibleLineEnd(int visible){
        View settingLine = findViewById(R.id.setting_line);
        settingLine.setVisibility(visible);
    }
}
