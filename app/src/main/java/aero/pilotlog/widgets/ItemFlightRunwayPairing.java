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

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.interfaces.ItemSettingInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phuc.dd on 3/23/2017.
 */
public class ItemFlightRunwayPairing  extends ItemBase{
    private ItemSettingInterface.OnCustomText obOCustomText = null;


    private EditText inputText;

    public String getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    private String typeItem;

    public ItemFlightRunwayPairing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreateLayout() {
        inflate(mContext, R.layout.item_flight_runway_pairing, this);
    }

    @Override
    public void onCustomUpdateUi(String stringAccount, View view) {
        int minLines;
        if (this.obOCustomText != null) {
            stringAccount = obOCustomText.onCustomText(stringAccount, view);
        }
        mTvDescription.setText(stringAccount);
        minLines = mTypedAttrs.getInt(R.styleable.ItemSetting_minLines, 1);
        mTvDescription.setMinLines(minLines);

    }

    @Override
    public String getValueFromSharePref(String value) {
        return value;
    }

    /**
     * Update text in description
     *
     * @param text text
     */
    public void setText(String text) {
        if (text != null) {
            mTvDescription.setText(text);
        }
    }

    @Override
    public void onItemClick(final ItemSettingInterface.ItemSettingListener obItemSettingListener, final View view) {
        if(mIsEnableClick){
            if(!mIsSelectDate){
                displayTextBox();
            }else {
                displayDateDialog();
            }
        }
    }

    private void displayTextBox() {
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        String titleDialog;
        titleDialog = mTvTitle.getText().toString();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final EditText inputText = (EditText) inputTextDialog.findViewById(R.id.input);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(50);
        //fArray[1] = new InputFilter.AllCaps();
        inputText.setFilters(fArray);
        if (!TextUtils.isEmpty(mTvDescription.getText())) {
            inputText.setText(mTvDescription.getText());
            inputText.setSelection(mTvDescription.getText().length() - 1);
        }
        inputText.selectAll();
        new android.support.v7.app.AlertDialog.Builder(mContext)
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
                        mTvDescription.setText(inputText.getText().toString());
                        if (!TextUtils.isEmpty(TAG)) {
                            aero.pilotlog.databases.manager.DatabaseManager db = new aero.pilotlog.databases.manager.DatabaseManager(mContext);
                            db.updateSetting(TAG, inputText.getText().toString());
                        }
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

    /**
     * set controller custom text
     *
     * @param obOCustomText
     */
    public void setObCustomTextListener(ItemSettingInterface.OnCustomText obOCustomText) {
        this.obOCustomText = obOCustomText;
    }

    public ItemSettingInterface.OnCustomText getObCustomTextListener() {
        return obOCustomText;
    }
}
