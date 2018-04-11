package aero.pilotlog.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.Pilot;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.fragments.AircraftInfoFragment;
import aero.pilotlog.fragments.AirfieldsInfoFragment;
import aero.pilotlog.fragments.BaseFragment;
import aero.pilotlog.fragments.BaseMCCFragment;
import aero.pilotlog.fragments.PilotInfoFragment;
import aero.pilotlog.interfaces.IItemsFlight;

/**
 * Created by phuc.dd on 3/27/2017.
 */
public class ItemsFlightView extends LinearLayout {

    public void setSaveState(String saveState) {
        this.saveState = saveState;
    }

    public String getSaveState() {
        return saveState;
    }

    private String saveState;
    protected TextView tvTitle;

    public MccEditText getEdtDescription() {
        return edtDescription;
    }

    protected MccEditText edtDescription;
    protected LinearLayout lnFlight;
    protected TextView tvFootNote;
    protected boolean isEnableEditText;
    protected ImageView ivIconTop;
    protected LinearLayout lnIconTop;
    protected ImageView ivIconBottom;
    protected LinearLayout lnIconBottom;
    protected View lineBottom;
    protected Context context;
    protected TextView tvSchedule;
    protected TextView tvTotalSchedule;
    protected LinearLayout lnTotalSchedule;
    protected TextView tvTotalArr;
    protected LinearLayout lnTotalArr;
    protected ImageView ivIconInfo;
    protected LinearLayout lnTitle;
    protected int mTypeItem;
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
    public static final int ITEM_FLIGHT_DELAY = 6;
    private DatabaseManager databaseManager;
    public static final int ICON_TYPE_COPY_CURRENCY = 1;
    public static final int ICON_TYPE_CLEAR = 2;
    public static final int ICON_TYPE_NAVIGATION = 0;
    public static final int ICON_TYPE_CLEAR_DESCRIPTION = 3;
    protected int iconType;

    public int getMinutesData() {
        return minutesData;
    }

    public void setMinutesData(int minutesData) {
        this.minutesData = minutesData;
    }

    private int minutesData = 0;

    public int getMinutesDataOnlyForMinArr() {
        return minutesDataOnlyForMinArr;
    }

    public void setMinutesDataOnlyForMinArr(int minutesDataOnlyForMinArr) {
        this.minutesDataOnlyForMinArr = minutesDataOnlyForMinArr;
    }

    private int minutesDataOnlyForMinArr = 0;

    /*public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    private int itemIndex;*/

    private Class<? extends BaseFragment> navigationToFragment;
    private BaseMCCFragment parentFragment;

    private IItemsFlight mIItemFlight;
    int flightType;

    public int getFlightType() {
        return this.flightType;
    }

    public void setIItemFlight(IItemsFlight pIItemFlight) {
        this.mIItemFlight = pIItemFlight;
    }

    public ItemsFlightView(Context context) {
        super(context);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("ResourceType")
    public ItemsFlightView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        if (this.getId() != R.id.item_flight_route_aircraft && this.getId() != R.id.item_flight_route_date &&
                this.getId() != R.id.item_flight_route_departure && this.getId() != R.id.item_flight_route_arrival)
            this.setVisibility(View.GONE);
        String title;
        boolean isHaveLineBorder;
        final int iconBottom, colorValue2;
        this.context = context;
        TypedArray mTypedAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemFlight);
        flightType = mTypedAttrs.getInt(1, ITEM_FLIGHT_ROUTE);
        title = mTypedAttrs.getString(0);
        mTypeItem = mTypedAttrs.getInt(2, TWO_LINE_ICON);
        isHaveLineBorder = mTypedAttrs.getBoolean(4, true);//default always have line border bottom
        iconBottom = mTypedAttrs.getResourceId(9, R.drawable.ic_arrow);
        colorValue2 = mTypedAttrs.getColor(7, getResources().getColor(R.color.grey_footer_text));
        isEnableEditText = mTypedAttrs.getBoolean(11, false);
        boolean isAllCaps = mTypedAttrs.getBoolean(15, false);
        boolean isNumeric = mTypedAttrs.getBoolean(16, false);
        int maxLength = mTypedAttrs.getInt(17, 0);
        String hint = mTypedAttrs.getString(18);
        iconType = mTypedAttrs.getInt(19, 0);

        switch (flightType) {
            case ITEM_FLIGHT_ROUTE:
            case ITEM_FLIGHT_HOURS:
            case ITEM_FLIGHT_CURRENCIES:
                inflate(getContext(), R.layout.item_flights, this);
                if (flightType == ITEM_FLIGHT_ROUTE || flightType == ITEM_FLIGHT_HOURS) {
                    ivIconInfo = (ImageView) findViewById(R.id.iv_arrowInfo);
                    lnTitle = (LinearLayout) findViewById(R.id.ln_title);
                }
                ivIconBottom = (ImageView) findViewById(R.id.ivFlightIcon2);
                lnIconBottom = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                tvTitle = (TextView) findViewById(R.id.tvFlightTitle);
                lnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                edtDescription = (MccEditText) findViewById(R.id.edtFlightValue1);
                tvFootNote = (TextView) findViewById(R.id.edtFlightValue2);
                tvSchedule = (TextView) findViewById(R.id.tvSchedule);
                tvTotalSchedule = (TextView) findViewById(R.id.total_schedule);
                tvTotalArr = (TextView) findViewById(R.id.total_arr);
                lnTotalSchedule = (LinearLayout) findViewById(R.id.ln_total_sched);
                lnTotalArr = (LinearLayout) findViewById(R.id.ln_total_air);
                tvFootNote.setTextColor(colorValue2);
                //edtDescription.setSingleLine();
                switch (mTypeItem) {
                    case TWO_LINE_ICON:
                        tvFootNote.setMaxLines(2);
                        ivIconBottom.setImageResource(iconBottom);
                        break;
                    case TWO_LINE:
                        //lnIconBottom.setVisibility(INVISIBLE);
                        ivIconBottom.setImageResource(iconBottom);
                        tvFootNote.setLines(1);
                        break;
                    case TWO_LINE_PADDING: //apply for runway, pairing ...
                        lnFlight.setPadding((int) getResources().getDimension(R.dimen.margin_left_item_pairing), 0, 0, 0);
                        //lnIconBottom.setVisibility(INVISIBLE);
                        ivIconBottom.setImageResource(iconBottom);
                        tvFootNote.setLines(1);
                        View lineTop = findViewById(R.id.viewLineBorderFlightTop);
                        lineTop.setVisibility(VISIBLE);
                        break;
                    case ONE_LINE_ICON:
                        tvFootNote.setVisibility(VISIBLE);
                        tvTitle.setLines(1);
                        ivIconBottom.setImageResource(iconBottom);
                        break;
                    case ONE_LINE:
                        tvFootNote.setVisibility(GONE);
                        //lnIconBottom.setVisibility(INVISIBLE);
                        ivIconBottom.setImageResource(iconBottom);
                        tvTitle.setLines(1);
                        break;
                    case ONE_LINE_PADDING:
                        tvFootNote.setVisibility(GONE);
                        lnFlight.setPadding((int) getResources().getDimension(R.dimen.margin_left_item_pairing), 0, 0, 0);
                        lineTop = findViewById(R.id.viewLineBorderFlightTop);
                        lineTop.setVisibility(VISIBLE);
                        //lnIconBottom.setVisibility(INVISIBLE);
                        ivIconBottom.setImageResource(iconBottom);
                        tvTitle.setLines(1);
                        break;
                    default:
                        break;
                }
                break;
            case ITEM_FLIGHT_CREW:
                inflate(getContext(), R.layout.item_flight_crew, this);
                ivIconTop = (ImageView) findViewById(R.id.ivFlightIcon1);
                ivIconBottom = (ImageView) findViewById(R.id.ivFlightIcon2);
                lnIconBottom = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                lnIconTop = (LinearLayout) findViewById(R.id.ln_flight_icon_1);
                ivIconInfo = (ImageView) findViewById(R.id.iv_arrowInfo);
                lnTitle = (LinearLayout) findViewById(R.id.ln_title);
                tvTitle = (TextView) findViewById(R.id.tvFlightTitle);
                lnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                edtDescription = (MccEditText) findViewById(R.id.edtFlightValue1);
                tvFootNote = (TextView) findViewById(R.id.edtFlightValue2);
                tvFootNote.setTextColor(colorValue2);
                edtDescription.setSingleLine();
                break;
            case ITEM_FLIGHT_LOG:
                inflate(getContext(), R.layout.item_flight_log, this);
                lnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                edtDescription = (MccEditText) findViewById(R.id.edtFlightValue1);
                tvTitle = (TextView) findViewById(R.id.tvFlightTitle);
                tvFootNote = (TextView) findViewById(R.id.edtFlightValue2);
                ivIconBottom = (ImageView) findViewById(R.id.ivFlightIcon2);
                lnIconBottom = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                edtDescription.setSingleLine(false);
                break;
            case ITEM_FLIGHT_DELAY:
                inflate(getContext(), R.layout.item_flights_delay, this);
                lnFlight = (LinearLayout) findViewById(R.id.lnFlight);
                edtDescription = (MccEditText) findViewById(R.id.edtFlightValue1);
                tvTitle = (TextView) findViewById(R.id.tvFlightTitle);
                ivIconBottom = (ImageView) findViewById(R.id.ivFlightIcon2);
                lnIconBottom = (LinearLayout) findViewById(R.id.ln_flight_icon_2);
                edtDescription.setSingleLine(false);
                break;
            default:
                break;
        }

        tvTitle.setText(title);
        lineBottom = findViewById(R.id.viewLineBorderFlight);
        edtDescription.setText(saveState);
        edtDescription.setEnabled(isEnableEditText);
        edtDescription.setSelectAllOnFocus(true);
        setFilter(maxLength, isAllCaps);
        setActionDone(edtDescription);
        if (isNumeric)
            edtDescription.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        if (!TextUtils.isEmpty(hint)) {
            edtDescription.setHint(hint);
        }
        if (!isHaveLineBorder) {
            lineBottom.setVisibility(GONE);
        }
        edtDescription.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {

                if (mIItemFlight != null) {
                    Handler handler = new Handler();
                    if (!hasFocus) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mIItemFlight.onFinishInput(ItemsFlightView.this, true);
                            }
                        });
                    }else {
                        if(ItemsFlightView.this.getId()==R.id.item_flight_hobbs_out || ItemsFlightView.this.getId()==R.id.item_flight_hobbs_in){
                            setFilter(6,false);
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            mIItemFlight.onFocusChange(v, ItemsFlightView.this);
                        }
                    });
                }
            }
        });
        lnFlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnableEditText) {
                    edtDescription.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edtDescription, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        switch (ItemsFlightView.this.getId()) {
            case R.id.item_flight_route_aircraft:
            case R.id.item_flight_route_arrival:
            case R.id.item_flight_route_departure:
            case R.id.item_flight_crew_pic:
            case R.id.item_flight_crew_2nd:
            case R.id.item_flight_crew_3rd:
            case R.id.item_flight_crew_4th:
                if (lnTitle != null) {
                    if (ivIconInfo != null)
                        ivIconInfo.setVisibility(VISIBLE);
                    lnTitle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showInfoPage();
                        }
                    });
                    if(tvSchedule!=null)
                    tvSchedule.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showInfoPage();
                        }
                    });
                }
                break;
            case R.id.item_flight_off_block:
            case R.id.item_flight_on_block:
                if (lnTitle != null) {
                   /* if (ivIconInfo != null)
                        ivIconInfo.setVisibility(VISIBLE);*/
                    lnTitle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mIItemFlight != null) {
                                final Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIItemFlight.onShowInfoPage(ItemsFlightView.this.getId());
                                    }
                                });
                            }
                        }
                    });
                    tvSchedule.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mIItemFlight != null) {
                                final Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIItemFlight.onShowInfoPage(ItemsFlightView.this.getId());
                                    }
                                });
                            }
                        }
                    });
                }

                break;

        }

        lnIconBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickIconBottom();
            }
        });
        if (lnIconTop != null)
            lnIconTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIItemFlight != null) {
                        mIItemFlight.onClickIconTop(ItemsFlightView.this);
                    }
                }
            });
        mTypedAttrs.recycle();
    }

    public void setScheduleHour(String hour) {
        tvSchedule.setText(hour);
    }

    public void setTotalSchedule(String hour) {
        tvTotalSchedule.setText(hour);
        if (!TextUtils.isEmpty(hour))
            lnTotalSchedule.setVisibility(View.VISIBLE);
    }

    public void setTotalArr(String hour) {
        tvTotalArr.setText(hour);
        if (!TextUtils.isEmpty(hour))
            lnTotalArr.setVisibility(View.VISIBLE);
    }

    public String getTotalSchedule() {
        return tvTotalSchedule.getText().toString();
    }

    public String getTotalArr() {
        //tvTotalArr.getText().toString().replace("‒", "");
        return tvTotalArr.getText().toString().replace("‒", "");
    }

    public void setVisibleScheduleHour(int visibility) {
        tvSchedule.setVisibility(visibility);
    }

    public void setVisibleIvInfo(int visibility) {
        ivIconInfo.setVisibility(visibility);
    }

    private void setEditTextFilterAirCraft() {
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
        edtDescription.setFilters(new InputFilter[]{filterChar, filterCap, new InputFilter.LengthFilter(25)});
    }

    public ItemsFlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextTitle(String value) {
        if (tvTitle != null) {
            tvTitle.setText(value);
        }
    }

    public String getTextTitle() {
        if (tvTitle != null) {
            return tvTitle.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public void setDescription(String value) {
        if (value != null) {
            if (edtDescription != null) {
                edtDescription.setTextColor(getResources().getColor(android.R.color.black));
                if (value.equals("0:00") || value.equals("00:00") || value.equals("0:0") ||
                        value.equals("0.00") || value.equals("00.00") || value.equals("0.0") ||
                        value.equals("0,00") || value.equals("00,00") || value.equals("0,0"))
                    value = "";
                edtDescription.setText(value);
                saveState = value;
                //if ("".equals(value)) setMinutesData(0);
            }
        }
    }

    public void setDescription(int value) {
        if (edtDescription != null) {
            edtDescription.setTextColor(getResources().getColor(android.R.color.black));
            edtDescription.setText(value == 0 ? "" : String.valueOf(value));
            saveState = String.valueOf(value);
        }
    }

    public void setFinishInput() {
        if (mIItemFlight != null) {
            mIItemFlight.onFinishInput(ItemsFlightView.this, false);
        }
    }

    public void setDescription(String value, boolean isOnFinishInput) {
        if (edtDescription != null) {
            edtDescription.setTextColor(getResources().getColor(android.R.color.black));
            edtDescription.setText(value);
            saveState = value;
        }
        if (isOnFinishInput && mIItemFlight != null) {
            mIItemFlight.onFinishInput(ItemsFlightView.this, false);
        }
    }

    public void restoreState() {
        if (!"0".equalsIgnoreCase(saveState))
            edtDescription.setText(saveState);
    }

    public void setDescription(String value, int color) {
        if (edtDescription != null) {
            edtDescription.setTextColor(getResources().getColor(color));
            edtDescription.setText(value);
            saveState = value;
        }
    }

    public String getDescription() {
        if (edtDescription != null) {
            return edtDescription.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public int getDescriptionInt() {
        if (edtDescription != null) {
            if (!TextUtils.isEmpty(edtDescription.getText())) {
                return Integer.parseInt(edtDescription.getText().toString());
            }
            return 0;
        }
        return 0;
    }

    public void setFootNote(String pValue) {
        if (tvFootNote != null) {
            tvFootNote.setTextColor(getResources().getColor(R.color.grey_footer_text));
            tvFootNote.setText(pValue);
        }
    }

    public void setFootNote(String pValue, int color) {
        if (tvFootNote != null) {
            tvFootNote.setTextColor(getResources().getColor(color));
            tvFootNote.setText(pValue);
        }
    }

    public String getFootNote() {
        if (tvFootNote != null) {
            return tvFootNote.getText().toString();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public void setInvisibleIconBottom() {
        if (ivIconBottom != null) {
            ivIconBottom.setEnabled(false);
            ivIconBottom.setVisibility(GONE);
        }
    }

    public void setIconArrowInfo(int draw) {
        if (ivIconInfo != null) {
            ivIconInfo.setImageResource(android.R.color.transparent);
            ivIconInfo.setBackgroundResource(draw);
        }
    }

    public void setViewLineBorder(int visibility) {
        this.lineBottom.setVisibility(visibility);
    }

    public void setNavigationToFragment(Class<? extends BaseFragment> navigationToFragment, BaseMCCFragment parentFragment) {
        this.navigationToFragment = navigationToFragment;
        this.parentFragment = parentFragment;
    }

    public void setParentFragment(BaseMCCFragment fragment) {
        this.parentFragment = fragment;
    }

    private void onClickIconBottom() {
        switch (iconType) {
            case ICON_TYPE_NAVIGATION:
                if (mIItemFlight != null) {
                    mIItemFlight.onClickIconBottom(ItemsFlightView.this);
                }
                break;
            case ICON_TYPE_CLEAR:
                setFootNote("");
                setDescription("", true);
                break;
            case ICON_TYPE_CLEAR_DESCRIPTION:
                setDescription("");
                break;
            case ICON_TYPE_COPY_CURRENCY:
                if (!TextUtils.isEmpty(getDescription())) {
                    setDescription("");
                } else {
                    setDescription("1");
                }
                break;
        }

    }

    public void onCheckIconBottom(String value, int idChecked) {
        if (this.getId() == idChecked && !isChecked) {
            this.edtDescription.setText(value);
            isChecked = true;
        } else {
            this.edtDescription.setText("");
            isChecked = false;
        }
    }

    public void onCheckIconBottom(String value) {
        if (!isChecked) {
            this.edtDescription.setText(value);
            isChecked = true;
        } else {
            this.edtDescription.setText("");
            isChecked = false;
        }
    }

    private void setInputMode(int type) {
        edtDescription.setInputType(type);
    }

    private void showInfoPage() {
        Bundle bundle = new Bundle();
        byte[] infoPageCode;
        if (TextUtils.isEmpty(edtDescription.getText().toString())) return;
        if (parentFragment != null)
            switch (getId()) {
                case R.id.item_flight_route_aircraft:
                    databaseManager = DatabaseManager.getInstance(context);
                    Aircraft aircraft = databaseManager.getAircraftByReference(edtDescription.getText().toString());
                    if (aircraft != null) {
                        infoPageCode = aircraft.getAircraftCode();
                        bundle.putByteArray(MCCPilotLogConst.AIRCRAFT_CODE_KEY, infoPageCode);
                        bundle.putString(MCCPilotLogConst.AIRCRAFT_INFO_MODE, MCCPilotLogConst.ONE_STRING);
                        if (MCCPilotLogConst.sIsTablet) {
                            parentFragment.replaceFragment(R.id.rightContainerFragment, AircraftInfoFragment.class, bundle, true);
                        } else {
                            parentFragment.replaceFragment(R.id.fragmentMain, AircraftInfoFragment.class, bundle, true);
                        }
                    }
                    break;
                case R.id.item_flight_route_arrival:
                case R.id.item_flight_route_departure:
                    databaseManager = DatabaseManager.getInstance(context);
                    Airfield airfield = databaseManager.getAirfieldByICAOIATA(edtDescription.getText().toString());
                    if (airfield != null) {
                        infoPageCode = airfield.getAFCode();
                        bundle.putByteArray(MCCPilotLogConst.AIRFIELD_CODE_KEY, infoPageCode);
                        bundle.putString(MCCPilotLogConst.AIRFIELD_INFO_MODE, MCCPilotLogConst.ONE_STRING);
                        if (MCCPilotLogConst.sIsTablet) {
                            parentFragment.replaceFragment(R.id.rightContainerFragment, AirfieldsInfoFragment.class, bundle, true);
                        } else {
                            parentFragment.replaceFragment(R.id.fragmentMain, AirfieldsInfoFragment.class, bundle, true);
                        }
                    }
                    break;
                case R.id.item_flight_crew_pic:
                case R.id.item_flight_crew_2nd:
                case R.id.item_flight_crew_3rd:
                case R.id.item_flight_crew_4th:
                    databaseManager = DatabaseManager.getInstance(context);
                    Pilot pilot = databaseManager.getPilotByName(edtDescription.getText().toString());
                    if (pilot != null) {
                        infoPageCode = pilot.getPilotCode();
                        bundle.putByteArray(MCCPilotLogConst.PILOT_CODE_KEY, infoPageCode);
                        bundle.putString(MCCPilotLogConst.PILOT_INFO_MODE, MCCPilotLogConst.ONE_STRING);
                        if (MCCPilotLogConst.sIsTablet) {
                            parentFragment.replaceFragment(R.id.rightContainerFragment, PilotInfoFragment.class, bundle, true);
                        } else {
                            parentFragment.replaceFragment(R.id.fragmentMain, PilotInfoFragment.class, bundle, true);
                        }
                    }
                    break;

            }
    }

    private void setFilter(int maxLength, boolean isAllCaps) {
        if (getId() == R.id.item_flight_route_aircraft) {
            setEditTextFilterAirCraft();
        } else if (maxLength > 0 && isAllCaps) {

            setFilterAllCapAndMaxLength(maxLength);
        } else if (maxLength > 0) {
            InputFilter[] inputFilters = new InputFilter[1];
            inputFilters[0] = new InputFilter.LengthFilter(maxLength);
            edtDescription.setFilters(inputFilters);
        }
    }

    private void setFilterAllCapAndMaxLength(int length) {
        InputFilter[] inputFilters = new InputFilter[2];
        inputFilters[0] = new InputFilter.LengthFilter(length);
        inputFilters[1] = new InputFilter.AllCaps();
        edtDescription.setFilters(inputFilters);
    }

    public void clearValue() {
        setMinutesData(0);
        saveState = MCCPilotLogConst.STRING_EMPTY;
        edtDescription.setText(MCCPilotLogConst.STRING_EMPTY);
        if (tvFootNote != null) {
            tvFootNote.setText(MCCPilotLogConst.STRING_EMPTY);
        }
        if (tvTotalSchedule != null) {
            tvTotalSchedule.setText(MCCPilotLogConst.STRING_EMPTY);
        }
        if (tvTotalArr != null) {
            tvTotalArr.setText(MCCPilotLogConst.STRING_EMPTY);
        }
    }

    public void setHint(String pHint) {
        edtDescription.setHint(pHint);
    }

    public void setHint(int pHintId) {
        edtDescription.setHint(pHintId);
    }

    private void setActionDone(final MccEditText edt) {
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (mIItemFlight != null) {
                        mIItemFlight.onFinishInput(ItemsFlightView.this, true);
                    }
                    edt.clearFocus();
                    final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private boolean isChecked = false;

    public void disableForFlightView() {
        if (ivIconTop != null) {
            ivIconTop.setVisibility(View.GONE);
            ivIconTop.setEnabled(false);
        }
        if (ivIconBottom != null) {
            ivIconBottom.setVisibility(View.GONE);
            ivIconBottom.setEnabled(false);
        }
       /* if (tvFootNote != null) {
            tvFootNote.setVisibility(GONE);
        }*/
        if (edtDescription != null) {
            edtDescription.setEnabled(false);
            edtDescription.setHint("");
        }
        if (lnFlight != null) {
            lnFlight.setEnabled(false);
        }
        if (lnIconBottom != null) {
            lnIconBottom.setEnabled(false);
        }
        if (lnIconTop != null) {
            lnIconTop.setEnabled(false);
        }
    }

    public void setViewOnlyMode() {
        if (ivIconTop != null) {
            ivIconTop.setVisibility(View.GONE);
            ivIconTop.setEnabled(false);
        }
        if (ivIconBottom != null) {
            ivIconBottom.setVisibility(View.GONE);
            ivIconBottom.setEnabled(false);
        }
        if (tvFootNote != null) {
            tvFootNote.setVisibility(GONE);
        }
        if (edtDescription != null) {
            edtDescription.setEnabled(false);
            edtDescription.setTextColor(getResources().getColor(R.color.gray_1));
        }
        if (lnFlight != null) {
            lnFlight.setEnabled(false);
        }
        if (lnIconBottom != null) {
            lnIconBottom.setEnabled(false);
        }
        if (lnIconTop != null) {
            lnIconTop.setEnabled(false);
        }
    }

    public void showImageSign(){
        ImageView imageView = (ImageView) findViewById(R.id.imageSign);
        if(imageView!=null)imageView.setVisibility(View.VISIBLE);
        edtDescription.setVisibility(View.GONE);
    }
    public void setImageSign(Bitmap bm){
        ImageView imageView = (ImageView) findViewById(R.id.imageSign);
        if(imageView!=null)imageView.setImageBitmap(bm);
    }
}
