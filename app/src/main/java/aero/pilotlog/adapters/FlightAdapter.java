package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ISwipeLayoutFlightCallback;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 4/28/2017.
 */
public class FlightAdapter extends BaseSwipeAdapter {
    private List<FlightModel> mFlightList;
    private Context mContext;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private String mTimeInDecimal;
    private DatabaseManager mDbManager;
    private SwipeLayout mSwipeLayout;

    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }

    public void setSwipeLayoutCallBack(ISwipeLayoutFlightCallback mSwipeLayoutCallBack) {
        this.mSwipeLayoutCallBack = mSwipeLayoutCallBack;
    }

    private ISwipeLayoutFlightCallback mSwipeLayoutCallBack;


    public boolean isEnableClick() {
        return isEnableItemClick;
    }

    public FlightAdapter(Context pContext, List<FlightModel> pFlightList) {
        this.mContext = pContext;
        this.mFlightList = pFlightList;
        DatabaseManager mDatabaseManager = DatabaseManager.getInstance(mContext);
        SettingConfig setting = mDatabaseManager.getSetting(50);
        if (setting != null) {
            mTimeInDecimal = setting.getData();
        }
    }

    @Override
    public int getCount() {
        return mFlightList.size();
    }

    @Override
    public Object getItem(int i) {
        if (mFlightList.size() > 0)
            return mFlightList.get(i);
        else return null;
    }

    @Override
    public long getItemId(int i) {
        return mFlightList.get(i).hashCode();
    }

    @Override
    public void closeAllItems(boolean smooth) {

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    protected String getStringItem(int pPosition) {
        return "";
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }

    public void removeAllItemSelected() {
        for (int i = 0, count = mFlightList.size(); i < count; i++) {
            mFlightList.get(i).setSelected(false);
        }
    }

    public void setSelectItem(int position) {
        mFlightList.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_item_flight, parent, false);

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        mDbManager = new DatabaseManager(mContext);
        FlightModel flight;
        flight = (FlightModel) getItem(position);
        if (flight != null) {
            final ViewHolder viewHolder = new ViewHolder(convertView, flight, position);
            if (flight.isToEdit()) {
                viewHolder.rlFront.setBackgroundColor(mContext.getResources().getColor(R.color.bg_incomplete));
                viewHolder.tvLBFlightDate.setTextColor(mContext.getResources().getColor(R.color.font_incomplete));
                viewHolder.tvLBFlightNumber.setTextColor(mContext.getResources().getColor(R.color.font_incomplete));
            } else if (flight.isNextPage()) {
                viewHolder.rlFront.setBackgroundColor(mContext.getResources().getColor(R.color.bg_next_page));
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() == 2) {
                viewHolder.rlFront.setBackgroundColor(mContext.getResources().getColor(R.color.bg_simulator));
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() == 3) {
                viewHolder.rlFront.setBackgroundColor(mContext.getResources().getColor(R.color.bg_drone));
                viewHolder.tvLBFlightDate.setTextColor(mContext.getResources().getColor(R.color.font_drone));
                viewHolder.tvLBFlightNumber.setTextColor(mContext.getResources().getColor(R.color.font_drone));
            } else if (flight.getAircraftDeviceCode() == 1) {
                viewHolder.rlFront.setBackgroundColor(Color.WHITE);
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() < 0) {
                viewHolder.rlFront.setBackgroundColor(mContext.getResources().getColor(R.color.bg_previous));
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            }

            String strCurrentDate = flight.getFlightDateUTC();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.tvLBFlightDate.setText(DateTimeUtils.formatDateToString(newDate));
            viewHolder.tvLBFlightNumber.setText(flight.getFlightNumber());
            viewHolder.tvLBFlightDepArr.setText(flight.getFlightAirfield());
            viewHolder.tvLBFlightTotalTime.setText(flight.getFlightTime());
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.setSwipeEnabled(true);//In select mode then disable swipe layout

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

    }

    public void refreshLogbookAdapter(List<FlightModel> pFlightList) {
        this.mFlightList = pFlightList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        @Bind(R.id.tvLBFlightDate)
        TextView tvLBFlightDate;
        @Bind(R.id.tvLBFlightDepArr)
        TextView tvLBFlightDepArr;
        @Bind(R.id.tvLBFlightNumber)
        TextView tvLBFlightNumber;
        @Bind(R.id.tvLBFlightTotalTime)
        TextView tvLBFlightTotalTime;
        @Bind(R.id.rlFront)
        RelativeLayout rlFront;
        @Bind(R.id.lnFlight)
        LinearLayout lnFlight;

        private final FlightModel mFlight;
        private int mIndex;


        public ViewHolder(View pView, FlightModel pFlight, int pPosition) {
            ButterKnife.bind(this, pView);
            this.mFlight = pFlight;
            this.mIndex = pPosition;
           /* if (mTypeScreen == MCCPilotLogConst.BUTTON_DUTY) {
                mBtnItemSelect.setVisibility(View.VISIBLE);
            }*/
        }

        @OnClick({R.id.btnDelete})
        public void onClick(View pView) {
            switch (pView.getId()) {
                case R.id.btnDelete:
                    if (mSwipeLayoutCallBack != null) {
                        mSwipeLayoutCallBack.onDeleteRecord(pView, mFlight, mIndex);
                    }
                    break;
            }
        }

    }


}
