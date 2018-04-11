package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;

import aero.pilotlog.databases.entities.ZTimeZone;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.Utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TimezoneAdapter extends MCCPilotLogBaseAdapter {

    private List<ZTimeZone> mTimezoneLists = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public TimezoneAdapter(Context pContext, List<ZTimeZone> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTimezoneLists = pList;
    }


    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mTimezoneLists.get(pPosition).getTimeZone();
    }

    @Override
    public int getCount() {
        return mTimezoneLists.size();
    }

    @Override
    public ZTimeZone getItem(int position) {
        return mTimezoneLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTimezoneLists.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_delay, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimezoneAdapter.ViewHolder) convertView.getTag();
        }

        ZTimeZone zTimeZone = getItem(position);
        if (zTimeZone != null) {
            viewHolder.tvApp.setText(zTimeZone.getZoneShort());
            viewHolder.tvDetail.setText(zTimeZone.getTimeZone());

            TimeZone timeZone = TimeZone.getTimeZone(DatabaseManager.getInstance(mContext).getTimeZoneByCode(zTimeZone.getTZCode()).getTimeZone());
            Calendar c = Calendar.getInstance(timeZone);
            String offset;
            double offsetValue = (double) Utils.getTimeOffset(timeZone.getOffset(c.getTimeInMillis()) / 60000, zTimeZone.getTZCode()) / 60;
            if (offsetValue < 0) {
                offset = "" + offsetValue;
            } else {
                offset = "+" + offsetValue;
            }
            if (offset.endsWith(".0")) offset = offset.replace(".0", "");
            //itemTimeZoneOut.setFootNote("UTC " + offset);
            viewHolder.tvPrecision.setText("UTC " + offset);
        }

        return convertView;
    }

    public void refreshAdapter(List<ZTimeZone> timezones) {
        this.mTimezoneLists = timezones;
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
