package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.Aircraft;
import aero.pilotlog.interfaces.ISwipeLayoutAircraftCallback;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ngoc.dh on 8/17/2015.
 * Aircraft adapter
 */
public abstract class AircraftListAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<Aircraft> mAircraftList = new ArrayList<>();
    //private List<FlightPilot> mFlightPilots;
    //v5node
    private aero.pilotlog.databases.manager.DatabaseManager mDatabaseManager5;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private boolean mIsSelectMode = false;
    private SwipeLayout mSwipeLayout;
    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }
    public void setISwipeLayoutCallback(ISwipeLayoutAircraftCallback mISwipeLayoutCallback) {
        this.mISwipeLayoutCallback = mISwipeLayoutCallback;
    }

    public ISwipeLayoutAircraftCallback mISwipeLayoutCallback;

    /**
     * Constructor
     *
     * @param pContext      Context
     * @param pAircraftList Aircraft list
     */
    public AircraftListAdapter(Context pContext, List<Aircraft> pAircraftList) {
        this.mContext = pContext;
        this.mAircraftList = pAircraftList;
        mDatabaseManager5 = aero.pilotlog.databases.manager.DatabaseManager.getInstance(pContext);
        //mFlightPilots = populateFlightPilot();
    }

    public boolean isEnableClick() {
        return isEnableItemClick;
    }

    public void setSelectMode(boolean pFlag) {
        mIsSelectMode = pFlag;
    }

    public void closeSwipe(boolean pSmooth) {
        if (mSwipeLayout != null) {
            mSwipeLayout.close(pSmooth);
            mSwipeLayout = null;
        }
    }

    public boolean getSwipeLayoutShown() {
        return mSwipeLayout != null && mSwipeLayout.isShown();
    }

    @Override
    public int getCount() {
        return mAircraftList.size();
    }

    @Override
    public Aircraft getItem(int position) {
        return mAircraftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        Aircraft aircraft;
        aircraft = (Aircraft) getItem(position);
        ViewHolder holder = new ViewHolder(convertView, aircraft, position);
        TextView t = (TextView) convertView.findViewById(R.id.position);
        t.setText((position + 1) + ".");

        Aircraft mAircraft = getItem(position);
        holder.mTvAircraftRef.setText(TextUtils.isEmpty(mAircraft.getReference()) ? MCCPilotLogConst.STRING_EMPTY : mAircraft.getReference().trim());
        String aircraftModel, aircraftVariant;
        if (TextUtils.isEmpty(mAircraft.getModel())) {
            aircraftModel = MCCPilotLogConst.STRING_EMPTY;
        } else {
            aircraftModel = mAircraft.getModel().trim();
        }
        /*Add variant and hyphen if necessary*/
        if (TextUtils.isEmpty(mAircraft.getSubModel())) {
            aircraftVariant = MCCPilotLogConst.STRING_EMPTY;
            holder.mTvHyphen.setVisibility(View.GONE);
            holder.mTvAircraftVariant.setVisibility(View.GONE);
        } else {
            holder.mTvHyphen.setVisibility(View.VISIBLE);
            holder.mTvAircraftVariant.setVisibility(View.VISIBLE);
            aircraftVariant = mAircraft.getSubModel().trim();
        }

        holder.mTvAircraftModel.setText(aircraftModel);
        holder.mTvAircraftVariant.setText(aircraftVariant);
        holder.mTvAircraftCompany.setText(TextUtils.isEmpty(mAircraft.getCompany()) ? MCCPilotLogConst.STRING_EMPTY : mAircraft.getCompany().trim());
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setSwipeEnabled(!mIsSelectMode); //In select mode then disable swipe layout
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

        /*Handle delete button*/
       /* holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlightPilots != null) {
                    deleteAction(mFlightPilots, position);
                }
            }
        });*/
        holder.rlFront.setBackgroundColor(mAircraft.isSelected() ? mContext.getResources().getColor(
                R.color.mcc_cyan_derived)
                : Color.TRANSPARENT);
    }

    @Override
    protected String getStringItem(int pPosition) {
        return getItem(pPosition).getReference();
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_item_aircraft, parent, false);

        return view;
    }

    @Override
    public void closeAllItems(boolean smooth) {

    }

    public void removeAllItemSelected() {
        for (int i = 0, count = mAircraftList.size(); i < count; i++) {
            mAircraftList.get(i).setSelected(false);
        }
    }

    public void setSelectItem(int position) {
        mAircraftList.get(position).setSelected(true);
        notifyDataSetChanged();
    }

    /**
     * Delete an item
     *
     * @param flightPilots: flight pilot list
     * @param position:     item's position
     */
    // public abstract void deleteAction(List<FlightPilot> flightPilots, int position);

    /**
     * Populate flight pilot list
     *
     * @return: flight pilot list
     */
    /*private List<FlightPilot> populateFlightPilot() {
        List<FlightPilot> flightPilots;
        flightPilots = mDatabaseManager.getAllFlightPilots();
        return flightPilots;
    }*/

    /**
     * Refresh aircraft list
     *
     * @param pAircrafts: aircraft list
     */
    public void refreshAircraftListAdapter(List<Aircraft> pAircrafts) {
        this.mAircraftList = pAircrafts;
        notifyDataSetChanged();
    }

    /**
     * Create view holder
     */
    public class ViewHolder {
        final Aircraft mModel;
        private int mIndex;
        @Bind(R.id.tvReference)
        TextView mTvAircraftRef;
        @Bind(R.id.tvModel)
        TextView mTvAircraftModel;
        @Bind(R.id.tvVariant)
        TextView mTvAircraftVariant;
        @Bind(R.id.tvHyphen)
        TextView mTvHyphen;
        @Bind(R.id.tvCompany)
        TextView mTvAircraftCompany;
        @Bind(R.id.btnDelete)
        Button mBtnDelete;
        @Bind(R.id.rlFront)
        RelativeLayout rlFront;

        @OnClick({R.id.btnDelete, R.id.btnDeActive})
        public void onClickDelete(View pView) {
            switch (pView.getId()) {
                case R.id.btnDelete:
                    if (mISwipeLayoutCallback != null) {
                        mISwipeLayoutCallback.onDelete(pView, mModel, mIndex);
                    }
                    break;
                case R.id.btnDeActive:
                    if (mISwipeLayoutCallback != null) {
                        mISwipeLayoutCallback.onDeActivate(pView, mModel, mIndex);
                    }
                    break;
            }

        }
      /*  @OnClick(R.id.btnDelete)
        public void onClickDelete(View pView) {
            if (mISwipeLayoutCallback != null) {
                mISwipeLayoutCallback.onDelete(pView, mModel, mIndex);
            }
        }*/

        public ViewHolder(View view, Aircraft aircraft, int index) {
            ButterKnife.bind(this, view);
            this.mModel = aircraft;
            this.mIndex = index;
        }
    }
}

