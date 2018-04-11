package aero.pilotlog.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ngoc.dh on 7/20/2015.
 * Indexable Listview
 */
public class IndexableListView extends ListView {
    private boolean mIsFastScrollEnabled = false;
    private IndexScroller mScroller = null;
    private GestureDetector mGestureDetector = null;
    //2015/07/29 Begin Tuan.PV add new
    private int mWidth, mWidthOld, mHeight, mHeightOld;
    //2015/07/29 End Tuan.PV add new

    public IndexableListView(Context context) {
        super(context);
    }

    public IndexScroller getScroller() {
        return mScroller;
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        mIsFastScrollEnabled = enabled;
        if (mIsFastScrollEnabled) {
            if (mScroller == null)
                mScroller = new IndexScroller(getContext(), this);
            //2015/07/29 Begin Tuan.PV add new
            mScroller.onSizeChanged(mWidth, mHeight, mWidthOld, mHeightOld);
            //2015/07/29 End Tuan.PV add new
        } else {
            if (mScroller != null) {
                mScroller.hide();
                mScroller = null;
            }
        }
    }

    //2015/08/14 Begin Tuan.PV add new
    private boolean mIsDrawRightBar = false;

    /**
     * Check to draw or not draw right bar on list view
     *
     * @param pFlag =true: draw, = false: not draw
     */
    public void setDrawRightBar(boolean pFlag) {
        this.mIsDrawRightBar = pFlag;
    }
    //2015/08/14 End Tuan.PV add new

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        //2015/08/14 Begin Tuan.PV Edit
//        if (mScroller != null)
//            mScroller.draw(canvas);
        if (mScroller != null && mIsDrawRightBar)
            mScroller.draw(canvas);
        //2015/08/14 End Tuan.PV Edit
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Intercept ListView's touch event
        if (mScroller != null && mScroller.onTouchEvent(ev) && mScroller.getState() != mScroller.STATE_HIDDEN) {
            return true;
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
                    // If fling happens, index bar shows
                    if (mScroller != null) {
                        mScroller.show();
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
        mGestureDetector.onTouchEvent(ev);

        return super.onTouchEvent(ev);
    }

   /* @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mScroller != null && mScroller.contains(ev.getX(), ev.getY()) || super.onInterceptTouchEvent(ev);

    }*/

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //2015/07/29 Begin Tuan.PV add new
        mWidth = w;
        mHeight = h;
        mWidthOld = oldw;
        mHeightOld = oldh;
        //2015/07/29 End Tuan.PV add new
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }
}
