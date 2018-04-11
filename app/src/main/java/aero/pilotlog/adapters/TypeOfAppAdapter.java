package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZApproach;
import aero.pilotlog.databases.entities.ZApproachCat;
import aero.pilotlog.databases.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tuan.na on 9/16/2015.
 */
public class TypeOfAppAdapter extends MCCPilotLogBaseAdapter {

    private List<ZApproach> mTypeOfApps = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public TypeOfAppAdapter(Context pContext, List<ZApproach> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTypeOfApps = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mTypeOfApps.get(pPosition).getAPShort();
    }

    @Override
    public int getCount() {
        return mTypeOfApps.size();
    }

    @Override
    public ZApproach getItem(int position) {
        return mTypeOfApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTypeOfApps.get(position).hashCode();
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

        ZApproach zApproach = getItem(position);
        if (zApproach != null) {
            viewHolder.tvApp.setText(zApproach.getAPShort());
            viewHolder.tvDetail.setText(zApproach.getAPLong());
            ZApproachCat zApproachCat = DatabaseManager.getInstance(mContext).getZApproachCat(String.valueOf(zApproach.getAPCat()));
            if (zApproachCat != null) {
                if (zApproachCat.getAPCatLong() != null){
                    viewHolder.tvPrecision.setText(zApproachCat.getAPCatLong());
                }

            }
        }

        return convertView;
    }

    public void refreshAdapter(List<ZApproach> pTypeOfApps) {
        this.mTypeOfApps = pTypeOfApps;
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
