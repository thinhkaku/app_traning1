package aero.pilotlog.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.MccEnum;
import aero.pilotlog.interfaces.IItemFlight;

/**
 * Created by tuan.pv on 2015/08/17.
 * Item Flight View
 */
public class ItemFlightView extends LinearLayout implements View.OnClickListener, MccEditText.EditTextImeBackListener, TextWatcher {

    protected TextView mTvFlightTitle;
    protected MccEditText mEdtFlightValue1;
    protected LinearLayout mLnFlight;
    protected TextView mTvFlightValue2;
    protected boolean mIsEnableEditText;
    protected ImageView mIvIcon1;
    protected LinearLayout mLnIcon1;
    protected ImageView mIvIcon2;
    protected LinearLayout mLnIcon2;


    public void setViewLineBorder(int visibility) {
        this.mViewLineBorder.setVisibility(visibility);
    }

    protected View mViewLineBorder;
    protected Context mContext;
    protected TextView mTvSchedule;
    protected ImageView mIvArrowInfo;
    protected LinearLayout mLnTitle;


    protected int mTypeItem;//=1: contain 2 line + icon, = 2: contain 2 line, =3: contain 1 line + icon, = 4: contain 1 line
    public static final int TWO_LINE_ICON = 1;
    public static final int TWO_LINE = 2;
    public static final int ONE_LINE_ICON = 3;
    public static final int ONE_LINE = 4;
    public static final int ONE_LINE_PADDING = 5;
    public static final int TWO_LINE_PADDING = 6;

    public static final int ITEM_FLIGHT_ROUTE = 1;
    public static final int ITEM_FLIGHT_HOURS = 2;
    public static final int ITEM_FLIGHT_CURRENCIES = 3;
    public static final int ITEM_FLIGHT_CREW = 4;
    public static final int ITEM_FLIGHT_LOG = 5;
    private IItemFlight mIItemFlight;
    protected OnHideKeyboardListener mOnHideKeyboard;
    protected OnTextChanged mOnTextChanged;
    private boolean isCheck = false;
    private ToggleIcon2 mToggleIcon2;
    //PL-112 keyboard logic add new
    protected OnApplyLogicWhenFieldFocusListener mOnApplyLogicWhenFieldFocus;
    //End PL-112
    //PL-487
    protected OnShowInfoPageListener mOnShowInfoPage;

    //End PL-487
    public void setIItemFlight(IItemFlight pIItemFlight) {
        this.mIItemFlight = pIItemFlight;
    }

    public ItemFlightView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("ResourceType")
    public ItemFlightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String title;
        boolean isHaveLineBorder, isCrewNoIcon, isFlightLogNoIcon;
        final int iconTop, resIdIcon2, colorValue2, flightType;
        mContext = context;
        TypedArray mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemFlight);
        flightType = mTypedAttrs.getInt(1, ITEM_FLIGHT_ROUTE);

        title = mTypedAttrs.getString(0);
        mTypeItem = mTypedAttrs.getInt(2, TWO_LINE_ICON);
        isHaveLineBorder = mTypedAttrs.getBoolean(4, true);//default always have line border bottom
        iconTop = mTypedAttrs.getResourceId(3, R.drawable.ic_crew_clear);
        resIdIcon2 = mTypedAttrs.getResourceId(9, R.drawable.ic_arrow);
        colorValue2 = mTypedAttrs.getColor(7, getResources().getColor(R.color.grey_footer_text));
        mIsEnableEditText = mTypedAttrs.getBoolean(11, false);
        switch (flightType) {
            case ITEM_FLIGHT_ROUTE:
            case ITEM_FLIGHT_HOURS:
            case ITEM_FLIGHT_CURRENCIES:
                inflate(getContext(), R.layout.item_flights, this);
                if (flightType == ITEM_FLIGHT_ROUTE) {
                    mIvArrowInfo = (ImageView) findViewById(R.id.iv_arrowInfo);
                    mLnTitle = (LinearLayout) findViewById(R.id.ln_title);
                }
                mIvIcon2 = (ImageView) findViewById(R.id.ivFlightIcon2);
                mLnIcon2 = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                mTvFlightTitle = (TextView) findViewById(R.id.tvFlightTitle);
                mLnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                mEdtFlightValue1 = (MccEditText) findViewById(R.id.edtFlightValue1);
                mTvFlightValue2 = (TextView) findViewById(R.id.edtFlightValue2);
                mTvSchedule = (TextView) findViewById(R.id.tvSchedule);
                mTvFlightValue2.setTextColor(colorValue2);
                mEdtFlightValue1.setSingleLine();
                switch (mTypeItem) {
                    case TWO_LINE_ICON:
                        mTvFlightValue2.setMaxLines(2);
                        mIvIcon2.setImageResource(resIdIcon2);
                        break;
                    case TWO_LINE:
                        mLnIcon2.setVisibility(INVISIBLE);
                        mTvFlightValue2.setLines(1);
                        break;
                    case TWO_LINE_PADDING: //apply for runway, pairing ...
                        mLnFlight.setPadding((int) getResources().getDimension(R.dimen.margin_left_item_pairing), 0, 0, 0);
                        mLnIcon2.setVisibility(INVISIBLE);
                        mTvFlightValue2.setLines(1);
                        View lineTop = findViewById(R.id.viewLineBorderFlightTop);
                        lineTop.setVisibility(VISIBLE);
                        break;

                    case ONE_LINE_ICON:
                        mTvFlightValue2.setVisibility(VISIBLE);
                        mTvFlightTitle.setLines(1);
                        mIvIcon2.setImageResource(resIdIcon2);
                        break;
                    case ONE_LINE:
                        mTvFlightValue2.setVisibility(GONE);
                        //mIvIcon2.setVisibility(INVISIBLE);
                        mLnIcon2.setVisibility(INVISIBLE);
                        mTvFlightTitle.setLines(1);
                        break;
                    case ONE_LINE_PADDING:
                        mTvFlightValue2.setVisibility(GONE);
                        mLnFlight.setPadding((int) getResources().getDimension(R.dimen.margin_left_item_pairing), 0, 0, 0);
                        lineTop = findViewById(R.id.viewLineBorderFlightTop);
                        lineTop.setVisibility(VISIBLE);
                        mLnIcon2.setVisibility(INVISIBLE);
                        mTvFlightTitle.setLines(1);
                        break;
                    default:
                        break;
                }

                break;
            case ITEM_FLIGHT_CREW:
                inflate(getContext(), R.layout.item_flight_crew, this);
                mIvIcon1 = (ImageView) findViewById(R.id.ivFlightIcon1);
                mIvIcon2 = (ImageView) findViewById(R.id.ivFlightIcon2);
                mLnIcon2 = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                mLnIcon1 = (LinearLayout) findViewById(R.id.ln_flight_icon_1);
                mIvArrowInfo = (ImageView) findViewById(R.id.iv_arrowInfo);
                mLnTitle = (LinearLayout) findViewById(R.id.ln_title);
                mTvFlightTitle = (TextView) findViewById(R.id.tvFlightTitle);
                mLnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                mEdtFlightValue1 = (MccEditText) findViewById(R.id.edtFlightValue1);
                mTvFlightValue2 = (TextView) findViewById(R.id.edtFlightValue2);
                mTvFlightValue2.setTextColor(colorValue2);
                mEdtFlightValue1.setSingleLine();
                isCrewNoIcon = mTypedAttrs.getBoolean(8, false);
              /*  if (isCrewNoIcon) {
                    mIvIcon1.setVisibility(GONE);
                    mIvIcon2.setVisibility(INVISIBLE);
                }*/
                break;
            case ITEM_FLIGHT_LOG:
                inflate(getContext(), R.layout.item_flight_log, this);
                //isFlightLogNoIcon = mTypedAttrs.getBoolean(10, false);
                mLnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                mEdtFlightValue1 = (MccEditText) findViewById(R.id.edtFlightValue1);
                mTvFlightTitle = (TextView) findViewById(R.id.tvFlightTitle);
                mIvIcon2 = (ImageView) findViewById(R.id.ivFlightIcon2);
                mLnIcon2 = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                mEdtFlightValue1.setSingleLine(false);
             /*   if (isFlightLogNoIcon) {
                    mIvIcon2.setVisibility(INVISIBLE);
                }*/
                break;
            default:
                break;
        }

        mTvFlightTitle.setText(title);
        mViewLineBorder = findViewById(R.id.viewLineBorderFlight);
        mEdtFlightValue1.setEnabled(mIsEnableEditText);

        if (!isHaveLineBorder) {
            mViewLineBorder.setVisibility(GONE);
        }

      /*  if (mIvIcon1 != null) {
            mIvIcon1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIItemFlight != null) {
                        mIItemFlight.onClickFlightIcon1(ItemFlightView.this);
                    }
                }
            });
        }*/
        if (mLnIcon1 != null) {
            mLnIcon1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIItemFlight != null) {
                        mIItemFlight.onClickFlightIcon1(ItemFlightView.this);
                    }
                }
            });
        }

      /*  if (mIvIcon2 != null && mIvIcon2.getVisibility() != INVISIBLE) {
            mIvIcon2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //isCheck = !TextUtils.isEmpty(mEdtFlightValue1.getText().toString());
                    //JIRA PL-638
                    isCheck = (!TextUtils.isEmpty(mEdtFlightValue1.getText().toString()) && !mEdtFlightValue1.getText().toString().equalsIgnoreCase("PM")) ? true : false;
                    //end PL-638
                    if (mIItemFlight != null) {
                        mIItemFlight.onClickFlightIcon2(ItemFlightView.this, !isCheck);
                        isCheck = !isCheck;
                        if (mToggleIcon2 != null) {
                            if (isCheck) {
                                mToggleIcon2.onIcon2Checked(ItemFlightView.this);
                            } else {
                                mToggleIcon2.onIcon2UnChecked(ItemFlightView.this);
                            }
                        }
                    }
                }
            });
        }*/
        if (mLnIcon2 != null && mLnIcon2.getVisibility() != INVISIBLE) {
            mLnIcon2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //isCheck = !TextUtils.isEmpty(mEdtFlightValue1.getText().toString());
                    //JIRA PL-638
                    isCheck = (!TextUtils.isEmpty(mEdtFlightValue1.getText().toString()) && !mEdtFlightValue1.getText().toString().equalsIgnoreCase("PM")) ? true : false;
                    //end PL-638
                    if (mIItemFlight != null) {
                        mIItemFlight.onClickFlightIcon2(ItemFlightView.this, !isCheck);
                        isCheck = !isCheck;
                        if (mToggleIcon2 != null) {
                            if (isCheck) {
                                mToggleIcon2.onIcon2Checked(ItemFlightView.this);
                            } else {
                                mToggleIcon2.onIcon2UnChecked(ItemFlightView.this);
                            }
                        }
                    }
                }
            });
        }

         /*Logic for edit text to be self-restored its value */
        mEdtFlightValue1.setId(this.getId() + 1000);
        this.setOnClickListener(this);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            mEdtFlightValue1.setId(Utils.generateViewId());
//        } else {
//            mEdtFlightValue1.setId(View.generateViewId());
//        }
        mEdtFlightValue1.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (!hasFocus) {
                    applyLogicWhenFieldFocus(false, mEdtFlightValue1.getInputTopKeyType());
                    if (mOnHideKeyboard != null) {

                        final Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Do Whatever
                                mOnHideKeyboard.onFinishInput(ItemFlightView.this, false);
                            }
                        });
                        //End PL-246
                    }
                    mEdtFlightValue1.setFocusable(false);
                    mEdtFlightValue1.setFocusableInTouchMode(false);
                } else {
                    try {
                        mOnHideKeyboard.onFinishInput(ItemFlightView.this, true);
                        applyLogicWhenFieldFocus(true, mEdtFlightValue1.getInputTopKeyType());
                    } catch (Exception e) {
                    }
                }
            }
        });

        mEdtFlightValue1.setSelectAllOnFocus(true);
        mEdtFlightValue1.setFocusable(false);

        mEdtFlightValue1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP)
                    if (mIsEnableEditText) {
                        mEdtFlightValue1.setFocusableInTouchMode(true);
//                    mEdtFlightValue1.requestFocus();
                    }
                return false;
            }
        });

//PL-184
        mLnFlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEnableEditText) {
                    mEdtFlightValue1.setFocusableInTouchMode(true);
                    //mEdtFlightValue1.setFocusable(true);
                    mEdtFlightValue1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEdtFlightValue1, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    //PL-274
                    mEdtFlightValue1.setEnabled(true);
                    mEdtFlightValue1.setFocusable(false);
                    mEdtFlightValue1.setFocusableInTouchMode(false);
                    mEdtFlightValue1.setClickable(true);
                    mIvIcon2.performClick();
                    mEdtFlightValue1.setEnabled(false);
                    //PL-274
                }
            }
        });
        //End PL-184
        //PL-487
        switch (ItemFlightView.this.getId()) {
            case R.id.item_flight_route_aircraft:
            case R.id.item_flight_route_arrival:
            case R.id.item_flight_route_departure:
            case R.id.item_flight_crew_pic:
            case R.id.item_flight_crew_2nd:
            case R.id.item_flight_crew_3rd:
            case R.id.item_flight_crew_4th:
            case R.id.item_flight_off_block:
            case R.id.item_flight_on_block:
                if (mLnTitle != null) {
                    if (mIvArrowInfo != null)
                        mIvArrowInfo.setVisibility(VISIBLE);
                    mLnTitle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnShowInfoPage != null) {
                                mOnShowInfoPage.onShowInfoPage(ItemFlightView.this.getId());
                            }
                        }
                    });
                    mTvSchedule.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnShowInfoPage != null) {
                                mOnShowInfoPage.onShowInfoPage(ItemFlightView.this.getId());
                            }
                        }
                    });
                } else {
                    if (mIvArrowInfo != null) {
                        mIvArrowInfo.setVisibility(VISIBLE);
                        mIvArrowInfo.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnShowInfoPage != null) {
                                    mOnShowInfoPage.onShowInfoPage(ItemFlightView.this.getId());
                                }
                            }
                        });
                    }
                    mTvFlightTitle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnShowInfoPage != null)
                                mOnShowInfoPage.onShowInfoPage(ItemFlightView.this.getId());

                        }
                    });
                }

                break;

        }
        //End PL-487

        if (!mIsEnableEditText) {
            mEdtFlightValue1.setEnabled(true);
            mEdtFlightValue1.setFocusable(false);
            mEdtFlightValue1.setFocusableInTouchMode(false);
            mEdtFlightValue1.setClickable(true);
            mEdtFlightValue1.setKeyListener(null);
            mEdtFlightValue1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIvIcon2.performClick();
                    mEdtFlightValue1.setEnabled(false);
                }
            });
        }

        mTypedAttrs.recycle();
    }

    public void setLinkClickable(String contentString) {
        SpannableString content = new SpannableString(contentString);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mTvFlightTitle.setText(content);
    }

    public void setScheduleHour(String hour) {
        mTvSchedule.setText(hour);
    }

    public void setVisibleScheduleHour(int visibility) {
        mTvSchedule.setVisibility(visibility);
    }

    public void setEditTextFilterAirCraft() {
        InputFilter filterChar = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '(', ')', '/', '.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        InputFilter filterCap = new InputFilter.AllCaps();
        mEdtFlightValue1.setFilters(new InputFilter[]{filterChar, filterCap});
    }

    public ItemFlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextTitle(String pValue) {
        if (mTvFlightTitle != null) {
            mTvFlightTitle.setText(pValue);
        }
    }

    public void setTextTitleColor(int color) {
        mTvFlightTitle.setTextColor(getResources().getColor(color));
    }

    public String getTextTitle() {
        if (mTvFlightTitle != null) {
            return mTvFlightTitle.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public void setTextValue1(String pValue) {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setTextColor(getResources().getColor(android.R.color.black));
            mEdtFlightValue1.setText(pValue);
        }
    }

    public void setTextValue1(String pValue, int pColor) {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setTextColor(getResources().getColor(pColor));
            mEdtFlightValue1.setText(pValue);
        }
    }

    public void setTextValue1(String pValue, int pColor, float pFontSize) {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setTextColor(getResources().getColor(pColor));
            mEdtFlightValue1.setText(pValue);
            mEdtFlightValue1.setTextSize(TypedValue.COMPLEX_UNIT_PX, pFontSize);
        }
    }

    public void setMarginUserDifinable() {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setGravity(Gravity.LEFT);
        }
    }

    public void setGravityValue2() {
        if (mTvFlightValue2 != null) {
            mTvFlightValue2.setGravity(Gravity.LEFT);
            /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTvFlightValue2.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);*/
        }
    }

    public String getTextValue1() {
        if (mEdtFlightValue1 != null) {
            return mEdtFlightValue1.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public MccEditText getEdittextValue1() {
        return mEdtFlightValue1;
    }

    public void setTextValue2(String pValue) {
        if (mTvFlightValue2 != null) {
            mTvFlightValue2.setTextColor(getResources().getColor(R.color.grey_footer_text));
            mTvFlightValue2.setText(pValue);
        }
    }

    public void setTextValue2(String pValue, int pColor) {
        if (mTvFlightValue2 != null) {
            mTvFlightValue2.setTextColor(getResources().getColor(pColor));
            mTvFlightValue2.setText(pValue);
        }
    }

    public String getTextValue2() {
        if (mTvFlightValue2 != null) {
            return mTvFlightValue2.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }


    public void setTextColorValue2(int pColor) {
        if (mTvFlightValue2 != null) {
            mTvFlightValue2.setTextColor(pColor);
        }
    }

    public int getTextColorValue1() {
        if (mEdtFlightValue1 != null) {
            return mEdtFlightValue1.getCurrentTextColor();
        }
        return Color.BLACK;
    }

    public void setOnHideKeyboardListener(OnHideKeyboardListener pListener) {
        mEdtFlightValue1.setOnEditTextImeBackListener(this);
        mOnHideKeyboard = pListener;
    }

    public void setTextWatcherForValue1(OnTextChanged pWatcher) {
        mEdtFlightValue1.addTextChangedListener(this);
        mOnTextChanged = pWatcher;
    }

    public void setInputModeForValue1(int pType) {
        mEdtFlightValue1.setInputType(pType);
    }

    //PL-711
    public void setInputFilterValue1AllCaps() {
        try {
            InputFilter[] inputFilters = mEdtFlightValue1.getFilters();
            if (inputFilters == null) {
                inputFilters = new InputFilter[]{new InputFilter.AllCaps()};
            } else {
                if (inputFilters.length > 0) {
                    InputFilter[] inputFilterCole = inputFilters.clone();
                    inputFilters = new InputFilter[inputFilterCole.length + 1];
                    for (int i = 0; i < inputFilterCole.length; i++) {
                        inputFilters[i] = inputFilterCole[i];
                    }
                    inputFilters[inputFilterCole.length] = new InputFilter.AllCaps();
                } else
                    inputFilters = new InputFilter[]{new InputFilter.AllCaps()};
            }

            mEdtFlightValue1.setFilters(inputFilters);
        } catch (Exception e) {
        }
    }

    public void setMaxLengthForValue1(int pLength) {
        try {
            InputFilter[] inputFilters = mEdtFlightValue1.getFilters();
            if (inputFilters == null) {
                inputFilters = new InputFilter[]{new InputFilter.LengthFilter(pLength)};
            } else {
                if (inputFilters.length > 0) {
                    InputFilter[] inputFilterCole = inputFilters.clone();
                    inputFilters = new InputFilter[inputFilterCole.length + 1];
                    for (int i = 0; i < inputFilterCole.length; i++) {
                        inputFilters[i] = inputFilterCole[i];
                    }
                    inputFilters[inputFilterCole.length] = new InputFilter.LengthFilter(pLength);
                } else
                    inputFilters = new InputFilter[]{new InputFilter.LengthFilter(pLength)};
            }

            mEdtFlightValue1.setFilters(inputFilters);
        } catch (Exception ex) {

        }
    }

    //End PL-711
    public void setHint(String pHint) {
        mEdtFlightValue1.setHint(pHint);
    }

    public void setHint(int pHintId) {
        mEdtFlightValue1.setHint(pHintId);
    }

    public void clearValue() {
        mEdtFlightValue1.getText().clear();
        mEdtFlightValue1.setText(MCCPilotLogConst.STRING_EMPTY);
        if (mTvFlightValue2 != null) {
            mTvFlightValue2.setText(MCCPilotLogConst.STRING_EMPTY);
        }
    }

    public void performCheck(boolean pCheck) {
        if (mToggleIcon2 != null) {
            isCheck = pCheck;//TuanPV add
            if (pCheck) {
                mToggleIcon2.onIcon2Checked(this);
            } else {
                mToggleIcon2.onIcon2UnChecked(this);
            }
        }
    }

    public boolean isEnableValue1() {
        return mEdtFlightValue1 != null && mEdtFlightValue1.isEnabled();
    }

    public void setEnableValue1(boolean pIsEnalbe) {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setEnabled(pIsEnalbe);
        }
    }

    public void setIcon2Interface(ToggleIcon2 pToggleIcon2) {
        mToggleIcon2 = pToggleIcon2;
    }

    @Override
    public void onClick(View v) {
        // hide keyboard PL-246
        /*if (v.equals(this)) {
            if (mOnHideKeyboard != null && ((ItemFlightView)v).getEdittextValue1().hasFocus()) {
                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtFlightValue1.getWindowToken(), 0);
                mEdtFlightValue1.setFocusableInTouchMode(false);
                mEdtFlightValue1.setFocusable(false);
                mOnHideKeyboard.onFinishInput(ItemFlightView.this);
            }
        }*/
        //end PL-246
    }

    @Override
    public void onHideKeyboard(MccEditText pEditText) {
        if (mEdtFlightValue1 != null) {
            mEdtFlightValue1.setFocusable(false);
            mEdtFlightValue1.setFocusableInTouchMode(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mOnTextChanged != null) {
            mOnTextChanged.afterTextChanged(this, s);
        }
    }

    public interface OnHideKeyboardListener {
        void onFinishInput(ItemFlightView pItemn, boolean isFocus);
    }

    public interface OnTextChanged {
        void afterTextChanged(ItemFlightView pItem, Editable pS);
    }

    public interface ToggleIcon2 {
        void onIcon2Checked(ItemFlightView pItem);

        void onIcon2UnChecked(ItemFlightView pItem);
    }

    public void setStatusIcon2(boolean pFlag) {
        mIvIcon2.setEnabled(pFlag);
    }

    //PL-487
    public interface OnShowInfoPageListener {
        //void onShowInfoPage(String title, PageInfoType pageInfoType);
        void onShowInfoPage(int flightItemId);
    }

    public void setmOnShowInfoPage(OnShowInfoPageListener onShowInfoPageListener) {
        mOnShowInfoPage = onShowInfoPageListener;
    }
    //End PL-487

    //PL-112 keyboard logic add new
    public interface OnApplyLogicWhenFieldFocusListener {

        void OnTopKeyShow(boolean isShow);
    }

    public void setOnApplyLogicWhenFieldFocusListener(OnApplyLogicWhenFieldFocusListener onApplyLogicWhenFieldFocusListener) {
        mOnApplyLogicWhenFieldFocus = onApplyLogicWhenFieldFocusListener;
    }

    String keyboardMemoryTemp1 = "";
    String keyboardMemoryTemp2 = "";


    public void applyLogicWhenFieldFocus(boolean isFocus, MccEnum.topKeyboardCustomInput input) {
        if (isFocus) {
            //store the current field value in a temp-memory
            keyboardMemoryTemp1 = mEdtFlightValue1.getText().toString();
            if (mTvFlightValue2 != null)
                keyboardMemoryTemp2 = mTvFlightValue2.getText().toString();
            //clear the field
            {
                /*mEdtFlightValue1.setText("");
                if (mTvFlightValue2 != null)
                    mTvFlightValue2.setText("");*/
            }
            //open the keyboard
            if (mOnApplyLogicWhenFieldFocus != null)
                mOnApplyLogicWhenFieldFocus.OnTopKeyShow(true);
        } else {
            switch (input) {
                case DONE:
                    keyboardMemoryTemp1 = "";
                    keyboardMemoryTemp2 = "";
                    break;
                case CANCEL:
                    mEdtFlightValue1.setText(keyboardMemoryTemp1);
                    if (mTvFlightValue2 != null)
                        mTvFlightValue2.setText(keyboardMemoryTemp2);
                    keyboardMemoryTemp1 = "";
                    keyboardMemoryTemp2 = "";
                    break;
                case NONE:
                case NEXT:
                   /* if ("".equals(mEdtFlightValue1.getText().toString())) {
                        if (!"".equals(keyboardMemoryTemp1)) {
                            mEdtFlightValue1.setText(keyboardMemoryTemp1);
                            if (mTvFlightValue2 != null)
                                mTvFlightValue2.setText(keyboardMemoryTemp2);
                        }
                    }*/
                    keyboardMemoryTemp1 = "";
                    keyboardMemoryTemp2 = "";
                    break;
            }
            mEdtFlightValue1.setInputTopKeyType(MccEnum.topKeyboardCustomInput.NONE);
            if (mOnApplyLogicWhenFieldFocus != null && input != MccEnum.topKeyboardCustomInput.NEXT)
                mOnApplyLogicWhenFieldFocus.OnTopKeyShow(false);
        }

        mEdtFlightValue1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mEdtFlightValue1.setInputTopKeyType(MccEnum.topKeyboardCustomInput.DONE);
                    mEdtFlightValue1.clearFocus();
                    applyLogicWhenFieldFocus(false, MccEnum.topKeyboardCustomInput.DONE);
                    if (!MCCPilotLogConst.sIsTablet)
                        mEdtFlightValue1.clearFocus();
                    else {
                        final InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.hideSoftInputFromWindow(mEdtFlightValue1.getWindowToken(), 0);
                    }

                    //return  true;
                }
                return false;
            }
        });
    }
    //End PL-112

    public void setInvisibleIcon2() {
        if (mIvIcon2 != null) {
            mIvIcon2.setEnabled(false);
            mIvIcon2.setVisibility(INVISIBLE);
        }
    }

    public void setIconArrowInfo(int draw) {
        if (mIvArrowInfo != null) {
            mIvArrowInfo.setImageResource(android.R.color.transparent);
            mIvArrowInfo.setBackgroundResource(draw);
        }
    }
}