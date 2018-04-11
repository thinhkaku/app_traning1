package aero.pilotlog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZDelay;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.pv on 2015/07/13.
 */
public class DelayTailsAdapter extends MCCPilotLogBaseAdapter {
    private List<ZDelay> mDelays = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private int mTypeAdapter = MCCPilotLogConst.DELAY_ADAPTER;
    private Context mContext;
    private int mViewType = MCCPilotLogConst.LIST_MODE;

    public void setViewType(int mViewType) {
        this.mViewType = mViewType;
    }

    public DelayTailsAdapter(Context pContext, List<ZDelay> pDelays) {
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDelays = pDelays;
        mContext = pContext;
    }

    /**
     * set type adapter is Delay or Tails
     *
     * @param pType = 1: Delay, = 2: Tails
     */
    public void setTypeAdapter(int pType) {
        this.mTypeAdapter = pType;
    }

    @Override
    public int getCount() {
        return  mDelays.size();
    }

    @Override
    public Object getItem(int pPosition) {
        return mDelays.get(pPosition);
    }

    @Override
    public long getItemId(int pPosition) {
        return getItem(pPosition).hashCode();
    }

    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
        ViewHolder viewHolder;
        if (pConvertView == null) {
            pConvertView = mLayoutInflater.inflate(R.layout.row_item_delay, pParent, false);
//            pConvertView = mLayoutInflater.inflate(mTypeAdapter == MCCPilotLogConst.DELAY_ADAPTER ? R.layout.row_item_delay : R.layout.row_item_tail, pParent, false);
            viewHolder = new ViewHolder(pConvertView);
            pConvertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) pConvertView.getTag();
        }

            ZDelay delay = (ZDelay) getItem(pPosition);
            viewHolder.tvNumericCode.setText(delay.getDelayCode() + " (" + delay.getDelayDD() + ")");
            viewHolder.tvDelayName.setText(delay.getDelayShort());
            viewHolder.tvDescription.setText(delay.getDelayLong());
            if (mViewType == MCCPilotLogConst.LIST_MODE) {
                viewHolder.llItemDelay.setBackgroundColor(Color.WHITE);
            } else {
                viewHolder.llItemDelay.setBackgroundResource(R.drawable.list_selector);
            }


        return pConvertView;
    }

    /**
     * from acronym of continent, return full name
     *
     * @param pAcronym
     * @return
     */
    private String getContinent(String pAcronym) {
        switch (pAcronym) {
            case MCCPilotLogConst.CONTINENT_ASIA:
                return mContext.getString(R.string.text_asia);
            case MCCPilotLogConst.CONTINENT_AFRICA:
                return mContext.getString(R.string.text_africa);
            case MCCPilotLogConst.CONTINENT_ANTARCTICA:
                return mContext.getString(R.string.text_antarctica);
            case MCCPilotLogConst.CONTINENT_EUROPE:
                return mContext.getString(R.string.text_europe);
            case MCCPilotLogConst.CONTINENT_MORTH_AMERICA:
                return mContext.getString(R.string.text_north_america);
            case MCCPilotLogConst.CONTINENT_OCEANIA:
                return mContext.getString(R.string.text_oceania);
            case MCCPilotLogConst.CONTINENT_SOUTH_AMERICA:
                return mContext.getString(R.string.text_south_america);
            default:
                return "";
        }
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mDelays.get(pPosition).getDelayDD();
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    /**
     * Set again value of list delay and refresh list view
     *
     * @param pDelays
     */
    public void refreshDelayAdapter(List<ZDelay> pDelays) {
        this.mDelays = pDelays;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @Bind(R.id.tvNumericCode)
        TextView tvNumericCode;
        @Bind(R.id.tvDelayName)
        TextView tvDelayName;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.llItemDelay)
        LinearLayout llItemDelay;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}
