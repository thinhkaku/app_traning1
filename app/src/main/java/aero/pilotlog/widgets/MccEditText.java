package aero.pilotlog.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MccEnum;

/**
 * Created by tuan.na on 7/13/2015.
 */
public class MccEditText extends EditText implements TextView.OnEditorActionListener{
    public boolean isFlag = true;
    public MccEnum.topKeyboardCustomInput getInputTopKeyType() {
        return inputTopKeyType;
    }

    public void setInputTopKeyType(MccEnum.topKeyboardCustomInput inputTopKeyType) {
        this.inputTopKeyType = inputTopKeyType;
    }

    public MccEnum.topKeyboardCustomInput inputTopKeyType = MccEnum.topKeyboardCustomInput.NONE;

    private EditTextImeBackListener mOnImeBack;

    public MccEditText(Context context) {
        super(context);
        this.setOnEditorActionListener(this);
        this.setHighlightColor(context.getResources().getColor(
                R.color.gray_hLight));
    }

    public MccEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnEditorActionListener(this);
        this.setHighlightColor(context.getResources().getColor(
                R.color.gray_hLight));
    }

    public MccEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnEditorActionListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MccEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setOnEditorActionListener(this);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
        super.dispatchKeyEvent(event);
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) && mOnImeBack != null) {
            mOnImeBack.onHideKeyboard(this);
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            this.clearFocus();
            InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
        return false;
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE && mOnImeBack != null) {
            mOnImeBack.onHideKeyboard(this);
        }
        return false;
    }

    public interface EditTextImeBackListener {
        void onHideKeyboard(MccEditText pEditText);
    }


}
