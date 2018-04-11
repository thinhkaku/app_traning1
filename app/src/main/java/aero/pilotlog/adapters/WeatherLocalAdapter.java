package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.common.SettingsConst;
import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.WeatherLocal;
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ISwipeLayoutCallback;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherLocalAdapter extends BaseSwipeAdapter {

    private SwipeLayout mSwipeLayout;
    private List<WeatherLocal> mWeatherList;
    private static final int TYPE_MAX_COUNT = 2;
    private final Context mContext;
    public ISwipeLayoutCallback mISwipeLayoutCallback;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private String mDisplayType;
    private int mSortType;
    private boolean mIsSelectMode = false;
    private int mTypeAdapter = MCCPilotLogConst.AIRFIELD_ADAPTER;

    public static final String WX_REPORT_SAVED = "WX report saved "; /* Data with 24-Oct-2014, time is HH:MM*/
    public static final String DATA_SAVED_FORMAT = "%s%s at %s";

    /**
     * set type adapter is Airfield or Weather
     *
     * @param pType = 1: Airfield, = 2: Weather
     */
    public void setTypeAdapter(int pType) {
        this.mTypeAdapter = pType;
    }

    public void setDisplayType(String pType) {
        this.mDisplayType = pType;
    }
    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }
    public void setSortType(int pType) {
        this.mSortType = pType;
    }

    public void setISwipeLayoutCallback(ISwipeLayoutCallback pISwipeLayoutCallback) {
        this.mISwipeLayoutCallback = pISwipeLayoutCallback;
    }

    public WeatherLocalAdapter(Context mContext, List<WeatherLocal> pWeatherList) {
        this.mContext = mContext;
        this.mWeatherList = pWeatherList;
    }

    public boolean isEnableClick() {
        return isEnableItemClick;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(mTypeAdapter == MCCPilotLogConst.AIRFIELD_ADAPTER ? R.layout.row_item_airfield : (MCCPilotLogConst.sIsTablet ? R.layout.row_item_weather_tablet : R.layout.row_item_weather), parent, false);

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        WeatherLocal weather;
            try{
                weather = (WeatherLocal) getItem(position);
                if (weather != null) {
                    final ViewHolder viewHolder = new ViewHolder(convertView, weather, position);
                    viewHolder.tvAirfieldCode.setText(getTitleWeather(weather));


                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(Long.valueOf(weather.getDateSaved()));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    String date = sdf.format(c.getTime());
                    sdf = new SimpleDateFormat("HH:mm", Locale.US);
                    String hour = sdf.format(c.getTime());

                    viewHolder.tvWXReport.setText(!MCCPilotLogConst.sIsTablet ? String.format(DATA_SAVED_FORMAT, WX_REPORT_SAVED, date, hour) : String.format(DATA_SAVED_FORMAT, "", date, hour));
                    if (viewHolder.ivWXReport != null)
                        viewHolder.ivWXReport.setImageResource(mContext.getResources().getIdentifier(weather.getIcon(), "drawable", mContext.getPackageName()));
                    if (!TextUtils.isEmpty(weather.getAFCountry()) && !TextUtils.isEmpty(weather.getAFName())) {
                        viewHolder.tvCountryName.setText(weather.getAFCountry());
                        viewHolder.tvAirfieldName.setText(weather.getAFName());
                    } else {
                        Airfield airfield = DatabaseManager.getInstance(mContext).getAirfieldByICAOIATA(weather.getAFIcao());
                        ZCountry zCountry = DatabaseManager.getInstance(mContext).getZCountry(airfield.getAFCountry());
                        viewHolder.tvCountryName.setText(zCountry.getCountryName());
                        viewHolder.tvAirfieldName.setText(airfield.getAFName());
                    }
                    viewHolder.rlFront.setBackgroundColor(weather.isSelected() ? mContext.getResources().getColor(
                            R.color.mcc_cyan_derived)
                            : Color.TRANSPARENT);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setSwipeEnabled(!mIsSelectMode);//In select mode then disable swipe layout
        swipeLayout.setClickToClose(true);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                mSwipeLayout = layout;
            }

            @Override
            public void onHandRelease(final SwipeLayout layout, float xvel, float yvel) {
                isEnableItemClick = false;
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isEnableItemClick = true;
                    }
                }, DELAY_ENABLE_CLICK);
                super.onHandRelease(layout, xvel, yvel);
            }
        });
    }

    public String getTitleAirfield(Airfield airfield) {
        String strAFIata = airfield.getAFIATA();
        String strAFIcao = airfield.getAFICAO();
        String strResult = "";

        switch (mSortType) {
            case MCCPilotLogConst.SORT_BY_AIRFIELD_NAME:
                if (mDisplayType.equalsIgnoreCase(SettingsConst.SETTING_IATA)) {
                    strResult = (checkAFIataEmpty(strAFIata) ? strAFIata + "-" : "") + strAFIcao;
                } else {
                    strResult = strAFIcao + (checkAFIataEmpty(strAFIata) ? "-" + strAFIata : "");
                }
                break;
            case MCCPilotLogConst.SORT_BY_AIRFIELD_IATA:
                strResult = (checkAFIataEmpty(strAFIata) ? strAFIata + "-" : "") + strAFIcao;
                break;
            case MCCPilotLogConst.SORT_BY_AIRFIELD_ICAO:
                strResult = strAFIcao + (checkAFIataEmpty(strAFIata) ? "-" + strAFIata : "");
                break;
            default:
                break;
        }

        return strResult;
    }

    public String getTitleWeather(WeatherLocal pWeather) {
        String strAFIata = pWeather.getAFIata();
        String strAFIcao = pWeather.getAFIcao();
        return mDisplayType.equalsIgnoreCase(SettingsConst.SETTING_IATA) ? ((checkAFIataEmpty(strAFIata) ? strAFIata + "-" : "") + strAFIcao) : (strAFIcao + (checkAFIataEmpty(strAFIata) ? "-" + strAFIata : ""));
    }

    public void removeAllItemSelected() {

            for (int i = 0, count = mWeatherList.size(); i < count; i++) {
                mWeatherList.get(i).setSelected(false);
            }

    }

    public void setSelectItem(int position) {

            mWeatherList.get(position).setSelected(true);

        notifyDataSetChanged();
    }

    public void setSelectMode(boolean pFlag) {
        mIsSelectMode = pFlag;
    }

    private boolean checkAFIataEmpty(String pText) {
        return !TextUtils.isEmpty(pText) && !"null".equalsIgnoreCase(pText);
    }

    @Override
    public int getCount() {
        return mWeatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return  mWeatherList.get(position);
    }

    @Override
    protected String getStringItem(int pPosition) {
        if (mTypeAdapter == MCCPilotLogConst.WEATHER_ADAPTER) {
            WeatherLocal weather = (WeatherLocal) getItem(pPosition);
            return getTitleWeather(weather);
        }
        return "";
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }


    /**
     * Set again value of list Weather and refresh list view
     *
     * @param pWeatherList Weather list
     */
    public void refreshWeatherAdapter(List<WeatherLocal> pWeatherList) {
        this.mWeatherList = pWeatherList;
        notifyDataSetChanged();
    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public class ViewHolder {

        private final WeatherLocal mWeather;
        private int mIndex;

        @Bind(R.id.tvAirfieldCode)
        TextView tvAirfieldCode;
        @Bind(R.id.tvAirfieldName)
        TextView tvAirfieldName;
        @Nullable
        @Bind(R.id.tvCountryName)
        TextView tvCountryName;
        @Bind(R.id.rlFront)
        LinearLayout rlFront;
        @Nullable
        @Bind(R.id.tvWXReport)
        TextView tvWXReport;
        @Nullable
        @Bind(R.id.ivWXReport)
        ImageView ivWXReport;

        @OnClick(R.id.btnDelete)
        public void onClickDelete(View pView) {
            if (mISwipeLayoutCallback != null) {
                mISwipeLayoutCallback.onDelete(pView, mWeather, mIndex);
            }
        }

        public ViewHolder(View pView, @Nullable WeatherLocal pWeather, int pIndex) {
            ButterKnife.bind(this, pView);
            this.mIndex = pIndex;
            this.mWeather = pWeather;
        }
    }

    @Override
    public void closeAllItems(boolean smooth) {

    }
}
