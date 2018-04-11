package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.ZCountry;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by phuc.dd on 03-04-17.
 */

public class CountryAdapter extends MCCPilotLogBaseAdapter {
    private List<ZCountry> mCountryLists = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;

    public CountryAdapter(Context pContext, List<ZCountry> mCountryLists) {
        this.mCountryLists = mCountryLists;
        mContext = pContext;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected int getCountList() {
        return getCount();
    }

    @Override
    protected String getStringItem(int pPosition) {
        return mCountryLists.get(pPosition).getCountryName();
    }

    @Override
    public int getCount() {
        return mCountryLists.size();
    }

    @Override
    public ZCountry getItem(int position) {
        return mCountryLists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return mCountryLists.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_country, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CountryAdapter.ViewHolder) convertView.getTag();
        }
        ZCountry zCountry = getItem(position);
        if (zCountry != null) {
            viewHolder.tvCountryName.setText(zCountry.getCountryName());
            int resId = mContext.getResources().getIdentifier(String.format("flag_%03d", zCountry.getCountryCode()), "drawable", mContext.getPackageName());
            viewHolder.ivFlag.setBackgroundResource(resId);
        }
        //viewHolder.tvPrecision.setVisibility(View.GONE);
        return convertView;
    }

    public void refreshAdapter(List<ZCountry> countries) {
        this.mCountryLists = countries;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tvCountryName)
        TextView tvCountryName;
        @Bind(R.id.ivFlag)
        ImageView ivFlag;

        public ViewHolder(View pView) {
            ButterKnife.bind(this, pView);
        }
    }
}

