package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZExpense;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 7/21/2017.
 */

public class ZExpenseAdapter extends MCCPilotLogBaseAdapter {

    private List<ZExpense> mExpense = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public ZExpenseAdapter(Context pContext, List<ZExpense> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExpense = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mExpense.get(pPosition).getExpTypeShort();
    }

    @Override
    public int getCount() {
        return mExpense.size();
    }

    @Override
    public ZExpense getItem(int i) {
        return mExpense.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mExpense.get(i).hashCode();
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

        ZExpense zExpense = getItem(position);
        if (zExpense != null) {
            viewHolder.tvApp.setText(zExpense.getExpTypeShort());
            viewHolder.tvDetail.setText(zExpense.getExpTypeLong());
            //viewHolder.tvPrecision.setText(getContinent(zCountry.getContinent()));
        }

        return convertView;
    }

    public void refreshAdapter(List<ZExpense> expense) {
        this.mExpense = expense;
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
