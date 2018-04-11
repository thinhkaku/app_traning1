package aero.pilotlog.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.models.SettingGeneralModel;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by phuc.dd on 12/30/2016.
 */
public class SettingGeneralAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<SettingGeneralModel> mData = new ArrayList<SettingGeneralModel>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public SettingGeneralAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final SettingGeneralModel item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final SettingGeneralModel item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SettingGeneralModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_setting_general, null);
                    holder.tvSettingGroup = (TextView) convertView.findViewById(R.id.tvSettingGroup);
                    holder.tvSettingName = (TextView) convertView.findViewById(R.id.tvSettingName);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.item_setting_general_section_header, null);
                    holder.tvSettingGroup = (TextView) convertView.findViewById(R.id.tvSectionHeader);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSettingGroup.setText(mData.get(position).getSettingGroup());
        if(!TextUtils.isEmpty(mData.get(position).getSettingName())){
            holder.tvSettingName.setText(mData.get(position).getSettingName());
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvSettingGroup;
        public TextView tvSettingName;
    }

}

