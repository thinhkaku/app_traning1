package aero.pilotlog.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import aero.crewlounge.pilotlog.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ngoc.dh on 7/31/2015.
 * Paint utils
 */
public class PaintUtils extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private static final float STROKE_WIDTH = 6;
    private static Canvas mCanvas;
    public boolean mIsCleared = false;
    static Context mContext;
    private Bitmap mBitmap;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private float mX, mY;
    private int mFontWatermark;


    public PaintUtils(Context pContext) {
        super(pContext);
        mContext = pContext;
    }

    public PaintUtils(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PaintUtils(final Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(STROKE_WIDTH);


        //DisplayMetrics metrics = pContext.getResources().getDisplayMetrics();
        //int bitmapWidth;// = metrics.widthPixels;
        //int bitmapHeight;// = metrics.heightPixels;

        final ViewTreeObserver vtObserver = this.getViewTreeObserver();
        vtObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int bitmapWidth = PaintUtils.this.getWidth();
                int bitmapHeight = PaintUtils.this.getHeight();
                mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
                mCanvas = new Canvas(mBitmap);
                mPath = new Path();
                mBitmapPaint = new Paint(Paint.DITHER_FLAG);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    PaintUtils.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    PaintUtils.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                clearCanvas(Calendar.getInstance(), true);
            }
        });

      /*  Display d = ((WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int bitmapWidth = d.getWidth();
        int bitmapHeight = d.getHeight();


        mBitmap = Bitmap.createBitmap(bitmapWidth - 2 * (int) pContext.getResources().getDimension(R.dimen.signature_margin_top) - 4,
                bitmapHeight - (int) pContext.getResources().getDimension(R.dimen.sign_action_bar_height)
                        - (int) pContext.getResources().getDimension(R.dimen.signature_margin_top)
                        - (int) pContext.getResources().getDimension(R.dimen.signature_margin_bottom) - 5, Bitmap.Config.RGB_565);
        //mBitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas pCanvas) {

        pCanvas.drawColor(Color.WHITE);//0xfff

        pCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        pCanvas.drawPath(mPath, mPaint);
    }

    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    /**
     * Handle end up touch
     */
    private void touchUp() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
        // mPath.
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                mIsCleared = false;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void clearCanvas(Calendar pCalendar, boolean isPaintWatermark) {
        Paint paint = new Paint();
        paint.setXfermode(null);
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        if (mCanvas == null) return;

        mCanvas.drawARGB(255, 255, 255, 255);
        if (isPaintWatermark) {
            paintWatermark(paint, pCalendar);
        }else {
            clearPaintWatermark(paint, pCalendar);
        }
        mIsCleared = true;
    }

    private void clearPaintWatermark(Paint pPaint, Calendar pCalendar) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String sDate = sdf.format(pCalendar.getTime()) + "    ";
        pPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER));
        pPaint.setColor(getResources().getColor(R.color.background_white));
        pPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mFontWatermark = mCanvas.getWidth() / 8;
        pPaint.setTextSize(mFontWatermark);

        Rect bounds = new Rect();
        pPaint.getTextBounds(sDate, 0, sDate.length(), bounds);
        int xPos = ((mCanvas.getWidth() / 2) - (bounds.width() / 2));
        int yPos = ((mCanvas.getHeight() / 2) + mFontWatermark / 2);
        mCanvas.drawText(sDate, xPos, yPos, pPaint);
        Rect rect = mCanvas.getClipBounds();
//        rect.bottom -= 50;
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(getResources().getColor(R.color.background_white));
        p.setStrokeWidth(2);
        mCanvas.drawRect(rect, p);
    }

    private void paintWatermark(Paint pPaint, Calendar pCalendar) {
//        if (pSizeMultiplier == 0) {
//            pSizeMultiplier = 1;
//        }
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String sDate = sdf.format(pCalendar.getTime()) + "    ";
        pPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER));
        pPaint.setColor(Color.parseColor("#EEEEEE"));
        pPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mFontWatermark = mCanvas.getWidth() / 8;
        pPaint.setTextSize(mFontWatermark);

        Rect bounds = new Rect();
        pPaint.getTextBounds(sDate, 0, sDate.length(), bounds);
        int xPos = ((mCanvas.getWidth() / 2) - (bounds.width() / 2));
        int yPos = ((mCanvas.getHeight() / 2) + mFontWatermark / 2);
        mCanvas.drawText(sDate, xPos, yPos, pPaint);
        Rect rect = mCanvas.getClipBounds();
//        rect.bottom -= 50;
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(getResources().getColor(R.color.mcc_blue));
        p.setStrokeWidth(2);
        mCanvas.drawRect(rect, p);
    }

    private Bitmap addBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(getResources().getColor(R.color.mcc_cyan));
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

}

