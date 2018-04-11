package aero.pilotlog.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by tuan.pv on 2015/07/13.
 * Keyboard Utils
 */

/**
 * All utils for keyboard.
 */
public class KeyboardUtils {
    /**
     * Hide keyboard.
     *
     * @param activity activity
     */
    private static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null
                && activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            activity.getCurrentFocus().clearFocus();
        }
    }

    /**
     * Hide keyboard when touch outside view.
     *
     * @param activity activity
     * @param view     view root
     */
    public static void hideKeyboard(final Activity activity, View view) {
        if (activity == null) {
            return;
        }
        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText) && !(view instanceof Button)) {
        if (!(view instanceof EditText) && !(view instanceof ScrollView) && !(view instanceof ToggleButton)) {
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity, v);
                    return false;
                }
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            int size = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < size; i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(activity, innerView);
            }
        }
    }

    /**
     * Show keyboard
     *
     * @param view view
     */
    public static void showKeyboard(View view) {
        if (view.isFocused()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showKeyboard(View view, int pShowType) {
        if (view.isFocused()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, pShowType);
    }

    public static void hideKeyboardOnLostFocus(final Activity activity, View v) {
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideSoftKeyboard(activity, v);
                }
            }
        });
    }

    /**
     * Show keyboard after a delay
     *
     * @param view      view
     * @param timeDelay time
     * @return {@link Handler}. Should call
     * {@link Handler#removeCallbacksAndMessages(Object)} with
     * parameter= null when view is destroyed to avoid memory leak.
     */
    public static Handler showDelayKeyboard(final View view, long timeDelay) {
        Handler handler = new Handler();
        if (view == null || view.getContext() == null) {
            return handler;
        }

        if (timeDelay < 0) {
            timeDelay = 0;
        }
        view.requestFocus();
        Runnable delayRunnable = new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) view
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        };
        handler.postDelayed(delayRunnable, timeDelay);
        return handler;
    }

    public static void hideKeyboardOnCustomView(Activity mActivity, View v) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity mActivity) {
        try {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                final View focusView = mActivity.getCurrentFocus();
                if (focusView != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * Hide keyboard when click on image action search keyboard
     *
     * @param pEditText Edit Text
     * @param pActivity Activity
     */
    public static void hideKeyboardWhenClickLoupe(final EditText pEditText, final FragmentActivity pActivity) {
        pEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pEditText.clearFocus();
                    InputMethodManager in = (InputMethodManager) pActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(pEditText.getWindowToken(), 0);

                    return true;
                }

                return false;
            }
        });
    }

    public static void showKeyboard(FragmentActivity activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // force show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static boolean isKeyboardShow(Activity activity) {
        // 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard
        // Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 100 + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
        final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

        final int heightDiff = activity.getWindow().getDecorView().getRootView().getHeight() - (r.bottom - r.top);
        // Threshold size: dp to pixels, multiply with display density /
        return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                final View focusView = activity.getCurrentFocus();
                if (focusView != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                    focusView.clearFocus();
                } else {
                    if (isKeyboardShow(activity)) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
