package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZFNPT;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 2/27/2017.
 */
public class FNPTAdapter extends MCCPilotLogBaseAdapter {
    private List<ZFNPT> mFNPTs = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public FNPTAdapter(Context pContext, List<ZFNPT> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFNPTs = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mFNPTs.get(pPosition).getFnptShort();
    }

    @Override
    public int getCount() {
        return mFNPTs.size();
    }

    @Override
    public ZFNPT getItem(int position) {
        return mFNPTs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFNPTs.get(position).hashCode();
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

        ZFNPT zfnpt = getItem(position);
        if (zfnpt != null) {
            viewHolder.tvApp.setText(zfnpt.getFnptShort());
            viewHolder.tvDetail.setText(zfnpt.getFnptLong());
        }
        viewHolder.tvPrecision.setVisibility(View.GONE);

        return convertView;
    }

    public void refreshAdapter(List<ZFNPT> pFNPTs) {
        this.mFNPTs = pFNPTs;
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
