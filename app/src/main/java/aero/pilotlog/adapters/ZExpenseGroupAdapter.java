package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZExpenseGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 7/21/2017.
 */

public class ZExpenseGroupAdapter extends MCCPilotLogBaseAdapter {

    private List<ZExpenseGroup> mExpenseGroups = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public ZExpenseGroupAdapter(Context pContext, List<ZExpenseGroup> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExpenseGroups = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mExpenseGroups.get(pPosition).getExpGroupShort();
    }

    @Override
    public int getCount() {
        return mExpenseGroups.size();
    }

    @Override
    public ZExpenseGroup getItem(int i) {
        return mExpenseGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mExpenseGroups.get(i).hashCode();
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

        ZExpenseGroup zExpenseGroup = getItem(position);
        if (zExpenseGroup != null) {
            viewHolder.tvApp.setText(zExpenseGroup.getExpGroupShort());
            viewHolder.tvDetail.setText(zExpenseGroup.getExpGroupLong());
            //viewHolder.tvPrecision.setText(getContinent(zCountry.getContinent()));
        }

        return convertView;
    }

    public void refreshAdapter(List<ZExpenseGroup> expenseGroups) {
        this.mExpenseGroups = expenseGroups;
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
