package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.IOnClickDutyItemSearch;
import aero.pilotlog.interfaces.ISwipeLayoutDutyCallback;
import aero.pilotlog.models.DutyModels;
import aero.pilotlog.models.FlightModel;
import aero.pilotlog.utilities.DateTimeUtils;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.interfaces.SwipeAdapterInterface;
import aero.pilotlog.widgets.swipelayout.interfaces.SwipeItemMangerInterface;
import aero.pilotlog.widgets.swipelayout.util.Attributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 7/20/2017.
 */

public class DutyExpandAdapter extends BaseExpandableListAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {
    private Context _context;
    private List<DutyModels> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<DutyModels, List<FlightModel>> _listDataChild;
    private DatabaseManager mDbManager;
    private SwipeLayout mSwipeLayout;
    private boolean isEnableItemClick = true;
    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }

    public void setSwipeLayoutCallBack(ISwipeLayoutDutyCallback mSwipeLayoutCallBack) {
        this.mSwipeLayoutCallBack = mSwipeLayoutCallBack;
    }

    private ISwipeLayoutDutyCallback mSwipeLayoutCallBack;

    public void setOnClickDutyItemSearch(IOnClickDutyItemSearch iOnClickDutyItemSearch) {
        this.iOnClickDutyItemSearch = iOnClickDutyItemSearch;
    }

    private IOnClickDutyItemSearch iOnClickDutyItemSearch;

    public DutyExpandAdapter(Context context, List<DutyModels> listDataHeader,
                             HashMap<DutyModels, List<FlightModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_flight_duty, null);
            //convertView = linearLayoutRoot.getChildAt(0);
        }
        mDbManager = new DatabaseManager(_context);
        FlightModel flight;
        flight = (FlightModel) getChild(groupPosition, childPosition);
        if (flight != null) {
            final ViewHolderChild viewHolder = new ViewHolderChild(convertView, groupPosition, childPosition);
            if (flight.isToEdit()) {
                viewHolder.rlFront.setBackgroundColor(_context.getResources().getColor(R.color.bg_incomplete));
                viewHolder.tvLBFlightDate.setTextColor(_context.getResources().getColor(R.color.font_incomplete));
                viewHolder.tvLBFlightNumber.setTextColor(_context.getResources().getColor(R.color.font_incomplete));
            } else if (flight.isNextPage()) {
                viewHolder.rlFront.setBackgroundColor(_context.getResources().getColor(R.color.bg_next_page));
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() == 2) {
                viewHolder.rlFront.setBackgroundColor(_context.getResources().getColor(R.color.bg_simulator));
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() == 3) {
                viewHolder.rlFront.setBackgroundColor(_context.getResources().getColor(R.color.bg_drone));
                viewHolder.tvLBFlightDate.setTextColor(_context.getResources().getColor(R.color.font_drone));
                viewHolder.tvLBFlightNumber.setTextColor(_context.getResources().getColor(R.color.font_drone));
            } else if (flight.getAircraftDeviceCode() == 1) {
                viewHolder.rlFront.setBackgroundColor(Color.WHITE);
                viewHolder.tvLBFlightDate.setTextColor(Color.BLACK);
                viewHolder.tvLBFlightNumber.setTextColor(Color.BLACK);
            } else if (flight.getAircraftDeviceCode() < 0) {
                viewHolder.rlFront.setBackgroundColor(_context.getResources().getColor(R.color.bg_previous));
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

            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(groupPosition));
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
                    }, 100);
                    super.onHandRelease(layout, xvel, yvel);
                }
            });
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      /*  if (_listDataChild == null || _listDataHeader == null || _listDataHeader.get(groupPosition) == null ||
                this._listDataChild.get(this._listDataHeader.get(groupPosition)) == null)
            return 0;*/
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_item_duty, null);
        }
        DutyModels item;
        item = (DutyModels) getGroup(groupPosition);
        if (item != null) {
            final ViewHolderHeader viewHolder = new ViewHolderHeader(convertView, groupPosition);

            String strCurrentDate = item.getDutyDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date();
            try {
                newDate = format.parse(strCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.tvFlightDate.setText(DateTimeUtils.formatDateToString(newDate));
            viewHolder.tvDescription.setText(item.getDescription());
            viewHolder.tvCheckOutIn.setText(item.getDutyTime());
            viewHolder.tvDuration.setText(item.getDuration());
            if (item.isSelected()) {
                if (getChildrenCount(groupPosition) == 0) {
                    viewHolder.lnNoFlight.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.lnNoFlight.setVisibility(View.GONE);
                }
                viewHolder.setIconArrowInfo(R.drawable.ic_info_arrow_up);
            } else {
                viewHolder.lnNoFlight.setVisibility(View.GONE);
                viewHolder.setIconArrowInfo(R.drawable.ic_info_arrow_down);
            }

            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(groupPosition));
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
                    }, 100);
                    super.onHandRelease(layout, xvel, yvel);
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void refreshAdapter(List<DutyModels> _listDataHeader, HashMap<DutyModels, List<FlightModel>> listChildData) {
        this._listDataHeader = _listDataHeader;
        this._listDataChild = listChildData;
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void notifyDatasetChanged() {

    }

    @Override
    public void openItem(int position) {

    }

    @Override
    public void closeItem(int position) {

    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {

    }

    @Override
    public void closeAllItems() {

    }

    @Override
    public void closeAllItems(boolean smooth) {

    }

    @Override
    public List<Integer> getOpenItems() {
        return null;
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return null;
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {

    }

    @Override
    public boolean isOpen(int position) {
        return false;
    }

    @Override
    public Attributes.Mode getMode() {
        return null;
    }

    @Override
    public void setMode(Attributes.Mode mode) {

    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }


    public class ViewHolderHeader {

        @Bind(R.id.tvFlightDate)
        TextView tvFlightDate;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.tvCheckOutIn)
        TextView tvCheckOutIn;
        @Bind(R.id.tvDuration)
        TextView tvDuration;
        @Bind(R.id.rlFront)
        RelativeLayout rlFront;
        @Bind(R.id.lnFlight)
        LinearLayout lnFlight;
        @Bind(R.id.lnNoFlight)
        LinearLayout lnNoFlight;
        @Bind(R.id.iv_arrowInfo)
        ImageView ivArrow;


        private int mIndex;

        public void setIconArrowInfo(int draw) {
            if (ivArrow != null) {
                ivArrow.setImageResource(android.R.color.transparent);
                ivArrow.setBackgroundResource(draw);
            }
        }

        public ViewHolderHeader(View pView, int pPosition) {
            ButterKnife.bind(this, pView);
            mIndex = pPosition;
        }

        @OnClick({R.id.lnFlight, R.id.btnDelete})
        public void onClick(View pView) {
            switch (pView.getId()) {
                case R.id.lnFlight:
                    if (iOnClickDutyItemSearch != null) {
                        refreshAdapter(_listDataHeader, _listDataChild);
                        iOnClickDutyItemSearch.onClickDutyItemSearch(mIndex);
                    }
                    break;
                case R.id.btnDelete:
                    if (mSwipeLayoutCallBack != null) {
                        mSwipeLayoutCallBack.onDeleteRecord(pView, _listDataHeader.get(mIndex), mIndex);
                    }
                    break;
            }
        }
    }

    public class ViewHolderChild {
        @Bind(R.id.tvLBFlightDate)
        TextView tvLBFlightDate;
        @Bind(R.id.tvLBFlightDepArr)
        TextView tvLBFlightDepArr;
        @Bind(R.id.tvLBFlightNumber)
        TextView tvLBFlightNumber;
        @Bind(R.id.tvLBFlightTotalTime)
        TextView tvLBFlightTotalTime;
        @Bind(R.id.rlFront)
        LinearLayout rlFront;
        private int mGroupPosition;
        private int mChildPosition;

        public ViewHolderChild(View pView, int groupPosition, int childPosition) {
            ButterKnife.bind(this, pView);
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @OnClick({R.id.btnDelete})
        public void onClick(View pView) {
            switch (pView.getId()) {
                case R.id.btnDelete:
                    if (mSwipeLayoutCallBack != null) {
                        mSwipeLayoutCallBack.onDeleteChildRecord(pView, _listDataHeader.get(mGroupPosition), (FlightModel) getChild(mGroupPosition, mChildPosition), mChildPosition);
                    }
                    break;
            }
        }
    }
}
