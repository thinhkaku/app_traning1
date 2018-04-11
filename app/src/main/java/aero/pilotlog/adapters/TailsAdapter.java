package aero.pilotlog.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.common.MCCPilotLogConst;
import aero.pilotlog.databases.entities.ZCountry;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 2/27/2017.
 */
public class TailsAdapter extends MCCPilotLogBaseAdapter {
    private List<ZCountry> mZCountries = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public TailsAdapter(Context pContext, List<ZCountry> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mZCountries = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mZCountries.get(pPosition).getRegAC().replace(MCCPilotLogConst.SPLIT_KEY_APPEND,"");
    }

    @Override
    public int getCount() {
        return mZCountries.size();
    }

    /* @Override
     public boolean isEnabled(int position) {
         return false;
     }*/
    @Override
    public ZCountry getItem(int position) {
        return mZCountries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mZCountries.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_delay, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ZCountry zCountry = getItem(position);
        if (zCountry != null) {
            viewHolder.tvApp.setText(getRegAC(zCountry.getRegAC()));
            viewHolder.tvDetail.setText(zCountry.getCountryName());
            viewHolder.tvPrecision.setText(getContinent(zCountry.getContinent()));
        }

        return convertView;
    }

    private String getRegAC(String oldRegAC) {
        String newRegAC = "";
        if (!TextUtils.isEmpty(oldRegAC)) {
            String[] arrayRegAC = oldRegAC.split(MCCPilotLogConst.SPLIT_KEY);
            if (arrayRegAC != null) {
                for (int i = 0; i < arrayRegAC.length; i++) {
                    newRegAC += arrayRegAC[i]+ MCCPilotLogConst.SPACE;
                }
            }
        }
        return newRegAC;
    }

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

    public void refreshAdapter(List<ZCountry> pCountries) {
        this.mZCountries = pCountries;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tvNumericCode)
        TextView tvApp;
        @Bind(R.id.tvDelayName)
        TextView tvDetail;
        @Bind(R.id.tvDescription)
        TextView tvPrecision;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}
