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
import aero.pilotlog.databases.entities.ZCountry;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.interfaces.ISwipeLayoutAirfieldCallback;
import aero.pilotlog.widgets.swipelayout.SimpleSwipeListener;
import aero.pilotlog.widgets.swipelayout.SwipeLayout;
import aero.pilotlog.widgets.swipelayout.adapters.BaseSwipeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by phuc.dd on 3/13/2017.
 */
public class AirfieldsAdapter extends BaseSwipeAdapter {
    private SwipeLayout mSwipeLayout;
    public ISwipeLayoutAirfieldCallback mISwipeLayoutCallback;
    private String mDisplayType;
    private int mSortType;
    private boolean mIsSelectMode = false;
    private int mTypeAdapter = MCCPilotLogConst.AIRFIELD_ADAPTER;
    private final Context mContext;
    private List<Airfield> mModelList;
    private boolean isEnableItemClick = true;
    private static final long DELAY_ENABLE_CLICK = 100;
    private static final int TYPE_MAX_COUNT = 2;
    public boolean isOpenSwipe() {
        return mSwipeLayout.getOpenStatus() == SwipeLayout.Status.Open;
    }
    public void setTypeAdapter(int pType) {
        this.mTypeAdapter = pType;
    }

    public void setDisplayType(String pType) {
        this.mDisplayType = pType;
    }

    public void setSortType(int pType) {
        this.mSortType = pType;
    }

    public void setISwipeLayoutCallback(ISwipeLayoutAirfieldCallback pISwipeLayoutCallback) {
        this.mISwipeLayoutCallback = pISwipeLayoutCallback;
    }

    public AirfieldsAdapter(Context mContext, List<Airfield> pModelList) {
        this.mContext = mContext;
        this.mModelList = pModelList;
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
        Airfield airfield = (Airfield) getItem(position);
        if (airfield != null && airfield.getAFCode() != null) {
            view = inflater.inflate(R.layout.row_item_airfield, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_item_airfield_header, parent, false);
        }
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Airfield airfield;
        airfield = (Airfield) getItem(position);
        final ViewHolder viewHolder = new ViewHolder(convertView, airfield, position);
        if (airfield != null) {
            if (airfield.getAFCode() != null) {
                viewHolder.tvAirfieldCode.setText(getTitleAirfield(airfield));
                viewHolder.tvAirfieldName.setText(airfield.getAFName());
                ZCountry zCountry = DatabaseManager.getInstance(mContext).getCountryByCode(airfield.getAFCountry());
                if (zCountry != null) {
                    viewHolder.tvCountryName.setText(zCountry.getCountryName());
                }
                airfield.getAFCountry();
                viewHolder.rlFront.setBackgroundColor(airfield.isSelected() ? mContext.getResources().getColor(
                        R.color.mcc_cyan_derived)
                        : Color.TRANSPARENT);

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

                //viewHolder.tvHeader.setVisibility(View.GONE);
                // viewHolder.linearLayoutItem.setVisibility(View.VISIBLE);
                //viewHolder.swipeBack.setVisibility(View.VISIBLE);
            } else {
                final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
                swipeLayout.setSwipeEnabled(false);
                viewHolder.tvHeader.setText(airfield.getAFName());
                //viewHolder.swipeBack.setVisibility(View.GONE);
                //viewHolder.linearLayoutItem.setVisibility(View.GONE);
                //viewHolder.tvHeader.setVisibility(View.VISIBLE);
                //viewHolder.tvHeader.setText(airfield.getAFName());
            }
        }
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


    public void removeAllItemSelected() {
        for (int i = 0, count = mModelList.size(); i < count; i++) {
            mModelList.get(i).setSelected(false);
        }
    }

    public void setSelectItem(int position) {
        mModelList.get(position).setSelected(true);
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
        return mModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mModelList.get(position);
    }

    @Override
    protected String getStringItem(int pPosition) {
        Airfield airfield = (Airfield) getItem(pPosition);

        return getTitleAirfield(airfield);
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
     * Set again value of list delay and refresh list view
     *
     * @param pModeList Mode list
     */
    public void refreshAirfieldAdapter(List<Airfield> pModeList) {
        this.mModelList = pModeList;
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

        final Airfield mModel;
        private int mIndex;
        @Nullable
        @Bind(R.id.tvAirfieldCode)
        TextView tvAirfieldCode;
        @Nullable
        @Bind(R.id.tvAirfieldName)
        TextView tvAirfieldName;
        @Nullable
        @Bind(R.id.tvCountryName)
        TextView tvCountryName;
        @Nullable
        @Bind(R.id.rlFront)
        LinearLayout rlFront;
        @Nullable
        @Bind(R.id.tvWXReport)
        TextView tvWXReport;
        @Nullable
        @Bind(R.id.ivWXReport)
        ImageView ivWXReport;
        @Nullable
        @Bind(R.id.tvSectionHeader)
        TextView tvHeader;

        @Nullable

        @OnClick(R.id.btnDelete)
        public void onClickDelete(View pView) {
            if (mISwipeLayoutCallback != null) {
                mISwipeLayoutCallback.onDelete(pView, mModel, mIndex);
            }
        }


        public ViewHolder(View pView, Airfield pModel, int pIndex) {
            ButterKnife.bind(this, pView);
            this.mModel = pModel;
            this.mIndex = pIndex;
        }
    }

    @Override
    public void closeAllItems(boolean smooth) {

    }
}
