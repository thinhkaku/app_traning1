package aero.pilotlog.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import aero.crewlounge.pilotlog.R;

/**
 * Created by tuan.pv on 25/8/2015.
 */

public class PopupSelectionMenu {

    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private OnClickMenuListener onClickMenuListenerOb;
    private PopupWindow mPopupWindow;
    private final Context context;

    public PopupSelectionMenu(Context context) {
        this.context = context;
        initPopupWindow(R.style.PopupAnimation);

    }

    /**
     * init popup
     * @param animationStyle
     */
    private void initPopupWindow(int animationStyle) {
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(animationStyle);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                } else return false;
            }
        });
    }

    /**
     * show popup window with content layout is @rootLayout above anchor
     * @param rootLayout
     * @param anchor
     */
    public void show(LinearLayout rootLayout, View anchor) {
        try {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);

            Rect anchorRect = new Rect(location[X_INDEX], location[Y_INDEX],
                    location[X_INDEX] + anchor.getWidth(), location[Y_INDEX] + anchor.getHeight());
            if (rootLayout.getLayoutParams() == null) {
                rootLayout.setLayoutParams(
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            rootLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // set click listener
            setOnClick(rootLayout);
            // calculate position y coordinates right top of popup window
            int x, y;
            x = 0;
            y = anchorRect.top - rootLayout.getMeasuredHeight();
            mPopupWindow.setWidth(anchor.getMeasuredWidth());
            mPopupWindow.setContentView(rootLayout);
            mPopupWindow.dismiss();
//            mPopupWindow.setAnimationStyle(R.style.AnimationPopup);
            mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handler click event when touch each child item
     * @param rootLayout
     */
    private void setOnClick(LinearLayout rootLayout) {
        for (int i = 0; i < rootLayout.getChildCount(); i ++) {
            final int position = i;
            View view = rootLayout.getChildAt(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickMenuListenerOb != null) {
                        onClickMenuListenerOb.onClickMenu(position);
                        mPopupWindow.dismiss();
                    }
                }
            });
        }
    }

    public OnClickMenuListener getOnClickMenuListenerOb() {
        return onClickMenuListenerOb;
    }

    public void setOnClickMenuListenerOb(OnClickMenuListener onClickMenuListenerOb) {
        this.onClickMenuListenerOb = onClickMenuListenerOb;
    }

    public interface OnClickMenuListener {
        public void onClickMenu(int position);
    }

    public void dissmissPopup() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing(){
        return mPopupWindow != null && mPopupWindow.isShowing();
    }
}
