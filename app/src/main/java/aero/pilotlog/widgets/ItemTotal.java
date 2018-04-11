package aero.pilotlog.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;

/**
 * Created by tuan.pv on 2015/08/17.
 * Item Total
 */
public class ItemTotal extends LinearLayout {

    protected TextView mTvTitle1, mTvValue1;//, mTvTitle2, mTvTitle3, , mTvValue2, mTvValue3;
    //protected LinearLayout lnRowTotal3;
    //protected View mViewTotal3;
    protected View mLineBorder;
    protected Context mContext;

    public ItemTotal(Context context) {
        super(context);
    }
    @SuppressWarnings("ResourceType")
    public ItemTotal(Context context, AttributeSet attrs) {
        super(context, attrs);
        String title1;//, title2, title3;
        boolean isHasLineBorder;//, isHasLine3, isHasLine2;
        mContext = context;
        TypedArray mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemTotal);
        title1 = mTypedAttrs.getString(0);
        isHasLineBorder = mTypedAttrs.getBoolean(1, true); //default always has border

        inflate(getContext(), R.layout.row_item_total, this);

        mTvTitle1 = (TextView) findViewById(R.id.tvTitle1);
        mTvValue1 = (TextView) findViewById(R.id.tvValue1);
        mLineBorder = findViewById(R.id.viewLineTotals);

        mTvTitle1.setText(title1);
        mLineBorder.setVisibility(isHasLineBorder ? VISIBLE : GONE);

        /*Logic for edit text to be self-restored its value */
        mTvTitle1.setId(this.getId() + 1000);
        mTvValue1.setId(this.getId() + 1001);
        mTypedAttrs.recycle();
    }

    public ItemTotal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextTitle(String pValue1) {
        if (mTvTitle1 != null) {
            mTvTitle1.setText(pValue1);
        }
    }


    public void setTextValue(String pValue1) {
        if (mTvValue1 != null) {
            mTvValue1.setText(pValue1);
        }
    }
    public void setTextValueBold(String pValue1) {
        if (mTvValue1 != null) {
            mTvValue1.setText(pValue1);
            mTvValue1.setTypeface(mTvValue1.getTypeface(),Typeface.BOLD);
        }
    }

    public void setTextValue(String pValue1, int pColor, float pFontSize) {
        if (mTvValue1 != null) {
            mTvValue1.setTextColor(getResources().getColor(pColor));
            mTvValue1.setText(pValue1);
            mTvValue1.setTextSize(TypedValue.COMPLEX_UNIT_PX, pFontSize);
        }
    }
    public void setTextValue(String pValue1, int pColor) {
        if (mTvValue1 != null) {
            mTvValue1.setTextColor(getResources().getColor(pColor));
            mTvValue1.setText(pValue1);
        }
    }

   /* public void setTextValue2(String pValue2) {
        if (mTvValue2 != null) {
            mTvValue2.setText(pValue2);
        }
    }

    public void setTextValue2(String pValue2, int pColor, float pFontSize) {
        if (mTvValue2 != null) {
            mTvValue2.setTextColor(getResources().getColor(pColor));
            mTvValue2.setText(pValue2);
            mTvValue2.setTextSize(TypedValue.COMPLEX_UNIT_PX, pFontSize);
        }
    }
    public void setTextValue2(String pValue2, int pColor) {
        if (mTvValue2 != null) {
            mTvValue2.setTextColor(getResources().getColor(pColor));
            mTvValue2.setText(pValue2);
        }
    }

    public void setTextValue3(String pValue3) {
        if (mTvValue3 != null) {
            mTvValue3.setText(pValue3);
        }
    }

    public void setTextValue3(String pValue3, int pColor, float pFontSize) {
        if (mTvValue3 != null) {
            mTvValue3.setTextColor(getResources().getColor(pColor));
            mTvValue3.setText(pValue3);
            mTvValue3.setTextSize(TypedValue.COMPLEX_UNIT_PX, pFontSize);
        }
    }*/

    public void goneTitle() {
        mTvTitle1.setVisibility(View.GONE);
    }

   /* public void goneTitle2() {
        mTvTitle2.setVisibility(View.GONE);
    }*/

 /*   public void goneTitle3() {
        mTvTitle3.setVisibility(View.GONE);
    }

    public void goneRowTotal3() {
        lnRowTotal3.setVisibility(View.GONE);
    }*/

    public void showTitle() {
        mTvTitle1.setVisibility(View.VISIBLE);
    }

 /*   public void showTitle2() {
        mTvTitle2.setVisibility(View.VISIBLE);
    }

    public void showTitle3() {
        mTvTitle3.setVisibility(View.VISIBLE);
    }*/

    public void boldValue() {
        mTvValue1.setTypeface(null, Typeface.BOLD);
    }

  /*  public void boldValue2() {
        mTvValue2.setTypeface(null, Typeface.BOLD);
    }

    public void boldValue3() {
        mTvValue3.setTypeface(null, Typeface.BOLD);
    }*/

    public void unBoldValue() {
        mTvValue1.setTypeface(null, Typeface.NORMAL);
    }

  /*  public void unBoldValue2() {
        mTvValue2.setTypeface(null, Typeface.NORMAL);
    }

    public void unBoldValue3() {
        mTvValue3.setTypeface(null, Typeface.NORMAL);
    }*/

    public void setMarginUserDifinable1() {
        if (mTvValue1 != null) {
            mTvValue1.setGravity(Gravity.LEFT);
        }
    }

 /*   public void setMarginUserDifinable2() {
        if (mTvValue2 != null) {
            mTvValue2.setGravity(Gravity.LEFT);
        }
    }

    public void setMarginUserDifinable3() {
        if (mTvValue3 != null) {
            mTvValue3.setGravity(Gravity.LEFT);
        }
    }*/

    public void setTextValue(String pValue, float pFontSize) {
    }
}