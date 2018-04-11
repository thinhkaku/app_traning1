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
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.activities.BaseActivity;
import aero.pilotlog.interfaces.ItemSettingInterface;
import aero.pilotlog.utilities.KeyboardUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by binh.pd on 2/4/2015.
 */
public class ItemInputText extends ItemBase {

    private ItemSettingInterface.OnCustomText obOCustomText = null;
    private TextView mTvFootNote;

    public void setIItemInputText(IItemInputText iItemInputText) {
        this.iItemInputText = iItemInputText;
    }

    private IItemInputText iItemInputText;
   public void setTextFootNote(String text){
       if(!TextUtils.isEmpty(text) && mTvFootNote!=null){
           mTvFootNote.setText(text);
           mTvFootNote.setVisibility(View.VISIBLE);
       }
   }



    public String getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    private String typeItem;

    public ItemInputText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreateLayout() {
        inflate(mContext, R.layout.item_setting_base, this);
        mTvFootNote = (TextView)findViewById(R.id.tvFootNote);
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputTextDialog = inflater.inflate(R.layout.dialog_input_text, null);
        final MccEditText inputText = (MccEditText) inputTextDialog.findViewById(R.id.input);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(50);
        //fArray[1] = new InputFilter.AllCaps();
        inputText.setFilters(fArray);
        if (!TextUtils.isEmpty(mTvDescription.getText())) {
            inputText.setText(mTvDescription.getText());
            inputText.setSelection(mTvDescription.getText().length() - 1);
        }
        inputText.selectAll();
        if(iItemInputText!=null){
            iItemInputText.showInputText(inputText, this);
        }
        TextView tvTitle = (TextView) inputTextDialog.findViewById(R.id.message);
        tvTitle.setText(mTvTitle.getText().toString());
        new android.support.v7.app.AlertDialog.Builder(mContext,R.style.MessageDialogTheme)
                .setView(inputTextDialog)
                .setNegativeButton(R.string.sign_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.clearFocus();
                        if(KeyboardUtils.isKeyboardShow((BaseActivity)mContext)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText.clearFocus();
                        mTvDescription.setText(inputText.getText().toString());
                        if(KeyboardUtils.isKeyboardShow((BaseActivity)mContext)){
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
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
        DatePickerDialog datePicker = new DatePickerDialog(mContext,R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
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

    public interface IItemInputText {
        void showInputText(MccEditText editText, ItemInputText itemInputText);
    }
}