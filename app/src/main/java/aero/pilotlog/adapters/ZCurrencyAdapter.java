package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZCurrency;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 8/1/2017.
 */

public class ZCurrencyAdapter extends MCCPilotLogBaseAdapter {
    private List<ZCurrency> mCurrencies = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public ZCurrencyAdapter(Context pContext, List<ZCurrency> pList) {
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCurrencies = pList;
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mCurrencies.get(pPosition).getCurrShort();
    }

    @Override
    public int getCount() {
        return mCurrencies.size();
    }

    @Override
    public ZCurrency getItem(int i) {
        return mCurrencies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mCurrencies.get(i).hashCode();
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

        ZCurrency zCurrency = getItem(position);
        if (zCurrency != null) {
            viewHolder.tvApp.setText(zCurrency.getCurrShort());
            viewHolder.tvDetail.setText(zCurrency.getCurrLong());
        }

        return convertView;
    }

    public void refreshAdapter(List<ZCurrency> currencies) {
        this.mCurrencies = currencies;
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
